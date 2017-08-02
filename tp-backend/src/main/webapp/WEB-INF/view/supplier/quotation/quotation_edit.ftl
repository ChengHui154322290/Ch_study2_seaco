<#include "/common/common.ftl"/>
<@backend title="编辑报价单" js=[
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
                <b class="pl15">编辑报价单</b>
            </div>
            <div class="box_center" style="margin-bottom: 30px;">
                <form class="jqtransform" action="${domain}/supplier/quotationSave.htm" id="quotation_add_form" method="post">
                    <input type="hidden" name="status" value="0" id="statusSet" />
                    <#include "/supplier/quotation/subpage/sp_com_quo_form_edit.ftl" />
                    <#include "/supplier/quotation/subpage/sp_com_quotation_base.ftl" />
                    <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td>
                                    <#if quotationVO.auditStatus==5 >
	                                    <input type="button" value="提交" id="quotationAddSub" class="btn btn82 btn_add" name="button">
	                                    <#--<input type="button" value="预览" id="previewId" class="btn btn82 btn_search" name="button">-->
                                    <#else>
										<input type="button" value="保存" id="quotationEditSave" class="btn btn82 btn_save2" name="button">
	                                   	<input type="button" value="提交" id="quotationAddSub" class="btn btn82 btn_add" name="button">
	                                    <#--<input type="button" value="预览" id="previewId" class="btn btn82 btn_search" name="button">-->
									</#if>
                                    <#--<input type="button" value="取消" id="quotationAddCancel" class="btn btn82 btn_nochecked" name="button">-->
                                </td>
                                <td colspan="13">
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <input type="hidden" value="0" id="indexNumHiddenId" />
                </form>
            </div>
        </div>
    </div>
</div>
</@backend>