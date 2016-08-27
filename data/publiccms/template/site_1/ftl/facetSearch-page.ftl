<#if page??>
	<div class="page">
		<#if !page.firstPage>
			<a href="facet.html?word=${(word?url)!}&page=${page.prePage}&categoryId=${(categoryId?url)!}&modelId=${(modelId?url)!}">上一页</a>
		<#else>
			<span>上一页</span>
		</#if>
		<#assign start=1/>
		<#if (page.pageIndex-5) gt start>
			<#assign start=page.pageIndex-4/>
		</#if>
		<#assign end=page.totalPage/>
		<#if (page.pageIndex+5) lt end>
			<#assign end=page.pageIndex+4/>
		</#if>
		<#if start gt 1>
			<a href="?word=${(word?url)!}&page=1&categoryId=${(categoryId?url)!}&modelId=${(modelId?url)!}">1</a> ...
		</#if>
		<#list start..end as n><a href="?word=${(word?url)!}&page=${n}&categoryId=${(categoryId?url)!}&modelId=${(modelId?url)!}"<#if n=page.pageIndex> class="selected"</#if>>${n}</a></#list>
		<#if end lt page.totalPage>
			... <a href="?word=${(word?url)!}&page=${page.totalPage}&categoryId=${(categoryId?url)!}&modelId=${(modelId?url)!}">${page.totalPage}</a>
		</#if>
		<#if !page.lastPage>
			<a href="?word=${(word?url)!}&page=${page.nextPage}&categoryId=${(categoryId?url)!}&modelId=${(modelId?url)!}">下一页</a>
		<#else>
			<span>下一页</span>
		</#if>
	</div>
</#if>