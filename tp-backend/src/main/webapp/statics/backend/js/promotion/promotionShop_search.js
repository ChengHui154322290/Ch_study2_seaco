var QUERY_SUPPLIER_SHOP = domain +"/topic/supplier/querySupplierShop";


function querySupplierShop(supplierId) {
    if(supplierId==null || supplierId=="") return;
    $.get(QUERY_SUPPLIER_SHOP,{supplierId:supplierId},function (data) {
        if(data.success && data.data != null){
            var des = data.data.introMobile;
            phoneEditor.html(des);
        }else {
            phoneEditor.html("");
        }
    });


}