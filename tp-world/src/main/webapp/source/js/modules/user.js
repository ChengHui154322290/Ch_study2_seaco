/**
 *
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-13 13:48:51
 * @version Ver 1.0.0
 */
//post相关的接口，只能调用xigou.post(pd)方法,如调用xigou.ajax.post(dp,params)方法，会报400错误的，不知道为啥出现这种情况。
;
(function($) {
	var me = this;
	me.activeUser = me.activeUser || {};

	var responseData = {};
	if (me.locationData){
		responseData[me.activeHost + me.activeUrl.user.logon] = static_data.user.logon;
		responseData[me.activeHost + me.activeUrl.user.getTheCode] = static_data.user.getTheCode;
		responseData[me.activeHost + me.activeUrl.user.regist] = static_data.user.regist;
		//修改密码
		responseData[me.activeHost + me.activeUrl.user.modifypwd] = static_data.user.modifypwd;
		//全部订单
		responseData[me.activeHost + me.activeUrl.order.list] = static_data.order.list;
		//待付款
		responseData[me.activeHost + me.activeUrl.user.allorders + "2"] = static_data.user.allorders2;
		//待收货
		responseData[me.activeHost + me.activeUrl.user.allorders + "5"] = static_data.user.allorders5;
		//优惠券（已有）,个人中心
		responseData[me.activeHost + me.activeUrl.coupon.list] = static_data.coupon.list;
		//积分（西客币），个人中心
		responseData[me.activeHost + me.activeUrl.point.list] = static_data.point.list;
		//红包,个人中心
		responseData[me.activeHost + me.activeUrl.user.redenvelope + "1"] = static_data.user.redenvelope1;
		//收货地址列表
		responseData[me.activeHost + me.activeUrl.address.list] = static_data.address.list;
		//收货地址-编辑
		responseData[me.activeHost + me.activeUrl.address.edit] = static_data.address.edit;
		//收货地址-编辑
		responseData[me.activeHost + me.activeUrl.address.add] = static_data.address.add;
		//收货地址-设置默认地址
		responseData[me.activeHost + me.activeUrl.address.isdefault] = static_data.address.isdefault;
		//用户-实名认证-查询
		responseData[me.activeHost + me.activeUrl.safety.checkauth] = static_data.safety.checkauth;
		//用户-实名认证-修改
		responseData[me.activeHost + me.activeUrl.safety.auth] = static_data.safety.auth;
		//订单详情
		responseData[me.activeHost + me.activeUrl.user.orderinfo] = static_data.user.orderinfo;
		//物流信息
		responseData[me.activeHost + me.activeUrl.user.logistics] = static_data.customer.logistics;
		//全部订单
		responseData[me.activeHost + me.activeUrl.order.list] = static_data.order.list;
		//删除或取消
		responseData[me.activeHost + me.activeUrl.order.calordel] = static_data.order.calordel;
		//确认收货
		responseData[me.activeHost + me.activeUrl.pay.confirm] = static_data.pay.confirm;
		//用户-初始化个人中心
		responseData[me.activeHost + me.activeUrl.user.supcount] = static_data.user.supcount;
	}


	var ajaxPost = function(params, path) {
		//默认请求参数
		var dp = {
			url: path,
			requestBody: '',
			callback: function(response, status) { //回调函数
				if (status != me.dictionary.success) {
					me.alert('请求失败，详见' + response);
				}
			}
		};
		$.extend(dp, params);

		if (me.locationData && typeof(path) != undefined) {
			if (
				path.indexOf(me.activeUrl.order.list) > -1 || path.indexOf(me.activeUrl.coupon.list) > -1 || path.indexOf(me.activeUrl.user.redenvelope) > -1
			) {
				$.isFunction(params.callback) && params.callback(responseData[path + dp.requestBody.type], me.dictionary.success);
			} else {
				$.isFunction(params.callback) && params.callback(responseData[path], me.dictionary.success);
			}
		} else {
			me.post(dp);
		}
	};

	//登录
	me.activeUser.logon = function(params) {
		var path = me.activeHost + me.activeUrl.user.logon;
		ajaxPost(params, path);
	};
	//绑定手机
	me.activeUser.bindunion = function(params) {
		var path = me.activeHost + me.activeUrl.user.bindunion;
		ajaxPost(params, path);
	};

	// 联合登录
	me.activeUser.unionlogon = function(params) {
		var path = me.activeHost + me.activeUrl.user.unionlogon;
		ajaxPost(params, path);
	};

	// 登出
	me.activeUser.logout = function(params) {
		var path = me.activeHost + me.activeUrl.user.logout;
		ajaxPost(params, path);
	};

	//获取手机验证码
	me.activeUser.getTheCode = function(params) {
		if (me.locationData){
			$.isFunction(params.callback) && params.callback(static_data.user.getcaptcha, me.dictionary.success);
		}
		else{
			var path = me.activeHost + me.activeUrl.user.getcaptcha;
			ajaxPost(params, path);
		}
	};

	//极验预处理
	me.activeUser.getPreGeetest = function(params) {
			var path = me.activeHost + me.activeUrl.user.pregeetest;
			ajaxPost(params, path);

	};

	//发送验证码
	me.activeUser.sendGeeCode = function(params) {
		var path = me.activeHost + me.activeUrl.user.sendgeecode;
		ajaxPost(params, path);

	};
	//发送验证码（不校验图片）
	me.activeUser.getCaptcha = function(params) {
		var path = me.activeHost + me.activeUrl.user.getcaptcha;
		ajaxPost(params, path);
	};

	//注册
	me.activeUser.registr = function(params) {
		if (me.locationData){
			$.isFunction(params.callback) && params.callback(static_data.user.regist, me.dictionary.success);
		}
		else{
			var path = me.activeHost + me.activeUrl.user.regist;
			ajaxPost(params, path);
		}
	};
	
	//注册
	me.activeUser.dssregistr = function(params) {
		if (me.locationData){
			$.isFunction(params.callback) && params.callback(static_data.dssuser.regist, me.dictionary.success);
		}
		else{
			var path = me.activeHost + me.activeUrl.dssuser.regist;
			ajaxPost(params, path);
		}
	};

	//绑定
	me.activeUser.binding = function(params){
		var path = me.activeHost + me.activeUrl.user.binding;
		ajaxPost(params, path);
	};

	//修改密码
	me.activeUser.modifypassword = function(params) {
		if (me.locationData){
			$.isFunction(params.callback) && params.callback(static_data.user.modifypwd, me.dictionary.success);
		}
		else{
			var path = me.activeHost + me.activeUrl.user.modifypwd;
			ajaxPost(params, path);
		}
	};

	//找回密码
	me.activeUser.forgetpassword = function(params) {
		var path = me.activeHost + me.activeUrl.user.forgetpassword;
		ajaxPost(params, path);
	};


	//用户-退出
	me.activeUser.signout = function(params) {
		var path = me.activeHost + me.activeUrl.user.signout;
		ajaxPost(params, path);
	};

	//用户-订单详情
	me.activeUser.orderinfo = function(params) {
		if (me.locationData){
			$.isFunction(params.callback) && params.callback(static_data.order.detail, me.dictionary.success);
		}
		else{
			var path = me.activeHost + me.activeUrl.order.detail;
			ajaxPost(params, path);
		}
	};

	//用户-订单
	me.activeUser.allorders = function(params) {
		if (me.locationData){
			$.isFunction(params.callback) && params.callback(static_data.order.list, me.dictionary.success);
		}
		else{
			var path = me.activeHost + me.activeUrl.order.list;
			ajaxPost(params, path);
		}
	};

	//用户-订单详情
	me.activeUser.orderinfoTry = function(params) {
		var path = me.activeHost + me.activeUrl.user.orderinfoTry;
		ajaxPost(params, path);
	};

	//用户-订单
	me.activeUser.allordersTry = function(params) {
		var path = me.activeHost + me.activeUrl.user.allordersTry;
		ajaxPost(params, path);
	};

	//用户-是否可领取
	me.activeUser.trialcheck = function(params) {
		var path = me.activeHost + me.activeUrl.user.trialcheck;
		ajaxPost(params, path);
	};

	//新增宝宝信息
	me.activeUser.addbabyInfo = function(params) {
		var path = me.activeHost + me.activeUrl.user.addbabyInfo;
		ajaxPost(params, path);
	}
	

	//试用-确认收货
	me.activeUser.confirmshipmentTry = function(params) {
		var path = me.activeHost + me.activeUrl.user.confirmshipmentTry;
		ajaxPost(params, path);
	};


	//用户-取消或者删除订单
	me.activeUser.deleteorder = function(params) {
		if (me.locationData){
			$.isFunction(params.callback) && params.callback(static_data.order.calordel, me.dictionary.success);
		}
		else{
			var path = me.activeHost + me.activeUrl.order.calordel;
			ajaxPost(params, path);
		}
	};

	//用户-确认收货
	me.activeUser.confirmshipment = function(params) {
		if (me.locationData){
			$.isFunction(params.callback) && params.callback(static_data.order.confirm, me.dictionary.success);
		}
		else{
			var path = me.activeHost + me.activeUrl.order.confirm;
			ajaxPost(params, path);
		}
	};

	//用户-点评
	me.activeUser.comment = function(params) {
		var path = me.activeHost + me.activeUrl.user.comment;
		ajaxPost(params, path);
	};

	//用户-售后-申请
	me.activeUser.apply = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.customer.apply, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.user.apply;
			ajaxPost(params, path);
		}
	};

	//用户-售后-历史记录
	me.activeUser.history = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.customer.history, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.user.history;
			ajaxPost(params, path);
		}
	};

	//用户-售后-列表详情
	me.activeUser.details = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.customer.details, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.user.details;
			ajaxPost(params, path);
		}
	};

	//用户-售后-获取物流公司列表
	me.activeUser.getlogcompany = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.customer.getlogcompany, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.user.getlogcompany;
			ajaxPost(params, path);
		}
	};

	//用户-售后-物流
	me.activeUser.logistics = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.customer.logistics, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.order.logistic;
			ajaxPost(params, path);
		}
	};

	//帮宝适-试用-物流
	me.activeUser.logisticsTry = function(params) {
		var path = me.activeHost + me.activeUrl.user.logisticsTry;
		ajaxPost(params, path);
	};

	//用户-售后-提交物流号
	me.activeUser.postnum = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.customer.postnum, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.user.postnum;
			ajaxPost(params, path);
		}
	};

	//商品信息-添加购物信息
	me.activeUser.addshopping = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.cart.add, me.dictionary.success);
		} else {
			var path=me.activeHost + me.activeUrl.cart.add;
			ajaxPost(params,path);
		}
	};

	//用户-实名认证
	me.activeUser.authentication = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.safety.auth, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.safety.auth;
			ajaxPost(params, path);
		}
	};

	//用户-实名认证-查询
	me.activeUser.auth_query = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.safety.checkauth, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.safety.checkauth;
			ajaxPost(params, path);
		}
	};

	//用户-优惠券-已有
	me.activeUser.haved = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.coupon.list, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.coupon.list;
			ajaxPost(params, path);
		}
	};

	//用户-兑换优惠券
	me.activeUser.exchangCoupon = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.coupon.inreceive, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.coupon.inreceive;
			ajaxPost(params, path);
		}
	};

	// 用户 领取红包
	me.activeUser.redpacket = function(params) {
		var path = me.activeHost + me.activeUrl.coupon.redpacket;
		ajaxPost(params, path);
	};

	// 获取线下活动的兑换码
	me.activeUser.offlinecouponcode = function(params) {
		var path = me.activeHost + me.activeUrl.coupon.offlinecouponcode;
		ajaxPost(params, path);
	};

	//用户-领取优惠券
	me.activeUser.receivecoupon = function(params) {
		var path = me.activeHost + me.activeUrl.user.receivecoupon;
		ajaxPost(params, path);
	};

	//用户-扫码领取优惠券
	me.activeUser.receivecouponbycode = function(params) {
		var path = me.activeHost + me.activeUrl.user.receivecouponbycode;
		ajaxPost(params, path);
	};

	//用户-可领取优惠券列表
	me.activeUser.alllivecoupon = function(params) {
		var path = me.activeHost + me.activeUrl.user.alllivecoupon;
		ajaxPost(params, path);
	};
	//用户-扫码可领取优惠券(单个)
	me.activeUser.onecoupon = function(params) {
		var path = me.activeHost + me.activeUrl.user.onecoupon;
		ajaxPost(params, path);
	};
	//用户-红包
	me.activeUser.redenvelope = function(params) {
		var path = me.activeHost + me.activeUrl.user.redenvelope;
		ajaxPost(params, path);
	};
	
	//用户-可领取红包列表
	me.activeUser.allliveredenvelope = function(params) {
		var path = me.activeHost + me.activeUrl.user.allliveredenvelope;
		ajaxPost(params, path);
	};

	//用户-红包-删除
	me.activeUser.redenvdel = function(params) {
		var path = me.activeHost + me.activeUrl.user.redenvdel;
		ajaxPost(params, path);
	};

	//用户-收货地址-列表
	me.activeUser.lists = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.address.list, me.dictionary.success);
		} 
		else {
			var path = me.activeHost + me.activeUrl.address.list;
			ajaxPost(params, path);
		}
	};

	//用户-收货地址-新增
	me.activeUser.newAdd = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.address.add, me.dictionary.success);
		} 
		else {
			var path = me.activeHost + me.activeUrl.address.add;
			ajaxPost(params, path);
		}
	};

	//用户-收货地址-编辑
	me.activeUser.edit = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.address.edit, me.dictionary.success);
		} 
		else {
			var path = me.activeHost + me.activeUrl.address.edit;
			ajaxPost(params, path);
		}
	};
	//用户-收货地址-设置为默认
	me.activeUser.isdefault = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.address.isdefault, me.dictionary.success);
		} 
		else {
			var path = me.activeHost + me.activeUrl.address.isdefault;
			ajaxPost(params, path);
		}
	};
	//用户-收货地址-删除
	me.activeUser.del = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.address.del, me.dictionary.success);
		} 
		else {
			var path = me.activeHost + me.activeUrl.address.del;
			ajaxPost(params, path);
		}
	};

	//用户-退货地址
	me.activeUser.return_address = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.user.returnAddsessInfo, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.user.return_address;
			ajaxPost(params, path);
		}

	};

	//用户-初始化个人中心
	me.activeUser.initCount = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.user.supcount, me.dictionary.success);
		} 
		else
		{
			var path = me.activeHost + me.activeUrl.user.supcount;
			ajaxPost(params, path);
		}
	};

	//用户-订单详情
	me.activeUser.itemReview = function(params) {
		var path = me.activeHost + me.activeUrl.user.orderinfo;
		ajaxPost(params, path);
	};

	//商品信息-立即购买
	me.activeUser.buynow = function(params) {
		var path=me.activeHost + me.activeUrl.order.buynow;
		ajaxPost(params,path);
	};
	
	//得到微信openid
	me.activeUser.openid=function(params){
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.wx.getopenid, me.dictionary.success);
		} 
		else{
			var path = me.activeHost + me.activeUrl.wx.getopenid+"?code=" + params.requestBody.code;
			ajaxGet(params, path);
		}
	};

	// 网页授权获取用户信息
	me.activeUser.wxInfo = function(params) {
		var path = me.activeHost + me.activeUrl.wx.getuserinfo+"?code=" + params.requestBody.code;
		ajaxGet(params, path);
	};

	// 获取code链接
	me.activeUser.getcode = function(params){
		var path = me.activeHost + me.activeUrl.wx.getcodeurl+"?url=" + params.requestBody.url + '&scope=' + params.requestBody.scope;
		ajaxGet(params, path);
	};

	// 接口注入权限验证配置
	me.activeUser.config = function(params) {
		var path=me.activeHost + me.activeUrl.wx.config + '?'+ params.p;
		ajaxGet(params,path);
	}

	//上传图片
	me.activeUser.uploadimage = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.file.uploadimg, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.file.uploadimg;
			ajaxPost(params, path);
		}
	};

	//code换优惠券
	me.activeUser.codeChange = function(params) {
		var path = me.activeHost + me.activeUrl.user.codeChange;
		ajaxPost(params, path);
	};

	//西客币币兑换红包
	me.activeUser.coinChange = function(params) {
		var path = me.activeHost + me.activeUrl.user.coinChange;
		ajaxPost(params, path);
	};
	
	//用户积分（西客币）详情
	me.activeUser.pointlist = function(params){
		var path = me.activeHost + me.activeUrl.point.list;
		ajaxPost(params, path);
	}

}).call(xigou, Zepto);