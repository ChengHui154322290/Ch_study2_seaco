<table class="form_table pt15 pb15"  width="100%" cellspacing="0" cellpadding="0" border="0" >
	<tbody>
		<tr>
			<td class="td_right w80">退货单号：</td>
			<td>${rejectInfo.rejectCode}</td>
			<td>申请退货时间：</td>
			<td>${rejectInfo.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
			<td>退货原因：</td>
			<td>${rejectInfo.rejectReason}</td>
		</tr>
		<tr>
			<td class="td_right w80">补偿：</td>
			<td>${rejectInfo.offsetAmount}</td>
			<td>审核人：</td>
			<td>${rejectInfo.updateUserId}</td>
		</tr>
	</tbody>
</table>
     <div id="table" class="mt10">
        <div class="box span10 oh">
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
</div>
</div>