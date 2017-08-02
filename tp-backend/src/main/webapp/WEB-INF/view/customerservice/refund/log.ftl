<#if logList??>
<div>
	<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table">
		<thead>
			<tr>
				<th width="100">原状态</th>
				<th width="100">当前状态</th>
				<th>内容</th>
				<th width="130">创建时间</th>
				<th width="100">操作人</th>
			</tr>
		</thead>
		<tbody>
			<#list logList as log>
			<tr class="tr">
				<td>
					<#if log.oldRefundStatus==1>等待打款</#if>
					<#if log.oldRefundStatus==2>打款中</#if>
					<#if log.oldRefundStatus==3>打款成功</#if>
					<#if log.oldRefundStatus==4>打款失败</#if>
					<#if log.oldRefundStatus==5>取消</#if>
				</td>
				<td>
					<#if log.currentRefundStatus==1>等待打款</#if>
					<#if log.currentRefundStatus==2>打款中</#if>
					<#if log.currentRefundStatus==3>打款成功</#if>
					<#if log.currentRefundStatus==4>打款失败</#if>
					<#if log.currentRefundStatus==5>取消</#if>
				</td>
				<td>${log.logContent}</td>
				<#if log.currentRejectStatus == 0 || log.currentRejectStatus == 4><td></td>
				<#else>
					<td>${log.createTime?string('yy-MM-dd HH:mm:ss')}</td>
				</#if>
				<td>
					${log.operatorName}
					[<#if log.operatorType == 1>会员</#if>
					<#if log.operatorType == 2>西客商城</#if>
					<#if log.operatorType == 3>商家</#if>
					<#if log.operatorType == 4>供应商</#if>
					<#if log.operatorType == 5>系统</#if>
					<#if log.operatorType == 6>外部系统</#if>]
				</td>
			</tr>
			</#list>
		</tbody>
	</table>
</div>
</#if>