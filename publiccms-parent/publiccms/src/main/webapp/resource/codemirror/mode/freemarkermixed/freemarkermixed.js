// Distributed under an MIT license: http://codemirror.net/LICENSE

/**
 * @file freemarkermixed.js
 */

(function(mod) {
	if (typeof exports == "object" && typeof module == "object" ) // CommonJS
		mod(require("../../lib/codemirror"), require("../htmlmixed/htmlmixed"), require("../freemarker/freemarker"));
	else if (typeof define == "function" && define.amd ) // AMD
		define([ "../../lib/codemirror", "../htmlmixed/htmlmixed", "../freemarker/freemarker" ], mod);
	else
		// Plain browser env
		mod(CodeMirror);
})
		(function(CodeMirror) {
			"use strict";

			CodeMirror
					.defineMode("freemarkermixed", function(config) {
						var htmlMixedMode = CodeMirror.getMode(config, "htmlmixed");
						var freemarkerMode = CodeMirror.getMode(config, "freemarker");

						var settings = {
							leftDelimiter : '<', rightDelimiter : '>',	tagSyntax : 1
						// 1 angle_bracket,2 square_bracket
						};

						if (config.hasOwnProperty("tagSyntax") ) {
							if (config.tagSyntax === 2 ) {
								settings.tagSyntax = 2;
								settings.leftDelimiter = '[';
								settings.rightDelimiter = ']';
							}
						}
						
						var freemarkerFlagArray = ["#","/#","@","/@"];
						
						function regEsc(str) {
							return str.replace(/[^\s\w]/g, "\\$&");
						}
						
						var regLeftArray=[],htmlHasLeftDelimeterRegArray=[];
						
						for (var i = 0; i < freemarkerFlagArray.length; i++) {
							regLeftArray.push( new RegExp(".*" + regEsc(settings.leftDelimiter + freemarkerFlagArray[i])));
							htmlHasLeftDelimeterRegArray.push( new RegExp("[^<>]*" + regEsc(settings.leftDelimiter + freemarkerFlagArray[i])));
						}
						
						var helpers = {
							chain : function(stream, state, parser) {
								state.tokenize = parser;
								return parser(stream, state);
							},

							cleanChain : function(stream, state, parser) {
								state.tokenize = null;
								state.localState = null;
								state.localMode = null;
								return (typeof parser == "string") ? (parser ? parser : null) : parser(stream, state);
							},

							maybeBackup : function(stream, pat, style) {
								var cur = stream.current();
								var close = cur.search(pat) , m;
								if (close > -1 ) {
									stream.backUp(cur.length - close);
								} else if (m = cur.match(/<\/?$/) ) {
									stream.backUp(cur.length);
									if (!stream.match(pat, false) ) {
										stream.match(cur[0]);
									}
								}
								return style;
							}
						};

						var parsers = {
							html : function(stream, state) {
								if (!state.noparse) {
									var htmlTagName = state.htmlMixedState.htmlState.context && state.htmlMixedState.htmlState.context.tagName
							        ? state.htmlMixedState.htmlState.context.tagName
							        : null;
									for (var i = 0; i < freemarkerFlagArray.length; i++) {
										if ( (stream.match(htmlHasLeftDelimeterRegArray[i], false) && htmlTagName === null ) || stream.match(settings.leftDelimiter + freemarkerFlagArray[i], false)) {
									        state.tokenize = parsers.freemarker;
									        state.localMode = freemarkerMode;
									        state.localState = freemarkerMode.startState(htmlMixedMode.indent(state.htmlMixedState, "", ""));
									        return helpers.maybeBackup(stream, settings.leftDelimiter + freemarkerFlagArray[i], freemarkerMode.token(stream, state.localState));
									    } else if (stream.match("${", false)) {
									    	state.tokenize = parsers.freemarker;
									        state.localMode = freemarkerMode;
									        state.localState = freemarkerMode.startState(htmlMixedMode.indent(state.htmlMixedState, "", ""));
									        return helpers.maybeBackup(stream, "${", freemarkerMode.token(stream, state.localState));
									    }
									}
								}
								return htmlMixedMode.token(stream, state.htmlMixedState);
							},

							freemarker : function(stream, state) {
								if (stream.match(settings.leftDelimiter + "#--", false) ) {
									return helpers.chain(stream, state, parsers.inBlock("comment", "--"
												+ settings.rightDelimiter));
								} else if (stream.match(settings.rightDelimiter, false) ) {
									stream.eat(settings.rightDelimiter);
									state.tokenize = parsers.html;
									state.localMode = htmlMixedMode;
									state.localState = state.htmlMixedState;
									return "tag";
								} else if (stream.match("}", false) ) {
									stream.eat("}");
									state.tokenize = parsers.html;
									state.localMode = htmlMixedMode;
									state.localState = state.htmlMixedState;
									return "keyword";
								}

								return helpers.maybeBackup(stream, settings.rightDelimiter, freemarkerMode
										.token(stream, state.localState));
							},

							inBlock : function(style, terminator) {
								return function(stream, state) {
									while (!stream.eol()) {
										if (stream.match(terminator) ) {
											helpers.cleanChain(stream, state, "");
											break;
										}
										stream.next();
									}
									return style;
								};
							}
						};

						return {
							startState : function() {
								var state = htmlMixedMode.startState();
								return {
									token : parsers.html, localMode : null, localState : null, htmlMixedState : state,
									tokenize : null, noparse : false
								};
							},

							copyState : function(state) {
								var local = null , tok = (state.tokenize || state.token);
								if (state.localState ) {
									local = CodeMirror
											.copyState((tok != parsers.html ? freemarkerMode : htmlMixedMode), state.localState);
								}
								return {
									token : state.token, tokenize : state.tokenize, localMode : state.localMode,
									localState : local,
									htmlMixedState : CodeMirror.copyState(htmlMixedMode, state.htmlMixedState),
									noparse : state.noparse
								};
							},

							token : function(stream, state) {
								if (stream.match(settings.leftDelimiter+"#", false) ) {
									if (!state.noparse && stream.match("#noparse", true)) {
							          state.noparse = true;
							          return "keyword";
							        } else if (state.noparse && stream.match("/#noparse", true)) {
							          state.noparse = false;
							          return "keyword";
							        }
								}
								if (state.noparse && state.localState != state.htmlMixedState ) {
									state.tokenize = parsers.html;
									state.localMode = htmlMixedMode;
									state.localState = state.htmlMixedState;
								}
								var style = (state.tokenize || state.token)(stream, state);
								return style;
							},

							indent : function(state, textAfter) {
								if (state.localMode == freemarkerMode || (state.noparse && !state.localMode) ) {
									for (var i = 0; i < regLeftArray.length; i++) {
										if(regLeftArray[i].test(textAfter)){
											return CodeMirror.Pass;
										}
									}
								}
								return htmlMixedMode.indent(state.htmlMixedState, textAfter, "");
							},

							innerMode : function(state) {
								return {
									state : state.localState || state.htmlMixedState,
									mode : state.localMode || htmlMixedMode
								};
							}
						};
					}, "htmlmixed", "freemarker");

			CodeMirror.defineMIME("text/freemarker", "freemarkermixed");
			// vim: et ts=2 sts=2 sw=2

		});
