<#include "/common/common.ftl"/> 

<@backend title="" 
	js=[

'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
'/statics/backend/js/wms/stockout.js'
] 
	css=[
	'/statics/backend/css/style.css' ] >
	<div style="height:30px;"></div>
		<input type="hidden" id="ids" name="ids" value="${ids}">
		<div style="text-align:center; height:30px;">
			选择状态：<select id="selectstatus" name="status" class="select">
                   	<#list statusList as statusArr>
                   	<option value="${statusArr.code}" >${statusArr.desc}</option>
                   	</#list>
                   </select>
			<input type="button" value="更新" id="doUpdateStockOutStatus" class="ext_btn ext_btn_submit m10 dataFormSave">
		</div>
</@backend>