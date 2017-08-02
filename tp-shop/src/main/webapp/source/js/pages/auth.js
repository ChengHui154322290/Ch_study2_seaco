/**
 * 
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-12 17:16:47
 * @version Ver 1.0.0
 */
var backpage;
var checkidphoto = xigou.getSessionStorage("checkidphoto");
$(function() {
	setbackpage();
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				auth_query();
			}else{
				window.location.href="logon.html";
			}
		}	
	});
});

var $input_align_change = {
	_focus : function(){
		$('#'+this.id).removeClass("text-right");
    		$('#'+this.id).addClass("text-left");
	},
	_blur : function(){
		$('#'+this.id).removeClass("text-left");
    		$('#'+this.id).addClass("text-right");
	},
	
	_init:function(id){
		this.id=id;
		$('#'+this.id).focus(this._focus);
		$('#'+this.id).blur(this._blur);
	}
}

//是否实名认证
var isrealname=false;
//用户-实名认证
function authentication(){
	if($(".auth-name-input").val().trim() == "" ){
		$.tips({
			content:'请填写姓名',
			stayTime:2000,
			type:"warn"
		})

		return;
	}
	if($(".auth-num-input").val().trim() == ""){
		$.tips({
			content:'请填写身份证号码',
			stayTime:2000,
			type:"warn"
		})

		return;
	}
	//if(!$("#auth-photo-left-img").attr('data-src') ||!$("#auth-photo-right-img").attr('data-src')){
	//	$.tips({
	//		content:'请上传身份证照片',
	//		stayTime:2000,
	//		type:"warn"
	//	})
    //
	//	return;
	//}

	if($(".auth-name-input").val()&&$(".auth-num-input").val()
		//&& $("#auth-photo-left-img").attr('data-src') && $("#auth-photo-right-img").attr('data-src')
	)
	{
		var params = {
			'token': xigou.getToken(),
			'name': $(".auth-name-input").val().trim(), //名字
			'code': $(".auth-num-input").val().trim(), //身份证号
			'imgfront': $("#auth-photo-left-img").attr('data-src'),//xigou.getSessionStorage("certification_zheng"), //身份证正面图片数据
			'imgback': $("#auth-photo-right-img").attr('data-src')//xigou.getSessionStorage("certification_fan") //身份证反面图片数据
		};

		xigou.activeUser.authentication({
			requestBody: params,
			callback: function(response, status) { //回调函数
				if (status == xigou.dictionary.success) {
					if (null == response) {
						$.tips({
				            content:xigou.dictionary.chineseTips.server.value_is_null,
				            stayTime:2000,
				            type:"warn"
				         })
						// xigou.alert(xigou.dictionary.chineseTips.server.value_is_null);
					} else {
						switch (response.code) {
							case "0":
								isrealname=true;
								xigou.setSessionStorage("clearing_ht_idNumber", $(".auth-num-input").val().trim());
								xigou.setSessionStorage("clearing_ht_realname", $(".auth-name-input").val().trim());

								var dia=$.dialog({
                                    title:'',
                                    content:response.msg||"实名认证成功",
                                    button:["确认"]
                                });

                                dia.on("dialog:action",function(e){
                                	location.reload();
                                });
								break;
							default:
								$.tips({
						            content:response.msg||"实名认证失败",
						            stayTime:2000,
						            type:"warn"
						         })
								break;
						}
					}
				} else {
					$.tips({
						content:'请求失败，详见' + response,
						stayTime:2000,
						type:"warn"
					})
				}
			}
		});
	}
};

function uploadImage(img_id){
		var dataURL_face = "iVBORw0KGgoAAAANSUhEUgAAAaQAAAE7CAIAAAAdHmL0AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDIxIDc5LjE1NTc3MiwgMjAxNC8wMS8xMy0xOTo0NDowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NzE1ODI0MDcyOTVDMTFFNUJFN0NGODA0ODY5MUIxQzciIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NzE1ODI0MDgyOTVDMTFFNUJFN0NGODA0ODY5MUIxQzciPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo3MTU4MjQwNTI5NUMxMUU1QkU3Q0Y4MDQ4NjkxQjFDNyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3MTU4MjQwNjI5NUMxMUU1QkU3Q0Y4MDQ4NjkxQjFDNyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PlhWhzYAAAr+SURBVHja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";
		var dataURL_back = "iVBORw0KGgoAAAANSUhEUgAAAaQAAAE7CAIAAAAdHmL0AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDIxIDc5LjE1NTc3MiwgMjAxNC8wMS8xMy0xOTo0NDowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NzE1ODI0MDMyOTVDMTFFNUJFN0NGODA0ODY5MUIxQzciIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NzE1ODI0MDQyOTVDMTFFNUJFN0NGODA0ODY5MUIxQzciPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo3MTU4MjQwMTI5NUMxMUU1QkU3Q0Y4MDQ4NjkxQjFDNyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3MTU4MjQwMjI5NUMxMUU1QkU3Q0Y4MDQ4NjkxQjFDNyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PrdiYTMAAAu0SURBVHja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";
		//&&xigou.getSessionStorage("certification_fan")&&xigou.getSessionStorage("certification_zheng")
		var imgStr = "";
		if(img_id == "auth-photo-right-img") {
			imgStr = xigou.getSessionStorage("certification_fan") || dataURL_back;
		}else {
			imgStr = xigou.getSessionStorage("certification_zheng") || dataURL_face;
		}

		var params = {
			'token': xigou.getToken(),
            //'numhide':$(".auth-num-input").html().trim().replace($(".auth-num-input").html().trim().substring(6,14),"******"),//身份证号隐藏
			//'image': dataURL_face// xigou.getSessionStorage("certification_zheng"), //身份证正面图片数据
			'image': imgStr, //身份证正面图片数据
		};

		xigou.activeUser.uploadimage({
			requestBody: params,
			callback: function(response, status) { //回调函数
				if (status == xigou.dictionary.success) {
					if (null == response) {
						$.tips({
							content:xigou.dictionary.chineseTips.server.value_is_null,
							stayTime:2000,
							type:"warn"
						})
					} else {
						//xigou.setSessionStorage("image1",response.data.imagestr);
						$("#"+img_id).attr("data-src",response.data.imagestr);
					}
				} else {
					$.tips({
							content:'请求失败，详见' + response,
							stayTime:2000,
							type:"warn"
					})
				}
			}
		});
};

//用户-实名认证-查询
function auth_query(){
	var params = {
		'token': xigou.getToken()
	};
	xigou.activeUser.auth_query({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				if (null == response) {
					$.tips({
				        content:xigou.dictionary.chineseTips.server.value_is_null,
				        stayTime:2000,
				        type:"warn"
				    })
					// xigou.alert(xigou.dictionary.chineseTips.server.value_is_null);
				} else {
					switch (response.code) {
						case "0":
							$('#auth-summit')[xigou.events.click](function(){
								authentication();
							});
							if(typeof(response.data) != "undefined" && response.data != "")
							{
								isrealname=true;//已实名
								//实认编辑修改
								$(".auth-name-input").val(response.data.name);
								$(".auth-num-input").val(response.data.code);
								$input_align_change._init('name');
								$input_align_change._init('num');

								$('.button').removeClass('hide');
								if(!backpage || checkidphoto == 1) {
									uploadimageinit();//上传初始化
								}
								xigou.setSessionStorage("realname",response.data.name);
								xigou.setSessionStorage("realnum",response.data.code);
								if(response.data.imgfront){
									$("#auth-photo-left-img").attr("src",response.data.imgfront);
									$("#auth-photo-left-img").attr("data-src",response.data.imgfront.split(".com/")[1]);
								}
								if(response.data.imgback){
									$("#auth-photo-right-img").attr("src",response.data.imgback);
									$("#auth-photo-right-img").attr("data-src",response.data.imgback.split(".com/")[1]);
								}
							}
							break;
						default:
							isrealname=false;
							$(".auth-name-input").val("");
							$(".auth-num-input").val("");
							$input_align_change._init('name');
							$input_align_change._init('num');
							$('.button').removeClass('hide');
							$('#auth-summit')[xigou.events.click](function(){
								authentication();
							});
							uploadimageinit();//上传初始化
							break;
					}
				}
			} else {
				$.tips({
				    content:'请求失败，详见' + response,
				    stayTime:2000,
				    type:"warn"
				})
				// xigou.alert('请求失败，详见' + response);
			}
		}
	});
}



function uploadimageinit(){
	//不包含海外直邮商品;用户未实名认证
	if(backpage && checkidphoto == 0) {
		$("#real_photo").hide();
		uploadImage("auth-photo-left-img");
		uploadImage("auth-photo-right-img");
	}
	//document.getElementById('fileImg').addEventListener('change', handleFileSelect, false);

	$("#btn_zheng")[xigou.events.click](function(){
		$("#auth-photo-left-id")[0].click();
	});
	$("#auth-photo-left-id").change(function(){
		handleFileSelect(event,"auth-photo-left-img","certification_zheng");
		//xigou.uploadImage($("#auth-photo-left-id")[0].files,function(base64str){
		//	$("#auth-photo-left-img")[0].src=base64str;
		//	xigou.setSessionStorage("certification_zheng",base64str.split(",")[1]);
		//	//authentication();
		//})
	});
	
	$("#btn_fan")[xigou.events.click](function(){
		$("#auth-photo-right-id")[0].click();
	});
	$("#auth-photo-right-id").change(function(){
		handleFileSelect(event,"auth-photo-right-img","certification_fan");
		//xigou.uploadImage($("#auth-photo-right-id")[0].files,function(base64str){
		//	$("#auth-photo-right-img")[0].src=base64str;
		//	xigou.setSessionStorage("certification_fan",base64str.split(",")[1]);
		//	//authentication();
		//})
	});
}


function handleFileSelect (evt,elm,site) {
	var files = evt.target.files;
	for (var i = 0, f; f = files[i]; i++) {

		// Only process image files.
		if (!f.type.match('image.*')) {
			continue;
		}

		var reader = new FileReader();

		// Closure to capture the file information.
		reader.onload = (function(theFile) {
			return function(e) {
				// Render thumbnail.
				// console.log(evt.target.files[0]);
				// console.log(e.target);
				var i = document.getElementById(elm);
				i.src = e.target.result;
				// var prop;
				// if($(i).width()>$(i).height()){
				// 	prop = 960/$(i).width();
				// 	var newHeight = $(i).height() * prop;
				// 	$(i).css({'width':'960px','height':newHeight});
				// }else{
				// 	prop = 960/$(i).height();
				// 	var newWidth = $(i).width() * prop;
				// 	$(i).css({'width':newWidth,'height':'960px'});
				// }
				var quality =  15;
				i.src = xigou.compress(i,quality).src;
				if(i.src.length < 30){
					i.src = this.result;
				}
				xigou.setSessionStorage(site,i.src.split(",")[1]);
				i.style.display = "block";
				var imageStr = i.src.toString().split(",")[1];
				var params = {
					"token":xigou.getToken(),
					"imgstream":imageStr
				}
				uploadImageUrl(params,elm);
			};
		})(f);


		// Read in the image file as a data URL.
		reader.readAsDataURL(f);
	}
}
var setbackpage = function(){
	backpage = xigou.getQueryString("backpage");
	if(backpage){
		$('a.back').attr('href',backpage+"?holdTime=1");
		if(checkidphoto == 0){
			$("#real_photo").hide();
		}
		
	}
};

function uploadImageUrl(params,imgId){
	xigou.activeUser.uploadimage({
		requestBody:params,
		callback:function(response,status){
			if(status==xigou.dictionary.success){
				if(response.code==0){
			var _imgUrl = response.data.path;
			$("#"+imgId).attr("data-src",_imgUrl);}
		}
			
		}
	})
}










