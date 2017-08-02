<#include "/common/common.ftl"/>
<@backend title="编辑供应商" js=[
    '/statics/supplier/js/jquery.form.js',
    '/statics/supplier/js/common.js',
    '/statics/supplier/js/validator.js',
    '/statics/supplier/js/image_upload.js',
    '/statics/supplier/js/web/supplier_add_licen.js',
    '/statics/supplier/js/web/supplier_add.js',
    '/statics/supplier/js/web/supplier_edit.js'
    ] 
    css=[] >
<style type="text/css">
.pb15 {
    padding-bottom: 0px;
}
</style>
<script>
   var isSupplierEdit = false;
</script>
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_center" style="margin-bottom: 30px;">
                <div class="box_top"><b class="pl15">基本信息</b></div>
                <#include "/supplier/subpage/sp_com_supplier_num.ftl" />
                <#include "/supplier/subpage/show/sp_com_base_show.ftl" />
                <div class="box_top"><b class="pl15">附件信息</b></div>
                <#include "/supplier/subpage/show/sp_com_licen_1_show.ftl" />
                <#include "/supplier/subpage/show/sp_com_licen_2_show.ftl" />
                <form id="supplierShowAuditForm" method="post" action="/supplier/supplierAuditSave.htm">
                    <input type="hidden" value="${supplierVO.id}" name="spIdAudit" id="supplierIdHiddenId" >
                    <input type="hidden" value="stop" name="status" id="supplierStatusHiddenId" >
                </form>
                <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                    <tbody>
                        <tr>
                            <td class="td_right"></td>
                            <td>
                                <#if supplierVO.auditStatus==2>
                                    <input type="button" id="supplierShowAuditBtn" value="审核" class="btn btn82 btn_save2" name="button">
                                    <#--<input type="button" id="supplier_add_cancel" value="取消" class="btn btn82 btn_nochecked" name="button">-->
                                <#elseif supplierVO.auditStatus==4>
                                    <input type="button" id="supplierShowStopBtn" value="终止" class="btn btn82 btn_save2" name="button">
                                    <#--<input type="button" id="supplier_add_cancel" value="取消" class="btn btn82 btn_nochecked" name="button">-->
                                </#if>
                            </td>
                        </tr>
                        <#--虚线-->
                        <tr>
                            <td colspan="2">
                                <hr style="border: 1px dashed #247DFF;" />
                            </td>
                        </tr>
                    </tbody>
                </table>
                <#include "/supplier/common/audit_records.ftl" />
            </div>
        </div>
    </div>
</div>
</@backend>