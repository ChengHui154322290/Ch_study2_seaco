<#include "/common/common.ftl"/>
<@backend title="查看代销订单" js=[
        '/statics/supplier/js/common.js', 
        '/statics/supplier/js/pop_common.js', 
        '/statics/supplier/js/validator.js',
        '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
        '/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
        '/statics/supplier/js/idTabs.js',
        '/statics/supplier/js/web/sell_order_add.js'] 
    css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
<style type="text/css">
.pb15 {
    padding-bottom: 0px;
}
</style>
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">查看代销订单</b>
            </div>
            <div class="box_center" style="margin-bottom: 30px;">
                <#include "/supplier/order/subpage/sp_com_sellorder_base_edit.ftl" />
                <#include "/supplier/order/subpage/show/sp_com_sellorder_item_show.ftl" />
                <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                    <tbody>
                        <tr>
                            <td>
                                <#if purchaseVO.auditStatus == 1>
                                <input type="button" value="取消" id="quotationShowCancel" class="btn btn82 btn_nochecked" name="button">
                                <#elseif purchaseVO.auditStatus == 2>
                                <input type="button" value="审核" id="quotationShowAudit" class="btn btn82 btn_add" name="button">
                                <#-- <input type="button" value="取消" id="quotationAddCancel" class="btn btn82 btn_nochecked" name="button">-->
                                <#elseif purchaseVO.auditStatus == 5>
                                <input type="button" value="取消" id="quotationShowCancel" class="btn btn82 btn_nochecked" name="button">
                                </#if>
                                 <#--<input type="button" value="取消" id="quotationAddCancel" class="btn btn82 btn_nochecked" name="button">-->
                            </td>
                            <td colspan="13">
                            </td>
                        </tr>
                    </tbody>
                </table>
                <form class="jqtransform" action="/supplier/sellorderSave.htm" id="order_form">
                    <input type="hidden" value="${purchaseVO.id}" name="purId" id="pruOrderId" />
                    <input type="hidden" value="" id="examineStatusId" name="status" />
                    <input type="hidden" value="0" id="indexNumHiddenId" />
                </form>
                <p><hr style="border:1px dashed #247DFF;" /></p>
                <#-------------------------------------------->
                <#include "/supplier/common/audit_records.ftl" />
            </div>
        </div>
    </div>
</div>
</@backend>