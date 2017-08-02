<#include "/common/common.ftl"/> 

<@backend title="" 
	js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/customerservice/upload-file.js'
	] 
	css=[
	'/statics/backend/css/style.css' ] >
<!--
-->
<form id="inputForm" action="${domain}/customerservice/offset/importtemplate.htm" method="post" enctype="multipart/form-data">
	<div style="height:30px;"></div>
	<div style="border:1px solid #ccc ;">
		<div style="text-align:left; height:30px;">
			上传文件：<input type="file" value="上传" id= "skuExcel" name="skuExcel" >
		</div>
		<div class="tc">
			<input type="button" value="上传" id= "dataFormSave" class="ext_btn ext_btn_submit m10">
			<input type="button" value="返回" class="ext_btn m10" id="closeBtn">
		</div>
	</div>	
</form>
</@backend>