$(function() {
    var ua = window.navigator.userAgent.toLowerCase();
    if(isWeiXin()){
        var e = xigou.getLocalStorage("openid")
            , t = xigou.getQueryString("channel")
            , i = xigou.getQueryString("type");
        if (e && t) {
            var n = xigou.activeHost + "mkt/channel/save.htm?channel=" + t + "&uuid=" + e + "&type=" + i;
            window.location.href = n
        }
        else {
            var _code = xigou.getQueryString("code");
            if (_code) {
                xigou.setSessionStorage("weixin_code", _code);
                getopenid();
            }
            else {
                var n = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6f7f5f0bab32e7d3&redirect_uri=' + location.href.replace("&", "%26") + '&response_type=code&scope=snsapi_base&state=1#wechat_redirect';
                window.location.href = n;
            }
        }
    }
});

function getopenid() {
    var e = xigou.getSessionStorage("weixin_code");
    xigou.activeUser.openid({
        requestBody: {
            code: e
        },
        callback: function(e) {
            xigou.setLocalStorage("openid", e.data.openid);
            var e = xigou.getLocalStorage("openid")
                , t = xigou.getQueryString("channel")
                , i = xigou.getQueryString("type");
            if (e && t) {
                var n = xigou.activeHost + "mkt/channel/save.htm?channel=" + t + "&uuid=" + e + "&type=" + i;
                window.location.href = n
            }
        }
    })
}