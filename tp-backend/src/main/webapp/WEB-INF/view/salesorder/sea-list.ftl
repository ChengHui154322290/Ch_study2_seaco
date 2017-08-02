<#include "/common/common.ftl"/> 
<@backend title="" js=[ 
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/layer/extend/layer.ext.js',
	'/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
	'/statics/backend/js/dateTime2/js/jquery-ui.min.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
	'/statics/backend/js/salesorder/utils.js', 
	'/statics/backend/js/salesorder/sea-list.js'] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css',
	'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css'] >

<form method="post" action="${domain}/order/sea-list.htm" id="orderSearchForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top"><b class="pl15">订单列表页面</b></div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>订单编号</td>
						<td><input type="text" name="orderCode" class="input-text lh25" size="20" value="${(query.code)!}${(query.orderCode)!}">
						<!--	<input style="display: none;" type="text" name="type" value="3"> -->
						</td>
						<td />
						<td>供应商</td>
						<td><input type="text" name="supplierName" class="input-text lh25" size="20"  value="${(query.supplierName)!}"></td>
						<td>海关审核状态</td>
						<td>
							<select name="clearanceStatus" class="select" style="width: 120px;">
								<option value="">请选择</option>
								<#list clearanceStatusList as cs>
									<option value="${cs.code}" <#if cs.code==query.clearanceStatus>selected="selected"</#if>>${cs.desc}</option>
								</#list>
							</select>
						</td>
					</tr>
				</table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
				<div class="search_bar_btn" style="text-align: right;">
					<a href="#"><input class="btn btn82 btn_search" type="submit" value="查询" /></a>
					<a href="#"><input class="btn btn82 btn_search clearanceinfolist" type="button" value="清关单" /></a>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="table" class="mt10">
	<div class="box span10 oh">
	<table id="CRZ0" width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
		<tr>
			<th width="8%">订单编号</th>
			<th width="8%">支付编号</th>
			<th width="8%">支付时间</th>
			<th width="8%">真实姓名</th>
			<th width="8%">身份证号</th>
			<th width="8%">实付金额</th>
			<th width="5%">订单类型</th>
			<th width="5%">订单状态</th>
			<th width="8%">海关审核状态</th>
			<th width="5%">操作</th>
		</tr>
		<#if page.rows??> 
		<#list page.rows as sub>
		<tr>
			<td>${sub.orderCode}</td>
			<td>${sub.payCode}</td>
			<td><#if sub.payTime??>${sub.payTime?string('yyyy-MM-dd HH:mm:ss')}</#if></td>
			<td></td>
			<td></td>
			<td>${(sub.total!0)?string("0.00")}</td>
			<td>${sub.typeStr}</td>
			<td>${sub.statusStr}</td>
			<td>${sub.clearanceStatusStr}</td>
			<td width="200" data_subOrderId="${sub.id}"><a href="${domain}/order/view.htm?code=${sub.orderCode}">[查看]</a><a class="btn-cancel" href="javascript:void(0);" data_code="${sub.orderCode}">[取消]</a></td>
		</tr>
		</#list> 
		</#if>

	</table>
</div>
	
</div>
<@pager  pagination=page  formId="orderSearchForm"  /> 
</form>
</@backend>
