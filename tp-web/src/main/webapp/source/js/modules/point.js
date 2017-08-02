/**
 * 签到接口
 */
;
(function($) {
	var me = this;
	//me.locationData = true;
	me.point = me.point || {};

	me.point.showcalendar = function(params) {
		var path = xigou.activeHost + xigou.activeUrl.usersign.showcalendar;
		ajaxPost(params, path);
	};
	me.point.show = function(params) {
		var path = xigou.activeHost + xigou.activeUrl.usersign.show;
		ajaxPost(params, path);
	};
	

}).call(xigou, Zepto);