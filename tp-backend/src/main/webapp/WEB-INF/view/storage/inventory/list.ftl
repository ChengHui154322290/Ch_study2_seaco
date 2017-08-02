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
	<form class="jqtransform" method="post" id="queryInventoryForm" action="${domain}/storage/inventory/list.htm">
    <div >
	   <div>	      
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>   
                  	<td >SKU：</td>    
                  	<td><input type="text" id="sku" name="sku" class="input-text lh25" size="20" value='${sku}'></td> 
                  	<td>商家：</td>
                  	<td>
                  		<select name="spId" id="supplierId" class="select2" style="width:350px; margin-left: 1px" >
							<option value="">--请选择商家--</option>
							<#list supplierList as s>
								<option value="${s.id}" <#if s.id=spId> selected </#if> >${s.name}</option>
							</#list>
						</select>
					</td>                  	
                   <td>
                   	  <input class="btn btn82 btn_search" type="button"    id="searthAtt" value="查询" >
                   </td>  
                </tr>
              </table>
	   </div>
    </div>
    <div id="table" class="mt10">
        <div class="box span10 oh">
        		 <#if  onData ?? >
				    <div class="mb10">
				     	${onData}
				   	</div>
				 </#if>
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                	 <th width="100">商家名称</th>
                      <th width="100">SKU</th>
                      <th width="100">条形码</th>
                      <th width="100">商品名称</th>
	                  <th width="100">仓库名称</th>
	                  <th width="100">可销售库存</th>
	                  <th width="100">现货库存</th> 
	                  <th width="100">占用库存</th> 
                </tr>
                <#if page.rows?? >
   						<#list page.rows as sku>   
				            <tr class="tr" >
				            	<td class="td_center">${sku.spName}</td>
					              <td class="td_center">${sku.sku}</td>
					              <td class="td_center">${sku.barcode}</td>
					              <td class="td_center">${sku.mainTitle}</td>
					              <td class="td_center">${sku.warehouseName}</td>
					              <td class="td_center">
					              	<#if sku.inventory lt 0>
					              		0
					              	<#else>
					              		${sku.inventory}
					              	</#if>
					              </td>
					              <td class="td_center">
					              	<#if sku.realInventory lt 0>
					              		0
					              	<#else>
					              		<a href="javascript:void(0);" onclick="showRealList('${sku.sku}','${sku.warehouseId}');">${sku.realInventory}</a>
					              	</#if>
					              </td> 
					              <td class="td_center">
					              	<a  href="javascript:void(0);" onclick="showOccupyList('${sku.sku}');">${sku.occupy}</a>
					              </td>  
				             </tr>
	           		 </#list>
				</#if>
              </table>
              <@pager  pagination=page  formId="queryInventoryForm"  /> 
	     </div>
	</div>
   </form>
   
</@backend>