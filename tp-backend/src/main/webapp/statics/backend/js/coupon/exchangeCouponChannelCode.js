$(function(){
	$(document.body).on('click','.bingcouponcodetopromoterbtn',function(){
		var beginCodeSeq = $(':text[name=beginCodeSeq]').val();
		var endCodeSeq = $(':text[name=endCodeSeq]').val();
		var couponId = $(':hidden[name=couponId]').val();
		var promoterName = $(':text.promotername').val();
		var promoterId = $(':hidden[name=promoterId]').val();
		var refreshBind = $(this).attr('refreshbind');
		var couponCodeIdList ="";
		$(':checkbox.couponcodeid:checked').each(function(i){
			couponCodeIdList=couponCodeIdList+"&couponCodeIdList["+i+"]="+$(this).val();
		});
		if(couponCodeIdList.length==0){
			couponCodeIdList = "";
		}
		if(promoterName=='' || $.trim(promoterName)=='' || promoterId=='' || $.trim(promoterId)==''){
			layer.alert('请选择要绑定的推广员');
			return false;
		}
		if(couponId=='' || $.trim(couponId)==''){
			layer.alert('请选择要绑定的批次');
			return false;
		}
		if(couponCodeIdList==null){
			if(beginCodeSeq=='' || $.trim(beginCodeSeq)==''){
				layer.alert('请输入要绑定起始序列号');
				return false;
			}
			if(endCodeSeq=='' || $.trim(endCodeSeq)==''){
				layer.alert('请输入要绑定结束序列号');
				return false;
			}
			if(parseInt(beginCodeSeq)>parseInt(endCodeSeq)){
				layer.alert('序列号区间起始不能大于结束');
				return false;
			}
		}
		
		$.layer({
    	    shade: [0],
    	    area: ['auto','auto'],
    	    dialog: {
    	        msg: '你确认要把这些卡券绑定到'+promoterName+' ['+promoterId+']',
    	        btns: 2,                    
    	        type: 4,
    	        btn: ['是的','点错了'],
    	        yes: function(){
    	        	$.ajax({
    	                cache: true,
    	                type: "POST",
    	                url:domain + '/topicCoupon/updatepromoteridbind?'+couponCodeIdList,
    	                data:{beginCodeSeq:beginCodeSeq,endCodeSeq:endCodeSeq,couponId:couponId,promoterId:promoterId,refreshBind:refreshBind},
    	                async: false,
    	                error: function(request) {
    	                	layer.alert("系统异常");
    	                },
    	                success: function(result) {
    	                	if(result.success){
    	                		layer.msg("绑定成功,此次绑定张数:"+result.data, 1,1);
    	                		$('form#change_coupon_code_list_form').submit();
    	                	}else{
    	                		layer.error(result.msg.message, 8);
    	                	}
    	                }
    	            });
    	        }, no: function(){
    	        	layer.msg('休息会吧^_*', 1,12);
    	        }
    	    }
    	});
		
	});
	
	$(document.body).on('click','.disablecouponcodepromoterbtn',function(){
		var beginCodeSeq = $(':text[name=beginCodeSeq]').val();
		var endCodeSeq = $(':text[name=endCodeSeq]').val();
		var couponId = $(':hidden[name=couponId]').val();
		var promoterName = $(':text.promotername').val();
		var promoterId = $(':hidden[name=promoterId]').val();
		var _enabled = $(this).attr('enabled');
		var _enabledShow=("1"==_enabled?"激活":"封存");
		var couponCodeIdList ="";
		$(':checkbox.couponcodeid:checked').each(function(i){
			couponCodeIdList=couponCodeIdList+"&couponCodeIdList["+i+"]="+$(this).val();
		});
		if(couponCodeIdList.length==0){
			couponCodeIdList = "";
		}
		if(couponId=='' || $.trim(couponId)==''){
			layer.alert('请选择要'+_enabledShow+'的批次');
			return false;
		}
		if(couponCodeIdList==null){
			if(beginCodeSeq=='' || $.trim(beginCodeSeq)==''){
				layer.alert('请输入要'+_enabledShow+'起始序列号');
				return false;
			}
			if(endCodeSeq=='' || $.trim(endCodeSeq)==''){
				layer.alert('请输入要'+_enabledShow+'结束序列号');
				return false;
			}
			if(parseInt(beginCodeSeq)>parseInt(endCodeSeq)){
				layer.alert('序列号区间起始不能大于结束');
				return false;
			}
		}
		
		$.layer({
    	    shade: [0],
    	    area: ['auto','auto'],
    	    dialog: {
    	        msg: '你确认要把这些卡券'+_enabledShow,
    	        btns: 2,                    
    	        type: 4,
    	        btn: ['是的','点错了'],
    	        yes: function(){
    	        	$.ajax({
    	                cache: true,
    	                type: "POST",
    	                url:domain + '/topicCoupon/disabledpromoterid?'+couponCodeIdList,
    	                data:{beginCodeSeq:beginCodeSeq,endCodeSeq:endCodeSeq,couponId:couponId,enabled:_enabled},
    	                async: false,
    	                error: function(request) {
    	                	layer.alert("系统异常");
    	                },
    	                success: function(result) {
    	                	if(result.success){
    	                		layer.msg(_enabledShow+"成功,此次"+_enabledShow+"张数:"+result.data, 1,1);
    	                		$('form#change_coupon_code_list_form').submit();
    	                	}else{
    	                		layer.error(result.msg.message, 8);
    	                	}
    	                }
    	            });
    	        }, no: function(){
    	        	layer.msg('休息会吧^_*', 1,12);
    	        }
    	    }
    	});
		
	});
	
	$(document.body).on('click','.allcouponcodebtn',function(){
		if($(this).attr('checked')){
			$(':checkbox.couponcodeid').attr({'checked':true});
		}else{
			$(':checkbox.couponcodeid').removeAttr('checked');
		}
	})
	
  $('#canclecouponcodepromoterbtn').live('click',function(){
	  var cancleCouponCodeIdList ="";
		$(':checkbox.couponcodeid:checked').each(function(i){
			var status=$(this).parents("tr").find('td:eq(10)').text();
			if(status!="未兑换"){
				layer.alert("请选择未兑换的优惠券！");
				return false;
			}
			cancleCouponCodeIdList=cancleCouponCodeIdList+"&couponCodeIdList["+i+"]="+$(this).val();
			
		});
		alert(cancleCouponCodeIdList);
		if(cancleCouponCodeIdList.length==0){
			layer.alert('请选择未兑换的优惠券');
			return false;
		}
	  pageii = $.layer({
			type : 2,
			title : '卡券作废',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 500 ],
			iframe : {
				src : domain+'/topicCoupon/cancleCodeDetail?'+cancleCouponCodeIdList
			}
		});
		 
	});
	

	
	
	
})