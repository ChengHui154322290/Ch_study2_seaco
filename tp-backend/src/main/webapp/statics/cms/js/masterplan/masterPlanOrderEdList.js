var PFUNC ={};
/**
 * 页面事件注册
 */
$(function(){
	
	PFUNC.initAdvertiseRegion =function(){
		$("#cancelMasterRule").on("click", function() {
			history.go(-1);
		});
	};
	
});

//页面初始化
$(document).ready(function()
{
	PFUNC.initAdvertiseRegion.call(this); 
	
});	