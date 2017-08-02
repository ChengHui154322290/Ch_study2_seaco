<#include "/common/common.ftl"/> 
<@backend title="" 
	css = ['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css','/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css']
	js = [ 
		'/statics/backend/js/dateTime2/js/jquery-ui.min.js',
		'/statics/backend/js/layer/layer.min.js',
		'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
		'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
		'/statics/backend/js/account/list.js'
	] 
>

<form method="post" action="${domain}/account/list.htm" id="itemSearchForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">账户列表页面</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>对接方编号</td>
						<td><input type="text" name="id" format='number' class="input-text lh25" size="20" value="${(query.id)!}"></td>
						<td>对接方名称</td>
						<td><input type="text" name="name" class="input-text lh25" size="20" value="${(query.name)!}"></td>
					</tr>
				</table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
				<div class="search_bar_btn" style="text-align: right;">
					<span style="margin-right: 10px;">共<span style="color:red;">${page.totalCount}</span>条数据</span>
					<input class="btn btn82 btn_search" type="submit" value="查询" />
					<input class="btn btn82 btn_add" type="button" opt_type="add" value="新增" />
				</div>
			</div>
		</div>
	</div>
</div>
<div id="table" class="mt10">
	<div class="box span10 oh">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
		<tr>
			<th width="8%">账户编号</th>
			<th width="8%">对接方名称</th>
			<th width="8%">AppKey</th>
			<th width="8%">token</th>
			<th width="5%">账号状态</th>
			<th width="8%">修改时间</th>
			<th width="8%">创建时间</th>
			<th width="5%">操作</th>
		</tr>
		<#if page.rows??> 
		<#list page.rows as sub>
		<tr>
			<td>${sub.account.id!}</td>
			<td>${sub.account.name!}</td>
			<td>${sub.account.appkey!}</td>
			<td>${sub.account.token!}</td>
			<td>
				<#if sub.account.status=1>有效<#elseif sub.account.status=0>无效<#else>已删除</#if>
			</td>
			<td>${sub.account.modifyTime?datetime!}</td>
			<td>${sub.account.createTime?datetime!}</td>
			<td width="200"> 
				<a href="javascript:void(0);" opt_type="edit" opt_value="${sub.account.appkey!}">[修改]</a>
				<a href="javascript:void(0);" opt_type="delete" opt_value="${sub.account.appkey!}">[删除]</a>
			</td>
		</tr>
		</#list> 
		</#if>
	</table>
</div>
	
</div>
<@pager  pagination=page  formId="itemSearchForm"/> 
</form>
</@backend>
