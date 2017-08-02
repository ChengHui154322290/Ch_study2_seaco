<#include "/supplier/pop_table/context.ftl"/>
<@backend title="添加合同" js=[] 
    css=[] >
<div id="contractPopTable" style="z-index: 19891099;width: 821px; background: none repeat scroll 0% 0% rgb(255, 255, 255);">
    <div class="box_center">
        <form method="post" action="#" class="jqtransform" id="contract_search_form">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                <tbody>
                    <tr>
                        <td width="20" style="text-align:left;width:25px;" class="td_left">合同编号：</td>
                        <td width="50" align="left" class=""><input type="text" size="20" class="input-text lh30" name="contractCode"></td>
                        <td width="20" style="text-align:left;width:25px;" class="td_left">合同名称：</td>
                        <td width="50" align="left" class=""><input type="text" size="20" class="input-text lh30" name="contractName"></td>
                    </tr>
                    <tr>
                        <td align="left" colspan="4">
                            <input type="button" value="查询" class="btn btn82 btn_search" id="contractListQuery" name="button">
                            <input type="button" value="确定" class="btn btn82 btn_save2" id="contractListConfirm" name="button">
                        </td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" name="index" value="1" id="pageIndexId">
            <input type="hidden" name="supplierId" value="${supplierId}">
        </form>
        <div style="width: 821px; background: none repeat scroll 0% 0% rgb(255, 255, 255);">
	        <hr style="border: 1px dashed #247DFF;">
		    <div class="box_center" style="overflow: auto;height: 255px;">
		        <table width="100%" style="height:50px;overflow:scroll;" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
		            <tbody>
		                <tr>
		                    <td colspan="8">
		                        <table width="100%" cellspacing="0" cellpadding="0" border="0" id="CRZ0" class="list_table CRZ">
		                            <tbody>
		                                <tr align="center">
		                                    <th></th>
											<th style="text-align:center;">合同Id</th>
		                                    <th style="text-align:center;">合同编号</th>
		                                    <th style="text-align:center;">合同名称</th>
		                                </tr>
		                                <#list contracts.data as contract>
		                                <tr align="center" style="background-color: rgb(255, 255, 255);" class="tr">
		                                    <td style="text-align:center;">
		                                        <input name="contractIdSel" type="radio" dataName="${contract.contractName}" contractCode="${contract.contractCode}" dataName2="${contractTypesMap[contract.contractType]}" value="${contract.id}" />
		                                    </td>
		                                    <td style="text-align:center;">${contract.id}</td>
		                                    <td style="text-align:center;">${contract.contractCode}</td>
		                                    <td style="text-align:center;">${contract.contractName}</td>
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
</@backend>