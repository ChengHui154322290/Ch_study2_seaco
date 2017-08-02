var pageii;
$(function(){
	$('.editAttributeValue').live('click',function(){
		var attId=$(this).attr('param');
		var cateId=$(this).attr('param1');
		pageii= $.layer({
            type : 2,
            title: '新增/更新 属性值',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['600px', 400],                     
            iframe: {
                src : domain+'/basedata/attributeValue/edit.htm?attId='+attId+"&cateId="+cateId
            } 
        });
	});
	
	$('.deleteAttrCateLinked').live('click',function(){
		var attrId=$(this).attr('param');
		var cateId=$(this).attr('param1');
		layer.confirm('您确认移除么?', function(){
			$.ajax({
		        cache: true,
		        type: "get",
		        url:domain+ '/basedata/category/deleteCateAttrLinked.htm?attrId='+attrId+"&cateId="+cateId,
		        async: false,
		        error: function(request) {
		            alert("Connection error");
		        },
		        success: function(data) {
		        	if(data.success){
		                layer.alert("删除成功", 1, function(){
		            	window.location.reload();
		            	layer.close(parent.pageii);
		                })
		            	}else{
		            		layer.alert(data.msg.message, 8);		
		            	}
		        }
		    });
			
			
		 },function(){
		 	
		});

	});
	
	
	
	
	
	$('.selectMore').live('click',function(){
		var cateId=$(this).attr('param');
		pageii =$.layer({
	        type : 2,
	        title: '商品属性群',
	        shadeClose: true,
	        maxmin: true,
	        fix : false,  
	        area: ['500px', 600],                     
	        iframe: {
	            src : domain+'/basedata/attribute/view.htm?cateId='+cateId
	        } 
	    });
	});
	
	$('.closebtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
});



function deleteAttrCateLinked(attra){
	var address= $(attra).attr('param');
	layer.confirm('您确认删除么?', function(){
		$.ajax({
	        cache: true,
	        type: "get",
	        url:domain+address,
	        async: false,
	        error: function(request) {
	            alert("Connection error");
	        },
	        success: function(data) {
	        	if(data.resultInfo.type=="1"){
	                layer.alert(data.resultInfo.message, 1, function(){
	            	window.location.reload();
	            	layer.close(parent.pageii);
	                })
	            	}else{
	            		layer.alert(data.resultInfo.message, 8);		
	            	}
	        }
	    });
		
		
	 },function(){
	 	
	});
	
}


