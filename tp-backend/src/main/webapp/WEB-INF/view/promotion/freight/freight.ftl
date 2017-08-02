<#include "/common/common.ftl"/>
<@backend title="" js=['/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/promotion/freight.js',
	'/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js']
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css'] >
	<div id="search_bar" class="mt10">
       <div class="box">
        	<#include "/promotion/freight/subpages/form.ftl" />       
       </div>
    </div>
    <table id="tree"></table>  
    <div id="gridpager" class="scroll"></div>       
</@backend>