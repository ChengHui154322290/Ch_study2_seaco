var SEARCH_SKU = domain + "/topicItem/SKU/search?";
var index = parent.layer.getFrameIndex(window.name);
$(document).ready(function() {

	$("#barcodeSearch").on("click", function() {
		var barCode = $("#barcode").val();
		var spu = parent.$("#preSpu").val();
		var supplierId = parent.$("#preSupplierId").val();
		var parentBrandId = $("#parentBrandId").val();
		if(null == barCode || 0 == $.trim(barCode).length){
			layer.alert("请输入条码");
			return;
		}
		var data = syncGet(SEARCH_SKU + new Date().getTime(), {
			barCode : barCode,
			sku : "",
			spu : spu,
			brandId : parentBrandId,
			supplierId : supplierId
		});
		$("#topicItemSearchList").html(data);
		$("input[name='skuId']").on("click", function() {
			$("#skuId").val($("input[name='skuId']:checked").val());
		})
	});
	$("#skuSearch").on("click", function() {
		var sku = $("#sku").val();
		var spu = parent.$("#preSpu").val();
		var parentBrandId = $("#parentBrandId").val();
		var supplierId = parent.$("#preSupplierId").val();
		if(null == sku || 0 == $.trim(sku).length){
			layer.alert("请输入sku");
			return;
		}
		var data = syncGet(SEARCH_SKU + new Date().getTime(), {
			barCode : "",
			sku : sku,
			spu : spu,
			brandId : parentBrandId,
			supplierId : supplierId
		});
		$("#topicItemSearchList").html(data);
		$("input[name='skuId']").on("click", function() {
			$("#skuId").val($("input[name='skuId']:checked").val());
		})
	});

	$("#save").on("click", function() {
		var selectSku = $("#skuId").val();
		if(null == selectSku || "" == $.trim(selectSku)){
			return;
		}
		parent.$("#skuId").val($("#skuId").val());
		parent.layer.close(index);
	});

	$("#cancel").on("click", function() {
		parent.layer.close(index);
	});

	$("input[name='skuId']").on("click", function() {
		$("#skuId").val($("input[name='skuId']:checked").val());
	})
});