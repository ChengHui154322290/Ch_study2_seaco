// viewpoint 
$(function(){
	
	$("#viewpointTime").datetimepicker({
		dayOfWeekStart : 1,
		lang : 'zh',
		dateFormat:'yyyy-MM-dd HH:mm:ss'
	});
	//ajax query user  method: post
	$("#queryUserByAccountBtn").live("click",function(){
		var userAccount = $('#userAccount').val();
		$.ajax({ 
			url: 'getUserInfo.htm', 
			dataType: 'text',
			data: "userAccount="+userAccount,
			type: "post", 
			cache : false, 
			success: function(data) {
				var msg = eval("("+data+")");
				$("#userName").val(msg.userName);
				$("#userAccount").val(msg.userName);
				$("#userId").val(msg.userId);
			}
		});
	});
	
	//ajax query prdid method: post
	$("#queryPrdByBarcodeBtn").live("click",function(){
		var barcode = $('#barcode').val();
		$.ajax({ 
			url: 'getPrdInfo.htm', 
			dataType: 'text',
			data: "barcode="+barcode,
			type: "post", 
			cache : false, 
			success: function(data) {
				var msg = eval("("+data+")");
				$("#spu").val(msg.spu);
				$("#itemTitle").val(msg.itemTitle);
			}
		});
	});
	
	//save viewpoint form method: post
	/**
	 * 保存
	 */
	$('#dataFormSave').live('click',function(){
		if(validateForm()){
				$.ajax({ 
					url: 'save.htm', 
					data: $('#viewpointForm').serialize(), 
					type: "post", 
					cache : false, 
					success: function(data) {
						if(data.success){//成功
							layer.confirm('操作成功,是否返回列表页?点击确认按钮返回列表,否则,继续修改西客观点', function(){
								location.href='list.htm';
							 },function(){
							 	location.href='edit.htm?id='+obj.getMessage();
							});
					    }else{//失败
					    	 layer.alert(data.msg.message, 8);
					    }			
					}
				});
		}
	});
	
	
	
	
});

//validate viewpoint
function validateForm(){
	//validate sort
	var checkSku = true;
	$("input[name='sort']").each(function(){
		if($.trim($(this).val()).isEmpty()){
			alert("序号不能为空");
			checkSku = false;
			$(this).focus();
			return false;
		}
		if(!/^[-+]?\d*$/.test($.trim($(this).val()))){
			alert("序号只能为整数");
			checkSku = false;
			$(this).focus();
			return false;
		}
	});
	
	
	if(!checkSku){
		return false;
	}
	
	//validate barcode
	if($.trim($('#barcode').val()).isEmpty()){
		alert("条码不能为空");
		$('#barcode').focus();
		return false;
	}
	
	//validate spu
	if($.trim($('#spu').val()).isEmpty()){
		alert("SPU不能为空");
		return false;
	}
	
	
	var score = $.trim($('#score').val());
	
	if($.trim($('#score').val()).isEmpty()){
		alert("分值不能为空");
		$('#score').focus();
		return false;
	}
	if(score!=1&&score!=2&&score!=3&&score!=4&&score!=5){
		alert("分值只能为1-5的整数");
		$('#score').focus();
		return false;
	}
	
	//validate viewpointTime
	if($.trim($('#viewpointTime').val()).isEmpty()){
		alert("评论时间不能为空");
		$('#viewpointTime').focus();
		return false;
	}
	//输入10个字以上5000字以下）
	editor.sync(); 
	var editorVal = $.trim($("#editor").val());
	
	if(editorVal==""){
	    alert("观点内容不能为空");
		return false;
	}
	if(editorVal.length>50000||editorVal.length<10){
		 alert("观点内容长度为10-50000");
		 return false;
	}
	
	return true;
}