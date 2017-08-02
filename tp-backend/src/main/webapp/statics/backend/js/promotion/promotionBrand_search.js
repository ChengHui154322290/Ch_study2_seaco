var SEARCH_TOPIC_BRAND_DETAIL = domain + "/topic/setbrand/search?";
var index = parent.layer.getFrameIndex(window.name);

$(document).ready(function() {
	$("#searchBrand").on("click", function() {
		$("#startPage").val(1);
		searchBrand();
	});
	$("#save").on("click", function() {
		var brandId = $("input[name='brandId']:checked").val();
		parent.$("#brandId").val(brandId);//编辑专题时使用
		//编辑优惠券时使用
		$("span[name=brand_span_2][current=true] .brandId",window.parent.document).val(brandId);
		//编辑优惠券不包含时使用
		$("span[name=brand_span_2_no][current=true] .brandId",window.parent.document).val(brandId);
		parent.layer.close(index);
	});
	$("#cancel").on("click", function() {
		parent.layer.close(index);
	});
});

function searchBrand(){
	var nameEn = $("#nameEn").val();
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
		nameEn : nameEn,
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
