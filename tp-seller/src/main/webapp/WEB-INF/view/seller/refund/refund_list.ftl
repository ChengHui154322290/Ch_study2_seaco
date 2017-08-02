<#include "/layout/inner_layout.ftl" />

<@sellContent title="首页" 
    js=['/static/scripts/web/reject.js'
    
    ]
    css=[
    '/static/seller/css/style.css',
    '/static/select2/css/select2.css',
	'/static/select2/css/common.css',
	'/static/themes/msgbox.css',
	'/static/seller/css/style.css',
	'/static/seller/css/main.css',
	'/seller/css/main.css'
    
    ]>
<div class="panel panel-default">
	<div class="panel-heading">
	    <h3 class="panel-title">退货确认</h3>
	</div>
	<div class="panel-body">
		<form class="form-inline" id="rejectInfoForm" role="form" action="${domain}/seller/refund/refundList.htm" method="GET">
			<table height="150px;">
				<tr>
		              <td>订单编号：</td>
		              <td><input type="text" name="orderNo" value="${(query.orderNo)!}" class="form-control" size="20"></td>
		              <td>退货单号：</td>
		              <td>
		               	<input type="text" name="rejectNo" value="${(query.rejectNo)!}" class="form-control" size="20">
		              </td>
		              <td>退货状态：</td>
		              <td>
		                <select class="form-control" name="rejectStatus">
		                	<option value="">-- 全部 --</option>
		                	<#list rejectStatusList as rejectStatus>
		                		<option value="${rejectStatus.code}" <#if query?? && query.rejectStatus?? && query.rejectStatus==rejectStatus.code>selected="selected"</#if>>${rejectStatus.cnName}</option>
		                	</#list>
		                </select>
		              </td>
		              <td>审核状态：</td>
		              <td>
		                <select class="form-control" name="auditStatus">
		                	<option value="">-- 全部 --</option>
		                	<#list auditStatusList as auditStatus>
		                		<option value="${auditStatus.code}" <#if query?? && query.auditStatus?? && query.auditStatus==auditStatus.code>selected="selected"</#if>>${auditStatus.cnName}</option>
		                	</#list>
		                </select>
		              </td>
	            </tr>
	            <tr>
	                <td colspan="8" align="center"><button class="btn btn-primary btn-sm" type="button" id="refund_list_query"><i class="icon-ok bigger-110"></i>查询</button></td>
	            </tr>
			</table>
		</form>
		
	 <span id="contentShow">
        
     </span>
	</div><#-- panel-body -->

</div>
	
</@sellContent>