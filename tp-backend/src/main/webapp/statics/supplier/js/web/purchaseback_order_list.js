function _gotoPage(index){
	jQuery("#pageIndexId").val(index);
	jQuery("#purchase_orderback_list_form").submit();
}
jQuery(document).ready(function(){
	jQuery("#purchase_orderback_list_add_btn").click(function(){
		supplierShowTab("purchase_orderback_list_add_btn","新增采购退货单","/supplier/purchaseorderbackAdd.htm");
	});
	jQuery("#purchaseOrderBackListQuery").click(function(){
		jQuery("#purchase_orderback_list_form").submit();
	});
	jQuery("#purchaseOrderBackListQueryReset").click(function(){
		$(":input[type='text']").attr("value","");
		$("select").find('option:selected').removeAttr('selected');
	});
});

/**
 * 弹出编辑页面
 * 
 * @param spId
 * @returns {Boolean}
 */
function showEditPage(spId){
	supplierShowTab("purOrderBack_list_show_edit_btn","采购退货单编辑","/supplier/purchaseOrderBackEdit.htm?purId="+spId);
	return false;
}

/**
 * 展示详情页面
 * 
 * @param spId
 * @returns {Boolean}
 */
function showDetailPage(spId){
	supplierShowTab("purOrderBack_list_show_detail_btn","采购退货单详情","/supplier/purchaseOrderBackShow.htm?purId="+spId);
	return false;
}