if(window.parent!=window && "string" === typeof templatePath ){
    function checkDiy() {
        var parameters = window.location.search.substring(1).split("&");
        for (var i=parameters.length-1;0<=i;i--) {
            var pair = parameters[i].split("=");
            if("diy" === pair[0] ) {
                return true;
            }
        }
        return false;
    }
    if(checkDiy() ) {
        var links=document.getElementsByTagName("a");
        for (var i=0; i<links.length; i++) {
            if("_blank" === links[i].target){
                links[i].target="";
            }
            if(-1 === links[i].href.indexOf("javascript") && -1 === links[i].href.indexOf("diy") ) {
                links[i].href += (-1 === links[i].href.indexOf("?") )?"?diy":"&diy";
            }
        }
        var itemType,itemId;
        if("string" === typeof itemString ) {
            var parameters = itemString.split("&");
            for (var i=0; i<parameters.length; i++) {
                var pair = parameters[i].split("=");
                if("itemType" === pair[0] && pair[1] ) {
                    itemType=pair[1];
                } else if ("itemId" === pair[0] && pair[1] ) {
                    itemId=pair[1];
                }
            }
        }
        window.parent.postMessage({url:location.href,templatePath:templatePath,itemType:itemType,itemId:itemId},"*");
    }
}