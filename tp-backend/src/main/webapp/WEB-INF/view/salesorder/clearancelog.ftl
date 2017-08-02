<#include "/common/common.ftl"/> <@backend title="" js=[
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js','/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js','/statics/backend/js/salesorder/utils.js' ]
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >


<div class="box span10 oh">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
		<tr>
			<th width="8%">订单号</th>
			<th width="8%">结果</th>
			<th width="8%">结果描述</th>
			<th width="5%">时间</th>
		</tr>
		<#list clearanceLogList as log>
		<tr>
			<td>${log.orderCode}</td>
			<td>
				<#if "${log.result}"=="0">
					失败
				</#if>
				<#if "${log.result}"=="1">
					成功
				</#if>
			</td>
			<td>${log.resultDesc}</td>
			<td>${log.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
		</tr>
		</#list>
	</table>
</div>

</@backend>
