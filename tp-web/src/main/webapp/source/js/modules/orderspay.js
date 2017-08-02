/**
 * 结算接口相关
 */
;
(function($) {
	var me = this;
	me.activeHtClearing = me.activeHtClearing || {};

	//待支付-订单待付款去支付。
	me.activeHtClearing.payNow = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.pay.payorder, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.pay.payorder;
			ajaxPost(params, path);
		}
	};

}).call(xigou, Zepto);