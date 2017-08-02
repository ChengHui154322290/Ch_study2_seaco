$(function(){
    if (!isWeiXin()) {
        return;
    }

    var openid = xigou.getLocalStorage("openid");
    if (!openid) {
        xigou.getwxOpenid(1, true);
        return;
    }

    // 联合登录获取用户分销信息
    var params = {
        uniontype: '1',
        unionval: openid,
//        signature:'1a0708f6e3fdc5ebb25c89a4e0765fdc', //wx1.0.1
        signature:'af288db869b27f21cc1440385d63d4d5', //wx1.5.0
        curtime: (new Date()).valueOf().toString()
    };

    xigou.activeUser.unionlogon({
        requestBody: params,
        callback: function(response, status) { // 回调函数
            if (status == xigou.dictionary.success && response && response.data && response.data.promoterinfomobile && response.data.promoterinfomobile.isshopdss) {   // 分销员
                // 注册成功 跳转到dsshome页面
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
                xigou.setLocalStorage("login_name", response.data.tel);
                xigou.setLocalStorage("show_name", response.data.tel || response.data.name);
                xigou.setLocalStorage("token", response.data.token);

                window.location.href = 'dss/dsshome.html';
            }else {
                window.location.href = 'shop.html';
            }
        }
    })
});