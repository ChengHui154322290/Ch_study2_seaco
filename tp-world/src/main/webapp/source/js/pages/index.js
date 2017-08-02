var refreshComp;//刷新组件
var myScroll;
var listCount = 1;
var firstssale = true;
var imgUrl = '';
var dssUserMobile = null;
var dssName = null;
var fromDsshome=xigou.getQueryString("from");
var showFlag = "";
var showLoading = true;
var groupBuyPage = 1;
var bMaxPage = false;
var maxLabsElementCnt = 5;
$(function() {
	if (isWeiXin()){
		$(".ui-header").hide();
		$(".header_wx").show();
		$(".content").css({
			"margin-top":"0px"
		})
		// var openid = xigou.getLocalStorage("openid");
		// if (!openid) {
		// 	xigou.getwxOpenid(1, true);
		// 	return;
		// }
		showLoading = false;
	}
	getDssUserInfo();
	



	//touch.on('body', 'swiping', function(ev){
	//	var top = document.documentElement.scrollTop || document.body.scrollTop;
	//	if(top>124){
	//		$(".home-bg").addClass("fix")
	//	}else if(top<124){
	//		$(".home-bg").removeClass("fix")
	//	}
	//});
	
	window.onscroll = function() {
	    var top = document.documentElement.scrollTop || document.body.scrollTop;
	    if(top>30){
	    	$(".home-name").addClass("fixed")
	    }else if(top<30){
	    	$(".home-name").removeClass("fixed")
	    }
	    var height = 2*$(window).height();
	    if(top>height)
        {
            $(".top").show();
        }else{
        	 $(".top").hide();
         }
	}
	
	
});


function setShopName(dssUser) {
	if (dssUser.mobile || dssUser.shopmobile) {
		var dssName = "";
		if (dssUser.nickname || dssUser.shopnickname) {
			dssName = dssUser.nickname || dssUser.shopnickname;
		}
		else {
			dssName = dssUser.name;
		}
		if(isWeiXin()){
			if (dssName) {
				$('.home-name-val').html(dssName + '的西客屋');
				$("title").html(dssName + '的西客屋');
			}
			else {
				$(".home-name-val").html("西客商城");
			}
		}else{
			if (dssName) {
				$('.home-name-val').html(dssName + '的西客屋');
			}
			else {
				$(".home-name-val").html("西客商城");
			}
		}
		
		//分销用户更换首页头部样式
		/*dssIndex();*/
	}
}

function dssShareShop(dssName){
	if(isWeiXin()){
		if (dssName) {
			$('.home-name-val').html(dssName + '的西客屋');
			$("title").html(dssName + '的西客屋');
		}
		else {
			$(".home-name-val").html("西客商城");
		}
	}else{
		if (dssName) {
			$('.home-name-val').html(dssName + '的西客屋');
		}
		else {
			$(".home-name-val").html("西客商城");
		}
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

function getDssUserInfo(){
	var dssUser = xigou.getLocalStorage("dssUser");
	var shop = xigou.getQueryString("shop");
	var shareShop = xigou.getSessionStorage("shareShop");
	var DssName = xigou.getSessionStorage("DssName")
	if(shareShop && shareShop != "null" && shareShop != "undefined"){
		// 当前读取别人分享的店铺
		/*var DssName = xigou.getQueryString("dssName");
		shareShop=decodeURI(DssName);*/
		dssUserMobile = shareShop;
		dssName = DssName;
		dssShareShop(dssName);
		wxunionlogin(shareShop);
		return;
	}else{
		var shareShop = xigou.getQueryString("shop");  
		var DssName =  xigou.getQueryString("dssName");
		if(DssName && DssName != "null"){
			var DssName = DssName.split('&')[0]
		}
		xigou.setSessionStorage("shareShop",shareShop);
		xigou.setSessionStorage("DssName",DssName);
		if(shareShop && shareShop != "undefined"){
			// 当前读取别人分享的店铺
			dssUserMobile = shareShop;
			dssName = DssName;
			dssShareShop(dssName);
			wxunionlogin(shareShop);
			return;
		}else{
			if (dssUser && dssUser != "{}") {
				dssUser = JSON.parse(dssUser);
				if (dssUser.token && dssUser.token == xigou.getToken() && (dssUser.mobile || dssUser.shopmobile)) {
					// 当前用户就是分销员
					setShopName(dssUser);
					dssUserMobile = dssUser.mobile || dssUser.shopmobile;
					dssName = dssUser.nickname || dssUser.shopnickname || name;
					wxunionlogin(dssUserMobile);
					return;
				}
				else if (!shop) {
					// 没带参数,读取缓存
					setShopName(dssUser);
					dssUserMobile = dssUser.mobile || dssUser.shopmobile;
					dssName = dssUser.nickname || dssUser.shopnickname || name;
					wxunionlogin(dssUserMobile);
					return;
				}
			}
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
					if(xigou.getLocalStorage("dssUser", true) == null || xigou.getLocalStorage("dssUser", true) == "" || xigou.getLocalStorage("dssUser", true) == "{}"  || xigou.getLocalStorage("dssUser", response, true).mobile != response.mobile){
						xigou.setLocalStorage("dssUser", response, true);
					}

					setShopName(response);
					wxunionlogin(dssUserMobile);
				}
				else {
					wxunionlogin();
				}
			}
			else {
				wxunionlogin();
			}
		}
	});
}
//广告位信息
function moduleInfo() {
	xigou.activeIndex.module({
		showLoading : showLoading,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length == 0) return false;

				var code = json.code;
				if (code == 0) {
					if (!response.data) {
						return;
					}
					adInfo(response.data.banners);
					labsInfo(response.data.labs);
				}
			}
		}
	});
	function adInfo(data) {
		if (!data || data.length == 0) {
			return;
		}

		var html = [];
		html.push('<div class="ui-slider">');
		html.push('<ul class="ui-slider-content">');
		for (var i = 0; i < data.length; i++) {
			var url = gethref(data[i]).split("com")[1];
			html.push('<li>');
			if(url && url != "undefined"){
				html.push('	<a href="' + url + '">');
			}else{
				html.push('	<a href="' + gethref(data[i]) + '">');
			}
			
			html.push('	<img src="' + data[i].imageurl + '"/>');
			html.push('	</a>');
			html.push('</li>');

			if (i == 0) {
				imgUrl = data[i].imageurl;
			}
		}
		html.push('</ul>');
		html.push('</div>');
		$('#slider').empty().html(html.join(''));
		var slider = new fz.Scroll('.ui-slider', {
			role: 'slider',
			indicator: true,
			autoplay: true,
			interval: 3000
		})
	}

	function labsInfo(data) {
		var htmlData = [];
		var html = [];
		for(var i = 0; i < data.length; i++){		
			if(i > 0 && i % maxLabsElementCnt == 0){
				html.push('<div class="div_top_icon">' + htmlData.join('') + "</div>");
				htmlData = [];
			}
			var linmUrl = data[i].linkurl.split("com")[1];
			if(linmUrl && linmUrl != "undefined"){
				htmlData.push('<div><a class="error" href="' + linmUrl + '"><img src="' + data[i].imageurl + '">' + data[i].title + '</a></div>');
			}else{
				htmlData.push('<div><a class="error" href="' + data[i].linkurl + '"><img src="' + data[i].imageurl + '">' + data[i].title + '</a></div>');
			}
			
		}
		html.push('<div class="div_top_icon">' + htmlData.join('') + "</div>");
		$('.div_top_lab').html(html.join(''));
		$('.div_top_lab').show();
		
		
		//遍历item的方式不能换行
		/*$.each(data, function(index, item){
			var url = item.linkurl||gethref(item);
			htmlData.push('<div><a href="' + url + '"><img src="' + item.imageurl + '">' + item.title + '</a></div>');
		});*/
		/*if (htmlData.length > 0) {
			$('.div_top_icon').html(htmlData.join(''));
			$('.div_top_icon').show();
		}
		else {
			$('.div_top_icon').hide();
		}*/
	}

	function gethref(data) {
		var url = data.content.text;
		var tid = data.content.tid;
		var sku = data.content.sku;
		switch (data.type) {
			case '0':	// 超链接
				if(url.indexOf("?")<0){
					url += '?mtoapp=0';
				}else{
					url += '&mtoapp=0';
				}
				break;
			case '1':	// 专场id
				url = 'hd.htm?tid=' + tid;
				break;
			case '2':	// 根据专场id和商品id来商品详情
				url = 'item.htm?tid=' + tid + '&sku=' + sku;
				break;
			case '3':	// 秒杀
				url = 'spike.html?mtoapp=3';
				break;
			case '4':	// 购物车
				url = 'cart.html?mtoapp=4';
				break;
			case '5':	// 登录
				url = 'logon.html';
				break;
			case '6':	// 注册
				url = 'registr.html';
				break;
			case '7':	// 优惠券
				url = 'coupon.html?mtoapp=7';
				break;
			case '8':	// 红包
				url = 'coupon.html?mtoapp=7';
				break;
			case '9':	// 全部订单
				url = 'orders.html';
				break;
			case '10':	// 售后
				url = 'aftersaleslist.html';
				break;
			case '11':  //个人中心
				url = 'home.html';
				break;
			case '12':  //签到
				url = 'sign.html';
				break;
			case '13':  //线下团购
				url = 'group_buying.html';
				break;
			case '14':  //搜索
				url = 'search.html?key=' + url;
				break;
			case '15': //品牌
				url = 'search.html?brandid=' + tid + '&name=' + url;
				break;
			case '16': //分类
				url = 'search.html?categoryid= '+ tid + '&name=' + url;
				break;
			default:
				url = 'javascript:void(0);';
				break;
		}

		return url;
	}
};


var todayHave_max = "N";
//首页-今日特卖
function todayHave(page,me, shopMobile) {
	if(todayHave_max == "Y"){
		isScrolling = false;
		// me.disable("down",false);
		$('.dropload-down').hide();
		$('.ui-refresh-down>span:first-child').removeClass();
		//xigou.toast('没有更多了',"toast_bottom");
	}
	if(todayHave_max != "Y"){
		xigou.activeIndex.todayHave({
			p: 'curpage=' + page + '&shopmobile='+shopMobile,
			showLoading : false,
			callback: function(response, status) { //回调函数
				isScrolling = false;
				$('#todayNew_product_group_item .ui-refresh-down').remove();
//	            $(window).unbind("scroll");//移除scroll绑定的function
				if (status == xigou.dictionary.success) {
					var json = response || null;
					if (null == json || json.length == 0) return false;

					var code = json.code;
					if (code == 0) {

						if(typeof(response.data) == "undefined")
						{
							todayHave_max = "Y";
							$(".dropload-down").hide();
							if (me) {
								me.lock();
							}

							// 第一页数据为空
							if (page == 1) {
								$("#indexlists").empty();
								$("#indexlists").append('<div class="div_empty"></div>');
								var bSelf = false;
								var dssUser = xigou.getLocalStorage("dssUser");
								if (dssUser && dssUser != "{}") {
									dssUser = JSON.parse(dssUser);
									if (dssUser.token && dssUser.token == xigou.getToken()) {
										// 当前用户就是分销员
										bSelf = true;
									}
								}
								if (bSelf) {
									$("#indexlists").append('<div class="div_empty_desc">你还木有可售商品，快去添加哟</div>');
									$("#indexlists").append('<a href="dss/hotcommodity.html"><div class="div_go_add">选品上架</div></a>');
								}
								else {
									$("#indexlists").append('<div class="div_empty_desc">该店铺木有商品哟!</div>');
								}
							}
							return;
						}
						else if(response.data.list == null || response.data.list.length == 0)
						{
							todayHave_max = "Y";
							$(".dropload-down").hide();
							if (me) {
								me.lock();
							}
							// 第一页数据为空
							if (page == 1) {
								$("#indexlists").empty();
								$("#indexlists").append('<div class="div_empty"></div>');
								var bSelf = false;
								var dssUser = xigou.getLocalStorage("dssUser");
								if (dssUser && dssUser != "{}") {
									dssUser = JSON.parse(dssUser);
									if (dssUser.token && dssUser.token == xigou.getToken()) {
										// 当前用户就是分销员
										bSelf = true;
									}
								}
								if (bSelf) {
									$("#indexlists").append('<div class="div_empty_desc">你还木有可售商品，快去添加哟</div>');
									$("#indexlists").append('<a href="dss/hotcommodity.html"><div class="div_go_add">选品上架</div></a>');
								}
								else {
									$("#indexlists").append('<div class="div_empty_desc">该店铺木有商品哟!</div>');
								}
							}

							return;
						}

						if (response.data.list != null && response.data.list.length > 0) {
							var htmlData = createItemData(response.data.list);

							if (page == 1) {
								$("#indexlists").empty();
							}
							if (htmlData.length > 0) {
								$("#indexlists").append(htmlData.join(" "));
							}
							$('.ui-imglazyload').imglazyload();

							$('.todayHave').each(function(index, el) {
								var s = Date.parse(new Date()),
									e = $(el).attr('endtime');
								getTimeRest(s, e, $(el));
							});
								
							 //记录锚点=========================
							 var dataStr = JSON.stringify(response.data.list);
							 if(page == 1) {
							 	xigou.setSessionStorage("speciallist2","");
							 }
							 var _speciallist = xigou.getSessionStorage("speciallist2");
							 if(_speciallist) {
								 var data = response.data.list;
								 var _oldData = JSON.parse(_speciallist);
								 for(var m = 0,len = data.length;m < len ;m++) {
									 _oldData.push(data[m]);
								 }
								 xigou.setSessionStorage("speciallist2",JSON.stringify(_oldData));
							 }/*else {
							 	xigou.setSessionStorage("speciallist2",dataStr);
							 }*/
							xigou.setSessionStorage("historypage2",page);
							xigou.setSessionStorage("historypageshop2",shopMobile);

							//绑定横向滑动事件
							$(".ui-scroller-a").each(function(index,element){
								var scroll = new fz.Scroll(element, {
									scrollX: true
								});
							})
							//myScroll.refresh();
							//====================
						}
						// if(me) {
						// 	me.afterDataLoading("down");
						// }

					}
				}
			}
		});
	}
}

function loadHistoryData(){
	listCount = xigou.getSessionStorage("historypage2");
	var _speciallist = xigou.getSessionStorage("speciallist2");

	if(_speciallist) {
		var data = JSON.parse(_speciallist);
		var htmlData = createItemData(data);

		if (htmlData.length > 0) {
			$("#indexlists").append(htmlData.join(" "));
		}
		else {
			// 数据为空
			$("#indexlists").append('<div class="div_empty"></div>');
			var bSelf = false;
			var dssUser = xigou.getLocalStorage("dssUser");
			if (dssUser && dssUser != "{}") {
				dssUser = JSON.parse(dssUser);
				if (dssUser.token && dssUser.token == xigou.getToken()) {
					// 当前用户就是分销员
					bSelf = true;
					alert(1)
				}
			}
			if (bSelf) {
				$("#indexlists").append('<div class="div_empty_desc">你还木有可售商品，快去添加哟</div>');
				$("#indexlists").append('<a href="dss/hotcommodity.html"><div class="div_go_add">选品上架</div></a>');
			}
			else {
				$("#indexlists").append('<div class="div_empty_desc">该店铺木有商品哟!</div>');
			}
		}
		$('.ui-imglazyload').imglazyload();

		$('.todayHave').each(function(index, el) {
			var s = Date.parse(new Date()),
				e = $(el).attr('endtime');
			getTimeRest(s, e, $(el));
		});

		var tid = xigou.getSessionStorage("specialid");
		var sku = xigou.getSessionStorage("specialSku");
		if(sku){
			var _scrollEl = $('a[href$="'+sku+'"]')[0];
			if (_scrollEl) {
				setTimeout(function(){
					_scrollEl.scrollIntoView();
				}, 500);
			}
		}else if(tid) {
			var _scrollEl = $('a[href$="'+tid+'"]')[0];
			if (_scrollEl) {
				setTimeout(function(){
					_scrollEl.scrollIntoView();
				}, 500);
			}


		}
		//绑定横向滑动事件
		$(".ui-scroller-a").each(function(index,element){
			var scroll = new fz.Scroll(element, {
				scrollX: true
			});
		})
		xigou.setSessionStorage("specialid","");
		xigou.setSessionStorage("specialSku","");
	}
}

//处理空值 
function solveNull(d, n) {
	return (d) == null || typeof(d) == "undefined" ? n : d
}

function InitWeixin(){
	xigou.loading.open();
	var pa = [];
	var url = location.href.split('#')[0].replace(/&+/g, "%26");
	pa.push('url=' + url);

	xigou.activeUser.config({
		p : pa.join('&'),
		showLoading : false,
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

	var lineLink = location.href.split('?')[0];
	if(dssUserMobile != null && dssUserMobile != 'undefined' && dssUserMobile != '') {
		lineLink = lineLink + "?shop=" + dssUserMobile + "&dssName=" + dssName;
	}
	var title = '西客屋';
	var desc = '一键式轻创业模式， 用更少的钱体验全球品质生活';

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
	$("#index_scan, #index_scan1").on("click",function(){
		wx.scanQRCode({
		    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
		    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
		    success: function (res) {
		    	var str = JSON.stringify(res).substring(14,34);
		    	if(str == 'http://w.51seaco.com' || str == 'https://w.51seaco.com'){
					window.location.href=res.resultStr;
				}else if(str == 'http://weixin.qq.com'){
					wx.scanQRCode();
				}
		    	else{
					$.tips({
						content:'小西不支持的消息格式！',
						stayTime:2000,
						type:"warn"
					});
				}
		    }
		});
	});
	xigou.loading.close();
	xigou.wxreadyalert();
});

wx.error(function (res) {
});
// 微信自动联合登录
function wxunionlogin(shopMobile){
	InitPage(shopMobile);
// 	if (!isWeiXin()) {
// 		forceBindTel();
// 		return;
// 	}
// 	//var token = xigou.getLocalStorage("token");
// 	//if (token) {
// 	//	forceBindTel();
// 	//	return;
// 	//}
// 	var openid = xigou.getLocalStorage("openid");
// 	var params = {
// 		uniontype: '1',
// 		unionval: openid,
// //      signature:'1a0708f6e3fdc5ebb25c89a4e0765fdc', //wx1.0.1
//         signature:'af288db869b27f21cc1440385d63d4d5', //wx1.5.0
// 		curtime: (new Date()).valueOf().toString()
// 	};
//
// 	if (shopMobile) {
// 		params.shopMobile = shopMobile;
// 	}
//
// //	if(!isWeiXin()){
// 		xigou.activeUser.unionlogon({
// 			requestBody: params,
// 			showLoading: false,
// 			callback: function(response, status) { // 回调函数
// 	//			$.tips({
// 	//				content:'111111111',
// 	//                stayTime:2000,
// 	//                type:"warn"
// 	//			})
// 				if (status == xigou.dictionary.success && response.code == 0) {
// 					// 登录成功
// 					if (response.data.promoterinfo && response.data.promoterinfo != "{}") {
// 						var promoterinfo = 	JSON.parse(response.data.promoterinfo);
// 						promoterinfo.token = response.data.token;
// 						xigou.setLocalStorage("dssUser", promoterinfo, true);
// 						setShopName(promoterinfo);
// 					}
//
// 					var back = {
// 						'token': response.data.token,
// 						'telephone': response.data.tel,
// 						'name': response.data.name
// 					};
// 					//实名认证信息清除
// 					xigou.removeSessionStorage('realname');
// 					xigou.removeSessionStorage('realnum');
// 					xigou.setSessionStorage("userinfo", back, true);
// 					xigou.setLocalStorage("show_name", response.data.tel || response.data.name);
// 					xigou.setLocalStorage("token", response.data.token);
// 					forceBindTel();
// 				}
// 				else{
// 					if (!xigou.getLocalStorage('wxnickname') && !xigou.getLocalStorage('wxImage') && !xigou.getSessionStorage('getediconnickname')) {
// 						xigou.getwxOpenid(1, true);
// 					}
// 					forceBindTel();
// 				}
// 			}
// 		})
//	}
}

/*function forceBindTel(){
	xigou.forceBindTel({
		callback:function(nNeed) {
			InitPage(dssUserMobile);
		}
	});
}*/

function InitPage(shopMobile){
	moduleInfo();
	loadGroupBuyList(groupBuyPage++, null);
	if (!shopMobile) {
		//return;
	}

	xigou.setSessionStorage('productdetails_backurl', window.location.href);

	var dssData =xigou.getLocalStorage("dssUser");
	if(dssData){
		var dssUser = JSON.parse(dssData);
		showFlag = dssUser.mobile && dssUser.token ?true :false;
		if(showFlag){
			$(".div_icon").show();

		}else{
			//$(".home-name").css("top","46px");
		}
	}

	var dssData =xigou.getLocalStorage("dssUser");
	if(dssData){
		var dssUser = JSON.parse(dssData);
		showFlag = dssUser.mobile && dssUser.token ?true :false;
		if(showFlag){
			$(".div_icon").show();

		}else{
			$(".dsshomeBtn").hide();
			$(".home-name").addClass("centerAlign");
		}
	}

	$(".dropload-refresh").hide();
	//锚点数据
	if(xigou.getSessionStorage("speciallist2") && ( (shopMobile == null &&  xigou.getSessionStorage("historypageshop2") == 'null')|| xigou.getSessionStorage("historypageshop2") == shopMobile)) {
		loadHistoryData();
	}else {
		todayHave(listCount, null, shopMobile);
	}
	listCount++;

	$('.content').dropload({
		scrollArea : window,
		loadDownFn : function(me){
			me.resetload();
			todayHave(listCount,me,shopMobile);
			listCount++;
		}
	});

	if (isWeiXin()) {
		InitWeixin();
	}

}

function createItemData(dataList){
	var htmlData = [];
	$.each(dataList, function(index, data){
		// 单品团
		if (1 == data.type) {
			var Yuan = data.price.split('.')[0] || '00';
			var Fen = data.price.split('.')[1] || '00';
			if (!data.countryname) {
				data.countryname = '';
			}
			if (!data.channel) {
				data.channel = '';
			}
			htmlData.push('<a href="item.htm?tid=' + data.tid + '&sku=' + data.sku + '">');
			/*if (index < dataList.length - 1 && dataList[index + 1].type && dataList[index + 1].type == 1) {
				// 下一个还是单品团
				htmlData.push('<div class="div_item ssale_desc_bottom">');
			}
			else {
				htmlData.push('<div class="div_item">');
				$(".div_top_icon2").show();
			}*/
			htmlData.push('<div class="div_item ssale_desc_bottom">');
			htmlData.push('		<div class="div_item_img">');
			htmlData.push('			<img src="' + data.imgurl + '">');
			htmlData.push('		</div>');
			htmlData.push('		<div class="div_item_desc">');
			htmlData.push('			<div class="div_desc">');
			if (data.countryimg) {
				htmlData.push('<img class="contry-img" src="' + data.countryimg + '">');
			}
			if (data.countryname) {
				htmlData.push('<span class="country-info">' + data.countryname + '</span>')
			}
			if (data.channel) {
				htmlData.push('<span class="country-info">' + data.channel + '</span>');
			}
			htmlData.push('			</div>');
			htmlData.push('			<div class="div_name">' + data.name + '</div>');
			htmlData.push('		</div>');
			htmlData.push('		<div class="div_price_info">');
			htmlData.push('			<div class="new_price">¥<span>' + Yuan + '</span>.' + Fen + '</div>');
			if (dssUserMobile && data.commision && showFlag) {	// 返佣
				htmlData.push('<div class="old_price"><span>¥' + data.oldprice + ' </span>&nbsp;&nbsp;返佣：<span class="return">¥' + data.commision + '</span></div>');
			}
			else {
				htmlData.push('<div class="old_price"><span>¥' + data.oldprice + ' </span></div>');
			}
			htmlData.push('		</div>');
			htmlData.push('	</div>');
			htmlData.push('</a>')
		}
		// 主题团
		else if (2 == data.type){
			htmlData.push('<a href="hd.htm?tid=' + data.tid + '&from=shop" class="hot_link"">');
			if(data.itemlist && data.itemlist.length > 0 ) {
				htmlData.push('<p class = "div_img">');
			}
			else {
				htmlData.push('<p class = "div_img div_none_item">');
			}
			htmlData.push('	<img class="ui-imglazyload" data-url="' + data.imgurl + '"/>');
			htmlData.push('</a>');
			htmlData.push('</p>');
			//专题产品外露
			var oWidth = Math.round($(".content").width()/3.8);
			var oHeight = Math.round(oWidth+70);
			if (dssUserMobile  && showFlag) {
				oHeight += 15;
			}
			if(data.itemlist && data.itemlist.length > 0 ){
				htmlData.push('<div style = "height:'+oHeight+'px" class="ui-scroller-a seckill-new-container">');
				htmlData.push('<ul class="ui-slider-content">');
				var oList = data.itemlist;
				for(var j = 0;j<oList.length;j++){
					var oY = oList[j].price.split(".")[0];
					var oJ = oList[j].price.split(".")[1];
					htmlData.push( '<li style = "width:'+oWidth+'px"><a class="error_tuan" style="display:block;width:100%;height:'+oWidth+'px;overflow: hidden;" href="item.htm?tid='+oList[j].tid+'&sku='+oList[j].sku+'"><img src = '+oList[j].imgurl+' /></a>');
					htmlData.push('<div class="seckill-item-price">');
					htmlData.push('<span class="seckill-new-title">'+oList[j].name+'</span>');
					htmlData.push('<span class="seckill-new-price"><b class = "mS">¥</b>'+oY+'<b class = "mB">.'+oJ+'</b><b class="mS" style = "padding-left:0.5em;color:#999;text-decoration:line-through;">¥'+oList[j].oldprice+'</b></span>');

					if (dssUserMobile && oList[j].commision  && showFlag) {
						htmlData.push('<span class="span_return">返佣：<span>¥' + oList[j].commision + '</span></span>');
					}

					htmlData.push('</div></li>');
				}
				htmlData.push('<li><a href="hd.htm?tid=' + data.tid + '"><img src = "img/zt_more.png" /></a></li>');
				htmlData.push('</ul>');
				htmlData.push('</div>');
			}
		}
	});

	return htmlData;
}

/*活动动态配置*/
	
function loadGroupBuyList(page, me) {
    var param = {
        page: page,
        callback: function(response, status) {
            if (status == xigou.dictionary.success) {
                if (response.code == 0) {
                    LoadTuanListData(me, response.data);
                }
                else {
                    $.tips({
                        content:response.msg||'获取数据失败,请检测网络连接!',
                        stayTime:2000,
                        type:"warn"
                    });
                }
            }
            else if (!response) {
                $.tips({
                    content:'获取数据失败,请检测网络连接!',
                    stayTime:2000,
                    type:"warn"
                });
            }
            else {
                $.tips({
                    content:response.msg||'获取数据失败,请检测网络连接!',
                    stayTime:2000,
                    type:"warn"
                });
            }
        }
    };
    xigou.groupbuy.listforindex(param);
}

function LoadTuanListData(me, data) {
    if (!data || !data.list || data.list.length == 0) { // 请求数据移除
        bMaxPage = true;
        if (me) {
            me.lock();
        }
//        $(".dropload-down").hide();
        return;
    }

    var htmlData = [];
    var oWidth = Math.round($(".content").width()/3.8);
	var oHeight = Math.round(oWidth+42);
	if (dssUserMobile  && showFlag) {
		oHeight += 15;
	}
	if(data.list && data.list.length > 0 ){
		htmlData.push('<div style = "height:'+oHeight+'px" class="ui-scroller-a seckill-new-container">');
		htmlData.push('<ul class="ui-slider-content">');
		var oList = data.list;
		for(var j = 0;j<oList.length;j++){
			htmlData.push( '<li style = "width:'+oWidth+'px;"><a class="error_tuan" style="display:block;width:100%;height:'+oWidth+'px;overflow: hidden; border: 1px solid #e8e8e8;" href="tuan.html?gbid='+oList[j].gbid+'"><img src = '+oList[j].imgurl+' /></a>');
			htmlData.push('<div class="seckill-item-price">');
			htmlData.push('<span class="group-new-price"><b class="groupnub"><b class="lP">'+oList[j].pa+'</b>人团</b><b class = "lS">¥</b><b class="lG">'+oList[j].groupprice+'</b></span>');
			htmlData.push('</div></li>');
		}
		htmlData.push('<li><a href="tuanlist.html"><img src = "img/zt_more.png" /></a></li>');
		htmlData.push('</ul>');
		htmlData.push('</div>');
	}
	
	$('.groupconfigure').show();
    $('.tuanlist').append(htmlData.join(' '));
    $(".tuan_show_more").on("click",function(){
    	window.location.href= "tuanlist.html";
	})

    if (data.curpage == data.totalpagecount) {  // 获取到最大页数据
        bMaxPage = true;
        if (me) {
            me.lock();
        }
        $(".dropload-down").hide();
    }
    
    $(".ui-scroller-a").each(function(index,element){
		var scroll = new fz.Scroll(element, {
			scrollX: true
		});
	})
}