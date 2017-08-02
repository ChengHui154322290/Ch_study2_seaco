<#if order??>
<tr>
	  <td class="td_right">订单编号：</td>
	  <td style="width:120px;">${order.orderCode}</td>
	  
      <td class="td_right">订单状态：</td>
      <td style="width:120px;">${order.statusStr}</td>
      <td class="td_right">用户账号：</td>
      <td style="width:120px;">${buyUser.nickName}<input type="hidden" name="orderCreateUser" value="${user.nickName}"/></td>
</tr>
<tr>
      <td class="td_right">收货人手机：</td>
      <td>${buyUser.mobile}</td>
      <td class="td_right">订单类型：</td>
      <td> ${order.typeStr}</td>
      <td class="td_right">商家名称：</td>
      <td> ${order.supplierName}</td>
</tr>
<#if rejectInfoDO ??>
    	<tr>
    		<td colspan="8">
    			<table class="list_table"  width="100%" cellspacing="0" cellpadding="0" border="0" >
					<thead>
						<tr>
							<th>退货编号</th>
							<th>退货状态</th>
							<th>商品名称</th>
							<th>商品数量</th>
							<th>金额</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${rejectInfoDO.rejectCode}</td>
							<td>${rejectInfoDO.zhRejectStatus}</td>
							<td>
								<#if listRejectItemDO ??>
									<#list listRejectItemDO as rejectItemDO>
										${rejectItemDO.itemName}
									</#list>
								</#if>
							</td>
							<td>
								<#if listRejectItemDO ??>
									<#list listRejectItemDO as rejectItemDO>
										${rejectItemDO.itemRefundQuantity}
									</#list>
								</#if>
							</td>
							<td>${rejectInfoDO.refundAmount}</td>
						</tr>
					</tbody>
				</table>
    		</td>
    	</tr>
    </#if>
<#if rejctList??>
<tr>
	<td><div>
		<table>
			<thead><tr><th>退货编号</th><th>退货状态</th><th>商品名称</th><th>商品数量</th><th>金额</th></tr></thead>
			<tbody>
				<#list rejectList as reject>	
					<tr>
						 <td>${reject.rejectCode}</td>
						 <td>${reject.zhRejectStatus}</td>
						<#if reject.rejectItemList?default([])?size !=0>
						   <#list reject.rejectItemList as item>
			               <td>${item.itemName}</td>
			               <td>${item.itemRefundQuantity}</td>
			               </#list>
						<#else>
		 				<td>-</td>
			            <td>-</td>
			            <td>${reject.refundAmount?string('0.00')}</td>
		               </#if>
					</tr>
				</#list>
			</tbody>
		</table>
	</div></td>
</tr>
</#if>
<#else>
<tr>
	<td class="td_right"></td>
	<td colspan="5">${errorMessage}</td>
</tr>
</#if>