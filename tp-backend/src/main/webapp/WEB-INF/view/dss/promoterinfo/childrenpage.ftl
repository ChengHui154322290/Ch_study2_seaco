<#include "/common/common.ftl"/> 
<@backend title="促销人员管理" 
 js=[ '/statics/backend/js/layer/layer.min.js',
	  '/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	  '/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
	  '/statics/backend/js/dss/promoterinfo.js'] 
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css'] >
<input type="hidden" name="parentPromoterId" value="${parentPromoterId}"/>
<table id="chilrentree" class="mt10 chilrentree"></table>
<div id="gridpager" class="scroll"></div> 
</@backend>