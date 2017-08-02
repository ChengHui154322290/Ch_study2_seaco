/**
 * 商品接口相关
 */
 ;
(function($) {
	var me = this;
	me.activeProduct = me.activeProduct || {};
	
	//商品信息-商品信息
	me.activeProduct.details = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.product.detail, me.dictionary.success);
		} else {
			var path=me.activeHost + me.activeUrl.product.detail + '?' + params.p;
			ajaxGet(params,path);
		}
	};


	//商品信息-添加购物信息
	me.activeProduct.addshopping = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.cart.add, me.dictionary.success);
		} else {
			var path=me.activeHost + me.activeUrl.cart.add;
			ajaxPost(params,path);
		}
	};


	//商品信息-点评列表
	me.activeProduct.comment = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.product.comment, me.dictionary.success);
		} else {
			var path=me.activeHost + me.activeUrl.product.comment;
			ajaxPost(params,path);
		}
	};

	//商品详情-仓库
	me.activeProduct.getstoreid = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.product.getstoreid, me.dictionary.success);
		} else {
			var path=me.activeHost + me.activeUrl.product.getstoreid;
			ajaxPost(params,path);
		}
	};

	//海淘商品-商品信息
	me.activeProduct.htdetails = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.product.details, me.dictionary.success);
		} else {
			var path=me.activeHost + me.activeUrl.product.details + '?' + params.p;
			ajaxGet(params,path);
		}
	};
	
	//商品信息-立即购买
	me.activeProduct.buynow = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.product.addshopping, me.dictionary.success);
		} else {
			var path=me.activeHost + me.activeUrl.order.buynow;
			ajaxPost(params,path);
		}
	};
	
	//商品/购物车满减商品列表
	me.activeProduct.productlist = function(params) {
		if(me.locationData) {
			
		}else{
			var path=me.activeHost + me.activeUrl.product.productlist + '?' + params.p;
			ajaxGet(params,path);
		}
	}
	
}).call(xigou, Zepto);

