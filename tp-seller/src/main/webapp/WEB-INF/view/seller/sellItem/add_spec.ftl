<#include "/layout/inner_layout.ftl" />
<@sellContent title="" js=[
		'/static/seller/js/jquery.min.js',
		'/static/seller/js/colResizable-1.3.min.js',
		'/static/seller/js/common.js',
		'/static/js/favrite/favrite.js',
		'/static/supplier/component/date/WdatePicker.js',
		'/static/js/validation/jquery.validate.min.js',
		'/static/js/validation/jquery.validate.method.min.js',
		'/static/seller/js/jquery.tools.js',
		'/static/seller/js/layer/layer.min.js',
		'/static/seller/js/tab.js',
		'/static/seller/js/item/item.js',
		'/static/seller/js/item/item-spec.js'
	] 
	css=['/static/seller/css/common.css',
	     '/static/seller/css/style.css',
	     '/static/themes/msgbox.css',
		'/static/seller/css/common.css',
		'/static/seller/css/main.css',
		'/static/supplier/css/supplier_common.css',
		'/static/js/validation/jquery.validate.min.css',
		'/static/supplier/component/date/skin/WdatePicker.css',
		'/static/seller/js/layer/skin/layer.css',
		'/static/seller/css/common.css',
		'/static/seller/css/style.css'
	]  >
	<div class="box_border">
		<div class="box_top">
			<b class="pl15">商品规格(最多选择三个规格组)</b>
		</div>
		<div class="tc">
			<label>
				<input type="checkbox" name="selectSpec" id="selectSpec" value="" />是否均码
			</label>
			<input type="button" id="selectAttributesbtn"  value="选择规格" class="ext_btn ext_btn_submit m10" />
		</div>
       <div class="box_center">
	   <table class="input">
		 <#if specGroupList?default([])?size !=0>
            <#list specGroupList as g>
			   <tr class="">
				<th>
					<label>
						<input type="checkbox" name="specGroup${g_index}"  specGroupName="${g.specGroup.name}" class="specGroup" value="${g.specGroup.id}" />${g.specGroup.name}
					</label>
				</th>
				<td>
					<span class="">
						<#if g.specDoList?default([])?size !=0>
					 		<#list g.specDoList as s>
								<label>
									<input type="checkbox" name="specGroup${g_index}_${s_index}" pid='groupId_${g.specGroup.id}'  specName="${s.spec}" class="specGroup${g_index}" value='${s.id}' />${s.spec}
								</label>
							</#list>
	        			</#if>
					</span>
				</td>
			  </tr>
		   </#list>
         </#if>
		</table>
		</div>
		</div>
	
</@sellContent>		
		