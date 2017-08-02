$(function(){
	if(!isWeiXin()){
		initgeetest();
	}
	else{
		getCode();
	}
	bindTel();
    bindTheCode();
//    initgeetest();
    if(isWeiXin()){
    	$(".ui-header").hide();
    	$(".input_info_list").css({
    		"margin-top":"0"
    	})
    	$("title").html("绑定手机号码");
    	$(".hide").hide();
    	$(".btn_pre").show();
    	//光标落入样式更改
    	$(".input-telnum").bind("focus",function(e){
    		$(".get_code").css({
    			"color":"#333",
    			"border-color":"#333"
    		})
    	})
    	$("#checkCode").bind("focus",function(e){
    		$(".btn_pre").css({
    			"background":"#333"
    		})
    	})
    	$("#hide-div").show();
    	$(".get_code").show();
    }
});

$("#hide-div").hide();


var handlerEmbed = function (captchaObj) {
    $('.h5,#achieve')[xigou.events.click](function () {
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
    //                            });
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
        //showCodeImg('input-telnum', 6, $('.register-btn'));
    })
}


function setCodeTime(time){
    var getTheCode=$('.h5');
    getTheCode.text(time+"s重发").addClass("disable");
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
        }
    	
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
                                });
                                setTimeout(function(){
                                    var back_url = xigou.getSessionStorage('loginjump_url') || 'index.html';
                                    xigou.setSessionStorage('loginjump_url', '');
                                    window.location.href = back_url;
                                },2000);
                                break;
                            default:
                                $.tips({
                                    content: response.msg || "获取验证码失败",
                                    stayTime: 2000,
                                    type: "warn"
                                });
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
        };
        xigou.activeUser.bindunion(dp);
    })
}
/**
 * 微信用户绑定手机号，发送手机验证码，不用图片校验
 */
function getCode(){
	$('.wx,#achieve')[xigou.events.click](function () {
	    var params = {
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
	                        $(".h5").hide();
                        	$(".wx").css({
                            	"color":"#ccc",
                            	"border-color":"#ccc"
                            });
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
	    xigou.activeUser.getCaptcha(dp);
	});
}