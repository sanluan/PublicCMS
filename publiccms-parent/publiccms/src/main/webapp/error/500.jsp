<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" trimDirectiveWhitespaces="true"%>
<%  
String basePath = "//" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";  
%>
<%!String exceptionMsgForInner(Throwable e) {
	String errorMessage = e.getLocalizedMessage();
	if (null == errorMessage){
	    errorMessage = "";
	}
	errorMessage += "\r\n";
	if (null != e.getCause()) {
	    errorMessage += exceptionMsgForInner(e.getCause());
	} else {
	    for (StackTraceElement stackTraceElement:e.getStackTrace()) {
	        errorMessage += stackTraceElement.toString() + "\r\n";
		}
	}
	return errorMessage.trim();
}%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<title>500 Error</title>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
</head>
<script language="JavaScript">
<!--
	function dokeydown() {
		var obj = document.getElementById("divexception");
		if ("none" == obj.style.display.toLowerCase()) {
			obj.style.display = "block";
		} else {
			obj.style.display = "none";
		}
		return false;
	}
//-->
</script>
</head>
<body>
	<div style="margin:0 auto;width:500px;">
		<div style="background-color:#f0f0f0;" onclick="dokeydown()">服务器错误,Server Error</div>
		<div>系统发生错误,请返回重试或查看其他内容!<br />An error has occurred. Please go back and try again or check out the other content!<br /><a href="<%=basePath%>">返回首页</a><br /><a href="<%=basePath%>">Return home</a></div>
		<div style="display: none;" id=divexception>
			<textarea rows="40" style="width:100%">
<%
	Throwable throwable;
	try{
	    throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
	}catch(Exception e){
	    throwable = e;
	}
	out.println(exceptionMsgForInner(throwable));
%>
			</textarea>
		<div>
	</div>
</body>
</html>
