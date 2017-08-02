<#include "/common/common.ftl"/> 
<@backend title="" 
js=[
		'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
		'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js']
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >


<div class="box span10 oh">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
		<tr>
			<th width="8%">当前状态</th>
			<th width="8%">先前状态</th>
			<th width="5%">创建时间</th>
			<th width="5%">日志内容</th>
		</tr>
		<#if logList?default([])?size !=0>
		<#list logList as log>
		<tr>
			<td>${log.curStatusStr}</td>
			<td>${log.preStatusStr}</td>
			<td>${log.createTime?datetime}</td>
			<td>${log.content}</td>
		</tr>
		</#list>
		</#if>
	</table>
</div>

</@backend>
