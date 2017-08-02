/**
 * 结算接口相关
 */
;
(function($) {
	var me = this;
	me.activeClearing = me.activeClearing || {};

	//结算-获取支付数据
	me.activeClearing.submitwaporder = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.account.submitwaporder, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.pay.submitwaporder;
			ajaxPost(params, path);
		}
	};

	//待支付-订单待付款去支付。
	me.activeClearing.submitunpaywaporder = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.account.submitunpaywaporder, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.pay.submitunpaywaporder;
			ajaxPost(params, path);
		}
	};

}).call(ourslook, Zepto);