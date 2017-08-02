var swfu;
var UPLOAD_IMAGE = domain + "/topic/uploadImage";
var index = parent.layer.getFrameIndex(window.name);

$(document).ready(function() {
	$("#close").on("click",function(){
		parent.layer.close(index);
	});
	createPicUploadControl();
});

function createPicUploadControl() {
	var layerShow;
	var sid = $("#sid").val();
	var settings = {
		upload_url : UPLOAD_IMAGE+"?sid="+sid,
		flash_url : domain + "/statics/backend/js/swfupload/swfupload.swf",
		file_size_limit : "1 MB",
		file_types : "*.jpg;*.jpeg;*.png;",
		file_types_description : "图片 最大1MB",
		file_upload_limit : 1,
		file_queue_limit : 0,
		custom_settings : {
			progressTarget : "picUploadProgress"
		},
		debug : false,
		post_params:{'sessionId':sid},
		// Button settings
		button_image_url : domain
				+ "/statics/backend/js/swfupload/images/btn.png",
		button_width : "100",
		button_height : "30",
		button_placeholder_id : "spanButtonPic",
		button_text : '<span class="theFont">上传图片</span>',
		button_text_style : ".theFont { font-size: 17;color:#FFFFFF;font-weight:bold;margin-top:10px }",
		button_text_left_padding : 12,
		button_text_top_padding : 3,
		// The event handler functions are defined in handlers.js
		file_queued_handler : fileQueued,
		file_queue_error_handler : fileQueueError,
		file_dialog_complete_handler : fileDialogComplete,
		upload_start_handler : function() {
			layerShow = layer.load("上传中");
		},
		upload_progress_handler : uploadProgress,
		upload_error_handler : uploadError,
		upload_success_handler : function(file, serverData) {
			var processingLayer = layer.load("处理中...");
			setTimeout(function(){
				try {
					if (!serverData || serverData.length == 0) {
					} else {
						var controlName = $("#control").val();
						var controlFullName = $("#fullControl").val();
						var data = JSON.parse(serverData);
						if (null != data) {
							if (null == data.file) {
								var result = eval("(" + data + ")");
								if (null != result) {
									parent.$("#" + controlName).val(result.file); // 文件提取码
									parent.$("#" + controlFullName).attr("src", result.fullPath);// 文件全路径
								}
							} else {
								parent.$("#" + controlName).val(data.file); // 文件提取码
								parent.$("#" + controlFullName).attr("src", data.fullPath);// 文件全路径
							}
						}
						layer.close(processingLayer);
						parent.layer.close(index);
					}
				} catch (ex) {
					this.debug(ex);
				}
			},1000);
		},
		upload_complete_handler : function() {
			layer.close(layerShow);
		},
		queue_complete_handler : queueComplete
	};

	swfu = new SWFUpload(settings);
}
