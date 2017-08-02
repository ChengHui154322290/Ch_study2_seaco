<div class="box_border">
	<div class="box_top">
		<b class="pl15">商品SPU信息</b>
	</div>
	<!-- 基础信息  -->
	<div class="box_center">
		<table class="input commContent">
			<tr>
				<td class="td_right">
					SPU：
				</td>
				<td class="" colspan=1>
				    ${item.itemInfo.spu}
				</td>
			</tr>
			<!--初始化列表查询-->
			<input type="hidden" id="largeIdHidden" value="${item.itemInfo.largeId}" />
			<input type="hidden" id="mediumIdHidden" value="${item.itemInfo.mediumId}" />
			<input type="hidden" id="smallIdHidden" value="${item.itemInfo.smallId}" />
			<input type="hidden" id="supplierId" name="supplierId" value="${supplierId}" />
			<tr>
				<td class="td_right"><span class="requiredField">*</span>商品类别：</td>
				<td class="">
				<span class="fl"> 
					<#if item.itemInfo.id?exists>
							<input type="hidden" id="spuId" name="infoId" value="${item.itemInfo.id}" />
					</#if>
					<select
						name="largeId" class="select2" id="largeIdSel" style="width:200px; margin-left: 1px">
							<option value="">--请选择分类--</option>
							<#if categoryList??>
								<#list categoryList as category>
									<option value="${category.id}"  <#if "${item.itemInfo.largeId}"== "${category.id}"> selected</#if> >${category.name} </option>
								</#list>
							</#if>
					</select> <select name="mediumId" class="select2" id="mediumIdSel" style="width:200px; margin-left: 1px" >
							<option value="">--请选择分类--</option>
							<#if midCategoryList??>
								<#list midCategoryList as midc>
									<option value="${midc.id}"  <#if "${item.itemInfo.mediumId}"=="${midc.id}"> selected</#if> >${midc.name} </option>
								</#list>
							</#if>
					</select> <select name="smallId" class="select2" id="smallIdSel" style="width:250px; margin-left: 1px" >
							<option value="">--请选择分类--</option>
						<#if smlCategoryList??>
							<#list smlCategoryList as sml>
								<option value="${sml.id}"  <#if "${item.itemInfo.smallId}"=="${sml.id}"> selected</#if> >${sml.name} </option>
							</#list>
						</#if>
					</select>
				</span></td>
			</tr>
			<tr>
				<td class="td_right"><span class="requiredField">*</span>商品品牌：</td>
				<td class="">
					<span class="fl">
				   		<select
							name="brandId" id="brandIdSel" class="select2" style="width:200px; margin-left:1px"  >
								<option value="">--请选择品牌--</option>
								<#list brandList as brand>
								<option value="${brand.id}" <#if "${brand.id}"=="${item.itemInfo.brandId}"> selected</#if> >${brand.name}</option>
							</#list>
					   </select>	
					</span>
					<span class="requiredField">*</span>单位：
					<select
						name="unitId" class="select2" id="unitId"  style="width:150px; margin-left:1px" >
							<option value="">--请选择单位--</option>
						<#list unitList as unit>
							<option value="${unit.id}"  <#if "${unit.id}"=="${item.itemInfo.unitId}"> selected</#if> >${unit.name}</option>
						</#list>
				    </select>		
				</td>
			</tr>
			<tr>
				<td class="td_right"><span class="requiredField">*</span>SPU名称：</td>
				<td class=""><input type="text" name="mainTitle" id="mainTitle" value="${((item.itemInfo.mainTitle)!'')?xhtml}" 	class="input-text lh30" size="60" maxlength=60  onMouseOver="this.title=this.value" /></td>
			</tr>
			<tr>
				<td class="td_right">备注：</td>
				<td class="" colspan=3><input type="text" id="spuRemark" value="${((item.itemInfo.remark))?xhtml}" name="remark" class="input-text lh30" size="60" maxlength=60 onMouseOver="this.title=this.value"  /></td>
			</tr>
		</table>
	</div>
</div>