$(function(){
	$( "#createBeginTime" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	        $( "#createEndTime" ).datepicker( "option", "minDate", selectedDate );
	    }
	});
	$( "#createEndTime" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	        $( "#createBeginTime" ).datepicker( "option", "maxDate", selectedDate );
	    }
	});
});

/**
 * list
 * listExport
 */
function itemFormSubmit(oper){
	if(oper=="list"){
		$('#itemSearchForm').attr("action","list.htm");
		$('#itemSearchForm').submit();
	}else{
		$('#itemSearchForm').attr("action","exportList.htm");
		$('#itemSearchForm').submit();
		
	}
}

function itemDetailFormSubmit(oper){
	$('#itemSearchForm').attr("action","loadItemDetail.htm");
	$('#itemSearchForm').submit();
	
}


function previewSku(){
	

	//alert("亲，下个版本再上线哦！");
	//return ;
	var detailIdArry = new Array();
	$(".detailIdCheckbox").each(function(){
		if ($(this).attr('checked')) {
			detailIdArry.push($(this).val());
		}
	});
	
	//校验
	if(detailIdArry.length==0){
		alert("请先选择商品，再做批量上下架");
		return ;
	}
	
	
	
	var detailIds = detailIdArry.join(",");
	var status = $("#itemStatus").val();
	
	var src = domain+'/item/previewSku.htm?detailIds='+detailIds+"&status=" + status ; 
	pageii=$.layer({
        type : 2,
        title: '商品-批量上下架',
        shadeClose: true,
        maxmin: true,
        fix : false,  
        area: ['850px', 600],                     
        iframe: {
            src : src
        } 
    });
	
	
	
}

function batchEdit(){
	var src = domain+'/item/batchEdit.htm';
	pageii=$.layer({
        type : 2,
        title: '商品-批量修改',
        shadeClose: true,
        maxmin: true,
        fix : false,  
        area: ['750px', 650],                     
        iframe: {
            src : src
        } 
    });
}