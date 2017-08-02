<#include "/supplier/pop_table/context.ftl"/>
<@backend title="添加仓库" js=[] 
    css=[] >
<div id="spAddStoragePopTable" style="z-index: 19891099;width: 821px; background: none repeat scroll 0% 0% rgb(255, 255, 255);">
    <#--
    <div class="box_top">
        <b class="pl15">添加</b>  
        <a class="box_top_r fr pr15" id="popClosebtn" href="javascript:void(0);">关闭</a>  
    </div>
    -->
    <div class="box_center">
        <form method="post" action="#" class="jqtransform" id="spAddStorage_search_form">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                <tbody>
                    <tr>
                        <td width="20" style="text-align:left;width:25px;" class="td_left">仓库ID：</td>
                        <td width="50" align="left" class=""><input type="text" size="20" class="input-text lh30" name="spAddStorageId"></td>
                        <td width="20" style="text-align:left;width:25px;" class="td_left">仓库名称：</td>
                        <td width="50" align="left" class=""><input type="text" size="20" class="input-text lh30" name="spAddStorageName"></td>
                    </tr>
                    <tr>
                        <td align="left" colspan="4">
                            <input type="button" value="查询" class="btn btn82 btn_search" id="spAddStorageListQuery" name="button">
                            <input type="button" value="确定" class="btn btn82 btn_save2" id="spAddStorageListConfirm" name="button">
                        </td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" name="supplierId" value="${supplierId}">
            <input type="hidden" name="index" value="1" id="pageIndexId">
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
		                                    <th style="text-align:center;">仓库ID</th>
		                                    <th style="text-align:center;">仓库编号</th>
		                                    <th style="text-align:center;">仓库名称</th>
		                                </tr>
		                                <#list spAddStorages as spAddStorage>
		                                <tr align="center" style="background-color: rgb(255, 255, 255);" class="tr">
		                                    <td style="text-align:center;"><input name="spAddStorageIdSel" type="radio" dataName="${spAddStorage.name}" value="${spAddStorage.id}" /></td>
		                                    <td style="text-align:center;">${spAddStorage.id}</td>
		                                    <td style="text-align:center;">${spAddStorage.code}</td>
		                                    <td style="text-align:center;" width="40%" class="line-compress" t-width="450">${spAddStorage.name}</td>
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