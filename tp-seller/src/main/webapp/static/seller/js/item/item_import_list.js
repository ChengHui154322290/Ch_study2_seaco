var orderListFnMap = null;
jQuery(function(){
	orderListFnMap = initQueryPage("importLogForm","/seller/item/itemImportQuery","contentShow");
	orderListFnMap.loadPage(1);
	
	jQuery("#itemSearchForm").click(function(e){
		e.preventDefault();
		orderListFnMap.loadPage(1);
		return false;
	});
	jQuery("#queryImportLogBtn").click(function(e){
		e.preventDefault();
		orderListFnMap.loadPage(1);
		return false;
	});
	
	
	$('#uploadFileBtn').on('click',function(){
		  pageii=$.layer({
          type : 2,
          title: '上传文件',
          shadeClose: true,
          maxmin: true,
          fix : false,  
          area: ['600px','300px', 300],                     
          iframe: {
              src : domain+'/seller/item/uploadFile.htm'
          } 
      });
	}); 
	
});

/**
 * 
 * @param index
 */
function _gotoPage(index){
	orderListFnMap.gotoPage(index);
}

/**
 * 订单详情tab
 * 
 * @param orderId
 */
function showOrderTab(orderId){
	newTab("order_list_detail","订单详情","/seller/order/orderDetails?oid="+orderId);
}

/**
 * 显示物流
 * 
 * @param orderId
 */
function showDelivery(orderId){
	showPopDiv("/seller/order/orderDelivery",{"orderId":orderId});
}
