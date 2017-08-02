/**
 * 修改手机号码接口
 */
;
(function($) {
	var me = this;
	//me.locationData = true;
	me.modifymobile = me.modifymobile || {};

	me.modifymobile.init = function(params) {//获取用户信息
		var path = xigou.activeHost + xigou.activeUrl.user.init;
		ajaxPost(params, path);
	};
	me.modifymobile.checklogon = function(params) {
		var path = xigou.activeHost + xigou.activeUrl.user.logon;
		ajaxPost(params, path);
	};
	me.modifymobile.sendgeecode = function(params) {
		var path = xigou.activeHost + xigou.activeUrl.user.sendgeecode;
		ajaxPost(params, path);
	};
	me.modifymobile.modifymobile = function(params) {
		var path = xigou.activeHost + xigou.activeUrl.user.modifymobile;
		ajaxPost(params, path);
	};
	me.modifymobile.checkmobile = function(params) {
		var path = xigou.activeHost + xigou.activeUrl.user.checkmobile;
		ajaxPost(params, path);
	};
	me.modifymobile.getcaptcha = function(params) {
		var path = xigou.activeHost + xigou.activeUrl.user.getcaptcha;
		ajaxPost(params, path);
	};
	me.modifymobile.getcaptchaNew = function(params) {
		var path = xigou.activeHost + xigou.activeUrl.user.getcaptchaNew;
		ajaxPost(params, path);
	};
	
}).call(xigou, Zepto);