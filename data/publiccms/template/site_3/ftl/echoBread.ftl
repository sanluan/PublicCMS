	<a href="${site.sitePath}">首页</a> &gt;
<#macro echoBread parentId>
	<#if parentId?has_content>
		<@_category id=parentId absoluteURL=true>
			<#if object.parentId?has_content>
				<@echoBread object.parentId!/>
				<a href="${object.url!}">${object.name!}</a>
			<#else>
				<a href="${object.url!}">${object.name!}</a>  &gt;
			</#if>
		</@_category>
	</#if>
</#macro>