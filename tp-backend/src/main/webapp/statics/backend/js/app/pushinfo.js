$(function(){
	if($(":text[name='sendDate']").is('input')){
		$(":text[name='sendDate']").datetimepicker({format: 'yyyy-MM-dd HH:mm'});
	}
	$(document.body).on('click','.editPushInfoBtn', function() {
		var id = $(this).attr('param');
		pageii = $.layer({
			type : 2,
			title : '编辑推送消息',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 700 ],
			iframe : {
				src : domain + '/app/push/save?id='+id
			}
		});
	});
	$(document.body).on('click','.reSendMsgBtn', function() {
		var id = $(this).attr('param');
		$.get(domain + '/app/push/send?id='+id,function(data,status){
			if(data.success){
        		layer.alert("发送成功", 1, function(){
        			window.location.reload();
        			layer.close(parent.pageii);
                })
        	}else{
        		layer.alert("发送失败", 8);
        	}
		});
	});
	
	var validate = $("#savePushInfoForm").validate({
		debug: false, //调试模式取消submit的默认提交功能   
        //errorClass: "label.error", //默认为错误的样式类为：error   
        onkeyup: false,   
        /*submitHandler: function(form){   //表单提交句柄,为一回调函数，带一个参数：form  
        	
        	//savePushInfo(form);  
        }, */  
	    rules: {
	    	title:{required:true},
	    	pushType:{required:true},
	    	platform:{required:true},
	    	pushWay:{required:true},
	    	activeTime:{required:true},
	    	pushTarget:{required:true}
	    },
	    messages: {
	    	title: {required:'请输入标题'},
	    	pushType: {required:'请选择推送类型'},
	    	platform: {required:'请选择推送平台'},
	    	pushWay: {required:'请选择推送方式'},
	    	activeTime: {required:'请设置有效时长'},
	    	pushTarget: {required:'请选择推送目标'}
	    }
	});
	
	 $("#savePushInfoBtn").click(function(){
		 if(validate.form(this.form)){ //若验证通过，则调用保存/修改方法
			 savePushInfo(this.form);
	      }
	});
	 
	$("#sendMessageBtn").click(function(){
		 if(validate.form(this.form)){ //若验证通过，则调用保存/修改方法
			 sendMessage(this.form);
	      }
	});
	
	$('#pushType').change(function(){ 
		var p1=$(this).children('option:selected').val();
		if(p1 == '0') {
			$("#tr_topic").val('').hide();
			$("#tr_sku").val('').hide(); 
			$("#tr_link").show();
		}
		if(p1 == '1') {
			$("#tr_link").val('').hide(); 
			$("#tr_topic").show();
			$("#tr_sku").val('').hide(); 
		} 
		if(p1 == '2'){
			$("#tr_link").val('').hide(); 
			$("#tr_topic").show();
			$("#tr_sku").show();
		}
		if(/^[456789]$/.test(p1) || p1=='10' || p1=='11'){
			$("#tr_link").val('').hide(); 
			$("#tr_topic").val('').hide();
			$("#tr_sku").val('').hide();
		}
	}); 
	
	$('#pushTarget').change(function(){ 
		var p1=$(this).children('option:selected').val();
		if(p1 == '1') $("#tr_clientId").show(); 
		else $("#tr_clientId").hide(); 
	}); 
	
	$(":radio[name='pushWay']").click(function(){
        var v = $("input[name='pushWay']:checked").val();
        if(v == 1)$(":text[name=sendDate]").val('').hide(); 
        else $(":text[name=sendDate]").show(); 
   });
});

function savePushInfo(form){
	$.ajax({
        cache: true,
        type: "POST",
        url:domain + '/app/push/save',
        data:$(form).serialize(),
        async: false,
        error: function(request) {
            alert("Connection error");
        },
        success: function(result) {
        	if(result.success){
        		layer.alert("保存成功", 1, function(){
        			parent.window.location.reload();
        			parent.layer.close(parent.pageii);
                })
        	}else{
        		layer.alert(result.msg.message, 8);
        	}
        }
    });
}

function sendMessage(form){
	$.ajax({
        cache: true,
        type: "POST",
        url:domain + '/app/push/send',
        data:$(form).serialize(),
        async: false,
        error: function(request) {
            alert("Connection error");
        },
        success: function(result) {
        	if(result.success){
        		layer.alert("发送成功", 1, function(){
        			parent.window.location.reload();
        			parent.layer.close(parent.pageii);
                })
        	}else{
        		layer.alert(result.msg.message, 8);
        	}
        }
    });
}
