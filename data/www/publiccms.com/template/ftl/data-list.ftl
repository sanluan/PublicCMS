<dl>
	<dt>
		<h3><a href="${getSitePath(a.url)}" target="_blank">${a.title}</a><span>${a.publishDate?date}</span></h3>
	</dt>
	<dd>
		<#if a.cover?has_content>
			<a href="${getSitePath(a.url)}" target="_blank"><div class="img" style="background-image:url(${getUploadPath(getThumb(a.cover,144,192))})"></div></a>
		</#if>
		<div class="tags">
			<#if a.tags?has_content>标签：
				<@_tag ids=a.tags>
					<#list map?keys as key>
						<a href="${getCmsPath()}tags.html?id=${map[key].id}" target="_blank"><em>${map[key].name}</em></a>
					</#list>
				</@_tag>
			</#if>
		</div>
		<p><@cut a.description 100 '...'/><a href="${getSitePath(a.url)}" target="_blank"> 详细 &gt;&gt;</a> </p>
	</dd>
</dl>