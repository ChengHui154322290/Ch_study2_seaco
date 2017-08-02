var pageii;
$(function(){
/**新增优惠券信息**/
$('.couponAddBtn').live('click',function(){		
		window.location.href = domain+'/coupon/issueForm';
	}); 



/**编辑优惠券信息**/
$('a.couponeditbtn').live('click',function(){	
		var cid = $(this).attr("param");
		window.location.href = domain+'/coupon/toEditCouponSend?id='+cid;
	}); 

/**查看优惠券信息**/
$('a.couponviewbtn').live('click',function(){	
	var cid = $(this).attr("param");
	window.location.href = domain+'/coupon/toViewCouponSend?id='+cid;
}); 

/** 优惠券设为终止*/
$('a.coupon_stop').live('click',function(){	
	var _this = $(this);
	  if(confirm("是否继续终止？")){
		  var cid = $(this).attr("param");
		  var url = domain + "/coupon/stop_coupon_send";
		  $.ajax({
			  url: url,
			  data: {couponSendId:cid},
			  success: function(data){
				  if(data){
					  _this.parent().prev().html("已终止");
					 // alert("终止成功！");
					  _this.remove();
				  }else{
					  alert("终止失败！");
				  }
			  }
		  });
	  }
}); 

/** 优惠券设为驳回*/
$('a.coupon_refused').live('click',function(){	
	var _this = $(this);
	var pa= _this.parent();
	if(confirm("是否继续驳回？")){
		var cid = $(this).attr("param");
		var url = domain + "/coupon/refused_coupon_send";
		$.ajax({
			url: url,
			data: {couponSendId:cid},
			success: function(data){
				if(data){
					_this.parent().prev().html("已驳回");
					_this.prev().remove();
					_this.remove();
					var temp = "<a style='padding-right:5px;'  class='editcatabtn couponeditbtn' param='" + cid +"'  href='javascript:void(0);'>[编辑]</a>";
					temp = temp +  "<a style='padding-right:5px;'  class='coupon_cancel ' param='" + cid +"'  href='javascript:void(0);'>[取消]</a>";
					pa.html(temp + pa.html());
				}else{
					alert("驳回失败！");
				}
			}
		});
	}
}); 



/** 优惠券设为通过*/
$('a.coupon_approve').live('click',function(){	
	var _this = $(this);
	var pa= _this.parent();
	if(confirm("是否审核批准？批准后优惠券会被发放!")){
		var cid = $(this).attr("param");
		var url = domain + "/coupon/approve_coupon_send";
		$.ajax({
			url: url,
			data: {couponSendId:cid},
			success: function(data){
				
				/*if (data.success) {
					_this.parent().prev().html("审核通过");
					_this.next().remove();
					_this.remove();
				} else {
					layer.alert(data.data);
				}*/
				
			}
		});
		_this.parent().prev().html("审核通过");
		_this.next().remove();
		_this.remove();
	}
}); 
/** 优惠券设为取消*/
$('a.coupon_cancel').live('click',function(){	
	var _this = $(this);
	if(confirm("是否取消？")){
		var cid = $(this).attr("param");
		var url = domain + "/coupon/cancel_coupon_send";
		$.ajax({
			url: url,
			data: {couponSendId:cid},
			success: function(data){
				if(data){
					_this.parent().prev().html("已取消");
					_this.prev().remove();
					_this.remove();
				}else{
					alert("取消失败！");
				}
			}
		});
	}
}); 

	$("#searthAtt").on('click',function(){
		var pattern=/^[1-9]\d*$/;
		if( $("input[name='id']").val() != null && $("input[name='id']").val() !="" &&  !pattern.test($("input[name='id']").val())){
			alert("请输入正确的批次号。");  
			return false;
		}else{
			queryAttForm.submit();     
		}  
	});
}); 