var userId;
function comment(){
	if (CMS_PATH) {
		var csrf;
		$.ajax({url:CMS_PATH+"member/getcsrf.html?userId="+userId,async:false,success:function(data){
			csrf=data.csrf;
		}});
		if(csrf){
			$('.comment-text-box input[name=_csrf]').val(csrf);
			return true;
		}
	}
	return false;
}
$(function(){
	// 登陆状态显示
	if (CMS_PATH) {
		$.ajaxSetup({xhrFields: {withCredentials: true}});
		$.getJSON(CMS_PATH+'loginStatus', function(data){
			if(data.id){
				userId=data.id;
				$('.user-login').hide();
				$('.nickname').text(data.nickname);
				$('.user-logout').show();
				$('.user-login').hide();
				if(data.superuser&&true==data.superuser){
					$('.user-logout .master').show();
				}
			}else{
				$('.user-login').show();
				$('.user-logout').hide();
			}
		});
	}
	// 登陆链接增加返回地址
	if(0>window.location.href.indexOf('returnUrl')){
		$('a.user-login,.user-login a').each(function(){
			$(this).prop('href',$(this).prop('href')+'?returnUrl='+encodeURIComponent(window.location.href));
		});
	}
	if($('.breadcrumbs a[data-id]').length){
		$('.navbar-nav .nav-item[data-id='+$('.breadcrumbs a[data-id]:eq(0)').data('id')+']').addClass('selected');
	}
	$('[data-bs-target="#searchModal"]').click(function(){
		$('#searchModal input[name=word]').focus();
	});
});