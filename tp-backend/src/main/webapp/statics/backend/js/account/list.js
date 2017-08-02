/**
 * 拦截非数字字符
 */
function numner(){
	this.value = this.value.replace(/[^\d]/g, '');
};

var ADD_URL = "/account/add.htm";
var EDIT_URL = "/account/edit.htm";
var DELTE_URL = "/account/delete.htm";

$(document).ready(function() {
	/**
	 * 只允许输入数字
	 */
	$('input[format=number]').change(numner).keyup(numner);
	/**
	 * 账户新增
	 */
	$('input[opt_type=add]').bind('click',function(){
		showTab('account_list_add','账户新增',ADD_URL);
	});
	/**
	 * 账户修改
	 */
	$('a[opt_type=edit]').bind('click',function(a,b){
		var value = $(this).attr("opt_value");
		showTab('account_list_edit_'+value,'账户修改',EDIT_URL+'?appkey='+value);
	});
	/**
	 * 账户修改
	 */
	$('a[opt_type=delete]').bind('click',function(){
		opt_account_delete($(this).attr('opt_value'));
		return false;
	});
});

/**
 * 删除账户信息
 */
function opt_account_delete(value){
	$.ajax({
        cache: true,
        type: "POST",
        url:DELTE_URL,
        data:'appkey='+value,
        async: false,
        error: function(request) {
            alert("Connection error");
        },
        success: function(data) {    	
        	if(data.isSuccess == true){
            	var ly = layer.alert("删除账户成功", 1, function(){
            		layer.close(ly);
	        		location.reload();
            	});
        	}else{
        		layer.alert(data.resultInfo.message, 8);		
        	}
        }
    });
};

function showTab(id, text, url) {
	var tv = {
		linkId:id+"_link",
		tabId:id,
		url:url,
		text:text
	};
	try{
		window.parent.showTab(tv);
	} catch(e){}
}
