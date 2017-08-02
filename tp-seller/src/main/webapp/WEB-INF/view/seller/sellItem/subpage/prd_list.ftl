<div class="box_border">
	<div class="box_top">
		<b class="pl15">PRD列表信息</b>
	</div>
	<div class="box_center">
		<table id="skuListTable" class="input tabContent">
			<#if item.itemDetailList?default([])?size !=0>
				<tr>
					<th width="60">条形码</th>
					<th width="100">PRDID</th>
					<th width="100">商品名称</th>
					<!--动态规格-->
					<#if groupList?default([])?size !=0>
						<#list groupList?sort_by("id")  as g> 
							<th width="100">${g.name}</th>
						</#list>
					</#if>
					<th width="100">是否上架</th>
					<th width="100">操作</th>
				</tr>
                <#list item.itemDetailList as detail>
			<tr class="prdidsList">
				<td width="60">
				<input type="hidden" name="id" value="${detail.id}" />
				<input type="text" name="barcode" value="${detail.barcode}" class="input-text lh30" size="25" /> </td>
				<td width="100">${detail.prdid}</td>
				<td width="100"><input type="text" name="mainTitle" value="${detail.mainTitle}" class="input-text lh30" size="25"  /></td>
				<#if detail.itemDetailSpecList?default([])?size !=0>
					<#list detail.itemDetailSpecList?sort_by("specGroupId") as dd>
						<td width="100">
							<input type="hidden" class="specIdInfo"  value = "${dd.specId}"  name = "${dd.specGroupId}_${dd.specId}" />${dd.specName}
						</td>
					</#list>
				</#if>
				<td width="100">${detail.onSaleStr}</td>
				<td width="100"><a href="javascript:void(0);" onclick="show(${detail.id});">[sku编辑]</a></td>
			</tr>
			</#list>
			</#if>
		</table>
	</div>
</div>