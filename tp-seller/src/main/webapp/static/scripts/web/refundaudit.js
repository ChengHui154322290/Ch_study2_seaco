$(function(){
	$('.rejectbtnlist').on('click','.submitreject',function(){
		
		$("input:hidden[name=success]").val($(this).attr('suc'));
		/*if(isNaN($("input[name='amount']").val())){
			//layer.alert("退款数量必须为数字",8);
			alertMsg("退款数量必须为数字");
			return ;
		}
*/
		ajaxRequest({
			method:'post',
			data:$("#rejectaudit").serialize(),
			url:'/seller/refund/refundAudit',
			success:function(ret){
				if(ret.success  && 'false' != ret.success){
					alertMsg("审核成功。");
					
				}else{
					alertMsg(ret.msg.message);
					//layer.alert(ret.data,8);
				}
				hidePopPage();
				refundListFnMap.loadPage(1);
			}
		})
		
	});
	
	
	if($('.submitreject').is(':button')){
		
	}else{
		var amount = $('input:text[name=amount]');
		amount.parent().html(amount.val());
	}
	 /*$(".list_table").colResizable({
         liveDrag:true,
         gripInnerHtml:"<div class='grip'></div>", 
         draggingClass:"dragging", 
         minWidth:30
       }); */
});
