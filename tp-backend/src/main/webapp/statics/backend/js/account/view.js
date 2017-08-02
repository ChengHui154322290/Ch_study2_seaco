
var reg_supplier = /^(\d+,)*(\d+\s*)$/;
/**
 * 拦截非数字字符
 */
function numner(){
	this.value = this.value.replace(/[^\d,]/g, '');
};
$(document).ready(function() {
	/**
	 * 只允许输入数字 ,
	 */
	$('textarea[name=supplier]').change(numner).keyup(numner);
	$('button[name=btn_supplier]').bind('click',opt_layer_supList);
});

/**
 * 表单验证
 */
function verify_form(){
	var name = $('input[name=name]').val();
	/**
	 * 账户名称不能为空
	 */
	if("" == name){
		layer.alert("账户名称不能为空", 8);		
		return false;
	}
	var supplier = $('textarea[name=supplier]').val();
	/**
	 * 供应商 id 集合格式不对
	 */
	if(reg_supplier.test(supplier) == false){
		layer.alert("供应商格式不正确", 8);		
		return false;
	}
	return true;
}


/**
 * 供应商列表弹出框
 */
function opt_layer_supList(){
	var supplier = $('textarea[name=supplier]').val();
	/**
	 * 供应商 id 集合格式不对
	 */
	if(reg_supplier.test(supplier) == false){
		layer.alert("格式不正确", 8);		
		return false;
	}
	
	$.layer({
        type : 2,
        title: '供应商列表',
        shadeClose: true,
        maxmin: true,
        fix : false,  
        area: ['800px', 300],                   
        iframe: {
            src : domain+"/account/supList.htm?supplier="+supplier
        } 
    });
    return false;
}