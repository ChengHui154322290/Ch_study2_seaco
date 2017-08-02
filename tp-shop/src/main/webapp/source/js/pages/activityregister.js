$(function(){
    if(isWeiXin()){
        var openid = xigou.getLocalStorage("openid");
        if (!openid) {
            xigou.getwxOpenid();
            return;
        }
    }

    showattention();
    getTheCode();
    register();

    function showattention(){
        $('.top_tip')[xigou.events.click](function(){
            $('.div_attention').show();
        });
        $('.close_attention')[xigou.events.click](function(){
            $('.div_attention').hide();
        })
    }

    // 注册
    function register(){
        $('.register-register')[xigou.events.click](function() {
            var params = {
                'loginname': $("#input-telnum").val(),
                'captcha': $("#input-code").val(),
                'pwd': $("#input-pwd").val()
            };

            if(isWeiXin()){
                params.uniontype = '1';
                params.unionval = xigou.getLocalStorage("openid");
            }

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
                        } else {
                            var stauts = response.code;
                            xigou.debugPrint("注册成功返回的code值：" + stauts);
                            switch (stauts) {
                                case "0":
                                    jumpstep2(response.data.token);
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
            xigou.activeUser.registr(dp);
        });
    }

    // 获取验证码
    function getTheCode() {
        $('.register-btn')[xigou.events.click](function() {
            if ($('.register-btn').hasClass('disable')) {
                return;
            }
            var params = {
                'tel': $(".input-telnum").val(),
                'type': '1'
            };
            var dp = {
                requestBody: params,
                callback: function(response, status) {
                    //回调函数
                    if (status == xigou.dictionary.success) {
                        if (null  == response) {
                            $.tips({
                                content: xigou.dictionary.chineseTips.server.value_is_null,
                                stayTime: 2000,
                                type: "warn"
                            })
                        } else {
                            var stauts = response.code;
                            switch (stauts) {
                                case "0":
                                    $('.register-btn').addClass("disable");
                                    setCodeTime(60);
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
            };
            xigou.activeUser.getTheCode(dp);
        });
        function setCodeTime(time) {
            var getTheCode = $('.register-btn');
            getTheCode.text(time);
            var _flag = setInterval(function() {
                if (time <= 0) {
                    clearInterval(_flag);
                    getTheCode.text("获取验证码");
                    $('.register-btn').removeClass("disable");
                    return;
                }
                getTheCode.text(--time);
            }, 1000);

        }
    }

    function jumpstep2(token){
        var params = {
            token:token
        };
        xigou.activeUser.offlinecouponcode({
            requestBody: params,
            callback: function(response, status) {  // 回调函数
                if (status == xigou.dictionary.success && response.code == 0) {
                    $('.div_step_1').hide();
                    $('.div_step_2').show();
                    if (response.data.qrcode) {
                        $('.div_code_bg').html('<img src="' + response.data.qrcode + '">')
                    }
                }
                else if(response && response.msg){
                    $.tips({
                        content:response.msg,
                        stayTime:2000,
                        type:"warn"
                    });
                }
                else {
                    $.tips({
                        content:'获取获取线下活动的兑换码失败',
                        stayTime:2000,
                        type:"warn"
                    });
                }
            }
        })
    }
});