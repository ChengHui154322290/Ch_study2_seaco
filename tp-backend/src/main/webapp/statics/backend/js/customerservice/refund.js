$(function(){
	$(":text[name='createTimeBegin']").datepicker();
	$(":text[name='createTimeEnd']").datepicker();
	$('.refundlistdiv').on('click','.showrefundinfo',function(){
		var operate = $(this).attr('operate');
		var refundId = $(this).attr('refundid');
		pageii=$.layer({
            type : 2,
            title: '退款信息',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['1000px', 650],                     
            iframe: {
                src : domain+'/customerservice/refund/'+operate+'.htm?refundId='+refundId
            } 
        });
	});
	$('.operatebtn').on('click','.financeauditbtn',function(){
		var refundId = $(this).attr('refundid');
		$.ajax({
			url:domain+'/customerservice/refund/financeaudit.htm',
			data:{refundId:refundId},
			dataType:'json',
			type:'post',
			success:function(result){
				if(result.success){
					layer.alert('退款指令成功发出',8);
				}else{
					layer.alert('退款失败',8);
				}
			}
		});
	}); 
	$(".list_table").colResizable({
        liveDrag:true,
        gripInnerHtml:"<div class='grip'></div>", 
        draggingClass:"dragging", 
        minWidth:30
      });
	$(".auditrefund").click(function(){
		var success = $(this).attr('success');
		var refundId = $(this).attr('refundid');
		var gatewayId = $(this).attr('gatewayId');
		var isAuditComplete = $(this).attr('isAuditComplete');
		
		if(isAuditComplete && !confirm("确定要结束退款吗？")){
			return;
		}
			
		layerShow = layer.load("处理中...");
		$.ajax({
			url:domain+'/customerservice/refund/audit.htm',
			data:{refundId:refundId,success:success,isAuditComplete:isAuditComplete},
			dataType:'json',
			type:'post',
			success:function(result){
				if(result.success){
					if(gatewayId == 1){
						window.open(result.data.returnUrl,'_blank');
						parent.layer.close(parent.pageii);
					}
					else{
						layer.alert("成功",1, function(){
			            	parent.window.location.reload();
			            	parent.layer.close(parent.pageii);
			                });
					}
					 
				}else{
					//如果是阿里支付且在退款中，那么允许继续退款
					if(gatewayId == 1 && result.errorCode == 2){
						window.open(result.returnUrl,'_blank');
						parent.layer.close(parent.pageii);
					}
					else
						layer.alert(result.msg.message, 8);
				}
				layer.close(layerShow);
			}
			
		})
		
		
	});
	
	$(".selectall").click(function(){
		var selectall = $(this);
		$(".refundItem").each(function(){
			if(selectall.hasClass("btn_nochecked")){
				$(this).attr("checked",true);
			}
			else{
				$(this).attr("checked",false);
			}
		});
		if(selectall.hasClass("btn_nochecked"))
			selectall.removeClass("btn_nochecked").addClass("btn_checked");
		else
			selectall.removeClass("btn_checked").addClass("btn_nochecked");
	});
	$(".refundbatch").click(function(){
		if($(".refundItem:checked").length < 1){
			layer.alert("没有选中任何数据",8);
			return false;
		}
		layerShow = layer.load("处理中...");
		$.ajax({
			url: domain+'/customerservice/refund/refundbatch.htm',
			type: 'post',
			data: $(".refundItem:checked").serialize(),
			success: function(result){
				var dialog = layer.alert(result.data,1, function(){
					layer.close(dialog);
	            	window.location.reload();
	            });
			},
			error: function(e){
				layer.close(layerShow);
				layer.alert("失败",8);
			}
		});
	});
	
	$(document.body).on('click','.exportrefundlist',function(){
		window.open(domain+'/customerservice/refund/exporttemplate.htm?'+$('#refundlistform').serialize());
	});
})