<#include "/common/common.ftl"/>
<@backend title="" js=[
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/select2/js/select2.js','/statics/select2/js/select2Util.js','/statics/select2/js/select2_locale_zh-CN.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/storage/js/inventory_list.js'
] css=[
'/statics/select2/css/select2.css',
'/statics/select2/css/common.css',
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
	<form class="jqtransform" method="post" id="queryInventoryForm" action="${domain}/storage/inventory/manage/showLog.htm">
   	<div><input type="hidden" name="inventoryId" value="${inventoryId}"/></div>
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
					<!-- <th width="100">供应商</th>
					<th width="100">仓库</th>
					<th width="100">商品标题</th> -->
					<th width="100">sku</th>
					<!-- <th width="100">条形码</th> -->
					<th width="100">商品总库存数</th>
					<th width="100">商品预留库存数</th>
					<th width="100">占用库存</th>
					<th width="100">商品可售库存数<!--（总库存数-预留库存数-冻结库存数）--></th>
					<th width="100">商品预警库存数</th>
					<th width="100">操作类型</th>
					<th width="100">操作日期</th>
					<th width="100">操作人</th>
					<th width="300">备注</th>
                </tr>
                <#if page.rows?? >
   						<#list page.rows as sku>   
				            <tr class="tr" >
					              <!-- <td class="td_center">${sku.spName}</td>
					              <td class="td_center">${sku.warehouseName}</td>
					              <td class="td_center">${sku.mainTitle}</td> -->
					              <td class="td_center">${sku.sku}</td>
					               <!-- <td class="td_center">${sku.barcode}</td> -->
					              <td class="td_center">${sku.inventory}</td>
					              <td class="td_center">${sku.reserveInventory}</td>
					              <td class="td_center">${sku.occupy}</td>
					              <td class="td_center">${sku.inventory-sku.occupy-sku.reserveInventory}</td>
					              <td class="td_center">${sku.warnInventory}</td>
					              <td class="td_center">
					              <!--更改标志：1：新增，2：修改；3：删除 ${sku.changeType}-->
					              		<#if sku.changeType==1>
					              		新增
					              		<#elseif sku.changeType==2>
					              		修改
					              		<#elseif sku.changeType==3>
					              		删除
					              		<#else>
					              		</#if>
					              </td>
					              <td class="td_center">
					              	<#if sku.createTime??>${sku.createTime?string("yyyy-MM-dd HH:mm:ss")}</#if>
				              	  </td>
					              <td class="td_center">${sku.createUser}</td>
					              <td class="td_center">${sku.remark}</td>
				             </tr>
	           		 </#list>
				</#if>
              </table>
              <@pager  pagination=page  formId="queryInventoryForm"  /> 
	     </div>
	</div>
   </form>
</@backend>