<form class="jqtransform" method="post" id="queryAttForm" action="${domain}/freight/list.htm">
	<div class="box_border">
		<div class="box_top"><b class="pl15">运费模板</b></div>
		<div class="box_center pt10 pb10">
        	<#include "/promotion/freight/subpages/searchCondition.ftl" />       
        </div>      
        <div class="pb5 pt5" style="border-top:1px solid #dadada;">
        	<#include "/promotion/freight/subpages/submitButton.ftl" />     
		</div>
	</div>
</form>