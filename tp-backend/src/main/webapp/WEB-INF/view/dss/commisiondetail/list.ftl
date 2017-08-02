<#include "/common/common.ftl"/> 
<@backend title="佣金明细管理" 
 js=[ '/statics/backend/js/layer/layer.min.js',
 	  '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.js',
	  '/statics/backend/js/jqgrid/js/jquery.jqGrid.src.js',
	  '/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
	  '/statics/backend/js/dss/jqGridformatter.js',
	'/statics/backend/js/dss/commisiondetail.js'] 
	css=[
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css'
] >
<form method="post" action="${domain}/dss/commisiondetail/list.htm" id="commisiondetailListForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">佣金明细列表</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<#if fixed==1>
						<td><input type="hidden" name="promoterId" value="${commisionDetail.promoterId}"></td>
						<#else>
						<td>促销员</td>
						<td><input type="text" style="width:120px;margin-left:20px;" class="input-text lh30 promotername ui-autocomplete-input" size="40" autocomplete="off">
							<input type="hidden" name="promoterId" value="${commisionDetail.promoterId}">
						</td>
						</#if>
						<td>订单号</td>
						<td><input type="text" name="orderCode" value="${commisionDetail.orderCode}" class="input-text lh25" size="20"></td>
						<td>汇总状态</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="collectStatus" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list collectStatusList as collectStatus>
			                       		<option value="${collectStatus.code}" <#if collectStatus.code==commisionDetail.collectStatus>selected</#if> >${collectStatus.cnName}</option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
                    	</td>
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
</form>
<table id="commisiondetaillist" class="scroll"></table>
<div id="gridpager" class="scroll"></table>
</@backend>
