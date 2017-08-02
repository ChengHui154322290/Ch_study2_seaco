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
	jQuery("#purchase_order_list_form").submit();
}
jQuery(document).ready(function(){
	jQuery("#purchase_order_list_add").click(function(){
		supplierShowTab("purchase_order_list_add_btn","新增采购单","/supplier/purchaseorderAdd.htm");
	});
	jQuery("#purchaseOrderListQuery").click(function(){
		jQuery("#purchase_order_list_form").submit();
	});
	jQuery("#purchaseOrderListQueryReset").click(function(){
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
	supplierShowTab("purchase_order_list_show_edit_btn","采购订单编辑","/supplier/purchaseOrderEdit.htm?purId="+spId);
	return false;
}

/**
 * 展示详情页面
 * 
 * @param spId
 * @returns {Boolean}
 */
function showDetailPage(spId){
	supplierShowTab("purchase_order_list_show_detail_btn","采购订单详情","/supplier/purchaseOrderShow.htm?purId="+spId);
	return false;
}