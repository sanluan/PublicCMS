${"<@_"+entityName?replace('Cms','')?uncap_first+" id=id><#assign a=object/></@_"+entityName?replace('Cms','')?uncap_first+">"}
<div class="pageContent">
	<form method="post" action="${entityName?uncap_first}/save.do?callbackType=closeCurrent&navTabId=${entityName?uncap_first}" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
		<input name="id" type="hidden" value="<#noparse>${id!}</#noparse>" />
		<div class="pageFormContent" layoutH="57">
		<#list columnList as a>
			<#if "Date"=a.type>
			<dl>
				<dt>${a.title}：</dt>
				<dd>
					<input class="required date" name="${a.name}" type="text" size="20" dateFmt="yyyy-MM-dd HH:mm:ss" value="${r"${(a."+a.name+")!}"}"/>
					<a class="inputDateButton" href="javascript:;">选择</a>
				</dd>
			</dl>
			<#else>
			<dl>
				<dt>${a.title}：</dt>
				<dd><input class="required" name="${a.name}" type="text" size="30" value="${r"${(a."+a.name+")!}"}"/></dd>
			</dl>
			</#if>
		</#list>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li><div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
			</ul>
		</div>
	</form>
</div>