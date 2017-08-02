/**
 * sku与供应商关系
 */
$(function(){
	
	/**
	 * 全选/取消全选
	 */
	$('#chkall').click(function() {
		if ($(this).attr('checked')) {
			$("input[name='spId']").attr("checked", true);
		} else {
			$("input[name='spId']").attr("checked", false);
		}
	});
	
	//查询 
	$("#querySupplierBtn").on('click',function(){
		var supplierId = $('#supplierIdQuery').val();
		if(!checkIntNum(supplierId)){
			alert('编码只能为整数');
			return;
		}
		$('#selectSupplierForm').attr('action','selectSupplier.htm');
		$('#selectSupplierForm').submit();
	});

	var listUrl = "getSkuSupplier.htm?skuId=";
	//保存
	$("#saveSupplierBtn").on('click',function(){
		//校验
		var supplierIdArray = new Array();
		var supplierIds = $("#supplierIds").val();
		if(supplierIds != "") {
			if(supplierIds.indexOf(",")){
				supplierIdArray =  supplierIds.split(',');
			}else{
				supplierIdArray.push(supplierIds);
			}
		}
		var flag = true;
		//获取选择框的数据哈
		$('#skuSupplierList').val('');
		var puchaseArry = new Array();
		$('.supplierListTr').each(function(){
			if($(this).find('[name=spId]').attr('checked')){
				//动态添加行到父页面
				var supplierId = $(this).find('[name=spId]').val();
				var supplierName = $(this).find('[name=supplierName]').val();
				if(supplierIdArray.indexOf(supplierId)>-1){
					flag = false;
					alert("供应商["+ supplierName+"]已经存在sku中,不能再被选择");
					return ;
				}
				var supplierType = $(this).find('[name="supplierType"]').val();
				var skuAndSupplierJson ={supplierId:supplierId,supplierName:supplierName,supplierType:supplierType};
				puchaseArry.push(skuAndSupplierJson);
			} 
		});
		if(flag){
			var purchaseJson=JSON.stringify(puchaseArry);
			$('#skuSupplierList').val(purchaseJson);
			var skuId = $('#skuId').val();
			$.ajax({ 
				url: '/seller/item/addSkuSupplier.htm',
				dataType: 'text',
				data: $('#selectSupplierForm').serialize(), 
				type: "post", 
				cache : false, 
				success: function(data) {
					var obj=eval('('+data+')');
					if(obj.success){//成功
						layer.alert('操作成功', 4,function(){
							location.href=listUrl+skuId;
						});
				    }else{//失败
				    	 layer.alert(obj.msg.message, 8);
				    }			
				}
			});
		}
	});
	
	//删除
	$(".deleteSkuSupplierBtn").on('click',function(){
		var skuSupplierId = $(this).attr('id');
		var skuId = $(this).attr('skuId');
		$.ajax({ 
			url: '/seller/item/deleteSkuSupplier.htm?skuSupplierId='+skuSupplierId, 
			dataType: 'text',
			type: "post", 
			cache : false, 
			success: function(data) {
				var obj=eval('('+data+')');
				if(obj.success){//成功
					layer.alert('操作成功', 4,function(){
						location.href=listUrl+skuId;
					});
			    }else{//失败
			    	 layer.alert(obj.msg.message, 8);
			    }			
			}
		});
	});
	
});

/**
 * 检查是否为整数
 */
function checkIntNum(txt){
	var numReg = /^[-+]?\d*$/;
	return numReg.test(txt);
}
