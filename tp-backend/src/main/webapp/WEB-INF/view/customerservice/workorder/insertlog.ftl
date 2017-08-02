<link rel="stylesheet" type="text/css" href="${backend_folder}/js/customerservice/webUploader/css/webuploader.css">
<link rel="stylesheet" type="text/css" href="${backend_folder}/js/customerservice/webUploader/css/style.css">
<link rel="stylesheet" type="text/css" href="${backend_folder}/js/customerservice/webUploader/css/demo.css">
<script type="text/javascript">
	var webUploader = '${js}/webUploader';
	var workorder = '${base}';
</script>
<!--引入JS-->
<script src="${backend_folder}/js/customerservice/webUploader/js/webuploader.js" type="text/javascript"></script>
<script src="${backend_folder}/js/customerservice/workorderUpload/refresh-upload.js" type="text/javascript"></script>

<tr>
	<td style="text-align: left;width:100px">上传图片：</td>
	<td>
                                       图片信息： 为了帮助我们更好的解决问题，请您上传图片<br>
                                       最多可以上传5张,仅支持gif,jpg,png格式的图片，文件大小不超过2M
											<div class="wu-example" id="uploader">
												 <div class="queueList">
											        <div class="placeholder" id="dndArea">
											            <div id="filePicker" class="webuploader-container">
												            <div>+ 上传图片</div>
											            </div>
											        </div>
											        <ul style="margin-top: 10px;" class="filelist"></ul>
											    </div>
											</div>
                                        </td>
	
	
	
	
</tr>
<tr >
	<td style="text-align: left;">描述：</td>
	<td><textarea id="context" name="context" cols="50" rows="10" ></textarea></td>
</tr>
<div class="imageList"></div>
<script>
function checkUpload() {
	var cancelFiles = uploader.getFiles("cancelled");
	var completeFiles = uploader.getFiles("complete");
	var progressFiles = uploader.getFiles("progress");
	if (progressFiles.length > 0) {
		$(".imageError").html("有图片正在上传,请上传结束后再提交");
		return false;
	}
	if ($("#imgUrl").length == 0) $("<input type='hidden' id='imgUrl' name='imgUrl'/>").appendTo(".imageList");
	$("#imgUrl").val(getImageListHtmlForString(cancelFiles));
	return true;
}

function checkIsNeedUpImage() {
	var rejectReason = $("#rejectReason").val();
	if (rejectReason == $("#rejectReason > option:last").val()
			|| rejectReason == $("#rejectReason > option:last").prev().val())
		return false;
	else
		return true;
}
</script>