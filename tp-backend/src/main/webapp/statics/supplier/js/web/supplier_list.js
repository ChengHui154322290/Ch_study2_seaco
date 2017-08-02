/**
 * 校验品牌名称
 */
function checkBrandName(){
	var brandname = jQuery("#sp_brandname").val();
	var checkbrandname = /[, ]/;
	if(checkbrandname.test(brandname)){
		alertMsg("品牌查询条件不能有非法字符");
		return false;
	}
	return true;
}


/**
 * 到供应商编辑页面
 */
function toEditPage(supplierId){
	jumpToPage("/supplier/supplierEdit.htm?spId="+supplierId);
}
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
	jQuery("#supplier_list_form").submit();
}

$(function(){
	//window.parent.popWaitDivMap.hideWaitDiv();
	$("#supplier_list_add").click(function(){
		supplierShowTab("supplier_list_add_btn","添加供应商","/supplier/supplierAdd.htm");
		//jumpToPage("/supplier/supplierAdd.htm");
	});
	$("#supplierListQuery").click(function(){
		//校验品牌条件
		if(!checkBrandName()){
			return;
		}
		$("#supplier_list_form").submit();
	});
	
	$("#supplierListQueryReset").click(function(){
		$(":input[type='text']").attr("value","");
		$("select").find('option:selected').removeAttr('selected');
	});
});

/**
 * 展示附件上传页面
 */
function showLicenEditPage(spId){
	supplierShowTab("supplier_list_show_edit_btn","附件上传","/supplier/toSupplierLicenAdd.htm?spId="+spId);
	return false;
}

/**
 * 弹出编辑页面
 * 
 * @param spId
 * @returns {Boolean}
 */
function showEditPage(spId){
	supplierShowTab("supplier_list_show_edit_btn"+spId,"供应商编辑","/supplier/supplierEdit.htm?spId="+spId);
	return false;
}

/**
 * 展示详情页面
 * 
 * @param spId
 * @returns {Boolean}
 */
function showDetailPage(spId){
	supplierShowTab("supplier_list_show_detail_btn"+spId,"供应商查看","/supplier/supplierDetail.htm?spId="+spId);
	return false;
}

