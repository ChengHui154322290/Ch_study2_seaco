var orderListFnMap = null;
jQuery(function(){
	orderListFnMap = initQueryPage("itemSearchForm","/seller/item/itemQuery","contentShow");
	orderListFnMap.loadPage(1);

	//绑定导入事件
	jQuery("#itemImport").click(function(e){
		newTab("item_import","商品导入","/seller/item/import.htm");
	});
	
	//绑定新增事件事件
	jQuery("#addItem").click(function(e){
		newTab("item_import","商品新增","/seller/item/add.htm");
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


function show(id){
	var date = new Date();
	var tv={
		url:'/item/detail.htm?detailId='+id,
		linkid:'add_detail_'+date.getMilliseconds(),
		tabId:'add_detail_'+date.getMilliseconds(),
		text:'编辑sku信息'
	};
//	parent.window.showTab(tv);
	 newTab("item_sku","编辑sku信息","/seller/item/detail.htm?detailId="+id);
}

function newPrd(id){
	var date = new Date();
	var tv={
		url:'/item/queryPrd.htm?itemId='+id,
		linkid:'query_prd_'+date.getMilliseconds(),
		tabId:'query_prd_'+date.getMilliseconds(),
		text:'新增prd'
	};
//	parent.window.showTab(tv);
	newTab("item_sku","新增prd","/seller/item/queryPrd.htm?itemId="+id);
	
}