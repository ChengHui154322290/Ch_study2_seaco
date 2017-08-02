<#include "/common/common.ftl"/>
<@backend title="出库报检信息列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
'/statics/backend/js/wms/stockout.js'
] css=[
'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css',
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >

	<!-- 查询条件开始 -->
<form method="post" action="${domain}/wms/stockout/list.htm" id="stockOutForm">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">出库单管理</b></div>
            <div class="box_center pt10 pb10">
	       	<table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  	<!-- <td>申报单号：</td>   
                  	<td><input type="text" id="externalNo" name="externalNo" class="input-text lh25" size="20" value='${stockOutDO.externalNo}'></td>
                  	--> 
                  	<td class="td_right">订单号：</td>
                  	<td><input type="text" id="orderCode" name="orderCode" class="input-text lh25" size="20" value='${stockOutReq.orderCode}'></td>
                  	<td class="td_right">快递单号：</td>
                  	<td><input type="text" id="expressNo" name="expressNo" class="input-text lh25" size="20" value='${stockOutReq.expressNo}'></td>
                  	<td class="td_right">状态：</td>
                  	<td>
                  	<div class="select_border"> 
                        <div class="select_containers "> 
                        <input type="hidden" id="statusSelected" value="${stockOutReq.status}"/>
		                    <select id="status" name="status" class="select">
		                    	<option value="">全部状态</option>
		                    	<option value="1">成功</option>
		                    	<option value="0">失败</option>
		                    </select>
                    	</div>
                    </div>
                  	</td>
                </tr>
                <tr>
                  <!--	<td>货主：</td>    
                  	<td><input type="text" id="storer" name="storer" class="input-text lh25" size="20" value='${stockOutReq.storer}'></td>
                  -->
                  	<td class="td_right">收货人姓名：</td>
                  	<td><input type="text" id="consignee" name="consignee" class="input-text lh25" size="20" value='${stockOutReq.consignee}'></td>
                  	<td class="td_right">收货人联系电话：</td>
                  	<td><input type="text" id="mobile" name="mobile" class="input-text lh25" size="20" value='${stockOutReq.mobile}'></td>
           	<!--
                </tr>
                <tr>
            -->
                <!--
                  	<td class="td_right">创建时间：</td>    
                  	<td colspan="3">
                  	<input type="text" name="startCreateTime" id="startCreateTime" readonly value='${stockOutReq.startCreateTime?if_exists}' readonly class="input-text lh25" size="20">
                  	<span>到</span>
                  	<input type="text" name="endCreateTime" id="endCreateTime" readonly  value='${stockOutReq.endCreateTime?if_exists}' readonly class="input-text lh25" size="20">
                  	</td>
                  	<td class="td_right" colspan="2"><a href="${domain}/wms/stockout/downStockOutTemplate.htm">[下载导入模板]</a></td>
              	-->
                </tr>
            </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="$('#stockOutForm').submit();" type="button" value="查询" name="button" /></a>
                 <!-- <a href="javascript:void(0);"><input class="btn btn82 btn_add warehouseaddbtn" id="updateStatus" type="button" value="更新" name="button"/></a>
                 <a href="javascript:void(0);" title = "运单导入"><input class="btn btn82 btn_export" type="button" id="importBtn" value="导入" name="button" /></a>
                 -->
                 <a href="javascript:void(0);" title = "出库单导出"><input class="btn btn82 btn_export" type="button" id="exportBtn" value="导出" name="button" /></a>
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
                	<!-- <th width="100">申报单号</th> -->
                	 <th width="100">订单号</th>
                	 <th width="100">仓库编码</th>
                	 <th width="100">仓库名称</th>
                	<!-- <th width="100">货主</th> -->
                	 <th width="100">收货人</th>
                	 <th width="100">收货人手机号</th>
                	 <th width="100">运单号</th>
                	 <th width="100">承运商</th>
                	 <th width="100">状态</th>
                	 <th width="100">创建时间</th>
                	 <th width="150">操作</th>
                </tr>
                <#if  stockOutPages.rows?? >
   						<#list stockOutPages.rows as stockOut>
				            <tr class="tr" >
				            	<td align="center">
				            	<#if '${stockOut.status?if_exists}'!='60' && '${stockOut.status?if_exists}'!='90' && '${stockOut.status?if_exists}'!='91'>
								<input type="checkBox" name="stockoutCheck" value="${stockOut.id}"> 
								</#if>
								</td>
				            	<td class="td_center">${stockOut.orderCode}</td>
				            	<td class="td_center">${stockOut.warehouseCode}</td>
				            	<td class="td_center">${stockOut.warehouseName}</td>
				            	<td class="td_center">${stockOut.consignee}</td>
				            	<td class="td_center">${stockOut.mobile}</td>
				            	<td class="td_center">${stockOut.expressNo}</td>
				            	<td class="td_center">${stockOut.logisticsCompanyName}</td>
				            	<td class="td_center">
									<#if '${stockOut.status?if_exists}'=='0'>
										失败
									</#if>
									<#if '${stockOut.status?if_exists}'=='1'>
										成功
									</#if>
		                    	</td>
				            	<td class="td_center">${stockOut.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
				                <td align="center">
				                <a href="javascript:void(0)" onclick="viewItem(${stockOut.id})">[明细]</a>
				                <a href="javascript:void(0)" onclick="viewInvoice(${stockOut.id})">[发票]</a>
							    </td>
				             </tr>
	           		 </#list>
				</#if>
              </table>
              <@pager pagination=stockOutPages formId="stockOutForm" /> 
	     </div>
	</div>
	<!-- 列表结束 -->
</form>
</@backend>