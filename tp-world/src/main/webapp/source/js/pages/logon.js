$(function() {
	// if (isWeiXin()) {
	// 	var openid = xigou.getLocalStorage("openid");
	// 	if (!openid) {
	// 		xigou.getwxOpenid(1, true);
	// 		return;
	// 	}
	// }
	/*var params = {
		uniontype: '1',
		unionval: openid,
//      signature:'1a0708f6e3fdc5ebb25c89a4e0765fdc', //wx1.0.1
        signature:'af288db869b27f21cc1440385d63d4d5', //wx1.5.0
		curtime: (new Date()).valueOf().toString()
	};*/

	var dssUser = xigou.getLocalStorage("dssUser");
	if (dssUser && dssUser != "{}") {
		dssUser = JSON.parse(dssUser);
		if (dssUser && dssUser.mobile) {
			params.shopMobile = dssUser.mobile;
		}
	}
	
	var token = xigou.getToken();
	if(!token) return;
	
	var params = {
		token: xigou.getToken()
	};
	
	xigou.activeUser.unionlogon({
		requestBody: params,
		callback: function(response, status) { // 回调函数
			if (status == xigou.dictionary.success) {
				if (null == response) {
					return;
				} else {
					var stauts = response.code;
					switch (stauts) {
						case "0":
							var backurl = "";
							if (response.data.promoterinfo && response.data.promoterinfo != "{}") {
								var promoterinfo = 	JSON.parse(response.data.promoterinfo);
								promoterinfo.token = response.data.token;
								xigou.setLocalStorage("dssUser", promoterinfo, true);
							}

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
										backurl = "cart.html";
										//setTimeout(function(){
										//	window.location.href = "cart.html";
										//}, 250);
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
											//setTimeout(function(){
											//	window.location.href="settle.html";
											//}, 250);
											backurl = "settle.html";
										} else {
											//xigou.alert('请求失败，详见' + response);
											//setTimeout(function(){
											//	window.location.href=xigou.getSessionStorage("buy_now_details_url");
											//}, 250);
											backurl = xigou.getSessionStorage("buy_now_details_url");
										}
									}
								});
							}
							else if(loginjump_url){
								xigou.setSessionStorage("loginjump_url","");
								//setTimeout(function(){
								//	window.location.href = decodeURIComponent(loginjump_url);
								//}, 250);
								backurl = decodeURIComponent(loginjump_url);
							}
							else {
								if(xigou.getSessionStorage("refer",false).indexOf('login.html')>-1 || xigou.getSessionStorage("refer",false).indexOf('logon.html')>-1){
									//setTimeout(function(){
									//	window.location.href = "home.html";
									//}, 250);
									backurl = "home.html";
								}else{
									if(xigou.getSessionStorage("refer",false).indexOf("/wxhb")>-1||xigou.getSessionStorage("refer",false).indexOf("/wxhb.html")>-1){
										//setTimeout(function(){
										//	window.location.href = "home.html";
										//}, 250);
										backurl = "home.html";
									}
									else {
										//setTimeout(function(){
										//	window.location.href = "index.html";
										//}, 250);
										backurl = "index.html";
									}
								}
							}

							if (response.data.tel) {
								setTimeout(function(){
									backurl = "index.html";
								}, 250);
							}
							else {
								xigou.setSessionStorage('loginjump_url', backurl);
								window.location.href = 'forcebindtel.html';
							}

							break;
						default:
							return;
					}
				}
			}
		}
	});
});