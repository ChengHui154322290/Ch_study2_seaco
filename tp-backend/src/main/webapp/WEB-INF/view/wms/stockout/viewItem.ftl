<#include "/common/common.ftl"/>
<@backend title="出库详情列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/wms/stockout.js'
] css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >

	<div class="box">
    	<div class="box_border">
        	<div class="box_top"><b class="pl15">出库单信息</b></div>
  		</div>
    </div>
        
	<div id="table" class="mt10">
        <div class="box span10 oh">
			<table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0" style=" overflow:outo"> 
				<tbody>
					<tr>
						<td class="td_right" style="width: 20%">订单编号：</td>
						<td class="" style="width: 30%">
							${stockout.orderCode}
						</td>
						<td class="td_right" style="width: 20%">订单类型：</td>
						<td class="" style="width: 30%">${stockout.orderType}</td>
					</tr>
					<tr>
						<td class="td_right" style="width: 20%">仓库编号：</td>
						<td class="" style="width: 30%">
							${stockout.warehouseCode}
						</td>
						<td class="td_right" style="width: 20%">仓库名称：</td>
						<td class="" style="width: 30%">${stockout.warehouseName}</td>
					</tr>
					<tr>
						<td class="td_right" style="width: 20%">快递单号：</td>
						<td class="" style="width: 30%">
							${stockout.expressNo}
						</td>
						<td class="td_right" style="width: 20%">承运商：</td>
						<td class="" style="width: 30%">
							${stockout.logisticsCompanyName}
						</td>
					</tr>
					
					<tr>
						<td class="td_right" style="width: 20%">收货人：</td>
						<td class="" style="width: 30%">
							${stockout.consignee}
						</td>
						<td class="td_right" style="width: 20%">收货人手机：</td>
						<td class="" style="width: 30%">
							${stockout.mobile}
						</td>
					</tr>
					<tr>
						<td class="td_right" style="width: 20%">收货地址：</td>
						<td class="" style="width: 30%">${stockout.province} ${stockout.city} ${stockout.area} ${stockout.address}</td>
						<td class="td_right" style="width: 20%">创建时间：</td>
						<td class="" style="width: 30%">${stockout.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
					</tr>
					
				</tbody>
			</table>
		</div>
	</div>
	
	<!-- 查询条件开始 -->
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">出库单详情</b></div>
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
	           	   <th width="15%">SKU编码</th>
	           	   <th width="20%">SKU名称</th>
	           	   <th width="15%">商品条形码</th>
	           	   <th width="10%">数量</th>
	               <th width="10%">实际价格</th>
	               <th width="10%">销售价格</th>
	               <th width="10%">优惠金额</th>
                </tr>
            <#if outItemList?default([])?size !=0>
            <#list outItemList as item>
	             <tr>
	               <td width="10%" align="center">${item_index+1}</td>
	               <td width="15%" align="center">${item.itemSku}</td>
	               <td width="20%">${item.itemName}</td>
	               <td width="15%" align="center">${item.itemBarcode}</td>
	               <td width="10%" align="center">${item.quantity}</td>
	               <td width="10%" align="center">${item.actualPrice}</td>
	               <td width="10%" align="center">${item.salesPrice}</td>
	               <td width="10%" align="center">${item.discountAmount}</td>
	            </tr>
            </#list>
            </#if>
              </table>
	     </div>
	</div><!-- 详情列表结束 -->
</@backend>