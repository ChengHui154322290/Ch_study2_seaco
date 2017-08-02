var pageii;
// 弹出 属性项编辑窗口.
$(function() {
	
	// 保存增加
	function saveAddAtt(button) {
		var address = $(button).attr('param');
	
		$.ajax({
			cache : true,
			type : "POST",
			url : domain + address,
			data : $('#attributeAdd').serialize(),
			async : false,
			error : function(request) {
				alert("Connection error");
			},
			success : function(data) {
				if(data.resultInfo.type=="1"){
		            layer.alert(data.resultInfo.message, 1, function(){
		        	parent.window.location.reload();
		        	parent.layer.close(parent.pageii);
		            })
		        	}else{
		        		layer.alert(data.resultInfo.message, 8);		
		        	}
			}
		});
	}
});

