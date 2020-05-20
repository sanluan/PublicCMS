<@_commentList contentId=content.id  pageSize=10>
	<div class="comment-box">
		<p><a href="${site.dynamicPath}comment.html?contentId=${content.id}">评论</a>：<span class="float-right"><a href="${site.dynamicPath}login.html?returnUrl=${url?url}" class="nickname"></a></span></p>
		<div class="comment-text-box user-login">
			<div class="cover"><a href="${site.dynamicPath}login.html">登录后发布评论</a></div>
		</div>
		<div class="comment-text-box user-logout">
			<form method="post" action="${site.dynamicPath}comment/save.do" onsubmit="return comment();">
				<input type="hidden" name="replyId" value=""/>
				<input type="hidden" name="_csrf" value=""/>
				<input type="hidden" name="contentId" value="${content.id}"/>
				<input name="returnUrl" type="hidden" value="${site.dynamicPath}comment.html?contentId=${content.id!}" />
				<textarea name="text" maxlength="1000"></textarea>
				<input type="submit" value="提交"/>
			</form>
		</div>
		<#if page.totalCount gt 0>
			<#list page.list as a>
				<@t.merge 'userIds' a.userId!/>
				<@t.merge 'userIds' a.replyUserId!/>
			</#list>
			<@_sysUser ids=t.userIds!><#assign userMap=map!/></@_sysUser>
			<ul>
				<#list page.list as a>
				<li class="underline">
					<p><a href="${site.dynamicPath}user.html?id=${a.userId}" target="_blank">${userMap[a.userId?string].nickName}</a><#if userMap[a.userId?string].superuserAccess>[管理员]</#if> ${a.createDate?string('yyyy-MM-dd hh:mm')}<span class="float-right"><a href="${site.dynamicPath}comment.html?contentId=${content.id!}&replyId=${a.id}">回复</a></span></p>
					<#if a.replyId?has_content>
						<@_comment id=a.replyId>
							<p class="reply"><a href="${site.dynamicPath}user.html?id=${a.replyUserId}" target="_blank">@${userMap[a.replyUserId?string].nickName}</a> : ${object.text!}</p>
						</@_comment>
					</#if>
					<p>${a.text!}</p>
				</li>
				</#list>
			</ul>
		</#if>
		<#if page.totalPage gt 1>
			<div class="page"><a href="${site.dynamicPath}comment.html?contentId=${content.id}&pageIndex=2">查看更多评论</a></div>
		</#if>
	</div>
</@_commentList>