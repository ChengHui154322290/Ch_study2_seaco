var pageii;
$(function(){	
	
	$(".itemReviewQuery").on('click',function(){
		itemReviewForm.submit();     
	});
	
	$('.itemReviewAdd').live('click',function(){
			pageii=$.layer({
	            type : 2,
	            title: '新增评论',
	            shadeClose: true,
	            maxmin: true,
	            fix : false,  
	            area: ['500px', 505],                   
	            iframe: {
	                src : domain+"/comment/review/add.htm"
	            } 
	        });
		 
	});
	
	// 进入批量导入评论界面
	$('.itemReviewImport').live('click',function(){
		pageii=$.layer({
            type : 2,
            title: '导入评论',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['1000px', 600],                   
            iframe: {
                src : domain+"/comment/review/import.htm"
            } 
        });
	 
	});
	
	$('.detailReview').live('click', function() {
		var id = $(this).attr('param');

		pageii = $.layer({
			type : 2,
			title : '查看评论明细',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 500 ],
			iframe : {
				src : domain+'/comment/review/detail.htm?id='+id
			}
		});
	});
	
	// 时间控件初始化
	$( "#createBeginTime" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	        $( "#createEndTime" ).datepicker( "option", "minDate", selectedDate );
	    }
	});
	$( "#createEndTime" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	        $( "#createBeginTime" ).datepicker( "option", "maxDate", selectedDate );
	    }
	});
	
	//取消按钮
	$('.closebtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
	$("#queryUserByAccountBtn").live("click",function(){
		var memLoginName = $('#memLoginName').val();
		$.ajax({ 
			url: domain + '/comment/review/getMemberInfo.htm', 
			data: "loginName="+memLoginName,
			type: "post", 
			cache : true, 
			success: function(data) {
				if(data.success){
					//var msg = eval("("+data.data+")");
					$("#userName").val(data.data.memNickName);
					$("#loginName").val(data.data.memLoginName);
					$("#uid").val(data.data.uid);
				}else{
					layer.alert(data.msg.message, 8);
				}
			}
		});
	});
});

//保存增加 
function saveAdd(button){
	if(validateForm()){	
		$.ajax({
	        cache: true,
	        type: "POST",
	        url:domain+"/comment/review/addreview.htm",
	        data:$('#reviewAdd').serialize(),
	        async: false,
	        error: function(request) {
	            alert("Connection error");
	        },
	        success: function(data) {
	         	if(data.success){
	                layer.alert("保存成功", 1, function(){
	            	parent.window.location.reload();
	            	parent.layer.close(parent.pageii);
	                })
	            	}else{
	            		layer.alert(data.msg.message, 8);		
	            	}
	        }
	    });
	}
}

//保存增加 
function upd(button){
	$.ajax({
        cache: true,
        type: "POST",
        url:domain+"/comment/review/update.htm",
        data:$('#reviewEdit').serialize(),
        async: false,
        error: function(request) {
            alert("Connection error");
        },
        success: function(data) {
         	if(data.success){
                layer.alert("保存成功", 1, function(){
            	parent.window.location.reload();
            	parent.layer.close(parent.pageii);
                })
            	}else{
            		layer.alert(data.msg.message, 8);		
            	}
        }
    });	
}

//-------------------------------------重置按钮--如下----------------------------------------

function dataReset(button){
	clearForm('itemReviewForm');
}

function clearForm(id) {
	var formObj = document.getElementById(id);
	if (formObj == undefined) {
		return;
	}
	for (var i = 0; i < formObj.elements.length; i++) {
		if (formObj.elements[i].type == "text") {
			formObj.elements[i].value = "";
		} else if (formObj.elements[i].type == "password") {
			formObj.elements[i].value = "";
		} else if (formObj.elements[i].type == "radio") {
			formObj.elements[i].checked = false;
		} else if (formObj.elements[i].type == "checkbox") {
			formObj.elements[i].checked = false;
		} else if (formObj.elements[i].type == "select-one") {
			formObj.elements[i].options[0].selected = true;
		} else if (formObj.elements[i].type == "select-multiple") {
			for (var j = 0; j < formObj.elements[i].options.length; j++) {
				formObj.elements[i].options[j].selected = false;
			}
		} else if (formObj.elements[i].type == "file") {
			var file = formObj.elements[i];
			if (file.outerHTML) {
				file.outerHTML = file.outerHTML;
			} else {
				file.value = ""; // FF(包括3.5)
			}
		} else if (formObj.elements[i].type == "textarea") {
			formObj.elements[i].value = "";
		}
	}
}

function validateForm(){
	//validate userName
	if($.trim($('#userName').val()) == ""){
		alert("用户名不能为空");
		$('#userName').focus();
		return false;
	}
	return true;
}
