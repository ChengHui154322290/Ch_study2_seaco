<#include "/common/common.ftl"/> <@backend title="" js=[
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js','/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js','/statics/backend/js/salesorder/utils.js' ]
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >


<div class="box span10 oh">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
		<tr>
			<th width="8%">优惠券号</th>
			<th width="8%">优惠券类型</th>
			<th width="8%">面值</th>
			<th width="5%">实际优惠金额</th>
		</tr>
		<#list couponList as coupon>
		<tr>
			<td>${coupon.code}</td>
			<td>${coupon.typeStr!}</td>
			<td>${coupon.amount?string("0.00")}</td>
			<td>${coupon.discount?string("0.00")}</td>
		</tr>
		</#list>

	</table>
</div>

</@backend>
