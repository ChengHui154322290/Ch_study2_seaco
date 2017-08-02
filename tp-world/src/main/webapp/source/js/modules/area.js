/**
 *
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-10 23:01:56
 * @version Ver 1.0.0
 */

//区域相关接口
;
(function($) {
	var me = this;
	me.activeArea = me.activeArea || {};

	//获取省市列表
	me.activeArea.getProvinces = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.position.getprovlist, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.position.getprovlist;
			ajaxGet(params, path);
		}
	};

	//获取省市列表
	me.activeArea.getCity = function(params) {
		if (me.locationData) {
			switch (params.p.split('=')[1]) {
				case "11302":
					$.isFunction(params.callback) && params.callback(static_data.position.getarealist, me.dictionary.success);
					break;
				case "429":
					// $.isFunction(params.callback) && params.callback(static_data.shipping_address.counties, me.dictionary.success);
					// $.isFunction(params.callback) && params.callback(static_data.shipping_address.counties, me.dictionary.success);
					break;
				case "11303":
					// $.isFunction(params.callback) && params.callback(static_data.shipping_address.street, me.dictionary.success);
					$.isFunction(params.callback) && params.callback(static_data.position.getdistrictlist, me.dictionary.success);
					break;
				case "11304":
					// $.isFunction(params.callback) && params.callback(static_data.shipping_address.street, me.dictionary.success);
					$.isFunction(params.callback) && params.callback(static_data.position.getstreetlist, me.dictionary.success);
					break;
				default:
					$.isFunction(params.callback) && params.callback([], me.dictionary.success);
					break;
			}
		} else {
			var path = me.activeHost + me.activeUrl.position.getarealist + '?' + params.p;
			ajaxGet(params, path);
		}
	};

	me.activeArea.getAreaTree = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.position.getareatree, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.position.getareatree;
			ajaxGet(params, path);
		}
	};

}).call(xigou, Zepto);