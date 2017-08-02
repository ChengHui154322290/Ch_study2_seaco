$(function(){
	/**
	 * 全选/取消全选
	 */
	$('#chkall').click(function() {
		if ($(this).attr('checked')) {
			$("input[name='detailId']").attr("checked", true);
		} else {
			$("input[name='detailId']").attr("checked", false);
		}
	});
	
	$('#copyItemBtn').live('click',function(){
		var flag = true;
		$("input[name='detailId']").each(function(){
			if($(this).attr('checked')!=undefined){
				flag = false;
				return ;
			}
		});
		if(flag){
			alert("复制商品前，先选择商品");
			return ;
		}
		$('#itemSearchForm').attr('action','copy.htm');
		$('#itemSearchForm').submit();
	}); 	
});