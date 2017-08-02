var PFUNC ={};
var PCACHE  ={};

/**
 * 页面事件注册
 */
$(function(){
	
	PFUNC.retpar = function() {
		$('#returnPage').unbind().bind('click',function(){
			
			//这个是把查询条件带过去
        	var advertinfo={}; 
        	advertinfo.pageNameBak = $('#pageNameBak').val();
			advertinfo.templeNameBak = $('#templeNameBak').val();
			advertinfo.positionNameBak = $('#positionNameBak').val();
   	        window.location.href=domain+"/cms/listPositionOpr.htm?advertinfo="+$.toJSON(advertinfo);  
   	        
		});
	};
		
	PFUNC.inputFormSaveBtn = function() {
		$('#addWritten').unbind().bind('click',function(){
			if(Putil.isEmpty($("#name").val())){
				alert("请填写名称");
				return;
			}
			if(Putil.isEmpty($("#pcContentEditor").val())){
				alert("请填写内容");
				return;
			}
			if(Putil.isEmpty($("#status").val())){
				alert("请选择状态");
				return;
			}
			if(Putil.isEmpty($("#startdate").val())){
				alert("请选择开始时间");
				return;
			}
			if(Putil.isEmpty($("#enddate").val())){
				alert("请选择结束时间");
				return;
			}
			
			var params={}; 
			params.id = $('#definedId').val();
			params.positionId = $('#positionId').val();
			params.name = $('#name').val();
			params.content = $("#pcContentEditor").val();
			params.startdate = $('#startdate').val();
			params.enddate = $('#enddate').val();
			params.status = $('#status').val();
			$.post(domain +"/cmstemplet/saveDefinedElement.htm", {params:$.toJSON(params)}, function(redata){
		          
				if(redata.isSuccess){//添加成功
					alert(redata.data);
					
					window.location.href=domain+"/cmstemplet/listDefinedElement.htm?positionId="+$('#positionId').val()+"&pageName="
            		+$('#pageName').val()+"&templeName="+$('#templeName').val()+"&positionName="+$('#positionName').val()
            		+"&pageNameBak="+$('#pageNameBak').val()+"&templeNameBak="+$('#templeNameBak').val()
                		+"&positionNameBak="+$('#positionNameBak').val();;
						
			   }else{
				   alert(redata.data);
			   }
				
	        }, "json");
		});
	};
	
	//移除
	$('table.customertable a.delLocalActivity').unbind().bind('click',function(){
		var params={}; 
		params.id = $(this).parent().parent().find('td span.definedId').text();
		$(this).parent().parent().remove();
		$.post(domain +"/cmstemplet/delDefinedElement.htm", {params:$.toJSON(params)}, function(redata){
	           if(redata.isSuccess){//移除成功
	        	   alert("移除成功");
			   }else{
				   alert(redata.message);
			   }
	        }, "json");
	});
	
	//修改
	$('table.customertable a.editAtt').unbind().bind('click',function(){
		var params={}; 
		params.id = $(this).parent().parent().find('td span.definedId').text();
		
		params.positionId = $('#positionId').val();
		params.pageName = $('#pageName').val();
		params.templeName = $('#templeName').val();
		params.positionName = $('#positionName').val();
		
		var adv={}; 
		adv.pageNameBak = $('#pageNameBak').val();
		adv.templeNameBak = $('#templeNameBak').val();
		adv.positionNameBak = $('#positionNameBak').val();
		
		goPage.call(this,"/cmstemplet/editDefinedElement.htm",{params:$.toJSON(params),adv:$.toJSON(adv)});   
		
	});
	
	//创建
	$('#createWritten').unbind().bind('click',function(){
		$('#definedId').val("");
		$('#name').val("");
		$("#pcContentEditor").val("");
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