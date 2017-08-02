//与安卓或者IOS交互时隐藏头部
$(function() {
	
	if(isWeiXin()){
		$('header').hide();
	}
	var Type = xigou.getQueryString("type");
	if (1 != Type) {
		return;
	}

	$('header').hide();
});