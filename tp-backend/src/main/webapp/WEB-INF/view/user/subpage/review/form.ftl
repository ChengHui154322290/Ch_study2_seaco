<form class="jqtransform" method="post" id="reviewForm" action="${domain}/user/review/list.htm">
	<div class="box_border">
		<div class="box_top">
	    	<b class="pl15">评论管理->商品评论</b> 
	        
	    </div>
		<div class="box_center pt10 pb10">
        	<#include "/user/subpage/review/searchCondition.ftl" />       
        </div>      
        <div class="pb5 pt5" style="border-top:1px solid #dadada;">
        	<#include "/user/subpage/review/submitButton.ftl" />     
		</div>
	</div>
</form>