var backendDomain=domain;

$(function(){
	// 添加Cookie
	function addCookie(name, value, options) {
		if (arguments.length > 1 && name != null) {
			if (options == null) {
				options = {};
			}
			if (value == null) {
				options.expires = -1;
			}
			if (typeof options.expires == "number") {
				var time = options.expires;
				var expires = options.expires = new Date();
				expires.setTime(expires.getTime() + time * 1000);
			}
			document.cookie = encodeURIComponent(String(name)) + "=" + encodeURIComponent(String(value)) + (options.expires ? "; expires=" + options.expires.toUTCString() : "") + (options.path ? "; path=" + options.path : "") + (options.domain ? "; domain=" + options.domain : ""), (options.secure ? "; secure" : "");
		}
	}

	// 获取Cookie
	function getCookie(name) {
		if (name != null) {
			var value = new RegExp("(?:^|; )" + encodeURIComponent(String(name)) + "=([^;]*)").exec(document.cookie);
			return value ? decodeURIComponent(value[1]) : null;
		}
	}

	// 移除Cookie
	function removeCookie(name, options) {
		addCookie(name, null, options);
	}
	var sessionId = $('#sessionId').val();
	// 编辑器
	KindEditor.ready(function(K) {
		editor = K.create("#editor", {
			height: "300px",
			items: [
					"source","|", "justifyleft", "justifycenter", "justifyright",
					"justifyfull", "insertorderedlist", "insertunorderedlist", 
					 "fontsize", "forecolor", "hilitecolor", "bold",
					"italic", "underline", "strikethrough", "|", "image","multiimage","table","|","link","unlink","|", "fullscreen"
				],
			langType: "zh_CN",
			filterMode: false,
			uploadJson : domain+'/uploadFile.htm?bucketname=item',
			allowFileManager: false,
			afterChange: function() {
				this.sync();
			},
			afterUpload:function (data) {  
				if(undefined!= data && data.message != undefined && data.message !="undefined"){
					alert(data.message);
				}
	        }
		});
		mobileEditor = K.create("#mobileEditor",{
			height: "300px",
			items: [
					"source","|", "justifyleft", "justifycenter", "justifyright",
					"justifyfull", "insertorderedlist", "insertunorderedlist", 
					 "fontsize", "forecolor", "hilitecolor", "bold",
					"italic", "underline", "strikethrough", "|", "image","multiimage_mobile","table","|","link","unlink","|", "fullscreen"
				],
			langType: "zh_CN",
			filterMode: false,
			uploadJson : domain+'/uploadFile.htm?bucketname=item',
			allowFileManager: true,
			afterChange: function() {
				this.sync();
			},
			afterUpload:function (data) {  
				if(undefined!= data && data.message != undefined && data.message !="undefined"){
					alert(data.message);
				}
	        }
		});
	});
});
