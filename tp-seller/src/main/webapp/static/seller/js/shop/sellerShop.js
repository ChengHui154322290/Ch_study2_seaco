$(function(){
	/**
	 * 保存
	 */
	$('#dataFormSave').on('click',function(){
		if(validateForm()){
				$.ajax({ 
					url: domain + '/seller/shop/save.htm', 
					data: $('#inputForm').serialize(), 
					type: "post", 
					cache : true, 
					success: function(data) {
						var result=JSON.parse(data);
						if(result.isSuccess){
							layer.alert("保存店铺信息成功！", 8);
						}else{
							layer.alert(result.data, 8);
						}
						
					}
				});
		}
	});
});	

//validate viewpoint
function validateForm(){
	//validate sort
	var checkSku = true;
	if($.trim($('#shopName').val()).isEmpty()){
		alert("店铺名称不能为空");
		checkSku = false;
		$(this).focus();
		return false;
	}
	return true;
}


//查看店铺内容
$("#previewPhoneContent").on("click",function(){
	var phoneContent = mobileEditor.html();
	if(null == phoneContent || "" == $.trim(phoneContent)){
		layer.alert("请输入供应商铺介绍内容!");
		return;
	}
	$.layer({
		type : 1,
		title : "查看供应商活动专场内容",
		shadeClose : true,
		maxmin : true,
		fix : false,
		area: ['800px', 600],
		page: {
			html : phoneContent
		}
	});
});