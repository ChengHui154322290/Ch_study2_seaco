<#include "/supplier/pop_table/context.ftl"/>
<@backend title="添加订单" js=[ ] 
    css=[] >
<div id="orderPopTable" style="z-index: 19891099;background: none repeat scroll 0% 0% rgb(255, 255, 255);">
    <#--
    <div class="box_top">
        <b class="pl15">添加</b>  
        <a class="box_top_r fr pr15" id="popClosebtn" href="javascript:void(0);">关闭</a>  
    </div>
    -->
    <div class="box_center" style="width:881px;">
        <form method="post" action="${actionUrl}" class="jqtransform" id="order_search_form">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                <tbody>
                    <tr>
                        <td width="10%" style="text-align:left;">订单编号：</td>
                        <td align="left" width="10%"><input type="text" size="20" id="orderIdInput" class="input-text lh30" name="purchaseId"></td>
                        <td style="text-align:left;"  width="10%">订单日期： </td>
                        <td align="left" class="">
                            <input type="text" size="16" class="_dateField input-text lh30" name="startTime" readonly="readonly">到
                            <input type="text" size="16" class="_dateField input-text lh30" name="endTime" readonly="readonly">
                        </td>
                        <td  width="10%" style="text-align:left;">订单状态：</td>
                        <td  align="left" width="10%">
	                         <input type="text" size="20" class="input-text lh30 hasDatepicker" placeholder="订单完成" readonly="readonly">
	                         <input type="hidden" name="orderStatus" value="7" readonly="readonly">
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align:left;">条形码：</td>
                        <td align="left">
                            <#-- by zhs 01181804 修改 name -->                       
                            <input type="text" size="20" id="orderIdInput" class="input-text lh30" name="barcode">
                        <td style="text-align:left;">SKU： </td>
                        <td width="233" align="left">
                            <input type="text" size="18" class="input-text lh30" name="sku" placeholder="输入sku" id="productSkuId">
                            <input type="text" size="18" class="input-text lh30" name="productName" placeholder="商品名称" id="productNameId">
                        </td>
                        <td style="text-align:">批号：</td>
                        <td align="left" class="">
	                         <input type="text" size="20" class="input-text lh30" name="batchNum" value="" >
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan="6">
                            <input type="button" value="查询" class="btn btn82 btn_search" id="orderListQuery" name="button">
                            <input type="button" value="确定" class="btn btn82 btn_save2" id="orderListConfirm" name="button">
                        </td>
                    </tr>
                </tbody>
            </table>
		    <#-- supplierId by zhs 01161823 保存supplierId -->
            <#--  <input type="hidden" value="${(supplier.id)!}" name="supplierId" /> -->
            <input type="hidden" value="${supplierId}" name="supplierId"  />
            <input type="hidden" name="index" value="1" id="pageIndexId">
            <#if supplier?exists>
                <input type="hidden" value="${(supplier.incomeTaxRate)!}" id="jsRateSel">
            </#if>
        </form>
        <div style="background: none repeat scroll 0% 0% rgb(255, 255, 255);">
	        <hr style="border: 1px dashed #247DFF;">
		    <div class="box_center" style="overflow: auto;height: 200px;">
		        <table width="100%" style="height:50px;overflow:scroll;" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
		            <tbody>
		                <tr>
		                    <td colspan="8">
		                        <table width="100%" cellspacing="0" cellpadding="0" border="0" id="CRZ0" class="list_table CRZ">
		                            <tbody>
		                                <tr align="center">
		                                    <th></th>
		                                    <th style="text-align:center;">${orderTypeShow}编号</th>
		                                    <th style="text-align:center;">条形码</th>
		                                    <th style="text-align:center;">SKU</th>
		                                    <th style="text-align:center;">商品名称</th>
		                                    <th style="text-align:center;">供货价格</th>
		                                    <th style="text-align:center;">订单金额</th>
		                                    <th style="text-align:center;">批号</th>
		                                    <th style="text-align:center;">已退数量</th>
		                                    <th style="text-align:center;">可退数量</th>
		                                </tr>
		                                <#list orders as order>
		                                <tr align="center" style="background-color: rgb(255, 255, 255);" class="tr">
		                                    <td style="text-align:center;">
		                                        <input name="orderIdSel" type="checkbox" value="${order.id}" />
		                                        <input id="orderIdSel${order.id}" type="hidden" value="${order.purchaseId}" />
		                                        <input type="hidden" id="skuCodeSel${order.id}" value="${(order.sku)!}" />
		                                        <input type="hidden" id="brandName${order.id}" value="${(order.brandName)!}" />
		                                        <input type="hidden" id="daleiName${order.id}" value="${(order.bigName)!}" />
		                                        <input type="hidden" id="midCategoryName${order.id}" value="${(order.midName)!}" />
		                                        <input type="hidden" id="smaillCName${order.id}" value="${(order.smallName)!}" />
		                                        <input type="hidden" id="skuCode${order.id}" value="${(order.sku)!}" />
		                                        <input type="hidden" id="productName${order.id}" value="${(order.productName)!}" />
		                                        <input type="hidden" id="unitName${order.id}" value="${(order.productUnit)!}" />
		                                        <input type="hidden" id="boxProp${order.id}" value="${(order.boxProp)!}" />
		                                        <input type="hidden" id="productProp${order.id}" value="${(order.productProp)!}" />

		                                        <input type="hidden" id="batchNumber${order.id}" value="${(order.batchNumber)!}" />
		                                        <input type="hidden" id="purchaseRate${order.id}" value="${(order.purchaseRate?c)!}" />
		                                        <input type="hidden" id="tariffRate${order.id}" value="${(order.tariffRate?c)!}" />
		                                        <input type="hidden" id="noTaxAmount${order.id}" value="${(order.noTaxAmount?c)!}" />
		                                        <input type="hidden" id="productDesc${order.id}" value="${(order.productDesc)!}" />
		                                        <input type="hidden" id="prop1${order.id}" value="${(order.prop1)!}" />
		                                        <input type="hidden" id="prop2${order.id}" value="${(order.prop2)!}" />
		                                        <input type="hidden" id="prop3${order.id}" value="${(order.prop3)!}" />
		                                        <input type="hidden" id="standardPrice${order.id}" value="${(order.standardPrice?c)!}" />
		                                        <input type="hidden" id="discount${order.id}" value="${(order.discount?c)!}" />
		                                        <input type="hidden" id="orderPrice${order.id}" value="${(order.orderPrice?c)!}" />
		                                        <input type="hidden" id="subtotal${order.id}" value="${(order.subtotal?c)!}" />

		                                        <input type="hidden" id="storageCount${order.id}" value="${(order.storageCount?c)!}" />
		                                        <input type="hidden" id="numberReturns${order.id}" value="${(order.numberReturns?c)!}" />                                        
		                                        <input type="hidden" id="actualNumber${order.id}" value="${(order.actualNumber?c)!}" />                                        
		                                        <input type="hidden" id="barcodeSel${order.id}" value="${(order.barcode)!}" />                                        
		                                        
		                                    </td>
		                                    <td style="text-align:center;">${(order.purchaseId)!}</td>
		                                    <td style="text-align:center;">${(order.barcode)!}</td>
		                                    <td style="text-align:center;">${(order.sku)!}</td>
		                                    <td style="text-align:center;" class="line-compress">${(order.productName)!}</td>
		                                    <td style="text-align:center;">${(order.orderPrice)!}</td>
		                                    <td style="text-align:center;">${(order.subtotal)!}</td>
		                                    <td style="text-align:center;">${(order.batchNumber)!}</td>
		                                    <td style="text-align:center;">${(order.numberReturns)!}</td>
		                                    <td style="text-align:center;">
		                                         <#if (order.storageCount)?exists && (order.numberReturns)?exists>
		                                         ${order.storageCount - order.numberReturns}
		                                         </#if>
		                                    </td>
		                                </tr>
		                                </#list>
		                            </tbody>
		                        </table>
		                    </td>
		                </tr>
		            </tbody>
		        </table>
		    </div>
		</div>
    </div>
</div>
<script>
     (function($){
         function focusBlurEvent(target,compareVal){
             $(target).focus(function(){
	             var thisVal = $(this).val();
	             if(thisVal==compareVal) {
	                 $(this).val("");
	             }
	         });
	         $(target).blur(function(){
	             var thisVal = $(this).val();
	             if(!thisVal) {
	                 $(this).val(compareVal);
	             }
	         });
         }
         focusBlurEvent($("#productSkuId"),'输入sku');
         focusBlurEvent($("#productNameId"),'商品名称');
     })(jQuery);
</script>
</@backend>