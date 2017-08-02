$(function() {
	$('.div_login').bind("touchstart",function(event){
		$(".div_login > a").addClass("touch").removeClass("touch_change");
	  });
	$('.div_login > a').bind("touchend",function(event){
		var chanelCode=xigou.channelcode;
		if("hhb"==chanelCode.toLowerCase()){
			$(".div_register > a").addClass("touch_change").removeClass("touch");
			window.location.href = "http://m.hhbgroup.cn/newOpenId?parentPhone="+xigou.getSessionStorage('tel');
		}else{
			$(".div_login > a").addClass("touch_change").removeClass("touch").attr("href","login.html");
			/*$(".div_login > a");*/
		}
	  });
	
	$('.div_register').bind("touchstart",function(event){
		$(".div_register > a").addClass("touch").removeClass("touch_change");
	  });
	$('.div_register > a').bind("touchend",function(event){
		var chanelCode=xigou.channelcode;
		if("hhb"==chanelCode.toLowerCase()){
			$(".div_register > a").addClass("touch_change").removeClass("touch");
			window.location.href = "http://m.hhbgroup.cn/newOpenId?parentPhone="+xigou.getSessionStorage('tel');
		}else{
			$(".div_register > a").addClass("touch_change").removeClass("touch").attr("href","register.html");
		}
		/*$(".div_register > a")*/
	  });
	
	var channelCode = xigou.channelcode;
	if (xigou.channelcode) {
		$(".logon_bg > img").attr("src","shop/"+channelCode+"_logon.png");		
	}else{
		$('.logon_bg > img').attr('onerror',"this.src='img/logon/logon.png';");
	}
	/*$(".logon_bg > img").attr("src","shop/"+channelCode+"_logon.png");*/
//	if (isWeiXin()) {
//		var openid = xigou.getLocalStorage("openid");
//		if (!openid) {
//			xigou.getwxOpenid(1, true);
//			return;
//		}
//	}
//	var params = {
//		uniontype: '1',
//		unionval: openid,
//		signature:'1a0708f6e3fdc5ebb25c89a4e0765fdc',
//		curtime: (new Date()).valueOf().toString()
//	};

	var token = xigou.getToken();
	if(!token) return;
	
	var params = {
		token: xigou.getToken()
	};
	
	var dp = {
			requestBody: params,
			callback: function(response, status) { //回调函数
				if (status == xigou.dictionary.success) {
					if (null == response) {
						$.tips({
							content:xigou.dictionary.chineseTips.server.value_is_null,
							stayTime:2000,
							type:"warn"
						});
					} else {
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
				} else {
					$.tips({
						content:'请求失败，详见' + response,
						stayTime:2000,
						type:"warn"
					});
				}
			}
		};
		xigou.activeUser.logon(dp);
});