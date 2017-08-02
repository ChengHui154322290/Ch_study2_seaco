var SAVE_TOPIC = domain + "/topicChange/save";
var EDIT_TOPIC = domain + "/topicChange/save";
var SUBMIT_TOPIC = domain + "/topicChange/submit";
var RETURN_TOPIC_CHANGE_SEARCH = domain + "/topicChange/load";
var SEARCH_IMPT_TOPIC = domain + "/topicChange/importTopic?";
var SEARCH_TOPIC_CHANGE = domain + "/topicChange/{id}/search?";
var SEARCH_READONLY_TOPIC_CHANGE = domain + "/topicChange/{id}/searchReadonly?";
var TOPIC_SEARCH_PAGE = domain + "/topicChange/load.htm";
var TOPIC_ITEM_GET_SINGLE_FILTER = domain + "/topicItemChange/getFilterInfo?"
var EDIT_TOPIC_ITEM_CHANGE = domain+ "/topicItemChange/{topicId}/editItem/{topicItemId}/{topicItemChangeId}";
var EDIT_TOPIC_CHANGE = domain + "/topicChange/{topicChangeId}/edit";


function formInit() {

	var index = layer.load("请稍等...");
	var topicChangeId = $("#topicChangeId").val();
	if (topicChangeId == null || $.trim(topicChangeId).length == 0) {
		$("#confirmChangeTopicId").off("click");
		$("#confirmChangeTopicId").on("click", selectTopic);
		$("#return").off("click");
		$("#return").on("click", returnCick);
	} else {
		initControl();
	}
	layer.close(index);
}

function topicItemLockBind() {
}

function returnCick() {
	window.location.href = RETURN_TOPIC_CHANGE_SEARCH;
}

function formReload(){
	var topicId = $("#topicId").val();
	var url = EDIT_TOPIC_CHANGE.replaceAll("{topicChangeId}",topicId);
	window.location.href = url;
}

function selectTopic() {
	var index = layer.load("请稍等...");
	var topicChangeId = $("#changeTopicIdInput").val();
	if (topicChangeId == null || $.trim(topicChangeId).length == 0) {
		layer.alert("请输入需调整的活动序号");
		return;
	}
	$("#topicChangeId").val(topicChangeId);
	var data = syncGet(SEARCH_IMPT_TOPIC + new Date().getTime(), {
		id : topicChangeId
	});
	if (data == null || $.trim(data).length == 0) {
		layer.alert("导入活动出现异常:<br\>1.活动不存在<br\>2.活动状态不是已审批通过<br\>3.该活动存在未处理的变更单");
		return;
	} else {
		window.location.href = data;
	}
	layer.close(index);
}

function topicItemsInfo() {
	var items = new Array();
	var topicChangeId = $("#topicChangeId").val();
	$("#topicItemsList .topicItemData")
			.each(
					function() {
						var topicItemChangeObj = new Object();
						topicItemChangeObj.topicChangeId = topicChangeId;
						topicItemChangeObj.id = $(this).find(
								"input[name='topicItemId']").val();
						topicItemChangeObj.itemId = $(this).find(
								"input[name='topicItemItemId']").val();
						topicItemChangeObj.sku = $(this).find(
								"input[name='topicItemSku']").val();
						topicItemChangeObj.spu = $(this).find(
								"input[name='topicItemSpu']").val();
						topicItemChangeObj.name = $(this).find(
								"input[name='topicItemName']").val();
						topicItemChangeObj.barCode = $(this).find(
								"input[name='topicItemBarcode']").val();
						topicItemChangeObj.supplierId = $(this).find(
								"input[name='topicItemSupplierId']").val();
						topicItemChangeObj.topicPrice = $(this).find(
								"input[name='topicPrice']").val();
						topicItemChangeObj.topicImage = $(this).find(
								"input[name='topicImage']").val();
						topicItemChangeObj.limitAmount = $(this).find(
								"input[name='limitAmount']").val();
						topicItemChangeObj.limitTotal = $(this).find(
								"input[name='limitTotal']").val();
						topicItemChangeObj.stockLocationId = $(this).find(
								"input[name='topicItemLocationId']").val();
						topicItemChangeObj.pictureSize = $(this).find(
								"input[name='pictureSize']").val();
						topicItemChangeObj.salePrice = $(this).find(
								"input[name='salePrice']").val();
						topicItemChangeObj.categoryId = $(this).find(
								"input[name='categoryId']").val();
						topicItemChangeObj.brandId = $(this).find(
								"input[name='brandId']").val();
						topicItemChangeObj.bondedArea = $(this).find(
								"input[name='bondedArea']").val();
						topicItemChangeObj.whType = $(this).find(
								"input[name='whType']").val();
						topicItemChangeObj.countryId = $(this).find(
								"input[name='countryId']").val();
						topicItemChangeObj.countryName = $(this).find(
								"input[name='countryName']").val();
						topicItemChangeObj.stock = $(this).find(
								"input[name='stock']").val();
						topicItemChangeObj.sortIndex = $(this).find(
								"input[name='sortIndex']").val();
						topicItemChangeObj.supplierName = $(this).find("span")[0].innerHTML;
						topicItemChangeObj.itemSpec = $(this).find("span")[4].innerHTML;
						topicItemChangeObj.stockLocation = $(this).find("span")[5].innerHTML;
//						topicItemChangeObj.stockAmout = $(this).find("span")[6].innerHTML;
//						topicItemChangeObj.remark = $(this).find("span")[7].innerHTML;
						topicItemChangeObj.inputSource = $(this).find(
								"input[name='inputSource']").val();
						topicItemChangeObj.operStatus = $(this).find(
								"input[name='operStatus']").val();
						topicItemChangeObj.changeTopicItemId = $(this).find(
								"input[name='changeTopicItemId']").val();
						topicItemChangeObj.sourceLimitTotal = $(this).find(
								"input[name='sourceLimitTotal']").val();
						topicItemChangeObj.purchaseMethod = $(this).find(
								"select[name='purchaseMethod']").val();
						topicItemChangeObj.saledAmount = 0;
						topicItemChangeObj.itemTags = $(this).find("input[name='itemTags']").val();
						items.push(topicItemChangeObj);
					});
	var itemInfos = JSON.stringify(items);
	$("#itemInfos").val(itemInfos);
	var deleteItemInfos = JSON.stringify(deleteItems);
	$("#removeItemIds").val(deleteItemInfos);
}

function topicSpecialValidate() {
	return true;
}

function topicItemSpecialValidate() {
	return true;
}

function genTopicItemAddUrl(formatUrl) {
	var spus = "0";
	var supplierId = "-1";
	var topicId = $("#changeTopicId").val();
	if (topicId == null || $.trim(topicId).length == 0) {
		return formatUrl;
	}
	if ($(".topicItemData input[name='topicItemSpu']").length > 0) {
		spus = $(".topicItemData input[name='topicItemSpu']:first").val();
		supplierId = $(".topicItemData input[name='topicItemSupplierId']:first")
				.val();
	} else {
		var data = syncGet(TOPIC_ITEM_GET_SINGLE_FILTER + new Date().getTime(),
				{
					topicId : topicId
				});
		if (null != data.data) {
			if (null == data.data.spu) {
				var resultData = JSON.parse(data.data);
				var result = eval("(" + resultData + ")");
				if (null != result.spu) {
					spus = result.spu;
					supplierId = result.supplierId;
				}
			} else {
				spus = data.data.spu;
				supplierId = data.data.supplierId;
			}
		}
	}
	if (null != spus && $.trim(spus).length > 0) {
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
	if ($(".topicItemData").length == 0) {
		return true;
	}
	var uniqueSpu = null;
	var uniqueSpId = null;
	var hasDiff = false;
	var topicId = $("#changeTopicId").val();
	var data = syncGet(TOPIC_ITEM_GET_SINGLE_FILTER + new Date().getTime(), {
		topicId : topicId
	});
	if (null != data.data) {
		if (null == data.data.spu) {
			var resultData = JSON.parse(data.data);
			var result = eval("(" + resultData + ")");
			if (null != result.spu) {
				uniqueSpu = result.spu;
				uniqueSpId = result.supplierId;
			}
		} else {
			uniqueSpu = data.data.spu;
			uniqueSpId = data.data.supplierId;
		}
	} else {
		if ($(".topicItemData").length > 0) {
			uniqueSpu = $(".topicItemData input[name='topicItemSpu']:first")
					.val();
			uniqueSpId = $(
					".topicItemData input[name='topicItemSupplierId']:first")
					.val();
		}
	}

	$(".topicItemData").each(function() {
		var spu = $(this).find("input[name='topicItemSpu']").val();
		var spId = $(this).find("input[name='topicItemSupplierId']").val();
		if (uniqueSpu != spu || uniqueSpId != spId) {
			hasDiff = true;
			return;
		}
	});
	if (hasDiff) {
		layer.alert("单品团只能添加同一spu和供应商下的商品");
		return false;
	}
	return true;
}

function topicItemEditBind() {
	// selectId = topicItemChangeId
	var selectId = $(this).closest("tr").find("input[name='topicItemId']")
			.val();
	var changeTopicItemId = $(this).closest("tr").find(
			"input[name='changeTopicItemId']").val();
	var topicId = $("#changeTopicId").val();
	var url = EDIT_TOPIC_ITEM_CHANGE.replace("{topicId}", topicId);
	url = url.replace("{topicItemId}", changeTopicItemId);
	url = url.replace("{topicItemChangeId}", selectId);
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
	var selectId = $(this).closest("tr").find("input[name='topicItemId']")
			.val();
	var topicId = $("#changeTopicId").val();
	if (null == selectId || "0" == selectId) {
	} else {
		deleteItems.push(selectId);
	}
	$(this).closest("tr").detach();
}

function initialSkuList(skuArray) {
	var topicId = $("#changeTopicId").val();
	return skuArray;
}
