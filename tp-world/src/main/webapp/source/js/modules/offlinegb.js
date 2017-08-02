/**
 * 首页接口相关
 */
;
(function($) {
	var me = this;
	me.activeOfflinegb = me.activeOfflinegb || {};

	//广告位信息
	me.activeOfflinegb.banner = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.index.getbanners, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.offlinegb.banner;
			ajaxGet(params, path);
		}
	};

	//商家列表
	me.activeOfflinegb.shoplist = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.index.gettodaynew, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.offlinegb.shoplist+'?' + params.p;;
			ajaxGet(params, path);
		}
	};

	//热门团购
	me.activeOfflinegb.hotsale = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.index.getopengroup, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.offlinegb.hotsale;
			ajaxGet(params, path);
		}
	};


}).call(xigou, Zepto);