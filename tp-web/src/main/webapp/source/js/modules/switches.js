/**
 * 售后相关
 */
;
(function($) {
	var me = this;
	me.switches = me.switches || {};

	// 支付碼
	me.switches.swt = function(params) {
		var path = me.activeHost + me.activeUrl.switches.swt;
		ajaxPost(params, path);
	};



}).call(xigou, Zepto);