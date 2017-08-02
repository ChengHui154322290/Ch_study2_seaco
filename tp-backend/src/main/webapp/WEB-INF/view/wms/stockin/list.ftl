<#include "/common/common.ftl"/>
<@backend title="入库单列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/wms/stockin.js'
] css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >
	<!-- 查询条件开始 -->
<form method="post" action="${domain}/wms/stockin/list.htm" id="stockInForm">	
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">入库单列表</b></div>
            <div class="box_center pt10 pb10">
	       	<table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  	<td>采购单号：</td>    
                  	<td><input type="text" id="orderCode" name="orderCode" class="input-text lh25" size="20" value='${stockasn.orderCode}'></td>
                  	<td>快递单号：</td>    
                  	<td><input type="text" id="expressCode" name="expressCode" class="input-text lh25" size="20" value='${stockasn.expressCode}'></td>
                  	<td>供应商：</td>
                  	<td><input type="text" id="supplierName" name="supplierName" class="input-text lh25" size="20" value='${stockasn.supplierName}'></td>
                  	<!--  <td>推送时间：</td>
                  	<td>
	                  	<input type="text" name="startCreateTimeQo" id="startCreateTimeQo" readonly value='${stockasn.startCreateTimeQo?if_exists}' class="input-text lh25" size="20">
	                  	<span>到</span>
	                  	<input type="text" name="endCreateTimeQo" id="endCreateTimeQo" readonly  value='${stockasn.endCreateTimeQo?if_exists}' class="input-text lh25" size="20">
                  	</td>
                   <td>推送状态：</td>
                   <td>
                  	<div class="select_border"> 
                        <div class="select_containers "> 
		                    <select name="statusQo" class="select">
		                    	<option value=""  <#if '${stockasn.statusQo?if_exists}'==''>selected</#if>>全部状态</option>
		                    	<option value="0" <#if '${stockasn.statusQo?if_exists}'=='0'>selected</#if>>推送失败</option>
		                    	<option value="1" <#if '${stockasn.statusQo?if_exists}'=='1'>selected</#if>>推送成功</option>
		                    </select>
                    	</div>
                    </div>
                   </td>
                   -->
                </tr>
            </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
              	 <!-- <a href="javascript:void(0);"><input id="uploadFileBtn"  class="btn btn82 btn_export" type="button" value="导入" name="button"  /></a> -->
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="$('#stockInForm').submit();" type="button" value="查询" name="button" /></a>
              </div>
            </div>
          </div>
        </div>
    </div>
    <!-- 查询条件结束 -->
    
    <!-- 列表开始 -->
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table" id="list_table">
                <tr>
           	   <th width="20">序号</th>
           	   <th width="50">采购单号</th>
               <!-- <th width="50">合同号</th> -->
               <th width="50">供应商</th>
               <th width="50">订单生成时间</th>
               <th width="50">仓库名称</th>
			   <!-- <th width="50">快递单号</th>
			   <th width="50">收货人</th> 
			    -->
			   <th width="50">导入状态</th>
			   <th width="50">操作</th>
                </tr>
            <#if page.rows?default([])?size !=0>
            <#list page.rows as stockasn>
	             <tr>
	               <td width="20" align="center">${stockasn_index+1}</td>
	               <td width="50" align="center">${stockasn.orderCode?if_exists}</td>
	               <!-- <td width="50" align="center">${stockasn.contractCode?if_exists}</td> -->
	               <td width="50" align="center">${stockasn.supplierName?if_exists}</td>
	               <td width="50" align="center">${stockasn.orderCreateTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	               <td width="50" align="center">${stockasn.warehouseName?if_exists}</td>
	               <!-- <td width="50" align="center">${stockasn.expressCode?if_exists}</td>
	               <td width="50" align="center">${stockasn.consignee?if_exists}</td>
	                -->
	               <td width="50" align="center">
	               <#if '${stockasn.status?if_exists}' >
		               <#if '${stockasn.status}' = '1' >
		               	成功
		               <#else>
						失败
		               </#if>
	               </#if>
	               </td>
	               <td width="50" align="center">
	               <a href="javascript:void(0)" onclick="viewItem(${stockasn.id})">[明细]</a>
	               <a href="javascript:void(0)" onclick="viewFact(${stockasn.id})">[回执]</a>
	               <a href="javascript:void(0);">
	               		<input /*class="btn btn82 btn_export"*/ type="button" value="导入" name="button" onclick="uploadFile(${stockasn.id})" /></a>
				   </td>
	            </tr>
            </#list>
            </#if>
              </table>
              
	     </div>
	</div>
	<@pager  pagination=page  formId="stockInForm" /> 
</form>
</@backend>