var pageii;
/**
 * list listExport
 */
function replyWorkOrder(id, workOrderNo) {
	var context = $("#context");
	if (!checkUpload())
		return;
	if (context.val() == '') {
		alert("描述不能为空！");
		return false;
	}
	$.ajax({
		cache : false,
		type : "POST",
		url : '/seller/workorder/insertlog.htm?workorderNo=' + workOrderNo,
		data : $('#showLogForm').serialize(),
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.resultInfo.type == "1") {
				alert("操作成功！");
				refreshTab('log_list_detail',
						'/seller/workorder/showinsertlog.htm?id=' + id);
				
			} else {
				alert("操作失败！");
			}
		}
	});

}
/**
 * 增加
 * 
 * @param oper
 */
function addWorkOrder(oper) {
	newTab("add_workorder", "工单详情", "/seller/workorder/showadd.htm");
}
/**
 * 查看详细
 * 
 * @param oper
 */
function showDetailPage(oper) {
	newTab("workorder_detail", "工单详情", "/seller/workorder/show.htm?id=" + oper);
	// location.href = 'show.htm?id=' + oper;
}
/**
 * 查看log
 * 
 * @param oper
 */
function showInsertLogPage(oper) {
	newTab("log_list_detail_123", "继续处理", "/seller/workorder/showinsertlog.htm?id="
			+ oper);
	// location.href = 'showinsertlog.htm?id=' + oper;
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
		url : domain + '/seller/workorder/change.htm?type=' + selectval,
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
	var orderno = $("#orderNo");
	var context = $("#context");

	if (orderno.val() == '') {
		alert("订单号不能为空！");
		return false;
	} else if (context.val() == '') {
		alert("描述不能为空！");
		return false;
	}
	if (!checkUpload())
		return;
	$.ajax({
		cache : false,
		type : "POST",
		url : '/seller/workorder/insert.htm',
		data : $('#workorderinfoinsertform').serialize(),
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.resultInfo.type == "1") {
				alert("操作成功！");
			} else {
				alert("操作失败！");
			}
		}
	});
}

/**
 * 关闭
 */
function closeWorkorder(status) {
	if (status == 3) {
		if (!confirm("确认正常关闭工单？")) {
			return;
		}
	} else if (status == 4) {
		if (!confirm("确认强制关闭工单？")) {
			return;
		}
	}
	$.ajax({
		cache : false,
		type : "POST",
		url : '/seller/workorder/close.htm?status=' + status,
		data : $('#showLogForm').serialize(),
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.resultInfo.type == "1") {
				refreshTab('log_list_detail',
						'/seller/workorder/showinsertlog.htm?id=' + id);
				alert("操作成功！");
			} else {
				alert("操作失败！");
			}
		}
	});
}
/**
 * 恢复关闭工单
 */
function openOrder() {
	$.ajax({
		cache : false,
		type : "POST",
		url : '/seller/workorder/open.htm',
		data : $('#workorderInfoForm').serialize(),
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.resultInfo.type == "1") {
				alert("操作成功！");
				refreshTab('workorder_detail', '/seller/workorder/show.htm?id='+ id);
			} else {
				alert("操作失败！");
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
		url : '/seller/workorder/update.htm',
		data : $('#showLogForm').serialize(),
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.resultInfo.type == "1") {
				refreshTab('log_list_detail',
						'/seller/workorder/showinsertlog.htm?id=' + id);
				alert("操作成功！");
			} else {
				alert("操作失败！");
			}
		}
	});
}

/**
 * 审核
 */
function auditWorkorder(audit, id) {
	$.ajax({
		cache : false,
		type : "POST",
		url : '/seller/workorder/audit.htm?audit=' + audit,
		data : $('#workorderInfoForm').serialize(),
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.resultInfo.type == "1") {
				refreshTab('workorder_detail', '/seller/workorder/show.htm?id='+ id);
				alert("操作成功！");
			} else {
				alert("操作失败！");
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
	$("#createDateEnd").datepicker(
			{
				dateFormat : 'yy-mm-dd',
				onClose : function(selectedDate) {
					$("#createDateBegin").datepicker("option", "maxDate",
							selectedDate);
				}
			});
});

function selectByOrderNo() {

}

/**
 * 关闭选择
 */
// $('#closeWorkorder').live('click', function() {
// pageii = $.layer({
// type : 2,
// title : '关闭选择',
// shadeClose : true,
// maxmin : false,
// fix : false,
// area : [ '250px', '200px' ],
// iframe : {
// src : domain + '/customerservice/workorder/close.htm'
// }
// });
// });

/**
 * 
 * @param page
 */
var workworkOrderListFnMap = null;
jQuery(function() {
	workOrderListFnMap = initQueryPage("workorderInfoForm","/seller/workorder/listload.htm", "table");
	workOrderListFnMap.loadPage(1);

	jQuery("#workOrderQueryId").click(function(e) {
		e.preventDefault();
		workOrderListFnMap.loadPage(1);

		return false;
	});
});

function _gotoPage(index) {
	workOrderListFnMap.gotoPage(index);
}
