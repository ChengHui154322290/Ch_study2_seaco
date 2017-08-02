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
	jQuery("#contract_list_form").submit();
}

$(function(){
	jQuery("#contract_list_add").click(function(){
		supplierShowTab("contract_list_add_btn","新增合同", "/supplier/contract/add.htm");
	});
	jQuery("#contractListQuery").click(function(){
		jQuery("#contract_list_form").submit();
	});
	jQuery("#contractListQueryReset").click(function(){
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
	supplierShowTab("contract_list_show_edit_btn","合同编辑", "/supplier/contract/edit.htm?cId="+spId);
	return false;
}

/**
 * 展示详情页面
 * 
 * @param spId
 * @returns {Boolean}
 */
function showDetailPage(spId){
	supplierShowTab("contract_list_show_detail_btn","合同详情", "/supplier/contract/show.htm?cId="+spId);
	return false;
}
