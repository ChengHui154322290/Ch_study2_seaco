/**
 * 结算接口相关
 */
;
(function($) {
	var me = this;
	//me.locationData = true;
	me.activeHtClearing = me.activeHtClearing || {};

	//结算-获取订单总金额
	me.activeHtClearing.authprice = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.order.submitinfo, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.order.submitinfo;
			ajaxPost(params, path);
		}
	};

	//待支付-订单待付款去支付。
	me.activeHtClearing.submitseaorder = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.order.submit, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.order.submit;
			ajaxPost(params, path);
		}
	};

	// 提交订单合并支付
	me.activeHtClearing.mergesubmit = function(params) {
		var path = me.activeHost + me.activeUrl.order.mergesubmit;
		ajaxPost(params, path);
	}


	//可用优惠券数量
	me.activeUser.queryordercouponcount = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.coupon.inreceive, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.coupon.queryordercouponcount;
			ajaxPost(params, path);
		}
	};
	
	

}).call(xigou, Zepto);