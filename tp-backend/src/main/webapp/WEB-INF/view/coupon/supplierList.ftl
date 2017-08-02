<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ">
	<tbody>	
		<tr>
			<th>选择</th>
			<th>商户ID</th>
			<th>商户名称</th>
		</tr>
		<#if (supplierList??)>
			<#list supplierList as supplier>
				<tr align="center" class="tr" style="background-color: rgb(255, 255, 255);">
					<td><input type="radio" name="brandId" value="${supplier.id!}" /></td>
					<td>${supplier.id!}</td>
					<td>${supplier.name!}</td>
				</tr>
			</#list>
		</#if>
	</tbody>
</table>
<input type="hidden" id="startPage" name="brand.startPage" value="${currPage}" />
<div style="width:100%;height:30px;">
	<div style="float:right;padding-right:10px;">
		<a href="javascript:void(0);" id="nextPage">下一页</a>
	</div>
	<div style="float:right;padding-right:10px;">
		<a href="javascript:void(0);" id="prePage">上一页</a>
	</div>
	<div style="float:right;padding-right:10px;">
		总：<span>${supplierCount!}</span>
		每页：
		<#if ("15" == "${pageSize!}")>
			<select name="pageSize" id="pageSize">
				<option value="10">10</option>
				<option value="15" selected>15</option>
			</select>
		<#elseif ("10" == "${pageSize!}")>
			<select name="pageSize" id="pageSize">
				<option value="10" selected>10</option>
				<option value="15">15</option>
			</select>
		</#if>
		<span id="currPage">${currPage!}</span>/<span id="totalPage">${totalPage!}</span>页
	</div>
</div>