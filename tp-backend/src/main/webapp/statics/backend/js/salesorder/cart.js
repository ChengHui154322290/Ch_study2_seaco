$(function(){
	$(document.body).on('click','.deletecartitem',function(){
		var skuCode = $(this).attr('skucode');
		var topicId = $(this).attr('topicid');
		var memberId = $(this).attr('memberid');
		
		$.layer({
        	    shade: [0],
        	    area: ['auto','auto'],
        	    dialog: {
        	        msg: '你确认要购物车中的这条商品信息？',
        	        btns: 2,                    
        	        type: 4,
        	        btn: ['是的','点错了'],
        	        yes: function(){
        	        	$.ajax({
        	                cache: true,
        	                type: "POST",
        	                url:domain + '/salesorder/cart/deletecartitem',
        	                data:{skuCode:skuCode,topicId:topicId,memberId:memberId},
        	                async: false,
        	                error: function(request) {
        	                    alert("Connection error");
        	                },
        	                success: function(result) {
        	                	if(result.success){
        	                		layer.alert("删除成功", 1, function(){
        	                			$('form#cartSearchForm').submit();
        	                        })
        	                	}else{
        	                		layer.alert(result.msg.message, 8);
        	                	}
        	                }
        	            });
        	        }, no: function(){
        	        	layer.msg('还能愉快的玩耍吗？', 1,12);
        	        }
        	    }
        	});
	});
	
	$( ".membername" ).autocomplete({
		source: function (request, response) {
            var term = request.term;
            $.post(domain+'/user/querymemberinfobylike.htm', {
            	likeName:term
              }, response,'json');
        },
		max:10,
		minLength: 2,
		select: function( event, ui ) {
			 $(":hidden[name='memberId']").val(ui.item.id);
			 $("input.membername").val(ui.item.mobile);
			 return false;
		},
		messages: {
		  noResults: ''
		}
	}).on('blur',function(){
		if($(this).val()=='' || $(this).val()==null){
			$(":hidden[name='memberId']").val('');
		}
	});
	$(".membername").data("autocomplete")._renderItem = function (ul, item) {
	    return $("<li></li>")
	        .data("item.autocomplete", item)
	        .append("<a>" + item.nickName +",手机："+item.mobile+"</a>")
	        .appendTo(ul);
	};
})