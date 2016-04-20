<div class="col-md-3">
	<a href="<#if a.onlyUrl>${site.dynamicPath}content/redirect.do?id=${a.id}<#else>${a.url!}</#if>">
<#if a.cover?has_content>
		<img src="<@_thumb path=a.cover width=100 height=100/>" alt="${a.title}"/>
</#if>
		<h3>${a.title}</h3>
		<p><@t.cut a.description!'' 100 '...'/></p>
	</a>
</div>