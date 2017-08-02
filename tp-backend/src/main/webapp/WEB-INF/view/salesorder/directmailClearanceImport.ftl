<#include "/common/common.ftl"/> 

<@backend title="" 
	js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/salesorder/clearance.js'
	] 
	css=[
	'/statics/backend/css/style.css' ] >
<!--
-->
<form id="dmClearanceImportForm" action="uploadDMClearanceExcel.htm" method="post" enctype="multipart/form-data">
	<div style="height:30px;"></div>
	<div style="text-align:left; height:40px;">
		<div>批量更新直邮订单的报关数据：目前支持航班号，总提运单号，运输工具编号。</div>
	</div>
	<div style="border:1px solid #ccc ;">
		<div style="text-align:left; height:30px;">
			上传文件：<input type="file" value="上传" id= "dmClearanceExcel" name="dmClearanceExcel" >
		</div>
		<div style="text-align:left;height:30px;">
			导入模板：
			 <a id="downDMClearanceTemplateBtn" href="#">[下载模板]</a>
			 <input type="hidden" value="dmClearanceExcel" name="fieName" />
		</div>
		<div style="text-align:left; height:30px;">
			模板说明：请先导出完整的excel模板...
		</div>
		<div class="tc">
			<input type="button" value="上传"  class="ext_btn ext_btn_submit m10 dmClearanceImport">
			<input type="button" value="返回" class="ext_btn m10" id="dmImportCloseBtn">
		</div>
	</div>	
</form>
</@backend>
