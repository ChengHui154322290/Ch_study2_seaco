$(function(){
	/*if($(":text[name='birthday']").is('input')){
		$(":text[name='birthday']").datepicker({format: 'yyyy-mm-dd'});
	}*/
	$(document.body).on('click','.editWhiteInfoBtn', function() {
		var id = $(this).attr('param');
		pageii = $.layer({
			type : 2,
			title : '编辑白名单',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 700 ],
			iframe : {
				src : domain + '/mem/white/save?id='+id
			}
		});
	});
	
	$(document.body).on('click','.updatestatusbtn', function() {
		var current_btn = $(this);
		var whiteId = $(this).attr('whiteId');
		var whiteStatus = $(this).attr('whiteStatus');
		var statusOpert = whiteStatus == 0?'解冻':'冻结';
		var statusBtn = whiteStatus == 0?'冻结':'解冻';
		var statusText = whiteStatus == 0?'正常':'冻结';
		var status = whiteStatus==0?1:0;
		layer.confirm('您确认'+statusOpert+'么?', function(){
			$.ajax({
				url:domain + '/mem/white/uptstatus',
				data:{id:whiteId,status:status},
				type:'post',
				dataType:'json',
				success:function(result){
					if(result.success){
						pageii = layer.alert(statusOpert+"成功", 1,function(){
		        			//window.location.reload();
							layer.close(pageii);
							current_btn.attr('whiteStatus',status);
							current_btn.attr('value',statusBtn);
							//prev() 当前单元格左移的单元格  如果多个就直接叠加prev().prev()
							current_btn.parent().prev().text(statusText);
		                })
					}else{
		        		layer.alert(statusOpert+"失败", 8);
		        	}
				}
			});
		},function(){});
	});
	
	jQuery.validator.addMethod("isMobile", function(value, element) {
	    var length = value.length;
	    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
	    return this.optional(element) || (length == 11 && mobile.test(value));
	}, "请正确填写您的手机号码");
	
	var validate = $("#saveWhiteInfoForm").validate({
		debug: false, //调试模式取消submit的默认提交功能   
        //errorClass: "label.error", //默认为错误的样式类为：error   
        onkeyup: false,   
        /*submitHandler: function(form){   //表单提交句柄,为一回调函数，带一个参数：form  
        	
        	//savePushInfo(form);  
        }, */  
	    rules: {
	    	mobile:{
	    		required:true,
	    		minlength : 11,
	    		isMobile : true,
	    		 remote: {
                     url: domain + '/mem/isExist',
                     type: "post",
                     dataType: "text",
                     data:{
                    	 mobile: function() {
                             return $("#mobile").val();
                         }
                     },
                     dataFilter: function (result) {
                    	 var r = jQuery.parseJSON(result);
                    	 if(r.success){
                    		 return r.data;
                    	 }
                     }
                 }
	    	},
	    	level:{required:true}
	    },
	    messages: {
	    	mobile: {
	    		required:'手机号不能为空',
	    		 minlength : "确认手机不能小于11个字符",
	             isMobile : "请正确填写您的手机号码",
	             remote : "手机未注册"
	    			},
	    	level:{required:'级别不能为空'}
	    }
	});
	
	 $("#saveWhiteInfoBtn").click(function(){
		 if(validate.form(this.form)){ //若验证通过，则调用保存/修改方法
			saveWhiteInfo(this.form);
	      }
	});
	 
	 $("#mobile").blur(function(){
		 var mobile = $("#mobile").val();
		 $("#oldMobile").val(mobile);
		 queryAddressByMobile(mobile);
	});
	 
	 
	 $('#consigneeAddressSelect').change(function(){ 
			var p1=$(this).children('option:selected').text();
			$("#consigneeAddress").val(p1);
			var p2=$(this).children('option:selected').val();
			if(p2 == 0){
				 var mobile = $("#mobile").val();
				 queryAddressByMobile(mobile);
			}
	}); 
});

function queryAddressByMobile(mobile){
	if(null != mobile){
		$.ajax({
	        cache: true,
	        type: "POST",
	        url:domain + '/mem/white/addresslist',
	        data:{mobile:mobile},
	        async: false,
	        error: function(request) {
	            alert("Connection error");
	        },
	        success: function(result) {
	        	$("#consigneeAddressSelect").empty();
	        	if(result.success){
	        		var data = result.data;
	        		var optionstring = "";   
	        		$.each(data, function(i, value){
	        			if(i == 0)$("#consigneeAddress").val(value.value);
	        			optionstring += "<option value=\"" + value.id + "\" >" + value.value + "</option>";
	        		});
	        		$("#consigneeAddressSelect").html(optionstring);
	        	}else{
	        		$("#consigneeAddressSelect").html("<option value=\"0\" >------------------------查询------------------------</option>");
	        	}
	        }
	    });
	}
}

function saveWhiteInfo(form){
	$.ajax({
        cache: true,
        type: "POST",
        url:domain + '/mem/white/save',
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
