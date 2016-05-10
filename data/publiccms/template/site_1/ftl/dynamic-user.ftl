<#if user?has_content>
<script>
	$.cookie('CMS_USER','${user.id}##${user.nickName}##${user.superuserAccess?string}##${user.emailChecked?string}',{expires: 24000});
</script>
</#if>