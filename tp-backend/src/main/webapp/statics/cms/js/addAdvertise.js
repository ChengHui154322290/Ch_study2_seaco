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
			advertinfo.nameBak = encodeURI(encodeURI($('#nameBak').val()));
			advertinfo.positionBak = $('#positionBak').val();
			advertinfo.startdateBak = $('#startdateBak').val();
			advertinfo.enddateBak = $('#enddateBak').val();
			advertinfo.typeBak = $('#typeBak').val();
			advertinfo.statusBak = $('#statusBak').val();
   	        window.location.href=domain+"/cmsAdvertIndex/listAdvertiseTemp.htm?advertinfo="+$.toJSON(advertinfo);  
   	        
		});
	};
	
	PFUNC.fileupload = function() {
		$('button.kv-fileinput-upload').unbind().bind('click',function(){
			alert($("div.file-caption-name").eq(0).text());
			var imgPath = $("div.file-caption-name").eq(0).text();
            if (imgPath == "") {
                alert("请选择上传图片！");
                return;
            }
            //判断上传文件的后缀名
            var strExtension = imgPath.substr(imgPath.lastIndexOf('.') + 1);
            if (strExtension != 'jpg' && strExtension != 'gif' && strExtension != 'png' && strExtension != 'bmp') {
                alert("请选择图片文件");
                return;
            }
            var params = {};
			params.imgPath = $("input.announce_id").val();
            /*$.post(domain +"/cmsAdvertIndex/uplodeAdvertise.htm", {params:$.toJSON(params)}, function(redata){
		           
	        });*/
		});
	};
	PFUNC.inputFormSaveBtn = function() {
		$('#inputFormSaveBtn').live('click',function(){
			if($("input[name='picList']").eq(1).val() != undefined && 
					$("input[name='picList']").eq(1).val()  != "" && 
					$("input[name='picList']").eq(1).val()  != null){
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
	
	//点击查询，弹出图片类型查询框
	$('#advert_type_query').unbind().bind('click',function(){
		$("tr.temp_dev_tr_advertise").remove();   
		layerpopreceive=window.open("advert_type_pop","图片类型",function(){      
			},['550px', '450px'],true);
	});
	
	$('#picSizeRemark').unbind().bind('click',function(){
		layer.tips('首页-轮播图：1920*325<br>首页-最优惠（右下方）：宽度395，高度不限<br>最后疯抢-最优惠（右下方）：宽度395，高度不限<br>登陆页面-左侧：550*350<br>'+
				'App首页轮播图：640*380<br>App商城广告图：640*120', this, {
		    style: ['background-color:#78BA32; color:#fff', '#78BA32'],
		    maxWidth:300,
		    time: 5,
		    closeBtn:[0, true]
		});
	});
	
	/**********************************  一下是弹出框事件    *******************************************************/
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
		$.post(domain +"/cms/queryAdvertType.htm", {params:$.toJSON(params)}, function(redata){
				pageflag=true;
	           if(redata.isSuccess){//添加成功
	        	   var value = redata.data;
			        	$.each(value,function(i,val){  
			        		var tr = PCACHE.temp_dev_tr_advertise.clone(true);
			        		$(tr).find("span.pop_dev_name").text(Putil.toNull(val.name));  
			 				$(tr).find("span.pop_dev_ident").text(Putil.toNull(val.ident));  
			 				$(tr).find("span.pop_dev_id").text(val.id); 
			 				
			 				//添加单击和双击事件
				        	$(tr).find("span").unbind().bind("click",function(){ 	
				        		$("#type").val(Putil.toNull(val.name));
				        		$("#ident").val(Putil.toNull(val.ident));
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

/**
 * 改变平台标识会出发此事件
 */
function oncl(){
	if($('select.platformType').val() == "1"){
		$('div.appRemark').removeAttr("style");
	}else{
		$('div.appRemark').attr("style","display:none");
	}
}

//页面初始化
$(document).ready(function()
{
	// 返回事件
	PFUNC.retpar.call(this);
	
	PCACHE.temp_dev_tr_advertise=$("tr.temp_dev_tr_advertise").clone(true);
	// 上传事件
	//PFUNC.fileupload.call(this);
	
	// 提交事件
	PFUNC.inputFormSaveBtn.call(this);
	
	// 控制界面的输入框只能输入数字问题
	PFUNC.inputFormValidate.call(this);
	
	//$('#startdate').datepicker(); 
	//$('#enddate').datepicker(); 
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
	
	if($('select.platformType').val() != "1"){
		$('div.appRemark').attr("style","display:none");
	}
	
});	