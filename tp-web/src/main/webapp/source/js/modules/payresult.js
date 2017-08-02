/**
 *
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-03-15 21:48:55
 * @version Ver 1.0.0
 */

//支付返回状态相关接口
;
(function($) {
	var me = this;
	me.activePayresults = me.activePayresults || {};

	//获取
	me.activePayresults.status = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.pay.payresult, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.pay.payresult;
			ajaxPost(params, path);
		}
	};

}).call(xigou, Zepto);