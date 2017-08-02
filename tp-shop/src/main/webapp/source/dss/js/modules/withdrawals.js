/**
 * 获取提现明细接口
 */
;
(function($) {
	var me = this;
	//me.locationData = true;
	me.withdrawFunc = me.withdrawFunc || {};

	me.withdrawFunc.withdrawalsRecord = function(params) {
		if (me.locationData) {
		} else {
			var path = me.activeHost + me.activeUrl.withdrawals.getWithdrawDetail;
			ajaxPost(params, path);
		}
		
	};

}).call(xigou, Zepto);