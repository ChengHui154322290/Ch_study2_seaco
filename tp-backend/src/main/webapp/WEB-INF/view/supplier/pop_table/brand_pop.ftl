<#include "/supplier/pop_table/context.ftl"/>
<@backend title="添加品牌" js=[] 
    css=[] >
<script type="text/javascript">
   var daleiHtml = '${categoryHtml}';
</script>
<div class="container" id="brandPopTable" style="z-index: 19891099;">
    <form method="post" action="#" class="jqtransform" id="brand_search_form">
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
            <tbody>
                <tr>
                    <td width="20" style="text-align:left;width:25px;" class="td_left">品牌编号：</td>
                    <td width="50" align="left" class=""><input type="text" size="17" class="input-text lh30" name="brandId"></td>
                    <td width="20" style="text-align:left;width:25px;" class="td_left">品牌名称：</td>
                    <td width="50" align="left" class=""><input type="text" size="17" class="input-text lh30" name="brandName"></td>
                </tr>
                <tr>
                    <td align="left" colspan="4">
                        <input type="button" value="查询" class="btn btn82 btn_search" id="brandListQuery" name="button">
                        <input type="button" value="确定" class="btn btn82 btn_save2" id="brandListConfirm" name="button">
                    </td>
                </tr>
            </tbody>
        </table>
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
	                                    <th style="text-align:center;">品牌编号</th>
	                                    <th style="text-align:center;">品牌名称</th>
	                                </tr>
	                                <#list brands as brand>
	                                <tr align="center" style="background-color: rgb(255, 255, 255);" class="tr">
	                                    <td style="text-align:center;"><input name="brandIdSel" type="checkbox" dataName="${brand.name}" value="${brand.id}" /></td>
	                                    <td style="text-align:center;">${brand.id}</td>
	                                    <td style="text-align:center;">${brand.name}</td>
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