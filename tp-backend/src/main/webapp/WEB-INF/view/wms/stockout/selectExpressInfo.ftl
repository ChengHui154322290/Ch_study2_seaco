<#include "/common/common.ftl"/> 

<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/form.js',
	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
	'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
	'/statics/backend/js/wms/stockout.js',
	'/statics/backend/js/wms/js/jquery.form.js',
	'/statics/select2/js/select2.js',
	'/statics/select2/js/select2Util.js',
	'/statics/select2/js/select2_locale_zh-CN.js',
	'/statics/backend/js/item/item-select2.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js'
	] css=[
	'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css',
	'/statics/backend/css/style.css',
	'/statics/select2/css/select2.css',
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
 ] >
<form id="doUpdateStockOutStatusByExpressForm" action="${domain}/wms/stockout/doUpdateStockOutStatusByExpress.htm" method="post">
	<input type="hidden" id="expressCode" name="expressCode" value=""/> 
	<input type="hidden" id="expressName" name="expressName" value=""/>
	<div style="height:30px;"></div>
	<div style="text-align:center; height:30px;">
		选择快递公司：	<select class="select2" name="expressInfoName" id="expressInfoId" style="width:200px; margin-left: 1px">
	                  	<#list expressList as express>
	                  	<option value="${(express.code)!}">${(express.name)!}</option>
	                  	</#list>
					</select>		
	</div>
	<div style="height:30px;"></div>
	<div style="text-align:center; height:100px;">
		<#assign i=0>
		<#list stockOutList as stockOut>
		<input type="hidden" name="stockOutList[${i}].id" value="${stockOut.id}"/>
		订单编号：<input type="text" class="input-text lh25" id="stockOutList[${i}].externalNo" name="stockOutList[${i}].externalNo" value="${stockOut.externalNo}" readonly/>&nbsp;&nbsp;&nbsp;
		运单号：<input type="text" class="input-text lh25" id="stockOutList[${i}].expressId" name="stockOutList[${i}].expressId" value="${stockOut.expressId}"/>&nbsp;&nbsp;&nbsp;
		发货时间：<input type="text" class="input-text lh25" id="stockOutList[${i}].startCreateTime" name="stockOutList[${i}].startCreateTime" value="${stockOut.startCreateTime}" readonly/>
		</br>
		<#assign i=i+1>
		</#list>
		<input type="button" value="更新" onclick="doUpdateStockOutStatusByExpress(${stockOutList?size})" class="ext_btn ext_btn_submit m10 dataFormSave">
	<div id="importMsgDiv" style="text-align:center; height:30px;" class="color307fb1"></div>
	</div>
</form>
<script>
function addTimeControl(objName) {
	$(":text[name='"+objName+"']").datetimepicker();
}

$(function(){
	for (var i=0; i<${stockOutList?size}; i++) {
		addTimeControl("stockOutList["+i+"].startCreateTime");
	}
});
</script>
</@backend>