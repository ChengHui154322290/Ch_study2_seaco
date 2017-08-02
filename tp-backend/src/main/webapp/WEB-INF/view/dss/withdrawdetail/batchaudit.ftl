<#include "/common/common.ftl"/> 
<@backend title="流水明细" 
 js=[ '/statics/backend/js/layer/layer.min.js',
 	  '/statics/backend/js/dateTime2/js/jquery-ui.min.js',
	  '/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	  '/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
	  '/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
	  '/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
	  '/statics/backend/js/dss/jqGridformatter.js',
	  '/statics/backend/js/dss/withdrawdetail.js'] 
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css',
	'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css'] >

<form method="post" action="${domain}/dss/withdrawdetail/batchaudit.htm" id="withdrawauditform">
<#list withdrawDetailIdList as withdrawDetailId>
<input type="hidden" name="withdrawDetailIdList" value="${withdrawDetailId}"/>
</#list>
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_center pt10 pb10">
				<table class="form_table pt15 pb15" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="td_right">审核：</td>
						<td >
							<#if withdrawStatusList??>
								<#list withdrawStatusList as withdrawStatus>
									<input type="radio" value="${withdrawStatus.code}" name="withdrawStatus">${withdrawStatus.cnName}
								</#list>
							</#if>
						</td>
					</tr>
					<#if paymentShow?? && paymentShow == 1>
						<tr>
							<td class="td_right">打款人：</td>
							<td><input type="text" name="paymentor" class="input-text lh30"/></td>
						</tr>
						<tr>
							<td class="td_right">打款时间：</td>
							<td><input type="text" name="payedTime" class="input-text lh30"/></td>
						</tr>
					</#if>
					<tr>
						<td class="td_right">备注：</td>
						<td>
							<textarea class="textarea" rows="10" cols="30" name="remark"></textarea>
		                 </td>
					</tr>
				</table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
				<div class="search_bar_btn" style="text-align: center;">
					<input type="button" value="审核" class="btn btn82 btn_add withdrawbatchauditbtn" name="button" id="withdrawbatchauditbtn">
				</div>
			</div>
		</div>
	</div>
</div>
</form>
</@backend>