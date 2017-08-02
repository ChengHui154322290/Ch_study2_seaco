var PFUNC ={};
var PCACHE  ={};
var layerpopreceive;
var pageflag = true;

var newActTr;
var currenttr;

/**
 * 页面事件注册
 */
$(function(){
	
	//移除
	$('table.customertable a.delLocalActivity').unbind().bind('click',function(){
		var params={}; 
		params.id = $(this).parent().parent().find('td span.ActivityElementId').text();
		//params.singleTepnodeId = $(this).parent().parent().find('td span.singleTepnodeId').text();
		//params.singleTepnodeId = $("#tempnodeid").attr("value");
		$(this).parent().parent().remove();
		$.post(domain +"/cmstemplet/delActivityByID.htm", {params:$.toJSON(params)}, function(redata){
	           if(redata.isSuccess){//移除成功
	        	   alert("移除成功");
			   }else{
				   alert(redata.message);
			   }
	        }, "json");
	});
	
	
	//添加
	/*$('table.customertable_temp a.addActivity').unbind().bind('click',function(){
		var params={}; 
		
		if($(this).parent().parent().find('td a.delLocalActivity').text() == "移除"){
			params.id = $(this).parent().parent().find('td span.singleTempleActivityId').text();
			//params.singleTepnodeId = $(this).parent().parent().find('td span.singleTepnodeId').text();
			params.singleTepnodeId = $("#tempnodeid").attr("value");
			$(this).parent().parent().remove();
			$.post(domain +"/cmstemplet/delActivityByID.htm", {params:$.toJSON(params)}, function(redata){
		           if(redata.isSuccess){//移除成功
		        	   alert("移除成功");
				   }else{
					   alert(redata.message);
				   }
		        }, "json");
		}else{
			params.id = $(this).parent().parent().find('td span.singleTempleActivityId').text();
			params.singleTepnodeId = $("#tempnodeid").attr("value");
			params.startdate = $(this).parent().parent().find('td span.startdate').text();;
			params.enddate = $(this).parent().parent().find('td span.enddate').text();;
			
			var tr = PCACHE.temp_tr_announce.clone(true);
    		$(tr).find("span.activityCode").text($(this).parent().parent().find('td span.activityCode').text());  
			$(tr).find("span.activityName").text($(this).parent().parent().find('td span.activityName').text());  
			$(tr).find("span.skuCode").text($(this).parent().parent().find('td span.skuCode').text()); 
			$(tr).find("span.goodsName").text($(this).parent().parent().find('td span.goodsName').text()); 
			$(tr).find("span.seller").text($(this).parent().parent().find('td span.seller').text()); 
			$(tr).find("span.standardParams").text($(this).parent().parent().find('td span.standardParams').text()); 
			$(tr).find("span.limitTotal").text($(this).parent().parent().find('td span.limitTotal').text()); 
			$(tr).find("span.limitNumber").text($(this).parent().parent().find('td span.limitNumber').text()); 
			$(tr).find("span.sellingPrice").text($(this).parent().parent().find('td span.sellingPrice').text()); 
			$(tr).find("span.flagStr").text('刚添加'); 
			$(tr).find("span.startdate").text($(this).parent().parent().find('td span.startdate').text()); 
			$(tr).find("span.enddate").text($(this).parent().parent().find('td span.enddate').text()); 
			$(tr).find("span.singleTempleActivityId").text($(this).parent().parent().find('td span.singleTempleActivityId').text()); 
			$(tr).find("a.addActivity").text("移除"); 
			$(tr).find("a.addActivity").addClass("delLocalActivity");
			$(tr).find("a.delLocalActivity").removeClass("addActivity");
			$(tr).addClass("temp_tr_singleActivitytemp");
			$(tr).removeClass("temp_tr_singletemp");
			//$("table.customertable tbody").append(tr); 
			var currenttr = $(this).parent().parent();//.remove();
			$.post(domain +"/cmstemplet/addActivityByID.htm", {params:$.toJSON(params)}, function(redata){
		           if(redata.isSuccess){//添加成功
		        	   alert("添加成功");
		        	   $("table.customertable tbody").append(tr); 
		        	   currenttr.remove();
				   }else{
					   alert(redata.data);
				   }
		        }, "json");
		}
		
		
	});*/
	
	//重置
	$('.resetButtonInput').unbind().bind('click',function(){
            $("input.activityInputId").attr("value","");
            $("input.activityInputName").attr("value","");
            $("input.activityInputCode").attr("value","");
            $("input.activityInputSd").attr("value","");
            $("input.activityInputed").attr("value","");
	});
	
	//查询
	$('#singleTempleListQuery').unbind().bind('click',function(){
		var params={}; 
		
		if(pageflag){
			$('#currPage').text(1);
		}
		
		$("tr.temp_tr_singletemp").remove();   
		params.activityInputId = $('input.activityInputId').val();
		params.activityInputName = $('input.activityInputName').val();
		params.activityInputCode = $('input.activityInputCode').val();
		params.activityInputSd = $('input.activityInputSd').val();
		params.activityInputed = $('input.activityInputed').val();
		params.type = $('select.type').val();
		params.platformType = $('select.platformType').val();
		params.salesPartten = $('select.salesPartten').val();
		params.pageId = $('#currPage').text();
		params.pageSize = $('#perCount').find("option:selected").text();
		$.post(domain +"/cmstemplet/getCmsTopicInfoLixt.htm", {params:$.toJSON(params)}, function(redata){
			pageflag=true;
			if(redata.isSuccess){//添加成功
				
				//首先赋予初始值
	        	$("#currPage").text(1);
		        $("#totalPage").text(1);
		        $("#topicCount").text(0);
		        var currPage;
				var totalPage;
				var topicCount;
				var topicCountNum;
		        	
	        	   var value = redata.data;
			        	$.each(value,function(i,val){  
			        		var tr = PCACHE.temp_tr_announce.clone(true);
			        		
			        		currPage=val.startPage;
							totalPage=val.pageSize;
							topicCount=val.totalCount;
							topicCountNum=val.totalCountNum;
							
			        		$(tr).find("span.flagStr").parent().remove();  
			        		$(tr).find("span.activityCode").text(val.activityCode);  
			 				$(tr).find("span.activityName").text(val.activityName);  
			 				$(tr).find("span.skuCode").text(val.skuCode); 
			 				$(tr).find("span.goodsName").text(val.goodsName); 
			 				$(tr).find("span.seller").text(val.seller); 
			 				$(tr).find("span.standardParams").text(val.standardParams); 
			 				$(tr).find("span.limitTotal").text(val.limitTotal); 
			 				$(tr).find("span.limitNumber").text(val.limitNumber); 
			 				$(tr).find("span.sellingPrice").text(val.sellingPrice); 
			 				$(tr).find("span.startdate").text(val.startdateStr); 
			 				$(tr).find("span.enddate").text(val.enddateStr); 
			 				$(tr).find("span.singleTempleActivityId").text(val.id); 
			 				$("table.customertable_temp tbody").append(tr); 
			        	});
			        	
			        	$("#currPage").text(currPage);
			        	$("#totalPage").text(topicCount);
			        	$("#topicCount").text(topicCountNum);
			        	
			   }else{
				   alert(redata.message);
			   }
			
	        }, "json");
	});
	
	
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
		
	/*PFUNC.inputFormSaveBtn = function() {
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
	                	advertinfo.pageNameBak = $('#pageNameBak').val();
	        			advertinfo.templeNameBak = $('#templeNameBak').val();
	        			advertinfo.positionNameBak = $('#positionNameBak').val();
			   	        window.location.href=domain+"/cms/listPageOpr.htm?advertinfo="+$.toJSON(advertinfo);  
				   	       
	                }else{
	                	alert("提交失败");
	                }
	            });
		});
	};*/
	
	$('img.imgPrivewsrc').unbind().bind('click',function(){
		var imgSrc = $("img.imgPrivewsrc").attr("src");
		$.layer({
			type : 2,
			title : "图片信息",
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '600px', 400 ],
			iframe : {
				src : imgSrc
			}
		});
	});
	
	$("#clearRollPic").on("click",function(){
		$('#picture').val("");
	});
		
	$("#nextPage").on("click",function() {
		pageflag = false;
		var currentPage = $("#currPage")[0].innerHTML;
		if (null == currentPage
				|| $.trim(currentPage).length == 0
				|| 0 > parseInt(currentPage)) {
			return;
		}
		var totalPage = $("#totalPage")[0].innerHTML;
		var nextPage = parseInt(currentPage) + 1;
		if (nextPage > parseInt(totalPage)) {
			layer.alert("已经是最后一页");
			return;
		}
		//$("#pageNo").val(nextPage);
		$("#currPage").val(nextPage);
		$("#currPage").text(nextPage);
		$("#singleTempleListQuery").trigger('click');  
	});
	
	$("#prePage").on("click",function() {
		pageflag = false;
		var currentPage = $("#currPage")[0].innerHTML;
		if (null == currentPage
				|| $.trim(currentPage).length == 0
				|| 0 > parseInt(currentPage)) {
			return;
		}
		var prePage = parseInt(currentPage) - 1;
		if (prePage < 1) {
			layer.alert("已经是第一页");
			return;
		}
		//$("#pageNo").val(prePage);
		$("#currPage").val(prePage);
		$("#currPage").text(prePage);
		$("#singleTempleListQuery").trigger('click');  
	});
	
	
	/**********************************  一下是弹出框事件(页面查询事件)    *******************************************************/
	//点击添加按钮，弹出框
	$('a.addActivity').unbind().bind('click',function(){
		$('#fgStatus').val("0");
		//给id值赋值
		$('#activity_layer_Id').val($(this).parent().parent().find('td span.singleTempleActivityId').text());
		$('#position_layer_Id').val($('#positionId').val());
		//先给弹出框的值清除掉
		$('#startdatebak').val("");
		$('#enddatebak').val("");
		$('select.statusbak').val("");
		$('input.linkbak').val("");
		$('#picture').val("");
		$('#imageImg_src').attr("src","");
		
		/*newActTr = $("tr.temp_tr_singleActivitytemp").clone(true);
		$(newActTr).find("span.activityCode").text($(this).parent().parent().find('td span.activityCode').text());  
		$(newActTr).find("span.activityName").text($(this).parent().parent().find('td span.activityName').text());  
		$(newActTr).find("span.skuCode").text($(this).parent().parent().find('td span.skuCode').text()); 
		$(newActTr).find("span.goodsName").text($(this).parent().parent().find('td span.goodsName').text()); 
		$(newActTr).find("span.seller").text($(this).parent().parent().find('td span.seller').text()); 
		$(newActTr).find("span.standardParams").text($(this).parent().parent().find('td span.standardParams').text()); 
		$(newActTr).find("span.limitTotal").text($(this).parent().parent().find('td span.limitTotal').text()); 
		$(newActTr).find("span.limitNumber").text($(this).parent().parent().find('td span.limitNumber').text()); 
		$(newActTr).find("span.sellingPrice").text($(this).parent().parent().find('td span.sellingPrice').text()); 
		$(newActTr).find("span.flagStr").text('刚添加'); 
		$(newActTr).find("span.startdate").text($(this).parent().parent().find('td span.startdate').text()); 
		$(newActTr).find("span.enddate").text($(this).parent().parent().find('td span.enddate').text()); 
		
		currenttr = $(this).parent().parent();*/
		
		layerpopreceive=window.open("advert_type_pop","添加活动",function(){      
			},['550px', '450px'],true);
	});
	
	//点击修改按钮，弹出框
	$('a.updateLocalActivity').unbind().bind('click',function(){
		$('#fgStatus').val("1");
		//给id值赋值
		$('#activity_layer_Id').val($(this).parent().parent().find('td span.singleTempleActivityId').text());
		$('#position_layer_Id').val($('#positionId').val());
		
		//先给弹出框赋值
		$('#layer_Id').val($(this).parent().parent().find('td span.ActivityElementId').text());
		$('#startdatebak').val($(this).parent().parent().find('td span.startdate').text());
		$('#enddatebak').val($(this).parent().parent().find('td span.enddate').text());
		$('select.statusbak').val($(this).parent().parent().find('td span.statusCode').text());
		$('input.linkbak').val($(this).parent().parent().find('td span.link').text())
		$('input.seqbak').val($(this).parent().parent().find('td span.seq').text());
		$('input.picture').val($(this).parent().parent().find('td span.picSrc').text());
		$('#imageImg_src').attr("src",$(this).parent().parent().find('td img.imgPrivewsrc').attr("src"));
		
		layerpopreceive=window.open("advert_type_pop","修改活动",function(){      
			},['550px', '450px'],true);
	});
	
	//关闭弹出框
	$('#dev_advert_type_close').unbind().bind('click',function(){
		layer.close($("#advert_type_pop").data("pop"));
	});
	
	//点击弹出框里面的保存
	$('#add_activityElementId').unbind().bind('click',function(){
		
		if(Putil.isEmpty($("input.startdatebak").val())){
			alert("请填写开始日期");
			return;
		}
		if(Putil.isEmpty($("input.enddatebak").val())){
			alert("请填写结束日期");
			return;
		}
		if(Putil.isEmpty($("select.statusbak").val())){
			alert("请选择状态");
			return;
		}
		
		var params={}; 
		params.id = $('#layer_Id').val();
		params.positionId = $('#position_layer_Id').val();
		params.activityId = $('#activity_layer_Id').val();
		params.startDate = $('input.startdatebak').val();
		params.endDate = $('input.enddatebak').val();
		params.status = $('select.statusbak').val();
		params.link = $('input.linkbak').val();
		params.seq = $('input.seqbak').val();
		params.fgStatus = $('#fgStatus').val();
		params.picture = $('#picture').val();
		
		//表单添加url
		$("#inputForm").attr("action",domain +"/cmstemplet/addActivityByID.htm"); 
		$('#inputForm').ajaxSubmit(function(data){
            if(data.isSuccess){
            	alert("提交成功");
            	
            	/*if($('#fgStatus').val() == "0"){
	        		$("table.customertable tbody").append(newActTr); 
		        	currenttr.remove();
		        	newActTr="";
	        	}
 				layer.close($("#advert_type_pop").data("pop"));*/
            	
            	window.location.href=domain+"/cmstemplet/listActElement.htm?positionId="+$('#positionId').val()+"&pageName="
            		+$('#pageName').val()+"&templeName="+$('#templeName').val()+"&positionName="+$('#positionName').val()
            		+"&pageNameBak="+$('#pageNameBak').val()+"&templeNameBak="+$('#templeNameBak').val()
                		+"&positionNameBak="+$('#positionNameBak').val();
		   	       
            }else{
            	alert(data.data);
            }
          });
		
		
	});
	/*****************************************************************************************/
	
	// pc端图片上传
	$("#imageImg").on("click",function(){
		topicClickImgBind("查看图片");
	});
	
	function topicClickImgBind(title){
		var url = domain +"/cmstemplet/upload_act/Image.htm";
		$.layer({
			type : 2,
			title : "上传图片",
			maxmin : true,
			fix : false,
			area: ['400px', 300],
			iframe : {
				src : url
			}
		});
	}
	
});

//页面初始化
$(document).ready(function()
{
	//PFUNC.inputFormSaveBtn.call(this);
	
	// 返回事件
	PFUNC.retpar.call(this);
	
	PCACHE.temp_tr_announce=$("tr.temp_tr_singletemp").clone(true);
	
	//PCACHE.temp_tr_singleActivitytemp=$("tr.temp_tr_singleActivitytemp").clone(true);
	
	$("tr.temp_tr_singletemp").remove() ;
	
	$("#activityInputSd").datetimepicker({
        showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
    });
	
	$("#activityInputed").datetimepicker({
        showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
    });
	
	$("#startdatebak").datetimepicker({
        showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
    });
	
	$("#enddatebak").datetimepicker({
        showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
    });
	
});	