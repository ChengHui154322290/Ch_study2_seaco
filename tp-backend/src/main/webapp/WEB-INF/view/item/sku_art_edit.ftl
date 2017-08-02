<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/jquery.tools.js',    
	'/statics/backend/js/form.js',
	'/statics/select2/js/select2.js',
	'/statics/select2/js/select2Util.js',
	'/statics/select2/js/select2_locale_zh-CN.js',
	'/statics/backend/js/item/item-select2.js',
	'/statics/backend/js/item/item-sku-art-add.js']
	css=['/statics/select2/css/select2.css'] >
<div> 
   <form action="editSkuArtNumber.htm" method="post" class="jqtransform"   id='editSkuArtNumberForm'  > 
   	 <input type="hidden" name="id" value="${skuArtDO.id}" />
   	 <input type="hidden" name="skuId" value="${skuArtDO.skuId}" />
     <table class="form_table" border="0" cellpadding="0" cellspacing="0">
      <tr>
      	<td class="td_right requiredField">*商品备案号:</td>
		<td ><input type="text" value = "${skuArtDO.articleNumber}"class="input-text lh30" size="25"	name="articleNumber"  id="articleNumber" maxlength=60 onMouseOver="this.title=this.value" /></td>
      </tr>
      <tr>
     	 <td class="td_right requiredField">通关渠道:
		</td>
		 <td>
			<select name="bondedArea"  id="bondedArea" class="select" >
						<option value="" >请选择</option>
						<#list channels as t>
							<option value="${t.id}" <#if t.id==skuArtDO.bondedArea>selected="selected"</#if>>${t.name}</option>  
						</#list>
			 </select> 
		</td>
      </tr>  
       <tr>
       		<td class="td_right requiredField">*HS编码:</td>
      		 <td ><input type="text" class="input-text lh30" size="100"	name="hsCode"  id="hsCode" value="${skuArtDO.hsCode}" maxlength=100  onMouseOver="this.title=this.value" /></td>
       </tr>
       <tr>
     	 <td class="td_right requiredField">*HS第一单位:</td>
		 <td>
			<select name="itemFirstUnitCode"  id="itemFirstUnitCode" class="select2" style="width:150px;">
				<option value="">请选择</option>
				<#if customsUnitList??>
					<#list customsUnitList as cu>
						<option value="${cu.code}" <#if cu.code == skuArtDO.itemFirstUnitCode>selected="selected"</#if>>${cu.name}</option>
					</#list>
				</#if>				
			</select> 
		</td>
      </tr> 
      <tr>
       		<td class="td_right requiredField">*HS第一单位对应数量:</td>
      		 <td ><input type="text" class="input-text lh30" size="100"	value="${skuArtDO.itemFirstUnitCount}" name="itemFirstUnitCount"  id="itemFirstUnitCount" maxlength=100  onMouseOver="this.title=this.value" /></td>
       </tr>
      <tr>
     	 <td class="td_right">HS第二单位:</td>
		 <td>
			<select name="itemSecondUnitCode"  id="itemSecondUnitCode" class="select2" style="width:150px;">
				<option value="">请选择</option>
				<#if customsUnitList??>
					<#list customsUnitList as cu>
						<option value="${cu.code}" <#if cu.code == skuArtDO.itemSecondUnitCode>selected="selected"</#if>>${cu.name}</option>
					</#list>
				</#if>
			</select> 
		</td>
      </tr>
       <tr>
       		<td class="td_right requiredField">HS第二单位对应数量:</td>
      		 <td ><input type="text" class="input-text lh30" size="100"	value="${skuArtDO.itemSecondUnitCount}" name="itemSecondUnitCount"  id="itemSecondUnitCount" maxlength=100  onMouseOver="this.title=this.value" /></td>
       </tr>     
      <tr>
       		<td class="td_right requiredField">备案价:</td>
      		<td ><input type="text" class="input-text lh30" size="100"	value="${skuArtDO.itemPrice}" name="itemPrice"  id="itemPrice" maxlength=100  onMouseOver="this.title=this.value" /></td>
       </tr>
      <tr>
       		<td class="td_right requiredField">国检备案号:</td>
      		<td ><input type="text" class="input-text lh30" size="100"	value = "${skuArtDO.itemRecordNo}" name="itemRecordNo"  id="itemRecordNo" maxlength=100  onMouseOver="this.title=this.value" /></td>
       </tr>
       <tr>
       		<td class="td_right requiredField">商品报关名称:</td>
      		 <td ><input type="text" class="input-text lh30" size="100"	value="${skuArtDO.itemDeclareName}" name="itemDeclareName"  id="itemDeclareName" maxlength=100  onMouseOver="this.title=this.value" /></td>
       </tr> 
        <tr>
       		<td class="td_right">商品特征:</td>
      		 <td ><input type="text" class="input-text lh30" size="100"	value="${skuArtDO.itemFeature}" name="itemFeature"  id="itemFeature" maxlength=100  onMouseOver="this.title=this.value" /></td>
       </tr> 
      	<td class="td_right"> <a href="#"><input type="button" name="button" value="保存" id="saveEditSkuArtNumberBtn" class="btn btn82 btn_search"></a></td>
     </table>
	</form>
</div>
</@backend>