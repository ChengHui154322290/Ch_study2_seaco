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
		
	PFUNC.inputFormSaveBtn = function() {
		$('#inputFormSaveBtn').live('click',function(){
			if($("div.item-picture img").eq(1).attr("src") != undefined && 
					$("div.item-picture img").eq(1).attr("src") != "" && 
					$("div.item-picture img").eq(1).attr("src") != null){
				alert("您上传的图片过多，请删除后再操作");
				return;
			}
			
			if(Putil.isEmpty($("input.name").val())){
				alert("请填写图片名称");
				return;
			}
			if(Putil.isEmpty($("input.startdate").val())){
				alert("请填写开始时间");
				return;
			}
			if(Putil.isEmpty($("input.enddate").val())){
				alert("请填写结束时间");
				return;
			}
			if(Putil.isEmpty($("#picSrc").val()) &&  
					$("div.item-picture img").eq(0).attr("src") == undefined  ){
				alert("请先上传图片后，再提交");
				return;
			}
			if(Putil.isEmpty($("input.attr").val())){
				alert("请填写图片属性");
				return;
			}
			if(Putil.isEmpty($("select.status").val())){
				alert("请选择图片状态");
				return;
			}
			//表单添加url
			$("#inputForm").attr("action",domain +"/cmstemplet/savePicElement.htm"); 
			$('#inputForm').ajaxSubmit({
				type: "post",
				success: function(data){
	                if(data == "\"success\"" || data == "success"){
	                	alert("提交成功");
	                	
	                	/*$('input.name').val("");
	                	$('input.picAttr').val("");
	                	$('input.actName').val("");
	                	$('input.link').val("");
	                	$('input.startdate').val("");
	                	$('input.enddate').val("");
	                	$('input.picSrc').val("");
	                	$('input.status').val("");*/
	                	
	                	window.location.href=domain+"/cmstemplet/listPicElement.htm?positionId="+$('#positionId').val()+"&pageName="
	                		+$('#pageName').val()+"&templeName="+$('#templeName').val()+"&positionName="+$('#positionName').val()
	                		+"&pageNameBak="+$('#pageNameBak').val()+"&templeNameBak="+$('#templeNameBak').val()
	                		+"&positionNameBak="+$('#positionNameBak').val();
				   	       
	                }else{
	                	alert("提交失败");
	                }
		          }
			});
		});
	};
	
	//移除
	$('table.customertable a.delLocalActivity').unbind().bind('click',function(){
		var params={}; 
		params.id = $(this).parent().parent().find('td span.picId').text();
		$(this).parent().parent().remove();
		$.post(domain +"/cmstemplet/delPicElement.htm", {params:$.toJSON(params)}, function(redata){
	           if(redata.isSuccess){//移除成功
	        	   alert("移除成功");
			   }else{
				   alert(redata.message);
			   }
	        }, "json");
	});
	
	//修改
	$('table.customertable a.editAtt').unbind().bind('click',function(){
		$('#picElementId').val($(this).parent().parent().find('td span.picId').text());
		$('#picId').val($(this).parent().parent().find('td span.picId').text());
		$('#name').val($(this).parent().parent().find('td span.name').text());
		$('#attr').val($(this).parent().parent().find('td span.attr').text());
		$("#actName").val($(this).parent().parent().find('td span.actName').text());
		$('#link').val($(this).parent().parent().find('td span.link').text());
		$('#startdate').val($(this).parent().parent().find('td span.startdate').text());
		$('#enddate').val($(this).parent().parent().find('td span.enddate').text());
		$('#picSrc').val($(this).parent().parent().find('td span.picSrc').text());
		$('#rollPicSrc').val($(this).parent().parent().find('td span.rollPicSrc').text());
		$('#status').val($(this).parent().parent().find('td span.statusCode').text());
		$('#actType').val($(this).parent().parent().find('td span.actType').text());
		$('#activityId').val($(this).parent().parent().find('td span.activityId').text());
		$('#sku').val($(this).parent().parent().find('td span.sku').text());
	});
		
	//创建
	$('#createBtn').unbind().bind('click',function(){
		$('#picElementId').val("");
		$('#picId').val("");
		$('#name').val("");
		$('#attr').val("");
		$("#actName").val("");
		$('#link').val("");
		$('#startdate').val("");
		$('#enddate').val("");
		$('#picSrc').val("");
		$('#status').val("");
		$('#actType').val("");
		$('#activityId').val("");
		$('#sku').val("");
	});
	
	$('#imgPrivewsrc').unbind().bind('click',function(){
		var imgSrc = $("#imgPrivewsrc").attr("src");
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
	
	$('#rollPrivewsrc').unbind().bind('click',function(){
		var imgSrc = $("#rollPrivewsrc").attr("src");
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
		$('#rollPicSrc').val("");
	});
	
	// pc端图片上传
	$("#rollImageImg").on("click",function(){
		topicClickImgBind("查看卷帘图片");
	});
	
	function topicClickImgBind(title){
		var url = domain +"/cmstemplet/upload/Image.htm";
		$.layer({
			type : 2,
			title : "上传卷帘图片",
			maxmin : true,
			fix : false,
			area: ['400px', 300],
			iframe : {
				src : url
			}
		});
		/*var picUrl = $("#" + controlName).val();
		var model=$("#viewModel").val();
		var width="100px";
		var height = 100;
		var typeValue = "0";
		if("view" == model){
			typeValue=$("#typeValue").val();
		} else {
			typeValue=$("#type").val();
		}
		if("pcImage" == controlName){
			if("1" == typeValue){
				width = "303px";
				height = 380;
			} else {
				width = "790px";
				height = 245;
			}
		} else if("newImage" == controlName){
			width = "264px";
			height = 330;
		} else if("interestedImage" == controlName){
			width = "375px";
			height = 180;
		} else if("hitaoImage" == controlName){
			width = "564px";
			height = 345;
		} else if("phoneImage" == controlName){
			if("1" == typeValue){
				width = "608px";
				height = 470;
			} else {
				width = "608px";
				height = 420;
			}
		}
		if(null != picUrl && "" != $.trim(picUrl)){
			picUrl = $("#" + controlName + "Img").attr("src");
			$.layer({
				type : 2,
				title : title,
				shadeClose : true,
				maxmin : true,
				fix : false,
				area: [width, height],
				iframe : {
					src : picUrl
				}
			});
		} else if("view" != model){
			var url = UPLOAD_TOPIC_PIC.replace("{topicControlName}",controlName);
			$.layer({
				type : 2,
				title : "上传活动图片",
				maxmin : true,
				fix : false,
				area: ['400px', 300],
				iframe : {
					src : url
				}
			});
		}*/
	}
	
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