<li class="hoverShadow clearfix-after" data-diy-item="${a.id}">		
		<#if a.hasImages>			
			<div class="article-title">
				<h3><a href="<#if a.onlyUrl>${site.dynamicPath}content/redirect?id=${a.id}<#else>${a.url!}</#if>">${a.title?no_esc}</a></h3>
				<div class="clearfix-after">			
			<@cms.contentFileList contentId=a.id fileTypes='image' count=4>
				<#list page.list as i>
					<a href="<#if a.onlyUrl>${site.dynamicPath}content/redirect?id=${a.id}<#else>${a.url!}</#if>"><figure class="image-list"><img src="<@tools.thumb path=i.filePath width=160 height=120/>" alt="${a.title}"/></figure></a>
				</#list>
			</@cms.contentFileList>
				</div>
				<p class="info">
					<span><a href="${a.url}#comments">${a.comments}评论</a></span>
					<span><a href="${site.dynamicPath}comment.html?contentId=${a.id}">${a.scores}赞</a></span>
					<span>${a.publishDate?date}</span>
				</p>
			</div>
		<#else>
			<#if a.cover?has_content>
				<a href="<#if a.onlyUrl>${site.dynamicPath}content/redirect?id=${a.id}<#else>${a.url!}</#if>"><figure><img src="<@tools.thumb path=a.cover width=160 height=120/>" alt="${a.title}"/></figure></a>
			</#if>
			<div class="article-title">
				<h3><a href="<#if a.onlyUrl>${site.dynamicPath}content/redirect?id=${a.id}<#else>${a.url!}</#if>">${a.title?no_esc}</a></h3>
				<p>${(a.description?no_esc)!}</p>
				<p><@cms.tag ids=a.tagIds><#list map?keys as k><a href="?tagId=${k}">${map[k].name}</a><#sep> </#list></@cms.tag></p>
				<p class="info">
					<span><a href="${a.url}#comments">${a.comments}评论</a></span>
					<span><a href="${site.dynamicPath}comment.html?contentId=${a.id}">${a.scores}赞</a></span>
					<span>${a.publishDate?date}</span>
				</p>
			</div>
		</#if>		
	</li>
