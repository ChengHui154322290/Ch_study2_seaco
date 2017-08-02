<#include "/common/common.ftl"/>
<@backend title="" js=['/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
	'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
	'/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	'/statics/backend/js/user/review.js',
	'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js']
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css'] >
	<style>  
		.input-text {
		    width: 138px;
		}
    </style>
	<div id="search_bar" class="mt10">
       <div class="box">
        	<#include "/user/subpage/review/form.ftl" />       
       </div> 
    </div>
    
    <!--虚线-->
    <hr style="border: 1px dashed #247DFF;" />
                   
    <div style="margin-left:0px;margin-top:2px;margin-bottom:2px;">
    	<#include "/user/subpage/review/addImportButton.ftl" />
    </div>  
      
    <!--虚线-->
    <hr style="border: 1px dashed #247DFF;" />  
      
    <div style="margin-top:8px;">
    	<table id="tree"></table>  
    </div>      
    <div id="gridpager" class="scroll"></div>   
</@backend>