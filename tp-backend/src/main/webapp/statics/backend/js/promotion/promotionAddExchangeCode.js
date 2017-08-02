var PFUNC ={};
var PCACHE  ={};
var layerpopreceive;

var success_code = 1;
var pageii;

/**
 * 页面事件注册
 */
$(function(){

	PFUNC.loadExchangeCodeInfo = function () {
		var actId = $("input[name='actId']").val();
		if(actId!=null && actId !=""){
			$("#exchangeDetail").load("/topicCoupon/exchangeCodeDetail.htm",{"actId":actId});

		}
	}
	
	PFUNC.retpar = function() {
		$('#returnPage').unbind().bind('click',function(){
			
			//这个是把查询条件带过去
			var advertinfo={}; 
			advertinfo.actNameBak = $('#actNameBak').val();
   	        window.location.href=domain+"/topicCoupon/listStaticActivity.htm?advertinfo="+$.toJSON(advertinfo);
		});
	};
	
	PFUNC.inputFormSaveBtn = function() {
		$('#genertExchangeCode').live('click',function(){
			$("#genertExchangeCode").attr('disabled',true);
			if(Putil.isEmpty($("input:hidden[name=couponId]").val())){
				alert("请填写优惠券批次号");
				$("#genertExchangeCode").attr('disabled',false);
				return;
			}
			if(Putil.isEmpty($("input.currentnum").val())){
				alert("请填写兑换码数量");
				$("#genertExchangeCode").attr('disabled',false);
				return;
			}
			if(parseInt($("input.currentnum").val()) > 50000){
				alert("兑换码数量不能超过50000，例如需要生成100000条兑换码，需要分2次生成");
				$("#genertExchangeCode").attr('disabled',false);
				return;
			}
			var index = layer.load("请稍等...");
			$("#inputForm").attr("action",domain +"/topicCoupon/addExchangeCode.htm"); 
			$('#inputForm').ajaxSubmit(function(data){
				layer.close(index);
				if(data.success){
                	alert("生成成功");
                	$("#genertExchangeCode").attr('disabled',false);
					PFUNC.loadExchangeCodeInfo.call(this);
                }else{
                	alert(data.msg.message);
                	$("#genertExchangeCode").attr('disabled',false);
                }
	         });
		});
		
		/**
		 * 导出excel
		 */
	/*	$('#exportData').unbind().bind('click',function(){
			$('#inputForm').attr("action",domain +"/topicCoupon/export.htm"); 
			$('#inputForm').submit();
		}); */
		
		$('#sumbitExchangeCode').live('click',function(){
			if(Putil.isEmpty($("input.actName").val())){
				alert("请填写活动名称");
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
			if(Putil.isEmpty($("input.num").val())){
				alert("请填写兑换码总数");
				return;
			}
			//表单添加url
			$("#inputForm").attr("action",domain +"/topicCoupon/saveActExchangeCode.htm"); 
			$('#inputForm').ajaxSubmit(function(data){
				if(data.success){
                	alert("提交成功");
                	
                	var advertinfo={}; 
        			advertinfo.actNameBak = $('#actNameBak').val();
           	        window.location.href=domain+"/topicCoupon/listStaticActivity.htm?advertinfo="+$.toJSON(advertinfo); 
			   	       
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
			$("input.couponId").keyup(function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).bind("paste",function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).css("ime-mode", "disabled");    
			
			$("input.num").keyup(function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).bind("paste",function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).css("ime-mode", "disabled");   
			
			$("input.currentnum").keyup(function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).bind("paste",function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).css("ime-mode", "disabled");  
			
	};
	
	
});

//导出弹出框
$(function() {
	$('.codeExport').live('click', function () {
		var actiId = $(".actId").val();
					pageii = $.layer({
				type: 2,
				title: '导出兑换码',
				shadeClose: true,
				maxmin: true,
				fix: false,
				area: ['1000px', 600],
				iframe: {
					src: domain + "/topicCoupon/exChangeCodeSel.htm?actiId="+actiId
				}
			});
	});
	//取消按钮
	$('.closebtn').on('click', function () {
		parent.layer.close(parent.pageii);
	});
});

/*function exportExcel(button){
	parent.layer.close(parent.pageii);
	var address= $(button).attr('param');
	$.ajax({
		cache: true,
		type: "POST",
		url:domain+address,
		data:$('#exportData').serialize(),
		async: false,
		error: function(request) {
			alert("Connection error");
		},
		success: function(data) {
		/!*	if(data.resultInfo.type=="1"){*!/
				/!*layer.alert("success", 1, function(){*!/
					/!*parent.window.location.reload();*!/

				/!*})*!/
			/!*}else{
				layer.alert(data.resultInfo.message, 8);
			}*!/
		}
	});
}*/


function uploadExcel(){
	$('#inputForm').attr("action",domain +"/topicCoupon/export.htm");
	$('#inputForm').submit();
}
//页面初始化
$(document).ready(function()
{
	// 返回事件
	PFUNC.retpar.call(this);
	
	// 提交事件
	PFUNC.inputFormSaveBtn.call(this);
	
	// 控制界面的输入框只能输入数字问题
	PFUNC.inputFormValidate.call(this);

	PFUNC.loadExchangeCodeInfo(this);
	
	$(".startdate").datetimepicker({
		showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
	});

	$(".enddate").datetimepicker({
		showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
	});
});	