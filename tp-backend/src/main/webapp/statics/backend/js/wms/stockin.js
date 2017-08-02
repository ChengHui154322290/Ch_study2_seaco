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
	
	
	
	$("#doImportStockInBtn").click(function() {
		var stockInFile = $('#stockInFile').val();
		if(stockInFile == "") {
			alert("请选择要上传的文件!");
			return false;
		}
		$('#fileName').val(stockInFile);
		$('#stockInFileForm').submit();
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

function uploadFile(id) {
	pageii=$.layer({
        type : 2,
        title: '入库单明细导入',
        shadeClose: true,
        maxmin: true,
        fix : false,  
        area: ['500px','300px', 300],                     
        iframe: {
        	src : domain+'/wms/stockin/importStockIn.htm?stockasnId='+id
        }
	});
//	addTab("uploadFileBtn","入区信息管理-导入入区明细","/wms/stockin/importStockIn.htm");
};

function viewItem(id){
	addTab("view_stock_in_item"+id,"入库信息管理-查看入库详情","/wms/stockin/viewItem.htm?stockasnId="+id);
	return false;
}
function viewFact(id){
	addTab("view_stock_in_fact"+id,"入库信息管理-查看入库回执","/wms/stockin/fact.htm?stockasnId="+id);
	return false;
}
function viewDetail(id){
	addTab("view_stock_in_detailFact"+id,"入库信息管理-查看入库回执明细","/wms/stockin/detailFact.htm?stockasnFactId="+id);
	return false;
}
