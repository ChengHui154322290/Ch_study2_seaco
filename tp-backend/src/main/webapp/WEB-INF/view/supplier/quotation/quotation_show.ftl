<#include "/common/common.ftl"/>
<@backend title="查看报价单" js=[
        '/statics/supplier/js/common.js',
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
                <b class="pl15">查看报价单</b>
            </div>
            <div class="box_center" style="margin-bottom: 30px;">
                <#include "/supplier/quotation/subpage/sp_com_quo_form_show.ftl" />
                <#include "/supplier/quotation/subpage/sp_com_quotation_base_show.ftl" />
                <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                    <tbody>
                        <tr>
                            <td>
                                <#if quotationVO.quotationType ==  'common_quotation' && quotationVO.auditStatus == 2>
	                                <input type="button" value="审核" id="quotationShowAudit" class="btn btn82 btn_add" name="button">
	                                <input type="button" value="取消" id="quotationShowCancel" class="btn btn82 btn_nochecked" name="button">
	                                <#--<input type="button" value="预览" id="previewId" class="btn btn82 btn_search" name="button">-->
                                <#else>
                                	<input type="button" value="取消" id="quotationShowCancel" class="btn btn82 btn_nochecked" name="button">
                                	<input type="button" value="下载" id="downLoadId" class="btn btn82 btn_export" name="button">
                                </#if>
                            </td>
                            <td colspan="13">
                            </td>
                        </tr>
                    </tbody>
                </table>
                <input type="hidden" value="0" id="indexNumHiddenId" />
                <p><hr style="border:1px dashed #247DFF;" /></p>
                <#-------------------------------------------->
                <#include "/supplier/common/audit_records.ftl" />
            </div>
        </div>
    </div>
</div>
</@backend>