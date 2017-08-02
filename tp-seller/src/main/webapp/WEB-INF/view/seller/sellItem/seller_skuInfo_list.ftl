<#include "/common/common.ftl"/> 
<#include "/supplier/common/page.ftl" />
<@backend title="商家商品审核" 
    js=[
    '/static/seller/js/item/item-seller.js',
    '/static/seller/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
    '/static/seller/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js'
       ] 
    css=['/static/seller/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">商品管理->商家商品审核</b> 
            </div>
            <div class="box_center">
                <form id="querySkuForm" class="jqtransform" action="${domain}/item/seller_skuInfo_list.htm" method="post">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td class="td_right" width="12.5%" align="right">名称：</td>
                                
                                <td class="" width="12.5%" align="left">
                                   <input type="text"  name="detailName" class="input-text lh30" size="17" value=${(sellerSkuQuery.detailName)!}>
                                </td>
                                
                                <td class="td_right" width="12.5%" align="left">单位：</td>
                                
                                <td class="" width="12.5%" align="left">
                                    <input type="text" name="unitName" class="input-text lh30" size="17" value=${(sellerSkuQuery.unitName)!}>
                                </td>
                                
                                <td class="td_right" width="12.5%" align="left">品牌：</td>
                                
                                <td class="" width="12.5%" align="left">
                                    <input type="text" name="brandName" class="input-text lh30" size="17" value=${(sellerSkuQuery.brandName)!}>
                                </td>
                                
                                <td class="td_right" align="left">创建日期日期：</td>
                                
                                <td align="left" colspan="7"> 
					                  <td colspan="3">
					                  	<input type="text" name="createBeginTime" id="createBeginTime" value="<#if sellerSkuQuery.createBeginTime??>${sellerSkuQuery.createBeginTime?string("yyyy-MM-dd")}</#if>" class="input-text lh25" size="20">
					                  	<span>到</span>
					                  	<input type="text" name="createEndTime" id="createEndTime" value="<#if sellerSkuQuery.createBeginTime??>${sellerSkuQuery.createEndTime?string("yyyy-MM-dd")}</#if>" class="input-text lh25" size="20">
					                  </td>
                                </td>
                            </tr>
                           
                            <tr>
                                <td class="td_right" width="12.5%" align="left">商户：</td>
                                
                                <td class="" width="12.5%" align="left">
                                    <input type="text" name="supplierName" class="input-text lh30" size="17" value=${(sellerSkuQuery.supplierName)!}>
                                </td>
                            </tr>

                            <tr>
                                <td colspan="8" align="center">
                              <a href="${domain}/item/seller_skuInfo_list.htm"><input type="button" id="purchaseOrderListQueryReset" name="button" class="btn btn82 btn_res" value="重置"></a>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                    <input  type="submit" class="btn btn82 btn_search" value="查询">
                                </td>
                            </tr>
                        </tbody>
                    </table>
					            <div class="box_center  pt10 pb10">
                <table cellspacing="0" cellpadding="0" border="0" width="100%"
                    class="form_table pt15 pb15">
                    <tbody>
                        <tr>
                            <td colspan="8">
                                <table width="100%" cellspacing="0" cellpadding="0" border="0"
                                    class="list_table CRZ" id="CRZ0">
                                    <tbody>
                                        <tr align="center">
                                        	<th width="9%">商户编号</th>
                                            <th width="9%">商户名称</th>
                                            <th width="9%">条形码</th>
                                            <th width="9%">名称</th>
                                            <th width="9%">操作</th>
                                        </tr>
                                        <#if queryAllLikedofItemSkuByPage.rows?default([])?size !=0>       
                                        <#list queryAllLikedofItemSkuByPage.rows as sl>
                                        <tr align="center" class="tr"
                                            style="background-color: rgb(255, 255, 255);">
                                            <td>${sl.spId}</td>
                                            <td>${sl.spName}</td>
                                            <td>${sl.barcode}</td>
                                            <td>${sl.detailName}</td>                                       
                                            <td>
                                                <a href="javascript:void(0)" onclick="showAduitPage(${sl.id})">查看及审核</a>
                                            </td>
                                        </tr>
                                        </#list>
                                        </#if>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
                <@pager  pagination=queryAllLikedofItemSkuByPage  formId="querySkuForm"  /> 
                </form>
            </div>
        </div>
    </div>
</div>
</@backend>
