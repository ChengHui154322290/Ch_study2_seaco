var UPLOAD_PC_CONTENT = domain + "/uploadFile.htm?bucketname=";
var UPLOAD_PHONE_CONTENT = domain + "/uploadFile.htm?bucketname=";
var pcEditor;
var phoneEditor


KindEditor.ready(function(K) {
	bucketname= $(":hidden[name=bucketname]").val();
	UPLOAD_PC_CONTENT = UPLOAD_PC_CONTENT + bucketname;
	UPLOAD_PHONE_CONTENT = UPLOAD_PHONE_CONTENT + bucketname;
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
			filterMode : false,
			uploadJson : UPLOAD_PC_CONTENT,
			allowFileManager : false,
			afterChange : function() {
				this.sync();
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
