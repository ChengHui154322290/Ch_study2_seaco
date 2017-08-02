var UPLOAD_ITEM_IMAGE = domain + "/cmstemplet/uploadImage";
var swfuItem;
var index = parent.layer.getFrameIndex(window.name);

$(document).ready(function() {
	$("#close").on("click",function(){
		swfuItem = null;
		$("#spanButtonItemPic").remove();
		parent.layer.close(index);
	});
	createItemPicUploadControl();
})

function createItemPicUploadControl() {
	var layerShow;
	var sid = parent.$("#sessionId").val();
	var itemSettings = {
		flash_url : domain + "/statics/backend/js/swfupload/swfupload.swf",
		upload_url : UPLOAD_ITEM_IMAGE + "?sid=" + sid,
		file_size_limit : "1 MB",
		file_types : "*.jpg;*.jpeg;*.png;",
		file_types_description : "图片 最大1MB",
		file_upload_limit : 10,
		file_queue_limit : 0,
		custom_settings : {
			progressTarget : "itemPicUploadProgress"
		},
		debug : false,
		post_params:{'sessionId':sid},
		// Button settings
		button_image_url : domain
				+ "/statics/backend/js/swfupload/images/btn.png",
		button_width : "100",
		button_height : "30",
		button_placeholder_id : "spanButtonItemPic",
		button_text : '<span class="theFont">上传图片</span>',
		button_text_style : ".theFont { font-size: 17;color:#FFFFFF;font-weight:bold;margin-top:10px }",
		button_text_left_padding : 12,
		button_text_top_padding : 3,

		// The event handler functions are defined in handlers.js
		file_queued_handler : fileQueued,
		file_queue_error_handler : fileQueueError,
		file_dialog_complete_handler : fileDialogComplete,
		upload_start_handler : function(){
			layerShow = layer.load("上传中...");
		},
		upload_progress_handler : uploadProgress,
		upload_error_handler : function(file, serverData){alert("error");},
		upload_success_handler : function(file, serverData) {
			var processingLayer = layer.load("处理中...");
			setTimeout(function(){
				try {
					if (!serverData || serverData.length == 0) {
						layer.alert("图片上传出错!");
					} else {
						/*var itemIndex = $("#selectItemIndex").val();*/
						var data = JSON.parse(serverData);
						if (null != data) {
							if (null != data.file) {
								//var result = eval("(" + data + ")");
								parent.$("#picture").val(data.file);
								parent.$("#imageImg_src").attr("src",data.file_src);
							}
						}
						/*if (null != data) {
							if (null != itemIndex) {
								var row = parent.$("#topicItemsList tr")[itemIndex];
								if (null != row) {
									if (null == data.file) {
										var result = eval("(" + data + ")");
										if (null != result) {
											$(row).children().find("input[name='topicImage']").val(result.file);
											$(row).children().find("img[name='selectImage']").attr("src",result.fullPath);
										}
									} else {
										$(row).children().find("input[name='topicImage']").val(data.file);
										$(row).children().find("img[name='selectImage']").attr("src",data.fullPath);
									}
								}
							}
						}*/
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
	// Queue plugin event
	};

	swfuItem = new SWFUpload(itemSettings);
}
