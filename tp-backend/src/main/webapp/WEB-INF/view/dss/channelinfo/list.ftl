<#include "/common/common.ftl"/> 
<@backend title="促销人员管理" 
 js=['/statics/backend/js/layer/layer.min.js',
 	  '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.js',
	  '/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	  '/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
	  '/statics/backend/js/dss/jqGridformatter.js',
	'/statics/backend/js/dss/channelInfo.js'] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css'] >

<form method="post" action="${domain}/dss/refereedetail/list.htm" id="refereedetailListForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">营销渠道管理</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<input type="button" value="查询" class="btn btn82 btn_search querypagebtn" name="button">
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<table id="channelinfolist" class="scroll"></table>
<div id="gridpager" class="scroll"></div>
</form>
</@backend>
