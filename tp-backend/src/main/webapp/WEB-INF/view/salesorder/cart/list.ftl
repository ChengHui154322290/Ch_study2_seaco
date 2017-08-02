<#include "/common/common.ftl"/> 
<@backend title="" js=[ 
	'/statics/backend/js/dateTime2/js/jquery-ui.min.js',
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/salesorder/cart.js'] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
	] >

<form method="post" action="${domain}/salesorder/cart/list.htm" id="cartSearchForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top"><b class="pl15">订单列表页面</b></div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>会员</td>
						<td>
							<input type="text" class="input-text lh25 membername" size="20" name="memberName" value="${memberName}" autocomplete="off">
							<input type="hidden" name="memberId" value="${memberId}"/>
						</td>
						<td>
							<input class="btn btn82 btn_search" type="submit" value="查询" />
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<#if result??>
<div id="table" class="mt10">
	<div class="box span10 oh">
	<table id="CRZ0" width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
		<tr>
			<th width="8%">商品编码</th>
			<th width="8%">活动编码</th>
			<th width="8%">地区ID</th>
			<th width="8%">平台ID</th>
			<th width="8%">类型</th>
			<th width="8%">购买数量</th>
			<th width="5%">加入购物车时间</th>
			<th width="5%">操作</th>
		</tr>
		<#if result.data??> 
		<#list result.data as cartItem>
		<tr>
			<td>${cartItem.skuCode}</td>
			<td>${cartItem.topicId}</td>
			<td>${cartItem.areaId}</td>
			<td>${cartItem.platformId}</td>
			<td><#if cartItem.type==1>普通<#else>境外</#if></td>
			<td>${cartItem.quantity}</td>
			<td><#if cartItem.createTime??>${cartItem.createTime?string('yyyy-MM-dd HH:mm:ss')}</#if></td>
			<td>
				<a href="javascript:void(0);" class='deletecartitem' skucode="${cartItem.skuCode}" topicid="${cartItem.topicId}" memberid="${cartItem.memberId}">删除商品</a>
			</td>
		</tr>
		</#list> 
		</#if>
	</table>
	</div>
</div>
</#if>
</form>
</@backend>