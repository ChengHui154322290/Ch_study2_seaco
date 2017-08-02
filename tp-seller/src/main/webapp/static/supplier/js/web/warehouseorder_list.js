function _gotoPage(index){
	jQuery("#pageIndexId").val(index);
	jQuery("#warehouseorder_list_form").submit();
}
jQuery(document).ready(function(){
	jQuery("#warehouseorder_list_add").click(function(){
		supplierShowTab("warehouseorder_list_add_btn","添加仓库预约单","/supplier/warehouseorderAdd.htm");
	});
	jQuery("#warehouseorderListQuery").click(function(){
		jQuery("#warehouseorder_list_form").submit();
	});
	jQuery("#warehouseorderListQueryReset").click(function(){
		$(":input[type='text']").attr("value","");
		$("select").find('option:selected').removeAttr('selected');
	});
	
});


/**
 * 展示详情页面
 * 
 * @param spId
 * @returns {Boolean}
 */
function showDetailPage(spId){
	supplierShowTab("warehouse_list_show_detail_btn","预约单查看","/supplier/warehouseOrderDetail.htm?spId="+spId);
	return false;
}