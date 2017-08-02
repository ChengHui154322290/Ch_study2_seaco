/**
 * sku与供应商关系
 */
$(function(){
	//保存
	$("#saveSkuArtNumberBtn").on('click',function(){      
			if(false == validateAddSkuArt()) return;
			$.ajax({ 
				url: 'saveSkuArtNumber.htm',
				data: $('#addSkuArtNumberForm').serialize(), 
				type: "post", 
				cache : false, 
				success: function(data) {
					if(data.success){ 
						layer.alert("保存成功", 8);
						window.location.reload();
					}else{
						layer.alert(data.msg.message, 8);
						return;
					}
				}
			});
		});
	
	$("#saveEditSkuArtNumberBtn").on('click', function(){
		if(false == validateAddSkuArt()) return;
		$.ajax({ 
			url: 'saveSkuArtNumber.htm',
			data: $('#editSkuArtNumberForm').serialize(), 
			type: "post", 
			cache : false, 
			success: function(data) {
				if(data.success){ 
					layer.alert("保存成功", 8);
					var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
					parent.layer.close(index); //执行关闭
				}else{
					layer.alert(data.msg.message, 8);		
				}
			}
		});
	});
	
	//删除
	$(".deleteSkuArtBtn").on('click',function(){
		var id = $(this).attr('id');
		$.ajax({ 
			url: 'deleteSkuArtInfo.htm?id='+id,    
			type: "post", 
			cache : false, 
			success: function(data) {
				if(data.success){ 
					layer.alert("删除成功", 8);   
					window.location.reload();
				}else{
					layer.alert(data.msg.message, 8);		
				}
			}
		});
	});
	
	$(".editSkuArtBtn").on('click',function(){
		var id = $(this).attr('id');
		window.location.href = 'toEditSkuArtNumber.htm?id='+id;
	});
});




/**
 * 检查是否为整数
 */
function checkIntNum(txt){
	var numReg = /^[-+]?\d*$/;
	return numReg.test(txt);
}

function validateAddSkuArt(){
	var articleNumber = $("#articleNumber").val();
	if(articleNumber == ""){
		alert("备案编号不能为空");	
		return false;
	}
	var bondedArea = $("#bondedArea").val();
	if(bondedArea == ""){
		alert("通关渠道不能为空");	
		return false;
	}
	var hsCode = $("#hsCode").val();
	if(hsCode == ""){
		alert("HS编码不能为空");	
		return false;
	}
	
	var itemFirstUnitCode = $("#itemFirstUnitCode ").val();
	if(itemFirstUnitCode == ""){
		alert("第一备案单位不能为空");	
		return false;
	}
	var itemFirstUnitCount = $("#itemFirstUnitCount ").val();
	if(itemFirstUnitCount == ""){
		alert("第一备案数量不能为空");	
		return false;
	}
	
	var itemSecondUnitCode = $("#itemSecondUnitCode").val();
	var itemSecondUnitCount = $("#itemSecondUnitCount").val();
	if(itemSecondUnitCode != ""){
		if(itemSecondUnitCount == ""){
			alert("第二备案数量不能为空");	
			return false;
		}
	}
	var itemDeclareName = $("#itemDeclareName").val()
	if(itemDeclareName == ""){
		alert("商品报关名称不能为空");	
		return false;
	}
	var itemFeature = $("#itemFeature").val();
	if(itemFeature == ""){
		alert("商品特征不能为空");
		return false;
	}
}
