$(document).ready(function() {
	
	
	$(document.body).on('click','#cancleCoupon',function(){
        var  ids=$("#cancleExchangeIds").val();
        var couponCodeIdList ="";
        if($("#cancleReason").val()==""){
        	layer.alert("请选择作废原因！");
        	return false;
        }
        couponIds=ids.split(",");
		 for(var i=0;i<couponIds.length;i++){
			 couponCodeIdList=couponCodeIdList+"&couponCodeIdList["+i+"]="+couponIds[i]; 
		 }
		$.ajax({
            cache: true,
            type: "POST",
            url:domain + '/topicCoupon/cancleCouponPromoterid?'+couponCodeIdList+'&cancelReason='+$("#cancleReason").val(),
            data:{},
            async: false,
            error: function(request) {
            	layer.alert("系统异常");
            },
            success: function(result) {
            	if(result.success){
            		layer.alert("作废成功！");
            		setTimeout(function(){
            			$(window.parent.document).find('form#change_coupon_code_list_form').submit();
            		},1000)
            		
            	}else{
            		layer.error(result.msg.message, 8);
            	}
            }
        });
	 });
	
});