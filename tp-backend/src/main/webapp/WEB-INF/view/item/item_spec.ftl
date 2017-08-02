<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/select2/js/select2.js',
	'/statics/select2/js/select2Util.js',
	'/statics/select2/js/select2_locale_zh-CN.js',
	'/statics/backend/js/item/item-select2.js',
	'/statics/backend/js/item/item-prd.js'
	] 
	css=['/statics/backend/css/common.css','/statics/select2/css/select2.css',
	     '/statics/backend/css/style.css'
	] >
	<div class="box_border">
       <div class="box_center">
		 <#if specGroupList?default([])?size !=0>
			 <div class="tc">
			            条形码： <input type="text" class="input-text lh30" size="20" value="" id="barcode" maxlength=20  />
				<input type="button" id="chooseSpecBtn"  value="确定" class="ext_btn ext_btn_submit m10" />
			</div>
		   <table class="input">
            <#list specGroupList as g>
			   <tr class="">
				<th>
					<label>
						${g.specGroup.name}
					</label>
				</th>
				<td>
					<span class="">
						<#if g.specDoList?default([])?size !=0>
						    <select
								name="supplierId"  class="select2 specGroupSelect" style="width:250px; margin-left: 1px" >
									<option value=""  >--请选择规格--</option>
								<#list g.specDoList as s>
									<option specGroupId = "${g.specGroup.id}"  value="${s.id}" >${s.spec}</option>
								</#list>
							</select>
	        			</#if>
					</span>
				</td>
			  </tr>
		   </#list>
		   </table>
         <#else>
         	均码只能有一个prd
         </#if>
		</div>
		</div>
	
</@backend>		
		