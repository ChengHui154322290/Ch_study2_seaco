var dssUserMobile =null;
function getDssUserInfo(){
	var dssUser = xigou.getSessionStorage("dssUser");
	var shop = xigou.getQueryString("shop");
	if (dssUser && dssUser != "{}") {
		dssUser = JSON.parse(dssUser);
		if (dssUser.token && dssUser.token == xigou.getToken() && (dssUser.mobile || dssUser.shopmobile)) {
			// 当前用户就是分销员
			setShopName(dssUser);
			dssUserMobile = dssUser.mobile || dssUser.shopmobile;
			setShopName(dssUser);
			return;
		}
		else if (!shop) {
			// 没带参数,读取缓存
			setShopName(dssUser);
			dssUserMobile = dssUser.mobile || dssUser.shopmobile;
			return;
		}
	}

	// 重新获取dssUser
	xigou.getDssUserInfo({
		requestBody: {
			'shop': xigou.getQueryString("shop"),
			'token':xigou.getToken(),
			'priority':0
		},
		callback: function(response, status) {
			if (status == xigou.dictionary.success) {
				if (response != null && response.mobile) {
					dssUserMobile = response.mobile;
					if (parseInt(response.source) == 0) {
						dssUserMobile.token = xigou.getToken();
					}
					else {
						dssUserMobile.shop = shop;
					}
					if(xigou.getSessionStorage("dssUser", true) == null || xigou.getSessionStorage("dssUser", true) == "" || xigou.getSessionStorage("dssUser", true) == "{}"  || xigou.getSessionStorage("dssUser", response, true).mobile != response.mobile){
						xigou.setSessionStorage("dssUser", response, true);
					}

					setShopName(response);
				}
			}
		}
	});
}

function setShopName(dssUser) {
	var dssName = "";
	if (dssUser.nickname || dssUser.shopnickname) {
		dssName = dssUser.nickname || dssUser.shopnickname;
	}
	else {
		dssName = dssUser.name;
	}
	if (dssName) {
		$('.home-name-val').html(dssName + '的西客屋');
	}
	else {
		$(".home-name-val").html("西客商城");
	}


}

function dssIndex(){
	$("#slider").hide();
	$(".ui-header").hide();
	$(".div_top_icon").hide();
	$(".div_top_icon1").show();
	$(".ui-header-a").show();
	$(".content").css("margin-top","0px")
	//分销员登录可以跳转分销主页
	$("#backDsshome").attr("href","dss/dsshome.html");
	if(fromDsshome == "dsshome"){
		$(".home-name a").addClass("act");
	}
}
