$(function(){
	//审批
	$(".auditoffset").click(function(){
		var success = $(this).attr('success');
		var offsetId = $(this).attr('offsetId');
		var remarks = $(this).attr('input[name=auditRemarks]');
		var auditType = $(this).attr('auditType');
		if("finalaudit" == auditType && $("#serialNo").val().length < 1 && success == "true"){
			$("#serialNoTip").addClass("onError").text("交易流水号不能为空");
			return false;
		}
		else{
			$("#serialNoTip").removeClass("onError").text("");
		}
		$.ajax({
			url:domain+'/customerservice/offset/'+auditType+'.htm',
			data:{offsetId:offsetId,success:success,remarks:remarks,'serialNo':$("#serialNo").val(),couponCode: $("#couponCode").val()},
			dataType:'json',
			type:'post',
			success:function(result){
				if(result.success){
					 layer.alert("成功",1, function(){
			            	parent.window.location.reload();
			            	parent.layer.close(parent.pageii);
			                });
				}else{
					layer.alert(result.msg.message, 8);
				}
			}
		});
	});
	
	$('.container').on('click','.applyoffsetshowbtn',function(){
		var offsetId = $(this).attr('offsetid');
		var offsetNo = $(this).attr('offsetno');
		var operate = $(this).attr('operate');
		var auditType = $(this).attr('auditType');
		  pageii=$.layer({
            type : 2,
            title: '补偿管理',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['70%', '80%'],                     
            iframe: {
                src : domain+'/customerservice/offset/'+auditType+"/"+operate+'.htm?offsetId='+offsetId+'&offsetno='+offsetNo
            } 
        });
	}); 
	$(document.body).on('click','.searchorderbtn',function(){
		$.ajax({
			url:domain+'/customerservice/offset/order.htm',
			data:{orderCode:$('input:text.offsetorder').val()},
			dataType:'html',
			type:'post',
			success:function(result){
				$('.offsetapplyorderinfobody').html(result);
			}
		})
	});
	$(document.body).on('click','.exportoffsetlist',function(){
		window.open(domain+'/customerservice/offset/exporttemplate.htm?'+$('#offsetlistform').serialize());
//		$.ajax({
//			url:domain+'/customerservice/offset/exporttemplate.htm',
//			data:$('.offsetapplyform').serialize(),
//			success:function(result){
//				console.log(result);
//					 layer.alert("成功", 1);
//			},
//			error: function(e,i){
//				console.log(e);
//				console.log(i);
//				layer.alert("失败", 8);
//			}
//		});
	});
	$(document.body).on('click','.importoffsetlist',function(){
		var sdfsdew=$.layer({
            type : 2,
            title: '导入补偿列表',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['400px', 200],                     
            iframe: {
                src : domain+'/customerservice/offset/exportfile.htm'
            },
            onSuccess: function(){
            	layer.alert("成功", 1);
            }
        });
	});
	$(':radio[name=offsetType]').on('click','.form_table',function(){
		var offsetType = $(this).val();
		if(offsetType==1){
			$('table.form_table').removeClass('cachoffset').addClass('cachoffset');
		}else{
			$('table.form_table').removeClass('cachoffset');
		}
	})
	if($(".applyoffsetbtn").is(":button")){
		var butt=$("#applyoffsetbtn");
		$.formValidator.initConfig({
			submitButtonID:'applyoffsetbtn',
			debug:false,
			onSuccess:function(butt){
				$.ajax({
					url:domain+'/customerservice/offset/apply.htm',
					data:$('.offsetapplyform').serialize(),
					dataType:'json',
					type:'post',
					success:function(result){
						if(result.success){
							 layer.alert("成功", 1, function(){
						        	parent.window.location.reload();
						        	parent.layer.close(parent.pageii);
						    });
						}else{
							layer.alert(result.msg.message, 8);
						}
					}
				})
			},
			onError:function(){
				layer.alert('失败', 8);
			}
		});
		/**$("#orderNo").formValidator({onEmpty:"请选择正确的订单进行补偿",onCorrect:"正确"}).inputValidator({min:12,max:16,onError:"请选择正确的订单进行补偿"});
		*/
//		$("input:radio[name=offsetType]").formValidator({tipID:"offsetTypeTip",onShow:"请选择补偿方式",onFocus:"请选择补偿方式",onCorrect:"正确",defaultValue:["1"]}).inputValidator({min:1,max:1,onError: "请选择补偿方式"});
//		$("#offsetReason").formValidator({onShow:"请选择补偿原因",onFocus:"请选择补偿原因",onCorrect:"正确"}).inputValidator({min:1,max:1,onError: "请选择补偿原因"});
		$("#offsetAmount").formValidator({onShow:"请填写补偿金额",onFocus:"请填写补偿金额",onEmpty:"请填写补偿金额"}).regexValidator({regExp:"(^[1-9][0-9]*(\.[0-9]{1,2})?$)|(0(\.[0-9]{1,2})?$)", dataType:"string",onError:"补偿金额格式为xxxx.xx"});
//		$("#bear").formValidator({tipID:"bearTip",onFocus:'请选择补偿承担',onCorrect:"正确",defaultValue:["1"]}).inputValidator({min:1,max:1,onError: "请选择补偿承担"});
		$('.cachoffset #payee').formValidator({onFocus:'请填写收款人',onEmpty:"请填写收款人",onCorrect:"正确"}).inputValidator({min:2,max:20,onError:"收款人错误,请确认"});
		$('.cachoffset #payeeBank').formValidator({onFocus:'请填写收款人银行',onEmpty:"请填写收款人银行",onCorrect:"正确"}).inputValidator({min:4,max:24,onError:"收款人银行错误,请确认"});
		$('.cachoffset #payeeMobile').formValidator({onFocus:'请填写收款人手机',onEmpty:"请填写收款人手机",onCorrect:"正确"}).regexValidator({regExp:"phone", dataType:"enum",onError:"请填写正确的手机号"});
		console.log($('#serialNo'));
	}
	else if($('.offsetapplyorderinfobody').is('tbody')){
		$('input:text,textarea').not(".audit").each(function(){
			var val = $(this).val();
			$(this).parent().html(val);
		});
		$('input:radio').not(".show").each(function(){
			var val = '';
			if(null!=$(this).attr('checked')){
			val = $(this).attr('text');
			}
			$(this).parent().html(val);
		});
		$('select').each(function(){
			var val = $(this).find(':selected');
			$(this).parent().html(val.text());
		});
	}
	 $(".list_table").colResizable({
         liveDrag:true,
         gripInnerHtml:"<div class='grip'></div>", 
         draggingClass:"dragging", 
         minWidth:30
       }); 
});	