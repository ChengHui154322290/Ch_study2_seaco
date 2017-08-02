<#include "/common/common.ftl"/> 
<#include "/supplier/common/page.ftl" />
<@backend title="仓库预约单" 
    js=['/statics/supplier/js/common.js',
    '/statics/backend/js/layer/layer.min.js',
        '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
        '/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
        	'/statics/supplier/js/validator.js',
        '/statics/supplier/js/web/warehouseorder_list.js'
       ] 
    css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
            <div class="box_top">
                <b class="pl15">采购管理->采购->仓库预约单</b> 

            </div>
            	<!--by zhs 01132104 修改action -->
                <form id="warehouseorder_list_form" class="jqtransform" action="${domain}/supplier/warehouseorderList.htm" method="post">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
					            <td class="td_right" style="width:13%;" align="right">供应商编号：</td>
                                <td class="" style="width:6%;" align="left"><input type="text"
                                    name="supplierId" class="input-text lh30" size="12" value=${(purchaseWarehouseDO.supplierId)!}></td>

                                <td class="td_right" style="width:12%;"  align="left">供应商名称：</td>
                                <td class="" style="width:6%;" align="left"><input type="text"
                                    name="supplierName" class="input-text lh30" size="12" value=${(purchaseWarehouseDO.supplierName)!}></td>

                                <td class="td_right" style="width:10%;"  align="left">预约单编号：</td>
                                <td class="" style="width:6%;" align="left">

                                	<input type="text" name="id" class="input-text lh30" size=15 value=${(purchaseWarehouseDO.id)!}>

                                	<#--<input type="text" name="bookingCode" class="input-text lh30" size=“17” value=${(purchaseWarehouseDO.bookingCode)!}>-->

                                </td>
                                
                                <td class="td_right" style="width:10%;"  align="left">预约日期：</td>
                                <td align="left" style="width:40%;" class="">
                                   <input type="text" id="c_starDate_9" name="bookingStartDate" class="_dateField input-text lh30" size="12" value="<#if purchaseWarehouseDO.queryBookingStartDate??>${purchaseWarehouseDO.queryBookingStartDate?string("yyyy-MM-dd")}</#if>">&nbsp;~&nbsp;<input type="text" id="c_endDate_9" name="bookingEndDate" class="_dateField input-text lh30" size="12" value="<#if purchaseWarehouseDO.queryBookingEndDate??>${purchaseWarehouseDO.queryBookingEndDate?string("yyyy-MM-dd")}</#if>">
                                </td>
                                    
                            </tr>
                            
                            <tr>
                                <td class="td_right" align="center">仓库名称： </td>
                                <td style="width:6%;" class="">
					                <input type="text" name="warehouseName"  maxlength="60" size="12" class="_req input-text lh30" value=${(purchaseWarehouseDO.warehouseName)!}>
					            </td>
                               
                                <td class="td_right" align="left">单据类型：</td>
                                <td align="left" style="width:6%;" class="">
                                    <div class="select_border" style="vertical-align:middle;width:100%;">
                                        <div class="select_containers" style="width:100%;">
                                            <span class="fl" style="width:100%;"> 
                                            <select class="select" name="purchaseType"
                                                style="width: 100%;">
                                                    <option value="">全部</option>
                                                    <#if PURCHARSE_TYPE_MAP?exists>
						                                <#list PURCHARSE_TYPE_MAP?keys as key>
						                                   <#if purchaseWarehouseDO?? && purchaseWarehouseDO.purchaseType == key>
						                                   <option selected="selected" value="${key}">${PURCHARSE_TYPE_MAP[key]}</option>
						                                   <#else>
						                                   <option value="${key}">${PURCHARSE_TYPE_MAP[key]}</option>
						                                   </#if>
						                                </#list>
						                            </#if>
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                                
                                <td class="td_right" style="width:10%;" align="left" >状态：</td>
                                <td align="left" style="width:6%;" class="">
                                    <div class="select_border" style="width:100%;">
                                        <div class="select_containers" style="width:100%;">
                                            <span class="fl" style="width:100%;"> 
                                            <select class="select" name="auditStatus"
                                                style="width: 100%;">
                                                    <option value="">全部</option>
                                                    <#if PURCHARSE_STATUS_MAP?exists>
						                                <#list PURCHARSE_STATUS_MAP?keys as key>
						                                   <#if purchaseWarehouseDO?? && purchaseWarehouseDO.auditStatus == key>
						                                   <option selected="selected" value="${key}">${PURCHARSE_STATUS_MAP[key]}</option>
						                                   <#else>
						                                   <option value="${key}">${PURCHARSE_STATUS_MAP[key]}</option>
						                                   </#if>
						                                </#list>
						                            </#if>                                                    
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>

								<td class="td_right" style="width:10%;" >单据日期：</td>
                                <td align="left" style="width:40%;" class="">
                                   <input type="text" id="c_starDate_10" name="orderStartDate" class="_dateField _appdate input-text lh30" size="12" value="<#if purchaseWarehouseDO.queryOrderStartDate??>${purchaseWarehouseDO.queryOrderStartDate?string("yyyy-MM-dd")}</#if>">&nbsp;~&nbsp;<input type="text" name="orderEndDate" id="c_endDate_10" class="_dateField input-text lh30" size="12" value="<#if purchaseWarehouseDO.queryOrderEndDate??>${purchaseWarehouseDO.queryOrderEndDate?string("yyyy-MM-dd")}</#if>">
                                </td>
                            </tr>
							
                            <tr>
                                <td colspan="8" align="center"><input type="reset" id="warehouseorderListQueryReset"
                                    name="button" class="btn btn82 btn_res" value="重置">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                    <input type="button"
                                        name="button" id="warehouseorderListQuery" class="btn btn82 btn_search" value="查询">
                                </td>
                            </tr>
                        </tbody>
                    </table>
                     <div class="search_bar_btn  pt10 pb10 box_border" style="padding-left:5px;">
                <input type="button" name="button" id="warehouseorder_list_add" class="btn btn82 btn_add" value="新增">
            </div>
            <div class="box_center  pt10 pb10">
                <table width="100%" cellspacing="0" cellpadding="0" border="0"
                    class="list_table CRZ" id="CRZ0">
                    <tbody>
                        <tr align="center">
                        	<th width="8%">预约单编号</th>
                            <th width="10%">预约日期</th>
                            <th width="22%">供应商名称</th>
                            <th width="22%">仓库名称</th>
                            <th width="8%">单据类型</th>
                            <th width="8%">单号</th>
                            <th width="8%">状态</th>
                            <th>操作</th>
                        </tr>
                        
				<#if page.rows?default([])?size !=0>       
                    <#list page.rows as wh>
                        <tr align="center" class="tr"
                            style="background-color: rgb(255, 255, 255);">
                            <td>${wh.id}</td>
                            <td>${(wh.bookingDate?string("yyyy-MM-dd HH:mm:ss"))!}</td>
                            <td>${(wh.supplierName)!}</td>
                            <td class="line-compress">${(wh.warehouseName)!}</td>
                            <td>${(PURCHARSE_TYPE_MAP[wh.purchaseType])!}</td>
                            <td>${(wh.purchaseCode)!}</td>
                            <td>${PURCHARSE_STATUS_MAP[wh.auditStatus?c]}</td>
                            <td>

                                <a href="javascript:void(0)" onclick="showDetailPage(${wh.id})">[详情]</a>
                                <#if wh.auditStatus==3 >
                                    <a href="javascript:void(0)"  orderId="${wh.id}" class="order-fact" >[入库信息]</a><#else > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </#if>
                            </td>
                        </tr>
                        </#list>
                   </#if>
                    </tbody>
                </table>               
            </div>
            <@pager  pagination=page  formId="warehouseorder_list_form"  />
         </form>

</@backend>
