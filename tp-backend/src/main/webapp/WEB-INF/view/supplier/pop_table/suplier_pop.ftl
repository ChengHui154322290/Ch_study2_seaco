<#include "/supplier/pop_table/context.ftl"/>
<@backend title="添加供应商" js=[] 
    css=[] >
<div id="supplierPopTable" style="z-index: 19891099;">
    <form method="post" action="#" class="jqtransform" id="supplier_search_form">
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
            <tbody>
                <tr>
                    <td style="text-align:left;width:90px;" class="td_left">供应商名称：</td>
                    <td><input type="text" size="20" class="input-text lh30" name="name" value="${(supplierInfo.name)!}"></td>
                </tr>
                <tr>
                    <td align="right" colspan="2">
                        <input type="button" value="查询" class="btn btn82 btn_search" id="supplierListQuery" name="button">
                        <input type="button" value="确定" class="btn btn82 btn_save2" id="supplierListConfirm" name="button">
                    </td>
                </tr>
            </tbody>
        </table>
        <input type="hidden" name="index" value="1" id="pageIndexId">
        <input type="hidden" name="supplierType" value="${(supplierType)!}" >
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
	                                    <th style="text-align:center;">供应商编号</th>
	                                    <th style="text-align:center;">供应商名称</th>
	                                    <th style="text-align:center;">供应商类型</th>
	                                </tr>
	                                <#list sList as supp>
	                                <tr align="center" style="background-color: rgb(255, 255, 255);" class="tr">
	                                    <td style="text-align:center;">
	                                        <input name="supplierIdSel" type="radio" dataName="${supp.name}" dataName2="${(supp.supplierType)!}"  value="${supp.id}" />
	                                        <input type="hidden" id="supplierType${supp.id}" value="${(supp.supplierType)!}" />
	                                        <input type="hidden" id="supplierTypeName${supp.id}" value="${(supplierTypeMap[supp.supplierType])!}" />
	                                    </td>
	                                    <td style="text-align:center;">${supp.id}</td>
	                                    <td style="text-align:center;">${supp.name}</td>
	                                    <td style="text-align:center;">${(supplierTypeMap[supp.supplierType])!}</td>
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
</@backend>