<#include "/common/common.ftl"/>
<@backend title="上传商品图片"
		js=[
			'/statics/backend/js/json2.js',
            '/statics/qiniu/js/plupload/plupload.full.min.js',
            '/statics/qiniu/js/plupload/plupload.dev.js',
            '/statics/qiniu/js/plupload/moxie.js',
            '/statics/qiniu/js/plupload/moxie.js',
            '/statics/qiniu/src/qiniu.js',
            '/statics/qiniu/js/highlight/highlight.js',
            '/statics/qiniu/js/ui.js',
            '/statics/qiniu/xgUplod.js',
	        '/statics/backend/js/layer/layer.min.js',
			'/statics/backend/js/app/skin/skin_upload.js'
		]
		css=[]>
	<form id="uploadPicture" method="post" enctype="multipart/form-data">
		<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
			<tr>
				<td class="td_right" width="120" align="right">
                    <div class="main_left m10 span6">
                        <div id="container" style="position: relative; border-left-width: 100px; padding-left: 150px;">
                            <a class="btn btn-default btn-lg " id="pickfiles"   href="#" imagenameattribute="logo">
                                <i class="glyphicon glyphicon-plus"></i>
                                <span>选择文件</span>
                            </a>
                        </div>
                    </div>

				</td>
				</tr>
			<tr>
				<td  width="80%" align="left">
					<span id="spanButtonPic" style="margin-left:20px;"></span>
					<br/>
					<span style="margin-left:20px;">可以上传 jpg;jpeg;png;类型图片，最大1 MB 第一张默认为主图</span>
					<div class="fieldset flash" style="display:none" id="picUploadProgress">
						<span class="legend"></span>
					</div>
					<input type="hidden" id="control" value="${controlName}" />
                    <input type="hidden" id="bucketname" name="bucketname" value="${bucketname}">
                    <input type="hidden" id="bucketURL" name="bucketURL" value="${bucketURL}">
				</td>
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