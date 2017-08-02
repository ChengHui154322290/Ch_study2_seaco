<#include "/supplier/pop_table/context.ftl"/>
<@backend title="添加商品" js=[] 
    css=[] >

<script>
    var purchaseRateSel = '<select class="_req select" name="purchaseRate"><#if rateSelOption??><#list rateSelOption as taxRate><option value="${taxRate.id}">${taxRate.name}</option></#list></#if></select>';
    var tariffRateSel = '<select class="_req select" name="tariffRate"><#if trafficRage??><#list trafficRage as taxRate><option value="${taxRate.id}">${taxRate.name}</option></#list></#if></select>';
</script>    

<div class="container" id="brandPopTable" style="z-index: 19891099;width: 821px; background: none repeat scroll 0% 0% rgb(255, 255, 255);">
    <div class="box_center" style="overflow: auto;height: 325px;">
        <form method="post" action="#" class="jqtransform" id="product_pop_form">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                <tbody>
                    <tr>
                        <td class="td_right">条码：</td>
                        <td class="">
                            <input type="text" size="30" maxlength="80" class="input-text lh30" id="barCodeSel" name="barCode">
                            <input type="button" value="确定" id="order_item_dd_barcode_confirm" class="ext_btn ext_btn_submit">
                        </td>
                    </tr>
                    <tr>
                        <td class="td_right">SKU：</td>
                        <td class="">
                            <input type="text" size="30" maxlength="80" class="input-text lh30" id="skuCodeSel" name="skuCode">
                            <input type="button" value="确定" id="order_item_dd_sku_confirm" class="ext_btn ext_btn_submit">
                        </td>
                    </tr>
                    <tr>
                        <td class="td_right">商品名称：</td>
                        <td class="" id="productNameTd"><#-- 贝亲 -->
                        </td>
                    </tr>
                    <tr>
                        <td class="td_right">采购数量：</td>
                        <td class="">
                            <input type="text" size="30" maxlength="30" id="count" class="_price input-text lh30" name="marketPrice">
                        </td>
                    </tr>
                    <tr>
                        <td align="center" colspan="2">
                            <input type="button" value="确定" class="btn btn82 btn_save2" id="productPopConfirm" name="button">
                            <input type="button" value="取消" class="btn btn82 btn_del" id="productPopCancel" name="button">
                        </td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" id="supplierId" value="${supplierId!}" />
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
            
            <input type="hidden" id="batchNumber" value="" />
            <input type="hidden" id="purchaseRate" value="" />
            <input type="hidden" id="tariffRate" value="" />
            <input type="hidden" id="noTaxAmount" value="" />
            <input type="hidden" id="productDesc" value="" />
            <input type="hidden" id="prop1" value="" />
            <input type="hidden" id="prop2" value="" />
            <input type="hidden" id="prop3" value="" />
            <input type="hidden" id="standardPrice" value="" />
            <input type="hidden" id="discount" value="" />
            <input type="hidden" id="orderPrice" value="" />
            <input type="hidden" id="subtotal" value="" />
            <input type="hidden" id="wavesSign" value="" />
            <input type="hidden" id="rateSel" value="${(rateSel)!}" />
            <#if supplierDO?exists>
                <input type="hidden" value="${(supplierDO.incomeTaxRate)!}" id="jsRateSel">
            </#if>
        </form>
    </div>
</div>
</@backend>