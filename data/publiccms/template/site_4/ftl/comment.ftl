<div id="comments" data-diy="comment">
	<h6 class="mb-1 pb-2"><a href="${site.dynamicPath}comment.html?contentId=${content.id}">${content.comments}条评论</a></h6>
	<@import path="/ftl/tools.ftl" namespace="t"/><#-- 工具 -->
	<@cms.commentList contentId=content.id emptyReply=true pageSize=10>
	<ul class="comments-list px-0 py-4">
		<#list page.list as a>
		<@t.merge 'userIds' a.userId!/>
		<@t.merge 'userIds' a.replyUserId!/>
		</#list>
		<@sys.user ids=t.userIds!><#assign userMap=map!/></@sys.user>
		<#list page.list as a>
		<li class="comment">
			<div class="d-flex align-items-center">
				<figure class="rounded-circle">
					<img alt="${userMap[a.userId?string].nickname}" src="${userMap[a.userId?string].cover!(site.sitePath+'assets/img/logo-graphic.png')}">
				</figure>
				<div>
					<a href="${site.dynamicPath}user.html?id=${a.userId}" class="fw-bold me-2">${userMap[a.userId?string].nickname}</a><span><#if userMap[a.userId?string].superuser>[管理员]</#if></span>
					<div class="text-secondary text-opacity-50 fs-8">${a.createDate?string('yyyy-MM-dd HH:mm')}</div>
				</div>
				<a href="${site.dynamicPath}comment.html?contentId=${content.id!}&replyId=${a.id}" class="reply d-inline-flex align-items-center ms-auto fs-8 text-muted"><img src="${site.sitePath}assets/img/icon-comment-reply.svg" alt="reply"> 回复<#if a.replies gt 0> (${a.replies})</#if></a>
			</div>
			<p class="mt-2">
				${a.text!}
			</p>
			<#if a.replies gt 0>
			<@cms.commentList contentId=content.id replyId=a.id orderType='asc' pageSize=10>
			<ul class="child">
				<#list page.list as a>
				<@t.merge 'userIds' a.userId!/>
				<@t.merge 'userIds' a.replyUserId!/>
				</#list>
				<@sys.user ids=t.userIds!><#assign userMap=map!/></@sys.user>
				<#list page.list as a>
				<li class="comment">
					<div class="d-flex align-items-center">
						<figure>
							<img class="rounded-circle" alt="${userMap[a.userId?string].nickname}" src="${userMap[a.userId?string].cover!(site.sitePath+'assets/img/logo-graphic.png')}">
						</figure>
						<div>
							<a href="${site.dynamicPath}user.html?id=${a.userId}" class=" fw-bold  me-2">${userMap[a.userId?string].nickname}</a><span class="me-2"><#if userMap[a.userId?string].superuser>[管理员]</#if></span> <span class="text-muted">回复</span><a href="${site.dynamicPath}user.html?id=${a.userId}" class=" fw-bold">${userMap[a.replyUserId?string].nickname}</a>
							<div class="text-secondary text-opacity-50 fs-8">${a.createDate?string('yyyy-MM-dd HH:mm')}</div>
						</div>
						<a href="${site.dynamicPath}comment.html?contentId=${content.id!}&replyId=${a.id}" class="reply d-inline-flex align-items-center ms-auto fs-8 text-muted"><img src="${site.sitePath}assets/img/icon-comment-reply.svg" alt="reply"> 回复</a>
					</div>
					<p class="mt-2">
						${a.text!}
					</p>
				</li>
				</#list>
			</ul>
		</@cms.commentList>
		</#if>
		</li>
		</#list>
	</ol>
	</@cms.commentList>
	<h6 class="mb-1 pb-2">发表评论</h6>
	<div class="user-login">
		<div class="form-control text-center" style="line-height:5;"><a href="${site.dynamicPath}login.html">登录后发布评论</a></div>
	</div>
	<div class="user-logout comment-text-box">
		<form method="post" action="${site.dynamicPath}comment/save" onsubmit="return comment();">
			<input type="hidden" name="replyId" value=""/>
			<input type="hidden" name="_csrf" value=""/>
			<input type="hidden" name="contentId" value="${content.id}"/>
			<input name="returnUrl" type="hidden" value="${site.dynamicPath}comment.html?contentId=${content.id!}" />
			<textarea name="text" class="form-control" rows="3" maxlength="1000"></textarea>
			<button type="submit" class="btn btn-primary mt-3">提交</button>
		</form>
	</div>
</div>