<#include "/common/common.ftl"/>
<@backend title="编辑供应商" js=[
    '/statics/supplier/js/jquery.form.js',
    '/statics/supplier/js/common.js',
    '/statics/supplier/js/validator.js',
    '/statics/supplier/js/image_upload.js',
    '/statics/supplier/js/web/supplier_add_licen.js',
    '/statics/supplier/js/web/supplier_add.js',
    '/statics/supplier/js/web/supplier_edit.js',
    '/statics/qiniu/js/plupload/plupload.full.min.js',
    '/statics/qiniu/js/plupload/plupload.dev.js',
    '/statics/qiniu/js/plupload/moxie.js',
	'/statics/qiniu/js/plupload/moxie.js',
	'/statics/qiniu/src/qiniu.js',
	'/statics/qiniu/js/highlight/highlight.js',
	'/statics/qiniu/js/ui.js',
	'/statics/backend/js/uplod.js'
    ] 
    css=[] >
<style type="text/css">
.pb15 {
    padding-bottom: 0px;
}
.container {
    margin-left: unset;
}
</style>
<script>
   var isSupplierEdit = true;
   	var bucketURL = '${bucketURL}';
	var bucketname = '${bucketname}';
</script>
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_center" style="margin-bottom: 30px;">
                <div class="box_top"><b class="pl15">基本信息</b></div>
                <#include "/supplier/subpage/sp_com_supplier_num.ftl" />
                <form class="jqtransform" id="supplierEditForm" method="post" enctype="multipart/form-data" action="/supplier/supplierEditSave.htm">
                    <#include "/supplier/subpage/sp_com_base.ftl" />
                    <div class="box_top"><b class="pl15">附件信息</b></div>
                    <#include "/supplier/subpage/sp_com_licen_1.ftl" />
                    <input type="hidden" name="spId" value="${(supplierVO.id)!}" />
                    <input type="hidden" name="statusAudit" value="" id="statusValId2" />
                </form>
                <form class="jqtransform" id="supplierAuditForm" method="post" enctype="multipart/form-data" action="/supplier/supplierAuditSave.htm">
                    <input type="hidden" name="spIdAudit" value="${(supplierVO.id)!}" />
                    <input type="hidden" name="status" value="submit" id="statusValId" />
                </form>
                <#include "/supplier/subpage/sp_com_licen_2.ftl" />
                <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                    <tbody>
                        <tr>
                            <td class="td_right"></td>
                            <td>
                                <#if supplierVO.auditStatus==0>
                                <#elseif supplierVO.auditStatus == 4>
                                    <input type="button" id="supplierAuditEditSubBtn" value="提交" class="btn btn82 btn_save2" name="button">
                                    <#-- <input type="button" id="supplier_add_cancel" value="取消" class="btn btn82 btn_save2" name="button">-->
                                <#elseif supplierVO.auditStatus == 1>
                                    <input type="button" id="supplierEditSaveBtn" value="保存" class="btn btn82 btn_save2" name="button">
                                    <input type="button" id="supplierEditCancelBtn" value="取消" class="btn btn82 btn_nochecked" name="button">
                                    <input type="button" id="supplierEditSubBtn" value="提交" class="btn btn82 btn_save2" name="button">
                                <#elseif supplierVO.auditStatus == 5>
                                    <input type="button" id="supplierEditSubBtn" value="提交" class="btn btn82 btn_save2" name="button">
                                    <input type="button" id="supplierEditCancelBtn" value="取消" class="btn btn82 btn_nochecked" name="button">
                                <#elseif supplierVO.auditStatus == 6>
                                    <input type="button" id="supplierEdit_canceled" value="提交" class="btn btn82 btn_save2" name="button">
                                <#else>
                                </#if>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div style="display:none;">
	<select id="brandoptionselectid">
		<option value="">请选择 </option>
		<#if brandOption??>
		<#list brandOption as supplierCategory>
			<option value="${supplierCategory.brandName}">${supplierCategory.brandName}</option>
		</#list>
		</#if>
	</select>
	<select id="customsChannelListid">
		<option value="">请选择 </option>
		<#if customsChannelList??>
		<#list customsChannelList as clearanceChannels>
			<option value="${clearanceChannels.id}">${clearanceChannels.name}</option>
		</#list>
		</#if>
	</select>
	<select id="supplierBankTypes">
		<option value="">请选择 </option>
		<#if supplierBankTypes??>
		<#list supplierBankTypes as supplierBankType>
			<option value="${supplierBankType.value}">${supplierBankType.name}</option>
		</#list>
		</#if>
	</select>
	<select id="departmentList">
		<option value="">请选择 </option>
		<#if departmentList??>
		<#list departmentList as department>
			<option value="${department.id}">${department.name}</option>
		</#list>
		</#if>
	</select>
	<select id="categorysOption">
		<option value="">请选择 </option>
		<#if categorys??>
		<#list categorys as category>
			<option value="${category.id}">${category.name}</option>
		</#list>
		</#if>
	</select>
</div>
</@backend>