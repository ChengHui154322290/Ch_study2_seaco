/**
 * 售后相关
 */
;
(function($) {
	var me = this;
	me.aftersales = me.aftersales || {};

	// 售后列表
	me.aftersales.list = function(params) {
		var path = me.activeHost + me.activeUrl.aftersales.list;
		ajaxPost(params, path);
	};

	// 物流公司列表
	me.aftersales.companylist = function(params) {
		var path = me.activeHost + me.activeUrl.aftersales.companylist;
		ajaxPost(params, path);
	}

	// 申请售后
	me.aftersales.apply = function(params) {
		var path = me.activeHost + me.activeUrl.aftersales.apply;
		ajaxPost(params, path);
	}

	// 取消售后单
	me.aftersales.cancal = function(params) {
		var path = me.activeHost + me.activeUrl.aftersales.cancel;
		ajaxPost(params, path);
	}

	// 提交物流单号
	me.aftersales.submitlogistic = function(params) {
		var path = me.activeHost + me.activeUrl.aftersales.submitlogistic;
		ajaxPost(params, path);
	}
}).call(xigou, Zepto);