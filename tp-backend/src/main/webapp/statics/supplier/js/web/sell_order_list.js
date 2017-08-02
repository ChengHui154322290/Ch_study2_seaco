function _gotoPage(index){
	jQuery("#pageIndexId").val(index);
	jQuery("#sell_order_list_form").submit();
}
jQuery(document).ready(function(){
	jQuery("#sell_order_list_add").click(function(){
		supplierShowTab("sell_order_list_add_btn","代销订单新增","/supplier/sellorderAdd.htm");
	});
	jQuery("#sellOrderListQuery").click(function(){
		jQuery("#sell_order_list_form").submit();
	});
	jQuery("#sellOrderListQueryReset").click(function(){
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
	supplierShowTab("sell_order_list_show_edit_btn","代销订单编辑","/supplier/sellOrderEdit.htm?purId="+spId);
	return false;
}

/**
 * 展示详情页面
 * 
 * @param spId
 * @returns {Boolean}
 */
function showDetailPage(spId){
	supplierShowTab("sell_order_list_show_detail_btn","代销订单详情","/supplier/sellOrderShow.htm?purId="+spId);
	return false;
}