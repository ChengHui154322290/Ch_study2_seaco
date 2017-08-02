var CONFIRM_ITEM_BAR = domain + "/topicItem/itemBarCode/confirmSearch";
var CONFIRM_ITEM_SKU = domain + "/topicItem/itemSKU/confirmSearch";
var SELECTION_ITEM_SUPPLIER = domain
		+ "/topicItem/item/selectionSupplier/{sku}/{spu}/{supplierId}/{barcode}/{brandId}";
var SEARCH_ITEM_BAR = domain + "/topicItem/itemBarCode/search/{barCode}/{brandId}";
var SEARCH_ITEM_SKU = domain + "/topicItem/itemSKU/search/{sku}/{brandId}";
var SEARCH_SKU_INFO = domain + "/topicItem/SKUInfo/search";
var ITEM_WH = domain + "/topicItem/wh?";
var ITEM_INVENTORY = domain + "/topicItem/wh/inventory";
var TOPIC_ITEM_EMPTY_IMG = domain + "/statics/backend/images/wait_upload.png";
var index = parent.layer.getFrameIndex(window.name);

$(document).ready(function() {
	var bid = parent.$("#brandId").val();
	var topicType = parent.$("#type").val();
	if(topicType=="2" && null != bid){
		$("#parentBrandId").val(bid);
	}
	$("#itemBarConfirm").on("click", function() {
		var itemBar = $("#itemBar").val();
		var pBid = $("#parentBrandId").val();
		var pSpu = $("#preSpu").val();
		var supplierId = $("#preSupplierId").val();
		if(null == pBid || $.trim(pBid).length == 0){
			pBid = "0";
		}
		if(null == pSpu || $.trim(pSpu).length == 0){
			pSpu = "0";
		}
		if(null == supplierId || $.trim(supplierId).length == 0){
			supplierId = "-1";
		}
		if (null == itemBar || "" == $.trim(itemBar)) {
			layer.alert("条码不能为空!")
			return;
		}
		clearItemInfo(false);
		var data = syncGet(CONFIRM_ITEM_BAR, {
			itemBarCode : itemBar,
			brandId : pBid,
			spu : pSpu,
			supplierId : supplierId
		});
		itemBar = itemBar.replaceAll("/","__");
		if (data != null && data.success) {
			// 存在多个商家信息，返回空，提供商家选择框查询
			if (null == data.data || "" == data.data) {
				var url = SELECTION_ITEM_SUPPLIER.replace("{sku}", "0");
				url = url.replace("{barcode}", itemBar);
				url = url.replace("{brandId}", pBid);
				url = url.replace("{spu}", pSpu);
				url = url.replace("{supplierId}", supplierId);
				selectSupplier(url);
			} else {
				if(data.data.itemStatus != 1) {
					layer.alert("商品状态非上架状态，不能添加");
					return;
				}
				else{
					setData(data.data);
					return;
				}
			}
		} else {
			var msg ="";
			if(data.msg && data.msg.message){
				msg = data.msg.message;
			}
			layer.alert("没有查询到商品信息:<br\>"+"1.没有查询到该商品的相关信息<br\> 2.指定品牌下没有该商品<br\>" +
				"3.指定商家下没有该商品<br\>" +
				" 4.单品团只能添加同一spu和供应商下的商品<br\> 5.商品详细信息查询失败"+"<br\> "+msg);
		}
	});

	$("#itemBarSearch").on("click", function() {
		var itemBar = $("itemBar").val();
		var pBid = $("#parentBrandId").val();
		var pSpu = $("#preSpu").val();
		if(null == pBid || $.trim(pBid).length == 0){
			pBid = "0";
		}
		if (null == itemBar || "" == itemBar) {
			itemBar = "0";
		}
		var url = SEARCH_ITEM_BAR.replace("{barCode}", itemBar);
		url = url.replace("{brandId}", pBid);
		searchItem(url,false);
	});

	$("#itemSKUConfirm").on("click", function() {
		var sku = $("#itemSKU").val();
		var pBid = $("#parentBrandId").val();
		var pSpu = $("#preSpu").val();
		var supplierId = $("#preSupplierId").val();
		if(null == pBid || $.trim(pBid).length == 0){
			pBid = "0";
		}
		if (null == sku || "" == $.trim(sku)) {
			layer.alert("SKU不能为空!")
			return;
		}
		clearItemInfo(true);
		var data = syncGet(CONFIRM_ITEM_SKU, {
			sku : sku,
			brandId:pBid,
			spu : pSpu,
			supplierId : supplierId
		})
		if (data != null && data.success) {
			if (null == data.data || "" == data.data) {
				layer.alert("一个SKU不能存在多个商家")
			} else {
				if(data.data.itemStatus != 1) {
					layer.alert("商品状态非上架状态，不能添加");
					return;
				}
				else {
					if(!data.data.categoryId||data.data.categoryId==""){
						layer.alert("无效商品，不能添加");
						return;
					}
					setData(data.data);
					return;
				}
				
			}
		}
		var msg ="";
		if(data.msg && data.msg.message){
			msg = data.msg.message;
		}
		layer.alert("没有查询到商品信息:<br\> 1.没有查询到该商品的相关信息<br\> 2.指定品牌下没有该商品<br\> 3.单品团只能添加同一spu和供应商下的商品<br\> 4.商品详细信息查询失败"+"</br>"+msg);
	});

	$("#itemSKUSearch").on("click", function() {
		var pBid = $("#parentBrandId").val();
		var itemSKU = $("itemSKU").val();
		var supplierId = $("supplierId").val();
		var pSpu = $("#preSpu").val();
		if(null == pBid || $.trim(pBid).length == 0){
			pBid = "0";
		}
		if (null == itemSKU || "" == itemSKU) {
			itemSKU = "0";
		}
		var url = SEARCH_ITEM_SKU.replace("{sku}", itemSKU);
		url = url.replace("{brandId}", pBid);
		searchItem(url,true);
	});

	$("#save").on("click", function() {
		if (!validateForm()) {
			return;
		}
		var supplierId = $("#supplierId").val();
		//var whAmount = $("#warehouseAmount").text();
		//var lmTAmount = $("#limitTotalAmount").val();
		addTopicItemBlock(supplierId);
		parent.layer.close(index);
	});

	$("#cancel").on("click", function() {
		parent.layer.close(index);
	});

	$(".topicNumber").numeral();
	$(".topicInteger").integer();
	
	
	// 颜色选择器
	addColorEvent($("input[name='color']"), "#EE3454");
	addColorEvent($("input[name='textColor']"))
	$(".add_coupon_icon").click(function(){
		// 如果上一个标签为空 则不允许继续添加
		var currentTagContent = $(this).closest("tr").find("input[name='tags']").val();
		if(currentTagContent == null || currentTagContent == ""){
			layer.tips("当前标签为空，不允许继续添加<br>", $(this), {closeBtn:[0,true]});
			return;
		}
		
		
		var newTr = '<tr><td><input class="input-text lh30" type="text" name="tags" style="color:#ffffff;background-color:#EE3454"/></td><td><input type="text" name="color" value="#EE3454"/> </td><td><input type="text" name="textColor" value="#ffffff"/></td></tr>';
		var plusBtn = $(this).clone(true);
		var minusBtn = $(this).next().clone(true);
		$(this).closest("table").append(newTr);
		$(this).closest("table").find("td:last").append(plusBtn).append(minusBtn);
		addColorEvent($("#tagtable").find("input[name='color']:last"), "#EE3454");
		addColorEvent($("#tagtable").find("input[name='textColor']:last"));
		console.log($(newTr).find("td").length);
		
		$(this).remove();
	});
	$(".remove_coupon_icon").click(function(){
		if($(this).closest("table").find("tr").length<=1){
			return ;
		}
		
		var plusBtn = $(".add_coupon_icon:eq(0)").clone(true);
		var hasPlusBtn = $(this).prev(".add_coupon_icon");
		var tr = $(this).closest("tr").remove();
		if(hasPlusBtn.length>0){
			$(".remove_coupon_icon:last").before(plusBtn);
		}
	});
});

function addColorEvent(selector, initColor) {
	var color = initColor || "#ffffff";
	selector.spectrum({
	    color: color,
	    showInput: true,
	    className: "full-spectrum",
	    showInitial: true,
	    showPalette: true,
	    showSelectionPalette: true,
	    maxSelectionSize: 5,
	    preferredFormat: "hex",
	    localStorageKey: "spectrum.demo",
	    move: function (color) {
	        
	    },
	    show: function () {
	    
	    },
	    beforeShow: function () {
	    
	    },
	    hide: function (color) {
	    	if($(this).attr("name") == 'color')
	    		$(this).closest("tr").find("[name='tags']").css("background-color", color.toHexString());
	    	else {
	    		$(this).closest("tr").find("[name='tags']").css("color", color.toHexString());
	    	}
	    	$(this).val(color.toHexString());
	    },
	    change: function(color) {
	    	
	    },
	    palette: [
	        ["rgb(0, 0, 0)", "rgb(67, 67, 67)", "rgb(102, 102, 102)",
	        "rgb(204, 204, 204)", "rgb(217, 217, 217)","rgb(255, 255, 255)"],
	        ["rgb(152, 0, 0)", "rgb(255, 0, 0)", "rgb(255, 153, 0)", "rgb(255, 255, 0)", "rgb(0, 255, 0)",
	        "rgb(0, 255, 255)", "rgb(74, 134, 232)", "rgb(0, 0, 255)", "rgb(153, 0, 255)", "rgb(255, 0, 255)"], 
	        ["rgb(230, 184, 175)", "rgb(244, 204, 204)", "rgb(252, 229, 205)", "rgb(255, 242, 204)", "rgb(217, 234, 211)", 
	        "rgb(208, 224, 227)", "rgb(201, 218, 248)", "rgb(207, 226, 243)", "rgb(217, 210, 233)", "rgb(234, 209, 220)", 
	        "rgb(221, 126, 107)", "rgb(234, 153, 153)", "rgb(249, 203, 156)", "rgb(255, 229, 153)", "rgb(182, 215, 168)", 
	        "rgb(162, 196, 201)", "rgb(164, 194, 244)", "rgb(159, 197, 232)", "rgb(180, 167, 214)", "rgb(213, 166, 189)", 
	        "rgb(204, 65, 37)", "rgb(224, 102, 102)", "rgb(246, 178, 107)", "rgb(255, 217, 102)", "rgb(147, 196, 125)", 
	        "rgb(118, 165, 175)", "rgb(109, 158, 235)", "rgb(111, 168, 220)", "rgb(142, 124, 195)", "rgb(194, 123, 160)",
	        "rgb(166, 28, 0)", "rgb(204, 0, 0)", "rgb(230, 145, 56)", "rgb(241, 194, 50)", "rgb(106, 168, 79)",
	        "rgb(69, 129, 142)", "rgb(60, 120, 216)", "rgb(61, 133, 198)", "rgb(103, 78, 167)", "rgb(166, 77, 121)",
	        "rgb(91, 15, 0)", "rgb(102, 0, 0)", "rgb(120, 63, 4)", "rgb(127, 96, 0)", "rgb(39, 78, 19)", 
	        "rgb(12, 52, 61)", "rgb(28, 69, 135)", "rgb(7, 55, 99)", "rgb(32, 18, 77)", "rgb(76, 17, 48)"]
	    ]
	});
}

function setData(data) {
	$("#itemId").val(data.itemId);
	$("#itemSpec").val(data.itemSpec);
	$("#itemSKU").val(data.sku);
	$("#itemSPU").val(data.spu);
	$("#itemBar").val(data.barCode);
	$("#topicImage").val(data.topicImage);
	$("#imageFull").val(data.imageFullPath);
	$("#supplierId").val(data.supplierId);
	$("#supplierName").text(data.supplierName);
	$("#name").val(data.name);
	$("#brandId").val(data.brandId);
	$("#categoryId").val(data.categoryId);
	$("#stock").val(data.stock);
	$("#topicPrice").val(data.topicPrice);
	$("#salePrice").text(data.salePrice);
	$("#bondedArea").val(data.bondedArea);
	$("#whType").val(data.whType);
	$("#countryId").val(data.countryId);
	$("#countryName").val(data.countryName);
	loadWarehouses(data.sku,data.supplierId);
	$("#warehouse").on(
			"change",
			function() {
				var whId = $("#warehouse").val();
				var bondedArea = $("#warehouse option:selected").attr("bondedArea");
				$("#bondedArea").val(bondedArea);
				var whType = $("#warehouse option:selected").attr("whType");
				$("#whType").val(whType);
				/**
				if (null != whId) {
					var inven = getWhInventory(data.sku,whId);
					if(null == inven){
						layer.alert("获取库存失败");
					}else{
						$("#warehouseAmount").text(inven);
					}
				} else {
					$("#warehouseAmount").text(0);
				}
				*/
			});
}

function loadWarehouses(sku,supplierId) {
	var data = syncGet(ITEM_WH + new Date().getTime(), {
		supplierId : supplierId
	});
	$("#whList").html(data);
	if(null != $("#warehouse option") &&  $("#warehouse option").length > 0){
		$("#warehouse").find("option").selected = true;
	}
	var bondedArea = $("#warehouse option:selected").attr("bondedArea");
	$("#bondedArea").val(bondedArea);
	var whType = $("#warehouse option:selected").attr("whType");
	$("#whType").val(whType);
	var whId = $("#warehouse").val();
	/**
	if (null != whId) {
		var inven = getWhInventory(sku,whId);
		if(null == inven){
			layer.alert("获取库存失败");
		}else{
			$("#warehouseAmount").text(inven);
		}
	} else {
		$("#warehouseAmount").text(0);
	}
	*/
}

function getWhInventory(sku,whId){
	var data = syncGet(ITEM_INVENTORY, {
		sku : sku,
		whId : whId
	});
	if(null != data && data.success){
		if(null != data.data){
			return data.data.inventory;
		}else{
			return 0;
		}
	}else{
		return null;
	}
}

function addTopicItemBlock(supplierId, whAmount) {

	if (null == whAmount) {
		whAmount = "0";
	}
	var warehouseId = "0";
	if(null != $("#warehouse").val()){
		warehouseId =$("#warehouse").val();
	}
	var warehouseName = "";
	if($("#warehouse").find("option:selected").length > 0){
		warehouseName = $("#warehouse").find("option:selected")[0].text;
	}
	var pictureUrl = $("#topicImage").val();
	var pictureFullUrl = $("#imageFull").val();
	if(null == pictureFullUrl || $.trim(pictureFullUrl).length == 0){
		pictureFullUrl = TOPIC_ITEM_EMPTY_IMG;
	}
	var purchaseValue = $("#purchaseMethod").val();
	var normalPur = "";
	var atOncePur = "";
	if(purchaseValue == "1"){
		normalPur = "selected";
	} else if(purchaseValue == "2") {
		atOncePur = "selected";
	}
	
	
	var jsonArray = new Array();
	$("#tagtable").find("tr").each(function(idx){
		var json = new Object();
		json.tag = $(this).find("input[name='tags']").val();
		json.bgcolor = $(this).find("input[name='color']").val();
		json.fontcolor = $(this).find("input[name='textColor']").val();
		if(json.tag != null && json.tag != "")
			jsonArray[idx] = json;
	});
	var reserveInventoryFlag = parent.$("input[name='reserveInventoryFlag']:checked").val()||parent.$("input[name='reserveInventoryFlag']").val();
	var row = "<tr align='center' style='background-color: rgb(255, 255, 255);' class='tr topicItemData'>"
			+ "<td style='display:none;'>"
			+ "<input type='hidden' name='topicItemId' value='0' />"
			+ "<input type='hidden' name='topicItemItemId' value='" + $("#itemId").val() + "' />"
			+ "<input type='hidden' name='topicItemBarcode' value='" + $("#itemBar").val() + "' />"
			+ "<input type='hidden' name='topicItemSku' value='" + $("#itemSKU").val() + "' />"
			+ "<input type='hidden' name='topicItemSpu' value='" + $("#itemSPU").val() + "' />"
			+ "<input type='hidden' name='topicItemName' value='" + $("#name").val().replace(/"/g,"&quot;").replace(/'/g,"&apos;") + "' />"
			+ "<input type='hidden' name='topicItemSupplierId' value='" + supplierId + "' />"
			+ "<input type='hidden' name='topicItemLocationId' value='" + warehouseId + "' />"
			+ "<input type='hidden' name='topicImage' value='" + pictureUrl + "' />"
			+ "<input type='hidden' name='pictureSize' value='" + $("#pictureSize option[selected]").val() + "' />"
			+ "<input type='hidden' name='salePrice' value='" + $("#salePrice").text() + "' />"
			+ "<input type='hidden' name='categoryId' value='" + $("#categoryId").val() + "' />"
			+ "<input type='hidden' name='stock' value='" + $("#stock").val() + "' />"
			+ "<input type='hidden' name='brandId' value='" + $("#brandId").val() + "' />"
			+ "<input type='hidden' name='bondedArea' value='" + $("#bondedArea").val() + "' />"
			+ "<input type='hidden' name='whType' value='" + $("#whType").val() + "' />"
			+ "<input type='hidden' name='countryId' value='" + $("#countryId").val() + "' />"
			+ "<input type='hidden' name='countryName' value='" + $("#countryName").val() + "' />"
			+ "<input type='hidden' name='inputSource' value='0' />"
			+ "<input type='hidden' name='lockStatus' value='0' />"
			+ "<input type='hidden' name='itemTags' value='" + JSON.stringify(jsonArray) + "'/>"
			+ "</td>"
			+ "<td><input type='text' name='sortIndex' class='topicInteger' value='" + $("#sortIndex").val() + "' /></td>"
			+ "<td><span>" + $("#supplierName").text() + "</span></td>"
			+ "<td><span>" + $("#itemBar").val() + "</span><br/><span>" + $("#itemSKU").val()
			+ "</span><br/><span id='name'>" + $("#name").val() + "</span></td>"
			+ "<td><span>" + $("#itemSpec").val() + "</span></td>"
			+ "<td>"
			+ "<div style='float:left;width:90%;'>"
			+ "<img name='selectImage' style='width:90%;' src='" + pictureFullUrl + "'/>"
			+ "</div>"
			+ "<div style='float:left;'>"
			+ "<a name='removeImage' href='javascript:void(0);'>X</a>"
			+ "</div>"
			+ "</td>"
			+ "<td>"
			+ "<input type='text' class='input-text lh30 topicNumber' name='topicPrice' value='" + $("#topicPrice").val() + "'/>"
			+ "</td>"
			+ "<td>"
			+ "<input type='text' class='input-text lh30 topicInteger' name='limitAmount' value='" + $("#limitAmount").val() + "'/>"
			+ "</td>"
			+ "<td>"
			+ "<input type='text' class='input-text lh30 topicInteger' name='limitTotal' value='0' "+(reserveInventoryFlag==0?"readonly":"")+" />"
			+ "</td>"
			+ "<td><span>" + warehouseName + "</span></td>"
			//+ "<td><span>暂无" + "" + "</span></td>"
			+ "<td><span></span></td>"
			+ "<td><span></span></td>"
			+ "<td><select class='select' style='width:80px;' name='purchaseMethod' id='purchaseMethod'>"
			+ "<option value='1' "+ normalPur +">普通</option>"
			+ "<option value='2' "+ atOncePur +">立即购买</option>"
			+ "</select></td>"
			+ "<td><span>" + $("#remark").val() + "</span></td>"
			+ "<td>"
			+ "<a href='javascript:void(0);' name='removeItem'>删除</a>"
			+ "</td>" + "</tr>";
	parent.$("#topicItemsList").append(row);
	parent.querySameSkuPrice();	
}

function searchItem(url,bySku) {
	clearItemInfo(bySku);
	$.layer({
		type : 2,
		title : "选择商品",
		shadeClose : true,
		maxmin : true,
		fix : false,
		area : [ '600px', 400 ],
		iframe : {
			src : url
		},
		end : function() {
			var index = layer.load("请稍等...");
			var skuId = $("#skuId").val();
			if(null == skuId || "" == $.trim(skuId)){
				pBid = 0;
			}
			if (null != skuId && "" != $.trim(skuId)) {
				var data = syncGet(SEARCH_SKU_INFO, {
					skuId : skuId
				});
				if (data.success && null != data.data) {
					setData(data.data);
					layer.close(index);
					return true;
				} else {
					layer.close(index);
					layer.alert("没有查询到商品信息:<br\> 1.没有查询到该商品的相关信息<br\> 2.指定品牌下没有该商品<br\> 3.单品团只能添加同一spu和供应商下的商品<br\> 4.商品详细信息查询失败");
					return false;
				}
			}
			layer.close(index);
		}
	});
}

function selectSupplier(url) {
	$.layer({
		type : 2,
		title : "选择供应商",
		shadeClose : true,
		maxmin : true,
		fix : false,
		area : [ '600px', 400 ],
		iframe : {
			src : url
		},
		end : function() {
			var skuId = $("#skuId").val();
			var pBid = $("#parentBrandId").val();
			if(null == pBid || "" == $.trim(pBid)){
				pBid = 0;
			}
			if (null != skuId && "" != $.trim(skuId)) {
				var data = syncGet(SEARCH_SKU_INFO, {
					skuId : skuId,
					brandId : pBid
				});
				if (data.success && null != data.data) {
					setData(data.data);
					return;
				} else {
					layer.alert("查询商品信息失败!");
				}
			}
		}
	});
}

function validateForm() {
	var validate = true;
	var supplierId = $("#supplierId").val();
	//var whAmount = $("#warehouseAmount").text();
	//var lmTAmount = $("#limitTotalAmount").val();
	var lmAmount = $("#limitAmount").val();
	var topicPrice = $("#topicPrice").val();
	var salePrice = $("#salePrice").text();
	var expMoney = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
	var expInt = /^[-+]?\d*$/;
	if (null == supplierId || "" == $.trim(supplierId)) {
		layer.alert("供应商信息无效");
		validate = false;
		return validate;
	}
	
	if(null == $("#warehouse option[selected]") || "0" == $("#warehouse option[selected]").val()){
		layer.alert("必须选仓库");
		validate = false;
		return validate;
	}

	if (null == salePrice || "" == $.trim(salePrice)
			|| !expMoney.test(salePrice)) {
		validateInfo("吊牌价无效", $("#salePrice"), 2);
		validate = false;
	}
	
	if (null == topicPrice || "" == $.trim(topicPrice)
			|| !expMoney.test(topicPrice)) {
		validateInfo("请输入有效金额", $("#topicPrice"), 2);
		validate = false;
	}

	var salePriceValue = new Number(salePrice);
	var topicPriceValue = new Number(topicPrice);
	if (salePriceValue < topicPriceValue) {
		validateInfo("活动价不能大于吊牌价", $("#topicPrice"), 2);
		validate = false;
	}
	
	if (null == lmAmount || "" == $.trim(lmAmount) || !expInt.test(lmAmount)) {
		validateInfo("请输入有效限购数量", $("#limitAmount"), 2);
		validate = false;
	}

//	if (null == lmTAmount || "" == $.trim(lmTAmount) || !expInt.test(lmTAmount)) {
//		validateInfo("请输入有效限购总量", $("#limitTotalAmount"), 2);
//		validate = false;
//	}
	
//	if (null == whAmount || "" == $.trim(whAmount) || !expInt.test(whAmount)) {
//		validateInfo("库存数量无效", $("#warehouseAmount"), 2);
//		validate = false;
//	}
	
	if (parseInt(lmAmount) > 99) {
		validateInfo("限购数量必须小于99", $("#limitAmount"), 2);
		validate = false;
	}

//	if (parseInt(lmAmount) > parseInt(lmTAmount)) {
//		validateInfo("限购数量必须小于限购总量", $("#limitAmount"), 2);
//		validate = false;
//	}

	// 自营商品有库存,需要校验
//	if ("0" == supplierId) {
//		if (parseInt(lmTAmount) > parseInt(whAmount)) {
//			validateInfo("不能大于可用库存", $("#limitTotalAmount"), 2);
//			validate = false;
//		}
//	}
	
	if($("#warehouse").find("option:selected").length < 1){
		validateInfo("请选择仓库", $("#warehouse"), 2);
		validate = false;
	}
	return validate;
}

function validateInfo(txt, control, time) {
	layer.tips(txt, control, {
		guide : 1,
		time : time,
		more : true
	});
}

function clearItemInfo(bySku){
	$("#itemId").val("");
	$("#itemSpec").val("");
	$("#topicImage").val("");
	$("#imageFull").val("");
	$("#supplierId").val("");
	$("#skuId").val("");
	$("#name").val("");
	$("#categoryId").val("");
	$("#stock").val("0");
	$("#brandId").val("");
	$("#bondedArea").val("0");
	$("#countryId").val("0");
	$("#countryName").val("");
	if(bySku){
		$("#itemBar").val("");
	}
	if(!bySku){
		$("#itemSKU").val("");
	}
	$("#supplierName")[0].innerHTML="";
	$("#salePrice")[0].innerHTML="";
	$("#topicPrice").val("");
	$("#limitAmount").val("");
	$("#limitTotalAmount").val("");
	$("#warehouse").empty();
	//$("#warehouseAmount")[0].innerHTML="";
}