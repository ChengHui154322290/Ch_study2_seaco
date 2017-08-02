var userMobile = "",
 	oDom = '<div class="li-div bd_btm">'+
		'<div class="div-normal"  style="border-top:0px">'+
		'<div class="normal-key"><%=key=%></div>'+
		'<span class="normal-val" ><%=value=%></span>'+
		'</div>'+
		'</div>';
var shopMobileVal;
var dssUserInfo = xigou.getSessionStorage("dssUser");
if(dssUserInfo && dssUserInfo != "{}"){
	var userInfo =JSON.parse(dssUserInfo);
	shopMobileVal = userInfo ?(userInfo.mobile && userInfo.token ?userInfo.mobile :null):null;
}

$(function () {
	 if (isWeiXin()) {
	 	$(".div_name").hide()
	 }

	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status == xigou.dictionary.success){
				dssLogin();
			}else{
				xigou.setSessionStorage("loginjump_url", "dss/dsshome.html");
				window.location.href = "../logon.html";
				return;
			}
		}
	});
	xigou.removeSessionStorage('withdrawalsParam');
	
	/*  弹出提示框    
	$(".withdraw-div").click(function(){
		$(".cover").show();
		$(".btn-app").show();
		$(".cover_pop").show();
		$("body").attr("scroll","no");
		$("body").css("overflow-y","hidden");
	})
	$(".cover").click(function(){
		$(".cover").hide();
		$(".btn-app").hide();
		$(".cover_pop").hide();
		$("body").css("overflow-y","scroll");
	})*/
	
});

function dssLogin() {
	var token = xigou.getToken();
	var params = {
		'token': token
	};
	xigou.promoterFunc.dssLogin({
		requestBody: params,
		callback: function(response, status) {
			if (status == xigou.dictionary.success && response && response.data && response.data.isshopdss) {	// 是分销员
				loadDssAccount();
			}
			else {	// 跳转到分销员注册页面
				window.location.href = "../index.html";
			}
		}
	})
}

function loadDssAccount() {
	var token = xigou.getToken();
	var params = {
			'token': token,
			'type': '1'
		};
		xigou.promoterFunc.loadDssAccount({
			requestBody: params,
			callback: function(response, status) { //回调函数
				if (status == xigou.dictionary.success) {
					var json = response || null;
					if (null == json || json.length == 0)return false;
					if (json.code == 0) {
						var dssregister = xigou.getSessionStorage('dssregister');
						if (dssregister) {
							$.tips({
								content:dssregister,
							        stayTime:2000,
							        type:"warn"
							     });
							xigou.removeSessionStorage('dssregister');
						}
						InitDssAccountPage(json);
					}
				}
			}
		});	
};



function InitDssAccountPage(json) {
	var l_name = xigou.getLocalStorage("show_name") || xigou.getLocalStorage("login_name") || "福利惠会员";
	var help_flag= xigou.getLocalStorage("help_flag");
	var dp_flag =  xigou.getLocalStorage("dp_flag");
	bindHClick();
	if (l_name) {
		var oScript= document.createElement("script");
		oScript.type = "text/javascript";
		oScript.src='https://qiyukf.com/script/6e39dddabff63d902f55df3961c2793d.js?name=' + l_name + '&mobile=' + l_name;
		$('body')[0].appendChild(oScript);
	};
	var status = json.data.status;
	var withdrawStatus = json.data.withdrawstatus;
	var surplusamount = json.data.surplusamount;
	if(null == status || status > 3){ //数据异常
		return;
	}
	//未开通第一次进入以后显示遮罩层
	if(status == 0 && !help_flag){
		xigou.setLocalStorage("help_flag","true");
		$("#zf").show();
	}
	//开通以后第一次进入显示遮罩层
	if((status == 1|| status ==2) && !dp_flag){
		xigou.setLocalStorage("dp_flag","true");
		$("#wd").show();
	}

	//状态栏和提现按钮显示状态
	statusDivShow(status);
	withdrawButtionShow(status,surplusamount);
	if(2 == status){ //已开通
		if(null != withdrawStatus && 1 == withdrawStatus){	 //提现中
			$("#canWithdrawBtn").hide();			
			$("#withdrawingBtn").show();
			$("#withdrawDisableBtn").hide();
			//$("#withdrawingAmount").text(json.data.withdrawingamount);
		}		
	}
	
	$("#shopName").text(json.data.nickname + "的店铺");
	var tTitle = json.data.nickname?(json.data.nickname+"的店铺"):"全球购微店";
	$("title").text(tTitle);
	//$("#userName").text(json.data.name);
	$("#mobile").text(json.data.mobile);
	$("#fuserName").text(json.data.name);
	$("#fmobile").text(json.data.mobile);
	
	//总收入
	$("#accumulatedAmount").text(json.data.totalfees);
	//已提现
	$("#withdrawedAmount").text(json.data.withdrawedfees);	
	//未提现
	$("#surplusAmount").text(json.data.surplusamount);
	//新用户返现
	$("#referralFees").text(json.data.referralfees);
	//订单返现
	$("#orderAmount").text(json.data.orderamount);
	//客户总数
	$("#totalCustomers").text(json.data.totalCustomers);
	
	if(status == 0){
		$("#orders_btn_pay").attr("ordercode", json.data.paycode);
	}

	//账户详情
	accountDetail();
	//账单详情
	billinfo();
	//订单详情
	orderinfo();
	//分销查询
	dealers();
	//店铺查询
	shopquery();
	
	withdraw()
	
	//二维码
	xigou.setSessionStorage('dssstatus', json.data.status);
	xigou.getDssUserInfo({
		requestBody: {'withQR': true, 'token': xigou.getToken()},
		callback: function(response, status) {
			if (status == xigou.dictionary.success) {
				if(response != null && response != "undefined"){
					if (response.nickname) {
						$(".div_name").append(json.data.nickname + "的店铺");
					}
					else {
						$(".div_name").append(json.data.nickname + "的店铺");
					}
					$(".div_dsslogo").html("<img width='140px' style='margin:15px 10px 10px 10px;' src='data:image/png;base64,"+response.img+"'/>");
					userMobile = response.mobile;
					if(!isWeiXin()){
						$(".nowx_tip").html("你也可以手动复制链接：<br/>http://m.51seaco.com/dssinvite.html?i=" + shopMobileVal);
					}
				}
				else {
					window.location.href="home.html";
				}
			}
		}
	});
};
function bindHClick(){
	$("#zf .zf_pop").click(function(){
		$("#zf").hide();
	})
	//$("#xq .xq_pop").click(function(){
	//	$("#xq").hide();
	//	$("#sm").show();
	//})
	//$("#sm .sm_pop").click(function(){
	//	$("#sm").hide();
	//})
	$("#wd .dp_pop").click(function(){
		$("#wd").hide();
	})
}
function accountDetail(){

	$('.main-info').click(function(e){
		if($(e.target).hasClass('pass_btn')) {
			return;
		}
		window.location.href='../dssaboutus.html';
		//window.location.href='../index.html?from=dsshome';
	});
}

function billinfo(){
	$("#accountFlowQuery")[xigou.events.click](function(){
		window.location.href='billinfo.html?type=1';
	});
}

function orderinfo(){
	$("#orderQuery")[xigou.events.click](function(){
		window.location.href='dssorders.html?type=1';
	});
}

function dealers(){
	$("#partnerQuery")[xigou.events.click](function(){
		window.location.href='dealers.html?type=1';
	});
}

function shopquery(){
	$("#shopQuery")[xigou.events.click](function(){
		window.location.href='hotcommodity.html';
	});
}



function statusDivShow(status){
	if(0 == status){
		$("#normalStatusDiv").hide();
		$("#unpayStatusDiv").show();
		$("#forbiddenStatusDiv").hide();
	}else if(1 == status){
		$("#normalStatusDiv").show();
		$("#unpayStatusDiv").hide();
		$("#forbiddenStatusDiv").hide();
		$("#normalStatusDiv .wrz").show();
		Prompt();
		
	}else if(2 == status){
		$("#normalStatusDiv").show();
		$("#unpayStatusDiv").hide();
		$("#forbiddenStatusDiv").hide();
		$("#normalStatusDiv .yrz").show();
		Prompt();
		
	}else{
		$("#normalStatusDiv").hide();
		$("#unpayStatusDiv").hide();
		$("#forbiddenStatusDiv").show();
	}
}

/*  弹出提示框    */
function Prompt(){
	$(".withdraw-div").click(function(){
		$(".cover").show();
		$(".btn-app").show();
		$(".cover_pop").show();
		$("body").attr("scroll","no");
		/*$("body").css("overflow-y","hidden");*/
	})
	$(".cover").click(function(){
		$(".cover").hide();
		$(".btn-app").hide();
		$(".cover_pop").hide();
		$("body").css("overflow-y","scroll");
	})
}

function withdrawButtionShow(status,surplusamount){
	if(0 == status){	//未开通
		$("#withdrawingBtn").hide();
		$("#withdrawDisableBtn").show();
		$("#canWithdrawBtn").hide();
	}else if((2 == status || 1 == status) && surplusamount > 0){ //已开通已认证，已开通未认证
		$("#withdrawDisableBtn").hide();
		$("#canWithdrawBtn").show();
		$("#withdrawingBtn").hide();
	}else{				//被禁用
		$("#withdrawingBtn").hide();
		$("#withdrawDisableBtn").show();
		$("#canWithdrawBtn").hide();
	}
}

$(function() {
	if (isWeiXin()) {
		InitWeixin();
		wx.ready(function () {
			// 1 判断当前版本是否支持指定 JS 接口，支持批量判断
			wx.checkJsApi({
				jsApiList: [
					'getNetworkType',
					'previewImage'
				],
				success: function (res) {
				}
			});

			var lineLink = "http://"+window.location.host+"/dssinvite.html?i=";
			var title = '快来加入西客屋轻创业平台吧！';
			var desc = '西客屋，指尖上的跨境微店！0囤货 0风险 0压力，汇聚全球精品，全程溯源，内测期间超低门槛入驻！西客屋，为圆梦而生，动动手指即可拥有自己的跨境微店。';
			var imgUrl = "http://"+window.location.host+"/dss/img/share_icon.png";
			alert(lineLink + shopMobileVal);
			wx.onMenuShareAppMessage({
				title: title,
				desc: desc,
				link: lineLink + shopMobileVal,
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
				link: lineLink + userMobile,
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
				link: lineLink + userMobile,
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
				link: lineLink + userMobile,
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
				link: lineLink + userMobile,
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

			var imgCodeURL = $('div.div_dsslogo img')[0].src;
			if (imgCodeURL){
				var imgList = [imgCodeURL];
			}
			$('div.div_dsslogo')[xigou.events.click](function(){
				wx.previewImage({
					current: imgList[0],
					urls:  imgList
				});
			});
		})
	}
	
	
	
});

function InitWeixin(){
	var pa = [];
	var url = location.href.split('#')[0].replace(/&+/g, "%26");
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

$(".invite_button").click(function(){
	$(".share_friends").show();
});
$(".share_friends").click(function(){
	$(this).hide();
});
$(".share_button").on("click",function(){
	window.location.href="../dssshareshop.html?i="+userMobile;
})