<#if page??>
	<div class="page">
		<#if !page.firstPage>
			<a href="${getSitePath(page.list[page.pageIndex-2])}">上一页</a>
		<#else>
			<span>上一页</span>
		</#if>
		<#list page.list as url><a href="${getSitePath(url)}"<#if url?counter=page.pageIndex> class="selected"</#if>>${url?counter}</a></#list>
		<#if !page.lastPage>
			<a href="${getSitePath(page.list[page.pageIndex])}">下一页</a>
		<#else>
			<span>下一页</span>
		</#if>
	</div>
</#if>