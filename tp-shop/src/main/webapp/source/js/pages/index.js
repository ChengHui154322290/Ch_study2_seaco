var refreshComp;//刷新组件
var myScroll;
var listCount = 1;
var firstssale = true;
var imgUrl = '';
var showFlag = "";
var showLoading = true;

var groupBuyPage = 1;
var bMaxPage = false;
var maxLabsElementCnt = 5;

var defaulturl = xigou.activeHost + 'index.html';
var shop = xigou.getQueryString("shop");
var singleSign=true;

if(shop){
	xigou.setSessionStorage("shop", shop);	
}
var openId = xigou.getQueryString("openId");
var currentTel=xigou.getLocalStorage("show_name");
$(function() {
	//hhb用户通过openId登录
	var channelCode = xigou.channelcode;
	if("hhb"==channelCode.toLowerCase() && openId!=null && openId!=""){
		window.location.href = "hhblogin.html?openId="+openId;
	}
	var ua = window.navigator.userAgent.toLowerCase(); 
	//加载渠道信息
	loadChannelInfo();
	//加载分销员信息
	getDssUserInfo();
	//微信联合登录
	//wxunionlogin();
	InitPage();
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
	
	var channelCode = xigou.channelcode;
	if (xigou.channelcode == "hzftv") {
		$(".div_top_icon2 > img").attr("src","shop/"+channelCode+"_top_menu_spike.png");
	}else{
		$('.div_top_icon2 > img').attr('src',"img/top_menu_spike.png");
	}
});


function loadChannelInfo(){
	var channelInfo = xigou.getSessionStorage("channelInfo");
	if(channelInfo && channelInfo != "{}") {
		var info = JSON.parse(channelInfo);
		setMetaTitle(info.eshopname);
		return;
	}
	xigou.getChannelInfo({
		requestBody: {
			'token':xigou.getToken()
		},
		callback: function(response, status) {
		if (status == xigou.dictionary.success) {
			if (response != null && response.data != null) {
				xigou.setSessionStorage("channelInfo", JSON.stringify(response.data));
				setMetaTitle(response.data.eshopname);
			}
		}
	}
	});
}

function setMetaTitle(title){
	if(title)
		document.title = title;
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
			var url = gethref(data[i]);
			html.push('<li>');
			html.push('	<a href="' + url + '">');
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
			htmlData.push('<div><a href="' + data[i].linkurl + '"><img src="' + data[i].imageurl + '">' + data[i].title + '</a></div>');
		}
		html.push('<div class="div_top_icon">' + htmlData.join('') + "</div>");
		$('.div_top_lab').html(html.join(''));
		$('.div_top_lab').show();
		
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
				url = 'hd.htm?tid=' + tid+'&tel='+currentTel;
				break;
			case '2':	// 根据专场id和商品id来商品详情
				url = 'item.htm?tid=' + tid + '&sku=' + sku+'&tel='+currentTel;
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
			default:
				url = 'javascript:void(0);';
				break;
		}

		return url;
	}
};


var todayHave_max = "N";
//首页-今日特卖
function todayHave(page,me) {
	if(todayHave_max == "Y"){
		isScrolling = false;
		// me.disable("down",false);
		$('.dropload-down').hide();
		$('.ui-refresh-down>span:first-child').removeClass();
		//xigou.toast('没有更多了',"toast_bottom");
	}
	var shopMobile = xigou.getSessionStorage("shop");	
	if(todayHave_max != "Y"){
		xigou.activeIndex.todayHave({
			p: 'appversion=1.1.1&curpage=' + page+"&shopmobile="+shopMobile+"&token="+xigou.getToken(),
			showLoading : showLoading,
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
//								var bSelf = false;
//								var dssUser = xigou.getSessionStorage("dssUser");
//								if (dssUser && dssUser != "{}") {
//									dssUser = JSON.parse(dssUser);
//									if (dssUser.token && dssUser.token == xigou.getToken()) {
//										// 当前用户就是分销员
//										bSelf = true;
//									}
//								}
//								if (bSelf) {
//									$("#indexlists").append('<div class="div_empty_desc">你还木有可售商品，快去添加哟</div>');
//									$("#indexlists").append('<a href="dss/hotcommodity.html"><div class="div_go_add">选品上架</div></a>');
//								}
//								else {
									$("#indexlists").append('<div class="div_empty_desc">该商城木有商品哟!</div>');
//								}
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
//								var bSelf = false;
//								var dssUser = xigou.getSessionStorage("dssUser");
//								if (dssUser && dssUser != "{}") {
//									dssUser = JSON.parse(dssUser);
//									if (dssUser.token && dssUser.token == xigou.getToken()) {
//										// 当前用户就是分销员
//										bSelf = true;
//									}
//								}
//								if (bSelf) {
//									$("#indexlists").append('<div class="div_empty_desc">你还木有可售商品，快去添加哟</div>');
//									$("#indexlists").append('<a href="dss/hotcommodity.html"><div class="div_go_add">选品上架</div></a>');
//								}
//								else {
									$("#indexlists").append('<div class="div_empty_desc">该店铺木有商品哟!</div>');
//								}
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
								if($("#indexlists").children()[0].tagName == "A"){
									$(".div_top_icon2").show();
								}else{
									$(".div_top_icon2").hide();
								}
							}
							if(page == 1 && response.data.list[0].type==2){
								$(".div_top_icon2").hide();
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
							 	xigou.setSessionStorage("speciallist","");
							 }
							 var _speciallist = xigou.getSessionStorage("speciallist");
							 if(_speciallist) {
								 var data = response.data.list;
								 var _oldData = JSON.parse(_speciallist);
								 for(var m = 0,len = data.length;m < len ;m++) {
									 _oldData.push(data[m]);
								 }
								 xigou.setSessionStorage("speciallist",JSON.stringify(_oldData));
							 }else {
							 	xigou.setSessionStorage("speciallist",dataStr);
							 }
							xigou.setSessionStorage("historypage",page);
//							xigou.setSessionStorage("historypageshop",shopMobile);

							//绑定横向滑动事件
							$(".ui-scroller-a").each(function(index,element){
								var scroll = new fz.Scroll(element, {
									scrollX: true
								});
							})
						}

					}
				}
			}
		});
	}
sleep(100);

}

function loadHistoryData(){
	listCount = xigou.getSessionStorage("historypage");
	var _speciallist = xigou.getSessionStorage("speciallist");

	if(_speciallist) {
		var data = JSON.parse(_speciallist);
		var htmlData = createItemData(data);

		if (htmlData.length > 0) {
			$("#indexlists").append(htmlData.join(" "));
		}
		else {
			// 数据为空
			$("#indexlists").append('<div class="div_empty"></div>');
//			var bSelf = false;
//			var dssUser = xigou.getSessionStorage("dssUser");
//			if (dssUser && dssUser != "{}") {
//				dssUser = JSON.parse(dssUser);
//				if (dssUser.token && dssUser.token == xigou.getToken()) {
//					// 当前用户就是分销员
//					bSelf = true;
//				}
//			}
//			if (bSelf) {
//				$("#indexlists").append('<div class="div_empty_desc">你还木有可售商品，快去添加哟</div>');
//				$("#indexlists").append('<a href="dss/hotcommodity.html"><div class="div_go_add">选品上架</div></a>');
//			}
//			else {
				$("#indexlists").append('<div class="div_empty_desc">该店铺木有商品哟!</div>');
//			}
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
	
	/*if($("section a:first")){
		alert(1);
	}else{
		alert(2);
	}*/
	
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
		showLoading: false,
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
//	if(dssUserMobile != null && dssUserMobile != 'undefined' && dssUserMobile != '') {
//		lineLink = lineLink + "?shop=" + dssUserMobile;
//	}
	
	
	
	var channelCode = xigou.channelcode;
	if (xigou.channelcode == "hzftv") {
		var title = '4号店';
		var desc = '杭州4套官方商城——4号店，全球好货一键购买。';
	}else{
		var title = '全球购商城';
		var desc = '全球好货一键购买，精彩纷呈，欢迎选购！';
	}
	var channelinfo = xigou.getSessionStorage("channelInfo");
	if(channelinfo && channelinfo != "{}"){
		channelinfo = JSON.parse(channelinfo);
		if(channelinfo.sharetitle && channelinfo.sharetitle != ""){
			title = channelinfo.sharetitle;
			desc = channelinfo.sharecontent;
		}
	}
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
//	$(".index_scan").on("click",function(){
//		wx.scanQRCode({
//		    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
//		    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
//		    success: function (res) {
//		    	var str = JSON.stringify(res).substring(14,34);
//		    	if(str == 'http://m.51seaco.com'){
//					window.location.href=res.resultStr;
//				}else if(str == 'http://weixin.qq.com'){
//					wx.scanQRCode();
//				}
//		    	else{
//					$.tips({
//						content:'不支持的消息格式！',
//						stayTime:2000,
//						type:"warn"
//					});
//				}
//		    }
//		});
//	});
	xigou.loading.close();
	xigou.wxreadyalert();
});

wx.error(function (res) {
});

// 微信自动联合登录
function wxunionlogin(){
	if (!isWeiXin()) {
		forceBindTel();
		return;
	}
	//var token = xigou.getLocalStorage("token");
	//if (token) {
	//	forceBindTel();
	//	return;
	//}
	var openid = xigou.getLocalStorage("openid");
	var params = {
		uniontype: '1',
		unionval: openid,
		signature:'1a0708f6e3fdc5ebb25c89a4e0765fdc',
		curtime: (new Date()).valueOf().toString()
	};

//	if (shopMobile) {
//		params.shopMobile = shopMobile;
//	}

	xigou.activeUser.unionlogon({
		requestBody: params,
		showLoading: false,
		callback: function(response, status) { // 回调函数
			if (status == xigou.dictionary.success && response.code == 0) {

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
				forceBindTel();
			}
		}
	})
}

function forceBindTel(){
	xigou.forceBindTel({
		callback:function(nNeed) {
			InitPage();
		}
	});
}

function InitPage(){
	xigou.removeSessionStorage('productdetails_backurl');
	moduleInfo();
	/*initGroupBuyList();*/
	$(".dropload-refresh").hide();
	//锚点数据
	if(xigou.getSessionStorage("speciallist") && xigou.getSessionStorage("historypageshop") == 'null') {
		loadHistoryData();
	}else {
		todayHave(listCount, null);
	}
	listCount++;

	$('.content').dropload({
		scrollArea : window,
		loadDownFn : function(me){
			me.resetload();
			todayHave(listCount,me);
			listCount++;
		}
	});

	if (isWeiXin()) {
		InitWeixin();
	}

}

function initGroupBuyList() {	
	loadGroupBuyList(groupBuyPage++, null);
	 var dropload = $(".tuanlist").dropload({
      scrollArea: window,
      loadDownFn: function (me) {
          if (bMaxPage) {
              return;
          }  
          loadGroupBuyList(groupBuyPage++, me);
      }
	 });
}

function createItemData(dataList){
	var htmlData = [];
	$.each(dataList, function(index, data){
		// 单品团
		if (1 == data.type) {
			$(".div_top_icon2").show();
			var Yuan = data.price.split('.')[0] || '00';
			var Fen = data.price.split('.')[1] || '00';
			if (!data.countryname) {
				data.countryname = '';
			}
			if (!data.channel) {
				data.channel = '';
			}
			htmlData.push('<a href="item.htm?tid=' + data.tid + '&sku=' + data.sku +'&tel='+currentTel+ '">');
			if (index < dataList.length - 1 && dataList[index + 1].type && dataList[index + 1].type == 1) {
				// 下一个还是单品团
				htmlData.push('<div class="div_item ssale_desc_bottom">');
			}
			else {
				htmlData.push('<div class="div_item">');
				
			}
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
//			if (dssUserMobile && data.commision && showFlag) {	// 返佣
//				htmlData.push('<div class="old_price"><span>¥' + data.oldprice + ' </span>&nbsp;&nbsp;返佣：<span class="return">¥' + data.commision + '</span></div>');
//			}
//			else {
				htmlData.push('<div class="old_price"><span>¥' + data.oldprice + ' </span></div>');
//			}
			htmlData.push('		</div>');
			htmlData.push('	</div>');
			htmlData.push('</a>')
		}
		// 主题团
		else if (2 == data.type){
			if(data.itemlist && data.itemlist.length > 0 ) {
				htmlData.push('<p class = "div_img">');
			}
			else {
				htmlData.push('<p class = "div_img div_none_item">');
			}
			htmlData.push('<a href="hd.htm?tid=' + data.tid +'&tel='+currentTel+ '" class="hot_link">');
			htmlData.push('	<img class="ui-imglazyload" data-url="' + data.imgurl + '"/>');
			htmlData.push('</a>');
			htmlData.push('</p>');
			//专题产品外露
			var oWidth = Math.round($(".content").width()/3.8);
			var oHeight = Math.round(oWidth+70);
//			if (dssUserMobile  && showFlag) {
//				oHeight += 15;
//			}
			if(data.itemlist && data.itemlist.length > 0 ){
				htmlData.push('<div style = "height:'+oHeight+'px" class="ui-scroller-a seckill-new-container">');
				htmlData.push('<ul class="ui-slider-content">');
				var oList = data.itemlist;
				for(var j = 0;j<oList.length;j++){
					var oY = oList[j].price.split(".")[0];
					var oJ = oList[j].price.split(".")[1];
					htmlData.push( '<li style = "width:'+oWidth+'px"><a style="display:block;width:100%;height:'+oWidth+'px;overflow: hidden;" href="item.htm?tid='+oList[j].tid+'&sku='+oList[j].sku+'&tel='+currentTel+'"><img src = '+oList[j].imgurl+' /></a>');
					htmlData.push('<div class="seckill-item-price">');
					htmlData.push('<span class="seckill-new-title">'+oList[j].name+'</span>');
					htmlData.push('<span class="seckill-new-price"><b class = "mS">¥</b>'+oY+'<b class = "mB">.'+oJ+'</b><b class="mS" style = "padding-left:0.5em;color:#999;text-decoration:line-through;">¥'+oList[j].oldprice+'</b></span>');

//					if (dssUserMobile && oList[j].commision  && showFlag) {
//						htmlData.push('<span class="span_return">返佣：<span>¥' + oList[j].commision + '</span></span>');
//					}

					htmlData.push('</div></li>');
				}
				htmlData.push('<li><a href="hd.htm?tid=' + data.tid +'&tel='+currentTel+'"><img src = "img/zt_more.png" /></a></li>');
				htmlData.push('</ul>');
				htmlData.push('</div>');
			}
		}
	});
	
	/*if($("#indexlists").children().size()>0){
	    //含有子元素
		$(".div_top_icon2").show();
	}else{
	    //没有有子元素
		$(".div_top_icon2").hide();
	}*/
	
	return htmlData;
}

function sleep(n) {
    var start = new Date().getTime();
    while(true)  if(new Date().getTime()-start > n) break;
    }

/*if ($("#indexlists:has(a)").length==0)         
{   
  console.log(123);  
}   
else    
{   
	console.log(321);      
} */

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
			htmlData.push( '<li style = "width:'+oWidth+'px;"><a style="display:block;width:100%;height:'+oWidth+'px;overflow: hidden; border: 1px solid #e8e8e8;" href="item.htm?tid='+oList[j].tid+'&sku='+oList[j].sku+'"><img src = '+oList[j].imgurl+' /></a>');
			htmlData.push('<div class="seckill-item-price">');
			htmlData.push('<span class="group-new-price"><b class="groupnub"><b class="lP">'+oList[j].pa+'</b>人团</b><b class = "lS">¥</b><b class="lG">'+oList[j].groupprice+'</b></span>');
			htmlData.push('</div></li>');
		}
		htmlData.push('<li><a href="tuanlist.html"><img src = "img/zt_more.png" /></a></li>');
		htmlData.push('</ul>');
		htmlData.push('</div>');
	}

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