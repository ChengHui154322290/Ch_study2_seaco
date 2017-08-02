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
        	advertinfo.pageNameBak = $('#pageNameBak').val();
			advertinfo.templeNameBak = $('#templeNameBak').val();
			advertinfo.positionNameBak = $('#positionNameBak').val();
   	        window.location.href=domain+"/cms/listPositionOpr.htm?advertinfo="+$.toJSON(advertinfo);  
   	        
		});
	};
		
	/*PFUNC.inputFormCreatBtn = function() {
		$('#createWritten').unbind().bind('click',function(){
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
	};*/
		
	PFUNC.inputFormSaveBtn = function() {
		$('#addWritten').unbind().bind('click',function(){
			if(Putil.isEmpty($("#name").val())){
				alert("请填写名称");
				return;
			}
			if(Putil.isEmpty($("#link").val())){
				alert("请填写链接地址");
				return;
			}
			
			if(Putil.isEmpty($("#startdate").val())){
				alert("请填写开始时间");
				return;
			}
			if(Putil.isEmpty($("#enddate").val())){
				alert("请填写结束时间");
				return;
			}
			if(Putil.isEmpty($("#status").val())){
				alert("请选择状态");
				return;
			}
			
			var params={}; 
			params.id = $('#writtenId').val();
			params.positionId = $('#positionId').val();
			params.name = $('#name').val();
			params.link = $("#link").val();
			params.startdate = $('#startdate').val();
			params.enddate = $('#enddate').val();
			params.status = $('#status').val();
			$.post(domain +"/cmstemplet/saveWrittenElement.htm", {params:$.toJSON(params)}, function(redata){
		          
				if(redata.isSuccess){//添加成功
					alert(redata.data);
					/*$('#positionId').val("");
					$('#name').val("");
					$("#link").val("");
					$('#startdate').val("");
					$('#enddate').val("");
					$('#status').val("");*/
					
					window.location.href=domain+"/cmstemplet/listWrittenElement.htm?positionId="+$('#positionId').val()+"&pageName="
            		+$('#pageName').val()+"&templeName="+$('#templeName').val()+"&positionName="+$('#positionName').val()
            		+"&pageNameBak="+$('#pageNameBak').val()+"&templeNameBak="+$('#templeNameBak').val()
                		+"&positionNameBak="+$('#positionNameBak').val();
						
			   }else{
				   alert(redata.data);
			   }
				
	        }, "json");
		});
	};
	
	//移除
	$('table.customertable a.delLocalActivity').unbind().bind('click',function(){
		var params={}; 
		params.id = $(this).parent().parent().find('td span.writtenId').text();
		$(this).parent().parent().remove();
		$.post(domain +"/cmstemplet/delWrittenElement.htm", {params:$.toJSON(params)}, function(redata){
	           if(redata.isSuccess){//移除成功
	        	   alert("移除成功");
			   }else{
				   alert(redata.message);
			   }
	        }, "json");
	});
	
	//修改
	$('table.customertable a.editAtt').unbind().bind('click',function(){
		$('#writtenId').val($(this).parent().parent().find('td span.writtenId').text());
		$('#name').val($(this).parent().parent().find('td span.name').text());
		$("#link").val($(this).parent().parent().find('td span.link').text());
		$('#startdate').val($(this).parent().parent().find('td span.startdate').text());
		$('#enddate').val($(this).parent().parent().find('td span.enddate').text());
		$('#status').val($(this).parent().parent().find('td span.statusCode').text());
	});
	
	//创建
	$('#createWritten').unbind().bind('click',function(){
		$('#writtenId').val("");
		$('#name').val("");
		$("#link").val("");
		$('#startdate').val("");
		$('#enddate').val("");
		$('#status').val("");
	});
	
});

//页面初始化
$(document).ready(function()
{
	// 提交事件
	PFUNC.inputFormSaveBtn.call(this);
	
	// 创建事件
	//PFUNC.inputFormCreatBtn.call(this);
	
	// 返回事件
	PFUNC.retpar.call(this);
	
	$("#startdate").datetimepicker({
        showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
    });
	
	$("#enddate").datetimepicker({
        showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
    });
	
});	