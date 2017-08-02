// JavaScript Document

$(function() {
	registr();
	$('#div_get_code')[xigou.events.click](function() {
		var telNum=$("#input-telnum").val();
		if(telNum==""){
			 $.tips({
	             content: "请先输入手机号码！",
	             stayTime: 2000,
	             type: "warn"
	         })
	         return false;
		}
		showpopgeetest();
	});
});

var allowGetTheCode = true;
var invisteCode = xigou.getQueryString("inviteCode");


function setCodeTime(time) {
	var getTheCode = $('.register-btn');
	getTheCode.text(time);
	var _flag = setInterval(function() {
		if (time <= 0) {
			clearInterval(_flag);
			getTheCode.text("获取验证码");
			allowGetTheCode = true;
			return;
		}
		getTheCode.text(--time);
	}, 1000);

}

var handlerEmbed = function(captchaObj) {
	$('.div_img_code .div_code_btn2,#achieve')[xigou.events.click]
			(function() {
				var validate = captchaObj.getValidate();
				if (!validate) {
					$.tips({
						content : "请先完成验证",
						stayTime : 2000,
						type : "warn"
					});
				} else {
					var params = {
						'geetest_challenge' : $("[name=geetest_challenge]")
								.val(),
						'geetest_validate' : $("[name=geetest_validate]").val(),
						'geetest_seccode' : $("[name=geetest_seccode]").val(),
						'tel' : $("#input-telnum").val(),
						'randid' : $("#randid").val(),
						'type' : "5"
					};
					var dp = {
						requestBody : params,
						callback : function(response, status) {

	                        if (null == response) {
	                            $.tips({
	                                content: xigou.dictionary.chineseTips.server.value_is_null,
	                                stayTime: 2000,
	                                type: "warn"
	                            })
	                        } else {
	                            if (response.code == "0") {
	                                $("#hide-div").show();
	                                $(".img_code_bg").hide();
	                                $(".div_img_code").hide();
	                                
	                                $.tips({
	                                    content: response.msg,
	                                    stayTime: 2000,
	                                    type: "warn"
	                                })
	                            } else {
	                                $.tips({
	                                    content: response.msg || "获取验证码失败",
	                                    stayTime: 2000,
	                                    type: "warn"
	                                });
	                            }

	                        }
	                    
						}
					};
					xigou.activeUser.sendGeeCode(dp);

				}
			});
	// 将验证码加到id为captcha的元素里
	captchaObj.appendTo("#embed-captcha");
	captchaObj.onReady(function() {
	});
	// 更多接口参考：http://www.geetest.com/install/sections/idx-client-sdk.html
};

// 注册
function registr() {

	// 获取页面高度，设置body高度，解决输入法弹出把logo推上来问题
	var bh = $("body").height(), wh = $(window).height();

	$('.div_agree input').change(function() {
		var checked = this.checked;
		if (checked) {
			$('.div-register').removeClass('disable');
		} else {
			$('.div-register').addClass('disable');
		}
	});

	$('.div-register')[xigou.events.click]
			(function() {
				if ($(this).hasClass('disable')) {
					return;
				}
				if (!promoterNameCheck()) {
					return;
				}
				if (!userMobileCheck()) {
					return;
				}
//                if(!alipayCheck()){
//                	return;
//                }
//				if (!credentialCodeCheck()) {
//					return;
//				}
//
//				if (!bankNameCheck()) {
//					return;
//				}
//
//				if (!bankAccountCheck()) {
//					return;
//				}
//
//				if (!alipayCheck()) {
//					return;
//				}


				var params = {
					'inviteCode' : invisteCode,
					'password' : $("#input-pwd").val(),
					'promoterName' : $("#input-name").val(),
					'userMobile' : $("#input-telnum").val(),
					'credentialCode' : $("#credentialCode").val(),
					'bankName' : $("#input-bankName").val(),
					'bankAccount' : $("#input-bankAccount").val(),
					'alipay' : $("#input-alipay").val(),
					'realSmsCode':$("#input-code").val()
				};

				var dp = {
					requestBody : params,
					callback : function(response, status) { // 回调函数
						if (status == xigou.dictionary.success) {
							if (null == response) {
								$.tips({
											content : xigou.dictionary.chineseTips.server.value_is_null,
											stayTime : 2000,
											type : "warn"
										})
								// xigou.alert(xigou.dictionary.chineseTips.server.value_is_null);
							} else {
							   var stauts = response.code;
							   if(stauts=="0"){
								   $.tips({
										content : response.msg
												|| "注册失败",
										stayTime : 2000,
										type : "warn"
									});
									window.location.href = "./dssInvisterRegisterSuccess.html"; 
							   }else{
								   $.tips({
										content : response.msg,
										stayTime : 2000,
										type : "warn"
									});
							   }
							   
								
								
								
							}
						} else {
							xigou.alert('请求失败，详见' + response);
						}
					}
				};
				xigou.registerInvister(dp);
			

			});
}

function valiDataCallBack(message, id, mname) {
	$.tips({
		content : message,
		stayTime : 2000,
		type : "warn"
	})
	$("#" + id).focus();
};

function setCodeTime(time) {
	var getTheCode = $('#div_get_code');
	getTheCode.text(time);
	var _flag = setInterval(function() {
		if (time <= 0) {
			clearInterval(_flag);
			getTheCode.text("获取验证码");
			getTheCode.removeClass('disble');
			allowGetTheCode = true;
			return;
		}
		getTheCode.text(--time);
	}, 1000);

};

function userMobileCheck() {
	var reg = /^1\d{10}$/;
	var val = $("#input-telnum").val();
	if (reg.test(val)) {
		return true;
	} else {
		$.tips({
			content : "请输入正确格式的手机号",
			stayTime : 2000,
			type : "warn"
		});
		return false;
	}
}

function promoterNameCheck() {
	var reg = /^[\u4E00-\u9FA5A-Za-z0-9_]+$/;
	var val = $("#input-name").val();
	if (reg.test(val)) {
		return true;
	} else {
		$.tips({
			content : "请输入正确格式的姓名",
			stayTime : 2000,
			type : "warn"
		});
		return false;
	}
}

function credentialCodeCheck() {
	var reg = /^\d{17}(\d|x)$/i;
	var val = $("#input-credential-code").val();
	// var checkResult = isCardID(val);
	if (reg.test(val)) {
		return true;
	} else {
		$.tips({
			content : "你输入的身份证长度或格式错误",
			stayTime : 2000,
			type : "warn"
		});
		return false;
	}

}

function bankNameCheck() {
	var reg = /^[\u4E00-\u9FA5A-Za-z0-9_]+$/;
	var val = $("#input-bankName").val();
	if (reg.test(val)) {
		return true;
	} else {
		$.tips({
			content : "请输入正确格式的银行名称",
			stayTime : 2000,
			type : "warn"
		});
		return false;
	}
}

function bankAccountCheck() {
	var reg = /^\d{16}|\d{19}$/;
	var val = $("#input-bankAccount").val();
	if (reg.test(val)) {
		return true;
	} else {
		$.tips({
			content : "请输入正确格式的银行卡号",
			stayTime : 2000,
			type : "warn"
		});
		return false;
	}
}

function alipayCheck() {
	var val = $("#input-alipay").val();
	if (val!="") {
		return true;
	} else {
		$.tips({
			content : "请输入支付宝账号",
			stayTime : 2000,
			type : "warn"
		});
		return false;
	}
}
/**
 * 验证码非空校验
 * @returns
 */
function alipayCheck() {
	var val = $("#input-code").val();
	if (val!="") {
		return true;
	} else {
		$.tips({
			content : "请输入手机验证码",
			stayTime : 2000,
			type : "warn"
		});
		return false;
	}
}
