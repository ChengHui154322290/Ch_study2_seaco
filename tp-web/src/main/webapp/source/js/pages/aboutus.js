$(function() {
	if(isWeiXin()){
		$("header").hide();
		$(".header_wx").show();
		$("title").html("关于西客");
	}
	var Type = xigou.getQueryString("type");
	if (1 != Type) {
		return;
	}

	$('header').hide();
});