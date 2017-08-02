<#include "/layout/inner_layout.ftl" />
<@sellContent title="新增商品" 
    js=[ 
    	'/static/scripts/item/addSellerSpu.js',
    	'/static/scripts/item/item-category.js',
    	'/static/scripts/select2/js/select2.js',
		'/static/scripts/select2/js/select2Util.js',
		'/static/scripts/item/item-select2.js' 
    ]  
    css=[  
	    '/static/scripts/select2/css/select2.css'
    	]>

<style>


</style>
	<div class="box_top">
		<b class="pl15">商品SPU信息</b>
	</div>
	
	
	  <form class="form-inline" role="form" id="searchSpuForm">
		<table  class="table table-bordered">
			<tr>
				<td class="td_right">
					商品码（条形码）:
				</td>
				<td class="" colspan=1>
				    ${barCode}
				</td>
			</tr>
			<!--初始化列表查询-->
			<input type="hidden" id="largeIdHidden" value="${item.info.largeId}" />
			<input type="hidden" id="mediumIdHidden" value="${item.info.mediumId}" />
			<input type="hidden" id="smallIdHidden" value="${item.info.smallId}" />
			<tr>
				<td class="td_right"><span class="requiredField">*</span>商品类别：</td>
				<td class="">
				<span class="fl"> 
					<#if item.info.id?exists>
							<input type="hidden" id="spuId" name="infoId" value="${item.info.id}" />
					</#if>
					<select
						name="largeId" class="select2" id="largeIdSel" style="width:200px; margin-left: 1px">
							<option value="">--请选择分类--</option>
						<#list categoryList as category>
							<option value="${category.id}"  <#if item.info.largeId==category.id> selected</#if> >${category.name} </option>
						</#list>
					</select> <select name="mediumId" class="select2" id="mediumIdSel" style="width:200px; margin-left: 1px" >
							<option value="">--请选择分类--</option>
					</select> <select name="smallId" class="select2" id="smallIdSel" style="width:250px; margin-left: 1px" >
							<option value="">--请选择分类--</option>
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
								<option value="${brand.id}" <#if "${brand.id}"=="${item.info.brandId}"> selected</#if> >${brand.name}</option>
							</#list>
					   </select>	
					</span>
					<span class="requiredField">*</span>单位：
					<select
						name="unitId" class="select2" id="unitId"  style="width:150px; margin-left:1px" >
							<option value="">--请选择单位--</option>
						<#list unitList as unit>
							<option value="${unit.id}"  <#if "${unit.id}"=="${item.info.unitId}"> selected</#if> >${unit.name}</option>
						</#list>
				    </select>		
				</td>
			</tr>
			<tr>  
				<td class="td_right"><span class="requiredField">*</span>商品名称：</td>
				<td class=""><input type="text" name="itemName" id="itemName"  	class="input-text lh30" size="60" maxlength=60  placeholder="商品关键字搜索" />  <input type="button" class="btn btn-default" name="searchButton" value="查询" id="searchItemSpuForm"></td>
			</tr>
			
			<tr>
				<td class="td_right">备注：</td>
				<td class=""><input type="text" name="spuRemark" id="spuRemark"  	class="input-text lh30" size="60" maxlength=60  /></td>
			</tr>
		</table>
		
		
		
		
		<table class="table table-bordered">
				<b class="pl15">PRDID信息</b>
					<tr align="center">    
						<th  style="text-align:center;"  align="center" width="15">         
						    <input id="newPrdidBtn" value="新增" class="btn btn-default"  onclick="addNewSpec()" type="button"/>   
						    <input id="clearPrdidListBtn" value="清空" class="btn btn-default" type="button"/>  
						</th>
					</tr>
		</table>
		
		
		<table  id="prdListTable" class="table table-bordered">
		</table>
		
		<div style="height:50px; line-height:30px;text-align:center;">   
				<input type="button" class="btn btn-default" id="dataFormSave" value="保存" />  
		</div>
		
	</form>
	
	  	<div id="addSellerContentShow">
	  	
       
       </div>
</@sellContent>