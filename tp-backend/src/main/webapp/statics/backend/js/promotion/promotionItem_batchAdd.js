var ADD_BATCH_ITEM = domain + "/topicItem/batchAdd";
var index = parent.layer.getFrameIndex(window.name);

$(document).ready(function() {

	$("#save").on("click", function() {
		$.ajax({
			type : "post",
			url : ADD_BATCH_ITEM,
			data : $("#batchAdd").serialize(),
			success : function(data) {
				if (data.success) {
					if(addTopicItemBlock(data.data)){
						parent.layer.close(index);
					}
				} else {
					layer.alert(data.msg.message);
				}
			},
			error : function(data) {
				layer.alert("批量新增商品失败!");
			}
		})
	});

	$("#cancel").on("click", function() {
		parent.layer.close(index);
	});

});

function addTopicItemBlock(data) {
	// 活动是否预占库存：0否1是
	var reserveInventoryFlag = parent.$("input[name='reserveInventoryFlag']:checked").val()||parent.$("input[name='reserveInventoryFlag']").val();
	
	var valid=true;
	 $.each(data,function(n,value) {  
		var itemId = value.itemId;
		var barCode = value.barCode;
		var sku = value.sku;
		var name = value.name;
		var supplierId = value.supplierId;
		var stockLocationId = value.stockLocationId;
		var topicImage = value.topicImage;
		var pictureSize = value.pictureSize;
		var salePrice = value.salePrice;
		var categoryId = value.categoryId;
		var stock = value.stock;
		var brandId = value.brandId;
		var sortIndex = value.sortIndex;
		var supplierName = value.supplierName;
		var itemSpec = value.itemSpec;
		var salePrice = value.salePrice;
		var topicPrice = value.topicPrice;
		var limitAmount = value.limitAmount;
		var limitTotal = value.limitTotal;
		var stockLocation = value.stockLocation;
		//var stockAmount = value.stockAmout;
		var bondedArea = value.bondedArea;
		var countryId = value.countryId;
		var countryName = value.countryName;
		var whType = value.whType;
		var remark = value.remark;
		var purchaseValue = value.purchaseMethod;
		var normalPur = "";
		var atOncePur = "";
		if(purchaseValue == "1"){
			normalPur = "selected";
		} else if(purchaseValue == "2") {
			atOncePur = "selected";
		}
		if(null == sku || "" == $.trim(sku)){
			layer.alert("商品信息获取异常,sku不正确!");
			valid = false;
			return;
		}
		if(null == itemId || "" == $.trim(itemId)){
			layer.alert("商品信息获取异常,id不正确!sku["+sku+"]");
			valid = false;
			return;
		}
		if(null == name || "" == $.trim(name)){
			layer.alert("商品信息获取异常,名称不正确!sku["+sku+"]");
			valid = false;
			return;
		}
		if(null == supplierId || "" == $.trim(supplierId)){
			layer.alert("商品信息获取异常,供应商Id不正确!sku["+sku+"]");
			valid = false;
			return;
		}
		if(null == stockLocationId || "" == $.trim(stockLocationId)){
			layer.alert("商品信息获取异常,仓库Id不正确!sku["+sku+"]");
			valid = false;
			return;
		}
		if(null == salePrice || "" == $.trim(salePrice)){
			salePrice = "0";
		}
		if(null == limitAmount || "" == $.trim(limitAmount)){
			layer.alert("商品信息获取异常,导入的限购数量不正确!sku["+sku+"]");
			valid = false;
			return;
		}
		if(null == limitTotal || "" == $.trim(limitTotal)){
			layer.alert("商品信息获取异常,导入的限购总数不正确!sku["+sku+"]");
			valid = false;
			return;
		}
		if(null == topicPrice || "" == $.trim(topicPrice)){
			layer.alert("商品信息获取异常,导入的活动价格不正确!sku["+sku+"]");
			valid = false;
			return;
		}
		
		var row = "<tr align='center' style='background-color: rgb(255, 255, 255);' class='tr topicItemData'>" +
					"<td style='display:none;'>"+
						"<input type='hidden' name='topicItemId' value='0' />"+
						"<input type='hidden' name='topicItemItemId' value='"+value.itemId+"' />"+
						"<input type='hidden' name='topicItemBarcode' value='"+value.barCode+"' />"+
						"<input type='hidden' name='topicItemSku' value='"+value.sku+"' />" +
						"<input type='hidden' name='topicItemSpu' value='"+value.spu+"' />" +
						"<input type='hidden' name='topicItemName' value='"+value.name.replace(/"/g,"&quot;").replace(/'/g,"&apos;")  +"' />" +
						"<input type='hidden' name='topicItemSupplierId' value='"+value.supplierId+"' />" +
						"<input type='hidden' name='topicItemLocationId' value='"+value.stockLocationId+"' />" +
						"<input type='hidden' name='topicImage' value='"+value.topicImage+"' />" +
						"<input type='hidden' name='pictureSize' value='"+value.pictureSize+"' />" +
						"<input type='hidden' name='salePrice' value='"+value.salePrice+"' />" +
						"<input type='hidden' name='categoryId' value='"+value.categoryId+"' />" +
						"<input type='hidden' name='stock' value='"+value.stock+"' />" +
						"<input type='hidden' name='brandId' value='"+value.brandId+"' />" +
						"<input type='hidden' name='bondedArea' value='"+value.bondedArea+"' />" +
						"<input type='hidden' name='whType' value='"+value.whType+"' />" +
						"<input type='hidden' name='countryId' value='"+value.countryId+"' />" +
						"<input type='hidden' name='countryName' value='"+value.countryName+"' />" +
						"<input type='hidden' name='inputSource' value='1' />" +
						"<input type='hidden' name='lockStatus' value='0' />" +
					"</td>" +
					"<td><input type='text' name='sortIndex' value='"+value.sortIndex+"' /></td>" +
					"<td><span>"+value.supplierName+"</span></td>" +
					"<td><span>"+value.barCode+"</span><br/><span>"+value.sku+"</span><br/><span id='name'>"+value.name+"</span></td>" +
					"<td><span>"+value.itemSpec+"</span></td>" +
					"<td>" +
						"<div style='float:left;width:90%;'>" +
							"<img name='selectImage' style='width:90%;' src='" + value.imageFullPath +"'/>" +
						"</div>" +
						"<div style='float:left;'>" +
							"<a name='removeImage' href='javascript:void(0);'>X</a>" +
						"</div>" +
					"</td>" +
					"<td>" +
						"<input type='text' class='input-text lh30' name='topicPrice' value='"+value.topicPrice+"'/>" +
					"</td>" +
					"<td>" +
						"<input type='text' class='input-text lh30' name='limitAmount' value='"+value.limitAmount+"'/>" +
					"</td>" +
					"<td>" +
						"<input type='text' class='input-text lh30' name='limitTotal' value='"+value.limitTotal+"' "+(reserveInventoryFlag==0?"readonly":"")+"/>" +
					"</td>" +
					"<td><span>"+value.stockLocation+"</span></td>" +
					"<td><span></span></td>" +
					"<td><span></span></td>" +
					//"<td><span>"+value.stockAmout+"</span></td>" +
					//"<td></td>"+
					"<td><select class='select' style='width:80px;' name='purchaseMethod' id='purchaseMethod'>" +
					"<option value='1' "+ normalPur +">普通</option>" +
					"<option value='2' "+ atOncePur +">立即购买</option>" +
					"</select></td>" +
					"<td><span>"+value.remark+"</span></td>" +
					"<td>" +
						"<a href='javascript:void(0);' name='removeItem'>删除</a>" +
					"</td>" +
				"</tr>";
		parent.$("#topicItemsList").append(row);
		parent.querySameSkuPrice();		
	})
	return valid;
}
