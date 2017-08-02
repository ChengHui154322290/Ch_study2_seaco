<#include "/common/common.ftl"/> 
<@backend title="促销人员管理" 
 js=['/statics/backend/js/layer/layer.min.js',
 	  '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.js',
	  '/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	  '/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
	  '/statics/backend/js/dss/jqGridformatter.js',
	'/statics/backend/js/dss/refereedetail.js'] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css'] >

<form method="post" action="${domain}/dss/refereedetail/list.htm" id="refereedetailListForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">拉新佣金列表</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<#if fixed==1>
						<input type="hidden" name="promoterId" value="${promoterId}">
						<#else>
						<td>促销员</td>
						<td><input type="text" style="width:120px;margin-left:20px;" class="input-text lh30 promotername ui-autocomplete-input" size="40" autocomplete="off">
							<input type="hidden" name="promoterId" value="${commisionDetail.promoterId}">
						</td>
						</#if>
						<td>下单会员账号</td>
						<td>
							<input type="text" style="width:120px;margin-left:20px;" class="input-text lh30 memberaccount ui-autocomplete-input" size="40" autocomplete="off">
							<input type="hidden" name="memberId" value="${commisionDetail.memberId}">
						</td>
						<td>
							<input type="button" value="查询" class="btn btn82 btn_search querypagebtn" name="button">
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<table id="refereedetaillist" class="scroll"></table>
<div id="gridpager" class="scroll"></div>
</form>
</@backend>
