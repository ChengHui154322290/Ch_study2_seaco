<#include "/supplier/pop_table/context.ftl"/>
<@backend title="添加商品" js=[] 
    css=[] >
<div class="container" id="brandPopTable" style="z-index: 19891099;width: 821px; background: none repeat scroll 0% 0% rgb(255, 255, 255);">
    <#-- 
    <div class="box_top">
        <b class="pl15">新增报价单</b>  
        <a class="box_top_r fr pr15" id="popClosebtn" href="javascript:void(0);">关闭</a>  
    </div>
    -->
    <div class="box_center" style="overflow: auto;height: 430px;">
        <form method="post" action="/supplier/supplierproductExec.htm" class="jqtransform" id="product_pop_form">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                <tbody>
                    <tr>
                        <td class="td_right">条码：</td>
                        <td class="">
                            <input type="text" size="30" maxlength="80" class="input-text lh30" id="barCodeSel" name="barCode">
                            <input type="button" value="确定" id="quotation_add_barcode_confirm" class="ext_btn ext_btn_submit">
                        </td>
                    </tr>
                    <tr>
                        <td class="td_right">SKU：</td>
                        <td class="">
                            <input type="text" size="30" maxlength="80" class="input-text lh30" id="skuCodeSel" name="skuCode">
                            <input type="button" value="确定" id="quotation_add_sku_confirm" class="ext_btn ext_btn_submit">
                        </td>
                    </tr>
                    <tr>
                        <td class="td_right">商品名称：</td>
                        <td class="" id="productNameTd"><#-- 贝亲 -->
                        </td>
                    </tr>
                    <tr>
                        <td class="td_right">市场价：</td>
                        <td class="">
                            <input type="text" size="30" maxlength="30" id="marketPrice" class="_price input-text lh30" name="marketPrice">
                        </td>
                    </tr>
                    <#--<tr>-->
                        <#--<td class="td_right">建议最低售价：</td>-->
                        <#--<td class="">-->
                            <#--<input type="text" size="30" maxlength="30" id="salePrice" class="_price input-text lh30" name="salePrice">-->
                        <#--</td>-->
                    <#--</tr>-->
                    <#--<tr>-->
                        <#--<td class="td_right">供货价：</td>-->
                        <#--<td class="">-->
                            <#--<input type="text" size="30" maxlength="30" id="supplyPrice" class="_price input-text lh30" name="supplyPrice">-->
                        <#--</td>-->
                    <#--</tr>-->

                    <tr>
                        <td class="td_right">裸价：</td>
                        <td class="">
                            <input type="text" size="30" maxlength="30" id="basePrice" class="_price input-text lh30" name="basePrice">
                        </td>
                    </tr>

                    <tr>
                        <td class="td_right">运费：</td>
                        <td class="">
                            <input type="text" size="30" maxlength="30" id="freight" class="_price input-text lh30" name="freight">
                        </td>
                    </tr>

                    <tr>
                        <td class="td_right">跨境综合税率：</td>
                        <td class="">
                            <input type="text" size="30" maxlength="30" id="mulTaxRate" placeholder="税率取值范围0~1.0000" class="_price input-text lh30" name="mulTaxRate">
                        </td>
                    </tr>

                    <tr>
                        <td class="td_right">行邮税税率：</td>
                        <td class="">
                            <input type="text" size="30" maxlength="30" id="tarrifTaxRate" placeholder="税率取值范围0~1.0000" class="_price input-text lh30" name="tarrifTaxRate">
                        </td>
                    </tr>

                    <tr>
                        <td class="td_right">包邮包税代发价：</td>
                        <td class="">
                            <input type="text" size="30" maxlength="30" id="sumPrice" class="_price input-text lh30" name="sumPrice">
                        </td>
                    </tr>

                    <tr>
                     <#if supplierType?? && supplierType== "Purchase">
                    	 <input type="hidden" size="10" id="commissionPercent" maxlength="30" class="_price input-text lh30" name="commissionPercent">
                     <#else>
                      	<td class="td_right">平台使用费：</td>
                        <td class="">
                            <input type="text" size="10" id="commissionPercent" maxlength="30" class="_price input-text lh30" name="commissionPercent">%<input type="hidden" size="10" id="commissionPercentHidden"/>
                        </td>
                      </#if>
                    </tr>
                    <tr>
                        <td align="center" colspan="2">
                            <input type="button" value="确定" class="btn btn82 btn_save2" id="productPopConfirm" name="button">
                            <input type="button" value="取消" class="btn btn82 btn_del" id="productPopCancel" name="button">
                        </td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" id="supplierId" value="${supplierId}" />
            <input type="hidden" id="brandName" value="" />
            <input type="hidden" id="daleiName" value="" />
            <input type="hidden" id="midCategoryName" value="" />
            <input type="hidden" id="smaillCName" value="" />
            <input type="hidden" id="SPUCode" value="" />
            <input type="hidden" id="PRDIDCode" value="" />
            <input type="hidden" id="skuCode" value="" />
            <input type="hidden" id="productName" value="" />
            <input type="hidden" id="unitName" value="" />
            <input type="hidden" id="boxProp" value="" />
            <input type="hidden" id="productProp" value="" />
            <input type="hidden" id="supplierType" value="${(supplierType)!}"/>
        </form>
    </div>
</div>
</@backend>