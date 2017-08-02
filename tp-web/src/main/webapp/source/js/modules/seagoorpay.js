/**
 * 售后相关
 */
;
(function($) {
	var me = this;
	me.seagoorpay = me.seagoorpay || {};

	// 支付碼
	me.seagoorpay.code = function(params) {
		var path = me.activeHost + me.activeUrl.seagoorpay.code;
		ajaxPost(params, path);
	};

	// 支付碼
	me.seagoorpay.querypaystatus = function(params) {
		var path = me.activeHost + me.activeUrl.seagoorpay.querypaystatus;
		ajaxPost(params, path);
	};


}).call(xigou, Zepto);