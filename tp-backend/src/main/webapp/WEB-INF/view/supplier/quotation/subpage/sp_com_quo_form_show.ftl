<#--表单页面-->
<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <tbody>
        <tr >
           <td class="td_right" align="center">
               报价单编号： 
           </td>
           <td style="width:250px;">
               <input type="text" name="quoId" id="quoId" value="${(quotationVO.id)!}" readOnly="readOnly" class="input-text lh30" size="20">
           </td>
           <td class="td_right" align="left">
                <font color="red">*</font>名称： 
           </td>
           <td style="width:330px;">
               <input type="text" name="quotationName" readOnly="readOnly" value="${(quotationVO.quotationName)!}" class="_req input-text lh30" size="20">
           </td>
           <td class="td_right" align="center">
               	状态： 
           </td>
           <td>${statusShow!'新建'}
           </td>
        </tr>
        
        <tr>
            <td class="td_right" align="center"><font color="red">*</font>供应商/商家： 
            </td>
            <td>
                <input type="text" id="quotation_add_supplier_id" value="${(quotationVO.supplierId)!}" class="input-text lh30" readOnly="readOnly" size="5">
                <input type="text" name="supplierName" id="quotation_add_supplier_name" value="${(quotationVO.supplierName)!}" class="input-text lh30" readOnly="readOnly" size="10">
                <input type="hidden" name="supplierId" class="_req" value="${(quotationVO.supplierId)!}" id="quotation_add_supplier_id_hidden" />
            </td>
        
            <td class="td_right" > 
                <font color="red">*</font>合同编号： 
            </td>
            <td>
                <input type="text" readOnly="readOnly" id="quotation_add_contract_code" value="${(quotationVO.contractCode)!}" class="input-text lh30" size="5">
                <input type="text" readOnly="readOnly" name="contractName" id="quotation_add_contract_name" value="${(quotationVO.contractName)!}" class="input-text lh30" size="10">
                <input type="hidden" name="contractId" value="${(quotationVO.contractId)!}" id="quotation_add_contract_id_hidden" class="_req">
                <input type="hidden" name="contractCode" value="${(quotationVO.contractCode)!}" id="quotation_add_contract_id_hidden" class="_req">
            </td>
            <td class="td_right" width="50">
                <font color="red">*</font>合同类型：
            </td>
            <td><#--自营 -->
                <#if supplierTypes?exists>
                    <input type="text" readOnly="readOnly" id="quotation_add_contract_type" value="${supplierTypes[quotationVO.contractType]}" class="_req input-text lh30" size="20" />
                <#else>
                    <input type="text" readOnly="readOnly" id="quotation_add_contract_type" class="_req input-text lh30" size="20" />
                </#if>
            </td>
         </tr>
         <tr>
            <td class="td_right">备注:
            </td>
            <td>
                <input type="text" name="quotationDesc" readOnly="readOnly" value="${(quotationVO.quotationDesc)!}" class="input-text lh30" size="20">
            </td>
            <td class="td_right" align="left"><font color="red">*</font>有效日期：
            </td>
            <td><input type="text" name="startTime" readOnly="readOnly" value="${(quotationVO.startDate)?string('yyyy-MM-dd')}" class="input-text lh30" size="8">到
                <input type="text" name="endTime" readOnly="readOnly" value="${(quotationVO.endDate)?string('yyyy-MM-dd')}" class="input-text lh30" size="8">
            </td>
            <td class="td_right" colspan="2"></td>
        </tr>
       
        <tr>
           <td colspan="6"> <hr style="border:1px dashed #247DFF;" /></td>
        </tr>
     </tbody>
</table>