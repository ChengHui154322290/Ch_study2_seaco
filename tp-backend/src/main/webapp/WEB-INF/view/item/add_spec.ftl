<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/jquery.tools.js',
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/tab.js',
	'/statics/backend/js/item/item.js',
	'/statics/backend/js/item/item-spec.js'
	] 
	css=['/statics/backend/css/common.css',
	     '/statics/backend/css/style.css'
	] >
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
	
</@backend>		
		