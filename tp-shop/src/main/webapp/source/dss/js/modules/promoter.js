/**
 * 推广员相关接口
 */
;
(function($) {
	var me = this;
	me.promoterFunc = me.promoterFunc || {};
	
	//分销主页或者账户详细信息
	me.promoterFunc.loadDssAccount = function(params) {
		if (me.locationData) {
		} else {
			var path = me.activeHost + me.activeUrl.promoter.account;
			ajaxPost(params, path);
		}
	};
	
	//账单详情
	xigou.promoterFunc.queryBillInfo = function(params) {
		if (me.locationData) {
		} else {
			var path = me.activeHost + me.activeUrl.promoter.billinfo;
			ajaxPost(params, path);
		}
	};
	//分销员查询
	xigou.promoterFunc.queryDealers = function(params) {
		if (me.locationData) {
		} else {
			var path = me.activeHost + me.activeUrl.promoter.dealers;
			ajaxPost(params, path);
		}
	};
	//订单查询
	xigou.promoterFunc.queryOrders = function(params) {
		if (me.locationData) {
		} else {
			var path = me.activeHost + me.activeUrl.promoter.orders;
			ajaxPost(params, path);
		}
	};

	//爆款商品列表
	xigou.promoterFunc.items = function(params) {
		var path = me.activeHost + me.activeUrl.promoter.items + '?' + params.p;
		ajaxGet(params, path);
	}

	//获取活动专题
	xigou.promoterFunc.getopics = function(params){
		if (me.locationData) {
		} else {
			var path = me.activeHost + me.activeUrl.promoter.getopics;
			ajaxPost(params, path);
		}
	}

	xigou.promoterFunc.getopicitems = function(params){
		if (me.locationData) {
		} else {
			var path = me.activeHost + me.activeUrl.promoter.getopicitems;
			ajaxPost(params, path);
		}
	}

	xigou.promoterFunc.setshelves = function(params){
		if (me.locationData) {
		} else {
			var path = me.activeHost + me.activeUrl.promoter.setshelves;
			ajaxPost(params, path);
		}
	}

	xigou.promoterFunc.getitles = function(params){
		if (me.locationData) {
		} else {
			var path = me.activeHost + me.activeUrl.promoter.getitles;
			ajaxPost(params, path);
		}
	}

	xigou.promoterFunc.dsswithdraw = function(params){
		if (me.locationData) {
		} else {
			var path = me.activeHost + me.activeUrl.promoter.withdraw;
			ajaxPost(params, path);
		}
	}
	
	xigou.promoterFunc.dssLogin = function(params){
		if (me.locationData) {
		} else {
			var path = me.activeHost + me.activeUrl.promoter.login;
			ajaxPost(params, path);
		}
	}

	xigou.promoterFunc.updatepromoter = function(params){
		if (me.locationData) {
		} else {
			var path = me.activeHost + me.activeUrl.promoter.updatepromoter;
			ajaxPost(params, path);
		}
	}
	xigou.promoterFunc.withdrawdetail = function(params){
		if (me.locationData) {
		} else {
			var path = me.activeHost + me.activeUrl.promoter.withdrawdetail;
			ajaxPost(params, path);
		}
	}
	
	
	//邀请码验证
	xigou.promoterFunc.checkInvisteCode = function(params) {
		if (me.locationData) {
		} else {
			var path = me.activeHost + me.activeUrl.promoter.checkInvisteCode;
			ajaxPost(params, path);
		}
	}
	
	//邀请码注册
	xigou.promoterFunc.saveInvistePromoter = function(params) {
		if (me.locationData) {
		} else {
			var path = me.activeHost + me.activeUrl.promoter.saveInvistePromoter;
			ajaxPost(params, path);
		}
	}
	
}).call(xigou, Zepto);
