$(function(){
	// 手机页面导航效果
	$('.nav-icon').click(function(){$('header nav>ul').fadeToggle();return false;});
	var headerHeight=$('header').height();
	$(window).resize(function(){
		$('header nav>ul').removeAttr('style');
	});
	// 头部动态效果
	$(window).scroll(function(){
		if(headerHeight-$(window).scrollTop()>0){
			$('header').removeClass('fixed-position');
			$('main').removeClass('no-header');
		} else {
			$('header').addClass('fixed-position');
			$('main').addClass('no-header');
		}
	});
	$('.wechat').click(function(){
		$('.dialog').show();
	});
	$('.dialog .box').click(function(){
		$('.dialog').hide();
	});
	$('.dialog .background').click(function(){
		$('.dialog').hide();
	});
	// 登陆状态显示
	if (CMS_PATH) {
		$.ajaxSetup({xhrFields: {withCredentials: true}});
		$.getJSON(CMS_PATH+'loginStatus', function(data){
			if(data.id){
				$('.user-login').hide();
				$('.user-logout .nickname').text(data.nickname);
				$('.user-logout').show();
				if(data.superuserAccess&&true==data.superuserAccess){
					$('.user-logout .master').show();
				}
			}
		});
	}
	// 登陆链接增加返回地址
	if(0>window.location.href.indexOf('returnUrl')){
		$('.user-login a,.user-logout a:eq(2)').each(function(){
			$(this).prop('href',$(this).prop('href')+'?returnUrl='+encodeURIComponent(window.location.href));
		});		
	}
	// 首页焦点图
	var swiper_index = new Swiper('#index-focus', {
		loop: true,
		lazy: true,
		pagination: {
			el: '#index-focus .swiper-pagination',
			clickable: true,
		},
		slidesPerView: 2,
      	spaceBetween: 10,
		breakpoints: {
			768: {
				slidesPerView: 1,
				spaceBetween: 0,
			}
		},
		autoplay: {
			delay: 5000,
			disableOnInteraction: false,
		}
	});
	// 右侧图片推荐
	var swiper_right = new Swiper('#right-images', {
		loop: true,
		lazy: true,
		slidesPerView: 1,
		pagination: {
			el: '#right-images .swiper-pagination',
			clickable: true,
		},
		breakpoints: {
			1230: {
				slidesPerView: 3,
				spaceBetween: 10,
			},
			840: {
				slidesPerView: 2,
				spaceBetween: 10,
			},
			420: {
				slidesPerView: 1,
				spaceBetween: 0,
			}
		},
		autoplay: {
			delay: 3000,
			disableOnInteraction: false,
		}
	});
});