var index = parent.layer.getFrameIndex(window.name);
var EDIT_TOPICITEM_AMOUNT = domain + "/topicItem/editItem"
$(document).ready(function() {

	$("#save").on("click", function() {
		var topicId = $("#topicId").val();
		var topicItemId = $("#topicItemId").val();
		var currentLimitAmount = $("#currentLimitAmount").text();
		var avaliableAmount = $("#avaliableAmount").text();
		var addLimitAmount = $("#addLimitAmount").val();
		var remainStock = $("#remainStock").text();
		var supplierId = $("#supplierId").val();
		if(null == addLimitAmount){
			layer.alert("请输入商品增加限制总量");
			return;
		}
		if(null == avaliableAmount || null == currentLimitAmount){
			layer.alert("当前商品库存信息获取出错");
			return;
		}
		if(0 == parseInt(addLimitAmount)){
			layer.alert("变更库存数不能等于0");
			return;
		}
		if(null == supplierId){
			layer.alert("商品供应商信息出错");
			return;
		}
		if(parseInt(supplierId) == 0){
			if(parseInt(avaliableAmount) < parseInt(addLimitAmount)){
				layer.alert("新增库存大于商品的有效库存");
				return;
			}
		}
		if((parseInt(addLimitAmount)+parseInt(currentLimitAmount)) < 1){
			layer.alert("变更库存和现有库存总和不能小于等于0");
			return;
		}
		if(parseInt(addLimitAmount) < 0){
			if(parseInt(addLimitAmount) + parseInt(remainStock) < 0){
				layer.alert("退回库存数，必须小于等于活动占用剩余库存数");
				return;
			}
		}
		var data = syncPost(EDIT_TOPICITEM_AMOUNT,{
			topicItemId : topicItemId,
			amount : addLimitAmount
		});
		if (data != null) {
			if(data.success){
				var hasChange = false;
				var expInt = /^[-+]?\d*$/;
				parent.$("#topicItemsList .topicItemData").each(function(){
					var id = $(this).find("input[name='topicItemId']").val()
					if(topicItemId == id){
						//if(null == data.data || "" == $.trim(data.data) || !expInt.test(data.data)){
						//	layer.alert("库存请求失败!")
						//	return;
						//}
						var currentAmount = $(this).find("input[name='limitTotal']").val()
						if (null == currentAmount || "" == $.trim(currentAmount) || !expInt.test(currentAmount)) {
							layer.alert("更新现有库存失败!")
							return;
						}
						if(null == currentAmount){
							$(this).find("input[name='limitTotal']").val(parseInt(addLimitAmount));
						}else{
							$(this).find("input[name='limitTotal']").val(parseInt(addLimitAmount) + parseInt(currentAmount));
						}
						hasChange = true;
						parent.layer.close(index);
					}
				});
				if(hasChange){
					parent.layer.close(index);
				}
			} else {
				layer.alert(data.msg.message);
			}
		}
	});
	
	$("#cancel").on("click", function() {
		parent.layer.close(index);
	});

});