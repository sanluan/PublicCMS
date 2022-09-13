/**
 * jQuery ajax history plugins
 * @author 张慧华 z@j-ui.com
 */

(function($){

    $.extend({

        History: {
            _hash: new Array(),
            _currentHash: "",
            _callback: undefined,
            init: function(callback){
                $.History._callback = callback;
                var current_hash = location.hash.replace(/\?.*$/, '');
                $.History._currentHash = current_hash;

                if (!$.support.leadingWhitespace) {
                    if ($.History._currentHash == '') {
                        $.History._currentHash = '#';
                    }
                    $("body").append('<iframe id="jQuery_history" style="display: none;" src="about:blank"></iframe>');
                    var ihistory = $("#jQuery_history")[0];
                    var iframe = ihistory.contentDocument || ihistory.contentWindow.document;
                    iframe.open();
                    iframe.close();
                    iframe.location.hash = current_hash;
                }
                if ("function" === typeof this._callback)
                    $.History._callback(current_hash.skipChar("#"));
                setInterval($.History._historyCheck, 100);
            },
            _historyCheck: function(){
                var current_hash = "";
                if (!$.support.leadingWhitespace) {
                    var ihistory = $("#jQuery_history")[0];
                    var iframe = ihistory.contentWindow;
                    current_hash = iframe.location.hash.skipChar("#").replace(/\?.*$/, '');
                } else {
                    current_hash = location.hash.skipChar('#').replace(/\?.*$/, '');
                }
                if (current_hash != $.History._currentHash) {
                    $.History._currentHash = current_hash;
                    $.History.loadHistory(current_hash);
                }

            },
            addHistory: function(hash, fun, args){
                $.History._currentHash = hash;
                var history = [hash, fun, args];
                $.History._hash.push(history);
                if (!$.support.leadingWhitespace) {
                    var ihistory = $("#jQuery_history")[0];
                    var iframe = ihistory.contentDocument || ihistory.contentWindow.document;
                    iframe.open();
                    iframe.close();
                    iframe.location.hash = hash.replace(/\?.*$/, '');
                    location.hash = hash.replace(/\?.*$/, '');
                } else {
                    location.hash = hash.replace(/\?.*$/, '');
                }
            },
            loadHistory: function(hash){
                if (!$.support.leadingWhitespace) {
                    location.hash = hash;
                }
                for (var i = 0; i < $.History._hash.length; i += 1) {
                    if ($.History._hash[i][0] == hash) {
                        $.History._hash[i][1]($.History._hash[i][2]);
                        return;
                    }
                }
            }
        }
    });
})(jQuery);