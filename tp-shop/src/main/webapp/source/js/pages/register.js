// JavaScript Document

$(function () {
    getTheCode();
    registr();
    initgeetest();
});

var allowGetTheCode = true;
var uuid = "";//极验
var fanli = xigou.getQueryString("fanli");

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
                'type' : "1"
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


//获取图形验证码
function getImgCode() {
    $('input#name').change(function () {
        if (!xigou.valiformdata.checkSingleNode($("#name"), valiDataCallBack)) {
            return;
        }
        $('#codeImg').attr("src", "../../mobile/init/captchaimage.htm?telephone=" + $('input#name').val() + "&typeimage=1&time=" + new Date().getTime());

    });
    $('#codeImg').click(function () {
        if (!xigou.valiformdata.checkSingleNode($("#name"), valiDataCallBack)) {
            return;
        }
        $('#codeImg').attr("src", "../../mobile/init/captchaimage.htm?telephone=" + $('input#name').val() + "&typeimage=1&time=" + new Date().getTime());
        $('#imgcode').val('');
    });
}
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

function setCodeTime(time) {
    var getTheCode = $('.register-btn');
    getTheCode.text(time);
    var _flag = setInterval(function () {
        if (time <= 0) {
            clearInterval(_flag);
            getTheCode.text("获取验证码");
            allowGetTheCode = true;
            return;
        }
        getTheCode.text(--time);
    }, 1000);

}


//注册
function registr() {

    //获取页面高度，设置body高度，解决输入法弹出把logo推上来问题
    var bh = $("body").height(),
        wh = $(window).height();
    $(".logo").css("top", (Math.max(bh, wh)) * 0.92 - 42 + "px");

    $('.register-register')[xigou.events.click](function () {

        var params = {
            'loginname': $("#input-telnum").val(),
            'captcha': $("#input-code").val(),
            'pwd': $("#input-pwd").val()
        };

        // 推荐人
        var dssUser = xigou.getSessionStorage("dssUser");
        if (dssUser && dssUser != "{}") {
            dssUser = JSON.parse(dssUser);
            if (dssUser.mobile) {
                params.shopMobile = dssUser.mobile;
            }
        }

//        // 民生银行
//        var channelCode = cmbc.getChannelCode(false);
//        if (channelCode) {
//            params.channelcode = channelCode;
//            params.uniontype = cmbc.getUnionType();
//            params.unionval = cmbc.getUnionVal();
//            params.tpin = cmbc.getTuuid();
//        }
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
                        xigou.debugPrint("注册成功返回的code值：" + stauts);
                        switch (stauts) {
                            case "0":
                                var loginjump_url = xigou.getSessionStorage("loginjump_url");
                                if($("#input-telnum").val())
                                	xigou.setLocalStorage("login_name", $("#input-telnum").val());
                                var back = {
                                    'token': response.data.token,
                                    'telephone': response.data.tel,
                                    'name': response.data.name
                                };

                                var dia = $.dialog({
                                    title: '',
                                    content: response.msg || "注册成功",
                                    button: ["确认"]
                                });

                                dia.on("dialog:action", function (e) {
                                    console.log(e.index);
                                    window.location.href = "logon.html";
//                                    window.location.href = "home.html";
                                    xigou.removeSessionStorage('realname');
                                    xigou.removeSessionStorage('realnum');
                                    xigou.setSessionStorage("userinfo", back, true);
                                    if($("#input-telnum").val())
                                    	xigou.setLocalStorage("login_name", $("#input-telnum").val());
                                    xigou.setLocalStorage("token", response.data.token);
                                });
                                break;
                            case "-2":
                                $.tips({
                                    content: response.msg || "验证码错误",
                                    stayTime: 2000,
                                    type: "warn"
                                });
                                break;
                            default:
                                $.tips({
                                    content: response.msg || "注册失败",
                                    stayTime: 2000,
                                    type: "warn"
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
};


function valiDataCallBack(message, id, mname) {
    $.tips({
        content: message,
        stayTime: 2000,
        type: "warn"
    })
    $("#" + id).focus();
};

function initValiData() {
    var _form = {
        methods: {
            "name": {
                required: "required",
                mobile: true
            },
            "code": {
                required: "required"
            },
            "pwd": {
                required: "required",
                password: true
            },
            "imgcode": {
                required: "required"
            }
        },
        errors: {
            "name": {
                required: "请输入手机号码",
                mobile: "手机号码不正确，请重新输入"
            },
            "code": {
                required: "请输入验证码"
            },
            "pwd": {
                required: "请输入密码",
                password: "请输入6-30位密码，可以为数字、字母和下划线"
            },
            "imgcode": {
                required: "请输入验证码"
            }
        }
    };
    return xigou.valiformdata.initValiData(_form);
};