/**
 * 售后相关
 */
;
(function($) {
	var me = this;
	me.classify = me.classify || {};

	// 导航
	me.classify.navigation = function(params) {
		var path = me.activeHost + me.activeUrl.classify.navigation + '?';
		ajaxGet(params, path);
	};

	// 执行搜索
	me.classify.search = function(params) {
		var path = me.activeHost + me.activeUrl.classify.search + '?' + params.p;
		ajaxGet(params, path);
	}

	// 搜索结果 – 筛选条件
	me.classify.condition = function(params) {
		var path = me.activeHost + me.activeUrl.classify.condition;
		ajaxGet(params, path);
	}
}).call(xigou, Zepto);