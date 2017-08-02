$(function(){
/**查询库存信息**/
$("#searthAtt").on('click',function(){
		queryAttForm.submit();      
});


$(".exportButton").on('click',function(){
    $("#queryAttForm").attr("action",domain+"/storage/inventory-snapshot/export.htm");
	queryAttForm.submit();   
    $("#queryAttForm").attr("action",domain+"/storage/inventory-snapshot/list.htm");
});



	$( "#createBeginTime" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	    }
	});
	
	$( "#createEndTime" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	    }
	});

}); 