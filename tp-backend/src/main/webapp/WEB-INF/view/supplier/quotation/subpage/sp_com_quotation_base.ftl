<table cellspacing="0" cellpadding="0" border="0" width="100%" class="list_table">
    <thead>
        <tr>
            <td class="td_left" colspan="16" style="border:0px;">
                <a onclick="addItem();" href="javascript:void(0)" class="ext_btn"><span class="add"></span>添加</a>
                <#--<a onclick="pasteItem()"  href="javascript:void(0)" class="ext_btn"><span class="add"></span>粘贴输入</a>-->
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
            <td class="_indexTd"><input type="hidden" value="${product.id}" name = "productId"> ${countIndexShow}</td>
            <td><input type="hidden" value="${product.barCode}" name="barCode" />${(product.barCode)!}</td>
            <td><input type="hidden" value="${product.sku}" name="skuCode" /><label> ${product.sku}</label></td>
            <td><input type="hidden" value="${product.productName}" name="productName" /><label>${product.productName}</label></td>
            <td>${product.brandName}</td>
            <td>${product.bigName}</td>
            <td>${product.midName}</td>
            <td>${product.smallName}</td>
            <td>${(product.productUnit)!}<input type="hidden" value="${(product.productUnit)!}" name="unitName" /></td>
            <td><input type="hidden" name="marketPrice" value="${(product.standardPrice)!}"><label>${(product.standardPrice)!}</label></td>
            <#--<td><input type="hidden" name="salePrice" value="${product.salePrice}">${product.salePrice}</td>-->
            <#--<td><input type="hidden" name="supplyPrice" value="${product.supplyPrice}"><label> ${product.supplyPrice}</label></td>-->
            <td><input type="hidden" name="basePrice" value="${product.basePrice}"><label>${product.basePrice}</label></td>
            <td><input type="hidden" name="freight" value="${product.freight}"><label>${product.freight}</label></td>
            <td><input type="hidden" name="mulTaxRate" value="${product.mulTaxRate}"><label>${product.mulTaxRate}</label></td>
            <td><input type="hidden" name="tarrifTaxRate" value="${product.tarrifTaxRate}"><label>${product.tarrifTaxRate}</label></td>
            <td><input type="hidden" name="sumPrice" value="${product.sumPrice}"><label>${product.sumPrice}</label></td>
            <td><input type="hidden" name="commissionPercent" value="${product.commissionPercent}"><label>
	            <#if (product.commissionPercent)?exists>
	             	${product.commissionPercent}%
	            <#else>
	            	---
	            </#if></label>
            </td>
            <td>${product.boxProp}</td>
            <td>${product.productProp}</td>
            <td><input type="button" class="ext_btn" onclick="deleteThisProductTr(${countIndex})" value="删除">
                <input type="button" class="ext_btn" onclick="modifyThisProductTr(${countIndex})" value="修改">
                <input type="button" class="ext_btn" onclick="his(${countIndex})" value="历史">
            </td>
        </tr>
        <#assign countIndexShow=countIndexShow+1>
        <#assign countIndex=countIndex+1>
        </#list>
        </#if>
    </tbody>
</table>

