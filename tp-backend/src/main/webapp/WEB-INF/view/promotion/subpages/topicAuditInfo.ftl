<!-- 商品信息 -->
<!-- 审核记录 -->
<table id="topicAudit" class="input tabContent">
	<tr>
		<td class="td_left">
			<h4>审核记录</h4>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="topicAuditLogsList">
				<thead>
					<tr>
						<th>姓名</td>
						<th>操作</th>
						<th>备注</th>
						<th>时间</th>
					</tr>
				</thead>
				<#if (topicDetail.auditLogList??)>
					<tbody>
						<#list topicDetail.auditLogList as auditLog>
							<tr align="center" style="background-color: rgb(255, 255, 255);">
								<td>
									<span>${auditLog.auditName!}</span>
								</td>
								<td>
									<span>${auditLog.auditOperation!}</span>
								</td>
								<td>
									<span>${auditLog.remark!}</span>
								</td>
								<td>
									<#if (auditLog.createTime??)>
										<span>${auditLog.createTime?string("yyyy-MM-dd HH:mm:ss")!}</span>
									</#if>
								</td>
							</tr>
						</#list>
					</tbody>
				</#if>
			</table>
		</td>
	</tr>
</table>