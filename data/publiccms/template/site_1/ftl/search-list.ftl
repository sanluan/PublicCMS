<dl>
	<dt>
		<h3><a href="<#if a.onlyUrl>content/redirect.do?id=${a.id}<#else>${a.url}</#if>" target="_blank">${a.title}</a><span>${a.publishDate?date}</span></h3>
	</dt>
	<dd>
		<a href="<#if a.onlyUrl>content/redirect.do?id=${a.id}<#else>${a.url}</#if>" target="_blank">
<#if a.cover?has_content>
			<img src="<@_thumb path=a.cover width=144 height=192/>" alt="${a.title}"/>
</#if>
<#if a.hasImages>
		<@_contentFileList contentId=a.id image=true count=2>
			<#list page.list as i>
				<img src="<@_thumb path=i.filePath width=144 height=192/>" alt="${a.title}"/>
			</#list>
		</@_contentFileList>
</#if>
		</a>
		<p class="clearfix-before"><@t.cut a.description!'' 100 '...'/><a href="<#if a.onlyUrl>content/redirect.do?id=${a.id}<#else>${a.url}</#if>" target="_blank"> 详细 &gt;&gt;</a></p>
	</dd>
</dl>