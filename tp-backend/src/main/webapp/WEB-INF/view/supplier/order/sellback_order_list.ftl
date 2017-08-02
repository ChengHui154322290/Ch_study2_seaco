<#include "/common/common.ftl"/> 
<#include "/supplier/common/page.ftl" />
<@backend title="代销退货单列表" 
    js=['/statics/supplier/js/common.js',
        '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
        '/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
        '/statics/supplier/js/validator.js',
        '/statics/supplier/js/web/sellback_order_list.js'
       ] 
    css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
            <div class="box_top">
                <b class="pl15">采购管理->采购->代销退货单</b> 
            </div>
                <form id="sell_orderback_list_form" class="jqtransform" action="/supplier/sellorderbackList.htm" method="post">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td class="td_right" width="50" align="right">供应商编号：</td>
                                <td class="" width="50" align="left">
                                   <input type="text"  name="supplierId" class="input-text lh30" size="17" value=${(purchaseDO.supplierId)!}>
                                </td>
                                <td class="td_right" width="50" align="left">供应商名称：</td>
                                <td class="" width="50" align="left">
                                    <input type="text" name="supplierName" class="input-text lh30" size="17" value=${(purchaseDO.supplierName)!}>
                                </td>
                                <td class="td_right" width="50" align="left">状态：</td>
                                <td align="left" style="width:50px;">
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
                                <td class="td_right" width="50" align="left">类型：</td>
                                <td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> 
                                            <select class="select" name="orderTypeLevel"
                                                style="width: 130px;">
                                                    <option value="">全部</option>
	                                                <#if purchaseDO?? && purchaseDO.purchaseTypeLevel == '1'>
	                                                    <option selected="selected" value="1">普通退单</option>
	                                                    <option value="0">紧急退单</option>
	                                               	<#else>
	                                                	<#if purchaseDO?? && purchaseDO.purchaseTypeLevel == '0'>
	                                                    	<option value="1">普通退单</option>
	                                                    	<option selected="selected" value="0">紧急退单</option>
	                                                    <#else>
	                                                    	<option value="1">普通退单</option>
	                                                    	<option value="0">紧急退单</option>
	                                                    </#if>
	                                               </#if>
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            
                            <tr>
                                <td class="td_right" width="50" align="left">退货单编号：</td>
                                <td class="" width="50" align="left">
                                    <input type="text" name="purId" class="input-text lh30" size="17" alue=${(purchaseDO.id)!}>
                                </td>
                               <td class="td_right" width="50" align="left">退单确认：</td>
                                <td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> 
                                            <select class="select" name="isConfirm"
                                                style="width: 130px;">
                                                    <option value="">全部</option>
	                                                <#if purchaseDO?? && purchaseDO.isConfirm == 'true'>
	                                                    	<option selected="selected" value="1">退单已确认</option>
	                                                    	<option value="0">退单未确认</option>
	                                               	<#else>
	                                                	<#if purchaseDO?? && purchaseDO.isConfirm == 'false'>
	                                                    	<option value="1">退单已确认</option>
	                                                    	<option selected="selected" value="0">退单未确认</option>
	                                                    <#else>
	                                                    	<option value="1">退单已确认</option>
	                                                    	<option value="0">退单未确认</option>
	                                                    </#if>
	                                               </#if>
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                                <td class="td_right" align="left">退单日期：</td>
                                <td align="left" colspan="3">
                                   <input type="text" id="c_starDate_8" name="startTime" class="_dateField input-text lh30" size="20" value="<#if purchaseDO.startTime??>${purchaseDO.startTime?string("yyyy-MM-dd")}</#if>">&nbsp;~&nbsp;<input type="text" name="endTime" id="c_endDate_8" class="_dateField input-text lh30" size="20" value="<#if purchaseDO.endTime??>${purchaseDO.endTime?string("yyyy-MM-dd")}</#if>">
                                </td>
                            </tr>

                            <tr>
                                <td colspan="8" align="center"><input type="reset" id="sellOrderBackListQueryReset"
                                    name="button" class="btn btn82 btn_res" value="重置">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                    <input type="button"
                                        name="button" id="sellOrderBackListQuery" class="btn btn82 btn_search" value="查询">
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="search_bar_btn  pt10 pb10 box_border" style="padding-left:5px;">
                <input type="button" name="button" id="sell_orderback_list_add_btn" class="btn btn82 btn_add" value="新增">&nbsp;&nbsp;
                <#--<input type="button" name="button" id="" class="btn btn82 btn_add" value="复制">-->
            </div>
            <div class="box_center  pt10 pb10">
                <table width="100%" cellspacing="0" cellpadding="0" border="0"
                    class="list_table CRZ" id="CRZ0">
                    <tbody>
                        <tr align="center">
                            <th width="9%">代销退货单编号</th>
                            <th width="9%">供应商编号</th>
                            <th width="34%">供应商名称</th>
                            <th width="9%">商品数量</th>
                            <th width="9%">退货总金额</th>
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
           <@pager  pagination=page  formId="sell_orderback_list_form"  />
      </form>

</@backend>
