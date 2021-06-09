if (typeof window.cmsAnalytics !== 'object') {
  window.cmsAnalytics = (function(){
    var cmsAnalytics={};
    cmsAnalytics.sessionIdName='cmsAnalyticsSessionId';
    function guid() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
            return v.toString(16);
        });
    }
    function setCookie(cname,cvalue,exdays) {
      var d = new Date();
      d.setTime(d.getTime()+(exdays*24*60*60*1000));
      var expires = "expires="+d.toGMTString();
      document.cookie = cname + "=" + encodeURIComponent(cvalue) + "; " + expires;
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
      script.src = url;
      head.insertBefore(script, head.firstChild);
      if(callback){
        document.addEventListener ? script.addEventListener("load", callback, false) : script.onreadystatechange = function() {
          if (/loaded|complete/.test(script.readyState)) {
            script.onreadystatechange = null
            callback()
          }
        }
      }
    }
    function getReferrer() {
      return window.document.referrer || document.referrer;
    }

    function getLang(){
      return navigator.language || navigator.browserLanguage || navigator.systemLanguage || navigator.userLanguage || "";
    }
    if(getCookie(cmsAnalytics.sessionIdName) ==""){
      setCookie(cmsAnalytics.sessionIdName,guid() ,30);
    }
    function getParams(){
      var params=[];
      params.push('sessionId='+encodeURIComponent(getCookie(cmsAnalytics.sessionIdName)));
      params.push('lang='+encodeURIComponent(getLang()));
      params.push('screenw='+window.screen.width || 0);
      params.push('screenh='+window.screen.height || 0);
      params.push('referer='+encodeURIComponent(getReferrer()));
      params.push('url='+encodeURIComponent(document.location.href));
      params.push('title='+encodeURIComponent(document.title));
      return params;
    }
    cmsAnalytics.report=function(url){
      cmsAnalytics.url=url;
      url = url+( /\?/.test( url ) ? "&" : "?" ) + getParams().join('&');
      loadScript(url);
    }
    return cmsAnalytics;
  });
}