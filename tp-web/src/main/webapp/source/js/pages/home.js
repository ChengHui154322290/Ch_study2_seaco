$(function () {
	var success=xigou.getSessionStorage("success");
	var tipsLeft = $(window).width()/2 - 59;
	$(".tips").css({
		"left":tipsLeft + "px"
	})
	if(undefined!=success && null!=success && ""!=success){
		$(".tips").show();
		xigou.removeSessionStorage("success");
	}

	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status != xigou.dictionary.success){
				xigou.setSessionStorage("loginjump_url", "home.html");
				window.location.href = "logon.html";
			}else{
				// 判定是否需要重新拉取用户微信信息
				if (isWeiXin()){
					if (!xigou.getLocalStorage('wxnickname') && !xigou.getLocalStorage('wxImage') && !xigou.getSessionStorage('getediconnickname')) {
						xigou.getwxOpenid(1, true);
					}
				}
				initUserInfo();
				if(isWeiXin()){
					$("#bindTel").show();
				}
			}
		}
	});
	if(!isWeiXin()){
		window.onscroll = function() {
			var top = document.documentElement.scrollTop || document.body.scrollTop;
			if(top>10){
				$(".home-name").addClass("fixed")
			}else if(top<30){
				$(".home-name").removeClass("fixed")
			}
		}
	}


});

function dssLogin(){
	var token = xigou.getToken();
	var params = {
		'token': token
	};
	xigou.promoterFunc.dssLogin({
		requestBody: params,
		callback: function(response, status) {
			if (status == xigou.dictionary.success) {
				var homeTopHeight = $(".home-top").height();
				if (response != null && response.code == 0) {
					var json = response;
					var isShop = (null != json.data.isshopdss && 1 == json.data.isshopdss);
					var isCoupon = (null != json.data.iscoupondss && 1 == json.data.iscoupondss);
					var isScanCode = (null != json.data.isscandss && 1 == json.data.isscandss);
					var isTopScandss = (null != json.data.isTopScandss && 1 == json.data.isTopScandss);
					var htmlData = "",N = 0;
					if(isTopScandss){
						htmlData += '<div class="myTopScandss"><i></i><div>推广统计</div></div>';
						N++;
						isScanCode = 0;
					};
					if(isShop){
						htmlData  += '<div class="myDss"><i></i><div>我的西客屋</div></div>';
						N++;
					};
					if(isCoupon){
						htmlData += '<div class="myCard"><i></i><div>我的卡券</div></div>';
						N++;
					};
					if(isScanCode){
						htmlData += '<div class="myScanCode"><i></i><div>我的推广码</div></div>';
						N++;
					};

					$(".div_dss_info ").append(htmlData);
					if(N>1){
						$(".div_dss_info").removeClass("show_arr");
						$(".div_dss_info > div > div").eq(0).addClass("bdr");
						if(N>2){
							$(".div_dss_info > div >div").eq(1).addClass("bdr");
							if(N>3){
								$(".div_dss_info > div >div").eq(2).addClass("bdr");
							};
						};
					};
					$('.myDss')[xigou.events.click](function(){
						setTimeout(function(){
							window.location.href = 'dss/dsshome.html';
						}, 250);
					});
					$('.myCard')[xigou.events.click](function(){
						setTimeout(function(){
							window.location.href = 'dss/dsscouponhome.html';
						}, 250);
					});
					$('.myScanCode')[xigou.events.click](function(){
						setTimeout(function(){
							window.location.href = 'dss/dssScancodeHome.html';
						}, 250);
					});
					$('.myTopScandss')[xigou.events.click](function(){
						setTimeout(function(){
							window.location.href = 'dss/promotion_statistics.html';
						}, 250);
					});
				}else{
					//$('.home-bg').css('margin-top', homeTopHeight);
				};
			};
			bindState();
		}
	});
};

function initUserInfo(){
	xigou.activeUser.initCount({
		requestBody:{
			token: xigou.getToken()
		},
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				if (null == response) {
					$.tips({
				        content:xigou.dictionary.chineseTips.server.value_is_null,
				        stayTime:2000,
				        type:"warn"
				    })
				}else if("0" ==response.code){
					//待付款的数量
					if(response.data.unpaycount > 0)
					{
						$(".home-unpay").html(response.data.unpaycount);
						$(".home-unpay").show();
					}else {
						$(".home-unpay").hide();
					};
					//待收货的数量
					if(response.data.unreceiptcount > 0){
						$(".home-unrecv").html(response.data.unreceiptcount);
						$(".home-unrecv").show();
					}else {
						$(".home-unrecv").hide();
					};
					//待使用的数量
					if(response.data.unusecount > 0){
						$(".home-unuse").html(response.data.unusecount);
						$(".home-unuse").show();
					}else{
						$(".home-unuse").hide();
					};

					if (response.data.couponcount > 0) {
						$('.coupon_count').html(response.data.couponcount + '张');
					};

					var name = xigou.getLocalStorage("show_name")|| xigou.getLocalStorage("login_name") || "西客会员";
					var nickName = xigou.getLocalStorage('wxnickname') || xigou.getLocalStorage("show_name")|| xigou.getLocalStorage("login_name") || "西客会员";
					if (name) {
						var oScript= document.createElement("script");
						oScript.type = "text/javascript";
						oScript.src='https://qiyukf.com/script/6e39dddabff63d902f55df3961c2793d.js?name=' + name + '&mobile=' + name;
						$('body')[0].appendChild(oScript);
					};
					if (nickName) {
						$(".home-name")[0].innerHTML = nickName;
					};
					var imgIcon = xigou.getLocalStorage('wxImage');
					if (imgIcon) {
						$('div.div_icon_bg img').attr('onerror',"this.onerror=null;this.src='img/home/icon.png';xigou.removelocalStorage('wxImage');");
						$('div.div_icon_bg img').attr('src', imgIcon);
					};
					dssLogin();
				}else if("-100" ==response.code){
					window.location.href = "logon.html";
				};
			};
		}
	});
};


function bindState(){
	var params = {
		'token': xigou.getToken()
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
					if(stauts == 0) {
						$("#bindTel a").attr("href","changeTel.html?mobile="+response.data.tel);
					}
				};
			} else {
				$.tips({
					content:'请求失败，详见' + response,
					stayTime:2000,
					type:"warn"
				});
			};
		}
	};
	xigou.activeUser.logon(dp);
};
//function clearClass(){
//	$("#li_dss_home").removeClass("split-bottom");
//	$("#li_dss_home").removeClass("split-top");
//	$("#li_dss_couponhome").removeClass("split-bottom");
//	$("#li_dss_couponhome").removeClass("split-top");
//};

