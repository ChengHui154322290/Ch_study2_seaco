<table cellspacing="0" cellpadding="0" border="0" width="100%" class="list_table">
    <thead>
        <tr>
            <td class="td_left" colspan="16" style="border:0px;">
            </td>
        </tr>
        <tr align="center">
            <th>NO.</th>
            <th style="width:150px;">条形码</th>
            <th style="width:150px;">SKU</th>
            <th>商品名称</th>
            <th>品牌</th>
            <th>大类</th>
            <th>中类</th>
            <th>小类</th>
            <th>单位</th>
            <th>市场价</th>
            <#--<th>建议最低售价</th>-->
            <#--<th>供货价</th>-->
            <th>裸价</th>
            <th>运费</th>
            <th>跨境综合税率</th>
            <th>行邮税税率</th>
            <th>包邮包税代发价</th>
            <th>平台使用费</th>
            <th>箱规</th>
            <th>商品规格</th>
            <th>操作</th>
         </tr>
    </thead>
    <tbody id="quotation_item_add_body">
        <#if (quotationVO.quotationProductList)?exists>
        <#assign countIndex=1000>
        <#assign countIndexShow=1>
        <#list quotationVO.quotationProductList as product>
        <tr align="center" id="addQuotaItemTr_${countIndex}">
            <td class="_indexTd">${countIndexShow}</td>
            <td>${(product.barCode)!}</td>
            <td><input type="hidden" value="${product.sku}" name="skuCode" />${product.sku}</td>
            <td>${product.productName}</td>
            <td>${product.brandName}</td>
            <td>${product.bigName}</td>
            <td>${product.midName}</td>
            <td>${product.smallName}</td>
            <td>${product.productUnit}</td>
            <td><input type="hidden" name="marketPrice" value="${(product.standardPrice)!}">${(product.standardPrice)!}</td>
            <#--<td><input type="hidden" name="salePrice" value="${product.salePrice}">${product.salePrice}</td>-->
            <#--<td><input type="hidden" name="supplyPrice" value="${product.supplyPrice}">${product.supplyPrice}</td>-->
            <td><input type="hidden" name="basePrice" value="${product.basePrice}">${product.basePrice}</td>
            <td><input type="hidden" name="freight" value="${product.freight}">${product.freight}</td>
            <td><input type="hidden" name="mulTaxRate" value="${product.mulTaxRate}">${product.mulTaxRate}</td>
            <td><input type="hidden" name="tarrifTaxRate" value="${product.tarrifTaxRate}">${product.tarrifTaxRate}</td>
            <td><input type="hidden" name="sumPrice" value="${product.sumPrice}">${product.sumPrice}</td>
            <td><input type="hidden" name="commissionPercent" value="${product.commissionPercent}">
            <#if (product.commissionPercent)?exists>
             	${product.commissionPercent}%
            <#else>
            	---
             </#if>
           </td>
            <td>${product.boxProp}</td>
            <td>${product.productProp}</td>
            <td></td>
        </tr>
        <#assign countIndexShow=countIndexShow+1>
        <#assign countIndex=countIndex+1>
        </#list>
        </#if>
    </tbody>
</table>

