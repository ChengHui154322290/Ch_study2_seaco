<#include "/common/common.ftl"/> 

<@backend title="" 
	js=[
	'/statics/cms/js/jquery/jquery.form.js',
	'/statics/cms/js/common/upload-cms-file.js'
	] 
	css=[
	'/statics/backend/css/style.css' ] >
<!--
-->
<form id="inputForm" action="uploadMasterOrderExcel.htm" method="post" enctype="multipart/form-data">
	<div style="height:30px;"></div>
	<div style="border:1px solid #ccc ;">
		<div style="text-align:left; height:30px;">
			上传文件：<input type="file" value="上传" id= "skuExcel" name="skuExcel" >
		</div>
		<div style="text-align:left;height:30px;">
			导入模板：
			 <a href="downExcelTemplate.htm">[下载模板]</a>
			 <input type="hidden" value="skuExcel" name="fieName" />
		</div>
		<div style="text-align:left; height:30px;">
			模板说明：请先导出完整的excel模板...
		</div>
		<div class="tc">
			<input type="button" value="上传" id= "dataFormSave" class="ext_btn ext_btn_submit m10">
			<input type="button" value="返回" class="ext_btn m10" id="closeBtn">
		</div>
	</div>	
</form>
</@backend>
