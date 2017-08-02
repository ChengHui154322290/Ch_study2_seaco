$(function(){
	
	$(document.body).on('click','.editCommisionRateBtn', function() {
		var current_btn = $(this);
		var skuId = $(this).attr('skuId');
		var commisionRate = $(this).attr('commisionRate');
		var isUpdate = $(this).attr('isUpdate');
		if(isUpdate == 0){
			$("#CRZ0 td[value='"+skuId+"']").html("<input type='text' id='rate"+skuId+"' name='commisionRate' value='"+commisionRate+"'  class='input-text lh25' size='20'>");
			current_btn.attr('value',"确定");
			current_btn.attr('isUpdate',"1");
		}else if(isUpdate == 1){
			var newCommisionRate = $("#rate"+skuId).val();
			var commisionType=current_btn.parent().parent().find('select[name=commisionTypeSelect]').val();
			if(newCommisionRate != null || newCommisionRate != ''){
				layer.confirm('您确认修改分销佣金比例么?', function(){
					$.ajax({
						url:domain + '/item/commisionRate/updateRate',
						data:{id:skuId,commisionRate:newCommisionRate,commisionType:commisionType},
						type:'post',
						dataType:'json',
						success:function(result){
							if(result.success){
								pageii =layer.alert("修改成功", 1,function(){
									layer.close(pageii);
									current_btn.attr('isUpdate',"0");
									current_btn.attr('commisionRate',newCommisionRate);
									current_btn.attr('value',"修改");
									$("#CRZ0 td[value='"+skuId+"']").html(newCommisionRate);
				                })
							}else{
				        		layer.alert(result.msg.message, 8);
				        	}
						}
					});
				},function(){});
			}
		}
	});
});