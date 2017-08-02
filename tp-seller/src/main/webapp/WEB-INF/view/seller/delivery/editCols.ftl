<#include "/layout/inner_layout.ftl" />

<@sellContent title="首页" 
    js=['/static/scripts/select2/js/select2.js','/static/scripts/web/order_delivery.js']
    css=['/static/scripts/select2/css/select2.css']>
   <div class="panel panel-default">
	
		<table style="width:800px;" border=0 height="300" class="table table-bordered">
			<tbody>
					<#list colEnums as colEnum>
				        <#assign colEnumSize = colEnums?size>
				        <#if exportColNameList??>
					         <#if exportColNameList?seq_contains(colEnum.key)>
					          	<#assign checked = 'true'>
					         <#else>
					          	<#assign checked = 'false'>
					         </#if>
				         <#else>
				         	<#assign checked = 'true'>
				         </#if>
				         <#if colEnum.key =='orderCode' || colEnum.key == 'productName'>
				          	<#assign disabled = 'true'>
				         <#else>
				          	<#assign disabled = 'false'>
				         </#if>
				        <#assign colspanNum = 7 - colEnumSize % 6>
				        <#if colEnum_index % 6 == 0>
				        	<#if colEnum_index == colEnumSize -1 >
				        	    <tr align="left">
				            		<td colspan="${colspanNum}"><input type="checkbox"  name="sellerbox" value="${(colEnum.key)!}" <#if checked=='true'>checked</#if> <#if disabled=='true'>checked disabled</#if>/>&nbsp;&nbsp;${colEnum.keyName}</td>
				          		</tr>
				        	<#else>
						    	<tr align="left">
				            		<td><input type="checkbox"  name="sellerbox" value="${(colEnum.key)!}" <#if checked=='true'>checked</#if> <#if disabled=='true'>checked disabled</#if>/>&nbsp;&nbsp;${colEnum.keyName}</td>
					        </#if>
				        <#elseif  colEnum_index % 6 == 5>
				           		<td><input type="checkbox"  name="sellerbox" value="${(colEnum.key)!}" <#if checked=='true'>checked</#if> <#if disabled=='true'>checked disabled</#if>/>&nbsp;&nbsp;${colEnum.keyName}</td>
				        	</tr>
				        <#else>
				        	<#if colEnum_index == colEnumSize -1 >
				            		<td colspan="${colspanNum}"><input type="checkbox"  name="sellerbox" value="${(colEnum.key)!}" <#if checked=='true'>checked checked</#if> <#if disabled=='true'>disabled</#if>/>&nbsp;&nbsp;${colEnum.keyName}</td>
				          		</tr>
				        	<#else>
				          		<td><input type="checkbox"  name="sellerbox" value="${(colEnum.key)!}" <#if checked=='true'>checked</#if> <#if disabled=='true'>checked disabled</#if>/>&nbsp;&nbsp;${colEnum.keyName}</td>
				        	</#if>
			        	</#if>
					</#list>
				<tr align="center">
						<td colspan="6">
					 	<button class="btn btn-primary btn-sm" id="saveCol" >&nbsp; &nbsp;保 存&nbsp; &nbsp;</button>
					 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					   <button class="btn btn-primary btn-sm" id="cancelCol" > &nbsp;&nbsp;取 消 &nbsp;&nbsp;</button>
					 </td>
				</tr>
			</tbody>
		</table>
			<input type="hidden"  id="exportInfoId" value="${(exportInfoId)!}"/>
			<input type="hidden"  id="supplierId" value="${(supplierId)!}"/>		  
	</div>
</@sellContent>
