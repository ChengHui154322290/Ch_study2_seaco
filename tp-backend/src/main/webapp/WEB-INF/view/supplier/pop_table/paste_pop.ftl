<#include "/supplier/pop_table/context.ftl"/>
<@backend title="粘贴输入" js=[] 
    css=[] >
<div class="container" id="pastePop" style="z-index: 19891099;width: 821px; background: none repeat scroll 0% 0% rgb(255, 255, 255);">
    <div class="box_center" style="overflow: auto;height: 365px;">
        <form method="post" action="" class="jqtransform" id="product_pop_form">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                <tbody>
                    <tr>
                        <td class=""> 格式：条码	SKU	市场价	建议最低售价	供货价	平台使用费（%）<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">(条码，SKU必填其一)</font>
                        </td>
                    </tr>
                    <tr>
                        <td class="">
                           <textarea style="width:795px;height:250px;" id="pasteInfos"></textarea>
                        </td>
                    </tr>
                    <tr>
                       <td align="center">
	                       <input type="button" value="确定" id="quotation_paste_confirm" class="btn btn82 btn_save2">
	                       <input type="button" value="取消" id="quotation_paste_cancel" class="btn btn82 btn_del">
                       </td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" id="supplierId" value="${supplierId}" />
            <input type="hidden" id="supplierType" value="${supplierType}" />
        </form>
    </div>
</div>
</@backend>