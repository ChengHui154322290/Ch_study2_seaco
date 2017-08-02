/**
 * 专场接口相关
 */
;
(function($) {
	var me = this;
	me.activeSpecialsale = me.activeSpecialsale || {};

	//专场-商品列表
	me.activeSpecialsale.product = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.topic.products, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.topic.products +'?'+ params.p;
			ajaxGet(params, path);
		}
	};

	//海淘专场-商品列表
	me.activeSpecialsale.htproduct = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.topic.products, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.topic.products +'?'+ params.p;
			ajaxGet(params, path);
		}
	};

	//专场-详情
	me.activeSpecialsale.details = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.topic.detail, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.topic.detail + '?' + params.p;
			ajaxGet(params, path);
		}
	};

	//海淘专场-详情
	me.activeSpecialsale.htdetails = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.topic.detail, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.topic.detail + '?' + params.p;
			ajaxGet(params, path);
		}
	};

}).call(xigou, Zepto);