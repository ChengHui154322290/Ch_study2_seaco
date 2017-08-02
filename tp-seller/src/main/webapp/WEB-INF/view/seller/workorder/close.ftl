<#include "/common/common.ftl"/> 
<@backend title="工单管理" 
	js=[
		'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
		'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
		'/statics/backend/js/layer/layer.min.js',
		'/statics/backend/js/customerservice/workorder.js'
	  ] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
	<div class="box_center">
	<input id="closeWorkorder" type="button" value="正常关闭" onclick="closeWorkorder(${workorderInfo.id});closeBtn();" class="ext_btn m10"> 
	<input id="closeWorkorder" type="button" value="强制关闭" onclick="closeWorkorder(${workorderInfo.id})" class="ext_btn m10">
	</div>
	<script>
	function closeBtn(){
		var index = parent.layer.getFrameIndex(window.name); // 获取当前窗体索引
		parent.layer.close(index);
	} 
	</script>
</@backend>