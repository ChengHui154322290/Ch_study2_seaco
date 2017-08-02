$(function(){
	$("#syncButton").click(function(){
		layer.confirm('您确认和微信同步么,可能会时间较长?', function(){
			$.ajax({
				url:domain + '/mkt/channel/sync.htm',
//				url:domain + '/mkt/channel/syncNew.htm',
				type:'get',
				dataType:'json',
				success:function(result){
					if(result.success){
						layer.alert("同步成功", 1,function(){
		        			window.location.reload();
		        			/*layer.close();
							current_btn.attr('versionStatus',status);
							current_btn.attr('value',statusOpert);*/
		                })
					}else{
		        		layer.alert(result.msg.message, 8);
		        	}
				}
			});
		},function(){});
	});
	
//	$("#syncButton2").click(function(){
//		layer.confirm('您确认和微信同步么,可能会时间较长?', function(){
//			$.ajax({
////				url:domain + '/mkt/channel/sync.htm',
//				url:domain + '/mkt/channel/syncNew.htm?choise='+'1',
//				type:'get',
//				dataType:'json',
//				success:function(result){
//					if(result.success){
//						layer.alert("同步成功", 1,function(){
//		        			window.location.reload();
//		        			/*layer.close();
//							current_btn.attr('versionStatus',status);
//							current_btn.attr('value',statusOpert);*/
//		                })
//					}else{
//		        		layer.alert(result.msg.message, 8);
//		        	}
//				}
//			});
//		},function(){});
//	});
	
});