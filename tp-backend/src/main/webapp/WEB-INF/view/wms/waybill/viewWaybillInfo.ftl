<#include "/common/common.ftl"/>
<@backend title="出库详情列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/wms/waybill.js'
] css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >

	<div class="box">
    	<div class="box_border">
        	<div class="box_top"><b class="pl15">运单推送详情</b></div>
  		</div>
    </div>
        
	<div id="table" class="mt10">
        <div class="box span10 oh">
			<table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0" style=" overflow:auto"> 
				<tbody>
					<tr>
						<td class="td_right" style="width: 10%">编号：</td>
						<td class="" style="width: 10%">
							${waybill.id}
						</td>
						<td class="td_right" style="width: 10%">订单编号：</td>
						<td class="" style="width: 10%">
							${waybill.orderCode}
						</td>
						<td class="td_right" style="width: 10%">运单号：</td>
						<td class="" style="width: 20%">${waybill.waybillNo}</td>
					</tr>
					<tr>
						<td class="td_right" style="width: 10%">类型：</td>
						<td class="" style="width: 10%">${waybill.typeStr}</td>
						<td class="td_right" style="width: 10%">快递企业：</td>
						<td class="" style="width: 10%">
							${waybill.logisticsName}
						</td>
						<td class="td_right" style="width: 10%">快递编号：</td>
						<td class="" style="width: 20%">${waybill.logisticsCode}</td>
					</tr>
					<tr>
						<td class="td_right" style="width: 10%">状态：</td>
						<td class="" style="width: 10%">${waybill.statusStr}</td>
						<td class="td_right" style="width: 10%">失败次数：</td>
						<td class="" style="width: 10%">
							${waybill.failTimes}
						</td>
						<td class="td_right" style="width: 10%">原因：</td>
						<td class="" style="width: 20%">${waybill.errorMsg}</td>
					</tr>
					<tr>
						<td class="td_right" style="width: 10%">创建时间：</td>
						<td class="" style="width: 20%">${waybill.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
						<td class="td_right" style="width: 10%">更新时间：</td>
						<td class="" style="width: 20%">${waybill.updateTime?string("yyyy-MM-dd HH:mm:ss")}</td>
					</tr>			
				</tbody>
			</table>
		</div>
	</div>
   <div class="box_border">
		<div class="box_top">
			<b class="pl15">商品信息</b>
		</div>
		<div class="box_center">
			<table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td_right" style="width: 10%">商品名称：</td>
					<td class="" style="width: 20%">${waybill.mainGoodsName}</td>
					<td class="td_right" style="width: 10%">毛重：</td>
					<td class="" style="width: 20%">${waybill.grossWeight}</td>
					<td class="td_right" style="width: 10%">净重：</td>
					<td class="" style="width: 20%">${waybill.netWeight}</td>
				</tr>
				<tr>
					<td class="td_right" style="width: 10%">数量：</td>
					<td class="" style="width: 20%">${waybill.packAmount}</td>
					<td class="td_right" style="width: 10%">价值：</td>
					<td class="" style="width: 20%">${waybill.worth}</td>
				</tr>
				<tr>
					<td class="td_right" style="width: 10%">货到付款：</td>
					<td class="" style="width: 20%">${waybill.isPostagePayStr}</td>
					<td class="td_right" style="width: 10%">邮费到付：</td>
					<td class="" style="width: 20%">${waybill.isDeliveryPayStr}</td>
				</tr>
			</table>
		</div>
	</div>
   <div class="box_border">
		<div class="box_top">
			<b class="pl15">收货人信息</b>
		</div>
		<div class="box_center">
			<table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td_right" style="width: 10%">收货人姓名：</td>
					<td class="" style="width: 20%">${waybill.consignee}</td>
					<td class="td_right" style="width: 10%">邮编：</td>
					<td class="" style="width: 20%">${waybill.postCode}</td>
					<td class="td_right" style="width: 10%">电话：</td>
					<td class="" style="width: 20%">${waybill.tel}</td>
				</tr>
				<tr>
					<td class="td_right" style="width: 10%">手机：</td>
					<td class="" style="width: 20%">${waybill.mobile}</td>
					<td class="td_right" style="width: 10%">收货地址：</td>
					<td class="" colspan="3">
						${waybill.province}${waybill.city}${waybill.area}${waybill.address}
					</td>
				</tr>
			</table>
		</div>
	</div> 					
	 					
</@backend>