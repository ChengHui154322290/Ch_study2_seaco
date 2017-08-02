<#include "/common/common.ftl"/> 
<@backend title="流水明细" 
 js=[ '/statics/backend/js/layer/layer.min.js',
	  '/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	  '/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
	  '/statics/backend/js/dss/jqGridformatter.js',
	  '/statics/backend/js/dss/accountdetail.js'] 
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css'] >
<input type="hidden" name="userId" value="${accountDetail.userId}"/>
<table id="accountdetaillist" class="mt10 accountdetaillist scroll"></table>
<div id="gridpager" class="scroll"></div> 
</@backend>