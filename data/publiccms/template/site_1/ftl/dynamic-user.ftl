<#if user?has_content>
<script>
	$('.tools .user').hide();
	$('.tools .user-logout .nickname').text('${user.nickName}');
	$('.tools .user-logout').show();
	<#if user.superuserAccess>$('.tools .user-logout .master').show();</#if>
</script>
</#if>