// JavaScript Document
$(function() {
//	var ua = window.navigator.userAgent.toLowerCase();
//	if ("micromessenger" == ua.match(/MicroMessenger/i)) {
//		// 微信里面 获取openid
//		var openId = xigou.getLocalStorage("openid");
//		if (!openId) {
//			var _code = xigou.getQueryString("code");
//			if (_code) {
//				xigou.setSessionStorage("weixin_code", _code);
//				getopenid();
//			}
//			else {
//				var n = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6f7f5f0bab32e7d3&redirect_uri=' + location.href.replace(/&+/g, "%26") + '&response_type=code&scope=snsapi_base&state=1#wechat_redirect';
//				window.location.href = n;
//			}
//			return;
//		}
//	}
	InitPage();
	registr();
	//initgeetest();

	//initValiData();
	$(".gender").change(function(){
		if($(this).hasClass("gender-m")) {
			$("#genderm").removeClass().addClass("gender-label-m1");
			$("#genderf").removeClass().addClass("gender-label-f2");
			$('.label_m').addClass('select');
			$('.label_f').removeClass('select');
		}
		else{
			$("#genderm").removeClass().addClass("gender-label-m2");
			$("#genderf").removeClass().addClass("gender-label-f1");
			$('.label_m').removeClass('select');
			$('.label_f').addClass('select');
		}
	});
});


var handlerEmbed = function (captchaObj) {
	$('.div_img_code .div_code_btn2,#achieve')[xigou.events.click](function () {
		var validate = captchaObj.getValidate();
		if (!validate) {
			$.tips({
				content: "请先完成验证",
				stayTime: 2000,
				type: "warn"
			});
		} else {
			var params = {
				'geetest_challenge': $("[name=geetest_challenge]").val(),
				'geetest_validate': $("[name=geetest_validate]").val(),
				'geetest_seccode': $("[name=geetest_seccode]").val(),
				'tel': $("#input-telnum").val(),
				'randid':$("#randid").val(),
				'type' : "5"
			};
			var dp = {
				requestBody: params,
				callback: function (response, status) { //回调函数
					if (status == xigou.dictionary.success) {
						xigou.removeSessionStorage("dssUser");
						if (null == response) {
							$.tips({
								content: xigou.dictionary.chineseTips.server.value_is_null,
								stayTime: 2000,
								type: "warn"
							})
						} else {
							if (response.code == "0") {
								$.tips({
									content: response.msg,
									stayTime: 2000,
									type: "warn"
								});
								destroy();setCodeTime(60,$('#div_get_code'));
							} else {
								$.tips({
									content: response.msg || "获取验证码失败",
									stayTime: 2000,
									type: "warn"
								});
							}

						}
					} else {
						xigou.alert('请求失败，详见' + response);
					}
				}
			};
			xigou.activeUser.sendGeeCode(dp);

		}
	});
	// 将验证码加到id为captcha的元素里
	captchaObj.appendTo("#embed-captcha");
	captchaObj.onReady(function () {
	});
	// 更多接口参考：http://www.geetest.com/install/sections/idx-client-sdk.html
};


//注册
function registr() {
	$('.div_agree input').change(function() {
		var checked = this.checked;
		if (checked) {
			$('.div-register').removeClass('disable');
		}
		else {
			$('.div-register').addClass('disable');
		}
	});
		
	$('.div-register')[xigou.events.click](function() {
		if ($(this).hasClass('disable')) {
			return;
		}
		//昵称校验
		if(!nickNameCheck()){
			return;
		}

		if (!confirmPws() || !xigou.valiformdata.check(null, valiDataCallBack) ) {
			return;
		}
		
		var params = {
			'mobile': $("#input-telnum").val(),
			'captcha': $("#input-code").val(),
			'gender': $(".gender:checked").val(),
			'nickName': $("#input-nickname").val(),
			'passWord': $("#input-pwd").val(),
			'inviter' : xigou.getQueryString("i"),
			'userAgreed':$('.div_agree input')[0].checked
		};
		var dp = {
			requestBody: params,
			callback: function(response, status) { //回调函数
				if (status == xigou.dictionary.success) {
					if (null == response) {
						$.tips({
							content:xigou.dictionary.chineseTips.server.value_is_null,
							 stayTime:2000,
							type:"warn"
						})
						// xigou.alert(xigou.dictionary.chineseTips.server.value_is_null);
					} else {
						var stauts = response.code;
						xigou.debugPrint("注册成功返回的code值：" + stauts);
						switch (stauts) {
							case "0":
								// 注册成功 跳转到dsshome页面
								if (response.data.promoterinfo && response.data.promoterinfo != "{}") {
									var promoterinfo = 	JSON.parse(response.data.promoterinfo);
									promoterinfo.token = response.data.token;
									xigou.setSessionStorage("dssUser", promoterinfo, true);
								}

								var back = {
									'token': response.data.token,
									'telephone': response.data.tel,
									'name': response.data.name
								};
								//实名认证信息清除
								xigou.removeSessionStorage('realname');
								xigou.removeSessionStorage('realnum');
								xigou.setSessionStorage("userinfo", back, true);
								xigou.setLocalStorage("login_name", response.data.tel);
								xigou.setLocalStorage("show_name", response.data.tel || response.data.name);
								xigou.setLocalStorage("token", response.data.token);

								xigou.setSessionStorage("dssregister", "注册成功");
								window.location.href = "dss/dsshome.html";
								break;
							case "-2":
								$.tips({
									content:response.msg||"验证码错误",
									stayTime:2000,
									type:"warn"
								});
								break;
							default:
								$.tips({
									content:response.msg||"注册失败",
									stayTime:2000,
									type:"warn"
								});
								break;
						}
					}
				} else {
					xigou.alert('请求失败，详见' + response);
				}
			}
		};
		xigou.activeUser.dssregistr(dp);
	});

	$('#div_get_code')[xigou.events.click](function() {
	//	showCodeImg('input-telnum', 5, $('#div_get_code'));
		showpopgeetest();
	})
};

function setCodeTime(time){
	var getTheCode=$('#div_get_code');
	getTheCode.text(time);
	var _flag=setInterval(function(){
		if(time<=0){
			clearInterval(_flag);
			getTheCode.text("获取验证码");
			getTheCode.removeClass('disble');
			allowGetTheCode=true;
			return;
		}
		getTheCode.text(--time);
	},1000);

}

function valiDataCallBack(message, id, mname) {
	$.tips({
		content:message,
		stayTime:2000,
		type:"warn"
	})
	$("#"+id).focus();
};
function nickNameCheck(){
	var reg = /^[\u4E00-\u9FA5A-Za-z0-9_]+$/;
	var val = $("#input-nickname").val();
	if(reg.test(val)){
		return true;
	}else{
		$.tips({
			content:"请输入正确格式的昵称",
			stayTime:2000,
			type:"warn"
		});
		return false;
	}
}
function confirmPws() {
	if($("#input-pwd").val() == $("#input-pwd-cfm").val()){
		return true;
	}
	$.tips({
		content:"两次密码输入不一致",
		stayTime:2000,
		type:"warn"
	})
	return false;
}
function initValiData() {
	var _form = {
		methods: {
			//"input-name": {
			//	required: "required",
			//},
			"input-pwd": {
				required: "required",
				password:true
			},
			"input-pwd-cfm": {
				required: "required",
				password:true
			},
			"input-telnum": {
				required: "required",
				mobile:true
			},
			"input-credential-code": {
				required: "required",
			},
		},
		errors: {
			//"input-name": {
			//	required: "请输入用户真实姓名",
			//},
			"input-pwd": {
				required: "请输入密码",
				password:"密码只能为字母，数字(6-30位)"
			},
			"input-pwd-cfm": {
				required: "请输入确认密码",
				password:"密码只能为字母，数字(6-30位)"
			},
			"input-telnum": {
				required: "请输入手机号",
				mobile:"手机号码不正确，请重新输入"
			},
			"input-credential-code": {
				required: "请输入身份证号码",
			},
		}
	};
	return xigou.valiformdata.initValiData(_form);
};

function InitPage() {
	var info = xigou.getSessionStorage("registerinfo", true);
	if (info) {
		// 电话
		if (info.tel) {
			$("#input-telnum").val(info.tel);
		}
		// 验证码
		if (info.code) {
			$("#input-code").val(info.code);
		}
		// 密码
		if (info.pwd) {
			$("#input-pwd").val(info.pwd);
		}

		// 确认密码
		if (info.pwdcfm) {
			$("#input-pwd-cfm").val(info.pwdcfm);
		}

		// 昵称
		if (info.nickname) {
			$("#input-nickname").val(info.nickname);
		}

		// 性别 女
		if (info.inviter) {
			$("#genderm").removeClass().addClass("gender-label-m2");
			$("#genderf").removeClass().addClass("gender-label-f1");
			$('#gender-f')[0].checked = true;
			$('.label_m').removeClass('select');
			$('.label_f').addClass('select');
		}

		// 同意协议
		if (info.userAgreed) {
			$('.div_agree input')[0].checked = true;
			$('.div-register').removeClass('disable');
		}
	}
	xigou.removeSessionStorage("registerinfo");

	$('.read')[xigou.events.click](function(){
		var info = {};
		info.tel = $("#input-telnum").val();
		info.code = $("#input-code").val();
		info.pwd = $("#input-pwd").val();
		info.pwdcfm = $("#input-pwd-cfm").val();
		info.nickname = $("#input-nickname").val();
		if ($("#genderm").hasClass("gender-label-m2")) {
			info.inviter = true;
		}
		else {
			info.inviter = false;
		}
		info.userAgreed = $('.div_agree input')[0].checked;
		xigou.setSessionStorage("registerinfo", info, true);
		window.location.href = 'dss/useragreement.html';
	})

}

//function getopenid() {
//	var e = xigou.getSessionStorage("weixin_code");
//	xigou.activeUser.openid({
//		requestBody: {
//			code: e
//		},
//		callback: function(e) {
//			xigou.setLocalStorage("openid", e.data.openid);
//			InitPage();
//			registr();
//			//initValiData();
//			$(".gender").change(function(){
//				if($(this).hasClass("gender-m")) {
//					$("#genderm").removeClass().addClass("gender-label-m1");
//					$("#genderf").removeClass().addClass("gender-label-f2");
//					$('.label_m').addClass('select');
//					$('.label_f').removeClass('select');
//				}
//				else{
//					$("#genderm").removeClass().addClass("gender-label-m2");
//					$("#genderf").removeClass().addClass("gender-label-f1");
//					$('.label_m').removeClass('select');
//					$('.label_f').addClass('select');
//				}
//			});
//		}
//	})
//}