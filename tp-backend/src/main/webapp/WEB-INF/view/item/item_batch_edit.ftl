<#include "/common/common.ftl"/> 

<@backend title="" 
	js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/item/item_batch_edit.js'
	] 
	css=[
	'/statics/backend/css/style.css' ] >
<!--
-->
<form id="batchEditForm" action="uploadEditExcel.htm" method="post" enctype="multipart/form-data">
	<div style="height:30px;"></div>
	<div style="text-align:left; height:40px;">
		<div>批量修改商品数据：目前支持商品网站显示名，市场价, 商品详情市场价。</div>
		<span style="color:red">除SKU字段，其他字段为空值，表示对原值不做修改。重复SKU将只处理其中一条。</span>
	</div>
	<div style="border:1px solid #ccc ;">
		<div style="text-align:left; height:30px;">
			上传文件：<input type="file" value="上传" id= "editExcel" name="editExcel" >
		</div>
		<div style="text-align:left;height:30px;">
			导入模板：
			 <a id="downEditTemplateBtn" href="#">[下载模板]</a>
			 <input type="hidden" value="editExcel" name="fieName" />
		</div>
		<div style="text-align:left; height:30px;">
			模板说明：请先导出完整的excel模板...
		</div>
		<div class="tc">
			<input type="button" value="上传"  class="ext_btn ext_btn_submit m10 eidtDataFormSave">
			<input type="button" value="返回" class="ext_btn m10" id="closeBtn">
		</div>
	</div>	
</form>
</@backend>
