$(function() {
	if(isWeiXin()){
		$(".ui-header").hide();
		$(".input_info_list").css({
			"margin-top":"0"
		});
		$(".add_new").show();
		$("title").html("绑定手机号码");
	}
	
    var b_mobile = xigou.getQueryString("mobile");

    bindTheCode();
    bindTel();
    initgeetest();
    if (b_mobile) {
        $(".checkBox").hide();
        $(".register-register").hide();
        $("#input-telnum").val(b_mobile).attr("readonly","readonly");
        $("#embed-captcha").hide();
        $(".register-btn").hide();
    }
});


var handlerEmbed = function (captchaObj) {
    $('.register-btn,#achieve')[xigou.events.click](function () {
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
                'type' : "6" //bind tel
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
                                $("#hide-div").show();
                                $(".register-btn").hide();
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

function bindTheCode(){
    //$('.register-btn')[xigou.events.click](function() {
    //    if($('.register-btn').hasClass('disable')){
    //        return;
    //    }
    //
    //    var params = {
    //        'tel': $(".input-telnum").val(),
    //        'type': '6' //“1”注册验证码，“0”修改密码验证码 "6"绑定手机号
    //    };
    //    var dp = {
    //        requestBody: params,
    //        callback: function(response, status) { //回调函数
    //            if (status == xigou.dictionary.success) {
    //                if (null == response) {
    //                    $.tips({
    //                        content:xigou.dictionary.chineseTips.server.value_is_null,
    //                        stayTime:2000,
    //                        type:"warn"
    //                    })
    //                } else {
    //                    var stauts = response.code;
    //                    switch (stauts) {
    //                        case "0":
    //                            setCodeTime(60);
    //                            break;
    //                        default:
    //                            $.tips({
    //                                content:response.msg||"获取验证码失败",
    //                                stayTime:2000,
    //                                type:"warn"
    //                            })
    //                            break;
    //                    }
    //                }
    //            } else {
    //                $.tips({
    //                    content:'请求失败，详见' + response,
    //                    stayTime:2000,
    //                    type:"warn"
    //                })
    //            }
    //        }
    //    };
    //    xigou.activeUser.getTheCode(dp);
    //});

    $('.register-btn')[xigou.events.click](function() {
      // showCodeImg('input-telnum', 6, $('.register-btn'));
    })
};

function setCodeTime(time){
    var getTheCode=$('.register-btn');
    getTheCode.text(time+"s重发").addClass("disable").unbind("click");
    var _flag=setInterval(function(){
        if(time<=0){
            clearInterval(_flag);
            getTheCode.text("获取验证码").removeClass("disable");
            bindTheCode();
            return;
        }
        getTheCode.text(--time+"s重发");
    },1000);
}
function bindTel(){
    $(".register-register")[xigou.events.click](function(){
        var params = {
            'token': xigou.getToken(),
            'tel': $("#input-telnum").val(),
            'captcha': $("#checkCode").val(),
            'uniontype': '1'
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
                        var stauts = response.code;
                        switch (stauts) {
                            case "0":
                                if (response.data.promoterinfo && response.data.promoterinfo != "{}") {
                                    var promoterinfo = 	JSON.parse(response.data.promoterinfo);
                                    promoterinfo.token = response.data.token;
                                    xigou.setLocalStorage("dssUser", promoterinfo, true);
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
                                xigou.setLocalStorage("show_name", response.data.tel || response.data.name);
                                xigou.setLocalStorage("token", response.data.token);

                                $.tips({
                                    content: response.msg || "绑定成功",
                                    stayTime: 2000,
                                    type: "warn"
                                })
                                setTimeout(function(){
                                    window.location.href = "home.html";
                                },1500);
                                break;
                            default:
                                $.tips({
                                    content: response.msg || "获取验证码失败",
                                stayTime: 2000,
                                type: "warn"
                        })
                                break;
                        }
                    }
                } else {
                    $.tips({
                        content: '请求失败，详见' + response,
                        stayTime: 2000,
                        type: "warn"
                    })
                }
            }
        }
        xigou.activeUser.bindunion(dp);
    })
}