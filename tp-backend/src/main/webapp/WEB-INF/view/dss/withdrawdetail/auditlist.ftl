<#include "/common/common.ftl"/> 
<@backend title="流水明细" 
 js=[ '/statics/backend/js/layer/layer.min.js',
 	  '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.js',
	  '/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	  '/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
	  '/statics/backend/js/dss/jqGridformatter.js',
	  '/statics/backend/js/dss/withdrawdetail.js'] 
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css'] >
<form method="post" action="${domain}/dss/withdrawdetail/list.htm" id="withdrawdetaillistForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">提现申请审核列表</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>姓名/手机号</td>
						<td>
							<input type="text" style="width:120px;margin-left:20px;" class="input-text lh30 withdraworname ui-autocomplete-input" size="40" autocomplete="off" value="${withdrawDetail.withdrawor}">
							<input type="hidden" name="withdrawor">
						</td>
						<td>审核状态</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="withdrawStatus" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list withdrawStatusList as withdrawStatus>
			                       		<option value="${withdrawStatus.code}" <#if withdrawStatus.code==withdrawDetail.withdrawStatus>selected</#if> >${withdrawStatus.cnName}</option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
                    	</td>
					</tr>
				</table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
				<div class="search_bar_btn" style="text-align: center;">
					<input type="button" value="查询" class="btn btn82 btn_search querywithdrawpagebtn" name="button">
					<input type="button" value="导出" class="btn btn82 btn_export exportwithdrawbtn" name="button">
					<input type="button" value="批量审核" class="btn btn82 btn_save2 batchauditbtn" name="button">
					<input type="button" value="批量打款" class="btn btn82 btn_save2 batchpaymentedbtn" name="button">
				</div>
			</div>
		</div>
	</div>
</div>
<table id="withdrawauditlist" class="mt10 withdrawauditlist scroll"></table>
<div id="gridpager" class="scroll"></div> 
<div>
<br/>
</div>
<table id="withdrawloglist" class="mt10 withdrawloglist scroll"></table>
<div id="gridpagerlog" class="scroll"></div> 
</form>
</@backend>