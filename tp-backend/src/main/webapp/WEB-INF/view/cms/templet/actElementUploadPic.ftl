<#include "/common/common.ftl"/> 
<@backend title="上传图片" 
		js=[
		'/statics/cms/js/common.js',
		'/statics/backend/js/json2.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
		'/statics/cms/js/common/time/js/jquery-ui-1.9.2.custom.js',
        '/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/backend/js/swfupload/swfupload.js',
		'/statics/backend/js/swfupload/js/fileprogress.js',
		'/statics/backend/js/swfupload/js/handlers.js',
		'/statics/backend/js/swfupload/js/swfupload.queue.js',
		'/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
		'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
		'/statics/cms/js/jquery/jquery.ui.core.js',
		'/statics/cms/js/jquery/jquery.form.js',
		
		'/statics/cms/js/common/upload_actElement_img.js',
		'/statics/backend/js/promotion/utils.js'
			]
		css=['/statics/backend/css/style.css',
    	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css',
    	'/statics/cms/js/common/time/css/jquery-ui-timepicker-addon.css',
    	'/statics/cms/js/common/time/css/jquery-ui-1.8.17.custom.css']>
	<form id="uploadItemPicture" method="post" enctype="multipart/form-data">
		<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
			<tr>
				<td class="td_right" width="120" align="right">选择上传图片:</td>
				<td  width="80%" align="left">
					<span id="spanButtonItemPic" style="margin-left:20px;"></span>
					<br/>
					<span style="margin-left:20px;">可以上传 jpg;jpeg;png;类型图片</span>
					<div class="fieldset flash" style="display:none" id="itemPicUploadProgress">
						<span class="legend"></span>
					</div>
				</td>
				
				<input type="hidden" value="${sessionId}" id="sessionId" />
				
			</tr>
			<tr>
				<td colspan="2" align="center">
					<div style="padding-right:10px">
						<input type="button" class="btn btn82 btn_del closebtn" id="close" value="关闭" style="margin-top: 10px;" />
					</div>
				</td>
			</tr>
		</table>
	</form>
</@backend>