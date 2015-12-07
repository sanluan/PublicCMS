<dl>
	<dt>
		<h3><a href="<#if model.isUrl>content/redirect.do<#else>content.html</#if>?id=${a.id}" target="_blank">${a.title}</a><span>${a.publishDate?date}</span></h3>
	</dt>
	<dd>
		<a href="<#if model.isUrl>content/redirect.do<#else>content.html</#if>?id=${a.id}" target="_blank">
<#if a.cover?has_content>
			<img src="${getUploadPath(getThumb(a.cover,144,192))}" alt="${a.title}"/>
</#if>
<#if model.isImages>
		<#if (getContentAttr(a.id).text)?has_content>
			<#list getContentAttr(a.id).text?eval[0..1] as i>
				<img src="${getUploadPath(getThumb(i.image,144,192))}" alt="${a.title}"/>
			</#list>
		</#if>
</#if>
		</a>
		<p class="clearfix-before"><@t.cut a.description!'' 100 '...'/><a href="<#if model.isUrl>content/redirect.do<#else>content.html</#if>?id=${a.id}" target="_blank"> 详细 &gt;&gt;</a></p>
	</dd>
</dl>