<div class="box_border">
		<div class="box_top">
			<b class="pl15">商品属性</b>
		</div>
		<div class="box_center" >
			<div class="tl" style=" border: 1px solid #d3dbde" >
				<div style="float:left; " >
					<b class="pl15">属性组 ：</b>
				</div>
				
				<div style="float:left;" id="attributeId">
					<#list listAttributeResult as attributeResult>
						<div class="attrItem" style="margin-bottom:5px;"> 
							<input type="hidden" name="attributeId" value="${attributeResult.attribute.id}" />
							
							<span>${attributeResult.attribute.name}<#if attributeResult.attribute.isRequired==true><span class="requiredField">(必选)</span></#if>:</span> 
							<#if attributeResult.attribute.allowMultiSelect >
								<!--多选 -->
								<#list attributeResult.attributeValues as l>
										<input class="attributeValueClass"  type="checkbox" isRequired="${attributeResult.attribute.isRequired}" name="attributeValueId" value=${l.id} <#if "${l.isSelect}"=="1">checked</#if> >${l.name}
								</#list>
							<#else>
								<!--单选 -->
								<select name="attributeValueId" class="select" isRequired="${attributeResult.attribute.isRequired}">
									<#if attributeResult.attribute.isRequired==true>
									<#list attributeResult.attributeValues as l>
										<option value=${l.id}  <#if "${l.isSelect}"=="1"> selected</#if> >${l.name}</option>		
									</#list>	
									<#else>
									<option value=""  >请选择</option>	
									<#list attributeResult.attributeValues as l>
										<option value=${l.id}  <#if "${l.isSelect}"=="1"> selected</#if> >${l.name}</option>		
									</#list>	
									</#if>	
								</select>
							</#if>
						 </div>	
					</#list>
				</div>
				<div style="clear:both;"></div>
			</div>
			
			<div >
				<input type="button" id="addAttributeBtn"  value="添加属性" class="ext_btn ext_btn_submit m10">
			</div>
			<table id="detailAttrList" class="input tabContent">
				<tr>
				<th width="60">自定义属性名</th>
				<th width="200">自定义属性值</th>
				<th width="60">操作 </th>
				</tr>
				<#list attrList  as l>
				<tr  class="attrList">
					<td class="td_left"  >
						<input type="text" class="input-text lh30"  name="attrKey" value="${l.attrKey}"  maxlength="20"/>
					</td>
					<td class="td_left">
					 <input type="text" class="input-text lh30"  name="attrValue" value="${l.attrValue}"  maxlength="200" />
					</td>
					<td class="td_left">
						<input type="button" id="" value="-" class="ext_btn ext_btn_submit m10 deleteAttrBtn">
					</td>
				</tr>
				</#list>
			</table>	
			
			</div>
</div>
