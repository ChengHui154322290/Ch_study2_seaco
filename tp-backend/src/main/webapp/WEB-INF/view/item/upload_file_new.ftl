<#include "/common/common.ftl"/> 

<@backend title="" 
	js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/item/upload-file.js'
	] 
	css=[
	'/statics/backend/css/style.css' ] >
<!--
-->
<form id="inputForm" action="uploadItemDetail.htm" method="post" enctype="multipart/form-data">
	<div style="height:30px;"></div>
	<div style="border:1px solid #ccc ;">
		<div style="text-align:left; height:30px;">
			上传文件：<input type="file" value="上传" id= "itemDetailExcel" name="itemDetailExcel" >
			<input type="hidden" value="itemDetailExcel" name="fieName" />
		</div>
	<!--	
		<div style="text-align:left;height:30px;">
			导入模板：
			 <a id="downTemplateBtn" href="#">[下载模板]</a>
			 <input type="hidden" value="skuExcel" name="fieName" />
		</div>
		<div style="text-align:left; height:30px;">
			模板说明：请先导出完整的excel模板...
		</div>
	-->		
		<div class="tc">
			<input type="button" value="上传"  class="ext_btn ext_btn_submit m10 dataFormSaveNew">
			<input type="button" value="返回" class="ext_btn m10" id="closeBtn">
		</div>
	</div>	
</form>
</@backend>
