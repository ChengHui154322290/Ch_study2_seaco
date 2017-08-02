<#include "/common/common.ftl"/> 
<@backend title="" js=[ '/statics/backend/js/dateTime2/js/jquery-ui.min.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
	'/statics/backend/js/salesorder/utils.js', 
	'/statics/backend/js/salesorder/list.js',
	'/statics/select2/js/select2.js','/statics/select2/js/select2Util.js'
	] 
	css=[
		'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
		'/statics/select2/css/select2.css',
		'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css'
		] >

<form method="post" action="${domain}/order/list.htm" id="orderSearchForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">订单列表页面</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>订单编号</td>
						<td><input type="text" name="code" class="input-text lh25" size="16" value="${(query.parentOrderCode)!}${(query.orderCode)!}"></td>
						<td>保税区</td>
						<td><span class="fl">
								<div class="select_border">
									<div class="select_containers ">
										<select name="seaChannel" class="select" style="width: 120px;">
										 	<option value="" <#if ! query.seaChannel??>selected="selected"</#if>>请选择</option>
										 	<#list channelList as channel>
										 		<option value="${channel.id }" <#if ((query.seaChannel)!0) == (channel.id)>selected="selected"</#if>>${channel.name }</option>
										 	</#list>
										</select>
									</div>
								</div>
						</span></td>
						<td>收货人手机</td>
						<td><input type="text" name="consigneeMobile" class="input-text lh25" size="16"  value="${(query.consigneeMobile)!}"></td>
						<td>下单时间</td>
						<td><input type="text" name="startTime" class="input-text lh25" size="16" value="<#if query.startTime??>${(query.startTime)?string("yyyy-MM-dd HH:mm")}</#if>"></td>
						<td>至</td>
						<td><input type="text" name="endTime" class="input-text lh25" size="16"  value="<#if query.endTime??>${(query.endTime)?string("yyyy-MM-dd HH:mm")}</#if>"></td>
					</tr>

					<tr>
						<td>订单类型</td>
						<td><span class="fl">
								<div class="select_border">
									<div class="select_containers ">
										<select name="type" class="select" style="width: 120px;">
										 	<option value="" <#if ! query.type??>selected="selected"</#if>>请选择</option>
										 	<#list orderTypeList as orderType>	
										 		<option value="${orderType.code}" <#if query.type==orderType.code>selected="selected"</#if>>${orderType.cnName}</option>
											</#list>
										</select>
									</div>
								</div>
						</span></td>
						<td>下单渠道</td>
						<td><span class="fl">
								<div class="select_border">
									<div class="select_containers ">
										<select name="source" class="select" style="width: 120px;">
											<option value="">请选择</option>
											<#list resourceTypeList as resourceType>
												<option value="${resourceType.code}" <#if query.source==resourceType.code>selected</#if> >${resourceType.desc}</option>
											</#list>
										</select>
									</div>
								</div>
							</span>
						</td>
						<td>收货人姓名</td>
						<td><input type="text" name="consigneeName" class="input-text lh25" size="16"  value="${(query.consigneeName)!}"></td>
						<td>卡券</td>
						<td>
							<input type="text" name="promoterName" class="input-text lh30 promotername ui-autocomplete-input" size="16" value="${(query.promoterName)!}"  autocomplete="off">
							<input type="hidden" name="promoterId" value="${(query.promoterId)!}"/>
						</td>
						<td>线下扫码</td>
						<td>
							<input type="text" name="scanPromoterName" class="input-text lh30 scanpromotername ui-autocomplete-input"  size="16"  value="${(query.scanPromoterName)!}"  autocomplete="off">
							<input type="hidden" name="scanPromoterId" value="${(query.scanPromoterId)!}"/>
						</td>
					</tr>
					<tr>
						<td>订单状态</td>
						<td><span class="fl">
								<div class="select_border">
									<div class="select_containers ">
										<select name="orderStatus" class="select" style="width: 120px;">
										 	<option value="" <#if ! query.orderStatus??>selected="selected"</#if>>请选择</option>
										 	<option value="1" <#if ((query.orderStatus)!0) == 1>selected="selected"</#if>>新建</option>
											<option value="2" <#if ((query.orderStatus)!0) == 2>selected="selected"</#if>>待支付</option>
										 	<option value="3" <#if ((query.orderStatus)!0) == 3>selected="selected"</#if>>待转移</option>
										 	<option value="4" <#if ((query.orderStatus)!0) == 4>selected="selected"</#if>>待发货</option>
										 	<option value="5" <#if ((query.orderStatus)!0) == 5>selected="selected"</#if>>待收货</option>
										 	<option value="6" <#if ((query.orderStatus)!0) == 6>selected="selected"</#if>>已收货</option>
										 	<option value="0" <#if ((query.orderStatus)!-1) == 0>selected="selected"</#if>>已取消</option>
										</select>
									</div>
								</div>
						</span></td>
						<td>供应商</td>
						<td>
							<select id = "supplierId" name = "supplierId" class="select2" style="width: 145px;">
								<option value=''>请选择</option>
								<#if supplierList??>
								<#list supplierList as sp>
									<option value=${sp.id} <#if sp.id == query.supplierId>selected="selected"</#if>>${sp.name}</option>
								</#list>			
								</#if>
							</select>
						</td>
						<td>仓库</td>
						<td>
							<select id="warehouseId" name="warehouseId" class="select2" style="width:145px;">
								<option value=''>请选择</option>
								<#if warehouseList??>
								<#list warehouseList as wh>
									<option value=${wh.id} <#if wh.id==query.warehouseId>selected="selected"</#if>>${wh.name}</option>	
								</#list>
								</#if>
								</option>
							</select>
						</td>
						<td>店铺</td>
						<td>
							<input type="text" name="shopPromoterName" class="input-text lh30 shoppromotername ui-autocomplete-input"  size="16"  value="${(query.shopPromoterName)!}"  autocomplete="off">
							<input type="hidden" name="shopPromoterId" value="${(query.shopPromoterId)!}"/>
						</td>
						<td>商城</td>
						<td>
							<select name="channelCode" class="select" style="width: 120px;">
								<option value=''>请选择</option>
								<#if channelInfoList??>
								<#list channelInfoList as ci>
									<option value="${ci.channelCode}" <#if query.channelCode==ci.channelCode>selected</#if> >${ci.promoterName}</option>
								</#list>
								</#if>
							</select>
						</td>
					</tr>
				</table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
				<div class="search_bar_btn" style="text-align: right;">
					<span style="margin-right: 10px;">共<span style="color:red;">${page.records}</span>条数据</span>
					<a href="#"><input class="btn btn82 btn_search" type="submit" value="查询" /></a>
					<a href="#"><input class="btn btn82 btn_export" type="button" value="导出" /></a>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="table" class="mt10">
	<div class="box span10 oh">
	<table  id="CRZ0" width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
		<tr>
			<th style="width:125px">订单编号</th>
			<th style="width:100px">父订单编号</th>
			<th style="width:60px">订单状态</th>
			<th style="width:80px">订单类型</th>
			<th style="width:80px">支付类型</th>
			<th style="width:100px">支付单号</th>
			<th style="width:100px">供应商名称</th>
			<th style="width:60px">实付金额</th>
			<th style="width:80px">下单时间</th>
			<th style="width:80px">支付时间</th>
			<th style="width:80px">发货时间</th>
			<th style="width:60px">卡券推广</th>
			<th style="width:60px">店铺</th>
			<th style="width:80px">操作</th>
		</tr>
		<#if page.rows??> 
		<#list page.rows as sub>
		<tr>
			<td>${sub.subOrder.orderCode}</td>
			<td>${sub.order.parentOrderCode}</td>
			<td>${sub.subOrder.statusStr}</td>
			<td>${sub.subOrder.typeStr}</td>
			<#if sub.subOrder.type gte 3>
				<td>${sub.subOrder.payTypeStr} - ${sub.subOrder.payWayStr}</td>
			<#else>
				<td>${sub.order.payTypeStr} - ${sub.order.payWayStr}</td>
			</#if>
			<td>${sub.subOrder.payCode}</td>
			<td>${sub.subOrder.supplierName}</td>
			<td>${(sub.subOrder.total!0)?string("0.00")}</td>
			<td>${sub.subOrder.createTime?datetime}</td>
			<td><#if sub.order.payTime ??>${sub.order.payTime?datetime}</#if></td>
			<td><#if sub.orderDelivery.deliveryTime ??>${sub.orderDelivery.deliveryTime?string('yyyy-MM-dd HH:mm:ss')}</#if></td>
			<td>${sub.promoterName}</td>
			<td>${sub.shopPromoterName}</td>
			<td width="200" data_subOrderId="${sub.subOrder.id}"><a href="javascript:void(0);" onclick="view('${sub.subOrder.orderCode}')">[查看]</a><a href="javascript:void(0);" onclick="view('${sub.subOrder.parentOrderCode}')">[查看父单]</a></td>
		</tr>
		</#list> 
		</#if>

	</table>
</div>
	
</div>
<@pager  pagination=page  formId="orderSearchForm"  /> 
</form>
</@backend>
