var SEARCH_TOPIC_BRAND_DETAIL = domain + "/topic/setbrand/search?";
var index = parent.layer.getFrameIndex(window.name);

$(document).ready(function() {
	initSelect2();
	$("#save").on("click", function() {
		var supplierId = $("select[name='supplier']").val();
		var supplierName =  $("select[name='supplier']").find("option:selected").attr("suppName");
		if(supplierId==null || supplierId=="" || supplierId==undefined || supplierName=="" || supplierName==null){
			layer.msg("请选择商家");
			return;
		}
		$("#supplierId",window.parent.document).val(supplierId);
		$("#supplierName",window.parent.document).val(supplierName);

		$.get(QUERY_SUPPLIER_SHOP,{supplierId:supplierId},function (data) {
			if(data.success && data.data != null){
				var des = data.data.introMobile;
				parent.phoneEditor.html(des);
			}else {
				parent.phoneEditor.html("");
			}
			parent.layer.close(index);
		});



	});
	$("#cancel").on("click", function() {
		parent.layer.close(index);
	});

});



function initSelect2(){
	$(".select2").select2();
	// $(".select2").css("margin-left","1px");
}