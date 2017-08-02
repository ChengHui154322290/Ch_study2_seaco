var pageii;
$(function(){
	$(document.body).on('click','.updatesmsWhiteStatus', function() {
		var current_btn = $(this);
		var curStatus = $(this).attr('status');
		var changeStatus = curStatus == 0?1:0;
		$.ajax({
			url:domain + '/mem/smswhite/updatesmswhiteStatus.htm?status=' + changeStatus ,
			type:'post',
			cache: false,
			success:function(result){
				if(result.success){
					current_btn.attr('status',curStatus==0?1:0);
					current_btn.attr('value',curStatus==0?'关闭':'开启');
				}
			}
		});
	});
	//新增
	$(document.body).on('click','.addSmsWhite', function() {
		var mobile = $("#mobile").val();
		if(mobile == ""){
			alert("手机号不能为空");
			return;
		}	
		if(validMobile(mobile) == false){
			alert("请填写正确的手机号");
			return;
		}
		if(confirm("是否要添加手机号" + mobile + "到短信白名单？")){
			$.ajax({
				url:domain + '/mem/smswhite/addSmsWhite.htm?mobile=' + mobile ,
				type:'post',
				cache: false,
				success:function(result){
					if(result.success){
						alert("添加成功");
						window.location.reload();
					}else{
						alert(result.msg.message, 8);
					}
				}
			});	
		}		
	});
	
	//删除
	$(document.body).on('click','.deleteSmsWhite', function() {
		var mobile = $(this).attr('param');
		if(mobile == ""){
			alert("手机号不能为空");
			return;
		}	
		if(validMobile(mobile) == false){
			alert("请填写正确的手机号");
			return;
		}
		if(confirm("是否要从白名单中删除手机号" + mobile + "?")){
			$.ajax({
				url:domain + '/mem/smswhite/delSmsWhite.htm?mobile=' + mobile ,
				type:'post',
				cache: false,
				success:function(result){
					if(result.success){
						alert("删除成功");
						window.location.reload();
					}else{
						alert(result.msg.message, 8);
					}
				}
			});	
		}		
	});
	
	//限制
	$(document.body).on('click','.limitCondition', function() {
		pageii = $.layer({
			type : 2,
			title : '短信限制条件',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '500px', 400 ],
			iframe : {
				src : domain + '/mem/smswhite/smslimit.htm'
			}
		});
	});
	
	$(document.body).on('click','.saveLimitData', function() {
		$.ajax({
	        cache: false,
	        type: "POST",
	        url:domain + '/mem/smswhite/updateLimitData.htm',
	        data:$('#smsLimitForm').serialize(),
	        async: false,
	        error: function(request) {
	            alert("Connection error");
	        },
	        success: function(result) {
	        	if(result.success){
	        		alert("保存成功", 8);
	        		parent.layer.close(parent.pageii);
	        	}else{
	        		alert(result.msg.message, 8);
	        	}
	        }
	    });	
	});
	
	//统计
//	$(document.body).on('click','.statisticsData', function() {
//		pageii = $.layer({
//			type : 2,
//			title : '短信统计',
//			shadeClose : true,
//			maxmin : true,
//			fix : false,
//			area : [ '800px', 700 ],
//			iframe : {
//				src : domain + '/mem/smswhite/smsStatistics.htm'
//			}
//		});
//	});
	
	$(document.body).on('click','.closeBtn', function() {
		parent.layer.close(parent.pageii);
	});
	
});


function validMobile(value){
    var length = value.length;
    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
    return length == 11 && mobile.test(value);
}


function validateLimit(){
	
}
