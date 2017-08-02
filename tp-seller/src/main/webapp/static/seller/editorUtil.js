var backendDomain =domain;

var setting = {
				priceScale: "2",
				priceRoundType: "roundHalfUp",
				currencySign: "￥",
				currencyUnit: "元",
				uploadImageExtension: "",
				uploadFlashExtension: "",
				uploadMediaExtension: "",
				uploadFileExtension: ""
		};
		
	var sid = $('#sessionId').val();
		editor = KindEditor.create("#editor", {
			height: "300px",
			items: [
					"source","|", "justifyleft", "justifycenter", "justifyright",
					"justifyfull", "insertorderedlist", "insertunorderedlist", 
					 "fontsize", "forecolor", "hilitecolor", "bold",
					"italic", "underline", "strikethrough", "|", "image","multiimage","table","|","link","unlink","|", "fullscreen"
				],
			langType: "zh_CN",
			filterMode: false,
			uploadJson :domain+'/uploadFile.htm?bucketname=item',
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
			
			mobileEditor = KindEditor.create("#mobileEditor",{ 
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
					if(data.message != null && data.message !="undefined"){
						alert(data.message);
					}
		        }
		});
			
		
		$("table.mobiletable").hide();
		
		$("#pcEditorTabBtn").bind('click',function(){
			$("table.pctable").show();
			$("table.mobiletable").hide();
		});
		$("#mobileEditorTabBtn").bind('click',function(){
			$("table.pctable").hide();
			$("table.mobiletable").show();  
		});
		
			
			
	