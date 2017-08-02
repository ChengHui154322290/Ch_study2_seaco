var pageii;
$(function(){
	
	// 查看日志
	$('.journalReview').live('click',function(){
		var type="ExpressInfo";
		var id= $(this).attr('param');	
		pageii=$.layer({
			type : 2,
			title: '日志查询',
			shadeClose: true,
			maxmin: true,
			fix : false,  
			area: ['950px', 480],                     
			iframe: {
				src : domain+'/basedata/logs/view.htm?id='+id+"&type="+type
			} 
		});
	});
	
	// 增加
	$("#expressAddBtn").on('click',function(){
		pageii=$.layer({
			type : 2,
			title : '快递公司管理-->新增',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area: ['600px', 400],
			iframe : {
				src : domain + '/basedata/express/add.htm'
			}
		});
	});
	
	// 编辑
	$(".editexpressInfo").on('click',function(){
		var id=$(this).attr('param');
		pageii=$.layer({
			type : 2,
			title : '快递公司管理-->编辑',
			shadeClose : true,
			fix : false,
			area:['600px',400],
			iframe : {
				src : domain + '/basedata/express/edit.htm?id='+id
			}
		});
	});
	
	// 取消按钮 关闭当前弹窗
	$("#buttoncancel").on('click',function(){
		window.parent.layer.close(parent.pageii);
	});
	
	/**
	 * 增加编辑页面的数据校验
	 */
	if ($("#datasubmit").is("input")) {
		var param=$("#datasubmit").attr('param');
		var butt = $("#datasubmit");
		$.formValidator.initConfig({
			submitButtonID : "datasubmit",
			debug : false,
			onSuccess : function() {
				if(param=="saveexpress"){
					expressSaveSubmit(butt);
				}else{
					expressEditSubmit(butt);
				}
			},
			onError : function() {
				alert("数据输入有误，请看网页上的提示");
			}
		});
		validatorTagsInput();
	}
	
});


/**
 * 保存
 */
function expressSaveSubmit(button) {
		$.ajax({
			url : 'save.htm',
			data : $('#expressSaveForm').serialize(),
			type : "post",
			cache : false,
			success : function(data) {				
				if (data.success) {// 成功
					layer.alert('操作成功', 4, function() {
//						location.href = 'list.htm';
						parent.window.location.reload();
		            	parent.layer.close(parent.pageii);
					});
				} else {// 失败
					layer.alert(data.msg.message, 8);
				}
			}
		});
}


/**
 *编辑
 */
function expressEditSubmit(button) {
	$.ajax({
		url : 'update.htm',
		data : $('#expressUpdateForm').serialize(),
		type : "post",
		cache : false,
		success : function(data) {
			if (data.success) {// 成功
				layer.alert('操作成功', 4, function() {
//					location.href = 'list.htm';
					parent.window.location.reload();
	            	parent.layer.close(parent.pageii);
				});
			} else {// 失败
				layer.alert(data.msg.message, 8);
			}
		}
	});
}

/**
 * 数据校验方法
 */
function validatorTagsInput() {
	
	$("#name").formValidator({
		onShow : "请输入名称",
		onFocus : "名称在1~18个字符,字母数字最多36个",
		onCorrect : "正确"
	}).inputValidator({
		min : 1,
		max : 16,
		onError : "标签名称输入有误"
	});
	$("#code").formValidator({
		onShow:"请输入快递公司编号",
		onFocus:"不能为空,最多100个字符",
		onCorrect:"正确"
	}).inputValidator({
		min:1,
		max:100,
		onError:"你输入的备注不合法,请确认"
	});	
	$("#sortno").formValidator({
		onShow:"请输入排序值",
		onFocus:"不能为空，且为正整数",
		onCorrect:"正确"
	}).regexValidator({ 
		regExp: "intege1", 
		dataType: "enum", 
		onError: "排序值格式不正确"
	});
	
}

/**
 * 数据重置
 */
function dataReset(button){
	clearForm('expressFrom');
}

/**
 * 重置
 */
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

function getData(){
	var id =$.trim($("#id").val());
	var name =$.trim($("#name").val());
	var code = $.trim($("#code").val());
	var sortNo = $.trim($("#sortno").val());
	var datainfo={id:id,name:name,code:code,sortNo:sortNo};
	var dataJson=$.extend({},datainfo);
	return dataJson;
}

