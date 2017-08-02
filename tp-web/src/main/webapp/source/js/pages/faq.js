$(function() {
	if(isWeiXin()){
		$(".ui-header").hide();
		$("title").html("常见问题");
		$(".ui-tab-nav").css({
			"top":"0"
		});
		$(".ui-tab").css({
			"margin-top":"44px"
		});
		$("title").html("常见问题");
		
	}
	
	var Type = xigou.getQueryString("type");
	if (1 != Type) {
		return;
	}

	$('.ui-header').hide();
	$('.ui-tab-nav').css('top', '0px');
	$('.ui-tab').css('margin-top', '50px');
});