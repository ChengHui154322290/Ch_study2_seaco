$(function(){
	$('.rejectbtnlist').on('click','.submitreject',function(){
		$("input:hidden[name=success]").val($(this).attr('suc'));
		if(isNaN($("input[name='amount']").val())){
			layer.alert("退款数量必须为数字",8);
			return ;
		}
		$.ajax({
			type : "POST",
			url : domain + "/customerservice/reject/audit.htm",
			data : $("#rejectaudit").serialize(),
			error : function(request) {
				alert("Connection error");
			},
			success : function(ret) {
				if(ret.success){
					layer.alert("审核成功",1, function(){
		            	parent.window.location.reload();
		            	parent.layer.close(parent.pageii);
		                });
				}else{
					layer.alert(ret.data,8);
				}
			}
		});
	});
	
	
	$('.show_bar_btn').on('click','.showdelivery',function(){
		var expressNo = $(this).attr('expressNo');
		var rejectNo = $(this).attr('rejectNo');
		pageii=$.layer({
			  type : 2,
	          title: '快递单号 【'+expressNo+'】',
	          shadeClose: true,
	          maxmin: true,
	          fix : false,  
	          area: ['900px', 400],                     
	          iframe: {
	              src : domain+'/customerservice/reject/showDelivery.htm?expressNo='+expressNo+'&rejectNo='+rejectNo
	          } 
		});
		
	});

	$('.search_bar_btn').on('click','.queryrejectinfobtn',function(){
		  var rejectId = $(this).attr('rejectid');
		  var rejectNo = $(this).attr('rejectno');
		  var operate = $(this).attr('operate');
		  pageii=$.layer({
          type : 2,
          title: '退货编号 【'+rejectNo+'】',
          shadeClose: true,
          maxmin: true,
          fix : false,  
          area: ['1000px', 600],                     
          iframe: {
              src : domain+'/customerservice/reject/'+operate+'.htm?rejectId='+rejectId
          } 
      });
	}); 
	
	if($('.submitreject').is(':button')){
		
	}else{
		var amount = $('input:text[name=amount]');
		amount.parent().html(amount.val());
	}
	 $(".list_table").colResizable({
         liveDrag:true,
         gripInnerHtml:"<div class='grip'></div>", 
         draggingClass:"dragging", 
         minWidth:30
       });
	 
	 $('[data-pic]').click(function(){
		 var src = $(this).attr('data-pic');
		 var pic = $('<img>').attr('src', src);
		 var div = $('<div>').css({
			 'position' : 'fixed',
			 'left' : 0,
			 'top' : 0,
			 'width' : '100%',
			 'height' : '100%',
			 'line-height' : $(top).height() + 'px',
			 'text-align' : 'center',
			 'background' : 'rgba(0,0,0,.5)'
		 }).click(function(){
			 $(this).remove();
		 }).append(pic);
		 $(top.document.body).append(div);
	 })
});

   