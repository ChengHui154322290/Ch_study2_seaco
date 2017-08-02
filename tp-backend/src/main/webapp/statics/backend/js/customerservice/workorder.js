var pageii;
/**
 * list listExport
 */
function itemFormSubmit(oper) {
	if (oper == "list") {
		$('#workorderInfoForm').attr("action", "list.htm");
		$('#workorderInfoForm').submit();
	} else if (oper == "insertLog") {
		
		 var context = $("#context");
		
		 if(context.val() == ''){
			 alert("描述不能为空！");
			 return false;
		 }
		
		if (!checkUpload())
			return;
		
		$('#showLogForm').attr("action", "insertlog.htm");
		$('#showLogForm').submit();
	}
}
/**
 * 新增
 * @param oper
 */
function addWorkOrder(){
	var tv={
		url:'/customerservice/workorder/showadd.htm',
		text:'新增工单'
	};
	parent.window.showTab(tv);
}

/**
 * 查看详情
 * @param oper
 */
function showDetailPage(oper){
//	location.href='show.htm?id=' + oper;
	var date=new Date();
	var tv={
		url:'/customerservice/workorder/show.htm?id=' + oper,
		linkid:'showDetailPage'+date.getMilliseconds(),
		tabId:'showDetailPage'+date.getMilliseconds(),
		text:'工单详情'
	};
	parent.window.showTab(tv);
}

/**
 * 查看log
 * 
 * @param oper
 */
function showInsertLogPage(oper) {
//	location.href='showinsertlog.htm?id=' + oper;
	var date=new Date();
	var tv={url:'/customerservice/workorder/showinsertlog.htm?id=' + oper,
			linkid:'showInsertLogPage'+date.getMilliseconds(),
			tabId:'showInsertLogPage'+date.getMilliseconds(),
			text:'继续处理'
	};
	parent.window.showTab(tv);
}
/**
 * 下拉列表联动
 * 
 * @param selectval
 */
function selectReason(selectval) {
	$.ajax({
		cache : false,
		type : "POST",
		url : 'change.htm?type=' + selectval,
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			var reason = $("#reason");
			reason.empty();

			$.each(data,
					function(n, value) {
						var option = $("<option>").text(value.reason).val(
								value.reason);
						reason.append(option);
					});

		}
	});

}

/**
 * workorder添加
 */
function workorderSaveSubmit() {
	 var orderno = $("#orderno");
	 var context = $("#context");
	 var bizType = $("#bizType");
	 if(orderno.val() == '') {
		 alert("订单号不能为空！");
		 return false;
	 } else if(context.val() == ''){
		 alert("描述不能为空！");
		 return false;
	 }else if(bizType==0){
		 alert("请选择业务类型！");
		 return false;
	 }
	if (!checkUpload())
		return;
	$.ajax({
		cache : false,
		type : "POST",
		url : 'insert.htm',
		data : $('#workorderinfoinsertform').serialize(),
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.resultInfo.type == "1") {
				layer.alert(data.resultInfo.message, 1, function() {
					location.href = 'list.htm';
					parent.layer.close(parent.pageii);
				})
			} else {
				layer.alert(data.resultInfo.message, 8);
			}
		}
	});
}



/**
 * 关闭
 */
function closeWorkorder(status) {
	if(status==3){
		if (!confirm("确认正常关闭工单？")) {
			return;
		}
	}else if(status==4){
		if (!confirm("确认强制关闭工单？")) {
			return;
		}
	}
	$.ajax({
		cache : false,
		type : "POST",
		url : 'close.htm?status='+status,
		data : $('#showLogForm').serialize(),
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.resultInfo.type == "1") {
				layer.alert(data.resultInfo.message, 1, function() {
					location.href = 'list.htm';
					parent.layer.close(parent.pageii);
				})
			} else {
				layer.alert(data.resultInfo.message, 8);
			}
		}
	});
}
/**
 * 恢复关闭工单
 */
function openOrder(){
	$.ajax({
		cache : false,
		type : "POST",
		url : 'open.htm',
		data : $('#workorderInfoForm').serialize(),
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.resultInfo.type == "1") {
				layer.alert(data.resultInfo.message, 1, function() {
					location.href = 'list.htm';
					parent.layer.close(parent.pageii);
				})
			} else {
				layer.alert(data.resultInfo.message, 8);
			}
		}
	});
}

/**
 * 切换租
 */
function updateWorkorder(id) {
	var groupId = $("#groupId");
	if (groupId.val() == 0) {
		alert("请选择组别！");
		return false;
	}
	$.ajax({
		cache : false,
		type : "POST",
		url : 'update.htm',
		data : $('#showLogForm').serialize(),
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.resultInfo.type == "1") {
				layer.alert(data.resultInfo.message, 1, function() {
					location.href = 'list.htm';
					parent.layer.close(parent.pageii);
				})
			} else {
				layer.alert(data.resultInfo.message, 8);
			}
		}
	});
}

/**
 * 审核
 */
function auditWorkorder(audit) {
	$.ajax({
		cache : false,
		type : "POST",
		url : 'audit.htm?audit=' + audit,
		data : $('#workorderInfoForm').serialize(),
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.resultInfo.type == "1") {
				layer.alert(data.resultInfo.message, 1, function() {
					location.href = 'list.htm';
					parent.layer.close(parent.pageii);
				})
			} else {
				layer.alert(data.resultInfo.message, 8);
			}
		}
	});
}

$(function() {
	$("#createDateBegin").datepicker({
		dateFormat : 'yy-mm-dd',
		onClose : function(selectedDate) {
			$("#createDateEnd").datepicker("option", "minDate", selectedDate);
		}
	});
	$("#createDateEnd").datepicker({
		dateFormat : 'yy-mm-dd',
		onClose : function(selectedDate) {
			$("#createDateBegin").datepicker("option", "maxDate",selectedDate);
		}
	});
});

function selectByOrderNo(){
	
}

/**
 * 关闭选择
 */
//$('#closeWorkorder').live('click', function() {
//	pageii = $.layer({
//		type : 2,
//		title : '关闭选择',
//		shadeClose : true,
//		maxmin : false,
//		fix : false,
//		area : [ '250px', '200px' ],
//		iframe : {
//			src : domain + '/customerservice/workorder/close.htm'
//		}
//	});
//});

//查看
var VIEW_URL = domain + "/order/view.htm";
function view(code) {
	showTab("order_list_show_detail_btn", "订单查看", VIEW_URL + "?code=" + code);
	return false;
}

function showTab(id, text, url) {
	var tv = {};
	tv.linkId = id+"_link";
	tv.tabId =  id;
	tv.url = url;
	tv.text = text;
	try{
		window.parent.showTab(tv);
	} catch(e){
	}
}