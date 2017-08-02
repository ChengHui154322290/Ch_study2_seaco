<#include "/common/common.ftl"/> 
<@backend title="" js=[ '/statics/backend/js/salesorder/utils.js', '/statics/backend/js/salesorder/view.js'] css=[] >
<div class="container">
	<div class="box">
		<div id="forms" class="mt10">
			<div class="box">
				<form action="" class="jqtransform">
					<div class="box_border">
						<div class="box_top">
							<b class="pl15">订单基本信息</b>
						</div>
						<div class="box_center">
							<table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td class="td_right" style="width: 10%">订单编号：</td>
									<td class="" style="width: 20%">${sub.order.parentOrderCode}</td>
									<td class="td_right" style="width: 10%">订单状态：</td>
									<td class="" style="width: 20%">${sub.order.statusStr}</td>
								</tr>
								<tr>
									<td class="td_right" style="width: 10%">客户账号：</td>
									<td class="" style="width: 20%">${sub.loginName}</td>
									<td class="td_right" style="width: 10%">支付方式：</td>
									<td class="" style="width: 20%">${sub.order.payTypeStr} - ${sub.order.payWayStr}</td>
									<td class="td_right" style="width: 10%">下单时间：</td>
									<td class="" style="width: 20%">${sub.order.createTime?datetime}</td>
								</tr>
								<tr>
									<td class="td_right" style="width: 10%">支付时间：</td>
									<td class="" style="width: 20%"><#if sub.order.payTime??>${sub.order.payTime?datetime}</#if></td>
									<td class="td_right" style="width: 10%">订单备注：</td>
									<td class="" colspan="5">${(sub.order.remark)!}</td>
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
									<td class="" style="width: 20%">${sub.orderConsignee.name}</td>
									<td class="td_right" style="width: 10%">邮编：</td>
									<td class="" style="width: 20%">${sub.orderConsignee.postcode}</td>
									<td class="td_right" style="width: 10%">电话：</td>
									<td class="" style="width: 20%">${sub.orderConsignee.telephone}</td>
								</tr>
								<tr>
									<td class="td_right" style="width: 10%">手机：</td>
									<td class="" style="width: 20%">${sub.orderConsignee.mobile}</td>
									<td class="td_right" style="width: 10%">收货地址：</td>
									<td class="" colspan="3">
									${sub.orderConsignee.provinceName}${sub.orderConsignee.cityName}${sub.orderConsignee.countyName}${sub.orderConsignee.townName}${sub.orderConsignee.address}
									</td>
								</tr>
							</table>
						</div>
					</div>
					<div class="box_border">
						<div class="box_top">
							<b class="pl15">财务信息</b>
						</div>
						<div class="box_center">
							<table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td class="td_right" style="width: 10%">商品总金额：</td>
									<td class="" style="width: 20%">${sub.order.originalTotal?string("0.00")}</td>
									<td class="td_right" style="width: 10%">实际支付金额：</td>
									<td class="" style="width: 20%">${sub.order.payTotal?string("0.00")}</td>
									<td class="td_right" style="width: 10%">优惠总金额：</td>
									<td class="" style="width: 20%">${(sub.order.originalTotal - sub.order.total)?string("0.00")}</td>
								</tr>
								<tr>
									<td class="td_right" style="width: 10%">运费：</td>
									<td class="" style="width: 20%">${sub.order.freight?string("0.00")}</td>
									<td class="td_right" style="width: 10%">发票抬头：</td>
									<td class="" style="width: 20%">${(sub.orderReceipt.title)!}</td>
									<td class="td_right" style="width: 10%">使用<#if sub.order.channelCode=='hhb'><font color="blue">慧币</font><#else>西客币</#if>：</td>
									<td class="" style="width: 20%">${(sub.order.totalPoint)!}</td>
								</tr>
							</table>
						</div>
					</div>
					<div class="box_border">
						<div class="box_top">
							<b class="pl15">商品信息</b>
						</div>
						<div class="box_center">
							<table class="list_table" style="width: 100%;" border="0" cellpadding="0" cellspacing="0">
								<thead>
									<tr>
										<th width="10%">商品编号</th>
										<th width="20%">商品名称</th>
										<th width="10%">销售价</th>
										<th width="10%">商品数量</th>
										<th width="20%">小计</th>
									</tr>
								</thead>
								<tbody>
								<#list sub.orderItemList as line>
									<tr>
										<td>${line.skuCode}</td>
										<td>${line.spuName}</td>
										<td><#if line.salesPrice??>${line.salesPrice?string("0.00")}<#else>${line.price?string("0.00")}</#if></td>
										<td>${line.quantity}</td>
										<td>${line.originalSubTotal?string("0.00")}</td>
									</tr>
								</#list>
								</tbody>
							</table>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- <div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
		<div class="search_bar_btn" style="text-align: right;">
			<input class="ext_btn" type="button" onclick="location.href='javascript:history.go(-1)'" value="返回">
		</div>
	</div> -->
</div>
</@backend>