$(function() {

	$("#wareHouseDODataList tr:even").css("background-color", "#ecf6fc");
	
	$("#wareHouseDODataList tr:even").attr("bg", "#ecf6fc");
	$("#wareHouseDODataList tr:odd").attr("bg", "#ffffff");
	
	$("#wareHouseDODataList tr").mouseover(function() {
		$(this).css("background", "#bcd4ec");
	}).mouseout(function() {
		var bgc = $(this).attr("bg");
		$(this).css("background", bgc);
	});
	
	//delete warehourse 
	//judge warehouse status 
	//only no stock can be deleted 
	$('#deleteWareHourse').live('click',function(){
//		alert('亲,下个版本上线!');
//		return ;
		var id = $(this).attr('param');
		$.get("delete.htm",{id:id},function(data){
			if(data.success){//成功
				layer.alert('操作成功', 4,function(){
					location.href='list.htm';
				});
		    }else{
		    	layer.alert(data.msg.message, 8);
		    }	
		});
	});
	
	
	/**
	 * 显示返回结果。
	 */
	
});