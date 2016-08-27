<script>
	<#if user?has_content>
		$.cookie('CMS_USER','${user.id}##${user.nickName}##${user.superuserAccess?string}##${user.emailChecked?string}',{expires: 24000});
	<#else>
		$.cookie('CMS_USER',null);
	</#if>
</script>