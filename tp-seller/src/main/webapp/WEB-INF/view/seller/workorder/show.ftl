<#include "/layout/inner_layout.ftl" />
<@sellContent title="工单管理" 
	js=[
		'/static/workorder/workorder.js'
	  ] 
	css=[] >
	<!--
	<input type="button" value="审核通过" onclick="auditWorkorder('3',${workorderInfo.id})" class="ext_btn m10">
	<input type="button" value="审核不通过" onclick="auditWorkorder('2',${workorderInfo.id})" class="ext_btn m10">
	<input type="button" value="重新开启订单" onclick="openOrder()" class="ext_btn m10">
	-->
	<form id="workorderInfoForm" action="${domain}/seller/workorder/show.htm" method="post">
	<div class="mt10" id="forms">
        <div class="box">
          <div class="box_border">
            <#if errorMessage??><div class="box_top"><b class="pl15">${errorMessage}</b></div></#if>
            <#include "/seller/workorder/showinfo.ftl"/>
            <#include "/seller/workorder/log.ftl"/>
          </div>
        </div>
     </div>
     </form>
</@sellContent>