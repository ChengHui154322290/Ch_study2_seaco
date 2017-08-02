<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <tbody>
        <tr>
            <td class="td_right">代销订单编号： </td>
            <td class=""  width="350">
                <span>系统自动生成</span>
            </td>
            <td class="td_right" align="left">订单日期：</td>
            <td width="250">
                <input type="text" name="orderDate" value="" maxlength="60" class="_dateField _req input-text lh30" defaultDate="nowDay" size="20">
            </td>
            
            <td class="td_right">订单状态：</td>
            <td align="left" >新建
            </td>
        </tr>
        <tr>
            <td class="td_right" align="center"><font color="red">*</font>供应商：</td>
            <td width="360">
                <input type="text" size="5" class="input-text lh30" id="all_page_add_supplier_id">
                <input type="text" size="10" class="input-text lh30" id="all_page_add_supplier_name" name="supplierName">
                <input type="hidden" id="supplierType" value="sell" name="supplierType">
                <input type="button" class="ext_btn ext_btn_submit" id="all_page_add_supplier_confirm" value="确定">
                <input type="button" value="查询" id="all_page_add_supplier_query" class="ext_btn">
                <input type="hidden" id="all_page_add_supplier_id_hidden" value="" class="_req" name="supplierId">
            </td>
            <td class="td_right"><font color="red">*</font>税率：</td>
            <td>
                <div class="select_border">
                    <div class="select_containers">
                        <span class="fl">
                            <select class="_req select" name="rate" style="width:150px;" id="rateSelId">
                                <#list taxRateVOs as taxRateVO>
                                    <option value="${taxRateVO.rate}">${taxRateVO.rate}%</option>
                                </#list>
                            </select>
                         </span>
                     </div>
                </div>
            </td>
             <td class="td_right"><font color="red">*</font>币别：</td>
            <td>
            <div class="select_border">
                <div class="select_containers">
                    <span class="fl">
                        <select class="_req select" name="currency" style="width:150px;" id="currencySelId">
                            <#list supplierCurrencyTypes?keys as key>
                            <option value="${key}"> ${supplierCurrencyTypes[key]}</option>
                            </#list>
                        </select>
                     </span>
                 </div>
            </div>
            </td>
        </tr>
        
        <tr>
            <td class="td_right" align="center" ><font color="red">*</font>仓库： </td>
            <td width="360">
                <input type="text" size="5" class="input-text lh30" id="pur_order_add_warehouse_id">
                <input type="text" size="10" class="input-text lh30" id="pur_order_add_warehouse_name" name="warehouseName">
                <input type="button" class="ext_btn ext_btn_submit" id="pur_order_add_warehouse_confirm" value="确定">
                <input type="button" value="查询" id="pur_order_add_warehouse_query" class="ext_btn">
                <input type="hidden" id="pur_order_add_warehouse_id_hidden" value="1" class="_req" name="warehouseId">
            </td>
            <td class="td_right"><font color="red">*</font>期望日期：</td>
            <td >
            <div class="select_border">
                <input type="text" name="expectDate" class="_dateField _req input-text lh30" size="20"/>
            </div>
            </td>
            
            <td class="td_right" align="left" ><font color="red">*</font>汇率：</td>
            <td >
                <input type="text" name="exchangeRate" value="1"  id="exchangeRateId" maxlength="60" class="_req _price input-text lh30" size="10">CNY
            </td>
        </tr>
        
        <tr>
            <td class="td_right" ><font color="red">*</font>订单类型：</td>
            <td >
                <div class="select_border">
                <div class="select_containers">
                    <span class="fl">
                        <select class="_req select" name="orderTypeLevel" style="width:150px;">
                               <option value="1">普通订单</option>
                               <option value="0">紧急订单</option>
                        </select>
                     </span>
                 </div>
            </div>
            </td>
            
            <td class="td_right" align="left">备注：</td>
            <td>
                <input type="text" name="orderDesc" value="" maxlength="60" class="input-text lh30" size="20">
            </td>
            
             <td class="td_right">
                <input value="1" name="isConfirm" type="checkbox">订单已确认
             </td>
             <td>
             </td>
        </tr>
    </tbody>
</table>

