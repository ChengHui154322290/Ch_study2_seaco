<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="supplierTable">
	<thead>
		<tr>
			<th>选择</th>
			<th>商家编号</th>
			<th>商家名称</th>
		</tr>
	</thead>
	<#if (skus??)>
		<tbody>	
			<#list skus as sku>
				<tr align="center" class="tr" style="background-color: rgb(255, 255, 255);">
					<td><input type="radio" name="skuId" value="${sku.id!}" /></td>
					<td><span>${sku.spCode!}</span></td>
					<#if "0" == "${sku.spId!}">
						<td style="width:120px;"><span>西客商城自营</span></td>
					<#else>
						<td style="width:120px;"><span>${sku.spName!}</span></td>
					</#if>
				</tr>
			</#list>
		</tbody>
	</#if>
</table>
<input type="hidden" id="skuId" />