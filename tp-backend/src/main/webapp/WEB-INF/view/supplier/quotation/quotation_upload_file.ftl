<#include "/common/common.ftl"/> 

<@backend title="" 
	js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/supplier/js/web/quotation_upload_file.js'
	] 
	css=[
	'/statics/backend/css/style.css' ] >
<!--
-->
<form id="inputForm" action="${domain}/supplier/uploadQuotationExcel.htm" method="post" enctype="multipart/form-data">
	<div style="height:30px;"></div>
	<div style="border:1px solid #ccc ;">
		<div style="text-align:left; height:30px;">
			上传文件：<input type="file" value="上传" id= "quotationExcel" name="quotationExcel" >
						 <input type="hidden" value="quotationExcel" name="fieName" />
		</div>
		
		<div style="text-align:left;height:30px;">
			导入模板：
			 <a id="downTemplateBtn" href="#">[下载模板]</a>
		</div>
		
		<div class="tc">
			<input type="button" value="上传"  class="ext_btn ext_btn_submit m10 dataFormSave">
			<input type="button" value="返回" class="ext_btn m10" id="closeBtn">
		</div>
	</div>	
</form>
</@backend>
