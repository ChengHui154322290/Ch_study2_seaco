$(function(){
	var validate = $("#globalcommisonForm").validate({
		debug: false, //调试模式取消submit的默认提交功能   
        //errorClass: "label.error", //默认为错误的样式类为：error   
        onkeyup: false,   
        submitHandler: function(form){   //表单提交句柄,为一回调函数，带一个参数：form   
        	$.layer({
        	    shade: [0],
        	    area: ['auto','auto'],
        	    dialog: {
        	        msg: '你确认要编辑全局佣金比率？',
        	        btns: 2,                    
        	        type: 4,
        	        btn: ['是的','点错了'],
        	        yes: function(){
        	        	$.ajax({
        	                cache: true,
        	                type: "POST",
        	                url:domain + '/dss/globalcommision/insert',
        	                data:$(form).serialize(),
        	                async: false,
        	                error: function(request) {
        	                    alert("Connection error");
        	                },
        	                success: function(result) {
        	                	if(result.success){
        	                		layer.alert("保存成功", 1, function(){
        	                			window.location.reload();
        	                        })
        	                	}else{
        	                		layer.alert("保存失败", 8);
        	                	}
        	                }
        	            });
        	        }, no: function(){
        	        	layer.msg('还能愉快的玩耍吗？', 1,12);
        	        }
        	    }
        	});
        	
        },   
	    rules: {
	    	firstCommisionRate: {required:true,minlength:1,maxlength:5,range:[1.00,50.00]},
	    	secondCommisionRate: {required:true,minlength:1,maxlength:5,range:[1.00,50.00]},
	    	threeCommisionRate: {required:true,minlength:1,maxlength:5,range:[1.00,50.00]},
	    	scanCommisionRate: {required:true,minlength:1,maxlength:5,range:[1.00,100.00]}
	    },
	    messages: {
	    	firstCommisionRate: {required:'请输入一级佣金比率',minlength:'佣金比率必填',maxlength:'佣金比率过大',range:'佣金比率在{0}~{1}之间，保留两位小数'},
	    	secondCommisionRate: {required:'请输入二级佣金比率',minlength:'佣金比率必填',maxlength:'佣金比率过大',range:'佣金比率在{0}~{1}之间，保留两位小数'},
	    	threeCommisionRate: {required:'请输入三级佣金比率',minlength:'佣金比率必填',maxlength:'佣金比率过大',range:'佣金比率在{0}~{1}之间，保留两位小数'},
	    	scanCommisionRate: {required:'请输入扫码佣金比率',minlength:'佣金比率必填',maxlength:'佣金比率过大',range:'佣金比率在{0}~{1}之间，保留两位小数'}
	    }
	});	
})