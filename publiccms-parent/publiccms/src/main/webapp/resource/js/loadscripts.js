function loadScripts(urls, callback,base) {
    function loadScript(url, callback){
        var head = document.head || document.getElementsByTagName("head")[0] || document.documentElement;
        if(-1 < url.indexOf(".css")){
            var link = document.createElement("link");
            link.href = url;
            link.rel = "stylesheet";
            link.media = "screen";
            head.prepend(link);
            callback();
        }else{
            var script = document.createElement("script")
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
    }
    var i = 0,count = urls.length-1;
    var callbacks = [];
    if(callback){
        callbacks.push(callback);
    }
    var loadOrCallback=function(){
        if (i == count && 0 < callbacks.length) {
            for(var j=0; j<callbacks.length; j++){
                callbacks[j]();
            }
        } else {
            if(base){
                loadScript(base+urls[++i], loadOrCallback);
            }else{
                loadScript(urls[++i], loadOrCallback);
            }
        }
    };
    if(base){
        loadScript(base+urls[i], loadOrCallback);
    }else{
        loadScript(urls[i], loadOrCallback);
    }
}