var PFUNC ={};
var PCACHE  ={};

/**
 * 页面事件注册
 */
$(function(){
	
	PFUNC.retpar = function() {
		$('#returnPage').unbind().bind('click',function(){
			//history.go(-1);
			//goPageGet.call(this,domain +"/cmsAdvertIndex/listAdvertiseTemp.htm"); 
			
			//这个是把查询条件带过去
			var advertinfo={}; 
			advertinfo.nameBak = $('#nameBak').val();
			advertinfo.positionBak = $('#positionBak').val();
			advertinfo.startdateBak = $('#startdateBak').val();
			advertinfo.enddateBak = $('#enddateBak').val();
			advertinfo.typeBak = $('#typeBak').val();
			advertinfo.statusBak = $('#statusBak').val();
   	        window.location.href=domain+"/cms/listPageOpr.htm?advertinfo="+$.toJSON(advertinfo);  
   	        
		});
	};
		
	PFUNC.inputFormSaveBtn = function() {
		$('#inputFormSaveBtn').live('click',function(){
			if(Putil.isEmpty($("input.pageName").val())){
				alert("请填写页面名称");
				return;
			}
			if(Putil.isEmpty($("input.pageCode").val())){
				alert("请填写页面编码");
				return;
			}
			if(Putil.isEmpty($("select.status").val())){
				alert("请填写状态");
				return;
			}
			if(Putil.isEmpty($("input.seq").val())){
				alert("请填写顺序");
				return;
			}
			
			 $('#inputForm').ajaxSubmit(function(data){
	                if(data.isSuccess == "\"true\"" || data.isSuccess == true ){
	                	alert("提交成功");
	                	
	                	//这个是把查询条件带过去
	        			var advertinfo={}; 
	        			advertinfo.nameBak = $('#nameBak').val();
	        			advertinfo.positionBak = $('#positionBak').val();
	        			advertinfo.startdateBak = $('#startdateBak').val();
	        			advertinfo.enddateBak = $('#enddateBak').val();
	        			advertinfo.typeBak = $('#typeBak').val();
	        			advertinfo.statusBak = $('#statusBak').val();
			   	        window.location.href=domain+"/cms/listPageOpr.htm?advertinfo="+$.toJSON(advertinfo);  
				   	       
	                }else{
	                	alert("提交失败");
	                }
	            });
		});
	};
		
	
});

//页面初始化
$(document).ready(function()
{
	// 提交事件
	PFUNC.inputFormSaveBtn.call(this);
	
	// 返回事件
	PFUNC.retpar.call(this);
	
});	