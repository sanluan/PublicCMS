<dl>
	<dt>
		<h3><a href="${getSitePath(a.url)}" target="_blank">${a.title}</a><span>${a.publishDate?date}</span></h3>
	</dt>
	<dd>
		<#assign breakIndex=1/>
		<#if a.cover?has_content>
			<#assign breakIndex=0/>
			<a href="${getSitePath(a.url)}" target="_blank"><div class="img" style="background-image:url(${getUploadPath(getThumb(a.cover,144,192))});margin-right:2%"></div></a>
		</#if>
		<#if (getContentAttr(a.id).text)?has_content>
			<#list getContentAttr(a.id).text?eval as i>
				<a href="${getSitePath(a.url)}#p=${i?counter}" target="_blank"><div class="img" style="background-image:url(${getUploadPath(getThumb(i.image,144,192))});margin-right:2%"></div></a>
				<#if i?index gt breakIndex><#break/></#if>
			</#list>
		</#if>
		<p class="clearfix-before"><@cut a.description 100 '...'/><a href="${getSitePath(a.url)}" target="_blank"> 详细 &gt;&gt;</a></p>
	</dd>
</dl>