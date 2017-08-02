<#include "/common/common.ftl"/>
<@backend title="入区信息列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/wms/stocksync.js'
] css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >

	<!-- 查询条件开始 -->
<form method="post" action="${domain}/wms/stocksync/list.htm" id="stocksyncForm">	
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">库存同步列表</b></div>
            <div class="box_center pt10 pb10">
	       	<table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  	<td>仓库WMS编号：</td>    
                  	<td><input type="text" id="wmsCode" name="wmsCode" class="input-text lh25" size="20" value='${stocksyncInfo.wmsCode}'></td>
                  	<td>仓库SKU：</td>    
                  	<td><input type="text" id="stockSku" name="stockSku" class="input-text lh25" size="20" value='${stocksyncInfo.stockSku}'></td>
                  	<td>商品SKU：</td>
                  	<td><input type="text" id="sku" name="sku" class="input-text lh25" size="20" value='${stocksyncInfo.sku}'></td>
                  	<!--  <td>同步时间：</td>
                  	<td>
	                  	<input type="text" name="startCreateTimeQo" id="startCreateTimeQo" readonly value='${stocksyncInfo.startCreateTimeQo?if_exists}' class="input-text lh25" size="20">
	                  	<span>到</span>
	                  	<input type="text" name="endCreateTimeQo" id="endCreateTimeQo" readonly  value='${stocksyncInfo.endCreateTimeQo?if_exists}' class="input-text lh25" size="20">
                  	</td>
                   -->
                </tr>
            </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
              	 <!-- <a href="javascript:void(0);"><input id="uploadFileBtn"  class="btn btn82 btn_export" type="button" value="导入" name="button"  /></a> -->
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="$('#stocksyncForm').submit();" type="button" value="查询" name="button" /></a>
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
           	   <th width="50">仓库WMS编号</th>
               <th width="50">商品名称</th>
               <th width="50">仓库SKU</th>
               <th width="50">商品SKU</th>
               <th width="50">仓库返回实际库存</th>
			   <th width="50">系统库存</th>
			   <th width="50">同步时间</th>
			   <th width="50">创建时间</th>
                </tr>
            <#if page.rows?default([])?size !=0>
            <#list page.rows as stocksyncInfo>
	             <tr>
	               <td width="20" align="center">${stocksyncInfo_index+1}</td>
	               <td width="50" align="center">${stocksyncInfo.wmsCode?if_exists}</td>
	               <td width="50" align="center">${stocksyncInfo.skuName?if_exists}</td>
	               <td width="50" align="center">${stocksyncInfo.stockSku?if_exists}</td>
	               <td width="50" align="center">${stocksyncInfo.sku?if_exists}</td>
	               <td width="50" align="center">${stocksyncInfo.stockInventory?if_exists}</td>
	               <td width="50" align="center">${stocksyncInfo.inventory?if_exists}</td>
	               <td width="50" align="center">${stocksyncInfo.syncTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	               <td width="50" align="center">${stocksyncInfo.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	            </tr>
            </#list>
            </#if>
              </table>
              
	     </div>
	</div>
	<@pager  pagination=page  formId="stocksyncForm" /> 
</form>
</@backend>

