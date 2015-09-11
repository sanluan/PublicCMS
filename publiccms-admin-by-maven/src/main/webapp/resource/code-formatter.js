window.TAB = "  ";
window.QuoteKeys = true;
function checkArray(obj) {
	return obj && typeof obj === 'object' && typeof obj.length === 'number'
			&& !(obj.propertyIsEnumerable('length'));
}

function process($jsonObj,$canvasObj) {
	var json = $jsonObj.val();
	try {
		if (json == "")
			json = "\"\"";
		var obj = eval("[" + json + "]");
		$canvasObj.html("<pre class='CodeContainer'>" + processObject(obj[0], 0, false, false, false)
				+ "</pre>");
	} catch (e) {
		alert("JSON数据格式不正确:\n" + e.message);
		$canvasObj.html("");
	}
}
window._dateObj = new Date();
window._regexpObj = new RegExp();
function processObject(obj, indent, addComma, isArray, isPropertyContent) {
	var html = "";
	var comma = (addComma) ? "<span class='Comma'>,</span> " : "";
	var type = typeof obj;
	if (checkArray(obj)) {
		if (obj.length == 0) {
			html += getRow(indent, "<span class='ArrayBrace'>[ ]</span>"
					+ comma, isPropertyContent);
		} else {
			html += getRow(indent, "<span class='ArrayBrace'>[</span>",
					isPropertyContent);
			for ( var i = 0; i < obj.length; i++) {
				html += processObject(obj[i], indent + 1, i < (obj.length - 1),
						true, false);
			}
			html += getRow(indent, "<span class='ArrayBrace'>]</span>" + comma);
		}
	} else if (type == 'object') {
		if (obj == null) {
			html += formatLiteral("null", "", comma, indent, isArray, "Null");
		} else if (obj.constructor == window._dateObj.constructor) {
			html += formatLiteral("new Date(" + obj.getTime() + ") /*"
					+ obj.toLocaleString() + "*/", "", comma, indent, isArray,
					"Date");
		} else if (obj.constructor == window._regexpObj.constructor) {
			html += formatLiteral("new RegExp(" + obj + ")", "", comma, indent,
					isArray, "RegExp");
		} else {
			var numProps = 0;
			for ( var prop in obj)
				numProps++;
			if (numProps == 0) {
				html += getRow(indent, "<span class='ObjectBrace'>{ }</span>"
						+ comma, isPropertyContent);
			} else {
				html += getRow(indent, "<span class='ObjectBrace'>{</span>",
						isPropertyContent);
				var j = 0;
				for ( var prop in obj) {
					var quote = window.QuoteKeys ? "\"" : "";
					html += getRow(indent + 1, "<span class='PropertyName'>"
							+ quote
							+ prop
							+ quote
							+ "</span>: "
							+ processObject(obj[prop], indent + 1,
									++j < numProps, false, true));
				}
				html += getRow(indent, "<span class='ObjectBrace'>}</span>"
						+ comma);
			}
		}
	} else if (type == 'number') {
		html += formatLiteral(obj, "", comma, indent, isArray, "Number");
	} else if (type == 'boolean') {
		html += formatLiteral(obj, "", comma, indent, isArray, "Boolean");
	} else if (type == 'function') {
		if (obj.constructor == window._regexpObj.constructor) {
			html += formatLiteral("new RegExp(" + obj + ")", "", comma, indent,
					isArray, "RegExp");
		} else {
			obj = formatFunction(indent, obj);
			html += formatLiteral(obj, "", comma, indent, isArray, "Function");
		}
	} else if (type == 'undefined') {
		html += formatLiteral("undefined", "", comma, indent, isArray, "Null");
	} else {
		html += formatLiteral(obj.toString().split("\\").join("\\\\")
				.split('"').join('\\"'), "\"", comma, indent, isArray, "String");
	}
	return html;

}

function formatLiteral(literal, quote, comma, indent, isArray, style) {
	if (typeof literal == 'string')
		literal = literal.split("<").join("&lt;").split(">").join("&gt;");
	var str = "<span class='" + style + "'>" + quote + literal + quote + comma
			+ "</span>";
	if (isArray)
		str = getRow(indent, str);
	return str;

}

function formatFunction(indent, obj) {
	var tabs = "";
	for ( var i = 0; i < indent; i++)
		tabs += window.TAB;
	var funcStrArray = obj.toString().split("\n");
	var str = "";
	for ( var i = 0; i < funcStrArray.length; i++) {
		str += ((i == 0) ? "" : tabs) + funcStrArray[i] + "\n";
	}
	return str;
}

function getRow(indent, data, isPropertyContent) {
	var tabs = "";
	for ( var i = 0; i < indent && !isPropertyContent; i++)
		tabs += window.TAB;
	if (data != null && data.length > 0 && data.charAt(data.length - 1) != "\n")
		data = data + "\n";
	return tabs + data;
}
