var SAVE_TOPIC = domain + "/topic/save";
var SUBMIT_TOPIC = domain + "/topic/submit";
var ADD_TOPIC_ITEM = domain
		+ "/topicItem/{topicId}/addItem/{spu}/{supplierId}/{sortIndex}";
var EDIT_TOPIC_ITEM = domain + "/topicItem/{topicId}/editItem/{topicItemId}";
var REMOVE_TOPIC_ITEM = domain + "/topicItem/{topicId}/removeItem";
var UPLOAD_TOPIC_ITEM_PIC = domain + "/topicItem/{topicItemIndex}/upload/Image";
var UPLOAD_TOPIC_PIC = domain + "/topic/upload/Image/{topicControlName}";
var PASTE_TOPIC_ITEMS = domain + "/topicItem/{topicId}/pasteItems/{sortIndex}";
var SEARCH_TOPIC_BRAND = domain + "/topic/brand/query";
var CONFIRM_TOPIC_BRAND = domain + "/topic/brand/confirm?";
var TOPIC_SEARCH_PAGE = domain + "/topic/load.htm";
var TOPIC_ITEM_LOCK = domain + "/topicItem/lockItem?";
var TOPIC_ITEM_RELEASE = domain + "/topicItem/releaseItem?";
var TOPIC_CHANGE_GENERATE = domain + "/topicChange/generateTopic?";
var EDIT_TOPIC = domain + "/topic/{topicId}/edit";
var VIEW_TOPIC = domain + "/topic/{topicId}/detail";
var TOPIC_ITEM_BATCH_LOCK = domain + "/topicItem/batchLock?";

function formInit() {
	var index = layer.load("加载中...");
	initControl();
	var topicId = $("#topicId").val();
	$("#lockAllItem").off("click");
	$("#lockAllItem").on("click",function(){
		var data = syncPost(TOPIC_ITEM_BATCH_LOCK + new Date().getTime(), {
			topicId : topicId
		});
		if (data != null && data.success) {
			var url = VIEW_TOPIC.replaceAll("{topicId}",topicId);
			window.location.href = url;
		} else {
			layer.alert(data.data);
		}		
	});
	$("#genChangeOrder").off("click");
	$("#genChangeOrder").on("click", function() {
		var data = syncGet(TOPIC_CHANGE_GENERATE + new Date().getTime(), {
			id : topicId
		});
		if (data != null && data.success) {
			layer.msg("生成成功",2 , 9);
		} else {
			layer.alert("生成失败，可能是以下原因造成:<br\>1.活动不存在<br\>2.活动状态不是已审批通过<br\>3.该活动存在未处理的变更单");
		}
	});
	layer.close(index);
}

function formReload(){
	var topicId = $("#topicId").val();
	var url = EDIT_TOPIC.replaceAll("{topicId}",topicId);
	window.location.href = url;
}

function topicItemLockBind() {
	var link = $(this);
	var lockStatus = $(this).closest("tr").find("input[name='lockStatus']");
	var selectId = $(this).closest("tr").find("input[name='topicItemId']")
			.val();
	if (lockStatus.val() == null || link == null) {
		layer.alert("数据异常");
		return;
	}
	if (lockStatus.val() == "0") {
		$.layer({
			shade : [ 0 ],
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '是否锁定指定商品？',
				btns : 2,
				type : 4,
				btn : [ '确定', '取消' ],
				yes : function() {
					var data = syncPost(TOPIC_ITEM_LOCK + new Date().getTime(),
							{
								itemId : selectId
							});
					if (data != null) {
						if (data.success) {
							lockStatus.val("1");
							link[0].innerHTML = "[解锁]";
							layer.msg("商品锁定", 1, 9);
						} else {
							layer.alert(data.msg.message);
						}
					} else {
						layer.alert("数据处理异常");
					}
				},
				no : function() {
				}
			}
		});
	} else {
		$.layer({
			shade : [ 0 ],
			area : [ 'auto', 'auto' ],
			dialog : {
				msg : '是否解锁指定商品？',
				btns : 2,
				type : 4,
				btn : [ '确定', '取消' ],
				yes : function() {
					var itemId = $(this).closest("tr").find(
							"input[name='topicItemId']").val();
					var data = syncPost(TOPIC_ITEM_RELEASE
							+ new Date().getTime(), {
						itemId : selectId
					});
					if (data != null) {
						if (data.success) {
							lockStatus.val("0");
							link[0].innerHTML = "[锁定]";
							layer.msg("商品解锁", 1, 9);
						} else {
							layer.alert(data.msg.message);
						}
					} else {
						layer.alert("数据处理异常");
					}
				},
				no : function() {
				}
			}
		});
	}
}

function topicItemsInfo() {
	var items = new Array();
	var topicId = $("#topicId").val();
	$("#topicItemsList .topicItemData").each(
			function() {
				var topicItemObj = new Object();
				topicItemObj.topicId = topicId;
				topicItemObj.id = $(this).find("input[name='topicItemId']")
						.val();
				topicItemObj.itemId = $(this).find(
						"input[name='topicItemItemId']").val();
				topicItemObj.sku = $(this).find("input[name='topicItemSku']")
						.val();
				topicItemObj.spu = $(this).find("input[name='topicItemSpu']")
						.val();
				topicItemObj.name = $(this).find("input[name='topicItemName']")
						.val();
				topicItemObj.barCode = $(this).find(
						"input[name='topicItemBarcode']").val();
				topicItemObj.supplierId = $(this).find(
						"input[name='topicItemSupplierId']").val();
				topicItemObj.topicPrice = $(this).find(
						"input[name='topicPrice']").val();
				topicItemObj.topicImage = $(this).find(
						"input[name='topicImage']").val();
				topicItemObj.limitAmount = $(this).find(
						"input[name='limitAmount']").val();
				topicItemObj.limitTotal = $(this).find("input[name='limitTotal']").val();
				topicItemObj.stockLocationId = $(this).find("input[name='topicItemLocationId']").val();
				topicItemObj.pictureSize = $(this).find("input[name='pictureSize']").val();
				topicItemObj.salePrice = $(this)
						.find("input[name='salePrice']").val();
				topicItemObj.categoryId = $(this).find(
						"input[name='categoryId']").val();
				topicItemObj.bondedArea = $(this).find(
						"input[name='bondedArea']").val();
				topicItemObj.whType = $(this).find(
						"input[name='whType']").val();
				topicItemObj.countryId = $(this).find(
						"input[name='countryId']").val();
				topicItemObj.countryName = $(this).find(
						"input[name='countryName']").val();
				topicItemObj.brandId = $(this).find("input[name='brandId']")
						.val();
				topicItemObj.stock = $(this).find("input[name='stock']").val();
				topicItemObj.sortIndex = $(this)
						.find("input[name='sortIndex']").val();
				topicItemObj.supplierName = $(this).find("span")[0].innerHTML; // 供应商
				topicItemObj.itemSpec = $(this).find("span")[4].innerHTML; // 规格参数
				topicItemObj.stockLocation = $(this).find("span")[5].innerHTML; // 仓库名称
				
				//topicItemObj.stockAmout = $(this).find("span")[6].innerHTML;// 可分配库存
				//topicItemObj.remark = $(this).find("span")[7].innerHTML;// 专场剩余库存
				topicItemObj.inputSource = $(this).find("input[name='inputSource']").val();
				topicItemObj.lockStatus = $(this).find("input[name='lockStatus']").val();
				topicItemObj.purchaseMethod = $(this).find("select[name='purchaseMethod']").val();
				topicItemObj.saledAmount = 0;
				topicItemObj.itemTags = $(this).find("input[name='itemTags']").val();
				items.push(topicItemObj);
			});
	var itemInfos = JSON.stringify(items);
	$("#itemInfos").val(itemInfos);
	var deleteItemInfos = JSON.stringify(deleteItems);
	$("#removeItemIds").val(deleteItemInfos);
}

function topicSpecialValidate() {
	if (null == $(".topicItemData") || $(".topicItemData").length == 0) {
		layer.alert("至少添加一项商品!");
		return false;
	}
	return true;
}

function topicItemSpecialValidate() {
	return true;
}

function genTopicItemAddUrl(formatUrl) {
	var spus = $(".topicItemData input[name='topicItemSpu']:first").val();
	var supplierId = $(".topicItemData input[name='topicItemSupplierId']:first")
			.val();
	if (null != spus && "" != $.trim(spus)) {
		formatUrl = formatUrl.replace("{spu}", spus);
	} else {
		formatUrl = formatUrl.replace("{spu}", "0");
	}
	if (null != supplierId && "" != $.trim(supplierId)) {
		formatUrl = formatUrl.replace("{supplierId}", supplierId);
	} else {
		formatUrl = formatUrl.replace("{supplierId}", "-1");
	}
	return formatUrl;
}

function validateSingleType() {
	var uniqueSpu = null;
	var uniqueSpId = null;
	var hasDiff = false;
	$(".topicItemData").each(function() {
		var spu = $(this).find("input[name='topicItemSpu']").val();
		var spId = $(this).find("input[name='topicItemSupplierId']").val();
		if (null == uniqueSpu || null == uniqueSpId) {
			uniqueSpu = spu;
			uniqueSpId = spId;
		} else {
			if (uniqueSpu != spu || uniqueSpId != spId) {
				hasDiff = true;
				return;
			}
		}
	});
	if (hasDiff) {
		layer.alert("单品团只能添加同一spu和供应商下的商品");
		return false;
	}
	return true;
}

function topicItemEditBind() {
	var selectId = $(this).closest("tr").find("input[name='topicItemId']").val();
	var topicId = $("#topicId").val();
	var url = EDIT_TOPIC_ITEM.replace("{topicId}",topicId);
	url = url.replace("{topicItemId}",selectId);
	$.layer({
		type : 2,
		title : "商品库存详情",
		shadeClose : true,
		maxmin : true,
		fix : false,
		area : [ '600px', 400 ],
		iframe : {
			src : url
		}
	});
}

function topicItemRemoveBind() {
	var selectId = $(this).closest("tr").find("input[name='topicItemId']").val();
	var topicId = $("#topicId").val();
	if(null == selectId || "0" == selectId){
	}else{
		deleteItems.push(selectId);
	}
	$(this).closest("tr").detach();
}

function initialSkuList(skuArray){
	return skuArray;
}