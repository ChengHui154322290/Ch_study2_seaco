$(function(){
	
	/**查询库存信息**/
	$("#searthAtt").on('click',function(){
		queryInventoryForm.submit();     
	});
	
	$(".select2").select2();
	$(".select2").css("margin-left","1px");

	
}); 

function showRealList(sku,warehouseId){
	var date = new Date();
	var tv={
		url:"/storage/inventory/queryRealInventoryList.htm?sku="+sku+"&warehouseId="+warehouseId,
		linkid:'showRealList_'+date.getMilliseconds(),
		tabId:'showRealList_'+date.getMilliseconds(),
		text:'现货库存流水'
	};
	parent.window.showTab(tv);
}

function showOccupyList(sku){
	var date = new Date();
	var tv={
		url:'/storage/inventory/queryOccupyInventoryList.htm?sku='+sku,
		linkid:'add_detail_'+date.getMilliseconds(),
		tabId:'add_detail_'+date.getMilliseconds(),
		text:'库存占用流水'
	};
	parent.window.showTab(tv);
}