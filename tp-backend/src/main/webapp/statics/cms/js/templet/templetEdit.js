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
   	        window.location.href=domain+"/cms/listTempleOpr.htm?advertinfo="+$.toJSON(advertinfo);  
   	        
		});
	};
		
	PFUNC.inputFormSaveBtn = function() {
		$('#inputFormSaveBtn').live('click',function(){
			if(Putil.isEmpty($("input.pageName").val())){
				alert("请填写页面名称");
				return;
			}
			if(Putil.isEmpty($("input.templeName").val())){
				alert("请填写模块名称");
				return;
			}
			if(Putil.isEmpty($("input.templeCode").val())){
				alert("请填写模块编号");
				return;
			}
			if(Putil.isEmpty($("select.status").val())){
				alert("请填写状态");
				return;
			}
			if(Putil.isEmpty($("select.elementType").val())){
				alert("请填写元素类型");
				return;
			}
			
			 $('#inputForm').ajaxSubmit(function(data){
				 	if(data.isSuccess == "\"true\"" || data.isSuccess == true ){
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
			   	        window.location.href=domain+"/cms/listTempleOpr.htm?advertinfo="+$.toJSON(advertinfo);  
				   	       
	                }else{
	                	alert("提交失败");
	                }
	            });
		});
		
		$('#inputFormSaveAndCreatBtn').live('click',function(){
			if(Putil.isEmpty($("input.pageName").val())){
				alert("请填写页面名称");
				return;
			}
			if(Putil.isEmpty($("input.templeName").val())){
				alert("请填写模块名称");
				return;
			}
			if(Putil.isEmpty($("input.templeCode").val())){
				alert("请填写模块编号");
				return;
			}
			if(Putil.isEmpty($("select.status").val())){
				alert("请填写状态");
				return;
			}
			if(Putil.isEmpty($("select.elementType").val())){
				alert("请填写元素类型");
				return;
			}
			
			 $('#inputForm').ajaxSubmit(function(data){
				 	if(data.isSuccess == "\"true\"" || data.isSuccess == true ){
	                	alert("提交成功");
	                	//goPageGet.call(this,domain +"/cmsAdvertIndex/listAdvertiseTemp.htm");   
	                	
	                	//清除表单数据，只保留页面id和页面名称
	                	$("input.templeName").val("");
	                	$("input.templetId").val("");
	                	$("input.templeCode").val("");
	                	$("select.status").val("");
	                	$("input.seq").val("");
	                	$("select.elementType").val("");
	                	$("input.elementNum").val("");
				   	       
	                }else{
	                	alert("提交失败");
	                }
	            });
		});
		
		
	};
		
	/**********************************  一下是弹出框事件    *******************************************************/
	//点击查询，弹出查询框
	$('#advert_type_query').unbind().bind('click',function(){
		$("tr.temp_dev_tr_advertise").remove();   
		layerpopreceive=window.open("advert_type_pop","页面管理",function(){      
			},['550px', '450px'],true);
	});
	
	//关闭弹出框
	$('#dev_advert_type_close').unbind().bind('click',function(){
		layer.close($("#advert_type_pop").data("pop"));
	});
	
	//点击弹出框里面的查询
	$('#dev_advert_type_query').unbind().bind('click',function(){
		$("tr.temp_dev_tr_advertise").remove();   
		var params={}; 
		if($('input.dev_name').val()!=null && $('input.dev_name').val()!=""){
			params.name = $('input.dev_name').val();
		}
		params.status = "0";
		$.post(domain +"/cms/queryPageString.htm", {params:$.toJSON(params)}, function(redata){
				pageflag=true;
	           if(redata.isSuccess){//添加成功
	        	   var value = redata.data;
			        	$.each(value,function(i,val){  
			        		var tr = PCACHE.temp_dev_tr_advertise.clone(true);
			        		$(tr).find("span.pop_page_id").text(Putil.toNull(val.id));  
			 				$(tr).find("span.pop_page_name").text(Putil.toNull(val.pageName));  
			 				$(tr).find("span.pop_page_code").text(val.pageCode); 
			 				
			 				//添加单击和双击事件
				        	$(tr).find("span").unbind().bind("click",function(){ 	
				        		$("#pageName").val(Putil.toNull(val.pageName));
				        		$("#pageId").val(Putil.toNull(val.id));
				        		layer.close($("#advert_type_pop").data("pop"));
				        	});
				        	
			 				$("table.dev_customertable tbody").append(tr); 
			        	});
			   }else{
				   alert(redata.message);
			   }
	        }, "json");
	});
	/*****************************************************************************************/
	
});

//页面初始化
$(document).ready(function()
{
	// 提交事件
	PFUNC.inputFormSaveBtn.call(this);
	
	// 返回事件
	PFUNC.retpar.call(this);
	
	PCACHE.temp_dev_tr_advertise=$("tr.temp_dev_tr_advertise").clone(true);
	
});	