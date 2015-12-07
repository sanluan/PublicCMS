<div class="col-md-3">
	<a href="<#if model.isUrl>${getCmsPath('content/redirect.do')}?id=${a.id}<#else>${getSitePath(a.url)}</#if>">
<#if a.cover?has_content>
		<img src="${getUploadPath(getThumb(a.cover,400,300))}" alt="${a.title}"/>
</#if>
		<h3>${a.title}</h3>
		<p><@cut a.description!'' 100 '...'/></p>
		<h4>￥${a.modelExtend1!}</h4>
	</a>
</div>