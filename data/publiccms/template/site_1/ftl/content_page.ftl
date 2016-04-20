<#if page??>
	<div class="page">
		<#if !page.firstPage>
			<a href="${getPage(url,page.prePage)}">上一页</a>
		<#else>
			<span>上一页</span>
		</#if>
		<#list 1..page.totalCount as a><a href="${getPage(url,a)}"<#if a=page.pageIndex> class="selected"</#if>>${a}</a></#list>
		<#if !page.lastPage>
			<a href="${getPage(url,page.nextPage)}">下一页</a>
		<#else>
			<span>下一页</span>
		</#if>
	</div>
</#if>