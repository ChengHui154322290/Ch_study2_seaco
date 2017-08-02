 var swfu;
 
var fileCountlimit = 10;
var hasUploadCount = 0;
window.onload = function() {
	$('#item-pictures').find('.item-picture').each(function(i,n){
		$(n).children('.remove-btn').click(function(){
			$(n).remove();
			fileCountlimit++;
			swfu.setFileUploadLimit(fileCountlimit);
		});
		$(n).children('.pre-btn').click(function(){
			$(n).insertBefore($(n).prev());
		});
		$(n).children('.next-btn').click(function(){
			$(n).insertAfter($(n).next());
		});
		$(n).hover(function(){
			$(this).find('.remove-btn').fadeIn();
			$(this).find('.pre-btn').fadeIn();
			$(this).find('.next-btn').fadeIn();
		},function(){
			$(this).find('.remove-btn').fadeOut();
			$(this).find('.pre-btn').fadeOut();
			$(this).find('.next-btn').fadeOut();
		});
		hasUploadCount++;
	});

	
	var sid = $('#sessionId').val();
	var settings = {
		flash_url : domain+"/statics/backend/js/swfupload/swfupload.swf",
		upload_url: domain+"/item/uploadItemFiles.htm?sid="+sid,
		file_size_limit : "10 MB",
		file_types : "*.jpg;*.jpeg;*.png;*.bmp",
		file_types_description : "图片 最大1MB",
		file_upload_limit:fileCountlimit,
		file_queue_limit : 0,
		custom_settings : {
			progressTarget : "fsUploadProgress"
		},
		debug: false,
		post_params:{'sessionId':sid},
		// Button settings
		button_image_url: domain+"/statics/backend/js/swfupload/images/btn.png",
		button_width: "100",
		button_height: "30",
		button_placeholder_id: "spanButtonPlaceHolder",
		button_text: '<span class="theFont">上传图片</span>',
		button_text_style: ".theFont { font-size: 17;color:#FFFFFF;font-weight:bold;margin-top:10px }",
		button_text_left_padding: 12,
		button_text_top_padding: 3,
		
		// The event handler functions are defined in handlers.js
		swfupload_loaded_handler:function(){
			var stats = swfu.getStats();
			stats.successful_uploads = hasUploadCount;
			swfu.setStats(stats);
		},
		file_queued_handler : fileQueued,
		file_queue_error_handler : fileQueueError,
		file_dialog_complete_handler : fileDialogComplete,
		upload_start_handler : uploadStart,
		upload_progress_handler : uploadProgress,
		upload_error_handler : uploadError,
		upload_success_handler : function(file, serverData){
			try {
				var progress = new FileProgress(file, this.customSettings.progressTarget);
				progress.setComplete();
				progress.setStatus("上传完成.");
				progress.toggleCancel(false);

				if (!serverData || serverData.length == 0) {
				} else {
					var result = eval("("+serverData+")");
					if(result.type&&result.type=="error"){
						alert("图片上传失败");
						//加1
						fileCountlimit++;
						swfu.setFileUploadLimit(fileCountlimit);
						return;
					}
					
					var path = result.path;	//路径
					var key = result.key;	//key
					var allItems = $('#item-pictures');
					var img = $("<img />");
					setTimeout(function(){img.attr('src',path);}, 1000);
					
					
					var container = $("<div />");
					container.addClass("item-picture");
					container.append(img);
					container.hover(function(){
						container.find('.remove-btn').fadeIn();
						container.find('.pre-btn').fadeIn();
						container.find('.next-btn').fadeIn();
					},function(){
						container.find('.remove-btn').fadeOut();
						container.find('.pre-btn').fadeOut();
						container.find('.next-btn').fadeOut();
					});
					
					var input = $("<input />");
					input.val(key);
					input.attr('name','picList');
					container.append(input);
					
					var deleteBtn = $("<span />");
					deleteBtn.html("删除");
					deleteBtn.attr('title','删除');
					deleteBtn.addClass("remove-btn");
					container.append(deleteBtn);
					deleteBtn.click(function(){
						container.remove();
						fileCountlimit++;
						swfu.setFileUploadLimit(fileCountlimit);
					});
					
					var pre = $("<span />");
					pre.html("前移");
					pre.attr('title','前移');
					pre.addClass('pre-btn');
					container.append(pre);
					pre.click(function(){
						$(container).insertBefore($(container).prev());
					});
					
					var next = $("<span />");
					next.html("后移");
					next.attr('title','后移');
					next.addClass('next-btn');
					container.append(next);
					next.click(function(){
						$(container).insertAfter($(container).next());
					});
					
					allItems.append(container);
				}
			} catch (ex) {
				this.debug(ex);
			}
		},
		upload_complete_handler : uploadComplete,
		queue_complete_handler : queueComplete	// Queue plugin event
	};

	swfu = new SWFUpload(settings);
 };