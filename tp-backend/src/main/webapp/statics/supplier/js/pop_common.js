/**
 * 注册供应商弹出按钮
 */
function registerSupplierPop(fn,params1){
	//供应商查询按钮
	jQuery("#all_page_add_supplier_query").click(function(){
		var paramsMap = {};
		if(params1){
			paramsMap = jQuery.extend(paramsMap,params1);
		}
		// by zhs 01151822 传入supplierType
		//var supplierType = jQuery("#supplierType").val();
		ajaxRequest({
			method:'post',
			data:paramsMap,
			url:'/supplier/getSuppliers.htm',
			//data:{"supplierType":supplierType},
			success:function(data){
				showPopDiv(6,data,{"title":"选择供应商","callBackFn":fn});
			}
		})
	});
	
	//供应商确认按钮
	jQuery("#all_page_add_supplier_confirm").click(function(){
		var spName = jQuery("#all_page_add_supplier_name").val();
		var spId = jQuery("#all_page_add_supplier_id").val();
		if(!spId && !spName){
			alertMsg('请填写供应商id或者名称。');
			return;
		}
		var supplierType = jQuery("#supplierType").val();
		// by zhs 01142036 原data:{"supplierName":spName,"supplierId":spId
		ajaxRequest({
			method:'post',
			url:'/supplier/checkSupplierInfo.htm',
			data:{"name":spName,"id":spId,"supplierType":supplierType},
			success:function(data){
				data = eval('('+data+')');
				if(data.success){
					var supplier = data.data;
					jQuery("#all_page_add_supplier_id").val(supplier.id);
					jQuery("#all_page_add_supplier_id_hidden").val(supplier.id);
					jQuery("#all_page_add_supplier_name").val(supplier.name);
					jQuery("#all_page_add_supplier_type").val(supplier.supplierType);
					if(fn){
						fn(supplier);
					}
				} else {
					if (supplierType == 'Purchase') {
						alertMsg('自营供应商不存在。');
					} else if (supplierType == 'sell') {
						alertMsg('代销供应商不存在。');
					}else{
						alertMsg('供应商不存在。');
					}
					return;
				}
			}
		})
	});
}
