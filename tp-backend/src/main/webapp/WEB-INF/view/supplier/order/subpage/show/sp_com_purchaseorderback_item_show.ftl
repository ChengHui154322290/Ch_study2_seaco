<style>
.pagination ul li a:hover, 
.pagination ul .selected, 
.pagination ul .selected{
color:black
}
</style>
<hr style="border: 1px dashed #247DFF;">
<div class="page">
    <div class="pagination">
        <ul class="idTabs  ">
            <li><a class="selected" href="#gallery1">SKU</a></li>
            <li><a href="#gallery2">类别</a></li>
        </ul>
    </div>
</div>
<#-------------------------------------------->
<div id="gallery1">
    <table  cellspacing="0" cellpadding="0" width="100%" class="list_table CRZ" >
        <thead>
            <tr align="center">
                <th>NO.</th>
                <th style="width:150px;">SKU</th>
                <th style="width:150px;">条形码</th>
                <th>商品名称</th>
                <th>单位</th>
                <th>规格属性1</th>
                <th>规格属性2</th>
                <th>规格属性3</th>
                <th>退货数量</th>
                <th>标准价格</th>
                <th>折扣</th>
                <th>供货价</th>
                <th>小计</th>
                <th>出库数量</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody id="sp_order_table_body_1">
             <#if (purchaseVO.purchaseProductList)?exists>
             <#assign countIndex=1000>
             <#assign countIndexShow=1>
             <#list purchaseVO.purchaseProductList as product>
             <tr align="center" id="addQuotaItemTr_${countIndex}">
                <td class="_indexTd">
                    ${countIndexShow}
                </td>
                <td>
                    <input type="hidden" name="skuCode" id="skuCode${(product.originProductId)!}" value="${(product.sku)!}">${(product.sku)!}
                </td>
                <td>
                    ${(product.barcode)!}
                </td>
                <td>
                    ${(product.productName)!}
                </td>
                <td><input type="hidden" value="${(product.productUnit)!}" name="unitName">
                    ${(product.productUnit)!}
                </td>
                <td><input type="hidden" value="${(product.prop1)!}" name="prop1">
                   ${(product.prop1)!}
                </td>
                <td><input type="hidden" value="${(product.prop2)!}" name="prop2">
                    ${(product.prop2)!}
                </td>
                <td><input type="hidden" value="${(product.prop3)!}" name="prop3">
                    ${(product.prop3)!}
                </td>
                <td>
                    <input type="text" size="5" value="${(product.count?c)!}" name="count" class="_req input-text lh30">
                </td>
                <td>
                    <input type="text" size="5" value="${(product.standardPrice?c)!}" name="standardPrice" class="_req input-text lh30">
                </td>
                <td>
                    <input type="text" size="5" value="${(product.discount?c)!}" name="discount" class="_req input-text lh30">
                </td>
                <td>
                    <input type="text" size="5" value="${(product.orderPrice?c)!}" name="orderPrice" class="_req input-text lh30">
                </td>
                <td>
                    <input type="text" size="5" value="${(product.subtotal?c)!}" name="subtotal" class="_req input-text lh30">
                </td>
                <td style="background-color:#DDD;">${(product.storageCount)!}</td>
                <td>
                    <input type="hidden" name="originId" value="${(product.originId)!}">
                    <input type="hidden" name="originProductId" value="${(product.originProductId)!}">
                </td>
            </tr>
            <#assign countIndexShow=countIndexShow+1>
            <#assign countIndex=countIndex+1>
            </#list>
            </#if>
        </tbody>
    </table>
</div>
<div id="gallery2">
    <table  cellspacing="0" cellpadding="0" width="100%" class="list_table CRZ">
        <thead>
            <tr align="center">
                <th>NO.</th>
                <th style="width:150px;">SKU</th>
                <th>商品名称</th>
                <th>品牌</th>
                <th>大类</th>
                <th>中类</th>
                <th>小类</th>
                <th style="width:150px;">批号</th>
                <th>进项税率</th>
                <th>关税税率</th>
                <th>未税金额</th>
                <th>采购订单编号</th>
                <th>备注</th>
            </tr>
        </thead>
        <tbody id="sp_order_table_body_2">
            <#if (purchaseVO.purchaseProductList)?exists>
            <#assign countIndex=1000>
            <#assign countIndexShow=1>
            <#list purchaseVO.purchaseProductList as product>
            <tr align="center" id="addQuotaItemTr2_${countIndex}">
                <td class="_indexTd2">
                    ${countIndexShow}
                </td>
                <td>
                    ${(product.sku)!}
                </td>
                <td>
                    ${(product.productName)!}
                </td>
                <td>
                    ${(product.brandName)!}
                </td>
                <td>
                    ${(product.bigName)!}
                </td>
                <td>
                    ${(product.midName)!}
                </td>
                <td>
                    ${(product.smallName)!}
                </td>
                <td>
                    ${(product.batchNumber)!}
                    <input type="hidden" value="${(product.batchNumber)!}" name="batchNumber"> 
                </td>
                <td>
                    <input type="hidden" name="purchaseRate" value="${(product.purchaseRate)!}" />
                    ${(product.purchaseRate)!}%
                    <#--  
                    <select name="purchaseRate">
                    <#if taxRateVOs?exists>
                    <#list taxRateVOs as taxRate>
                        <#if taxRate.supplierRate == product.purchaseRate>
                        <option value="${(taxRate.supplierRate)!}" selected="selected">${(taxRate.supplierRate)!}%</option>
                        <#else>
                        <option value="${(taxRate.supplierRate)!}">${(taxRate.supplierRate)!}%</option>
                        </#if>
                    </#list>
                    </#if>
                   <select>
                   -->
                </td>
                <td>
                    <input type="hidden" name="tariffRate" value="${(product.tariffRate)!}" />
                    <#if product.tariffRate == null || product.tariffRate == "">
                    <#else>
                        ${(product.tariffRate)!}%
                    </#if>
                    <#-- 
                    <select name="tariffRate">
                        <#if traffiRateVOs?exists>
                        <#list traffiRateVOs as taxRate>
                            <#if taxRate.supplierRate == product.tariffRate>
                            <option value="${(taxRate.supplierRate)!}" selected="selected">${(taxRate.supplierRate)!}%</option>
                            <#else>
                            <option value="${(taxRate.supplierRate)!}">${(taxRate.supplierRate)!}%</option>
                            </#if>
                        </#list>
                        </#if>
                    </select>
                    -->
                </td>
                <td name="noTaxRateTd">${(product.noTaxAccount)!}</td>
                <td>${(product.originId)!}</td>
                <td>
                    <input type="text" name="productDesc" value="${(product.productDesc)!}" class="input-text lh30" size="10">
                </td>
                <input type="hidden" value="${(product.noTaxAccount)!}" name="noTaxRate" />
                <input type="hidden" value="${supplierDO.incomeTaxRate}" name="jsRate" />
            </tr>
            <#assign countIndexShow=countIndexShow+1>
            <#assign countIndex=countIndex+1>
            </#list>
            </#if>
        </tbody>
    </table>
</div>
<div class="search_bar_btn" width="100%"  style="text-align:right;">
    <table style="width:100%;">
        <tr>
            <td>
            </td>
            <td>退货数量:
                <input type="text" name="totalReturn" id="totalReturn" value="${(purchaseVO.totalCount?c)!}" readOnly="readOnly" maxlength="60" class="_req input-text lh30" size="20">
            </td>
            <td>退货金额：
                <input type="text" name="totalMoneyReturn" value="${(purchaseVO.totalMoney?c)!}" id="totalMoneyReturn" readOnly="readOnly" maxlength="60" class="_req input-text lh30" size="20">
            </td>
            <td>出库数量：
                <input type="text" name="totalMoney" value="${(purchaseVO.totalStorage?c)!}" id="totalMoney" readOnly="readOnly" maxlength="60" class="_req input-text lh30" size="20">
            </td>
        </tr>
    </table>
</div>
    
