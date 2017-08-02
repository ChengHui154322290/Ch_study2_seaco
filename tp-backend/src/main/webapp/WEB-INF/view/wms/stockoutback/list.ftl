<#include "/common/common.ftl"/>
<@backend title="出库回执单列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
'/statics/backend/js/wms/stockoutback.js'
] css=[
'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css',
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >

	<!-- 查询条件开始 -->
<form method="post" action="${domain}/wms/stockoutBack/list.htm" id="stockOutBackForm">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">出库回执单管理</b></div>
            <div class="box_center pt10 pb10">
	       	<table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  	<td class="td_right">订单号：</td>
                  	<td><input type="text" id="orderCode" name="orderCode" class="input-text lh25" size="20" value='${stockOutBackReq.orderCode}'></td>
                  	<td class="td_right">快递单号：</td>
                  	<td><input type="text" id="expressNo" name="expressNo" class="input-text lh25" size="20" value='${stockOutBackReq.expressNo}'></td>
                  	<td class="td_right">状态：</td>
                  	<td>
                  	<div class="select_border"> 
                        <div class="select_containers "> 
                        <input type="hidden" id="statusSelected" value="${stockOutBackReq.status}" />
		                    <select id="status" name="status" class="select">
		                    	<option value="">全部状态</option>
		                    	<option value="1">成功</option>
		                    	<option value="0">失败</option>
		                    </select>
                    	</div>
                    </div>
                  	</td>
                </tr>
            </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="$('#stockOutBackForm').submit();" type="button" value="查询" name="button" /></a>
                <!-- <a href="javascript:void(0);"><input class="btn btn82 btn_add warehouseaddbtn" id="updateStatus" type="button" value="更新" name="button"/></a> -->
                 <input type="button" id="selectExpressBtn"/>
                 <input type="hidden" id="selectIds" value=""/>
              </div>
            </div>
          </div>
        </div>
    </div>
    <!-- 查询条件结束 -->
    <!-- 列表开始 -->
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                	 <th width="50"><input type="checkBox" id="checkALL" value="${itemSkuDO.sku}">全选</th>
                	 <th width="100">订单号</th>
                	 <th width="100">仓库编码</th>
                	 <th width="100">运单号</th>
                	 <th width="100">承运商</th>
                	 <th width="100">包裹重量</th>
                	 <th width="100">审核人</th>
                	 <th width="100">审核时间</th>
                	 <th width="100">状态</th>
                	 <th width="100">创建时间</th>
                	 <th width="150">操作</th>
                </tr>
                <#if  stockOutBackPages.rows?? >
   						<#list stockOutBackPages.rows as stockOutBack>
				            <tr class="tr" >
				            	<td align="center">
								<input type="checkBox" name="stockoutbackCheck" value="${stockOutBack.id}"> 
								</td>
				            	<td class="td_center">${stockOutBack.orderCode}</td>
				            	<td class="td_center">${stockOutBack.warehouseCode}</td>
				            	<td class="td_center">${stockOutBack.expressNo}</td>
				            	<td class="td_center">${stockOutBack.logisticsCompanyName}</td>
				            	<td class="td_center">${stockOutBack.weight}</td>
				            	<td class="td_center">${stockOutBack.auditor}</td>
				            	<td class="td_center"><#if stockOutBack.auditTime>${stockOutBack.auditTime?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
				            	<td class="td_center">
									<#if '${stockOutBack.status?if_exists}'=='0'>
										失败
									</#if>
									<#if '${stockOutBack.status?if_exists}'=='1'>
										成功
									</#if>
		                    	</td>
				            	<td class="td_center">${stockOutBack.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
				                <td align="center">
				                <a href="javascript:void(0)" onclick="viewItem(${stockOutBack.id})">[明细]</a>
							    </td>
				             </tr>
	           		 </#list>
				</#if>
              </table>
              <@pager pagination=stockOutBackPages formId="stockOutBackForm" /> 
	     </div>
	</div>
	<!-- 列表结束 -->
</form>
</@backend>