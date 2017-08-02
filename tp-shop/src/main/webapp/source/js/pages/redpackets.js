var ccodes = ["1916165F0A4CBCB6","9F9D4EA3A27D73D6","B2A2154606897C96"];
var bReceive = false;
var imgUrl = "http://m.51seaco.com/img/redpackets/shareicon.png";
var lineLink = "http://m.51seaco.com/redpackets.html";
var title = '猴年猴赛雷 小西拜年派大礼';
var desc = "我正在领取全球购商城397元限量新春礼券包，你要一起来吗？手慢无哦。";
$(function() {
    clickPostCode();
    clickReceiceRedPacket();
    share();
    login();
    onClickHome();

    if(!isWeiXin())
    {
        $(".share").hide();
        $(".click_to_share").hide();
        $(".get_redpacket_input_result").css({"height": "60px"});
        $(".get_redpacket_input_check").css({"height": "60px"});
    } else {
        InitWeiXin();
    }
})

// 获取验证码
function clickPostCode() {
    $("#post_code")[xigou.events.click](function(){
        var params = {
            'tel': $("#input_tel").val(),
            'type': '4', // 4.手机号领取优惠券
        };
        var dp = {
            requestBody: params,
            callback:function(response, status) { // 回调函数
                if (status == xigou.dictionary.success) {
                    if (null == response) {
                        $.tips({
                            content:xigou.dictionary.chineseTips.server.value_is_null,
                            stayTime:2000,
                            type:"warn"
                        })
                    } else {
                        var stauts = response.code;
                        switch (stauts) {
                            case "0":
                                $('#post_code').hide();
                                $('.btnClassdisable').show();
                                setCodeTime(60);
                                break;
                            default:
                                $.tips({
                                    content:response.msg||"获取验证码失败",
                                    stayTime:2000,
                                    type:"warn"
                                })
                                break;
                        }
                    }

                } else {
                    $.tips({
                        content: response.msg || '请求失败',
                        stayTime:2000,
                        type:"warn"
                    })
                }

            }

        };
        xigou.activeUser.getTheCode(dp);
    })
}

function setCodeTime(time){
    var getTheCode=$('#registertime');
    getTheCode.text(time);
    var _flag=setInterval(function(){
        if(time<=0){
            clearInterval(_flag);
            $('#post_code').show();
            $('.btnClassdisable').hide();
            return;
        }
        getTheCode.text(--time);
    },1000);

}

// 领取礼包
function  clickReceiceRedPacket() {
    $("#click_to_receive")[xigou.events.click](function(){
        var params = {
            'ccodes': ccodes,
            'tel': $("#input_tel").val(),
            'captcha': $("#input_code").val(),
        };
        xigou.activeUser.redpacket({
            requestBody: params,
            callback: function(response, status) { // 回调函数
                if (status == xigou.dictionary.success) {
                    var succ = 0;   // 0 验证失败  1 领取失败 2领取成功
                    var msg = '';
                    var btnMsg = '';

                    if (null == response) {
                        msg = '领取红包失败';
                    }
                    else {
                        switch (response.code) {
                            case "0":
                                succ = 2;
                                msg = '397元限量新春礼券包已入账';
                                btnMsg = '立即开始购物'
                                break;
                            case "-1":      // 其他已知异常
                            case "-2":      // 系统异常
                                succ = 0;
                                msg = "出错啦,再试一次吧";
                                break;
                            case "-3":      // 已领取
                                succ = 1;
                                msg = "已领过新春大礼包！";
                                btnMsg = '去西客逛一逛';
                                break;
                            case "-11":      // 领券活动尚未开始
                                succ = 1;
                                msg = "活动还未开始,请稍后再来";
                                btnMsg = "先去物色好货";
                                break;
                            case "-12":      // 已过了优惠券的领取时间
                                succ = 1;
                                msg = "活动已结束,下次在跟小西一起玩！";
                                btnMsg = '去西客逛一逛';
                                break;
                            case "-13":      // 优惠券已经领取完
                                succ = 1;
                                msg = "新春大礼包已抢完，下次请赶早！";
                                btnMsg = '去西客逛一逛';
                                break;
                            case "-14":      // 手机号格式错误
                                succ = 0;
                                msg = "请输入正确的手机号！";
                                break;
                            default:
                                succ = 0;
                                msg = response.msg ||"领取红包失败";
                                break;
                        }
                    }

                    if (0 == succ) {
                        $.tips({
                            content: msg ||"领取红包失败",
                            stayTime: 2000,
                            type:"warn"
                        });
                    } else if (1 == succ) {
                        var htmlData = [];
                        htmlData.push('<div style="font-size: 26px;height: 26px;padding-top: 24px">' + msg + '</div>');
                        $('.get_redpacket_result_info').html(htmlData.join(" "));
                        $('.get_redpacket_div').hide();
                        $('.get_redpacket_result').show();

                        $("#home").html('<span>' + btnMsg + '</span>');

                        desc = "我在西客领到了397元的限量新春大礼包，是朋友才告诉你哦，加速抢！";
                    } else if (2 == succ) {
                        var htmlData = [];
                        htmlData.push('<div style="font-size: 17px;height: 26px;padding-top: 40px">' + msg + '</div>');
                        $('.get_redpacket_result_info').html(htmlData.join(" "));
                        $('.get_redpacket_div').hide();
                        $('.get_redpacket_result').show();

                        $("#home").html('<span>' + btnMsg + '</span>');
                        desc = "我在西客领到了397元的限量新春大礼包，是朋友才告诉你哦，加速抢！";
                    }
                } else {
                    $.tips({
                        content:"领取红包失败",
                        stayTime:2000,
                        type:"warn"
                    });
                }
            }
        })
    })
}

// 分享给朋友
function share() {
    $(".share")[xigou.events.click](function(){
        $('.share_friends').show();
        $('.share').hide();
    })
    $(".share_friends")[xigou.events.click](function(){
        $(".share_friends").hide();
        $('.share').show();
    })
    $(".click_to_share")[xigou.events.click](function(){
        $('.share_friends').show();
        $('.share').hide();
    })
}

// 登录
function login() {
    $("#login")[xigou.events.click](function(){
        window.location.href="logon.html";
    })
    $("#home")[xigou.events.click](function(){
        window.location.href="index.html";
    })
}

function isWeiXin() {
    var ua = window.navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i) == 'micromessenger')
    {
        return false;
    }else{
        return false;
    }
}

function  onClickHome() {
    $(".home_div")[xigou.events.click](function(){
        window.location.href="index.html";
    })
}

function  InitWeiXin() {
    var pa = [];
    var url = window.location.href;
    pa.push('url=http://m.51seaco.com/redpackets.html');

    xigou.activeUser.config({
        p : pa.join('&'),
        callback: function(response, status) { //回调函数
            if (status != xigou.dictionary.success) {
                return;
            } else if (!response || 0 != response.code) {
                return;
            }
            var data = response.data;
            wx.config({
                appId: data.appid,
                timestamp: data.timestamp,
                nonceStr: data.nonceStr,
                signature: data.signature,
                jsApiList: [
                    'checkJsApi',
                    'onMenuShareTimeline',
                    'onMenuShareAppMessage',
                    'onMenuShareQQ',
                    'onMenuShareWeibo',
                    'onMenuShareQZone',
					'scanQRCode',
                ]
            });
        }
    })
}

function checkInputTel() {
    var bEnablePostCode = false;
    var bEnableReceive = false;
    var telInput=document.getElementById('input_tel');
    if (!telInput) {
        return;
    }

    var telNum = telInput.value;
    var codeNum = document.getElementById('input_code').value;
    if (telNum) {
        bEnablePostCode = true;
        if (codeNum) {
            bEnableReceive = true;
        }
    }

    if (bEnablePostCode) {
        $('#post_code').removeClass("post_code_disable");
    } else {
        $('#post_code').addClass("post_code_disable");
    }

    if (bEnableReceive) {
        $('#click_to_receive').removeClass("big_btn_disable");
    } else {
        $('#click_to_receive').addClass("big_btn_disable");
    }
}

function checkInputCode() {
    var bEnableReceive = false;
    var telNum = document.getElementById('input_tel').value;
    var codeNum = document.getElementById('input_code').value;

    if (telNum && codeNum) {
        bEnableReceive = true;
    }

    if (bEnableReceive) {
        $('#click_to_receive').removeClass("big_btn_disable");
    } else {
        $('#click_to_receive').addClass("big_btn_disable");
    }
}

wx.ready(function () {
    // 1 判断当前版本是否支持指定 JS 接口，支持批量判断
    wx.checkJsApi({
        jsApiList: [
            'getNetworkType',
            'previewImage'
        ],
        success: function (res) {
        }
    });

    wx.onMenuShareAppMessage({
        title: title,
        desc: desc,
        link: lineLink,
        imgUrl: imgUrl,
        trigger: function (res) {
            // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
        },
        success: function (res) {
        },
        cancel: function (res) {
        },
        fail: function (res) {
        }
    });

    wx.onMenuShareTimeline({
        title: title,
        link: lineLink,
        imgUrl: imgUrl,
        trigger: function (res) {
            // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
        },
        success: function (res) {
        },
        cancel: function (res) {
        },
        fail: function (res) {
        }
    });

    wx.onMenuShareQQ({
        title: title,
        desc: desc,
        link: lineLink,
        imgUrl: imgUrl,
        trigger: function (res) {
        },
        complete: function (res) {
        },
        success: function (res) {
        },
        cancel: function (res) {
        },
        fail: function (res) {
        }
    });

    wx.onMenuShareWeibo({
        title: title,
        desc: desc,
        link: lineLink,
        imgUrl: imgUrl,
        trigger: function (res) {
        },
        complete: function (res) {
        },
        success: function (res) {
        },
        cancel: function (res) {
        },
        fail: function (res) {
        }
    });

    wx.onMenuShareQZone({
        title: title,
        desc: desc,
        link: lineLink,
        imgUrl: imgUrl,
        trigger: function (res) {
        },
        complete: function (res) {
        },
        success: function (res) {
        },
        cancel: function (res) {
        },
        fail: function (res) {
        }
    });
})

wx.error(function (res) {
});