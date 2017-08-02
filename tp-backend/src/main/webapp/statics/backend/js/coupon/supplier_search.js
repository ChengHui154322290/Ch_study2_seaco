var SEARCH_TOPIC_BRAND_DETAIL = domain + "/coupon/supplier/search?";
var index = parent.layer.getFrameIndex(window.name);

$(document).ready(function() {
	$("#searchBrand").on("click", function() {
		$("#startPage").val(1);
		searchBrand();
	});
	$("#save_source").on("click", function() {
		var brandId = $("input[name='brandId']:checked").val();
		parent.$("#brandId").val(brandId);
		//编辑优惠券时使用
		//$("span[name=brand_span_2][current=true] .brandId",window.parent.document).val(brandId);
		parent.$("#source_id").val(brandId);
		//parent.$("#source_name").val(brandId);
		parent.layer.close(index);
	});
	$("#cancel").on("click", function() {
		parent.layer.close(index);
	});
});

function searchBrand(){
	var id = $("#id").val();
	var name = $("#name").val();
	var startPage = $("#startPage").val();
	var pageSize = $("#pageSize").val();
	if(null == startPage || $.trim(startPage).length == 0){
		startPage = 1;
	}
	if(null == pageSize || $.trim(pageSize).length == 0){
		pageSize = 10;
	}
	$("#BrandList").html("");
	var data = syncPost(SEARCH_TOPIC_BRAND_DETAIL + new Date().getTime(), {
		supplierId : id,
		name : name,
		startPage : startPage,
		pageSize : pageSize
	});
	$("#BrandList").html(data);
	$("#nextPage").on("click", function() {
		var startPage = $("#startPage").val();
		var totalPage = $("#totalPage").text();
		var nextPageNo = 0;
		if(null != startPage){
			nextPageNo =  parseInt(startPage) + 1;
		}
		if(null != totalPage && nextPageNo > parseInt(totalPage)){
			layer.alert("已经最后一页了");
			return;
		}
		$("#startPage").val(nextPageNo);
		searchBrand();
	});
	$("#prePage").on("click", function() {
		var startPage = $("#startPage").val();
		var totalPage = $("#totalPage").text();
		var prePageNo = 0;
		if(null != startPage){
			prePageNo =  parseInt(startPage) - 1;
		}
		if(null != totalPage && prePageNo < 1){
			layer.alert("已经第一页了");
			return;
		}
		$("#startPage").val(prePageNo);
		searchBrand();
	});
	$("#pageSize").on("change", function() {
		$("#startPage").val(1);
		searchBrand();
	});
}
