<#include "/common/common.ftl"/> <@backend title="" js=[
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js','/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js','/statics/backend/js/salesorder/utils.js' ]
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >


<div class="box span10 oh">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
		<#if expressList??>
		<#list expressList as express>
		<tr>
			<td>快递公司</td>
			<td>${express.companyName}</td>
			<td>快递单号</td>
			<td>${express.packageNo}</td>
		</tr>
		<tr>
			<td>处理时间</td>
			<td colspan="3">处理信息</td>
		</tr>
		<#if (express.expressLogInfoList)??>
		<#list express.expressLogInfoList as log>
		<tr>
			<td>${log.dataTime}</td>
			<td colspan="3">${log.context }</td>
		</tr>
		</#list>
		</#if>
		</#list>
		</#if>

	</table>
</div>

</@backend>
