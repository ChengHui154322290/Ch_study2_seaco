<#include "/common/common.ftl"/> 
<@backend title="" 
	css = ['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css','/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css']
	js = [ 
		'/statics/backend/js/dateTime2/js/jquery-ui.min.js',
		'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
		'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
		'/statics/backend/js/account/suplist.js'
	] 
>
<div id="table" class="mt10">
	<div class="box span10 oh">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
			<tr>
				<th width="3%">序号</th>
				<th width="8%">编号(供应商)</th>
				<th width="8%">编码(供应商)</th>
				<th width="8%">简称(供应商)</th>
				<th width="8%">名称(供应商)</th>
			</tr>
			<#if supList??> 
			<#list supList as sup>
			<tr>
				<td>${sup_index+1}</td>
				<td>${sup.id!}</td>
				<td>${sup.supplierCode!}</td>
				<td>${sup.alias!}</td>
				<td>${sup.name!}</td>
			</tr>
			</#list> 
			</#if>
		</table>
	</div>
</div>
</@backend>
