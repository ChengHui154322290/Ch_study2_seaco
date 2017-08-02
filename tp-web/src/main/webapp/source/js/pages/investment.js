$(function() {
	if(isWeiXin()){
		$('.ui-header').hide();
		$("title").html("投资团队");
	}
	var Type = xigou.getQueryString("type");
	if (1 != Type) {
		return;
	}	
});