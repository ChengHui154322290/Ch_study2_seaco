$(function() {

	$("#list_table tr:even").css("background-color", "#ecf6fc");
	
	$("#list_table tr:even").attr("bg", "#ecf6fc");
	$("#list_table tr:odd").attr("bg", "#ffffff");
	
	$("#list_table tr").mouseover(function() {
		$(this).css("background", "#bcd4ec");
	}).mouseout(function() {
		var bgc = $(this).attr("bg");
		$(this).css("background", bgc);
	});
	
	$( "#startCreateTimeQo" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	    }
	});
	
	$( "#endCreateTimeQo" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	    }
	});
	
});

//function addTab(id,text,tabUrl){
//	var tv = {};
//	tv.linkId = id+"_link";
//	tv.tabId =  id;
//	tv.url = tabUrl;
//	tv.text = text;
//	try{
//		window.parent.showTab(tv);
//	} catch(e){
//	}
//}

