<#include "/common/common.ftl"/>
<div class="box span10 oh">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
		<tr>
			<th width="3%"><input type="checkbox" name="chkAll" /></th>
			<th width="8%">订单编号</th>
			<th width="8%">父订单编号</th>
			<th width="5%">订单状态</th>
			<th width="5%">订单类型</th>
			<th width="5%">支付类型</th>
			<th width="5%">订单来源</th>
			<th width="8%">供应商名称</th>
			<th width="8%">订单总额（含运费）</th>
			<th width="8%">下单时间</th>
			<th width="8%">支付时间</th>
			<th width="8%">发货时间</th>
			<th width="5%">操作</th>
		</tr>
		<#if page.list??> 
		<#list page.list as sub>
		<tr>
			<td style="text-align: center;"><input type="checkbox" name="chk" /></td>
			<td>${sub.subOrder.code}</td>
			<td>${sub.order.code}</td>
			<td>${sub.subOrder.statusStr}</td>
			<td>${sub.subOrder.typeStr}</td>
			<td>${sub.order.payTypeStr} - ${sub.order.payWayStr}</td>
			<td>${sub.order.sourceStr}</td>
			<td>${sub.subOrder.supplierName}</td>
			<td>${((sub.subOrder.total!0) + (sub.subOrder.freight!0))?string("0.00")}</td>
			<td>${sub.subOrder.createTime?datetime}</td>
			<td><#if sub.order.payTime ??>${sub.order.payTime?datetime}</#if></td>
			<td><#if sub.delivery.deliveryTime ??>${sub.delivery.deliveryTime?datetime}</#if></td>
			<td width="200" data_subOrderId="${sub.subOrder.id}"><a href="${domain}/order/view.htm?code=${sub.subOrder.code}">[查看]</a></td>
		</tr>
		</#list> 
		</#if>

	</table>
</div>
<@pager  pagination=page  formId="itemSearchForm"  /> 

