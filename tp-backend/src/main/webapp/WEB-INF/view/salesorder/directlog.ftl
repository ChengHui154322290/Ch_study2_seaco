<#include "/common/common.ftl"/> <@backend title="" js=[
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js','/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js','/statics/backend/js/salesorder/utils.js' ]
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >


<div class="box span10 oh">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
		<tr>
			<th width="8%">操作类型</th>
			<th width="8%">操作状态</th>
			<th width="40%">操作信息</th>
			<th width="15%">创建时间</th>
		</tr>
		<#list logList as log>
		<tr>
			<td>${log.operatTypeStr}</td>
			<td>${log.isSuccessStr}</td>
			<td>${log.operatMessage}</td>
			<td>${log.createTime?datetime}</td>
		</tr>
		</#list>

	</table>
</div>

</@backend>
