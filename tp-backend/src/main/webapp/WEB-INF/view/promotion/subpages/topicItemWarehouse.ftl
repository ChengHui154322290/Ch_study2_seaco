<#if (warehouses??)>
	<select class="select" style="width:150px;" id="warehouse">
		<#list warehouses as warehouse>
			<option value="${warehouse.id}" bondedArea="${warehouse.bondedArea}" whType="${warehouse.type}">
				${warehouse.name}
			</option>
		</#list>
	</select>
</#if>