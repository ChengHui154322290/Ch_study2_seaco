var pageii;
$(function(){
		if($("#createBeginTime").length>0){
			$( "#createBeginTime" ).datepicker({
				dateFormat:'yy-mm-dd',
				onClose: function( selectedDate ) {
			        $( "#createEndTime" ).datepicker( "option", "minDate", selectedDate );
			    }
			});
		};
	
	 if($("#createEndTime").length>0){
		$( "#createEndTime" ).datepicker({
			dateFormat:'yy-mm-dd',
			onClose: function( selectedDate ) {
		        $( "#createBeginTime" ).datepicker( "option", "maxDate", selectedDate );
		    }
		});
		};

		$('.closebtn').on('click',function(){
			parent.layer.close(parent.pageii);
		});

	$("#auditBtn").click(function(){
		var sellerSkuId=$("#sellerSkuId").val();
		  pageii=$.layer({
	            type : 2,
	            title: '商家商品->审核',
	            shadeClose: true,
	            maxmin: true,
	            fix : false,  
	            area: ['700px', 500],                     
	            iframe: {
	                src : domain+'/item/seller_item_audit.htm?sellerSkuId='+sellerSkuId
	            } 
	        });
		
	});
	
	
	
	$("#datasubmit").click(function(){
		  $.ajax({ 
				url: domain+'/item/sellerItemAudit.htm', 
				dataType: 'json',
				data: $('#auditForm').serialize(), 
				type: "post", 
				cache : false, 
				success: function(data) {
					if(data.success){
						layer.alert('操作成功', 4,function(){
			    		    var grandParentId=$("#parentFrameId",window.parent.document).val();
						    window.parent.parent.showIframe(grandParentId,domain+"/item/seller_skuInfo_list.htm");
							var parentFrameId = window.parent.frameElement && window.parent.frameElement.id ||'';
							parentFrameId = parentFrameId.toString().replace("mainIframe_","");
							window.parent.parent.closeTab(parentFrameId);
						});
					}else{
						layer.alert(data.msg.message, 8);
					}
				}	
		  });	
	});
	
	$("#checkAll").click(function() {
        $('input[name="listRejectKey"]').attr("checked",this.checked); 
    });
	
    var $subBox = $("input[name='listRejectKey']");
    $subBox.click(function(){
        $("#checkAll").attr("checked",$subBox.length == $("input[name='listRejectKey']:checked").length ? true : false);
    });

    $("input[name=auditResult]").click(function(){
    	switch($("input[name=auditResult]:checked").attr("id")){
    	  case "at3":
    		    $('input[name="listRejectKey"]').attr("checked",false);
    	        $("#checkAll").attr("checked",false);
    	        $('input[name="listRejectKey"]').attr("disabled",true);
    	        $("#checkAll").attr("disabled",true);
    	   break;
    	  case "at4":
    		    $('input[name="listRejectKey"]').attr("checked",false);
    	        $("#checkAll").attr("checked",false);
    	        $('input[name="listRejectKey"]').attr("disabled",false);
    	        $("#checkAll").attr("disabled",false);
    	   break;
    	  default:
    	   break;
    	 }
     });
	
});


function showAduitPage(id){
	var parentFrameId = window.frameElement && window.frameElement.id ||'';
	var date = new Date();
	var dateMill = date.getMilliseconds();
	tv={
	//	url:'/item/seller_item_info.htm?sellerSkuId='+id+'&dateMill='+dateMill,
		url:'/item/seller_item_info.htm?sellerSkuId='+id+'&parentFrameId='+parentFrameId,
		linkid:'add_detail_'+dateMill,
		tabId:'add_detail_'+dateMill,
		text:'待审核商品信息'
	};
	parent.window.showTab(tv);
}