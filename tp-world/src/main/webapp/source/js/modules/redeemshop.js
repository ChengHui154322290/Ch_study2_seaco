/**
 * 线下团购券管理接口相关
 */
;
(function($) {
	var me = this;
	//me.locationData = true;
	me.redeemManager = me.redeemManager || {};
	//团购券列表
	me.redeemManager.list = function(params) {
		var path = me.activeHost + me.activeUrl.orderredeem.list;
		ajaxPost(params, path);
	};

	//团购券统计
	me.redeemManager.count = function(params) {
		var path = me.activeHost + me.activeUrl.orderredeem.count;
		ajaxPost(params, path);
	};
	//团购券列表条件-项目列表
	me.redeemManager.paramskulist = function(params) {
		var path = me.activeHost + me.activeUrl.orderredeem.paramskulist;
		ajaxPost(params, path);
	};
	//团购券列表条件-状态列表
	me.redeemManager.paramcodestatus = function(params) {
		var path = me.activeHost + me.activeUrl.orderredeem.paramcodestatus;
		ajaxPost(params, path);
	};
	//团购券兑换
	me.redeemManager.exchange = function(params) {
		var path = me.activeHost + me.activeUrl.orderredeem.exchange;
		ajaxPost(params, path);
	};
	//团购券-店铺员工列表
	me.redeemManager.userlist = function(params) {
		var path = me.activeHost + me.activeUrl.orderredeem.userlist;
		ajaxPost(params, path);
	};
	//团购券-店铺员工新增
	me.redeemManager.usersave = function(params) {
		var path = me.activeHost + me.activeUrl.orderredeem.usersave;
		ajaxPost(params, path);
	};
	//团购券-店铺员工删除
	me.redeemManager.userdelete = function(params) {
		var path = me.activeHost + me.activeUrl.orderredeem.userdelete;
		ajaxPost(params, path);
	};
}).call(xigou, Zepto);