var SEARCH_COUPON = domain + "/coupon/searchCoupon";
var index = parent.layer.getFrameIndex(window.name);

$(document).ready(function() {
	$("#selectedRd").unbind().bind("click", function() {
		var couponId = $("input[name='couponId']:checked").val();//获取优惠券id
		var couponName = $("input[name='couponId']:checked").parent().parent().find("td.couponName").text();
		parent.$("input.tmpCmaCouponId").val(couponId);
		parent.$("input.cmaCouponId").val(couponId);
		parent.$("input.cmaCouponName").val(couponName);
		/*var selectIndex = $("#selectRow").val();
		if (null != selectIndex) {
			var row = parent.$("#couponList tr")[selectIndex];
			$(row).children().find("input[name='batchNo']").val(couponId);
		}*/
		parent.layer.close(index);
	});
	
	$("#cancelRd").unbind().bind("click", function() {
		parent.layer.close(index);
	});
});