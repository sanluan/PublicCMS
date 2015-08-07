	<a href="${getSitePath()}">首页</a> &gt;
<#macro echoParent parentId>
	<#if parentId?has_content>
		<@_category id=parentId>
			<#if object.parentId?has_content>
				<@echoParent object.parentId!/>
			<#else>
				<script>
					$("header nav ul li a:contains('${object.name}')").parent().addClass('selected');
				</script>
			</#if>
			<a href="${getSitePath(object.url!)}">${object.name!}</a>  &gt;
		</@_category>
	</#if>
</#macro>