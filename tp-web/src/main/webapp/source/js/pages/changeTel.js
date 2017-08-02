$(function(){
	if(!isWeiXin()){
		initgeetest();
	}
	
	getTheCode();
//	$(".btn_pre")[xigou.events.click](function(){
//		$(".main_next").show().siblings().hide();
//	});
//	$(".btn_next")[xigou.events.click](function(){
//		$(".main_success").show().siblings().hide();
//	})
	//获取原手机号
	xigou.modifymobile.init({
		requestBody: {'token': xigou.getToken()},
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				$(".oldMobile").text(userinfo.data.tel);
//				getUserInfo();
			}else{
				xigou.setSessionStorage("loginjump_url", "changeTel.html");
				window.location.href = "logon.html";
			}
		}
	});
	
	
});

$("#input-telnum").bind("focus",function(e){
	$('.get_code1').removeClass("status");
	$(".clear").show();
	$(".clear")[xigou.events.click](function(e){
		$("#password").val("");
		$("#input-telnum").val("");
	})
})

//$("#validateCode").bind("focus",function(e){
//	$('.get_code1').removeClass("status");
//	$(".clear").show();
//	$(".clear")[xigou.events.click](function(e){
//		$("#password").val("");
//		$("#validateCode").val("");
//	})
//})

$("#password, #validateCode").bind("focus",function(e){
	$(".clear").show();
	/*$(".view").show();*/
	$(".btn_pre, .btn_next").css({
		"background":"#333"
	})
})

/*$("#password, #input-telnum").bind("blur",function(e){	
	$(".view")[xigou.events.click](function(e){
		if($(".view").hasClass("display")){
			$(".view>img").attr("src","img/show.png");
			$(".view").removeClass("display");
			$("#password").attr("type","text");
		}else{
			$(".view>img").attr("src","img/display.png");
			$(".view").addClass("display");
			$("#password").attr("type","password");
		}
	})	
	$(".clear").hide();
})*/



//$(".btn_pre").click(function(){
//	var loginName = $(".oldMobile").text();
//	var password = $("#password").val();
//	var params = {
//			'token': '',
//			// 'uuid':uuid,
//			'loginname': loginName,
//			'pwd': password
//		};
//	if(password==null || password=="" ){
//		$.tips({
//			content:'密码不能为空',
//			stayTime:2000,
//			type:"warn"
//		});
//		return;
//	}
//	if (isWeiXin()) {
//		$("header").hide();
//		$(".ui-header").hide();
//		$(".input_info_list").css({
//			"margin-top":"0"
//		})
//		$("title").html("更换手机号");
//		var openid = xigou.getLocalStorage("openid");
//		if (openid) {
//			params.uniontype = 1;
//			params.unionval = openid;
//		}
//	}
//	xigou.modifymobile.checklogon({
//		requestBody: params,
//		callback: function(response, status) { //回调函数
//			if('0'==response.code){
//				window.location.href = "changeTel2.html";
////				$(".main_pre").hide();
////				$(".main_next").show().siblings().hide();
//			}else{
//				$.tips({
//					content:'账号验证失败，请重新输入密码',
//					stayTime:2000,
//					type:"warn"
//				});
//				$("#password").val("");
//			}
//		}
//	});
//});

$(".btn_pre").click(function(){
	var oldMobile = $("#oldMobile").text();
	var validateCode = $("#validateCode").val();
	var params = {
		'token': xigou.getToken(),
		'tel': oldMobile,
        'captcha': validateCode
	}
	
	xigou.modifymobile.checkmobile({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if('0'==response.code&&response.data){
				$.tips({
					content:response.msg,
					stayTime:2000,
					type:"info"
				});
				window.location.href = "changeTel2.html";
//				$(".main_pre").hide();
//				$(".main_next").show().siblings().hide();
			}else{
				$.tips({
					content:response.msg,
					stayTime:2000,
					type:"warn"
				});
//				$("#password").val("");
			}
		}
	});
});

var allowGetTheCode = true;
var uuid = "";//极验
var fanli = xigou.getQueryString("fanli");

var handlerEmbed = function (captchaObj) {
    $('.get_code,#achieve')[xigou.events.click](function () {    	
        var validate = captchaObj.getValidate();
        if (!validate) {
            $.tips({
                content: "请先完成验证",
                stayTime: 2000,
                type: "warn"
            });
        } else {        	
        	var tel = $("#input-telnum").val()||$("#oldMobile").text();
        	
            var params = {
                'geetest_challenge': $("[name=geetest_challenge]").val(),
                'geetest_validate': $("[name=geetest_validate]").val(),
                'geetest_seccode': $("[name=geetest_seccode]").val(),
                'tel': tel,
                'randid':$("#randid").val(),
                'type' : "11"
            };
            var dp = {
                requestBody: params,
                callback: function (response, status) { //回调函数
                    if (status == xigou.dictionary.success) {
                        if (null == response) {
                            $.tips({
                                content: xigou.dictionary.chineseTips.server.value_is_null,
                                stayTime: 2000,
                                type: "warn"
                            })
                        } else {
                            if (response.code == "0") {
                            	setCodeTime(60);
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

//获取验证码
function getTheCode() {
    //$('.register-btn,#achieve')[xigou.events.click](function() {
    //	var thisId = this.id;
    //
    //	var params = {
    //		'tel': $(".input-telnum").val(),
    //		'type': '1', //“1”注册验证码，“0”修改密码验证码
    //	};
    //	var dp = {
    //		requestBody: params,
    //		callback: function(response, status) { //回调函数
    //			if (status == xigou.dictionary.success) {
    //				if (null == response) {
    //					$.tips({
    //			            content:xigou.dictionary.chineseTips.server.value_is_null,
    //			            stayTime:2000,
    //			            type:"warn"
    //			         })
    //				} else {
    //					var stauts = response.code;
    //					switch (stauts) {
    //						case "0":
    //							if(thisId=="achieve"){
    //								$('#achieve').parents(".controls").addClass('hide');
    //								$('#code,#pwd,#registr').parents(".controls").removeClass('hide');
    //							}
    //							setCodeTime(60);
    //							break;
    //						default:
    //							$.tips({
    //					            content:response.msg||"获取验证码失败",
    //					            stayTime:2000,
    //					            type:"warn"
    //					        })
    //							break;
    //					}
    //				}
    //			} else {
    //				$.tips({
    //					content:'请求失败，详见' + response,
    //					 stayTime:2000,
    //					type:"warn"
    //				})
    //			}
    //		}
    //	};
    //	xigou.activeUser.getTheCode(dp);
    //});

    //$('.register-btn,#achieve')[xigou.events.click](function () {
    //
    //  //  showCodeImg('input-telnum', 1, $('.register-btn'));
    //
    //})
}

//验证码倒计时
function setCodeTime(time) {
    var getTheCode = $('.get_code');
    $('.get_code').addClass("status");
    getTheCode.text(time+'秒后重新获取');
    var _flag = setInterval(function () {
        if (time <= 0) {
            clearInterval(_flag);
            $('.get_code').removeClass("status");
            getTheCode.text("获取验证码");
            allowGetTheCode = true;
            return;
        }
        getTheCode.text(--time+'秒后重新获取');
        
    }, 1000);

}
function setCodeTime1(time) {
    var getTheCode = $('.get_code1');
    $('.get_code1').addClass("status");
    getTheCode.text(time+'秒后重新获取');
    var _flag = setInterval(function () {
        if (time <= 0) {
            clearInterval(_flag);
            $('.get_code1').removeClass("status");
            getTheCode.text("获取验证码");
            allowGetTheCode = true;
            return;
        }
        getTheCode.text(--time+'秒后重新获取');
        
    }, 1000);

}

$(".btn_next").click(function(){
	var newMobile = $("#input-telnum").val();
	var validateCode = $("#validateCode").val();
	var params = {
		'token': xigou.getToken(),
		'tel': newMobile,
        'captcha': validateCode
	}
	
	xigou.modifymobile.modifymobile({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if('0'==response.code){
				$(".oldMobile").text(response.data.tel);
				xigou.setLocalStorage("show_name",response.data.tel);
				xigou.setLocalStorage("login_name",response.data.tel)
				var back = {
					'token': response.data.token,
					'telephone': response.data.tel,
					'name': response.data.tel
				};
				xigou.setSessionStorage("userinfo", back, true);
//				xigou.setToken(response.data.token);
				xigou.setLocalStorage("token", response.data.token);
				$.tips({
					content:response.msg,
					stayTime:2000,
					type:"warn"
				});
				xigou.setSessionStorage("success",response.msg);
				window.location.href = "home.html";
//				$(".main_pre").hide();
//				$(".main_next").show().siblings().hide();
			}else{
				$.tips({
					content:response.msg,
					stayTime:2000,
					type:"warn"
				});
//				location.reload();
//				$("#password").val("");
			}
		}
	});
	
});

$("#getCode").click(function(){
	var tel = $("#oldMobile").text();
	
    var params = {
        'tel': tel,
        'randid':$("#randid").val(),
        'type' : "11"
    };
    var dp = {
        requestBody: params,
        callback: function (response, status) { //回调函数
            if (status == xigou.dictionary.success) {
                if (null == response) {
                    $.tips({
                        content: xigou.dictionary.chineseTips.server.value_is_null,
                        stayTime: 2000,
                        type: "warn"
                    })
                } else {
                    if (response.code == "0") {
                    	setCodeTime(60);
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
            } else {
                xigou.alert('请求失败，详见' + response);
            }
        }
    };
    xigou.modifymobile.getcaptcha(dp);
});
$("#getCode1").click(function(){
	var tel = $("#input-telnum").val();
	
    var params = {
        'tel': tel,
        'randid':$("#randid").val(),
        'type' : "11"
    };
    var dp = {
        requestBody: params,
        callback: function (response, status) { //回调函数
            if (status == xigou.dictionary.success) {
                if (null == response) {
                    $.tips({
                        content: xigou.dictionary.chineseTips.server.value_is_null,
                        stayTime: 2000,
                        type: "warn"
                    })
                } else {
                    if (response.code == "0") {
                    	setCodeTime1(60);
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
            } else {
                xigou.alert('请求失败，详见' + response);
            }
        }
    };
    xigou.modifymobile.getcaptchaNew(dp);
});


function isWeiXin() {
    var ua = window.navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i) == 'micromessenger')
    {
        return true;
    }else{
        return false;
    }
}