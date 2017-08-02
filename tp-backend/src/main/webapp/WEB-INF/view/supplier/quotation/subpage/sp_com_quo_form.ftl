<#--表单页面-->
<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <tbody>
        <tr >
           <td class="td_right" align="center">
               报价单编号： 
           </td>
           <td style="width:250px;">系统自动生成
           </td>
           <td class="td_right" align="left">
                <font color="red">*</font>名称： 
           </td>
           <td style="width:330px;">
               <input type="text" name="quotationName" class="_req input-text lh30" size="20">
           </td>
           <td class="td_right" align="center">
              	 状态： 
           </td>
           <td>新建
           </td>
        </tr>
        
        <tr>
            <td class="td_right" align="center"><font color="red">*</font>供应商/商家：
            </td>
            <td style="width:360px;">
                <#if supplier?exists>
                <input type="text" id="all_page_add_supplier_id" value="${supplier.id}" readOnly="readOnly" class="input-text lh30" size="5">
                <input type="text" name="supplierName" id="all_page_add_supplier_name"  readOnly="readOnly" value="${supplier.name}" class="input-text lh30" size="10">
                <input type="button" value="确定" class="ext_btn ext_btn_submit" disabled="true">
                <input type="button" class="ext_btn" value="查询"  disabled="true">
                <input type="hidden" name="supplierId" class="_req" id="all_page_add_supplier_id_hidden" readOnly="readOnly" value="${supplier.id}" />
                <#else>
                <input type="text" id="all_page_add_supplier_id" class="input-text lh30" size="5">
                <input type="text" name="supplierName" id="all_page_add_supplier_name"  class="input-text lh30" size="10">
                <input type="button" value="确定" id="all_page_add_supplier_confirm" class="ext_btn ext_btn_submit">
                <input type="button" class="ext_btn" id="all_page_add_supplier_query"  value="查询">
                <input type="hidden" name="supplierId" class="_req" id="all_page_add_supplier_id_hidden" />
                </#if>
                <input type="hidden" id="all_page_add_supplier_type" value="${(supplier.supplierType)!}"/>
            </td>
        
            <td class="td_right" > 
                <font color="red">*</font>合同编号： 
            </td>
            <td>
                <#if contract?exists>
                <input type="text" id="quotation_add_contract_code" class="input-text lh30" value="${contract.contractCode}" readOnley="readOnly" size="5">
                <input type="text" name="contractName" id="quotation_add_contract_name" value="${contract.contractName}"readOnley="readOnly"  class="input-text lh30" size="10">
                <input type="button" value="确定" id="quotation_add_contract_confirm" class="ext_btn ext_btn_submit" disabled="true">
                <input type="button" class="ext_btn" id="quotation_add_contract_query"  value="查询" disabled="true">
                <input type="hidden" name="contractId" id="quotation_add_contract_id_hidden" value="${contract.id}" readOnley="readOnly" class="_req">
                <input type="hidden" name="contractCode" id="quotation_add_contract_code_hidden" value="${contract.contractCode}" readOnley="readOnly" class="_req">
                <#else>
                <input type="text" id="quotation_add_contract_code" class="input-text lh30"  readOnley="readOnly" size="5">
                <input type="text" name="contractName" id="quotation_add_contract_name" class="input-text lh30" size="10">
                <input type="button" value="确定" id="quotation_add_contract_confirm" class="ext_btn ext_btn_submit">
                <input type="button" class="ext_btn" id="quotation_add_contract_query"  value="查询">
                <input type="hidden" name="contractId" id="quotation_add_contract_id_hidden" class="_req">
                <input type="hidden" name="contractCode" id="quotation_add_contract_code_hidden" readOnley="readOnly" class="_req">
                </#if>
            </td>
            <td class="td_right" width="50">
                <font color="red">*</font>合同类型：
            </td>
            <td><#--自营 -->
                <input type="text" readOnly="readOnly" id="quotation_add_contract_type" value="${(supplierTypes[contract.contractType])!}" name="contractType" class="_req input-text lh30" size="20" />
            </td>
         </tr>
         <tr>
            <td class="td_right" >备注:&nbsp;&nbsp;
            </td>
            <td>
                <input type="text" name="quotationDesc" class="input-text lh30" size="20">
            </td>
            <td class="td_right" align="left"><font color="red">*</font>有效日期：
            </td>
            <td style="width:500px"><input type="text" id="c_starDate_4" name="startTime" class="_req _dateField input-text lh30" size="8">到
                <input type="text" name="endTime" id="c_endDate_4" class="_req _dateField input-text lh30" size="8">
            </td>
            <td class="td_right" colspan="2"></td>
        </tr>
       
        <tr>
           <td colspan="6"> <hr style="border:1px dashed #247DFF;" /></td>
        </tr>
     </tbody>
</table>