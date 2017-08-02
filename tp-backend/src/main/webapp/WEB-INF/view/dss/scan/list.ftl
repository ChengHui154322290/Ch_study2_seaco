<#include "/common/common.ftl"/> 
<@backend title="邀请码分销员管理" 
 js=['/statics/themes/layout.js',
 	 '/statics/backend/js/layer/layer.min.js',
 	 '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.js',
	 '/statics/backend/js/jqgrid/js/jquery.jqGrid.src.js',
	 '/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
	 '/statics/backend/js/dss/jqGridformatter.js',
	'/statics/backend/js/dss/scanpromoterinfo.js'] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css'] >

<form method="post" action="${domain}/dss/list.htm" id="promoterinfoListForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">邀请码分销员列表</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>公司名或姓名</td>
						<td><input type="text" name="promoterName" class="input-text lh25" size="10" value="${promoterInfo.promoterName}"></td>
						<td>开通状态</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="inviteCodeUsed" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list inviteCodeUsedList as inviteCodeUsed>
			                       		<option value="${inviteCodeUsed.code}" <#if inviteCodeUsed.code==promoterInfo.inviteCodeUsed>selected</#if> >${inviteCodeUsed.cnName}</option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
                    	</td>
						<td><input type="hidden" name="promoterType" value="3" /></td>
					</tr>
				</table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
				<div class="search_bar_btn" style="text-align: right;">
					<input type="button" value="查询" class="btn btn82 btn_search querypagebtn" name="button">
					<input type="button" value="新增" class="btn btn82 btn_add editpromoterinfobtn" name="button" param="">
				</div>
			</div>
		</div>
	</div>
</div>
</form>
<table id="promoterinfolist" class="scroll"></table>
<div id="gridpager" class="scroll"></table>
</@backend>
