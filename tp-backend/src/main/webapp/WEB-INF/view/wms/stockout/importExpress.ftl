<#include "/common/common.ftl"/>
<@backend title="出库报检信息列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
'/statics/backend/js/wms/jquery.form.js',
'/statics/backend/js/wms/stockout.js'
] css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >
<form id="doImportExpressForm" action="${domain}/wms/stockout/doImportExpress.htm" method="post" enctype="multipart/form-data">
	<div style="height:30px;"></div>
	<div style="border:1px solid #ccc ;">
		<div style="text-align:center; height:50px;">
			上传文件：<input type="file" value="上传" id="expressExcel" name="expressExcel" >
			<input type="button" value="导入" id="doImportExpressBtn" class="ext_btn ext_btn_submit m10 dataFormSave">
		</div>
		<div id="importMsgDiv" class="color307fb1">
		</div>
	</div>	
</form>
</@backend>