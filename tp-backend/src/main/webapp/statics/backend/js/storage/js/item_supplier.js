var pageii;
$(function() {

	$("#confirmSuppliersBtn").on('click',function(){
		
		if($("#table_list_supplier").find("input[name='supplierId']:checked").length < 1){
			alert("请选择供应商");
		}else{
			var _radio = $("#table_list_supplier").find("input[name='supplierId']:checked");
			var _this = _radio.val();
			var _thisValue = _radio.parent().parent().find('td.supplierName').text();
			
			window.parent.$("#spname").trigger('focus');
			window.parent.document.getElementById("spname").value=_thisValue; 
			window.parent.document.getElementById("spid").value=_this; 
			window.parent.$("#spname").trigger('blur');
			if(_thisValue!=null){
				$.ajax({
					async:false,
					url:"getcode.htm",
					type:"post",
					dataType:"text",
					data:{code:_thisValue},
					success:function(data){
						window.parent.document.getElementById("code").value=data;
					}
				});
			}
			var index = parent.layer.getFrameIndex(window.name); // 获取当前窗体索引
			parent.layer.close(index); 
		}
	});

});