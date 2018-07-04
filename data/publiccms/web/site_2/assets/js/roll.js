$(function() {
		//配置变量
		var config = {
			showNum : 11,	//设置滚动的显示个数
			autoScroll : true,	//是否自动滚动，默认为 false
			autoScrollInterval : 3000	//自动滚动间隔，默认为 3000 毫秒，autoScroll = true 时才有效
		}
		
		var scrollUlWidth = $('.wrapper ul li').outerWidth(true),	//单个 li 的宽度
			scrollUlLeft = 0,	//.scroll_ul 初使化时的 left 值为 0
			prevAllow = true,	//为了防止连续点击上一页按钮
			nextAllow = true;	//为了防止连续点击下一页按钮
			
		//计算父容量限宽
		$('.wrapper').width(config.showNum * scrollUlWidth);

		//点击上一页
		$('#prev').mouseover(function() {
			if (prevAllow) {
				prevAllow = false;
				scrollUlLeft = scrollUlLeft - scrollUlWidth;
				$('.wrapper ul').css('left', scrollUlLeft);
				//复制最后一个 li 并插入到 ul 的最前面
				$('.wrapper ul li:last').clone().prependTo('.wrapper ul');
				//删除最后一个 li
				$('.wrapper ul li:last').remove();
				$('.wrapper ul').animate({
					left : scrollUlLeft + scrollUlWidth
				}, 1500, function() {
					scrollUlLeft = parseInt($('.wrapper ul').css('left'), 10);
					prevAllow = true;
				})
			}
		});
		
		//点击下一页
		$('#next').mouseover(function() {
			if (nextAllow) {
				nextAllow = false;
				$('.wrapper ul').animate({
					left : scrollUlLeft - scrollUlWidth
				}, 1500, function() {
					scrollUlLeft = parseInt($('.wrapper ul').css('left'), 10);
					scrollUlLeft = scrollUlLeft + scrollUlWidth;
					$('.wrapper ul').css('left', scrollUlLeft);
					//复制第一个 li 并插入到 ul 的最后面
					$('.wrapper ul li:first').clone().appendTo('.wrapper ul');
					//删除第一个 li
					$('.wrapper ul li:first').remove();
					nextAllow = true;
				})
			}
		});
		
		//自动滚动
		if (config.autoScroll) {
			setInterval(function() {
				$('#next').trigger('mouseover');
			}, config.autoScrollInterval)
		}
	})