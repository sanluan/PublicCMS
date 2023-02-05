var cmsSupportVersion = "1.0";
// cms analytics
if (typeof window.cmsAnalytics !== 'object') {
    if (!String.prototype.endsWith) {
        String.prototype.endsWith = function(search, this_len) {
            if (this_len === undefined || this_len > this.length) {
                this_len = this.length;
            }
            return this.substring(this_len - search.length, this_len) === search;
        };
    }
    window.cmsAnalytics = (function(){
        var cmsAnalytics={};
        cmsAnalytics.sessionIdName='PUBLICCMS_ANALYTICS_ID';
        function guid() {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
                return v.toString(16);
            });
        }
        function setCookie(cname,cvalue, exdays, path) {
            var d = new Date();
            d.setTime(d.getTime()+(exdays*24*60*60*1000));
            var suffix="expires="+d.toGMTString();
            if(cmsAnalytics.domain && location.hostname.endsWith(cmsAnalytics.domain) ){
                suffix+= "; domain=" + cmsAnalytics.domain;
            }
            if(cmsAnalytics.path && 0 == cmsAnalytics.path.indexOf("/") && cmsAnalytics.path.endsWith("/") ){
                suffix+= "; path=" + cmsAnalytics.path;
            }
            document.cookie = cname + "=" + encodeURIComponent(cvalue) + "; " + suffix;
        }
        function getCookie(cname) {
            var name = cname + "=";
            var ca = document.cookie.split(';');
            for(var i=0; i<ca.length; i++) {
                var c = ca[i].trim();
                if (c.indexOf(name)==0) return c.substring(name.length,c.length);
            }
            return "";
        }
        function loadScript(url,callback) {
            var head = document.head || document.getElementsByTagName("head")[0] || document.documentElement;
            var script = document.createElement("script");
            script.async = true;
            script.type = "text/javascript";
            url = url+( /\?/.test( url ) ? "&" : "?" )+ "_=" +(new Date()).getTime();
            if(callback){
                if (script.readyState) {
                    script.onreadystatechange = function() {
                        if (script.readyState == "loaded" || script.readyState == "complete"){
                            script.onreadystatechange = null
                            callback()
                        }
                    }
                } else {
                    script.onload = function(){
                        callback();
                    };
                }
            }
            script.src = url;
            head.appendChild(script);
        }
        function getReferrer() {
            return window.document.referrer || document.referrer;
        }

        function getLang(){
            return navigator.language || navigator.browserLanguage || navigator.systemLanguage || navigator.userLanguage || "";
        }
        function getParams(action){
            var params=[];
            params.push('sessionId='+encodeURIComponent(getCookie(cmsAnalytics.sessionIdName)));
            params.push('lang='+encodeURIComponent(getLang()));
            params.push('screenw='+window.screen.width || 0);
            params.push('screenh='+window.screen.height || 0);
            params.push('referer='+encodeURIComponent(getReferrer()));
            params.push('url='+encodeURIComponent(document.location.href));
            params.push('title='+encodeURIComponent(document.title));
            if(action){
                params.push('action='+action);
            }else{
                params.push('action=visit');
            }
            return params;
        }
        cmsAnalytics.setDomain=function(domain){
            cmsAnalytics.domain=domain;
        }
        cmsAnalytics.setPath=function(path){
            cmsAnalytics.path=path;
        }
        cmsAnalytics.report=function(url){
            if(getCookie(cmsAnalytics.sessionIdName) ==""){
                setCookie(cmsAnalytics.sessionIdName,guid() ,30);
            }
            cmsAnalytics.url=url;
            url = url+( /\?/.test( url ) ? "&" : "?" ) + getParams().join('&');
            loadScript(url);
        }
        cmsAnalytics.action=function(action){
            var url = cmsAnalytics.url;
            url = url+( /\?/.test( url ) ? "&" : "?" ) + getParams(action).join('&');
            loadScript(url);
        }
        return cmsAnalytics;
    });
}
// cms diy
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
        var forms=document.getElementsByTagName("form");
        for (var i=0; i<forms.length; i++) {
            if("get" == forms[i].method||"GET" == forms[i].method){
                var input = document.createElement("input");
                input.type="hidden";
                input.name="diy";
                forms[i].appendChild(input);
            }
        }
        var itemType,itemId;
        if("string" === typeof itemString ) {
            var parameters = itemString.split("&");
            for (var i=0; i < parameters.length; i++) {
                var pair = parameters[i].split("=");
                if("itemType" === pair[0] && pair[1] ) {
                    itemType=pair[1];
                } else if ("itemId" === pair[0] && pair[1] ) {
                    itemId=pair[1];
                }
            }
        }
        var scrollEle;
        function scroll(){
            if(scrollEle ) {
                var offset = scrollEle.getBoundingClientRect();
                window.parent.postMessage({diyevent:'scroll',x:offset.x,y:offset.y,width:offset.width,height:offset.height},"*");
            }
        }
        window.parent.postMessage({url:location.href,diyevent:'load',templatePath:templatePath,itemType:itemType,itemId:itemId,version:cmsSupportVersion},"*");
        var diyElements = document.querySelectorAll('[data-diy]');
        for (var i=0; i < diyElements.length; i++) {
            diyElements[i].onmouseenter = function(){
                scrollEle = this;
                var offset = this.getBoundingClientRect();
                window.parent.postMessage({url:location.href,diyevent:'enter',itemType:this.getAttribute("data-diy"),itemId:this.getAttribute("data-diy-id"),x:offset.x,y:offset.y,width:offset.width,height:offset.height,noborder:this.getAttribute("data-diy-norborder")},"*");
                window.addEventListener("scroll", scroll);
            };
            diyElements[i].onmouseleave = function(){
                scrollEle=null;
                window.parent.postMessage({url:location.href,diyevent:'leave'},"*");
                window.removeEventListener("scroll", scroll);
            };
            var items = diyElements[i].querySelectorAll('[data-diy-item]');
            for (var j=0; j < items.length; j++) {
                items[j].onmouseenter = function(){
                    var offset = this.getBoundingClientRect();
                    window.parent.postMessage({diyevent:'hoverItem',itemId:this.getAttribute("data-diy-item"),x:offset.x,y:offset.y,width:offset.width,height:offset.height},"*");
                };
                items[j].onmouseleave = function(){
                    window.parent.postMessage({diyevent:'leaveItem'},"*");
                };
            }
        }
    }
}