<#include "/common/common.ftl"/>
<@backend title="出库回执单详情列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/wms/stockoutback.js'
] css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >

	<div class="box">
    	<div class="box_border">
        	<div class="box_top"><b class="pl15">出库回执单信息</b></div>
  		</div>
    </div>
        
	<div id="table" class="mt10">
        <div class="box span10 oh">
			<table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0" style=" overflow:outo"> 
				<tbody>
					<tr>
						<td class="td_right" style="width: 20%">订单编号：</td>
						<td class="" style="width: 30%">
							${stockoutback.orderCode}
						</td>
						<td class="td_right" style="width: 20%">仓库编号：</td>
						<td class="" style="width: 30%">${stockoutback.warehouseCode}</td>
					</tr>
					<tr>
						<td class="td_right" style="width: 20%">运单号：</td>
						<td class="" style="width: 30%">
							${stockoutback.expressNo}
						</td>
						<td class="td_right" style="width: 20%">承运商：</td>
						<td class="" style="width: 30%">${stockoutback.logisticsCompanyName}</td>
					</tr>
					<tr>
						<td class="td_right" style="width: 20%">包裹重量：</td>
						<td class="" style="width: 30%">
							${stockoutback.weight}
						</td>
						<td class="td_right" style="width: 20%">状态：</td>
						<td class="" style="width: 30%">
							<#if '${stockoutback.status?if_exists}'=='0'>
								失败
							</#if>
							<#if '${stockoutback.status?if_exists}'=='1'>
								成功
							</#if>
						</td>
					</tr>
					
					<tr>
						<td class="td_right" style="width: 20%">审核人：</td>
						<td class="" style="width: 30%">
							${stockoutback.auditor}
						</td>
						<td class="td_right" style="width: 20%">审核时间：</td>
						<td class="" style="width: 30%">
							${stockoutback.auditTime?string("yyyy-MM-dd HH:mm:ss")}
						</td>
					</tr>
					<tr>
						<td class="td_right" style="width: 20%">创建时间：</td>
						<td class="" style="width: 30%">${stockoutback.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
					</tr>
					
				</tbody>
			</table>
		</div>
	</div>
	
	<!-- 查询条件开始 -->
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">出库回执单详情</b></div>
          </div>
        </div>
    </div>
    <!-- 查询条件结束 -->
    
    <!-- 详情列表开始 -->
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table" id="list_table">
                <tr>
	           	   <th width="10%">序号</th>
	           	   <th width="15%">平台方SKU</th>
	           	   <th width="15%">仓库方SKU</th>
	           	   <th width="15%">商品编码</th>
	           	   <th width="15%">数量</th>
	               <th width="15%">单个商品重量</th>
	               <th width="15%">创建时间</th>
                </tr>
            <#if outBackItemList?default([])?size !=0>
            <#list outBackItemList as item>
	             <tr>
	               <td width="10%" align="center">${item_index+1}</td>
	               <td width="15%" align="center">${item.itemSku}</td>
	               <td width="15%" align="center">${item.stockSku}</td>
	               <td width="15%" align="center">${item.productNo}</td>
	               <td width="15%" align="center">${item.quantity}</td>
	               <td width="15%" align="center">${item.weight}</td>
	               <td width="15%" align="center">${item.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	            </tr>
            </#list>
            </#if>
              </table>
	     </div>
	</div><!-- 详情列表结束 -->
</@backend>