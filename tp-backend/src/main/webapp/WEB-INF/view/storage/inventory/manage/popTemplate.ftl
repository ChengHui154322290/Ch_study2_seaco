<#include "/common/common.ftl"/> 

<@backend title="" 
	js=[
	'/statics/cms/js/jquery/jquery.form.js',
'/statics/backend/js/layer/layer.min.js'
	] 
	css=[
	'/statics/backend/css/style.css' ] >
<!--
-->
<script>
$(function(){
	$("#importTemplateBtn").bind("click",function(){
		var option = {
			url : "importTemplate.htm",
			type : 'POST',
			dataType : 'json',
			headers : {"ClientCallMode" : "ajax"}, //添加请求头部
			success : function(data) {
				data = JSON.parse(data)
				if(data.status==true||data.status=='true'){
					layer.alert(data.msg, 1, function(){
		        		parent.location.reload();
	            	});
				}else{
					layer.alert(data.msg, 8);
				}
				
			},
			error: function(data) {
				layer.alert("文件导入失败", 8);
			}
		};
		$("#inputForm").ajaxSubmit(option);
		return false; //最好返回false，因为如果按钮类型是submit,则表单自己又会提交一次;返回false阻止表单再次提交
	})
})
</script>
<form id="inputForm" action="importTemplate.htm" method="post" enctype="multipart/form-data">
	<div style="height:30px;"></div>
	<div style="border:1px solid #ccc ;">
		<div style="text-align:left; height:30px;">
			上传文件：<input type="file" value="上传" id= "inventoryFile" name="inventoryFile" >
		</div>
		<div style="text-align:left;height:30px;">
			导入模板：
			 <a href="downloadTemplate.htm">[下载模板]</a>
			 <input type="hidden" value="skuExcel" name="fieName" />
		</div>
		<div style="text-align:left; height:30px;">
			模板说明：请先导出完整的excel模板...
		</div>
		<div class="tc">
			<input type="button" value="上传" id= "importTemplateBtn" class="ext_btn ext_btn_submit m10">
		</div>
	</div>	
</form>
</@backend>
