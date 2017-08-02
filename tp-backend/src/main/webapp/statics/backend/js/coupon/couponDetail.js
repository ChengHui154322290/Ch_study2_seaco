var VIEW_COUPON_DETAIL = domain + "/coupon/viewDetail";
var VIEW_COUPON = domain + "/coupon/detailList";
var CANCEL_COUPON = domain + "/coupon/cancel";
var index = parent.layer.getFrameIndex(window.name);

$(document).ready(function() {
	$("a.viewdetailbtn").bind("click", function() {
		var couponId = $(this).closest("a").attr("param");
		$.layer({
			type : 2,
			title : '查看更多',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 500 ],
			iframe : {
				src : VIEW_COUPON_DETAIL + "?id=" + couponId
			}
		});
	});
	$("a.canceldetailbtn").bind("click", function() {
		var couponId = $(this).closest("a").attr("param");
		if (null == couponId || "" == $.trim(couponId)) {
			layer.alert("选择优惠券无效");
			return;
		}
		var url = CANCEL_COUPON + "?id=" + couponId;
		var data = $.post(url);
		if (data.success) {
			layer.msg("作废优惠券成功");
		} else {
			layer.msg("作废优惠券失败");
		}
	});
	$("#cancel").bind("click", function() {
		parent.layer.close(index);
	});
	$("#perCount").on("change", function() {
		$("#pageId").val(1);
		$("#couponDetailForm").submit();
	});
	$("#nextPage").bind("click", function() {
		var pageNo = $("#pageId").val();
		var totalPage = $("#totalPage")[0].innerHTML;
		if (null == pageNo || 1 > parseInt(pageNo)) {
			pageNo = 1;
		}
		if (parseInt(totalPage) <= parseInt(pageNo)) {
			layer.msg("最后一页");
			return;
		}
		pageNo = parseInt(pageNo) + 1;
		$("#pageId").val(pageNo);
		$("#couponDetailForm").submit();
	});
	$("#prePage").bind("click", function() {
		var pageNo = $("#pageId").val();
		if (null == pageNo || 1 > parseInt(pageNo)) {
			pageNo = 1;
		}
		if (1 == parseInt(pageNo)) {
			layer.msg("第一页");
			return;
		}
		pageNo = parseInt(pageNo) - 1;
		$("#pageId").val(pageNo);
		$("#couponDetailForm").submit();
	});
});
