var userMobile = "",
 	oDom = '<div class="li-div bd_btm">'+
		'<div class="div-normal"  style="border-top:0px">'+
		'<div class="normal-key"><%=key=%></div>'+
		'<span class="normal-val" ><%=value=%></span>'+
		'</div>'+
		'</div>';
$(function() {
	getDssUserInfo();
	
	if (isWeiXin()) {
		$(".div_name").hide();
		$(".top_div1").css({
			"padding-top":"14px"
		})
		$("title").html("账户详情")
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

			wx.onMenuShareAppMessage({
				title: title,
				desc: desc,
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
	
	
	$(".invite_button").click(function(){
		$(".share_friends").show();
	});
	$(".share_friends").click(function(){
		$(this).hide();
	});
	bindClick();
});

function bindClick(){
	$("#idCard .notBound").on("click",function(){
		window.location.href="./dss/checkId.html?type=1";
	})
	$("#bank .notBound").on("click",function(){
		window.location.href="./dss/bindCard.html?type=2";
	})
	$("#alipay .notBound").on("click",function(){
		window.location.href="./dss/checkAlipay.html?type=3";
	})
	$(".noValue#weixin").on("click",function(){
		var wx =$("#weixin .item-r").text();
		window.location.href="./dss/update.html?type=1&weixin="+wx;
	})
	$(".noValue#qq").on("click",function(){
		var qq =$("#qq .item-r").text();
		window.location.href="./dss/update.html?type=2&qq="+qq;
	})
	$(".noValue#email").on("click",function(){
		var email =$("#email .item-r").text();
		window.location.href="./dss/update.html?type=3&email="+email;
	})
	$(".share_button").on("click",function(){
		window.location.href="./dssshareshop.html?i="+userMobile;
	})

}
function getDssUserInfo(){
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				xigou.getDssUserInfo({
					requestBody: {'withQR': true, 'token': xigou.getToken()},
					callback: function(response, status) {
						if (status == xigou.dictionary.success) {
							if(response != null && response != "undefined"){
								if (response.nickname) {
									$(".div_name").append("账户详情");
								}
								else {
									$(".div_name").append("账户详情");
								}
								$(".div_dsslogo").html("<img width='180px' style='margin:15px 10px 10px 10px;' src='data:image/png;base64,"+response.img+"'/>");
								$(".tel").text(response.mobile);
								$(".nickName").text(response.nickname);
								if(response.passTime){
									$("#passTime").show();
									$("#passTime .sep-div span").text(response.passTime);
								}
								if(response.weixin){
									$("#weixin div.item-r").text(response.weixin);
								}
								if(response.qq){
									$("#qq div.item-r").text(response.qq);
								}
								if(response.email){
									$("#email div.item-r").text(response.email);
								}
								if(response.credentialCode){
									$("#idCard").text("");
									$(oDom.replace("<%=key=%>","姓名").replace("<%=value=%>",response.name)).appendTo('#idCard');
									$(oDom.replace("<%=key=%>","证件类型").replace("<%=value=%>",response.credentialType)).appendTo('#idCard');
									$(oDom.replace("<%=key=%>","证件号码").replace("<%=value=%>",response.credentialCode)).appendTo('#idCard');

								}
								if(response.bankName){
									$("#bank").text("");
									$(oDom.replace("<%=key=%>","开户银行").replace("<%=value=%>",response.bankName)).appendTo('#bank');
									$(oDom.replace("<%=key=%>","银行卡号码").replace("<%=value=%>",response.bankAccount)).appendTo('#bank');
								}
								if(response.alipay){
									var cDom = oDom.replace("<%=key=%>","支付宝账号");
									cDom = cDom.replace("<%=value=%>",response.alipay);
									$("#alipay").text("");
									$(cDom).appendTo('#alipay');
								}
								userMobile = response.mobile;
								if(!isWeiXin()){
									$(".nowx_tip").html("你也可以手动复制链接：<br/>http://m.51seaco.com/dssinvite.html?i=" + userMobile);
								}
							}
							else {
								window.location.href="home.html";
							}
						}
					}
				});
			}else{
				window.location.href = "logon.html";
			}
		}
	});
	
}

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
