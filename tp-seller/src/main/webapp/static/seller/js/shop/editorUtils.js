
var mobileEditor;

mobileEditor = KindEditor.create("#phoneContentEditor",{ 
	height: "600px",
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
		if(data.message != null && data.message !="undefined"){
			alert(data.message);
		}
    }
});

