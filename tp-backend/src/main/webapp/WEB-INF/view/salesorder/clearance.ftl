<#include "/common/common.ftl"/> 
<@backend title="" js=[
	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
	'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
	'/statics/backend/js/salesorder/utils.js',
	'/statics/backend/js/salesorder/clearance.js'
]
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >


<div class="box span10 oh">
	<div class="box_top">
		<b class="pl15">清关状态列表页面</b>
	</div>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
	    <tr>
			<th width="100">类型</th>
			<th width="100">状态描述</th>
			<th width="150">操作</th>
		</tr>
		<#if statusList??>
		<#list statusList as st>
			<tr>
				<td width="100">${st.type.desc}</td>
				<td withd="100">${st.statusStr}</td>
				<td width="150">
					<a href="${domain}/order/clearancelog.htm?orderCode=${orderCode}&type=${st.type.index}">[日志]</a>
					<a href="javascript:void(0);" data_type="${st.type.index}" data_code="${orderCode}" class="order-clearance-reset">[重置]</a>
				</td>	
			</tr>
		</#list>
		</#if>
	</table>
</div>

</@backend>
