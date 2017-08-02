<#if logList??>
<div>
	<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table">
		<thead>
			<tr>
				<th width="80">操作类型</th>
				<th width="100">审核状态</th>
				<th>内容</th>
				<th width="130">审核时间</th>
				<th width="100">操作人</th>
			</tr>
		</thead>
		<tbody>
			<#list logList as log>
			<tr class="tr">
				<td>${log.zhActionType}</td>
				<td>${log.zhAuditStatus}</td>
				<td>${log.logContent}</td>
				<td>${log.createTime?string('yy-MM-dd HH:mm:ss')}</td>
				<td>${log.operatorName}[${log.zhOperatorType}]</td>
			</tr>
			</#list>
		</tbody>
	</table>
</div>
</#if>