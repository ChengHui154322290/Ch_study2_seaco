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
                <th>采购数量</th>
                <th>标准价格</th>
                <th>折扣</th>
                <th>供货价</th>
                <th>小计</th>
                <th>入库数量</th>
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
                    ${countIndexShow!}
                </td>
                <td>
                    <input type="hidden" name="skuCode" value="${(product.sku)!}">${(product.sku)!}
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
                    <input type="text" size="5" value="${(product.count)!}" name="count" class="_req input-text lh30">
                </td>
                <td>
                    <input type="text" size="5" value="${(product.standardPrice)!}" name="standardPrice" class="_req input-text lh30">
                </td>
                <td>
                    <input type="text" size="5" value="${(product.discount)!}" name="discount" class="_req input-text lh30">
                </td>
                <td>
                    <input type="text" size="5" value="${(product.orderPrice)!}" name="orderPrice" class="_req input-text lh30">
                </td>
                <td>
                    <input type="text" size="5" value="${(product.subtotal)!}" name="subtotal" class="_req input-text lh30">
                </td>
                <td>${(product.storageCount)!}</td>
                <td></td>
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
                <th>退货数量</th>
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
                <#if purIdedit?exists>
                    ${(product.batchNumber)!}
                    <input type="hidden" value="${(product.batchNumber)!}" name="batchNumber"> 
                    <#else>
                       
                </#if>
                <#if purIdshow?exists>
                    ${(product.batchNumber)!}
                    <input type="hidden" value="${(product.batchNumber)!}" name="batchNumber"> 
                    <#else>
                       
                </#if>
                </td>
                <td>
                    <select name="purchaseRate" class="_req select" style="width:60px;">
                        <#if taxRateVOs?exists>
                        <#list taxRateVOs as taxRate>
                            <#if taxRate.rate == product.purchaseRate>
                            <option value="${(taxRate.rate)!}" selected="selected">${(taxRate.rate)!}%</option>
                            <#else>
                            <option value="${(taxRate.rate)!}">${(taxRate.rate)!}%</option>
                            </#if>
                        </#list>
                        </#if>
                    </select>
                </td>
                <td>
                <#if  product.tariffRate == null || product.tariffRate == 0><#--非进口-->
  					<input type="hidden" name="tariffRate" value=""/>
                 <#else><#--进口-->
                    <select name="tariffRate" class="_req select" style="width:60px;">
                        <#if traffiRateVOs?exists>
                        <#list traffiRateVOs as taxRate>
                            <#if taxRate.rate == product.tariffRate>
                            <option value="${(taxRate.rate)!}" selected="selected">${(taxRate.rate)!}%</option>
                            <#else>
                            <option value="${(taxRate.rate)!}">${(taxRate.rate)!}%</option>
                            </#if>
                        </#list>
                        </#if>
                    </select>
                 </#if>
                 
                </td>
                <td name="noTaxRateTd">${(product.noTaxAccount)!}</td>
                <td>${(product.numberReturns)!}</td>
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
            <td><#-- 可用量：
                <input type="text" name="totalAvailable" value="${(purchaseVO.totalAvailable)!}" readOnly="readOnly" maxlength="60" class="input-text lh30" size="20" id="totalAvailableId">
                 -->
            </td>
            <td>入库数量：
                <input type="text" name="totalStorage" value="${(purchaseVO.totalStorage)!}" readOnly="readOnly" maxlength="60" class="input-text lh30" size="20" id="totalStorageId">
            </td>
            <td>数量合计：
                <input type="text" name="totalCount" value="${(purchaseVO.totalCount)!}" id="itemCountSum" readOnly="readOnly" maxlength="60" class="_req input-text lh30" size="20">
            </td>
            <td>金额总计：
                <input type="text" name="totalMoney" value="${(purchaseVO.totalMoney)!}" id="itemAmountSum" readOnly="readOnly" maxlength="60" class="_req input-text lh30" size="20">
            </td>
        </tr>
    </table>
</div>
    
