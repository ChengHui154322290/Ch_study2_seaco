function _gotoPage(index){
	jQuery("#pageIndexId").val(index);
	jQuery("#sellback_order_list_form").submit();
}
jQuery(document).ready(function(){
	jQuery("#sell_orderback_list_add_btn").click(function(){
		supplierShowTab("sellback_order_list_add_btn","代销退货单新增","/supplier/sellorderbackAdd.htm");
	});
	jQuery("#sellOrderBackListQuery").click(function(){
		jQuery("#sell_orderback_list_form").submit();
	});
	jQuery("#sellOrderBackListQueryReset").click(function(){
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
	supplierShowTab("sellback_order_list_show_edit_btn","代销退货单编辑","/supplier/sellOrderBackEdit.htm?purId="+spId);
	return false;
}

/**
 * 展示详情页面
 * 
 * @param spId
 * @returns {Boolean}
 */
function showDetailPage(spId){
	supplierShowTab("sellback_order_list_show_detail_btn","代销退货单详情","/supplier/sellOrderBackShow.htm?purId="+spId);
	return false;
}