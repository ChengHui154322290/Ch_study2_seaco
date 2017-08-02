<#include "/common/common.ftl"/> 
<@backend title="" 
js=['/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/layer/extend/layer.ext.js',
	'/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
	'/statics/backend/js/salesorder/utils.js', 
	'/statics/backend/js/salesorder/view.js']
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css'] >
<div class="container">
	<div class="box">
		<div id="forms" class="mt10">
			<div class="box">
				<form action="" class="jqtransform">
					<div class="box_border">
						<div class="box_top">
							<b class="pl15">订单基本信息 <#if sub.subOrder.type==7 && sub.subOrder.orderStatus gte 3 && sub.subOrder.orderStatus lt 6><span> 订单限期送达时间：<span class="fastorderoverspan" style="color:blue;"><#if sub.subOrder.overTime gt 0 >还剩<#else>超时</#if><span class="fastorderovertime">${sub.subOrder.overTime}</span>分</span> 
							<#if sub.subOrder.orderStatus==3><input class="btn btn82 btn_save2 fastreceivingorderbtn" type="button" value="接单"  data_code="${sub.subOrder.orderCode}"/></#if>
							<#if sub.subOrder.orderStatus==4><input class="btn btn82 btn_save2 fastdeliveryorderbtn" type="button" value="配送" data_code="${sub.subOrder.orderCode}"/></#if>
							</#if></b>
						</div>
						<div class="box_center">
							<table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td class="td_right" style="width: 10%">订单编号：</td>
									<td class="" style="width: 20%">${sub.subOrder.orderCode} <a href="javascript:void(0);" data_code="${sub.subOrder.orderCode}" id="order-log">订单日志</a>
										<a href="javascript:void(0);" data_code="${sub.subOrder.orderCode}" id="order-track">物流跟踪</a>
										<!-- <#if sub.subOrder.directOrderStatus ??>
											<a href="javascript:void(0);" data_code="${sub.subOrder.orderCode}" id="directOrder-log">订单操作日志</a>
										</#if>
										-->
									</td>
									<td class="td_right" style="width: 10%">订单类型：</td>
									<td class="" style="width: 20%">${sub.subOrder.typeStr}</td>
									<td class="td_right" style="width: 10%">订单状态：</td>
									<td class="" style="width: 20%">${sub.subOrder.statusStr}</td>
								</tr>
								<tr>
									<td class="td_right" style="width: 10%">客户账号：</td>
									<td class="" style="width: 20%">${sub.loginName}</td>
									<td class="td_right" style="width: 10%">支付方式：</td>
									<#if sub.subOrder.type gte 3>
										<td class="" style="width: 20%">${sub.subOrder.payTypeStr} - ${sub.subOrder.payWayStr}</td>
									<#else>
										<td class="" style="width: 20%">${sub.order.payTypeStr} - ${sub.order.payWayStr}</td>
									</#if>
									<td class="td_right" style="width: 10%">下单时间：</td>
									<td class="" style="width: 20%">${sub.subOrder.createTime?datetime}</td>
								</tr>
								<tr>
									<td class="td_right" style="width: 10%">支付时间：</td>
									<#if sub.subOrder.type gte 3>
										<td class="" style="width: 20%"><#if sub.subOrder.payTime??>${sub.subOrder.payTime?datetime}</#if></td>
									<#else>
										<td class="" style="width: 20%"><#if sub.order.payTime??>${sub.order.payTime?datetime}</#if></td>
									</#if>
									<td class="td_right" style="width: 10%">发货时间：</td>
									<td class="" style="width: 20%"><#if sub.subOrder.deliveredTime??>${sub.subOrder.deliveredTime?datetime}</#if></td>
									<td class="td_right" style="width: 10%">完成时间：</td>
									<td class="" style="width: 20%"><#if sub.subOrder.doneTime??>${sub.subOrder.doneTime?datetime}</#if></td>
								</tr>
								<tr>
									<td class="td_right" style="width: 10%">快递公司：</td>
									<td class="" style="width: 20%">${(sub.orderDelivery.companyName)!}</td>
									<td class="td_right" style="width: 10%">快递单号：</td>
									<td class="" style="width: 20%">${(sub.orderDelivery.packageNo)!}</td>
									<td class="td_right" style="width: 10%">支付单号：</td>
									<#if sub.subOrder.type gte 3>
										<td class="" style="width: 20%">${(sub.subOrder.payCode)!}</td>
									<#else>
										<td class="" style="width: 20%">${(sub.order.payCode)!}</td>
									</#if>
								</tr>
								<tr>
									<td class="td_right" style="width: 10%">海关审核状态：</td>
									<td class="" style="width: 20%">${(sub.subOrder.clearanceStatusStr)!}
										<a href="javascript:void(0);" data_code="${sub.subOrder.orderCode}" id="order-clearance">清关状态</a>
										<a href="javascript:void(0);" data_code="${sub.subOrder.orderCode}" id="order-manual-push">重新推送</a>
									</td>
									<#if sub.subOrder.type == 6>
										<td class="td_right" style="width: 10%">推送仓库状态：</td>
										<td class="" style="width: 20%">																			
											${(sub.subOrder.putStatusStr)}
											<a href="javascript:void(0);" data_code="${sub.subOrder.orderCode}" id="order-saleout">推送仓库</a>										
										</td>
									</#if>
									<td class="td_right" style="width: 10%">订单备注：</td>
									<td class="" style="width: 20%">${(sub.order.remark)!}</td>
								</tr>
								<tr>
									<td class="td_right" style="width: 10%">第三方海外直邮订单状态：</td>
									<td class="" style="width: 20%">${(sub.subOrder.directOrderStatusStr)!}
										<a href="javascript:void(0);" data_code="${sub.subOrder.orderCode}" id="order-direct-push">重新推送</a>
										<a >${(log.originalResult)!}</a>
									</td>
									
									<td class="td_right" style="width: 10%"></td>
									<td class="" style="width: 20%"></td>
									
									<td class="td_right" style="width: 10%"></td>
									<td class="" style="width: 20%"></td>
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
								<tr>
									<td class="td_right" style="width: 10%">真实姓名：</td>
									<td class="" style="width: 20%">${sub.memRealinfo.realName}</td>
									<td class="td_right" style="width: 10%">身份证号：</td>
									<td class="" style="width: 20%">${sub.memRealinfo.identityCode}</td>
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
									<td class="" style="width: 20%">${sub.subOrder.originalTotal?string("0.00")}</td>
									<td class="td_right" style="width: 10%">实际支付金额：</td>
									<td class="" style="width: 20%">${sub.subOrder.payTotal?string("0.00")}</td>
									<td class="td_right" style="width: 10%">优惠总金额：</td>
									<td class="" style="width: 20%">${((sub.subOrder.discount)!0)?string("0.00")} 
										<a href="javascript:void(0);" data_code="${sub.subOrder.orderCode}" id="order-coupon">优惠券信息</a>
									</td>
								</tr>
								<tr>
									<td class="td_right" style="width: 10%">运费：</td>
									<td class="" style="width: 20%">${sub.subOrder.freight?string("0.00")}</td>
									<td class="td_right" style="width: 10%">发票抬头：</td>
									<td class="" style="width: 20%">${(sub.orderReceipt.titleStr)!}</td>
									<td class="td_right" style="width: 10%">发票类型：</td>
									<td class="" style="width: 20%">${(sub.receiptDetail.typeStr)!}</td>
								</tr>
								<tr>
									<td class="td_right" style="width: 10%">发票号码：</td>
									<td class="" style="width: 20%">${(sub.receiptDetail.numberStr)!}</td>
									<td class="td_right" style="width: 10%">使用<#if sub.subOrder.channelCode=='hhb'><font color="blue">慧币</font><#else>西客币</#if>：</td>
									<td class="" style="width: 20%">${(sub.subOrder.points)!}</td>
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
										<th width="10%">商品名称</th>
										<th width="10%">商品条形码</th>
										<th width="10%">活动编号</th>
										<th width="10%">备案号（海关）</th>
										<th width="5%">销售价</th>
										<th width="5%">商品数量</th>
										<th width="10%">优惠金额</th>
										<th width="5%">实付价</th>
										<th width="10%">实付小计</th>
									</tr>
								</thead>
								<tbody>
								<#list sub.orderItemList as line>
									<tr>
										<td>${line.skuCode}</td>
										<td>${line.spuName}</td>
										<td>${line.barCode}</td>
										<td>${line.topicId}</td>
										<td>${line.productCode}</td>
										<td><#if line.salesPrice??>${line.salesPrice?string("0.00")}<#else>${line.price?string("0.00")}</#if></td>
										<td>${line.quantity}</td>
										<td>${(((line.originalSubTotal)!0) - ((line.subTotal)!0))?string("0.00")}</td>
										<td>${(line.subTotal/line.quantity)?string("0.00")}</td>
										<td>${line.subTotal?string("0.00")}</td>
									</tr>
								</#list>
								</tbody>
							</table>
						</div>
					</div>
					<div class="box_border">
						<div class="box_top">
							<b class="pl15">退货信息</b>
						</div>
						<div class="box_center">
							<table class="list_table" style="width: 100%;" border="0" cellpadding="0" cellspacing="0">
								<thead>
									<tr>
										<th width="10%">退货编号</th>
										<th width="10%">商品编号</th>
										<th width="40%">商品名称</th>
										<th width="10%">退货数量</th>
										<th width="10%">退款金额</th>
										<th width="10%">退款状态</th>
										<th width="10%">审核状态</th>
									</tr>
								</thead>
								<tbody>
								<#list rejectList as rej>
									<#list rej.rejectItemList as item>
									<tr>
										<td>${item.rejectNo}</td>
										<td>${item.itemSkuCode}</td>
										<td>${item.itemName}</td>
										<td>${item.itemRefundQuantity}</td>
										<td>${(item.itemRefundAmount)?string("0.00")}</td>
										<td>${rej.zhRejectStatus}</td>
										<td>${rej.zhAuditStatus}</td>
									</tr>
									</#list>
								</#list>
								</tbody>
							</table>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<div class="fastuserinfodiv" style="display:none;">
		<#if fastUserInfoList??>
			<select class="select fastuserinfo">
				<#list fastUserInfoList as fastUserInfo>
					<option value="联系信息:${fastUserInfo.userName},${fastUserInfo.mobile}" fastuserid="${fastUserInfo.fastUserId}">${fastUserInfo.userName}-${fastUserInfo.userTypeCn}</option>
				</#list>
			</select>
		</#if>
	</div>
</div>

</@backend>