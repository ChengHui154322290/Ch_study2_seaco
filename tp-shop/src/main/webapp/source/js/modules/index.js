/**
 * 首页接口相关
 */
;
(function($) {
	var me = this;
	me.activeIndex = me.activeIndex || {};

	//广告位信息
	me.activeIndex.adInfo = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.index.getbanners, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.index.getbanners;
			ajaxGet(params, path);
		}
	};

	//首页-今日上新
	me.activeIndex.todayHave = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.index.gettodaynew, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.index.gettodaynew + '?' + params.p;
			ajaxGet(params, path);
		}
	};

	//首页-明日预告-开团提醒
	me.activeIndex.openGroup = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.index.getopengroup, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.index.opengroup;
			ajaxPost(params, path);
		}
	};

	//首页广告弹框
	me.activeIndex.homebox=function(params){
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.index.homebox, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.index.homebox;
			ajaxGet(params, path);
		}
	}
	
	//得到微信openid
	me.activeIndex.openid=function(params){
		
		var path = me.activeHost + me.activeUrl.index.openid+"?code=" + params.requestBody.code;
		ajaxGet(params, path);
	}

	// 模块信息
	me.activeIndex.module = function(params) {
		var path = me.activeHost + me.activeUrl.index.module + "?";
		ajaxGet(params, path);
	}


}).call(xigou, Zepto);