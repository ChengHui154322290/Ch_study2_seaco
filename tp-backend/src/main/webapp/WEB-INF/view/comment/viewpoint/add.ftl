<#include "/common/common.ftl"/> 
<@backend title="" 
	js=[
	'/statics/backend/js/jquery.tools.js',
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/editor/kindeditor-all.js',
	'/statics/backend/js/editorUtil.js', 
	'/statics/backend/js/tab.js',
	'/statics/backend/js/form.js',
	'/statics/backend/js/util.js',
	'/statics/js/jquery.cookie.js',
	"/statics/backend/js/swfupload/swfupload.js",
	"/statics/backend/js/swfupload/js/fileprogress.js",
	"/statics/backend/js/swfupload/js/handlers.js",
	"/statics/backend/js/swfupload/js/swfupload.queue.js",
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
	'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
	"/statics/backend/js/comment/viewpoint/viewpoint.js"
	] 
	css=[
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css' ] >
	<style type="text/css">
		.ke-dialog{
			top:15%;
		}
	</style>
	<form action="save.htm" id="viewpointForm">
	<input type="hidden" name="id" value="${view.id}"  />
	<div class="box_border">
		<div class="box_top">
			<b class="pl15">查看/修改观点信息</b>
		</div>
			<!-- 基础信息  -->
		<div class="box_center">
			<table class="input commContent">
				<tr>
					<td class="td_right"><span class="requiredField">*</span>序号：</td>
					<td class="">
					<input type="text" name="sort" id="sort" class="input-text lh30" size="20" maxlength=8  value="${view.sort}"  />
					</td>
				</tr>
				
				<tr>
					<td class="td_right"><span class="requiredField">*</span>是否隐藏：</td>
					<td class="">
					    <input type="radio" name="hideSign" value="0" <#if view.hideSign =="0">checked </#if>  />不隐藏
						<input type="radio" name="hideSign" value="1" <#if view.hideSign =="1">checked </#if> />隐藏
					</td>
				</tr>
				
				<tr>
					<td class="td_right"><span class="requiredField">*</span>置顶/置底：</td>
					<td class="">
						<input type="radio" name="stickSign" value="1"  <#if view.stickSign =="1">checked</#if> />默认
						<input type="radio" name="stickSign" value="2"  <#if view.stickSign =="2">checked</#if> />置顶
						<input type="radio" name="stickSign" value="0"  <#if view.stickSign =="0">checked</#if> />置底 
					</td>
				</tr>
				
				<tr>
					<td class="td_right"><span class="requiredField">*</span>条形码：</td>
					<td class="">
						<input type="text" name="barcode" id="barcode" value="${view.barcode}" class="input-text lh30" size="20" maxlength=20  />
						<input type="button" value="查询" id= "queryPrdByBarcodeBtn" class="ext_btn ext_btn_submit m10" />
					</td>
				</tr>
				
				<tr>
					<td class="td_right">SPU：</td>
					<td class="">
						<input type="text" name="spu" id="spu" class="input-text lh30"  readonly = readonly  value="${view.spu}"  />
					</td>
				</tr>
				<tr>
					<td class="td_right">商品名称：</td>
					<td class="">
						<input type="text" name="itemTitle" id="itemTitle" class="input-text lh30"  readonly = readonly size="100" value="${view.itemTitle}" />
					</td>
				</tr>
				
				<tr>
					<td class="td_right"><span class="requiredField">*</span>会员账号：</td>
					<td class="">
						<input type="text" id="memLoginName" name="memLoginName" class="input-text lh30" size="20" maxlength=20  value="${view.memLoginName}" />
						<input type="button" value="查询" id= "queryUserByAccountBtn" class="ext_btn ext_btn_submit m10" />
					</td>
				</tr>
				
				<tr>
					<td class="td_right">会员姓名：</td>
					<td class="">
						<input type="text" name="memNickName" id="memNickName" class="input-text lh30"  readonly = readonly value="${view.memNickName}"   />
						<input type="hidden" name="userId" id="userId" value="${view.userId}"  />
					</td>
				</tr>
				<tr>
					<td class="td_right"><span class="requiredField">*</span>日期：</td>
					<td class="">
					<input type="text" name="viewpointTime"  id="viewpointTime" value ='<#if view.viewpointTime??>${view.viewpointTime?string("yyyy-MM-dd HH:mm:ss")}</#if>'  class="_dateField input-text lh30" size="20"   />
					</td>
				</tr>
				
				<tr>
					<td class="td_right"><span class="requiredField">*</span>分值：</td>
					<td class="">
					<input type="text" name="score" id="score"  class="input-text lh30" size="20" value="${view.score}"  maxlength=1  onMouseOver="this.title=this.value"   />
					</td>
				</tr>
				
				<tr>
					<td class="td_right"><span class="requiredField">*</span>观点内容：</td>
					<td class="">
						<textarea id="editor" name="content" class="editor" style="width: 100%;">${view.content}</textarea>
					</td>
				</tr>
				
			</table>
		</div>
		<div class="tc">
			<input type="button" value="保存" id= "dataFormSave" class="ext_btn ext_btn_submit m10" />
			<input type="button" value="返回" onclick="location.href='list.htm'" 	class="ext_btn m10" />
		</div>
	  <div>	
	</form>
</@backend>