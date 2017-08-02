var refreshComp;//刷新组件
var myScroll;
var listCount = 1;
var firstssale = true;
var imgUrl = '';
var dssUserMobile = null;
var fromDsshome=xigou.getQueryString("from");
var showFlag = "";
var showLoading = true;
var CurPage = 1;
var bMaxPage = false;
var searchSortType = xigou.getQueryString('sort') || "";                    // 排序方式
var Key = xigou.getQueryString('key');      // 关键字
var categoryid = xigou.getQueryString('categoryid');
var brandid = xigou.getQueryString('brandid');
var fromPage = xigou.getQueryString('fromPage');
var Key = xigou.getQueryString('key');
var name = xigou.getQueryString('name');

$(function() {

	$("#sortByDefoule").click(function(){
        $(".group_type").show();
        $(".banner").hide();
        $("header").show();
        $(".group_type").css({
            'margin-top':"91px"
        });
        $(".div_search_sort").addClass("div_search_sorts").css({
            'margin-top':"51px"
        });
        $(".cover").show();
    })
    $('.type').click(function(){
        $(this).addClass('current_sort').siblings().removeClass("current_sort");
        $(".cover").hide();
        $("#sortByDefoule").html(($(this).html()));
        $(".group_type").hide();
        $(".shop_first").addClass("shop_top");
    });
    $(".group-search").attr("href","group_search.html?fromPage=group_buying");

	$("#input_search_txt").focus(function () {
          window.location.href="group_search.html?fromPage=group_buying";
	});
	$(".cancal_search").click(function () {
          $(".div_search_history").hide();
	})

	forceBindTel();
	if (isWeiXin()) {
		InitWeixin();
	}


});



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


//广告位信息
function moduleInfo() {
	xigou.activeOfflinegb.banner({
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
					adInfo(response.data);
				}
			}
		}
	});
	function adInfo(data) {
		if (!data || data.length == 0) {
			return;
		}		
		
/*		$(".group_top").click(function(){				
		$(".link").trigger('click');
	})*/
		
		var html = [];
		html.push('<div class="ui-slider">');
		html.push('<ul class="ui-slider-content">');
		for (var i = 0; i < data.length; i++) {
			var url = gethref(data[i]);
			html.push('<li>');
			html.push('	<a class="link" href="' + url + '">');
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
				url = 'group_shop.html?id=' + tid;
				break;
			case '2':	// 根据专场id和商品id来商品详情
				url = 'group_detail.html?tid=' + tid + '&sku=' + sku;
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

function shoplist(page,me, shopMobile) {
	if(todayHave_max == "Y"){
		isScrolling = false;
		// me.disable("down",false);
		$('.dropload-down').hide();
		$('.ui-refresh-down>span:first-child').removeClass();
		//xigou.toast('没有更多了',"toast_bottom");
	}
	if(todayHave_max != "Y"){
		xigou.activeOfflinegb.shoplist({
			p: 'appversion=1.1.1&curpage=' + page,
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
								$("#shop_products_id").empty();
							}
							if (htmlData.length > 0) {
								$("#shop_products_id").append(htmlData.join(" "));
							}
							$('.ui-imglazyload').imglazyload();

							// $('.todayHave').each(function(index, el) {
							// 	var s = Date.parse(new Date()),
							// 		e = $(el).attr('endtime');
							// 	getTimeRest(s, e, $(el));
							// });
								
							 //记录锚点=========================
							//  var dataStr = JSON.stringify(response.data.list);
							//  if(page == 1) {
							//  	xigou.setSessionStorage("speciallist","");
							//  }
							//  var _speciallist = xigou.getSessionStorage("speciallist");
							//  if(_speciallist) {
							// 	 var data = response.data.list;
							// 	 var _oldData = JSON.parse(_speciallist);
							// 	 for(var m = 0,len = data.length;m < len ;m++) {
							// 		 _oldData.push(data[m]);
							// 	 }
							// 	 xigou.setSessionStorage("speciallist",JSON.stringify(_oldData));
							//  }else {
							//  	xigou.setSessionStorage("speciallist",dataStr);
							//  }
							// xigou.setSessionStorage("historypage",page);
							// xigou.setSessionStorage("historypageshop",shopMobile);

							//绑定横向滑动事件
							// $(".ui-scroller-a").each(function(index,element){
							// 	var scroll = new fz.Scroll(element, {
							// 		scrollX: true
							// 	});
							// })
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

/*function loadHistoryData(){
	listCount = xigou.getSessionStorage("historypage");
	var _speciallist = xigou.getSessionStorage("speciallist");

	if(_speciallist) {
		var data = JSON.parse(_speciallist);
//		var htmlData = createItemData(data);

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
	
	
		$(function(){
			if($("#indexlists").children().size()>0){
			    //含有子元素
				$(".div_top_icon2").show();
			}else{
			    //没有有子元素
				$(".div_top_icon2").hide();
			}
		})
	
}*/

//处理空值 
function solveNull(d, n) {
	return (d) == null || typeof(d) == "undefined" ? n : d
}

function InitWeixin(){
	var showDownFlag = xigou.getSessionStorage("showDownFlag");
	//var u = navigator.userAgent;
	//var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
	//var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
	//if(isiOS){
	//	downUrl = "https://itunes.apple.com/us/app/xi-gou-quan-qiu-gou/id1080175355?l=zh&ls=1&mt=8";
	//}else if(isAndroid){
	//	downUrl = "http://android.myapp.com/myapp/detail.htm?apkName=com.tp.gj";
	//}
	/*if(showDownFlag != "false"){
		$(".content").css("margin-top","95px");
		$("body").append('<div class = "downLoadBtn"><div class = "verticalLine"></div><img src = "img/downLoadBtnLogo.png" /><div class = "f1">西客商城<br /><span>全球好货一站购</span></div><div class = "dlRight"><a class = "closeBtn"></a></div><a class = "dBtn" href="http://a.app.qq.com/o/simple.jsp?pkgname=com.tp.gj"></a></div>');
	}
	$(".closeBtn").on("click",function(){
		$(".downLoadBtn").hide();
		$(".content").css("margin-top","45px");
		xigou.setSessionStorage("showDownFlag","false");
	})*/

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
	if(dssUserMobile != null && dssUserMobile != 'undefined' && dssUserMobile != '') {
		lineLink = lineLink + "?shop=" + dssUserMobile;
	}
	
	var title = '西客线下团购';
	var desc = '全国首家跨境生活综合体的线下团购';

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
	$(".index_scan").on("click",function(){
		wx.scanQRCode({
		    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
		    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
		    success: function (res) {
		    	var str = JSON.stringify(res).substring(14,34);
		    	if(str == 'http://m.51seaco.com'){
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
	if (!isWeiXin()) {
		forceBindTel();
		
		$(window).scroll(function(){
		    var scrolltop = document.documentElement.scrollTop || document.body.scrollTop;
		        if(scrolltop>100)
		        {
		            $("header").show();
		            $(".banner").hide();
		            $(".group_type").addClass("group_types")
		            $(".div_search_sort").addClass("div_search_sorts");
		            $(".shop_products_list").addClass("shop_products_list1");
		/*        	$(".top").show();*/
		        }
		         else{
		             $(".banner").show();
		             $("header").hide();
		             $(".div_search_sort").removeClass("div_search_sorts");
		             $(".shop_first").removeClass("shop_top");
		             $(".shop_products_list").removeClass("shop_products_list1");
//		        	 $(".top").hide();
		         }
		    });
		
		return;
	}
		$(window).scroll(function(){
		    var scrolltop = document.documentElement.scrollTop || document.body.scrollTop;
		        if(scrolltop>50)
		        {
		            $(".top").show();
		        }
		         else{
		        	 $(".top").hide();
		         }
		    });

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

	if (shopMobile) {
		params.shopMobile = shopMobile;
	}

	xigou.activeUser.unionlogon({
		requestBody: params,
		showLoading: false,
		callback: function(response, status) { // 回调函数
			if (status == xigou.dictionary.success && response.code == 0) {
				// 登录成功
				if (response.data.promoterinfo && response.data.promoterinfo != "{}") {
					var promoterinfo = 	JSON.parse(response.data.promoterinfo);
					promoterinfo.token = response.data.token;
					xigou.setLocalStorage("dssUser", promoterinfo, true);
					setShopName(promoterinfo);
				}

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
			InitPage(dssUserMobile);
		}
	});
}

function InitPage(shopMobile){
	xigou.removeSessionStorage('productdetails_backurl');
	moduleInfo();
	$(".dropload-refresh").hide();
		shoplist(listCount, null, shopMobile);
	listCount++;

	$('.content').dropload({
		scrollArea : window,
		loadDownFn : function(me){
			me.resetload();
			shoplist(listCount,me,shopMobile);
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
/*
* <div class="shop">
 <span class="shop_logo"><img src="img/group_buying/shop_logo.png"></span>
 <div class="shop_info">
 <a class="shop_int" href="group_shop.html">
 <div class="shop_right"></div>
 <h3>韩尚宫料理</h3>
 <p class="shop_des">引爆杭城民众味蕾！</p>
 </a>
 <div class="shop_others shop_border">牛肉石锅拌饭<span class="price">￥42.00<del class="old_price">￥82.00</del></span></div>
 <div class="shop_others">牛肉石锅拌饭<span class="price">￥42.00<del class="old_price">￥82.00</del></span></div>
 </div>
 </div>
*
*
* */

		// 商家店铺
			if (2 == data.type){
			if(data.list && data.list.length > 0 ) {
				htmlData.push('');
			}
			else {
				htmlData.push('<div class="shop">');
			}

			htmlData.push('<span class="shop_logo"><img src="' + data.imgurl +'"></span>');
			htmlData.push('<div class="shop_info">');
			htmlData.push('<a class="shop_int" href="group_shop.html?id='+data.tid+' ">');
			htmlData.push(' <div class="shop_right"></div>');
			htmlData.push(' <h3>'+ data.name+'</h3>');
			htmlData.push('<p class="shop_des">'+data.topicpoint+'</p>	');
			htmlData.push('<div class="line"></div>');
			htmlData.push(' </a> ');
			//专题产品外露
			var oWidth = Math.round($(".content").width()*0.6);
			var oHeight = Math.round(oWidth+70);
			if (dssUserMobile  && showFlag) {
				oHeight += 15;
			}
			if(data.itemlist && data.itemlist.length > 0 ){
				var oList = data.itemlist;
				for(var j = 0;j<oList.length;j++){
					if(j>2) break;
					htmlData.push('<a href="group_detail.html?tid='+oList[j].tid+'&sku='+oList[j].sku+'">');
					htmlData.push( '<div class="shop_others shop_border">' +'<label>' + oList[j].name+'</label>' +'<span class="price" style="display: ">¥'+ oList[j].price+'<del class="old_price">¥'+oList[j].oldprice+'</del></span></div>');
					htmlData.push('</a>');
					}


				}

			}
			htmlData.push(' </div> </div>');
	});

	return htmlData;
}





$(function() {
    $(".dropload-refresh").hide();
    var dropload = $(".div_search_list").dropload({
        scrollArea: window,
        loadDownFn: function (me) {
            if (bMaxPage) {
                return;
            }
            me.resetload();
            CurPage++;
        }
    
    })
    
    switch (searchSortType) {
        case 1:
            $('.div_search_sort_item').removeClass('current_sort');
            $('#sortBySales').addClass('current_sort');
            break;
        case 2:
            $('.div_search_sort_item').removeClass('current_sort');
            $('#sortBySales').addClass('current_sort');
            break;
        case 3:
            $('.div_search_sort_item').removeClass('current_sort');
            $('#sortByPrice').addClass('current_sort');
            break;
        case 5:
            $('.div_search_sort_item').removeClass('current_sort');
            $('#sortByNew').addClass('current_sort');
            break;
    }

    window.onbeforeunload = function(){
        var url = [];
        if (searchSortType) {
            url.push('sort=' + searchSortType);
        }
        if (Key) {
            url.push('key=' + Key);
        }
        if (searchSortType) {
            url.push('sort=' + searchSortType);
        }
        if (categoryid) {
            url.push('categoryid=' + categoryid);
        }
        if (brandid) {
            url.push('brandid=' + brandid);
        }
        if (name) {
            url.push('name=' + name);
        }
        xigou.setSessionStorage('productdetails_backurl', 'search.html?' + url.join('&'));
    };
    onbeforeunload();
    $("form").submit(function(e){
        //e.preventDefault();
        return false;
    });

    $(".dropload-down").hide();
    selectSortType(dropload);
})

// 重新设置排序方式
function selectSortType(dropload) {
    $('.div_search_sort_item')[xigou.events.click](function(){
        var id = this.getAttribute("id");
        if (id == "sortBySales") {  
            if ($(this).hasClass('current_sort')) {
                    searchSortType = 1;
            }
            else {
                $('.div_search_sort_item').removeClass('current_sort');
                $(this).addClass('current_sort');
                searchSortType = 2;
            }
        }
        else {
            if ($(this).hasClass('current_sort')) {
                return;
            }

            $('.div_search_sort_item').removeClass('current_sort');
            $(this).addClass('current_sort');
            var id = this.getAttribute("id");
            if (id == 'sortByDefoule') {
                searchSortType = '';
            }
            else if (id == 'sortBySales') {
                searchSortType = 1;
            }
            else if (id == 'sortByNew') {
                searchSortType = 5;
            }
        }
        research(dropload);
    })
}

function research(dropload) {
    CurPage = 1;
    bMaxPage = false;
    $('.ul_search_list').empty();
    dropload.unlock();
    dropload.resetload();
    CurPage++;
}


function InitWeixin() {
	showLoading = false;
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
					'onMenuShareQZone',,
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
	$('.goShare').show();
	$('.goShare')[xigou.events.click](function(){
		$('.share_friends').show();
	})
	$('.share_friends')[xigou.events.click](function(){
		$('.share_friends').hide();
	})

	var lineLink = location.href.split('?')[0];


	var title ='西客商城线下团购';
	var desc = '引爆杭城味蕾!!!';

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
	xigou.loading.close();
	xigou.wxreadyalert();
});

wx.error(function (res) {
});
