	<a href="${site.sitePath}">首页</a>
<#macro echoBread parentId>
	<#if parentId?has_content>
		<@_category id=parentId>
			<#if object.parentId?has_content>
				<@echoBread object.parentId!/>
				  &gt; <a href="${object.url!}">${object.name!}</a>
			<#else>
				  &gt; <a href="${object.url!}">${object.name!}</a>
			</#if>
		</@_category>
	</#if>
</#macro>