<#include "/common/common.ftl"/> 

<@backend title="" 
	js=[
	'/statics/backend/js/jquery.tools.js',
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/tab.js',
	'/statics/backend/js/form.js',
	'/statics/backend/js/json2.js',
	'/statics/backend/js/item/item.js',
	'/statics/backend/js/item/item-category.js',
	'/statics/select2/js/select2.js',
	'/statics/select2/js/select2Util.js',
	'/statics/select2/js/select2_locale_zh-CN.js',
	'/statics/backend/js/item/item-select2.js'
	] 
	css=[
	'/statics/backend/css/style.css',
	'/statics/select2/css/select2.css',
	'/statics/select2/css/common.css'
	 ] >
<!--
-->
<form id="inputForm" action="save.htm" method="post" enctype="multipart/form-data">
<#include "/item/subpages/add_spu.ftl">
<#include "/item/subpages/add_prdid.ftl">
		<div class="tc">
			<#if item.info.id?exists>
				<input type="button" value="返回" onclick="location.href='list.htm'" 	class="ext_btn m10" />
			<#else>
				<input type="button" value="保存" id= "dataFormSave" class="ext_btn ext_btn_submit m10" />
				<input type="button" value="返回" onclick="location.href='list.htm'" 	class="ext_btn m10" />	
			</#if>
			
		</div>
</form>
</@backend>
