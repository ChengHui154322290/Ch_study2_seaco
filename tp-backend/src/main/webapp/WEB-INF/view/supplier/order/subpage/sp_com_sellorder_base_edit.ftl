<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <tbody>
        <tr>
            <td class="td_right" >采购订单编号： </td>
            <td class=""  width="350">
                <span>${purchaseVO.id}</span>
            </td>
            <td class="td_right" align="left"><font color="red">*</font>订单日期：</td>
            <td width="250">
                <#if (purchaseVO.purchaseDate)?exists>
                <input type="text" name="orderDate" readOnly="readOnly" value="${purchaseVO.purchaseDate?string("yyyy-MM-dd")}" maxlength="60" class="_dateField _req input-text lh30" size="20">
                <#else>
                <input type="text" name="orderDate" readOnly="readOnly" value="" maxlength="60" class="_dateField _req input-text lh30" size="20">
                </#if>
            </td>
            
            <td class="td_right">订单状态：</td>
            <td align="left" >${statusShow!}
            </td>
        </tr>
        <tr >
            <td class="td_right" align="center"><font color="red">*</font>供应商：</td>
            <td >
                <input type="text" size="5" value="${(purchaseVO.supplierId)!}" class="input-text lh30" id="all_page_add_supplier_id">
                <input type="text" size="15" value="${(purchaseVO.supplierName)!}" class="input-text lh30" id="all_page_add_supplier_name" name="supplierName">
                <input type="hidden" id="supplierType" value="sell" name="supplierType">
                <input type="button" class="ext_btn ext_btn_submit" id="all_page_add_supplier_confirm" value="确定">
                <input type="button" value="查询" id="all_page_add_supplier_query" class="ext_btn">
                <input type="hidden" value="${(purchaseVO.supplierId)!}" id="all_page_add_supplier_id_hidden" class="_req" name="supplierId">
            </td>
            <td class="td_right" ><font color="red">*</font>税率：</td>
            <td >
                <div class="select_border">
                    <div class="select_containers">
                        <span class="fl">
                            <select class="_req select" name="rate" style="width:150px;">
                        	    <#list taxRateVOs as taxRateVO>
                         		<#if taxRateVO.rate==purchaseVO.rate>
                            		<option value="${taxRateVO.rate}" selected="selected">${taxRateVO.rate}%</option>
                        		<#else>  
                        			<option value="${taxRateVO.rate}">${taxRateVO.rate}%</option>
                            	</#if>
                                </#list>
                            </select>
                         </span>
                     </div>
                </div>
            </td>
            <td class="td_right" ><font color="red">*</font>币别：</td>
            <td>
                <div class="select_border">
                    <div class="select_containers">
                        <span class="fl">
                            <select class="_req select" name="currency" style="width:150px;" id="currencySelId">
                                <#list supplierCurrencyTypes?keys as key>
                                <#if key==purchaseVO.currency>
                                <option value="${key}"> ${supplierCurrencyTypes[key]}</option>
                                <#else>
                                <option value="${key}"> ${supplierCurrencyTypes[key]}</option>
                                </#if>
                                </#list>
                            </select>
                         </span>
                     </div>
                </div>
            </td>
        </tr>
        
        <tr>
            <td class="td_right" align="center" ><font color="red">*</font>仓库： </td>
            <td >
                <input type="text" size="5" value="${(purchaseVO.warehouseId)!}" class="input-text lh30" id="pur_order_add_warehouse_id">
                <input type="text" size="15" class="input-text lh30" value="${(purchaseVO.warehouseName)!}" id="pur_order_add_warehouse_name" name="warehouseName">
                <input type="button" class="ext_btn ext_btn_submit" id="pur_order_add_warehouse_confirm" value="确定">
                <input type="button" value="查询" id="pur_order_add_warehouse_query" class="ext_btn">
                <input type="hidden" id="pur_order_add_warehouse_id_hidden" value="${(purchaseVO.warehouseId)!}" class="_req" name="warehouseId">
            </td>
            <td class="td_right"><font color="red">*</font>期望日期：</td>
            <td > 
            <div class="select_border">
                <#if purchaseVO.expectDate?exists>
                <input type="text" name="expectDate" value="${purchaseVO.expectDate?string("yyyy-MM-dd")}" class="_dateField _req input-text lh30" size="8"/>
                <#else>
                <input type="text" name="expectDate" class="_dateField _req input-text lh30" size="20"/>
                </#if>
            </div>
            </td>
            
            <td class="td_right" align="left" ><font color="red">*</font>汇率：</td>
            <td >
                <input type="text" name="exchangeRate" value="${(purchaseVO.exchangeRate)!}"  id="exchangeRateId" maxlength="60" class="_req input-text lh30" size="10">CNY
            </td>
        </tr>
        
        <tr>
        	<td class="td_right" ><font color="red">*</font>订单类型：</td>
        	<td >
        		<div class="select_border">
                <div class="select_containers">
                    <span class="fl">
                        <select class="_req select" name="orderTypeLevel" style="width:150px;">
                               <#if purchaseVO.purchaseTypeLevel == '1'>
                                   <option value="1" selected="selected">普通订单</option>
                                   <option value="0">紧急订单</option>
                               <#elseif purchaseVO.purchaseTypeLevel == '0'>
                                   <option value="1">普通订单</option>
                                   <option value="0" selected="selected">紧急订单</option>
                               <#else>
                                   <option value="1">普通订单</option>
                                   <option value="0">紧急订单</option>
                               </#if>
                               
                        </select>
                     </span>
                 </div>
            </div>
        	</td>
        	
        	<td class="td_right" align="left" >备注：</td>
            <td>
                <input type="text" name="orderDesc" value="${(purchaseVO.purchaseDesc)!}" maxlength="60" class="input-text lh30" size="20">
            </td>
        	
        	 <td class="td_right" >
        	    <#if purchaseVO.isConfirm == 1>
                <input value="1" name="isConfirm" checked="true" type="checkbox">
                <#else>
                <input value="1" name="isConfirm" type="checkbox"/>
                </#if> 订单已确认
             </td>
             <td>
             </td>
        	
        </tr>
    </tbody>
</table>

