var ISSUE_COUPON = domain + "/coupon/auto/issueCoupon";
var SEARCH_COUPON = domain + "/coupon/auto/showCouponSearch";
var SEARCH_COUPON_NAME = domain + "/coupon/auto/searchCouponName";

$(document).ready(function() {
	$(".issue").bind("click", function() {
		var batches = new Array();
		$("#couponBatch").find("input[name='batchNo']").each(function() {
			batches.push($(this).val());
		})
		var batchJSON = "";
		if (batches.length > 0) {
			batchJSON = JSON.stringify(batches);
		}else{
			layer.alert("请输入券号");
			return;
		}
		if (null == batchJSON || "" == $.trim(batchJSON)) {
			layer.alert("请输入券号");
			return;
		}
		var msgSend = $("#msgSend")[0].checked ? 1 : 0;
		var msgContent = $("#msgContent").val();
		var name = $("#name").val();
		var couponsendStatus = $(this).attr("status");
		var couponsendType = $("#type").val();
		var startTime = $("#start_time").val();
		var endTime = $("#end_time").val();
		
		if(startTime == null || "" == startTime){
			layer.alert("请输入开始时间");
			return;
		}
		
		if(endTime == null || "" == endTime ){
			layer.alert("请输入结束时间");
			return;
		}
		
		if (msgSend && (null == msgContent || "" == $.trim(msgContent))) {
			layer.alert("请输入需要发送的短信内容");
			return;
		}
		
		var strlegth = getByteVal($.trim(msgContent));
		if (msgSend && (strlegth > 280)) {
			layer.alert("短信内容不超过140个字");
			return;
		}
		
		if (null == name ) {
			layer.alert("请输入标题");
			return;
		}else{
			var strlegth = getByteVal($.trim(name));
			if(strlegth == 0){
				layer.alert("请输入标题");
				return;
			}
			if(strlegth > 255 ){
				layer.alert("标题不超过255个字符");
				return;
			}
		}

		var allUser = $("#allUser").prop('checked') ? 1: 0;

		var userList = $("#userList").val();
		if(allUser == 0){
			if(userList == null || $.trim(userList)==''){
				layer.alert("请输入需要发送的用户号");
				return;
			}
		}


		$(".ssue").attr('disabled',true);
		$.post(ISSUE_COUPON, {
			batches : batchJSON,
			msgSend : msgSend,
			msgContent : msgContent,
			name : name,
			couponsendType : couponsendType,
			couponsendStatus : couponsendStatus,
			startTime : startTime,
			endTime : endTime,
			users : userList,
			isAll : allUser
		}, function(data) {
			if (data.success) {
				if(couponsendStatus =='0')
					layer.alert("保存成功");
				else
					layer.alert("提交成功");
				window.location.href = domain+'/coupon/auto/sendList.htm';
			} else {
				layer.alert(data.msg.message);
			}
			$(".issue").attr('disabled',false);
		});

	});
	$("input[name='confirm']").unbind("click");
	$("input[name='confirm']").bind("click", confirmCoupon);

	$("input[name='search']").unbind("click");
	$("input[name='search']").bind("click", searchCoupon);

	$("a[name='plusCoupon']").unbind("click");
	$("a[name='plusCoupon']").bind("click", newCoupon);

	$("a[name='minuCoupon']").unbind("click");
	$("a[name='minuCoupon']").bind("click", removeCoupon);

	$("#allUser").on("change", function(){
		if($(this)[0].checked){
			$("#userList").text("");
			$("#userList").attr("disabled","true");
		}else{
			$("#userList").removeAttr("disabled");
		}
	});
	
	$("#cancel").click(function(){ 
		window.location.href = domain+'/coupon/auto/sendList.htm';
	});
	
	$( "#start_time" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
			// $( "#coupon_release_stime" ).datepicker( "option", "minDate", new Date() );
	    }
	});
	$( "#end_time" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	    }
	});
});


function getByteVal(val) {
	var byteValLen = 0; 
	for (var i = 0; i < val.length; i++) { 
	if (val[i].match(/[^\x00-\xff]/ig) != null) 
		byteValLen += 2; 
	else 
		byteValLen += 1; 
	} 
	return byteValLen; 
	} 

function newCoupon() {
	var rowHtml = "<tr><td style='width:80px'><a href='javascript:void(0);' name='plusCoupon' style='margin-right:10px;'> + </a>"
			+ "<a href='javascript:void(0);' name='minuCoupon'"
			+ "style='margin-right:10px;'> - </a></td>"
			+ "<td><span>优惠券批次:</span></td>"
			+ "<td style='width:120px'><input type='text' class='input-text lh30' style='width:80px' name='batchNo' /></td>"
			+ "<td style='width:300px'><input type='text' class='input-text lh30' name='batchName' /></td>"
			+ "<td>"
			+ "<input type='button' class='btn btn82 btn_save2' name='confirm' value='确定' />"
			+ "<input type='button' class='btn btn82 btn_search' name='search' value='查询' /></td></tr>";
	$("#couponList").append(rowHtml);

	$("input[name='confirm']").unbind("click");
	$("input[name='confirm']").bind("click", confirmCoupon);

	$("input[name='search']").unbind("click");
	$("input[name='search']").bind("click", searchCoupon);

	$("a[name='plusCoupon']").unbind("click");
	$("a[name='plusCoupon']").bind("click", newCoupon);

	$("a[name='minuCoupon']").unbind("click");
	$("a[name='minuCoupon']").bind("click", removeCoupon);
}
function removeCoupon() {
	var rowIndex = $("#couponList tr").length;
	if(null != rowIndex && 1 < parseInt(rowIndex)){
		$(this).closest("tr").remove();
	}
}
function searchCoupon() {
	var rowIndex = $(this).closest("tr").index();
	if (null == rowIndex || 0 > parseInt(rowIndex)) {
		layer.alert("数据异常");
		return;
	}
	$.layer({
		type : 2,
		title : '查询优惠券',
		shadeClose : true,
		maxmin : true,
		fix : false,
		area : [ '800px', 600 ],
		iframe : {
			src : SEARCH_COUPON + "?selectRow=" + rowIndex
		}
	});
}

function confirmCoupon() {
	var rowIndex = $(this).closest("tr").index();
	searchNameByBatchNo(rowIndex);
}

function searchNameByBatchNo(rowIndex){
	var row = $("#couponList tr")[rowIndex];
	var batchNo = $(row).children().find(
			"input[name='batchNo']").val();
	if(null == batchNo || $.trim(batchNo).length == 0){
		return;
	}
	$.get(SEARCH_COUPON_NAME, {
		couponId : batchNo
	}, function(data) {
		if (data.success) {
			$(row).children().find("input[name='batchName']")
					.val(data.data);
		} else {
			if(data.msg != null && "" != data.msg.message)
				layer.alert(data.msg.message);
			else
				layer.alert("查询优惠券批次信息失败");
		}
	});
}