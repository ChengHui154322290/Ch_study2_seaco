var PFUNC ={};
var PCACHE  ={};
var layerpopreceive;
/**
 * 页面事件注册
 */
$(function(){
	
	PFUNC.retpar = function() {
		$('#returnPage').unbind().bind('click',function(){
			//history.go(-1);
			//goPageGet.call(this,domain +"/cmsAdvertIndex/listAdvertiseTemp.htm"); 
			
			//这个是把查询条件带过去
			var feedbackInfo={}; 
			feedbackInfo.userIdBak = $('#userIdBak').val();
			feedbackInfo.userNameBak = $('#userNameBak').val();
   	        window.location.href=domain+"/cms/listFeedback.htm?feedbackInfo="+$.toJSON(feedbackInfo);  
   	        
		});
	};
	
});


//页面初始化
$(document).ready(function()
{
	// 返回事件
	PFUNC.retpar.call(this);
	
});	