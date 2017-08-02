var UPLOAD_PC_CONTENT = domain + "/uploadFile.htm?bucketname=cmsimg";
var UPLOAD_PHONE_CONTENT = domain + "/cms/upload/phoneContent.htm";
var pcEditor;
var phoneEditor;

KindEditor.ready(function(K) {
	pcEditor = K.create("#pcContentEditor", {
			height : "300px",
			width : "100%",
			items : [ "source", "|", "justifyleft", "justifycenter",
					"justifyright", "justifyfull", "insertorderedlist",
					"insertunorderedlist", "fontsize", "forecolor",
					"hilitecolor", "bold", "italic", "underline",
					"strikethrough", "|", "image", "table", "|",
					"fullscreen" ],
			langType : "zh_CN",
			fieldName: "file",
			filterMode : false,
			uploadJson : UPLOAD_PC_CONTENT,
			allowFileManager : false,
			afterChange : function() {
				this.sync();
			},
			afterUpload: function(a,b,c,d){
				
			}
		});
	phoneEditor = K.create("#phoneContentEditor", {
		height : "300px",
		width : "100%",
		items : [ "source", "|", "justifyleft", "justifycenter",
				"justifyright", "justifyfull", "insertorderedlist",
				"insertunorderedlist", "fontsize", "forecolor",
				"hilitecolor", "bold", "italic", "underline",
				"strikethrough", "|", "image", "table", "|",
				"fullscreen" ],
		langType : "zh_CN",
		filterMode : false,
		uploadJson : UPLOAD_PHONE_CONTENT,
		allowFileManager : false,
		afterChange : function() {
			this.sync();
		}
	});
})
