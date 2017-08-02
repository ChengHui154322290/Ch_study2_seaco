<#include "/common/common.ftl"/> 
<#include "/supplier/common/page.ftl" />
<@backend title="代销订单列表" 
    js=['/statics/supplier/js/common.js',
        '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
        '/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
        '/statics/supplier/js/validator.js',
        '/statics/supplier/js/web/sell_order_list.js'
       ] 
    css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">采购管理->采购->代销订单</b> 
            </div>
            <div class="box_center">
            	<!--by zhs 01132036 修改 action -->
                <form id="sell_order_list_form" class="jqtransform" action="${domain}/supplier/sellorderList.htm" method="post">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td class="td_right" width="12.5%" align="right">供应商编号：</td>
                                <td class="" width="12.5%" align="left">
                                   <input type="text"  name="supplierId" class="input-text lh30" size="17" value=${(purchaseDO.supplierId)!}>
                                </td>
                                <td class="td_right" width="12.5%" align="left">供应商名称：</td>
                                <td class="" width="12.5%" align="left">
                                    <input type="text" name="supplierName" class="input-text lh30" size="17" value=${(purchaseDO.supplierName)!}>
                                </td>
                                <td class="td_right" width="12.5%" align="left">状态：</td>
                                <td align="left" width="12.5%">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> 
                                            <select class="select" name="auditStatus"
                                                style="width: 130px;">
                                                    <option value="">全部</option>
                                                    <#if auditStatusMapStr?exists>
						                                <#list auditStatusMapStr?keys as key>
						                                   <#if purchaseDO?? && purchaseDO.auditStatus == key>
						                                   <option selected="selected" value="${key}">${auditStatusMapStr[key]}</option>
						                                   <#else>
						                                   <option value="${key}">${auditStatusMapStr[key]}</option>
						                                   </#if>
						                                </#list>
						                            </#if>
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                                <td class="td_right" width="12.5%" align="left">类型：</td>
                                <td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> 
                                            <select class="select" name="orderTypeLevel"
                                                style="width: 130px;">
                                                    <option value="">全部</option>
		                                                <#if purchaseDO?? && purchaseDO.purchaseTypeLevel == '1'>
		                                                    <option selected="selected" value="1">普通订单</option>
		                                                    <option value="0">紧急订单</option>
		                                               	<#else>
		                                                	<#if purchaseDO?? && purchaseDO.purchaseTypeLevel == '0'>
		                                                    	<option value="1">普通订单</option>
		                                                    	<option selected="selected" value="0">紧急订单</option>
		                                                    <#else>
		                                                    	<option value="1">普通订单</option>
		                                                    	<option value="0">紧急订单</option>
		                                                    </#if>
		                                               </#if>
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            
                            <tr>
                                <td class="td_right" align="left">代销订单编号：</td>
                                <td class="" align="left">
                                    <input type="text" name="purId" class="input-text lh30" size="17" value=${(purchaseDO.id)!}>
                                </td>
                               <td class="td_right" align="left">订单确认：</td>
                                <td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> 
                                            <select class="select" name="isConfirm"
                                                style="width: 130px;">
                                                    <option value="">全部</option>
	                                                <#if purchaseDO?? && purchaseDO.isConfirm == 'true'>
	                                                    	<option selected="selected" value="1">订单已确认</option>
	                                                    	<option value="0">订单未确认</option>
	                                               	<#else>
	                                                	<#if purchaseDO?? && purchaseDO.isConfirm == 'false'>
	                                                    	<option value="1">订单已确认</option>
	                                                    	<option selected="selected" value="0">订单未确认</option>
	                                                    <#else>
	                                                    	<option value="1">订单已确认</option>
	                                                    	<option value="0">订单未确认</option>
	                                                    </#if>
	                                               </#if>
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                                <td class="td_right" align="left">收货状态：</td>
                                <td align="left" colspan="3">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> 
                                            <select class="select" name="receiveStatus"
                                                style="width: 130px;">
                                                    <option value="">全部</option>
                                                   	<#if purchaseDO?? && purchaseDO.receiveStatus == '1'>
	                                                    	<option selected="selected" value="1">全部收货</option>
                                                    		<option value="0">部分收货</option>
	                                               	<#else>
	                                                	<#if purchaseDO?? && purchaseDO.receiveStatus == '0'>
	                                                    	<option value="1">全部收货</option>
                                                    		<option selected="selected" value="0">部分收货</option>
	                                                    <#else>
	                                                    	<option value="1">全部收货</option>
                                                    		<option value="0">部分收货</option>
	                                                    </#if>
	                                               </#if>
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            
                            <tr>
                                <td class="td_right" align="left">订单日期：</td>
                                <td align="left" colspan="7">
                                   <input type="text" id="c_starDate_7" name="startTime" class="_dateField input-text lh30" size="20" value="<#if purchaseDO.startTime??>${purchaseDO.startTime?string("yyyy-MM-dd")}</#if>">&nbsp;~&nbsp;<input type="text" name="endTime" id="c_endDate_7" class="_dateField input-text lh30" size="20" value="<#if purchaseDO.endTime??>${purchaseDO.endTime?string("yyyy-MM-dd")}</#if>">
                                </td>
                            </tr>

                            <tr>
                                <td colspan="8" align="center"><input type="reset" id="sellOrderListQueryReset"
                                    name="button" class="btn btn82 btn_res" value="重置">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                    <input type="button"
                                        name="button" id="sellOrderListQuery" class="btn btn82 btn_search" value="查询">
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="search_bar_btn  pt10 pb10 box_border" style="padding-left:5px;">
                		<input type="button" name="button" id="sell_order_list_add" class="btn btn82 btn_add" value="新增">&nbsp;&nbsp;
                		<#--<input type="button" name="button" id="" class="btn btn82 btn_add" value="复制">-->
            		</div>
            	<div class="box_center  pt10 pb10">
                	<table width="100%" cellspacing="0" cellpadding="0" border="0"
                   	 class="list_table CRZ" id="CRZ0">
                    <tbody>
                        <tr align="center">
                            <th width="9%">代销订单编号</th>
                            <th width="9%">供应商编号</th>
                            <th width="34%">供应商名称</th>
                            <th width="9%">商品数量</th>
                            <th width="9%">订单总金额</th>
                            <th width="9%">类型</th>
                            <th width="9%">状态</th>
                            <th>操作</th>
                        </tr>
                        <#if page.rows?default([])?size !=0>       
                        <#list page.rows as sl>
                        <tr align="center" class="tr"
                            style="background-color: rgb(255, 255, 255);">
                            <td>${sl.id}</td>
                            <td>${sl.supplierId}</td>
                            <td>${sl.supplierName}</td>
                            <td>${sl.totalCount}</td>
                            <td>${sl.totalMoney}</td>
                            <td>${orderTypeMap[sl.purchaseTypeLevel]}
                            </td>
                            <td>${auditStatusMapStr[sl.auditStatus?c]}</td>
                            <td>
                                <#if sl.auditStatus==1>
                                <a href="javascript:void(0)" onclick="showEditPage(${sl.id})">编辑</a>
                                <#elseif sl.auditStatus==5>
                                <a href="javascript:void(0)" onclick="showEditPage(${sl.id})">编辑</a>
                                <#else>
                                <a href="javascript:void(0)" onclick="showDetailPage(${sl.id})">详情</a>
                                </#if>
                            </td>
                        </tr>
                        </#list>
                        </#if>
                    </tbody>
                </table> 
            </div>
            <@pager  pagination=page  formId="sell_order_list_form"  />
      </form>
</@backend>
