/**
 * 出一个tab
 */
function supplierShowTab(id,text,tabUrl){
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




function _gotoPage(index){
	jQuery("#pageIndexId").val(index);
	jQuery("#quotation_list_form").submit();
}
$(function(){
	$("#quotation_list_add").click(function(){
		supplierShowTab("quotation_list_add_btn","新增报价单","/supplier/quotationAdd.htm");
	});
	
	$("#quotation_list_import").click(function(){
		supplierShowTab("quotation_list_import_btn","导入报价单","/supplier/quotationImport.htm");
	});
	
	$("#quotationListQuery").click(function(){
		$("#quotation_list_form").submit();
	});
	$("#quotationListQueryReset").click(function(){
		$(":input[type='text']").attr("value","");
		$("select").find('option:selected').removeAttr('selected');
	});
		
	// 导出
	$(".btn_export").click(function() {
		//$.post(domain + "/supplier/quotationExport.htm",$("#quotation_list_form").serialize());
		 window.open(domain + "/supplier/quotationExport.htm" + "?" + $("#quotation_list_form").serialize());
	});	
		
});

/**
 * 弹出编辑页面
 * 
 * @param spId
 * @returns {Boolean}
 */
function showEditPage(spId){
	supplierShowTab("quotation_list_show_edit_btn"+spId,"报价单编辑","/supplier/quotationEdit.htm?quoId="+spId);
	return false;
}

/**
 * 展示详情页面
 * 
 * @param spId
 * @returns {Boolean}
 */
function showDetailPage(spId){
	supplierShowTab("quotation_list_show_detail_btn"+spId,"报价单详情","/supplier/quotationShow.htm?quoId="+spId);
	return false;
}