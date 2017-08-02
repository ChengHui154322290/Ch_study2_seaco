/**
 * 团购
 */
;
(function($) {
	var me = this;
	me.groupbuy = me.groupbuy || {};

	// 团购详情
	me.groupbuy.detail = function(params) {
		var path = me.activeHost + me.activeUrl.groupbuy.detail;
		ajaxPost(params, path);
	};

	// 发起团购
	me.groupbuy.launch = function(params) {
		var path = me.activeHost + me.activeUrl.groupbuy.launch;
		ajaxPost(params, path);
	}

	// 参团
	me.groupbuy.join = function(params) {
		var path = me.activeHost + me.activeUrl.groupbuy.join;
		ajaxPost(params, path);
	}

	// 我的团购
	me.groupbuy.my = function(params) {
		var path = me.activeHost + me.activeUrl.groupbuy.my;
		ajaxPost(params, path);
	}

	// 团购列表
	me.groupbuy.list = function(params) {
		var path = me.activeHost + me.activeUrl.groupbuy.list + '?curpage=' + params.page;
		ajaxGet(params, path);
	}
	
	// 团购列表 for首页
	me.groupbuy.listforindex = function(params) {
		var path = me.activeHost + me.activeUrl.groupbuy.listforindex;
		ajaxGet(params, path);
	}
}).call(xigou, Zepto);