var refundListFnMap = null;
jQuery(function(){
	refundListFnMap = initQueryPage("rejectInfoForm","/seller/refund/refundQuery","contentShow");
	refundListFnMap.loadPage(1);

	jQuery("#refund_list_query").click(function(e){
		e.preventDefault();
		refundListFnMap.loadPage(1);
		return false;
	});
	
	
});

/**
 * 
 * @param index
 */
function _gotoPage(index){
	refundListFnMap.gotoPage(index);
}

/**
 * 显示物流
 * 
 * @param orderId
 */
function showDelivery(rejectNo,expressNo){
	showPopDiv("/seller/refund/refundDelivery",{"rejectNo":rejectNo,"expressNo":expressNo});
}

