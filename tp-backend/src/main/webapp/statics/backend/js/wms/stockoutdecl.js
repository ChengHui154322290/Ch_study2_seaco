$(function() {

	$("#list_table tr:even").css("background-color", "#ecf6fc");
	
	$("#list_table tr:even").attr("bg", "#ecf6fc");
	$("#list_table tr:odd").attr("bg", "#ffffff");
	
	$("#list_table tr").mouseover(function() {
		$(this).css("background", "#bcd4ec");
	}).mouseout(function() {
		var bgc = $(this).attr("bg");
		$(this).css("background", bgc);
	});
	
	$( "#startCreateTimeQo" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	    }
	});
	
	$( "#endCreateTimeQo" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	    }
	});
	
	$("#uploadFileBtn").click(function() {
		addTab("uploadFileBtnOutDecl","出库报检管理-导入报检明细","/wms/stockoutdecl/importStockOutDecl.htm");
	});
	
	$("#doImportStockOutDeclBtn").click(function() {
		var storer = $('#storer').val();
		var externalNo = $('#externalNo').val();
		var externalNo2 = $('#externalNo2').val();
		var stockOutDeclFile = $('#stockOutDeclFile').val();
		if(storer == "") {
			alert("请填写货主编码!");
			return false;
		}
		if(externalNo == "") {
			alert("请填写跨境平台系统订单号!");
			return false;
		}
		if(externalNo2 == "") {
			alert("请填写外部订单号!");
			return false;
		}
		if(stockOutDeclFile == "") {
			alert("请选择要上传的文件!");
			return false;
		}
		$('#stockOutDeclFileForm').submit();
	});
	
});

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

function viewItem(id){
	addTab("view_stock_out_item"+id,"入库报检管理-查看入库详情","/wms/stockoutdecl/viewItem.htm?id="+id);
	return false;
}
