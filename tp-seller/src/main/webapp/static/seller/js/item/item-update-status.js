$(function(){
	
	/**
	 * 父选子
	 * 全选/取消全选
	 */
	$('#checkAll').on('click',function() {
		if (this.checked == true) {
			$('#checkAll').attr("checked", true);
			  var isChecked = $(this).prop("checked");
			  $("input[name='skuIdCheckbox']").prop("checked", isChecked);
		} else {
			$('.skuIdCheckbox').attr("checked", false);
		}
	});
	
	
	$('#batchUpdateStatus').on('click',function(){
		var skuIdArry = new Array();
		$(".skuIdCheckbox").each(function(){
			if ($(this).is(':checked')) {
				skuIdArry.push($(this).val());
			}
		});
		//校验
		if(skuIdArry.length==0){
			alert("请先选择商品，再做批量上下架");
			return ;
		}
		var loadi = layer.load('正在更新商品的状态中,请不要关闭…'); 
		
		//var loadi = layer.load('加载中…'); 
		//需关闭加载层时，执行layer.close(loadi)即可
		var status = parent.$("#itemStatus").val();
		var skuIds = skuIdArry.join(",");
		$.post("/seller/item/changeItemStatus.htm",{status:status,skuIds:skuIds},
				function(obj){
					if(status == 0){
						if(obj.success){//成功
							$("#tipMsgId").html("操作成功!");
						}else{
							$("#tipMsgId").html("操作失败："+obj.msg.message);
						}
						
					}else{
						if(!obj.success){//成功
							$("#tipMsgId").html("操作失败："+obj.msg.message);
						}
						$.each($(obj.data),function(i,jsonObj) {
							 var skuId = jsonObj.code;
							 if(jsonObj.cnName=="更新成功"){
								 $("#skuId_"+skuId).html("更新成功！");
							 }else{
								 $("#skuId_"+skuId).html(jsonObj.cnName);
								 $("#skuId_"+skuId).css("color","red");
							 }
						});
					}
					
					layer.close(loadi);
				},"json");
	}); 

	
	
	
});
