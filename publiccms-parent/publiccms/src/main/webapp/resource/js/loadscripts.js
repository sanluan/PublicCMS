function loadScripts(urls, callback) {
    function loadScript(url, callback){
        var head = document.head || document.getElementsByTagName("head")[0] || document.documentElement;
        var script = document.createElement("script")
        script.type = "text/javascript";
        if (script.readyState){
            script.onreadystatechange = function(){
                if (script.readyState == "loaded" || script.readyState == "complete"){
                    script.onreadystatechange = null;
                    callback();
                }
            };
        } else {
            script.onload = function(){
                callback();
            };
        }
        script.src = url;
        head.appendChild(script);
    }
    var i = 0,count = urls.length-1;
    var loadOrCallback=function(){
        if (i == count) {
            callback && callback();
        }else{
            loadScript(urls[++i],loadOrCallback);
        }
    };
    loadScript(urls[i],loadOrCallback);
}