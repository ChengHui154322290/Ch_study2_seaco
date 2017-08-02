$(function() {
	var Type = xigou.getQueryString("type");
	if (1 != Type) {
		return;
	}

	$('.ui-header').hide();
	$('.ui-tab-nav').css('top', '0px');
	$('.ui-tab').css('margin-top', '50px');
});