var pageii;
$(function() {
	//列表状态下拉框设置选中
	var status=$("#statusSelected").val();
	$("#status").find("option[value='"+status+"']").attr("selected",true);
	
	$("#list_table tr:even").css("background-color", "#ecf6fc");
	
	$("#list_table tr:even").attr("bg", "#ecf6fc");
	$("#list_table tr:odd").attr("bg", "#ffffff");
	
	$("#list_table tr").mouseover(function() {
		$(this).css("background", "#bcd4ec");
	}).mouseout(function() {
		var bgc = $(this).attr("bg");
		$(this).css("background", bgc);
	});

//	$(":text[name='startCreateTime']").datetimepicker();
//	$(":text[name='endCreateTime']").datetimepicker();

	/*
	$( "#startCreateTime" ).datepicker({
		dateFormat:'yy-mm-dd HH:mm:ss',
		onClose: function( selectedDate ) {
	    }
	});
	
	$( "#endCreateTime" ).datepicker({
		dateFormat:'yy-mm-dd HH:mm:ss',
		onClose: function( selectedDate ) {
	    }
	});
	*/
	
	
	$('#updateStatus').live('click',function(){
		var ids = getIds("stockoutCheck");
		if(ids == '') {
			alert("请至少选择一条记录！");
			return false;
		}
		pageii=$.layer({
        type : 2,
        title: '选择状态',
        shadeClose: true,
        maxmin: true,
        fix : false,  
        area: ['500px','200px', 300],                     
        iframe: {
        	src : domain+'/wms/stockout/updateStockOutStatus.htm?ids='+ids
        }
      });
	}); 

	$('#doUpdateStockOutStatus').live('click', function() {
		var statusText = $("#selectstatus").find("option:selected").text(); 
		if(confirm("确定将选中的记录更新为《"+statusText+"》状态吗?")) {
			// alert("更新了。。。");
			var ids = $('#ids').val();
			var status = $("#selectstatus").val();
			if(status == "17") {	// 提示选择快递公司
				window.parent.$("#selectIds").val(ids);
				window.parent.$("#selectExpressBtn").trigger('click');
				var pageiframe = parent.layer.getFrameIndex(window.name);
				parent.layer.close(pageiframe);
			} else {
				var tUrl = domain+"/wms/stockout/doUpdateStockOutStatus.htm?ids="+ids+"&status="+status;
				// alert(tUrl);
				var htmlobj = $.ajax({url:tUrl, async:false, type:"POST"});
				var ly = layer.alert("操作成功！", 1, function(){
		    		layer.close(ly);
		    		// location.reload();
		    		var pageiframe = parent.layer.getFrameIndex(window.name);
		    		parent.location.reload();
		    		parent.layer.close(pageiframe);
		    	});
			}
		}
	});
	
	$('#selectExpressBtn').live('click',function(){
		var ids = $("#selectIds").val();
		pageii=$.layer({
	        type : 2,
	        title: '选择快递公司',
	        shadeClose: true,
	        maxmin: true,
	        fix : false,  
	        area: ['900px','500px', 300],                     
	        iframe: {
	        	src : domain+'/wms/stockout/selectExpressInfo.htm?ids='+ids
	        }
		});
	}); 

	$("#checkALL").click(function() {
		if ($(this).attr("checked")) {
			$("input[name='stockoutCheck']").attr("checked", "true");
		} else {
			$("input[name='stockoutCheck']").removeAttr("checked"); 
		}
	});
	
//	$('#exportBtn').live('click', function() {
//		pageii=$.layer({
//	        type : 2,
//	        title: '选择导出列',
//	        shadeClose: true,
//	        maxmin: true,
//	        fix : false,  
//	        area: ['900px','300px', 300],                     
//	        iframe: {
//	        	src : domain+'/wms/stockout/exportConfig.htm'
//	        }
//		});
//	});
	
	function getIds(checkName) {
		var ids = '';
		$("input[name='"+checkName+"']:checkbox:checked").each(function() {
			ids += $(this).val()+',';
		});
		return ids;
	}
	
	$('#doExportBtn').live('click', function() {
		var colCodes = getIds("beanCodeCol");
		window.location.href = domain+"/wms/stockout/doExport.htm?colCodes="+colCodes;
		var ly = layer.alert("导出成功！", 1, function(){
			layer.close(ly);
			var pageiframe = parent.layer.getFrameIndex(window.name);
			parent.layer.close(pageiframe);
		});
	});
	
	$('#importBtn').live('click', function() {
		pageii=$.layer({
	        type : 2,
	        title: '出库单管理-运单导入',
	        shadeClose: true,
	        maxmin: true,
	        fix : false,  
	        area: ['600px','300px', 300],                     
	        iframe: {
	        	src : domain+'/wms/stockout/importExpress.htm'
	        }
		});
	});
	
	$('#doImportExpressBtn').live('click', function() {
		var expressExcel = $('#expressExcel').val();
		if(expressExcel == "") {
			alert("请选择要上传的文件!");
			return false;
		}
		$("#doImportExpressForm").ajaxSubmit({
			beforeSubmit:function(XMLHttpRequest){
				$('#importMsgDiv').html("正在导入，请稍候...");
			},
			dataType:'text',
			success:function(data){
				$('#importMsgDiv').html(data);
			},
			error:function(XMLHttpRequest, textStatus){
				$('#importMsgDiv').html("网络或系统异常");
			},
			complete:function(XMLHttpRequest, textStatus){
		    }
		});
	});
	
});

function doUpdateStockOutStatusByExpress(allSize) {
	// alert(allSize);
	var msg = "";
	for (var i=0; i<allSize; i++) {
		var tExpressId = document.getElementById("stockOutList["+i+"].expressId").value;
		if(tExpressId == "") {
			msg += "第"+(i+1)+"行，订单编号："+document.getElementById("stockOutList["+i+"].externalNo").value+"，所对应的运单号为空！</br>";
		}
	}
	if(msg != "") {
		$('#importMsgDiv').html(msg);
		return false;
	}
	
	var expressCode = $("#expressInfoId").val();
	var expressName = $("#expressInfoId").find("option:selected").text(); 
	$("#expressCode").val(expressCode);
	$("#expressName").val(expressName);
	if(confirm("您选择的快递公司编号为："+expressCode+"，名称为："+expressName+"，确定更新吗?")) {
		// $("#doUpdateStockOutStatusByExpressForm").ajaxSubmit({
			// beforeSubmit:function(XMLHttpRequest){
				// $('#importMsgDiv').html("正在导入，请稍候...");
			// },
			// dataType:'text',
			// success:function(data){
				// $('#importMsgDiv').html(data);
			// },
			// error:function(XMLHttpRequest, textStatus){
				// $('#importMsgDiv').html("网络或系统异常");
			// },
			// complete:function(XMLHttpRequest, textStatus){
		    // }
		// });
		$.ajax({
			url: $("#doUpdateStockOutStatusByExpressForm").attr("action"),
			data: $("#doUpdateStockOutStatusByExpressForm").serialize(),
			beforeSend:function(XMLHttpRequest){
				$('#importMsgDiv').html("正在导入，请稍候...");
			},
			dataType:'text',
			type:'post',
			success:function(data){
				$('#importMsgDiv').html(data);
			},
			error:function(XMLHttpRequest, textStatus){
				$('#importMsgDiv').html("网络或系统异常");
			},
			complete:function(XMLHttpRequest, textStatus){
		    }
		});

	}
}

function addTab(id,text,tabUrl){
	var tv = {};
	tv.linkId = id+"_link";
	tv.tabId =  id;
	tv.url = tabUrl;
	tv.text = text;
	try{
		window.parent.showTab(tv);
	} catch(e){
	}
}
/*
 * 发货单明细
 */
function viewItem(id){
	addTab("view_stock_out_item"+id,"出库单管理-查看出库详情","/wms/stockout/viewItem.htm?id="+id);
	return false;
}
/*
 * 发货单发票信息
 */
function viewInvoice(id){
	addTab("view_stock_out_item"+id,"出库单管理-查看发票信息","/wms/stockout/viewInvoice.htm?id="+id);
	return false;
}

/*
 * 发货单信息导出
 */
$("#exportBtn").live('click', function() {
	var orderCode = $("#orderCode").val();
	var expressNo = $("#expressNo").val();
	var consignee = $("#consignee").val();
	var mobile = $("#mobile").val();
	var status = $("select option:checked").val();
	window.location.href = domain+"/wms/stockout/doExport.htm?orderCode="+orderCode
	+"&expressNo="+expressNo+"&consignee="+encodeURI(encodeURI(consignee))+"&mobile="+mobile+"&status="+status;
//	var stockout = {
//			'orderCode':orderCode,
//			'expressNo':expressNo,
//			'consignee':consignee,
//			'mobile':mobile,
//			'status':status
//	}
});




