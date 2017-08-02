<#include "/layout/inner_layout.ftl" />

<@sellContent title="订单日志" 
    js=[]
    css=['/static/css/bootstrap.css','/static/assets/css/ace.min.css']>
<div style="width:100%;height:100%; background-color: #ffffff;">
	<table width="100%" class="table table-striped table-bordered table-hover">
		<thead>
		<tr>
			<th width="15%">标题</th>
			<th width="5%">操作人</th>
			<th width="8%">当前状态</th>
			<th width="8%">先前状态</th>
			<th width="16%">创建时间</th>
			<th>日志内容</th>
		</tr>
		</thead>
		<tbody>
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
		</tbody>
	</table>
</div>
</@sellContent>