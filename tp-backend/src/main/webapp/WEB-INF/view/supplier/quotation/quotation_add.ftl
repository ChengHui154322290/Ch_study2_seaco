<#include "/common/common.ftl"/>
<@backend title="新增报价单" js=[
        '/statics/supplier/js/common.js',
        '/statics/supplier/js/pop_common.js', 
        '/statics/supplier/js/validator.js',
        '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
        '/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
        '/statics/supplier/js/web/quotation_add.js'
        ] 
    css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
<style type="text/css">
.pb15 {
    padding-bottom: 0px;
}
</style>
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">新增报价单</b>
            </div>
            <div class="box_center" style="margin-bottom: 30px;">
                <form class="jqtransform" action="${domain}/supplier/quotationSave.htm" id="quotation_add_form" method="post">
                    <#include "/supplier/quotation/subpage/sp_com_quo_form.ftl" />
                    <#include "/supplier/quotation/subpage/sp_com_quotation_base.ftl" />
                    <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td>
                                    <input type="button" value="保存" id="quotationAddSave" class="btn btn82 btn_save2" name="button">
                                    <input type="button" value="提交" id="quotationAddSub" class="btn btn82 btn_add" name="button">
                                </td>
                                <td colspan="13">
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <input type="hidden" value="0" id="indexNumHiddenId" />
                    <input type="hidden" value="${(quotationType)!}" name="quotationType" >
                </form>
            </div>
        </div>
    </div>
</div>
</@backend>