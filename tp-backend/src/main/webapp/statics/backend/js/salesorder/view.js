$(document).ready(function() {

	var TRACK_URL = domain + "/order/track.htm";
	var LOG_URL = domain + "/order/log.htm";
	
	// 全程跟踪
//	$("#order-track").click(function() {
//		
//	});

	// 订单日志
	$("#order-log").click(function() {
		$.layer({
			type : 2,
			title: '订单日志',
			shadeClose: true,
			maxmin: true,
			fix : false,  
			area: ['1024px', 500],                     
			iframe: {
				src : domain + "/order/log.htm?subCode=" + $(this).attr("data_code")
			} 
		});
	});

	// 物流跟踪
	$("#order-track").click(function() {
		$.layer({
			type : 2,
			title: '物流跟踪',
			shadeClose: true,
			maxmin: true,
			fix : false,  
			area: ['1024px', 500],                     
			iframe: {
				src : domain + "/order/track.htm?subCode=" + $(this).attr("data_code")
			} 
		});
	});
	// 海外直邮订单操作日志
	$("#directOrder-log").click(function() {
		$.layer({
			type : 2,
			title: '订单操作日志',
			shadeClose: true,
			maxmin: true,
			fix : false,  
			area: ['1024px', 500],                     
			iframe: {
				src : domain + "/order/directlog.htm?subCode=" + $(this).attr("data_code")
			} 
		});
	});
	// 优惠券信息
	$("#order-coupon").click(function() {
		$.layer({
			type : 2,
			title: '优惠券信息',
			shadeClose: true,
			maxmin: true,
			fix : false,  
			area: ['1024px', 500],                     
			iframe: {
				src : domain + "/order/coupon.htm?subCode=" + $(this).attr("data_code")
			} 
		});
	});
	
	// 清关状态
	$("#order-clearance").click(function() {
		$.layer({
			type : 2,
			title: '清关状态',
			shadeClose: true,
			maxmin: true,
			fix : false,  
			area: ['1024px', 500],                     
			iframe: {
				src : domain + "/order/clearance.htm?subCode=" + $(this).attr("data_code")
			} 
		});
	});
	
	
	//手动推送
	$("#order-manual-push").click(function(){
		var orderCode = $(this).attr("data_code");
		layer.confirm("手动推送海关仅用于测试阶段，是否继续？", function() {
			$.ajax({
				url:domain + '/order/manual_push.htm',
				type:'get',
				data:'subCode=' + orderCode,
				cache:false,
				success:function(data){
					if(data.success){
						layer.alert('推送成功', 4, function(){
							location.href = '/order/view.htm?code=' + orderCode;
						});
					} else{
						alert(data.msg.message, 8);
						window.location.reload();
					}
				}
			});
		}, function() {});
	});
	
	//手动推送第三方海外直邮订单
	$("#order-direct-push").click(function(){
		var orderCode = $(this).attr("data_code");
		layer.confirm("手动推送海外直邮仅用于测试阶段，是否继续？", function() {
			$.ajax({
				url:domain + '/order/direct_push.htm',
				type:'get',
				data:'subCode=' + orderCode,
				cache:false,
				success:function(data){
					if(data.success){
						layer.alert('推送成功', 4, function(){
							location.href = '/order/view.htm?code=' + orderCode;
						});
					} else{
						alert(data.msg.message, 8);
						window.location.reload();
					}
				}
			});
		}, function() {});
	});
		
	//手动推送仓库
	$("#order-saleout").click(function(){
		var orderCode = $(this).attr("data_code");
		layer.confirm("手动推送仓库仅用于测试阶段且只有清关通过的订单才能推送，是否继续？", function() {
			$.ajax({
				url:domain + '/order/manual_saleout.htm',
				type:'get',
				data:'subCode=' + orderCode,
				cache:false,
				success:function(data){
					if(data.success){
						layer.alert('推送成功', 4, function(){
							location.href = '/order/view.htm?code=' + orderCode;
						});
					} else{
						alert(data.msg.message, 8);
						window.location.reload();
					}
				}
			});
		}, function() {});
	});
	
	$(document.body).on("click",".fastdeliveryorderbtn",function() {
		var code = $(this).attr("data_code");
		var htmlData = [];
		htmlData.push('<table class="form_table pt15 pb15 deliveryuserinfotable" style="width:395px;"><body>');
		htmlData.push('<tr><td>配送人员：</td>');
		htmlData.push('<td>'+$('.fastuserinfodiv').html()+'</td></tr>');
		htmlData.push('<tr><td>备注：</td><td><input type="text" class="input-text lh30" name="deliveryremark"/></td></tr>');
		htmlData.push('<tr><td>　</td><td> </td></tr>');
		htmlData.push('<tr><td></td><td><input type="button" data_code="'+code+'" class="btn btn82 btn_save2 deliveryusersavebtn" value="确定"/></td></tr>');
		htmlData.push('</body></table>');
		var _html=htmlData.join('');
			
		var pageii2 = $.layer({
			  type: 1,
			  title: '请选择配送人员',
			  area: [400, 400],
			  shadeClose: true,
			  maxmin: true,
			  fix : false,  
			  page: {
			    html: _html
			  }
			});
		
	});
	$(document.body).on('click','.deliveryusersavebtn',function(){
		var code = $(this).attr("data_code");
		var deliveryuserinfo = $('.deliveryuserinfotable .fastuserinfo').val();
		var fastuserid = $('.deliveryuserinfotable .fastuserinfo :selected').attr('fastuserid');
		var remark = $('.deliveryuserinfotable [name=deliveryremark]').val();
		
		if(deliveryuserinfo==null || $.trim(deliveryuserinfo)==''){
			layer.alert('请选择配送人员');
			return false;
		}
		var content=deliveryuserinfo+($.trim(remark).length>0?(' '+remark):'');
		 $.ajax({
			  url:domain+'/order/deliveryorder',
			  data:{orderCode:code,content:content,fastUserId:fastuserid},
			  dataType:'json',
			  type:'post',
			  success:function(result){
				  if(result.success){
					  layer.msg('已配送',0);
					  location.reload();
				  }else{
					  layer.alert('配送失败：'+result.msg.message);
				  }
			  }
		  });
	});
	$(document.body).on("click",".fastreceivingorderbtn",function() {
		var code = $(this).attr("data_code");
		layer.confirm('确认要接单？',function(){
		  $.ajax({
			  url:domain+'/order/receivingorder',
			  data:{orderCode:code},
			  dataType:'json',
			  type:'post',
			  success:function(result){
				  if(result.success){
					  layer.msg('已接单',0);
					  location.reload();
				  }else{
					  layer.msg('接单失败：'+result.msg.message);
				  }
			  }
		  });
		}, function(){});
	});
	if($('.fastorderovertime').size()>0){
		setInterval("timer()",60000);
	}
	
	
});
function timer(){
	var overTime = $('.fastorderovertime').text();
	overTime = parseInt(overTime);
	if(overTime<1){
		$('.fastorderoverspan').html('超时:<span class="fastorderovertime">'+(--overTime)+'</span>分钟');
	}else{
		$('.fastorderovertime').text(--overTime);
	}
	var color = (256-(overTime*2))+",00,"+(overTime*2);
	if(overTime<1){
		color = "255,0,0";
	}
	$('.fastorderoverspan').css("color","rgb("+color+")");
}
