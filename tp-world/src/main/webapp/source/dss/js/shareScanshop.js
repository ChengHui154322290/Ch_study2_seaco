var userMobile = xigou.getQueryString("i");
$(function() {
 	shareShop();	
 	wxInit();
});
/**
 * 店铺分享
 * @returns
 */
function  shareShop(){
	xigou.shareScanPromoter({
		requestBody: {'withQR': true,'userMobile':userMobile},
		callback:function(response,status){
			if (status == xigou.dictionary.success) {
				if(response != null && response != "undefined"){
//					$(".div_share_image").html("<img style='margin:15px 0 10px;' src='data:image/png;base64,"+response.img+"'/>");
					$(".div_share_image").html("<img style='margin:0;' src='"+response.img+"'/>");
				}
				else {
					window.location.href="home.html";
				}
			}
		}
	});
	
}

function wxInit(){	
	
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

			var lineLink = window.location.href;
			var title = 'APP首单立减5元  扫码推广享100%返佣';
			var desc = '新用户通过扫描您的个人推广二维码注册享2000元大礼包且成功购买商品后，您将获得100%的佣金返现。每日限时秒杀、低价折扣享不停！';
			var imgUrl = $(".div_share_image>img").attr("src");
			wx.onMenuShareAppMessage({
				title: title,
				desc: desc,
				link: lineLink,
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
				link: lineLink,
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
				link: lineLink,
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
				link: lineLink,
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
				link: lineLink,
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
		})
	}
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


