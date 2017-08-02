/**
 * 购物车接口相关
 */
;
(function($) {
	var me = this;
	me.activeShoppingCart = me.activeShoppingCart || {};
	
	//购物车
	me.activeShoppingCart.loadCart = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.cart.load, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.cart.load;
			ajaxPost(params, path);
		}
	};

	//购物车-删除
	me.activeShoppingCart.getstoreid = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.shopping["delete"], me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.shoppingCart.getstoreid;
			ajaxPost(params, path);
		}
	};


	//购物车-获取总金额
	me.activeShoppingCart.authprice = function(params) {
		if (me.locationData) {
			//alter by zouyt start
			//$.isFunction(params.callback) && params.callback(static_data.shopping.authprice, me.dictionary.success);
			
			switch(params.requestBody.type){//“0”全选 “1”取消全选  “2”单选 “3”取消单选 “4”更新商品数量 "5”结算校验（在购物车点击结算会做一次校验）
				case "1"://全选
					$.isFunction(params.callback) && params.callback(static_data.cart.operation, me.dictionary.success);
					break;
				case "2"://取消全选
					$.isFunction(params.callback) && params.callback(static_data.cart.operation, me.dictionary.success);
					break;
					break;
				case "3"://单选
					$.isFunction(params.callback) && params.callback(static_data.cart.operation, me.dictionary.success);
					break;
				case "4"://取消单选
					$.isFunction(params.callback) && params.callback(static_data.cart.operation, me.dictionary.success);
					break;
				case "5"://更新商品数量
					$.isFunction(params.callback) && params.callback(static_data.cart.operation, me.dictionary.success);
					break;
				case "6":
					$.isFunction(params.callback) && params.callback(static_data.cart.operation, me.dictionary.success);
					break;
				case "7"://算校验（在购物车点击结算会做一次校验）
					$.isFunction(params.callback) && params.callback(static_data.cart.operation, me.dictionary.success);
					break;
			}			
		} else {
			var path = me.activeHost + me.activeUrl.cart.operation;
			ajaxPost(params, path);
		}
	};

}).call(xigou, Zepto);