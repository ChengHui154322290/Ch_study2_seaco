<table class="list_table"  width="100%" cellspacing="0" cellpadding="0" border="0" >
	<thead>
		<tr>
			<th>商品编号</th>
			<th>商品名称</th>
			<th>单价</th>
			<th>购买商品数量</th>
			<th>退商品数量</th>
			<th>退款金额小计</th>
		</tr>
	</thead>
	<tbody>
		<#list itemList as item>
			<tr>
				<td>${item.itemSkuCode}</td>
				<td>${item.itemName}</td>
				<td>${item.itemUnitPrice?string('0.00')}</td>
				<td>${item.itemQuantity}</td>
				<td>${item.itemRefundQuantity}</td>
				<td>${item.itemRefundAmount?string('0.00')}</td>
			</tr>
		</#list>
	</tbody>
</table>