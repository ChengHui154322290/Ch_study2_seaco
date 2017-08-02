<form class="jqtransform" method="post" id="queryAttForm" action="${domain}/user/point/my/list.htm">
	<div class="box_border">
		<div class="box_top"><b class="pl15">完善个人信息送积分</b></div>
		<div class="box_center pt10 pb10">
			<#include "/user/point/subpages/myCondition.ftl" />       
        </div>      
        <div class="pb5 pt5" style="border-top:1px solid #dadada;">
		    <#include "/user/point/subpages/submitButton.ftl" />     
		</div>
	</div>
</form>