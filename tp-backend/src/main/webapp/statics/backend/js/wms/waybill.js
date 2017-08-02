/**
 * 运单管理
 */
var pageii;
$(function(){
	//运单申请
	$('.waybillapplybtn').live('click',function(){
		pageii=$.layer({
          type : 2,
          title: '运单申请',
          shadeClose: true,
          maxmin: true,
          fix : false,  
          area: ['400px', 400],                     
          iframe: {
              src : domain+'/wms/waybill/toapply.htm'
          } 
      });
	}); 
	
	//申请
	$("#applyWaybill").on('click', function(){
		var expressCode = $("#logisticsCode").val();
		if(expressCode == ""){
			alert("请选择物流公司");
			return;
		}
		var amount = $("#amount").val();
		if(amount == ""){
			alert("请填写申请数量");
			return;
		}
		if(amount <= 0 || alert > 1000){
			alert("请填写正确的申请数量(不多于1000)");
			return;
		}
		$.ajax({
			url:'applyWaybill.htm',
			data:$('#applyWaybillForm').serialize(),
			type:"post",
			cache:false,
			success:function(data){
				if(data.success){
					alert("申请成功", 8);
					parent.window.location.reload();
	            	parent.layer.close(parent.pageii);
				}else{
					layer.alert(data.msg.message, 8);
				}
			}
		});	
	});
	
	$('.waybillLog').live('click',function(){
		var id = $(this).attr('waybill');
		pageii=$.layer({
          type : 2,
          title: '运单号使用日志',
          maxmin: true,
          fix : false,  
          area: ['800px', 800],                     
          iframe: {
              src : domain+'/wms/waybill/waybillNoLog.htm?waybillNo='+id 
          } 
      });
	}); 
	
	$('.waybillOperation').live('click',function(){
		var id = $(this).attr('waybill');
		var code = $(this).attr('orderCode');
		pageii=$.layer({
          type : 2,
          title: '运单号操作',
          maxmin: true,
          fix : false,  
          area: ['400px', 400],                     
          iframe: {
              src : domain+'/wms/waybill/waybillNoOperation.htm?waybillNo='+id + '&orderCode='+ code 
          } 
      });
	}); 
	
	//操作
	$(".waybillNoOperation").on('click',function(){
		var id = $("#waybillNo").val();
		var code = $("#orderCode").val();
		var type = $(this).attr('otype');
		if(type != 1 && type != 2 && type != 3){
			return;
		}
		$.ajax({ 
			url: 'waybillOper.htm?waybillNo='+id+'&orderCode='+code+'&type='+type,    
			type: "get", 
			cache : false, 
			success: function(data) {
				if(data.success){ 
					alert("操作成功", 8);   
					parent.window.location.reload();
	            	parent.layer.close(parent.pageii);
				}else{
					alert(data.msg.message, 8);		
				}
			}
		});	
	});
});


// 查看
function showWaybillInfo(id) {
	showTab("waybill_info_" + id, "运单推送查看", "/wms/waybill/waybillinfo?id=" + id);
	return false;
}

function showTab(id, text, url) {
	var tv = {};
	tv.linkId = id + "_link";
	tv.tabId = id;
	tv.url = url;
	tv.text = text;
	try {
		window.parent.showTab(tv);
	} catch (e) {
	}
}

/**
 * 检查是否为整数
 */
//function checkIntNum(txt){
//	var numReg = /^[-+]?\d*$/;
//	return numReg.test(txt);
//}
//
//function validateAddSkuArt(){
//	var articleNumber = $("#articleNumber").val();
//	if(articleNumber == ""){
//		alert("备案编号不能为空");	
//		return false;
//	}
//	var bondedArea = $("#bondedArea").val();
//	if(bondedArea == ""){
//		alert("通关渠道不能为空");	
//		return false;
//	}
//	var hsCode = $("#hsCode").val();
//	if(hsCode == ""){
//		alert("HS编码不能为空");	
//		return false;
//	}
//	
//	var itemFirstUnitCode = $("#itemFirstUnitCode ").val();
//	if(itemFirstUnitCode == ""){
//		alert("第一备案单位不能为空");	
//		return false;
//	}
//	var itemFirstUnitCount = $("#itemFirstUnitCount ").val();
//	if(itemFirstUnitCount == ""){
//		alert("第一备案数量不能为空");	
//		return false;
//	}
//	
//	var itemSecondUnitCode = $("#itemSecondUnitCode").val();
//	var itemSecondUnitCount = $("#itemSecondUnitCount").val();
//	if(itemSecondUnitCode != ""){
//		if(itemSecondUnitCount == ""){
//			alert("第二备案数量不能为空");	
//			return false;
//		}
//	}
//	var itemDeclareName = $("#itemDeclareName").val()
//	if(itemDeclareName == ""){
//		alert("商品报关名称不能为空");	
//		return false;
//	}
//	var itemFeature = $("#itemFeature").val();
//	if(itemFeature == ""){
//		alert("商品特征不能为空");
//		return false;
//	}
//}
