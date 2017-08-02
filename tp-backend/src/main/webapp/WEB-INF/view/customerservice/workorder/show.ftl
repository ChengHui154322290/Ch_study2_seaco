<#include "/common/common.ftl"/>
<@backend title="工单信息" 
	js=[
		'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
		'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
		'/statics/backend/js/textinput_common.js',
		'/statics/backend/js/layer/layer.min.js',
		'/statics/backend/js/customerservice/workorder.js',
		'/statics/backend/js/customerservice/reject.js'
	  ] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
	<#if workorderInfo.auditStatus ==1&&workorderInfo.status != 3&&workorderInfo.status != 4>
	<input type="button" value="审核通过" onclick="auditWorkorder('3')" class="ext_btn m10">
    </#if>
	<#if workorderInfo.auditStatus ==1&&workorderInfo.status != 3&&workorderInfo.status != 4>
	<input type="button" value="审核不通过" onclick="auditWorkorder('2')" class="ext_btn m10">
    </#if>
    <#if workorderInfo.status == 3 || workorderInfo.status == 4 >
    <input type="button" value="重新开启订单" onclick="openOrder()" class="ext_btn m10">
    </#if>
	
	<form id="workorderInfoForm" action="${domain}/customerservice/workorder/show.htm" method="post">
	<div class="mt10" id="forms">
        <div class="box">
          <div class="box_border">
            <#if errorMessage??><div class="box_top"><b class="pl15">${errorMessage}</b></div></#if>
            <#include "/customerservice/workorder/showinfo.ftl"/>
            <#include "/customerservice/workorder/log.ftl"/>
            <@pager  pagination=page  formId="workorderInfoForm" />
          </div>
        </div>
     </div>
     </form>
</@backend>