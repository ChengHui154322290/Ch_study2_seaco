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
			var advertinfo={}; 
			advertinfo.functionNameBak = $('#functionNameBak').val();
			advertinfo.areaBak = $('#areaBak').val();
			advertinfo.platformTypeBak = $('#platformTypeBak').val();
			advertinfo.statusBak = $('#statusBak').val();
   	        window.location.href=domain+"/cmsRuleRedis/listRuleRedisTemp.htm?ruleRedisinfo="+$.toJSON(advertinfo);  
		});
	};
	
	PFUNC.inputFormSaveBtn = function() {
		$('#inputFormSaveBtn').live('click',function(){
			if($("div.item-picture img").eq(1).attr("src") != undefined && 
					$("div.item-picture img").eq(1).attr("src") != "" && 
					$("div.item-picture img").eq(1).attr("src") != null){
				alert("您上传的图片过多，请删除后再操作");
				return;
			}
			
			if(Putil.isEmpty($("input.advertname").val())){
				alert("请填写图片名称");
				return;
			}
			if(Putil.isEmpty($("input.sort").val())){
				alert("请填写图片排序");
				return;
			}
			if(Putil.isEmpty($("input.startdateStr").val())){
				alert("请填写开始时间");
				return;
			}
			if(Putil.isEmpty($("input.enddateStr").val())){
				alert("请填写结束时间");
				return;
			}
			if(Putil.isEmpty($("input.link").val())){
				alert("请填写图片链接");
				return;
			}
			if(Putil.isEmpty($("input.type").val()) || 
					Putil.isEmpty($("input.ident").val())){
				alert("请填写页面位置");
				return;
			}
			if(Putil.isEmpty($("input.path").val()) &&  
					$("div.item-picture img").eq(0).attr("src") == undefined  ){
				alert("请先上传图片后，再提交");
				return;
			}
			
				 $('#inputForm').ajaxSubmit(function(data){
		                if(data == "\"success\"" || data == "success"){
		                	alert("提交成功");
		                	//goPageGet.call(this,domain +"/cmsAdvertIndex/listAdvertiseTemp.htm");   
		                	
		                	//这个是把查询条件带过去
		        			var advertinfo={}; 
		        			advertinfo.nameBak = $('#nameBak').val();
		        			advertinfo.positionBak = $('#positionBak').val();
		        			advertinfo.startdateBak = $('#startdateBak').val();
		        			advertinfo.enddateBak = $('#enddateBak').val();
		        			advertinfo.typeBak = $('#typeBak').val();
		        			advertinfo.statusBak = $('#statusBak').val();
				   	        window.location.href=domain+"/cmsAdvertIndex/listAdvertiseTemp.htm?advertinfo="+$.toJSON(advertinfo);  
					   	       
		                }else{
		                	alert("提交失败");
		                }
		            });
		});
	};
	
	/**
	 * 屏蔽输入框只能输入数字
	 */
	PFUNC.inputFormValidate = function() {
			$("input.sort").keyup(function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).bind("paste",function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).css("ime-mode", "disabled");    
			
			$("input.activityid").keyup(function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).bind("paste",function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).css("ime-mode", "disabled");    
			
			$("input.time").keyup(function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).bind("paste",function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).css("ime-mode", "disabled");    
	};
	
	
	
});

//页面初始化
$(document).ready(function()
{
	// 返回事件
	PFUNC.retpar.call(this);
	
	PCACHE.temp_dev_tr_advertise=$("tr.temp_dev_tr_advertise").clone(true);
	
	// 提交事件
	PFUNC.inputFormSaveBtn.call(this);
	
	// 控制界面的输入框只能输入数字问题
	PFUNC.inputFormValidate.call(this);
	
	
});	