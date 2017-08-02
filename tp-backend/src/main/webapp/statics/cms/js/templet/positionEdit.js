var PFUNC ={};
var PCACHE  ={};

elementStaMap = {
		"1": "活动",
		"2": "图片",
		"3": "文字",
		"4": "自定义编辑",
		"5": "SEO",
		"6": "公告",
		"7": "资讯"
	};

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
			advertinfo.pageNameBak = $('#pageNameBak').val();
			advertinfo.templeNameBak = $('#templeNameBak').val();
			advertinfo.positionNameBak = $('#positionNameBak').val();
   	        window.location.href=domain+"/cms/listPositionOpr.htm?advertinfo="+$.toJSON(advertinfo);  
   	        
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
			if(Putil.isEmpty($("input.positionName").val())){
				alert("请填写位置名称");
				return;
			}
			if(Putil.isEmpty($("input.positionCode").val())){
				alert("请填写位置编号");
				return;
			}
			if(Putil.isEmpty($("select.status").val())){
				alert("请选择状态");
				return;
			}
			if(Putil.isEmpty($("input.seq").val()) ){
				alert("请填写顺序");
				return;
			}
			
			 $('#inputForm').ajaxSubmit(function(data){
	                if(data.isSuccess == "\"true\"" || data.isSuccess == true ){
	                	alert("提交成功");
	                	//goPageGet.call(this,domain +"/cmsAdvertIndex/listAdvertiseTemp.htm");   
	                	
	                	//这个是把查询条件带过去
	        			var advertinfo={}; 
	        			advertinfo.pageNameBak = $('#pageNameBak').val();
	        			advertinfo.templeNameBak = $('#templeNameBak').val();
	        			advertinfo.positionNameBak = $('#positionNameBak').val();
			   	        window.location.href=domain+"/cms/listPositionOpr.htm?advertinfo="+$.toJSON(advertinfo);  
				   	       
	                }else{
	                	alert(data.data);
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
			if(Putil.isEmpty($("input.positionName").val())){
				alert("请填写位置名称");
				return;
			}
			if(Putil.isEmpty($("input.positionCode").val())){
				alert("请填写位置编号");
				return;
			}
			if(Putil.isEmpty($("select.status").val())){
				alert("请选择状态");
				return;
			}
			if(Putil.isEmpty($("input.seq").val()) ){
				alert("请填写顺序");
				return;
			}
			
			 $('#inputForm').ajaxSubmit(function(data){
	                if(data.isSuccess == "\"true\"" || data.isSuccess == true ){
	                	alert("提交成功");
	                	//goPageGet.call(this,domain +"/cmsAdvertIndex/listAdvertiseTemp.htm");   
	                	
	                	//清除表单数据，只保留页面id和页面名称
	                	$("input.positionName").val("");
	                	$("input.positionCode").val("");
	                	$("input.positionId").val("");
	                	$("select.status").val("");
	                	$("input.seq").val("");
	                	//$("select.elementType").val("");
				   	       
	                }else{
	                	alert(data.data);
	                }
	            });
		});
		
	};
		
	/**********************************  一下是弹出框事件(页面查询事件)    *******************************************************/
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
				        		$('#dev_page_id').val(Putil.toNull(val.id));
				        		$("select.elementType").val("");
				        		//清除模板信息数据
				        		$("#templeName").val("");
				        		$("#templeId").val("");
				        		layer.close($("#advert_type_pop").data("pop"));
				        	});
				        	
			 				$("table.dev_customertable tbody").append(tr); 
			        	});
			   }else{
				   alert(redata.message);
			   }
	        }, "json");
	});
	
	/**********************************  一下是弹出框事件(模块查询事件)    *******************************************************/
	//点击查询，弹出查询框
	$('#templet_type_query').unbind().bind('click',function(){
		if($('#dev_page_id').val() == null || $('#dev_page_id').val()==""){
			if($('#pageId').val() != null || $('#pageId').val()==""){
				$('#dev_page_id').val($('#pageId').val());
			}else{
				alert("请先选择页面");
				return;
			}
		}
		$("tr.temp_dev_tr_templet").remove();   
		layerpopreceive=window.open("templet_type_pop","模板管理",function(){      
			},['550px', '450px'],true);
	});
	
	//关闭弹出框
	$('#dev_templet_type_close').unbind().bind('click',function(){
		layer.close($("#templet_type_pop").data("pop"));
	});
	
	//点击弹出框里面的查询
	$('#dev_templet_type_query').unbind().bind('click',function(){
		$("tr.temp_dev_tr_templet").remove();   
		var params={}; 
		var templateName = $(this).parent().find('input.dev_name');
		if(templateName.val()!=null && templateName.val()!=""){
			params.name = templateName.val();
		}
		params.pageId = $('#dev_page_id').val();
		params.status = "0";
		$.post(domain +"/cms/queryTempletString.htm", {params:$.toJSON(params)}, function(redata){
				pageflag=true;
	           if(redata.isSuccess){//添加成功
	        	   var value = redata.data;
			        	$.each(value,function(i,val){  
			        		var tr = PCACHE.temp_dev_tr_templet.clone(true);
			        		$(tr).find("span.pop_templet_id").text(Putil.toNull(val.id));  
			 				$(tr).find("span.pop_templet_name").text(Putil.toNull(val.templeName));  
			 				$(tr).find("span.pop_templet_code").text(val.templeCode); 
			 				$(tr).find("span.pop_templet_emttype").text(elementStaMap[val.elementType]); 
			 				$(tr).find("span.pop_templet_num").text(val.elementNum); 
			 				
			 				//添加单击和双击事件
				        	$(tr).find("span").unbind().bind("click",function(){ 	
				        		$("#templeName").val(Putil.toNull(val.templeName));
				        		$("#templeId").val(Putil.toNull(val.id));
				        		$("select.elementType_bak").val(Putil.toNull(val.elementType));
				        		$("input.elementType").val(Putil.toNull(val.elementType));
				        		//$("select.elementType").attr("value",val.elementType);
				        		layer.close($("#templet_type_pop").data("pop"));
				        	});
				        	
			 				$("table.dev_templettable tbody").append(tr); 
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
	
	PCACHE.temp_dev_tr_templet=$("tr.temp_dev_tr_templet").clone(true);
	
});	