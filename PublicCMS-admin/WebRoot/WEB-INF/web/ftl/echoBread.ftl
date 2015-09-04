	<a href="${getCmsPath()}">首页</a> &gt;
<#macro echoBread parentId>
	<#if parentId?has_content>
		<@_category id=parentId>
			<#if object.parentId?has_content>
				<@echoBread object.parentId!/>
				<a href="${getCmsPath('list.html')}?id=${object.id}">${object.name!}</a>	
			<#else>
				<a href="${getCmsPath('category.html')}?id=${object.id}">${object.name!}</a>  &gt;
				<script>
					$("header nav ul li a:contains('${object.name}')").parent().addClass('selected');
				</script>
			</#if>
		</@_category>
	</#if>
</#macro>