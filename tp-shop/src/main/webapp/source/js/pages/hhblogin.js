/**
 * 
 */
var openId = xigou.getQueryString("openId");
$(function() {
	dologin(openId);
});

function dologin(openId){
	xigou.hhblogin.dologin({
		params: {
			'openId':openId
		},
		callback: function(response, status) {
//			if(response.code==0){
////				xigou.setSessionStorage("token", response.data.token, true);
//				var back = {
//						'token': response.data.token,
//						'telephone': response.data.tel,
//						'name': response.data.name
//					};
//				xigou.setSessionStorage("userinfo", back, true);
//				
//				xigou.setLocalStorage("token", response.data.token);
//				xigou.setLocalStorage("mobile", response.data.tel);
//				xigou.setLocalStorage("channelCode", response.data.token);
//				xigou.setLocalStorage("show_name", response.data.name || response.data.tel);
//				window.location.href="index.html";
//			}else{
//				$("#errMsg").text(response.msg);
//			}
			var stauts = response.code;
			switch (stauts) {
				case "0":
					var loginjump_url = xigou.getSessionStorage("loginjump_url");

					var back = {
						'token': response.data.token,
						'telephone': response.data.tel,
						'name': response.data.name
					};
					//实名认证信息清除
					xigou.removeSessionStorage('realname');
					xigou.removeSessionStorage('realnum');
					xigou.setSessionStorage("userinfo", back, true);
					if($("input.name").val())
						xigou.setLocalStorage("login_name", $("input.name").val());
					xigou.setLocalStorage("show_name", response.data.tel || response.data.name);
					xigou.setLocalStorage("token", response.data.token);

					//提交数据的单独处理(添加购物车）
					if(xigou.getSessionStorage("refer_data")){
						params = xigou.getSessionStorage("refer_data",true);
						params.token = response.data.token;

						xigou.activeUser.addshopping({
							requestBody: params,
							callback: function(response, status) { //回调函数
								xigou.removeSessionStorage("refer_data");
								if (status == xigou.dictionary.success) {
								} else {
									$.tips({
										content:'添加购物车失败！',
										stayTime:2000,
										type:"warn"
									});
								}
								window.location.href = "cart.html";
							}
						});
						//立即购买
					}else if(xigou.getSessionStorage("buy_now_refer_data")){
						params = xigou.getSessionStorage("buy_now_refer_data",true);
						params.token = response.data.token;
						xigou.activeUser.buynow({
							requestBody: params,
							callback: function(response, status) { //回调函数
								var tuntype = xigou.getSessionStorage("buy_now_tun_type");
								xigou.removeSessionStorage("buy_now_refer_data");
								xigou.removeSessionStorage("buy_now_tun_type");
								if (status == xigou.dictionary.success) {
									var json = response || null;
									if (null == json || json.length == 0) return false;
										xigou.setSessionStorage("buy_now_uuid", json.data.uuid);
										window.location.href="settle.html";
								} else {
									//xigou.alert('请求失败，详见' + response);
									window.location.href=xigou.getSessionStorage("buy_now_details_url");
								}
							}
						});
					}
					else if(loginjump_url){
						xigou.setSessionStorage("loginjump_url","");
						window.location.href = decodeURIComponent(loginjump_url);
					}
					else {
						if(xigou.getSessionStorage("refer",false).indexOf('login.html')>-1 || xigou.getSessionStorage("refer",false).indexOf('logon.html')>-1){
							window.location.href = "home.html";
						}else{
							if(xigou.getSessionStorage("refer",false).indexOf("/wxhb")>-1||xigou.getSessionStorage("refer",false).indexOf("/wxhb.html")>-1){
								window.location.href = "home.html";
								return;
							}
							window.location.href = "index.html";
						}
					}

					break;
				case "-99":
					var back = {
						'token': response.data.token,
						'telephone': response.data.tel,
						'name': response.data.name
					};
					xigou.setSessionStorage("userinfo", back, true);
					if($("input.name").val())
						xigou.setLocalStorage("login_name", $("input.name").val());
					xigou.setLocalStorage("token", response.token);
					window.location.href = "binding.html";
					break;
				default:
					$('input#name').unbind("change");

					$.tips({
				        content:response.msg||"登录失败",
				        stayTime:2000,
				        type:"warn"
				    });

					// xigou.toast(response.msg||"登录失败");
					// if(GeeTest){
					// 	GeeTest[0].refresh();
					// }

					break;
			}
		}
	});
}
