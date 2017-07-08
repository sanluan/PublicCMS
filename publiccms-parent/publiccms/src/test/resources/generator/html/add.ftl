${"<@_"+entityName?replace('Cms','')?uncap_first+" id=id><#assign a=object/></@_"+entityName?replace('Cms','')?uncap_first+">"}
<form method="post" action="${entityName?uncap_first}/save.do?callbackType=closeCurrent&navTabId=${entityName?uncap_first}/list" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
	<input name="id" type="hidden" value="<#noparse>${id!}</#noparse>" />
	<div class="formBar">
		<ul>
			<li><button type="submit" class="buttonActive">保存</button></li>
			<li><button type="button" class="button close">取消</button></li>
		</ul>
	</div>
	<div class="pageContent">
		<div class="pageFormContent" layoutH="56">
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
	</div>
</form>