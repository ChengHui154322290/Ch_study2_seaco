<#include "/common/common.ftl"/> <@backend title="" js=[
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js','/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js','/statics/backend/js/salesorder/utils.js' ]
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >


<div class="box span10 oh">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
		<tr>
			<th width="15%">标题</th>
			<th width="5%">操作人</th>
			<th width="8%">当前状态</th>
			<th width="8%">先前状态</th>
			<th width="15%">创建时间</th>
			<th>日志内容</th>
		</tr>
		<#list logList as log>
		<tr>
			<td>${log.name}</td>
			<td>${log.createUserName}</td>
			<td>${log.currStatusStr}</td>
			<td>${log.preStatusStr}</td>
			<td>${log.createTime?datetime}</td>
			<td>${log.content}</td>
		</tr>
		</#list>

	</table>
</div>

</@backend>
