<#if texts?has_content>
	<div class="page">
		<#if '1'!=pageIndex>
			<a href="?pageIndex=${(pageIndex)-1}">上一页</a>
		<#else>
			<span>上一页</span>
		</#if>
		<#list 1..texts?size as i><a href="?pageIndex=${i}"<#if i=pageIndex> class="selected"</#if>>${i}</a></#list>
		<#if texts?size=pageIndex>
			<a href="?pageIndex=${texts?size}">下一页</a>
		<#else>
			<span>下一页</span>
		</#if>
	</div>
</#if>