$(function(){
    var back_url = decodeURIComponent(xigou.getQueryString('back_url')) || '//m.51seaco.com/index.html';
    var ua = window.navigator.userAgent.toLowerCase();
    if ("micromessenger" != ua.match(/MicroMessenger/i)) {
        window.location.href = back_url;
        return;
    }
    var openId = xigou.getLocalStorage("openid");
    if(!openId){
    	openId = xigou.getQueryString("openid");
    }
    if (openId && !xigou.getQueryString('getNickName')) {
    	 if(/^.*\.html?$/.test(back_url) || back_url.endWith("/")){
         	back_url = back_url+"?openid="+openId;
         }else{
         	back_url = back_url+"&openid="+openId;
         }
        window.location.href = back_url;
        return;
    }
    if(openId && xigou.getQueryString('getNickName')){
        var wxImage = xigou.getLocalStorage("wxImage");
        var wxnickname = xigou.getLocalStorage("wxnickname");
        if(!wxImage){
        	wxImage = xigou.getQueryString("wxImage");
        }
        if(!wxnickname){
        	wxnickname = xigou.getQueryString("wxnickname");
        }
        if(openId && wxImage && wxnickname){
	        if(/^.*\.html?$/.test(back_url) || back_url.endWith("/")){
	        	back_url = back_url+"?openid="+openId+"&wxImage="+wxImage+"&wxnickname="+wxnickname;
	        }else{
	        	back_url = back_url+"&openid="+openId+"&wxImage="+wxImage+"&wxnickname="+wxnickname;
	        }
	        window.location.href = back_url;
        }
    }
    
    var _code = xigou.getQueryString("code");
    if (_code) {
        getopenid(_code);
        return;
    }
    
    // 获取code链接
    var params = {
        url: encodeURIComponent(window.location.href)
    };
    if (xigou.getQueryString('getNickName')){
        params.scope = 'snsapi_userinfo';
    }
    else {
        params.scope = 'snsapi_base';
    }
    xigou.activeUser.getcode({
        requestBody: params,
        callback:function(response){
            var back_url = response.data.codeurl || '//m.51seaco.com/index.html';
            if (back_url){
                window.location.href = back_url;
            }
        }
    })
});

function getopenid(code) {
    if (xigou.getQueryString('getNickName')){
        xigou.setSessionStorage('getediconnickname', true);
        xigou.activeUser.wxInfo({
            requestBody: {
                code: code
            },
            callback: function(response) {
                if (response && response.code == '0') {
                    if (response.data.openid) {
                        xigou.setLocalStorage("openid", response.data.openid);
                    }
                    if (response.data.headimgurl) {
                        xigou.setLocalStorage("wxImage", response.data.headimgurl);
                    }
                    if (response.data.nickname) {
                        xigou.setLocalStorage("wxnickname", response.data.nickname);
                    }
                }

                var back_url = decodeURIComponent(xigou.getQueryString('back_url')) || '//m.51seaco.com/index.html';
                if(/^.*\.html?$/.test(back_url) || back_url.endWith("/")){
                	back_url = back_url+"?openid="+response.data.openid+"&wxImage="+response.data.headimgurl+"&wxnickname="+response.data.nickname;
                }else{
                	back_url = back_url+"&openid="+response.data.openid+"&wxImage="+response.data.headimgurl+"&wxnickname="+response.data.nickname;
                }
                window.location.href = back_url;
            }
        })
    }
    else {
        xigou.activeUser.openid({
            requestBody: {
                code: code
            },
            callback: function(response) {
                xigou.setLocalStorage("openid", response.data.openid);
                var back_url = decodeURIComponent(xigou.getQueryString('back_url')) || '//m.51seaco.com/index.html';
                if(/^.*\.html?$/.test(back_url) || back_url.endWith("/")){
                	back_url = back_url+"?openid="+response.data.openid;
                }else{
                	back_url = back_url+"&openid="+response.data.openid;
                }
                window.location.href = back_url;
            }
        })
    }
}