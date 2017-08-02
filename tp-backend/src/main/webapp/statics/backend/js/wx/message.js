$(function(){
	$(document.body).on('click','.editMessageBtn', function() {
		var id = $(this).attr('param');
		pageii = $.layer({
			type : 2,
			title : '编辑回复消息',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 700 ],
			iframe : {
				src : domain + '/wx/message/save?id='+id
			}
		});
	});
	
	$(document.body).on('click','.updatestatusbtn', function() {
		var current_btn = $(this);
		var msgId = $(this).attr('msgId');
		var msgStatus = $(this).attr('msgStatus');
		var statusOpert = msgStatus == 0?'发布':'冻结';
		var statusBtn = msgStatus == 0?'冻结':'发布';
		var status = msgStatus==0?1:0;
		layer.confirm('您确认'+statusOpert+'么?', function(){
			$.ajax({
				url:domain + '/wx/message/uptstatus',
				data:{id:msgId,status:status},
				type:'post',
				dataType:'json',
				success:function(result){
					if(result.success){
						pageii = layer.alert(statusOpert+"成功", 1,function(){
		        			layer.close(pageii);
							current_btn.attr('msgStatus',status);
							current_btn.attr('value',statusBtn);
							current_btn.parent().prev().prev().text(statusOpert);
		                })
					}else{
		        		layer.alert(statusOpert+"失败", 8);
		        	}
				}
			});
		},function(){});
	});
	
	var validate = $("#saveMsgForm").validate({
		debug: false, //调试模式取消submit的默认提交功能   
        onkeyup: false,   
	    rules: {
	    	name:{required:true},
	    	code:{required:true},
	    	type:{required:true}
	    },
	    messages: {
	    	name: {required:'请输入名称'},
	    	code: {required:'请选择场景'},
	    	type: {required:'请选择类型'}
	    }
	});
	
	 $("#saveMsgBtn").click(function(){
		 if(validate.form(this.form)){ //若验证通过，则调用保存/修改方法
			 saveMsg(this.form);
	      }
	});
	 
	$('#sceneSelect').change(function(){ 
		var p1=$(this).children('option:selected').val();
		$("#titleTip").html("");
		if(p1 == 'click') {
			$("#tr_key").show(); 
		}else if(p1 == 'keyword'){
			$("#titleTip").html("多个关键字用下划线_隔开");
		}else $("#tr_key").hide();
	}); 
});

function saveMsg(form){
	$.ajax({
        cache: true,
        type: "POST",
        url:domain + '/wx/message/save',
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