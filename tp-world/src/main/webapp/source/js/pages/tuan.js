﻿var gbid = xigou.getQueryString("gbid");
var gid = xigou.getQueryString('gid') || xigou.getSessionStorage('tuan_gid');
var _flag = null;
var imgUrl = '';

$(function() {
	
	
    var from = xigou.getQueryString('from');
    var isappinstalled = xigou.getQueryString('isappinstalled');
    if (from || isappinstalled) {
        var link = 'tuan.html?gbid='+gbid;
        if (gid) {
            link += '&gid=' + gid;
        }
        window.location.href = link;
        return;
    }
    xigou.getLoginUserInfo({
        callback:function(userinfo,status){
            if(status != xigou.dictionary.success){
                var url = 'tuan.html?gbid=' + gbid + '&gid=' + gid;
                xigou.setSessionStorage("loginjump_url", window.location.href);
                window.location.href = "logon.html";
            }
            else {
                InitPage();
            }
        }
    });

    showItemDetail();
});

function InitPage() {
    var backurl = document.referrer;
    if (backurl) {
        var from = backurl.split('from').length;
        var isappinstalled = backurl.split('isappinstalled').length;
        if (from > 1 || isappinstalled > 1) {
            $(".goBack").attr("href", 'index.html');
        }
        else  if(backurl.split('login.html').length > 1) {
            $(".goBack").attr("href", 'index.html');
        }
        else {
            $(".goBack").attr("href", 'javascript:history.go(-1);');
        }
    }
    else {
        $(".goBack").attr("href", 'index.html');
    }

    var params = {
        gbid: gbid,
        gid: gid,
        token: xigou.getToken(),
    }

    xigou.groupbuy.detail({
        requestBody: params,
        callback: function(response, status) {  // 回调函数
            if (status == xigou.dictionary.success) {
                if (response.code == 0) {
                    InitStatus(response.data);
                    InitProductInfo(response.data.product);
                    InitTunInfo(response.data);
                    InitWeiXinShare();
                }
                else if (response.code == "-100") { // Token过期
                    var url = 'tuan.html?gbid=' + gbid + '&gid=' + gid;
                    xigou.setSessionStorage("loginjump_url", window.location.href);
                    window.location.href = "logon.html";
                }
                else {
                    $.tips({
                        content:response.msg||'获取数据失败,请检测网络连接!',
                        stayTime:2000,
                        type:"warn"
                    });
                }
            }
            else if (!response) {
                $.tips({
                    content:'获取数据失败,请检测网络连接!',
                    stayTime:2000,
                    type:"warn"
                });
            }
            else {
                $.tips({
                    content:response.msg||'获取数据失败,请检测网络连接!',
                    stayTime:2000,
                    type:"warn"
                });
            }
        }
    })

    window.onbeforeunload = function(){
        if (_flag) {
            clearInterval(_flag);
            _flag = "";
        }
    }
}

// 初始化商品信息
function InitProductInfo(data) {
    var htmlData = [];
    htmlData.push('<div class="ui-slider">');
    htmlData.push('<ul class="ui-slider-content">');
    for (var i = 0; i < data.imglist.length; i++) {
        htmlData.push('<li>');
        htmlData.push('<a href="javascript:void(0)">');
        htmlData.push('	<img src="' + data.imglist[i] + '"/>');
        htmlData.push('	</a>');
        htmlData.push('</li>');
    }

    if (data.imglist && data.imglist.length > 0) {
        imgUrl = data.imglist[0];
    }

    htmlData.push('</ul>');
    htmlData.push('<a href="itemdetail.html" class="more_details"></a>');
    htmlData.push('</div>');
    $('#slider').empty().html(htmlData.join(''));
    var slider = new fz.Scroll('.ui-slider', {
        role: 'slider',
        indicator: true,
        autoplay: true,
        interval: 3000
    });

    var Yuan = data.price.split('.')[0] || '00';
    var Jiao = data.price.split('.')[1] || '00';
    $('.YUAN').text(Yuan);
    $('.JIAO').text(Jiao);
    if (data.oldprice) {
        $('.old_price').text('¥' + data.oldprice);
    }

    if(data.salescountdesc) {
        $('.div_coun').text(data.salescountdesc);
    }

    if (data.name) {
        $('.div_title').text(data.name);
    }
    if (data.feature) {
        $('.div_desc').text(data.feature);
    }

    $("#contry-img").attr("src",data.countryimg);
    if (data.channel) {
        $(".warehouse")[0].innerHTML = data.channel;
    }
    if (data.countryname) {
        $(".country-name")[0].innerHTML = data.countryname;
    }

    xigou.setLocalStorage("itemdetail",data.detail);
    $('.detail-content').html(data.detail);
}

// 初始化团购信息
function InitTunInfo(data) {
    if (!data.fa) {
        data.fa = 0;
    }
    var prenct = parseInt(100*data.fa/data.pa);
    prenct += '%';
    $('.div_progress div').css({'width':prenct});
    $('.div_tuan_count').text(data.fa + '/' + data.pa);

    $('.my-tuan').unbind(xigou.events.click);
    $('.my-tuan')[xigou.events.click](function(){
        if (gid) {  // 记录gid 在我的团页面点返回用
            xigou.setSessionStorage('tuan_gid', gid);
        }
        window.location.href = 'mytuan.html';
    })
}

// 初始化团购状态
function InitStatus(data) {
    //data.tstatus = '5';
    //data.gid = '1';
    //data.lstatus = '2';
    //data.gstatus = '1';
    //data.pstatus = '2';
    //data.jstatus = '4';

    gbid = data.gbid;
    gid = data.gid;

    if (data.gstatus == '1') {  // 进行中
    	if (_flag) {
            clearInterval(_flag);
            _flag = '';
        };
        Initlsecond(data.lsecond);
        
    }
    else {
        $('.title_desc').empty();
        $('.title_desc').text('活动商品');
    }

    if (data.gtype == '2') {    // 新人团
        $('#mulite_tun').hide();
        $('#new_tun').show();
        $(".tun_introduce")[xigou.events.click](function() {
            window.location.href = 'multituanrule.html';
        })
    }
    else {
        $(".tun_introduce")[xigou.events.click](function() {
            window.location.href = 'newtuanrule.html';
        })
    }

    if (data.tstatus == '5') {    // 活动已结束
        $('.div_join').hide();
        $('.div_start').hide();
        $('.div_disable').show().text('活动已结束');
        return;
    }
    else if(data.product.status == '2') {     // 已抢光
        $('.div_join').hide();
        $('.div_start').hide();
        $('.div_disable').show().text('已抢光');
        return;
    }
    else if(data.product.status == '3') {     // 已下架
        $('.div_join').hide();
        $('.div_start').hide();
        $('.div_disable').show().text('已下架');
        return;
    }
    else if (data.gid) {
        if (data.gstatus == '1') {  // 进行中
            if (data.jstatus == '1') {    // 可参团
                join();
            }
            else if (data.jstatus == '2') {    // 已参团
                $('.div_join').text('已参团').addClass('disable');
            }
            else if (data.jstatus == '3') {    // 限新人
                $('.div_join').text('限新人').addClass('disable');
            }
            else if (data.jstatus == '4') {    // 不可参团
                $('.div_join').text('不可参团').addClass('disable');
            }
        }
        else if (data.gstatus == '2') { // 成功
            if (data.pstatus == '1') {      // 可支付
                $('.div_join').text('立即支付');
                payNow(data.product);
            }
            else if (data.pstatus == '2') { // 已支付
                $('.div_join').text('已支付').addClass('disable');
            }
            else if (data.pstatus == '3') { // 超时
                $('.div_join').text('已超时').addClass('disable');
            }
            else if (data.pstatus == '4') { // 不可支付
                $('.div_join').text('该团已满').addClass('disable');
            }
            else if (data.pstatus == '5') { // 达到限购
                $('.div_join').text('达到限购').addClass('disable');
            }
        }
        else if (data.gstatus == '3') {     // 开团失败
            $('.div_join').text('开团失败').addClass('disable');
        }
        $('.div_join').show();
    }
    else if(!data.gid) {
        $('.div_join').hide();
    }

    if (data.lstatus == '2') {  // 不可发起团购
        if ($('.div_join').hasClass('disable')) {
            $('.div_start').hide();
        }
        else {
            $('.div_start').addClass('disable');
        }
    }
    else {
        launch();
    }
}

// 团购剩余时间
function Initlsecond(second){
    var timeLeft = parseInt(second);
    var htmlData = FormatTime(timeLeft--);
    $('.div_ltime').empty();
    $('.div_ltime').append(htmlData);

    if (_flag) {
        clearInterval(_flag);
        _flag = '';
    };
    
    _flag = setInterval(function(){
        var htmlData = FormatTime(timeLeft--);
        $('.div_ltime').empty();
        $('.div_ltime').append(htmlData);
        if (timeLeft <= 0) {
            location.reload();
        }
    }, 1000);
    
}

function FormatTime(second) {
    var d = 0, h = 0 , m = 0,
    _second = parseInt(second);
    if (_second > 60) {
        m = parseInt(_second/60);
        s = parseInt(_second%60);
        if (m > 60) {
            h = parseInt(parseInt(_second/60)/60);
            m = parseInt(parseInt(_second/60)%60);
            if (h > 24) {
                d = parseInt(parseInt(parseInt(_second/60)/60)/24);
                h = parseInt(parseInt(parseInt(_second/60)/60)%24);
            }
        }
    } 
    s = pad(s, 2);
    m = pad(m, 2);
    h = pad(h, 2);
    d = pad(d, 2);

    var htmlData = ' ';
    htmlData += '<span>' + d + '</span>天';
    htmlData += '<span>' + h + '</span>时';
    htmlData += '<span>' + m + '</span>分';
    htmlData += '<span>' + s + '</span>秒';
    return htmlData;
}

function pad(num, n) {
    var len = num.toString().length;
    while(len < n) {
        num = "0" + num;
        len++;
    }
    return num;
}

// 发起团购
function launch() {
    var $start = $('.div_start');
    $start.unbind(xigou.events.click);
    $start[xigou.events.click](function() {
        var params = {
            gbid: gbid,
            token:xigou.getToken(),
        }

        xigou.groupbuy.launch({
            requestBody: params,
            callback: function (response, status) {  // 回调函数
                if (status == xigou.dictionary.success) {
                    if (response.code == 0) {
                        //InitStatus(response.data);
                        //InitTunInfo(response.data);
                        //$.tips({
                        //    content:'发起团购成功!',
                        //    stayTime:2000,
                        //    type:"warn"
                        //});
                        var link = 'tuan.html?gbid='+response.data.gbid;
                        if (response.data.gid) {
                            link += '&gid=' + response.data.gid;
                        }
                        window.location.href = link;
                    }
                    else {
                        $.tips({
                            content:response.msg||'发起团购失败,请检测网络连接!',
                            stayTime:2000,
                            type:"warn"
                        });
                    }
                }
                else if (!response) {
                    $.tips({
                        content:'发起团购失败,请检测网络连接!',
                        stayTime:2000,
                        type:"warn"
                    });
                }
                else {
                    $.tips({
                        content:response.msg||'发起团购失败,请检测网络连接!',
                        stayTime:2000,
                        type:"warn"
                    });
                }
            }
        })
    })
}

// 立即支付
function payNow(data) {
    var $join = $('.div_join');
    $join.unbind(xigou.events.click);
    $join[xigou.events.click](function() {
        if ($(this).hasClass('disable')) {
            return;
        }

        xigou.removeClearingData();
        var addressData = xigou.getLocalStorage("address", true) || xigou.defaultAddress;
        //商品信息-添加购物信息
        var params = {
            'token': xigou.getLocalStorage('token'),
            'tid': data.tid,
            'sku': data.sku,
            'count': 1,
            'gid':gid
        };

        xigou.setSessionStorage("buy_now_details_url",window.location.href);

        xigou.activeProduct.buynow({
            requestBody: params,
            callback: function(response, status) { //回调函数
                if (status == xigou.dictionary.success) {
                    var json = response || null;
                    if (null == json || json.length == 0) return false;
                    if (json.code == 0) {
                        xigou.setSessionStorage("buy_now_uuid", json.data.uuid);
                        xigou.setSessionStorage("gid",gid);
                        window.location.href="settle.html?gid=" + gid;
                    } else {
                        $.tips({
                            content:json.msg || "立即购买操作失败",
                            stayTime:2000,
                            type:"info"
                        })
                    }
                } else {
                    $.tips({
                        content:'请求失败，详见' + response,
                        stayTime:2000,
                        type:"info"
                    })
                }
            }
        });
    })
}

// 我要参团
function join() {
    var $join = $('.div_join');
    $join.unbind(xigou.events.click);
    $join[xigou.events.click](function() {
        if ($(this).hasClass('disable')) {
            return;
        }

        var params = {
            gbid: gbid,
            gid: gid,
            token:xigou.getToken(),
        }

        xigou.groupbuy.join({
            requestBody: params,
            callback: function (response, status) {  // 回调函数
                if (status == xigou.dictionary.success) {
                    if (response.code == 0) {
                        InitStatus(response.data);
                        InitProductInfo(response.data.product);
                        InitTunInfo(response.data);
                        $.tips({
                            content:'参团成功!',
                            stayTime:2000,
                            type:"warn"
                        });
                    }
                    else {
                        $.tips({
                            content:response.msg||'参团失败,请检测网络连接!',
                            stayTime:2000,
                            type:"warn"
                        });
                    }
                }
                else if (!response) {
                    $.tips({
                        content:'参团失败,请检测网络连接!',
                        stayTime:2000,
                        type:"warn"
                    });
                }
                else {
                    $.tips({
                        content:response.msg||'参团失败,请检测网络连接!',
                        stayTime:2000,
                        type:"warn"
                    });
                }
            }
        })
    })
}

function InitWeiXinShare(){
    if (isWeiXin()) {
        var pa = [];
        //var url = location.href.split('#')[0].replace("&", "%26");
        var gbidd = xigou.getQueryString("gbid");
        var gidd = xigou.getQueryString('gid');
        var lineLink = location.href.split('?')[0];
        var LinkData = [];
        if (gbidd) {
            LinkData.push("gbid=" + gbidd);
        }
        if (gidd) {
            LinkData.push("gid=" + gidd);
        }
        if (LinkData.length > 0) {
            lineLink += "?";
            lineLink += LinkData.join("&");
        }
        var url = lineLink.replace(/&+/g, "%26");

        pa.push('url=' + url);

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
}

wx.ready(function () {
    $('.goShare').show();
    $('.goShare')[xigou.events.click](function(){
        $('.share_friends').show();
    })
    $('.share_friends')[xigou.events.click](function(){
        $('.share_friends').hide();
    })

    var title = "帮我一起抢！" + $('.YUAN')[0].innerText +"."+$('.JIAO')[0].innerText + "元的" + $('.div_title')[0].innerText ;
    var desc = "小西超级福利团，你从未见过的任性价，邀请好友一起狂欢！" + $('.div_desc')[0].innerText;

    wx.onMenuShareAppMessage({
        title: title,
        desc: desc,
        link: location.href.split('?')[0] + '?gbid=' + gbid + '&gid=' + gid,
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
        link: location.href.split('?')[0] + '?gbid=' + gbid + '&gid=' + gid,
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
        link: location.href.split('?')[0] + '?gbid=' + gbid + '&gid=' + gid,
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
        link: location.href.split('?')[0] + '?gbid=' + gbid + '&gid=' + gid,
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
        link: location.href.split('?')[0] + '?gbid=' + gbid + '&gid=' + gid,
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

// 显示/隐藏商品详情
function showItemDetail() {
    var $this = $('.div_show_detail');
    var detail = $('.detail-content');
    $('.div_show_detail')[xigou.events.click](function(){
        if ($this.hasClass('up')) {
            $this.removeClass('up').addClass('down');
            detail.show();
        } else {
            $this.removeClass('down').addClass('up');
            detail.hide();
        }
    })
}