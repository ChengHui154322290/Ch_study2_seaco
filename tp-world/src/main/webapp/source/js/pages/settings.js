/**
 * 
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-14 13:41:54
 * @version Ver 1.0.0
 */
$(function(){
	if (isWeiXin()) {
    	 $("header").hide();
    	 $("title").html("设置");
	 }
	signout();
	modifypwd();
})

function signout(){
	$('.signout')[xigou.events.click](function() {
		
        var dia=$.dialog({
            title:'',
            content:'您确定要退出当前用户吗？',
            button:["取消","确认"]
        });

        dia.on("dialog:action",function(e){
            if(1 == e.index)
            {
                var params = {
                    'token': xigou.getToken(),
                }
                if (isWeiXin()) {
                    var openid = xigou.getLocalStorage("openid");
                    if (openid) {
                        params.uniontype = 1;
                        params.unionval = openid;
                    }
                   
                }

                var dp = {
                    requestBody: params,
                    callback: function(response, status) { //回调函数
                        if (status != xigou.dictionary.success) {
                            $.tips({
                                content:'退出失败，详见' + response.msg,
                                stayTime:2000,
                                type:"warn"
                            });
                        }
                        else {
                            if (null == response) {
                                $.tips({
                                    content:xigou.dictionary.chineseTips.server.value_is_null,
                                    stayTime:2000,
                                    type:"warn"
                                });
                            }
                            else {
                                //xigou.activeUser.logout()
                                xigou.removeSessionStorage("userinfo");
                                xigou.removelocalStorage("token");
                                xigou.removeSessionStorage("realname");
                                xigou.removeSessionStorage("realnum");
                                xigou.removeSessionStorage("clearing_ht_idNumber");
                                xigou.removeSessionStorage("clearing_ht_realname");
                                xigou.removelocalStorage("aftersaleslist");
                                xigou.removelocalStorage("dssUser");
                                window.location.href="index.html"
                            }
                        }
                    }
                }
                xigou.activeUser.logout(dp);
            }
        });

	});	
}; 

function modifypwd(){
	$('.modifypwd')[xigou.events.click](function(){
		window.location.href="modifypwd.html"
	})
}