var pageii;
$(function(){
	/**
	 * 父选子
	 * 全选/取消全选
	 */
	$('#checkAll').live('click',function() {
		if ($(this).attr('checked')) {
			$('.pgOrderCodeCheckbox').attr("checked", true);
		} else {
			$('.pgOrderCodeCheckbox').attr("checked", false);
		}
	});
	
	$('#dmImportCloseBtn').on('click', function(){
		parent.layer.close(pageii);
	});
	
	$('#downDMClearanceTemplateBtn').live('click',function(){
		$(this).attr('href','downDMClearanceTemplate.htm');
	});
	
	/**
	 * 上传附件
	 */
	$('.dmClearanceImport').live('click',function(){
		if(check()){
			$('#dmClearanceImportForm').attr('action',"uploadDMClearanceExcel.htm");			
			$('#dmClearanceImportForm').submit();
		}
	}); 
	
	$("#confirmImportBtn").live('click', function(){
		parent.layer.close(pageii);
	});
	
	$(".order-clearance-reset").click(function() {
		var orderCode = $(this).attr("data_code");
		var type = $(this).attr("data_type");
		$.ajax({
			url : domain + '/order/reset_clearance.htm',
			type : 'get',
			data : "subCode=" + orderCode + "&type=" + type,				
			cache : false,
			success : function(data) {
				if (data.success) {
					alert('重置成功', 8);
					window.location.reload();
				} else {
					alert(data.msg.message, 8);
				}
			}
		});	
	});
});

//查看
function view(type) {
	if(type == 'rest'){
		showTab(type + "_log_list_show", "RESTful请求日志列表", "/sys/log/restloglist.htm");	
	}else if(type == 'api'){
		showTab(type + "_log_list_show", "API请求日志列表", "/sys/log/apiloglist.htm");
	}
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

function directMailClearance(){
	var pgCodeArry = new Array();
	$(".pgOrderCodeCheckbox").each(function(){
		if ($(this).attr('checked')) {
			pgCodeArry.push($(this).val());
		}
	});
	
	//校验
	if(pgCodeArry.length==0){
		alert("请先选择需要报关的清单");
		return;
	}
	
	var pgOrderCodes = pgCodeArry.join(",");
	var src = domain+'/order/customs/directmailClearancePreview.htm?ordercodes='+pgOrderCodes; 
	pageii=$.layer({
        type : 2,
        title: '批量-直邮报关',
        shadeClose: true,
        maxmin: true,
        fix : false,  
        area: ['950px', 800],                     
        iframe: {
            src : src
        } 
    });
}


function dmBatchDelivery(){
	var pgorderCodeArry = new Array();
	$(".pgOrderCodeCheckbox").each(function(){
		if ($(this).attr('checked')) {
			pgorderCodeArry.push($(this).val());
		}
	});
	if(pgorderCodeArry.length==0){
		alert("请先选择需要清关的订单");
		return ;
	}
	var voyageNo = $.trim($("#voyageNo").val());
	if (voyageNo == null || voyageNo == ''){
		alert("航班号不能为空");
		return;
	}
	var billNo = $.trim($("#billNo").val());
	if (billNo == null || billNo == ''){
		alert("总提运单号不能为空");
		return;
	}
	var trafNo = $.trim($("#trafNo").val());
	if (trafNo == null || trafNo == ''){
		alert("运输工具编号不能为空");
		return;
	}
	var ordercodes = pgorderCodeArry.join(",");
	
	$.ajax({ 
		url: domain+'/order/customs/directmailClearance.htm?ordercodes=' + ordercodes, 
		dataType: 'json',
		data: $('#directMailDeliveryForm').serialize(), 
		type: "post", 
		cache : false, 
		success: function(data) {
			if(data.success){
				layer.alert('操作成功', 8);
			}else{
				layer.alert(data.msg.message, 8);
			}
		}	
  });
}

function directMailClearanceImport(){
	var src = domain+'/order/customs/directmailClearanceImport.htm'; 
	pageii=$.layer({
        type : 2,
        title: '批量-直邮报关',
        shadeClose: true,
        maxmin: true,
        fix : false,  
        area: ['850px', 600],                     
        iframe: {
            src : src
        } 
    });
}

function check(){
	var fileName = $("#dmClearanceExcel").val();
	if($.trim(fileName)==''){
		alert("请选择导入模板");
	    return false;
	}
	if(fileName.lastIndexOf(".")!=-1){
		var fileType = (fileName.substring(fileName.lastIndexOf(".")+1,fileName.length)).toLowerCase();
	    var suppotFile = new Array();
	    suppotFile[0] = "xls";
	    suppotFile[1] = "xlsx";
	    for(var i =0;i<suppotFile.length;i++){
	       if(suppotFile[i]==fileType){
	    	   return true;
	       }else{
	    	   continue;
	       }
	    }
	    alert("不支持文件类型"+fileType);
		return false;
	 }
}