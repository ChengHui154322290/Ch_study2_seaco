var PFUNC ={};
var PCACHE  ={};

/**
 * 页面事件注册
 */
$(function(){
	
	/**
	 * 出一个tab
	 */
	function supplierShowTab(id,text,tabUrl){
		var tv = {};
		tv.linkId = id+"_link";
		tv.tabId =  id;
		tv.url = tabUrl;
		tv.text = text;
		try{
			window.parent.showTab(tv);
		} catch(e){
		}
	}
	
	PFUNC.initRegion = function() {
		//需要页面刷新
		$('#rushxigouindex').unbind().bind('click',function(){
			var params={}; 
			$.post(domain +"/cmstemplet/rushXigouIndex.htm", {params:$.toJSON(params)}, function(redata){
				if(redata.isSuccess){
		            alert("刷新成功",1);
		        }else{
		        	alert("刷新失败",1);
		        }
		        }, "json");
		  });
		
		$('#subview').unbind().bind('click',function(){
			var params={}; 
			var subject = [];
			
			$("div.link-conrainer").find('p').each(function(){
				subject.push({
					coords:$(this).find("input").eq(1).val(),
					href:$(this).find("input").eq(0).val()
            	});
	        });  
			
			params = subject; 
			console.log(params);
			
			window.location.href=domain+"/cmstemplet/subview.htm?params="+$.toJSON(params);  
			
		  });
		
	}
	
		
});

//页面初始化
$(document).ready(function()
{
	PFUNC.initRegion.call(this); 
	
	$('#imgMap').imageMaps();
	
});	