var PFUNC ={};
var PCACHE  ={};
/**
 * 页面事件注册
 */
$(function(){
	
	PFUNC.inputFormBtn = function() {
		
		
		//移除
		$('table.customertable a.delLocalActivity').unbind().bind('click',function(){
			var params={}; 
			params.id = $(this).parent().parent().find('td span.singleTempleActivityId').text();
			//params.singleTepnodeId = $(this).parent().parent().find('td span.singleTepnodeId').text();
			params.singleTepnodeId = $("#tempnodeid").attr("value");
			$(this).parent().parent().remove();
			$.post(domain +"/cmsSingleTemple/delActivityByID.htm", {params:$.toJSON(params)}, function(redata){
		           if(redata.isSuccess){//移除成功
		        	   alert("移除成功");
				   }else{
					   alert(redata.message);
				   }
		        }, "json");
		});
		
		
		//添加
		$('table.customertable_temp a.addActivity').unbind().bind('click',function(){
			var params={}; 
			
			if($(this).parent().parent().find('td a.delLocalActivity').text() == "移除"){
				params.id = $(this).parent().parent().find('td span.singleTempleActivityId').text();
				//params.singleTepnodeId = $(this).parent().parent().find('td span.singleTepnodeId').text();
				params.singleTepnodeId = $("#tempnodeid").attr("value");
				$(this).parent().parent().remove();
				$.post(domain +"/cmsSingleTemple/delActivityByID.htm", {params:$.toJSON(params)}, function(redata){
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
				$.post(domain +"/cmsSingleTemple/addActivityByID.htm", {params:$.toJSON(params)}, function(redata){
			           if(redata.isSuccess){//添加成功
			        	   alert("添加成功");
			        	   $("table.customertable tbody").append(tr); 
			        	   currenttr.remove();
					   }else{
						   alert(redata.data);
					   }
			        }, "json");
			}
			
			
		});
		
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
			$("tr.temp_tr_singletemp").remove();   
			params.activityInputId = $('input.activityInputId').val();
			params.activityInputName = $('input.activityInputName').val();
			params.activityInputCode = $('input.activityInputCode').val();
			params.activityInputSd = $('input.activityInputSd').val();
			params.activityInputed = $('input.activityInputed').val();
			params.type = $('select.type').val();
			params.platformType = $('select.platformType').val();
			params.salesPartten = $('select.salesPartten').val();
			$.post(domain +"/cmsSingleTemple/getCmsTopicInfoLixt.htm", {params:$.toJSON(params)}, function(redata){
		          
				if(redata.isSuccess){//添加成功
		        	   var value = redata.data;
				        	$.each(value,function(i,val){  
				        		var tr = PCACHE.temp_tr_announce.clone(true);
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
				   }else{
					   alert(redata.message);
				   }
				
		        }, "json");
		});
		
		
		
	};
	
	PFUNC.retpar = function() {
		$('#returnPage').unbind().bind('click',function(){
			//history.go(-1);
			
			//这个是把查询条件带过去
			var singleTempInfo={}; 
			singleTempInfo.templeNameBak = $('#templeNameBak').val();
			singleTempInfo.positionNameBak = $('#positionNameBak').val();
			singleTempInfo.statusBak = $('#statusBak').val();
			singleTempInfo.platformTypeBak = $('#platformTypeBak').val();
			singleTempInfo.typeBak = $('#typeBak').val();
   	        window.location.href=domain +"/cmsSingleTemple/listSingletemple.htm?singleTempInfo="+$.toJSON(singleTempInfo);  
   	        
		});
	};
	
});

//页面初始化
$(document).ready(function()
{
	// 页面按钮注册
	PFUNC.inputFormBtn.call(this);
	
	// 返回事件
	PFUNC.retpar.call(this);
	
	PCACHE.temp_tr_announce=$("tr.temp_tr_singletemp").clone(true);
	
	PCACHE.temp_tr_singleActivitytemp=$("tr.temp_tr_singleActivitytemp").clone(true);
	
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
	
});	