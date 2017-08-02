<#include "/common/common.ftl"/>
<@backend title="合同维护" js=[
        '/statics/supplier/js/common.js'] 
    css=[] >
<style type="text/css">
.pb15 {
    padding-bottom: 0px;
}
</style>
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">新增</b>
            </div>
            <div class="box_center" style="margin-bottom: 30px;">
                <form class="jqtransform" action="">
                    <#include "/supplier/audit/subpage/sp_com_examination_base.ftl" />
                    <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td class="td_right"></td>
                                <td class="td_right"></td>
                                <td align="center">
                                    <input class="btn btn82 btn_save2" type="button" value="保存" name="button">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input class="btn btn82 btn_nochecked" type="button" value="取消" name="button">
                                </td>
                                <td class="td_right"></td>
                                <td class="td_right"></td>
                                <td class="td_right"></td>
                            </tr>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
</@backend>