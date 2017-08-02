<#include "/supplier/pop_table/context.ftl"/>
<@backend title="" js=[] 
    css=[] >
<div class="container" id="brandPopTable" style="z-index: 19891099;width: 821px; background: none repeat scroll 0% 0% rgb(255, 255, 255);">
    <div class="box_center" style="overflow: auto;height: 325px;">
        <form method="post" action="${actionUrl}" class="jqtransform" id="audit_pop_form">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                <tbody>
                    <tr>
                        <td class="td_right">审核结果：</td>
                        <td class="">
                            <span class="fl">
                                <div class="select_border"> 
                                    <div class="select_containers "> 
                                        <select class="select" name="auditStatus" style="width:100px;"> 
                                            <option value="4">通过</option> 
                                            <option value="5">拒绝</option> 
                                        </select> 
                                    </div> 
                                </div> 
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td class="td_right">审核意见：</td>
                        <td class="">
                            <textarea class="textarea" rows="10" cols="30" id="auditContentId" name="auditContent"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td align="center" colspan="2">
                            <input type="button" value="确定" class="btn btn82 btn_save2" id="auditPopConfirm" name="button">
                            <input type="button" value="取消" class="btn btn82 btn_del" id="auditPopCancel" name="button">
                        </td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" name="execId" value="${execId}" />
        </form>
    </div>
</div>
</@backend>