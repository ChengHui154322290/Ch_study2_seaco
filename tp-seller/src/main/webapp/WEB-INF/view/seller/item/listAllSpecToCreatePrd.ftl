<#include "/layout/inner_layout.ftl" />

<@sellContent title="" js=[
		'/static/scripts/item/item-spec.js'
	] 
	 >
	<div class="box_border">
		<div class="box_top">
			<b class="pl15">商品规格(最多选择三个规格组)</b>
		</div>
	</div>	
	   <table class="table table-bordered table-striped table-hover" >       
		 <#if specGroupList?default([])?size !=0>
            <#list specGroupList as g>
			   <tr class="">
				<th>
					<label>
						<input type="checkbox" name="specGroup${g_index}"  specGroupName="${g.specGroupDO.name}" class="specGroup" value="${g.specGroupDO.id}" />&nbsp;${g.specGroupDO.name}
					</label>
				</th>
				<td>
					<span class="">
						<#if g.specDoList?default([])?size !=0>
					 		<#list g.specDoList as s>
								<label>
									<input type="checkbox" name="specGroup${g_index}_${s_index}" pid='groupId_${g.specGroupDO.id}'  specName="${s.spec}" class="specGroup${g_index}" value='${s.id}' />${s.spec}
								</label>
							</#list>
	        			</#if>
					</span>
				</td>
			  </tr>
		   </#list>
         </#if>
		</table>   
		
		
		
		<table class="table table-bordered table-striped table-hover" style="height:50px;">            
				<tr>   
					<td>
						<td><input type="button" class="btn btn-default" id="selectAttributesbtn"  value="确定规格" /></td>  
					</td>    
       			</tr>
   		 </table>	  
   		 
	
</@sellContent>		
		