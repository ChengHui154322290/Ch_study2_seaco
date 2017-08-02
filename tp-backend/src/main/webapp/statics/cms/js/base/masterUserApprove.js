var MASTER_USER_SUBMIT = domain + "/cms/masterUser/submitApproveInfo";
var index = parent.layer.getFrameIndex(window.name);

$(document).ready(function(){
	$("#approve").on("click",function(){
		$("#status").val(2);
		submitApprInfo();
	});
	$("#refuse").on("click",function(){
		$("#status").val(3);
		submitApprInfo();
	});
	$("#cancel").on("click",function(){
		parent.layer.close(index);
	});
})

function submitApprInfo(){
	var ajax_url = MASTER_USER_SUBMIT; //表单目标 
    var ajax_type = "POST"; //提交方法 
    var ajax_data = $("#masterUserApprove").serialize(); //表单数据 
    $.ajax({ 
	     type:ajax_type, //表单提交类型 
	     url:ajax_url, //表单提交目标 
	     data:ajax_data, //表单数据
	     dataType:'html',
	     success:function(msg){
	    	 var data = eval("("+msg+")");
	    	 if(data.isSuccess){
	    		 parent.layer.close(index);
	    	 } else {
	    		 layer.alert(data.data);
	    	 }
	     },
		error : function(data) {
			layer.alert("保存失败!");
		}
    });
}