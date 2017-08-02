$(function(){
	/*if($(":text[name='birthday']").is('input')){
		$(":text[name='birthday']").datepicker({format: 'yyyy-mm-dd'});
	}*/
	$(document.body).on('click','.editVersionInfoBtn', function() {
		var id = $(this).attr('param');
		pageii = $.layer({
			type : 2,
			title : '编辑版本消息',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 700 ],
			iframe : {
				src : domain + '/app/version/save?id='+id
			}
		});
	});
	
	$(document.body).on('click','.updatestatusbtn', function() {
		var current_btn = $(this);
		var versionId = $(this).attr('versionId');
		var versionStatus = $(this).attr('versionStatus');
		var statusOpert = versionStatus == 0?'发布':'冻结';
		var statusBtn = versionStatus == 0?'冻结':'发布';
		var status = versionStatus==0?1:0;
		layer.confirm('您确认'+statusOpert+'么?', function(){
			$.ajax({
				url:domain + '/app/version/uptstatus',
				data:{id:versionId,status:status},
				type:'post',
				dataType:'json',
				success:function(result){
					if(result.success){
						pageii = layer.alert(statusOpert+"成功", 1,function(){
		        			//window.location.reload();
		        			layer.close(pageii);
							current_btn.attr('versionStatus',status);
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
	
	var validate = $("#saveVersionInfoForm").validate({
		debug: false, //调试模式取消submit的默认提交功能   
        //errorClass: "label.error", //默认为错误的样式类为：error   
        onkeyup: false,   
        /*submitHandler: function(form){   //表单提交句柄,为一回调函数，带一个参数：form  
        	
        	//savePushInfo(form);  
        }, */  
	    rules: {
	    	platform:{required:true},
	    	version:{required:true}
	    },
	    messages: {
	    	platform: {required:'请选择平台'},
	    	version:{required:'请输入版本号'}
	    }
	});
	
	 $("#saveVersionInfoBtn").click(function(){
		 if(validate.form(this.form)){ //若验证通过，则调用保存/修改方法
			 saveVersionInfo(this.form);
	      }
	});
	 
	 $(":radio[name='isNew']").click(function(){
	        var v = $("input[name='isNew']:checked").val();
	        if(v == 1){
	        	layer.confirm('您确认设置为最新版本么,是则其他更新为非最新?', function(index){
	        		layer.close(index);
	        	},function(){
	        		$("#radioNew2").attr("checked","checked");
	        	});
	        }
	 });
});

function saveVersionInfo(form){
	$.ajax({
        cache: true,
        type: "POST",
        url:domain + '/app/version/save',
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
