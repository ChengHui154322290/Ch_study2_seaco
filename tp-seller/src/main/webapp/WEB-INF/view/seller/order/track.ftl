<#include "/layout/inner_layout.ftl" />

<@sellContent title="订单日志" 
    js=[]
    css=['/static/css/bootstrap.css','/static/assets/css/ace.min.css']>
<div style="width:100%;height:100%; background-color: #ffffff;">
	<table width="100%" class="table table-striped table-bordered table-hover">
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

</@sellContent>
