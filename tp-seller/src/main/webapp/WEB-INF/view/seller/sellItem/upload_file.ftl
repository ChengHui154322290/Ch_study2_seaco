<#include "/layout/inner_layout.ftl" />
<script type="text/javascript" charset="utf-8" src="/static/scripts/common/jquery-1.9.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/common/jquery.form.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/component/jqueryui/js/jquery-ui-1.9.2.custom.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/common/bootstrap.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/web/main.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/component/date/WdatePicker.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/web/common/common.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/web/common/validator.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/layer/layer.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/layer/extend/layer.ext.js"></script>

<script type="text/javascript" charset="utf-8" src="/static/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/typeahead-bs2.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/jquery.ui.touch-punch.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/jquery.slimscroll.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/jquery.easy-pie-chart.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/jquery.sparkline.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/flot/jquery.flot.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/flot/jquery.flot.pie.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/flot/jquery.flot.resize.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/ace-elements.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/ace.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/ace-extra.min.js"></script>
<@sellContent title="" 
	js=['/static/seller/js/item/upload-file.js'] 
	css=['/static/seller/css/common.css',
	'/static/seller/css/style.css',
	'/static/seller/css/main.css',
	'/seller/css/main.css'] >
<form id="inputForm" action="uploadSkuExcel.htm" method="post" enctype="multipart/form-data">
	<div style="height:30px;"></div>
	<div style="border:1px solid #ccc ;">
		<div style="text-align:left; height:30px;">
			上传文件：<input type="file" value="上传" id= "skuExcel" name="skuExcel" >
		</div>
		<div style="text-align:left;height:30px;">
			导入模板：
			 <a id="downTemplateBtn" href="#">[下载模板]</a>
			 <input type="hidden" value="skuExcel" name="fieName" />
			  <!--来源于供应商-->
			  <input type="hidden" value="supplier" name="importFrom" />
		</div>
		<div style="text-align:left; height:30px;">
			模板说明：请先导出完整的excel模板...
		</div>
		<div class="tc">
			<input type="button" value="上传"  class="ext_btn ext_btn_submit m10 dataFormSave">
			<input type="button" value="返回" class="ext_btn m10" id="closeBtn">
		</div>
	</div>	
</form>
</@sellContent>
