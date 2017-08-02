<#include "/layout/inner_layout.ftl" />

<@sellContent title="首页" 
    js=['/static/select2/js/select2.js','/static/scripts/web/order_delivery.js' ]
    css=['/static/select2/css/select2.css']>
   <div class="panel panel-default">
	<div class="panel-heading">
	    <h3 class="panel-title">商家批量发货</h3>
	</div>
	<div class="panel-body">
	<input type="hidden"  id="exportInfoId" value="${(exportInfoId)!}"/>
	<input type="hidden"  id="supplierId" value="${(supplierId)!}"/>
	<input type="hidden"  id="totalCount" value="${(totalCount)!}"/>
		<table style="width:919px;" border=0  class="table table-bordered">
			<thead>
				<tr>
					<th colspan="4">导出等待发货的订单</th>
					<td colspan="4">
						<#if exportColKeyList?exists>
							<a id="hrefEditCols" href="#" style="text-decoration:underline;color:blue;">编辑导出列</a>
						<#else>
							<a id="hrefAddCols" href="#" style="text-decoration:underline;color:blue;">新增导出列</a>
						</#if>
					</td>
				</tr>
			</thead>
			<tbody>
				<#if exportColKeyList?exists>
					<#assign colKeySize = exportColKeyList?size>
					<#list exportColKeyList as colKey>
				        <#assign colspanNum = 9 - colKeySize % 8>
				        <#if colKey_index % 8 == 0>
				        	<#if colKey_index == colKeySize -1 >
				        	    <tr align="left">
				            		<th colspan="${colspanNum}">${(orderFieldMap[colKey])!}</th>
				          		</tr>
				        	<#else>
						    	<tr align="left">
				            		<th>${(orderFieldMap[colKey])!}</th>
					        </#if>
				        <#elseif  colKey_index % 8 == 7>
				           		<th>${(orderFieldMap[colKey])!}</th>
				        	</tr>
				        <#else>
				        	<#if colKey_index == colKeySize -1 >
				            		<th colspan="${colspanNum}">${(orderFieldMap[colKey])!}</th>
				          		</tr>
				        	<#else>
				          		<th>${(orderFieldMap[colKey])!}</th>
				        	</#if>
			        	</#if>
					</#list>
					<input type="hidden" id ="colKeySize" value="${colKeySize}"/>
			 	<#else>
			 		<tr align="center" rowspan="8">
						<td colspan="8" ><font size="4" style="font-weight:bold">请新增导出列！初次下载请勾选需要信息，勾选后系统自动保存</font></td>
					</tr>
			 	 </#if>
		
				<tr>
				    <th colspan="1">快递公司：</th>
					<td colspan="3">
						<select class="form-control"  id="expressSelect">
						<option></option>
						<#if allExpress?exists>
							<#list allExpress as express>
							<option value="${(express.code)!}">${(express.name)!}</option>
						  </#list>
						 </#if>
						</select>
					</td>
					<td colspan="4" style="vertical-align: baseline;">
					   <button class="btn btn-primary btn-sm" id="exportExpress" >导出默认快递公司</button>
					   <button class="btn btn-primary btn-sm" id="exportOrder" >导出未发货订单</button>
						（未发货总数：<font color="red">${(totalCount)!}</font>）
					</td>
				</tr>
			</tbody>
		</table>
		
		<form id="importForm" action="/seller/delivery/upload.htm" method="post" enctype="multipart/form-data">
			<table style="width:919px;" height="70" class="table table-bordered">
				<thead>
					<tr>
						<th colspan="8">将填有快递单号的订单导入进行发货</th>
					</tr>
				</thead>
				
				<tbody>
					<tr>
						<td align="left" width="30%">
							<input type="file" id= "ftltemple" name="file" class="input-text lh30 ftltemple" size="10"/>
						</td>
						<td align="left"><button class="btn btn-primary btn-sm" id="importOrder" >导入发货订单</button></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	</div>
</@sellContent>
