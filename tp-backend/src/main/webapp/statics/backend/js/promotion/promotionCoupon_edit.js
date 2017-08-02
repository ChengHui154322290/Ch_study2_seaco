var ADD_TOPIC_COUPON = domain + "/topicCoupon/add";
var SEARCH_TOPIC_COUPON = domain + "/topicCoupon/{rowIndex}/search";
var SEARCH_COUPON_NAME = domain + "/coupon/searchCouponById";

var couponRemoveList = new Array();
$(document).ready(
	function() {
		if(!isCouponTopic()){
			clearCouponList();
			$("#topicCouponDetails").css("display","none");
		}
		$("#addCoupon").on("click", function() {
			var row = $("#topicCouponsList tr:last");
			var sortIndex = 0;
			if($(row).children().find("input[name='couponSortIndex']") !=null 
					&& $(row).children().find("input[name='couponSortIndex']").length > 0){
				sortIndex = $(row).children().find("input[name='couponSortIndex']").val();
			}
			var data = syncGet(ADD_TOPIC_COUPON, {});
			$("#topicCouponsList").append(data);
			addCouponClickEventOperation();
			row = $("#topicCouponsList tr:last");
			$(row).children().find("input[name='couponSortIndex']").val(parseInt(sortIndex) + 10);
		});
		$("input[name='removeCoupon']").on(
				"click",
				function() {
					var topicCouponId = $(this).closest("tr").find("input[name='topicCouponId']")
									.val();
					if (topicCouponId == null || topicCouponId == "") {
					} else {
						if(!couponRemoveList.contains(topicCouponId)){
							couponRemoveList.push(topicCouponId);
						}
					}
					$(this).closest("tr").detach();
				});
});

function addCouponClickEventOperation() {
	$("input[name='confirmCoupon']").off("click");
	$("input[name='confirmCoupon']").on("click", confirmCouponBind);

	$("input[name='searchCoupon']").off("click");
	$("input[name='searchCoupon']").on("click", searchCouponBind);

	$("input[name='removeCoupon']").off("click");
	$("input[name='removeCoupon']").on("click", removeCouponBind);
	
	$("img[name='selectCouponImage']").off("click");
	$("img[name='selectCouponImage']").on("click", topicCouponClickImgBind);
	
	$("a[name='removeCouponImage']").off("click");
	$("a[name='removeCouponImage']").on("click", topicCouponRemoveImgBind);
}

function searchCouponBind(){
	var rowIndex = $(this).closest("tr").index();
	var url = SEARCH_TOPIC_COUPON.replaceAll("{rowIndex}", rowIndex);
	$.layer({
		type : 2,
		title : "查询专场优惠券",
		shadeClose : true,
		maxmin : true,
		fix : false,
		area : [ '800px', 600 ],
		iframe : {
			src : url
		}
	});
}

function removeCouponBind(){
	var topicCouponId = $(this).closest("tr").find(
						"input[name='topicCouponId']").val();
	if (topicCouponId == null || topicCouponId == "") {
	} else {
		var hasDuplicate = false;
		for (var row = 0; row < couponRemoveList.length; row++) {
			if(couponRemoveList[row] == topicCouponId){
				hasDuplicate = true;
				break;
			}
		}
		if(!hasDuplicate){
			couponRemoveList.push(topicCouponId);
		}
	}
	$(this).closest("tr").detach();
}

function confirmCouponBind() {
	var rowIndex = $(this).closest("tr").index(); 
	var couponId =  $(this).closest("tr").find(
						"input[name='couponIdValue']").val();
	if(null == couponId || $.trim(couponId).length == 0){
		layer.alert("请输入优惠券序号");
		return;
	}
	$.get(SEARCH_COUPON_NAME, {
		couponId : couponId
	}, function(data) {
		if (data.success) {
			var row = $("#topicCouponsList tr")[rowIndex];
			$(row).children().find("input[name='couponId']")
					.val(couponId);
			$(row).children().find("span")[0].innerHTML = data.data.couponName;
			if(data.data.couponType == '0'){
				$(row).children().find("span")[1].innerHTML = '满减券';
			} else if(data.data.couponType == '1'){
				$(row).children().find("span")[1].innerHTML = '现金券';
			}
		} else {
			layer.alert("查询优惠券批次信息失败");
		}
	});
}

function topicCouponClickImgBind(){
	var picUrl = $(this).closest("tr").find("input[name='couponImage']").val();
	var model=$("#viewModel").val();
	if(null != picUrl && "" != $.trim(picUrl)){
		picUrl = $(this).closest("tr").find("img[name='selectCouponImage']").attr("src");
		$.layer({
			type : 2,
			title : "查看优惠券图片",
			shadeClose : true,
			maxmin : true,
			fix : false,
			area: ['600px', 400],
			iframe : {
				src : picUrl
			}
		});
	}else if("view" != model){
		var selectIndex = $(this).closest("tr").index(); 
		var url = UPLOAD_COUPON.replace("{couponIndex}",selectIndex);
		$.layer({
			type : 2,
			title : "上传活动优惠券图片",
			maxmin : true,
			fix : false,
			area: ['400px', 300],
			iframe : {
				src : url
			},
			end : function(){
				addTopicCouponOperationScript();
			}
		});
	}
}

function topicCouponRemoveImgBind(){
	$(this).closest("tr").find("input[name='couponImage']").val("");
	$(this).closest("tr").find("img[name='selectCouponImage']").attr("src", UPLD_IMG);
}

function clearCouponList(){
	$(".topicCouponList").each(function(){
		var topicCouponId = $(this).closest("tr").find(
							"input[name='topicCouponId']").val();
		var hasDuplicate = false;
		for (var row = 0; row < couponRemoveList.length; row++) {
			if(couponRemoveList[row] == topicCouponId){
				hasDuplicate = true;
				break;
			}
		}
		if(!hasDuplicate){
			couponRemoveList.push(topicCouponId);
		}
		$(this).closest("tr").detach();
	})
}
