var pageii;
// 弹出 属性项编辑窗口.
$(function() {
	$('.attValueAddBtn').live('click', function() {
		var id=$(this).attr('param');
		pageii = $.layer({
			type : 2,
			title : '新增属性项',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '500px', 200 ],
			iframe : {
				src : domain + '/basedata/attributeValue/addAttrValue.htm?id=' + id
			}
		});
	});
	
	//取消按钮
	$('.closebtn').on('click',function(){
		parent.window.location.reload();
		parent.layer.close(parent.pageii);
	});
	
});

function saveAttrValue(button) {
	var address = $(button).attr('param');

	$.ajax({
		cache : true,
		type : "POST",
		url : domain + address,
		data : $('#cataValueAdd').serialize(),
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if(data.success){
	            layer.alert("保存成功", 1, function(){
	        	parent.window.location.reload();
	        	parent.layer.close(parent.pageii);
	            })
	        	}else{
	        		layer.alert(data.msg.message, 8);		
	        	}
		}
	});
}


 function saveEditAttValue(button) {
	 var address = $(button).attr('param');
		$.ajax({
			cache : true,
			type : "POST",
			url : domain + address,
			data : $('#cataValueEdit').serialize(),
			async : false,
			error : function(request) {
				alert("Connection error");
			},
			success : function(data) {
				if(data.success){
		            layer.alert("保存成功", 1, function(){
		        	parent.window.location.reload();
		        	parent.layer.close(parent.pageii);
		            })
		        	}else{
		        		layer.alert(data.msg.message, 8);		
		        	}
			}
		});
 }