$(function(){
	//加密
	$("#AESEncrypt").on('click', function(){
		var content = $("#content").val();
		var key = $("#key").val();
		if(content == ''){
			alert("加密内容为空");
			return;
		}
		if(key == ''){
			alert("加密KEY为空");
			return;
		}
		$.ajax({
			url:'aesEncrypt.htm',
			data:$('#AESUtilForm').serialize(),
			type:"post",
			cache:false,
			success:function(data){
				if(data.success){
					$("#result").val(data.data);
				}else{
					alert(data.msg.message);
				}
			}
		});	
	});
	
	//解密
	$("#AESDecrypt").on('click', function(){
		var content = $("#content").val();
		var key = $("#key").val();
		if(content == ''){
			alert("解密内容为空");
			return;
		}
		if(key == ''){
			alert("解密KEY为空");
			return;
		}
		$.ajax({
			url:'aesDecrypt.htm',
			data:$('#AESUtilForm').serialize(),
			type:"post",
			cache:false,
			success:function(data){
				if(data.success){
					$("#result").val(data.data);
				}else{
					alert(data.msg.message);
				}
			}
		});	
	});
	
	//加密
	$("#RSASign").on('click', function(){
		var content = $("#content").val();
		var key = $("#key").val();
		if(content == ''){
			alert("加密内容为空");
			return;
		}
		if(key == ''){
			alert("加密KEY为空");
			return;
		}
		$.ajax({
			url:'rsaSign.htm',
			data:$('#RSAUtilForm').serialize(),
			type:"post",
			cache:false,
			success:function(data){
				if(data.success){
					$("#result").val(data.data);
				}else{
					alert(data.msg.message);
				}
			}
		});	
	});
	
	//解密
	$("#RSAVerify").on('click', function(){
		var content = $("#content").val();
		var key = $("#key").val();
		var sign = $("#sign").val();
		if(content == ''){
			alert("解密内容为空");
			return;
		}
		if(key == ''){
			alert("解密KEY为空");
			return;
		}
		if(sign == ''){
			alert("签名为空");
			return;
		}
		$.ajax({
			url:'rsaVerify.htm',
			data:$('#RSAUtilForm').serialize(),
			type:"post",
			cache:false,
			success:function(data){
				if(data.success){
					$("#result").val(data.data);
				}else{
					alert(data.msg.message);
				}
			}
		});	
	});
	
});