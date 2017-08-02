/**
 * 
 */
 ;
(function($) {
	var me = this;
	me.hhblogin = me.hhblogin || {};
	//商品信息-商品信息
	me.hhblogin.dologin = function(params) {
		var path = me.activeHost + me.activeUrl.hhblogin.hhblogin + '?openId=' + params.params.openId;
		ajaxGet(params, path);
	}

}).call(xigou, Zepto);


