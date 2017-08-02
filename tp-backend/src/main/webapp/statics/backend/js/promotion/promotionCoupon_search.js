var index = parent.layer.getFrameIndex(window.name);

$(document).ready(function() {
	$("#selected").on("click", function() {
		if($("input[name='couponId']:checked").length > 0){
			var couponId = $("input[name='couponId']:checked").val();
			var couponName = $("input[name='couponId']:checked").closest("td").find("input[name='couponNameValue']").val();
			var couponType = $("input[name='couponId']:checked").closest("td").find("input[name='couponTypeValue']").val();
			var selectIndex = $("#selectRow").val();
			if (null != selectIndex) {
				var row = parent.$("#topicCouponsList tr")[selectIndex];
				$(row).children().find("input[name='couponId']").val(couponId);
				$(row).children().find("input[name='couponIdValue']").val(couponId);
				$(row).children().find("span")[0].innerHTML = couponName;
				if(couponType == '0'){
					$(row).children().find("span")[1].innerHTML = '满减券';
				} else if(couponType == '1'){
					$(row).children().find("span")[1].innerHTML = '现金券';
				}
				$(row).children().find("select[name='couponSize'] option")[0].selected = true;
			}
			parent.layer.close(index);
		}
	});
	
	$("#cancel").on("click", function() {
		parent.layer.close(index);
	});
	
	$("#nextPage").on("click", function(){
		var pageNo = $("#pageId").val();
		var totalPage = $("#totalPage").text();
		var nextPage = parseInt(pageNo) + 1;
		if (nextPage > parseInt(totalPage)) {
			alert("已经是最后一页了");
			return;
		}
		$("#pageId").val(nextPage);
		$("#queryAttForm").submit();
	});
	$("#prePage").on("click", function(){
		var pageNo = $("#pageId").val();
		var prePage = parseInt(pageNo) - 1;
		if (prePage < 1) {
			alert("已经是第一页了");
			return;
		}
		$("#pageId").val(prePage);
		$("#queryAttForm").submit();
	});
	$("#perCount").on("change",function(){
		$("#pageId").val(1);
		$("#queryAttForm").submit();
	});
});