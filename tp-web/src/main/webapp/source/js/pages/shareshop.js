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
	xigou.shareShop({
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
			var title = '闪购全球好货  零时差购遍世界';
			var desc = '西客商城是西客商城旗下的线上跨境电商购物平台，涵盖母婴、美妆个护、进口食品等，自建保税仓，通过保税模式实现快速发货，为消费者提供最优购物体验！';
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

