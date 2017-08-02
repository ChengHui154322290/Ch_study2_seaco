$(function(){
	$(document.body).on('click','.deliverygoodbtn',function(){
		if(window.confirm('你确定要进行强制发货？ 确认前请仔细查看注意事项')){
         }else{
            return false;
         }
		var deliverylist = $('.deliverygoodlist').val();
		var deliverylist = deliverylist.split(/\r|\n/);
		if(deliverylist.length<1){
			alert('请输入要强制发货的信息');
			return;
		}
		if(deliverylist.length>100){
			alert('请减少要强制发货的信息到100行');
			return;
		}
		var deliverygooddiv = $('.deliverygooddiv');
		deliverygooddiv.empty();
		var isSubmit = true;
		$(deliverylist).each(function(){
			var index = deliverygooddiv.find('input[name$=subOrderCode]').size();
			var delivery = this.split(/\s+/);
			if(delivery.length==1){
				
			}else if(delivery.length!=4){
				alert('第'+(index+1)+'行有数据不符合格式:'+delivery);
				isSubmit=false;
				return;
			}else{
				$('<input type="hidden" name="deliverList['+index+'].subOrderCode" value="'+$.trim(delivery[0])+'"/>').appendTo(deliverygooddiv);
				$('<input type="hidden" name="deliverList['+index+'].companyName" value="'+$.trim(delivery[1])+'"/>').appendTo(deliverygooddiv);
				$('<input type="hidden" name="deliverList['+index+'].companyId" value="'+$.trim(delivery[2])+'"/>').appendTo(deliverygooddiv);
				$('<input type="hidden" name="deliverList['+index+'].packageNo" value="'+$.trim(delivery[3])+'"/>').appendTo(deliverygooddiv);
			}
		});
		if(isSubmit){
			$('.forcedelivergoodsform').submit();
		}
	});
})
