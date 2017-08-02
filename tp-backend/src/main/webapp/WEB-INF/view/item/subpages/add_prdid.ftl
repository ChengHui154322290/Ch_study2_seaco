<div class="box_border">
	<div class="box_top">
		<b class="pl15">PRDID信息</b>
	</div>
	<div class="box_center">
	
		<!--编辑时候不可以修改规格  -->
		<#if item.detailList?default([])?size !=0>
		<#else>
		<table class="input tabContent">
			<tr>
				<th width="15">
				    <input type="button" id="newPrdidBtn" value="新增" class="ext_btn ext_btn_submit" >
				    <input type="button" id="clearPrdidListBtn" value="清空" class="ext_btn ext_btn_submit">
				</th>
			</tr>
		</table>
		</#if>
		
		<table id="skuListTable" class="input tabContent">
			<#if item.detailList?default([])?size !=0>
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
                <#list item.detailList as detail>
			<tr class="prdidsList">
				<td width="60">
				<input type="hidden" name="id" value="${detail.id}" />
				<input type="text" name="barcode" value="${detail.barcode}" class="input-text lh30" size="15" /> </td>
				<td width="100">${detail.prdid}</td>
				<td width="100"><input type="text" name="mainTitle" value="${detail.mainTitle}" class="input-text lh30" size="15"  /></td>
				<#if detail.detailSpecList?default([])?size !=0>
					<#list detail.detailSpecList?sort_by("specGroupId") as dd>
						<td width="100">${dd.specName}</td>
					</#list>
				</#if>
				<td width="100">${detail.onSaleStr}</td>
				<td width="100"><a href="javascript:void(0);" onclick="show(${detail.id});">[sku编辑]</a></td>
			</tr>
			</#list>
			</#if>
			<script>
			function show(id){
				var date = new Date();
				var tv={
					url:'/item/detail.htm?detailId='+id,
					linkid:'add_detail_'+date.getMilliseconds(),
					tabId:'add_detail_'+date.getMilliseconds(),
					text:'编辑sku信息'
				};
				parent.window.showTab(tv);
			}
			
			</script>
		</table>
	</div>
</div>