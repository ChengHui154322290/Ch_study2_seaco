/**
 * 分页跳转
 * 
 * @param index
 */
function _gotoPage(index){
	jQuery("#pageIndexId").val(index);
	jQuery("#examination_list_form").submit();
}

/**
 * 跳转到编辑页面
 * @param auditId
 */
function toEditPage(auditId){
	supplierShowTab("examination_list_edit_btn","编辑审批流","/supplier/examinationEdit.htm?id="+auditId);
}

jQuery(document).ready(function(){
	jQuery("#examination_list_add").click(function(){
		supplierShowTab("examination_list_add_btn","添加审批流","/supplier/examinationAdd.htm");
	});
	jQuery("#examinationListQuery").click(function(){
		jQuery("#examination_list_form").submit();
	});
});