var type=xigou.getQueryString("type");
if(type == null)
{
	window.location.href="../logon.html";
}
$(function() {
	if(type == 1){
			$("#idSummit").on("click",function(){
				if(ckDom()){
					var name_input = $("#name_input").val(),
						type_input = $("#type_input").val(),
						id_input = $("#id_input").val();
					var params = {
						token:xigou.getToken(),
						type:1,
						promoterName:name_input,
						credentialType:1,
						credentialCode:id_input
					}
					xigou.promoterFunc.updatepromoter({
						requestBody : params,
						callback : success
					})
				}else{
					return;
				}
			})
	}else if(type == 2){
		$("#bankSummit").on("click",function(){
			if(ckDom()){
				if(checkField()){
					var bankname_input = $("#bankname_input").val(),
						bankcode_input = $("#bankcode_input").val();
					var params = {
						token:xigou.getToken(),
						type:1,
						bankName:bankname_input,
						bankAccount:bankcode_input
					}
					xigou.promoterFunc.updatepromoter({
						requestBody : params,
						callback : success
					})
				}
			}else{
				return;
			}

		})
	}else if(type == 3){
		$("#alipaySummit").on("click",function(){
			if(ckDom()) {
				var alipay_input = $("#alipay_input").val();
				var params = {
					token: xigou.getToken(),
					type: 1,
					Alipay: alipay_input
				}
				xigou.promoterFunc.updatepromoter({
					requestBody: params,
					callback: success
				})
			}else{
				return;
			}
		})
	}

});
//合法字段校验
function checkField(){
	var Flag = false;
	if($("#bankname_input").val()){
		var reg =/^[\u4e00-\u9fa5]+$/;
		if(!reg.test($("#bankname_input").val())){
			$.tips({
				content:"请输入正确的银行名称",
				stayTime:2000,
				type:"warn"
			})
			Flag =  false;
		}else{
			Flag = true;
		}
	}
	return Flag;
}
//空值校验
function ckDom(obj){
	var checkFlag = true;
	var iValue = $("input[type=text]");
		$.each(iValue,function(index,item){
			if(!iValue.eq(index).val()){
				checkFlag = false;
			}
		});
	if(checkFlag){
		$(".ui-btn-lg-empty").addClass("subOn");
	}else{
		$(".ui-btn-lg-empty").removeClass("subOn");
	}
	return checkFlag;
}

function success(response, status){
	if (status == xigou.dictionary.success) {
		if (response.code == 0) {
			$.tips({
				content:response.msg || "操作成功",
				stayTime:2000,
				type:"warn"
			})
		}
		else
		{
			$.tips({
				content:response.msg || "修改失败",
				stayTime:2000,
				type:"warn"
			})
		}
	}
}