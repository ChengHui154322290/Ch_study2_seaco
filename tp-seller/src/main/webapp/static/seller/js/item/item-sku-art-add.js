/**
 * sku与供应商关系
 */
$(function(){
	//保存
	$("#saveSkuArtNumberBtn").on('click',function(){      
//			if($('#articleNumber').val() == null || $('#articleNumber').val()==""){
//				alert("请输入货号");
//				return false;
//			}
//		/*	if($('#bondedArea').val() ==null || $('#bondedArea').val() =="" ){ 
//				alert("请输选通关渠道");
//				return false;
//			}*/
//			if($('#hsCode').val() == null || $('#hsCode').val()==""){
//				alert("请输入HS编码");
//				return false;
//			}
			if(false == validateAddSkuArt()) return;
			$.ajax({ 
				url: '/seller/item/addSkuArtNumber.htm',
				data: $('#addSkuArtNumberForm').serialize(), 
				type: "post", 
				cache : false, 
				success: function(data) {
					var result = JSON.parse(data);
					if(result.success){ 
						alert("保存成功", 8);
						window.location.reload();
					}else{
						alert(result.msg.message, 8);		
					}
				}
			});
		});
	
	//删除
	$(".deleteSkuArtBtn").on('click',function(){
		var id = $(this).attr('id');
		$.ajax({ 
			url: '/seller/item/deleteSkuArtInfo.htm?id='+id,    
			type: "post", 
			cache : false, 
			success: function(data) {
				var result = JSON.parse(data);
				if(result.success){ 
					alert("删除成功", 8);   
					window.location.reload();
				}else{
					alert(result.msg.message, 8);		
				}
			}
		});
	});
	
	 $("#bondedArea").change(function(){
			var bondedArea = $(this).val();
			$.ajax({
				url : '/seller/item/customs_unit_list?bondedArea=' + bondedArea,
				type : "get",
				cache : false,
				success : function(data) {
					if (data.success) {// 成功
						var list = data.data;
						var html = [];
						html.push('<option value="" selected="selected">请选择</option>');
						var len = data.data.length;
						for(var i = 0; i< len; i++){
							var option = "<option " + " value=" + data.data[i].code + ">" + data.data[i].name + "</option>";
							html.push(option);
						}
						$("#itemFirstUnitCode").html(html);
						$("#itemSecondUnitCode").html(html);
					} else {// 失败
						alert(data.msg.message, 8);
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
