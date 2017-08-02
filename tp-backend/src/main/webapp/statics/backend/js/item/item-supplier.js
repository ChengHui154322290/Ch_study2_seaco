/**
 * 供应商与商户的维护关系
 */
$(function(){
	/*
       自营(西客商城) ： 代销 sell , 自营  Purchase
       商家：  联营 Associate         
	*/
	var supplierTypeContainer= $('#supplierTypeQuery');
	var hasXgSeller = $('#hasXgSeller').val();
	var supplierTypeQueryHidden = $('#supplierTypeQueryHidden').val();
	initSupplierTypeQuery(hasXgSeller);
	$('#selectSeller').change(function(){
		var val = $(this).val();
		supplierTypeContainer.empty();
		if(val==0){
			var option = $("<option />");
			option.html("全部");
			option.val("");
			supplierTypeContainer.append(option);
			option = $("<option />");
			option.html("自营");
			option.val("Purchase");
			supplierTypeContainer.append(option);
			option = $("<option />");
			option.html("代销");
			option.val("sell");
			supplierTypeContainer.append(option);
		}else{
			var option = $("<option />");
			option.html("联营");
			option.val("Associate");
			supplierTypeContainer.append(option);
		}
	});
	
	function initSupplierTypeQuery(hasXgSeller){
		if("Associate"==supplierTypeQueryHidden){
			var option = $("<option />");
			option.html("联营");
			option.val("Associate");
			supplierTypeContainer.append(option);
		}else{
			if(hasXgSeller&&hasXgSeller==1){
				var option = $("<option />");
				option.html("联营");
				option.val("Associate");
				supplierTypeContainer.append(option);
			}else{
				var option = $("<option />");
				option.html("全部");
				option.val("");
				supplierTypeContainer.append(option);
				if("Purchase"==supplierTypeQueryHidden){
					option = $("<option selected/>");
				}else{
					option = $("<option />");
				}
				option.html("自营");
				option.val("Purchase");
				supplierTypeContainer.append(option);
				
				if("sell"==supplierTypeQueryHidden){
					option = $("<option selected/>");
				}else{
					option = $("<option />");
				}
				option.html("代销");
				option.val("sell");
				supplierTypeContainer.append(option);
			}
		}
	}
});