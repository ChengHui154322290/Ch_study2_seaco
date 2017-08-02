var specialid = xigou.getQueryString('id'); //专场号
if(specialid.indexOf('#') > -1){
	specialid = specialid.substring(0,specialid.indexOf('#'));
}
//记录锚点,定位免税店首页
xigou.setSessionStorage("specialid",specialid);
var brandStory = ""; //品牌故事
var tmnotice = xigou.getQueryString('tmnotice');//明日预告过来的
var isTmrNotice = 0;
if(tmnotice!=''){
	isTmrNotice = 1;
}
var refreshComp;//刷新组件
tmnotice = tmnotice?"&tmnotice="+tmnotice:"";
var haitao = xigou.getQueryString('haitao');
var comefromAd = xigou.getSessionStorage("comeFromAd");
var isascending = "0";//“0”表示不参考价格因素“1”表示升序，价格从底到高“2”表示降序，价格从高到底

var title = '';
var desc = '';
var imgUrl = '';
var listCount = 1;
var showLoading = true;
var shopMobileVal = null;

$(function() {
	if (isWeiXin()) {
		InitWeixin();
		$("header").hide();
		$(".group-share1").show();
		$(".hd-tophtml").css({
			"margin-top":"0"
		})
	}else{
		$("header").show();
	}
		
	var dssUserInfo = xigou.getLocalStorage("dssUser");
	if(dssUserInfo && dssUserInfo != "{}"){
		var userInfo =JSON.parse(dssUserInfo);
		shopMobileVal = userInfo ?(userInfo.mobile && userInfo.token ?userInfo.mobile :null):null;
	}
	var dropload = $('.body').dropload({
		scrollArea : window,
		loadDownFn : function(me){
				product(listCount,isascending,me);

		}
	});

//	getDssUserInfo();
	details(dropload); //专场-详情
	xigou.setSessionStorage('productdetails_backurl', 'hd.htm?tid=' + specialid);

	if (xigou.getQueryString('from') == 'shop') {
		$('a.hd-back').attr("href", 'shop.html');
	}

	hotsale();
	Initkf();
	
	/*$("#tel").click(function(){
		$(".alert_body").show();
		$(".shop_alert").show();
	})
	$(".close").click(function(){
		$(".alert_body").hide();
		$(".shop_alert").hide();
	})*/
	
	//分享链接
	$(".group-share, .group-share1").click(function() {
		$('.share_friends').show();
	});
	$(".share_friends").click(function() {
		$('.share_friends').hide();
	});
	
});



//专场-详情
function details(dropload) {
	var params = {
		p: 'tid=' + specialid,
		showLoading : showLoading,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length == 0) return false;

				if (json.code == 0) {
					if(json.data.status && parseInt(json.data.status) == 0){
						window.location.href = "specialsaleend.html?specialname="+json.data.name;
						return;
					}


					title = json.data.name;
					desc = json.data.point;

					if(typeof(tophtml) != undefined)
					{
						$('.hd-tophtml').append(json.data.tophtml);
					}

					$('title').text(json.data.name);
					$("#shopname").text(json.data.shopName);
					$("#prestime").text(json.data.prestime);
					$("#tel").text(json.data.tel);
					$("#addr").text(json.data.addr);
					if($("#prestime").html() == null || $("#prestime").html() == ""){
						$("#prestime").hide();
					}
					if($("#addr").html() == null || $("#addr").html() == ""){
						$("#addr").hide();
					}
					if($("#tel").html() == null || $("#tel").html() == ""){
						$("#tel").hide();
					}
					$("#notice").text(json.data.notice==null ? '欢迎光临本店': json.data.notice);
					// if(json.data.notice == null || json.data.notice == "" || json.data.notice.length == 0){
					// 	$("#notice").css({
					// 		"display":"none"
					// 	});
					// }
					var htmlData = [];
					$('#headerImage').append(json.data.shareurl);
					//htmlData.push('<div id="headerImage">'+json.imageurl+'</div>');
					//htmlData.push('<ul class="hot_list"></ul>');
					htmlData.push('<div class="floor_top_filter">');
					htmlData.push('<div class="filter_box">');


					htmlData.push('	<div class="filter_box_right" style="width:44%">');

					htmlData.push('		<span class="filter_price" id="order_price">价格<u></u></span>');
					htmlData.push('	</div>');
					htmlData.push('</div>');
					htmlData.push('</div>');
					$(".tel").html(json.data.tel);
					$(".call").attr("href","tel:"+json.data.tel);
					$('#details').append(htmlData.join(''));
					$('.ui-imglazyload').imglazyload();
					$("#headerTitle").html(json.data.name); // 专场名称
					order_price(dropload);

					var el = $('.zhuangchang'),
						n = Date.parse(new Date()),
						s = $(el).attr('starttime'),
						e = $(el).attr('endtime');
					/*if(xigou.getQueryString('tmnotice') || (json.starttime-nowTime)>0  ){
					 getTimeRest(s, e, $(el),"tmnotice");
					 }else {
					 getTimeRest(s, e, $(el));
					 }*/
					if(parseInt(e-n)/1000/60/60/24<=7) {
						getTimeRest(n,e,$(el),true);
					}

					//专场
					if(window.location.href.indexOf("=6885")>-1||window.location.href.indexOf("=7498")>-1||window.location.href.indexOf("=7348")>-1||window.location.href.indexOf("=7707")>-1){
						xigou.setSessionStorage('comeFromAd','1');
						$(".floor_top_filter").addClass('hide');
						$(".products_list").addClass('hide');
					}

					var _script = $('script[src*="bokecc.com"]');
					if(_script && _script.length != 0){
						var sc = document.createElement("script");
						sc.src = _script[0].src;
						var _hImage =$("#headerImage")[0];
						_hImage.appendChild(sc);
						_script.remove();
					}

					var ImgItem = $('img')[0];
					if (ImgItem) {
						imgUrl = ImgItem.getAttribute('src');
					}
				}
			}
		}
	};
	if(haitao){
		xigou.activeSpecialsale.htdetails(params);
	}else{
		xigou.activeSpecialsale.details(params);
	}

};

//商品列表
var specialsale_max = "N";
function product(page, isascending, me) {
	//isascending说明
	//“0”表示不参考价格因素
	//“1”表示升序，价格从底到高
	//“2”表示降序，价格从高到底
	//默认为空
	if(me && specialsale_max == "Y") {
		//me.disable("down",false);
		//$('.ui-refresh-down').hide();
		//$('.ui-refresh-down>span:first-child').removeClass();
		if (me) {
			me.lock();
		}
		return false;
	}
	if (typeof(isascending) == 'undefined') isascending = '';
	var pa = [];
	pa.push('tid=' + specialid);
	pa.push('curpage=' + page);
	pa.push('sort=' + isascending);
	pa.push('shopmobile=' + shopMobileVal);
	pa.push("pagesize=20")
	var params = {
		p: pa.join('&'),
		showLoading : false,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length == 0) return false;

				if (json.code == 0) {
					//$(window).unbind("scroll");//移除scroll绑定的function
					//$('.ui-refresh-down').remove();
					var isLoad = false;
					if (status == xigou.dictionary.success) {
						var json = response || null;
						if (null == json || json.length == 0) return false;
						if (json.code == 0) {
							var htmlData = [],
								hothtmlData = [],
								data = json.data.list,
								flgs = false,
								_status = "",
								default_img = xigou.default_img;

							if(typeof(response.data) == "undefined" ||  response.data == "" || typeof(response.data.list) == "undefined" || response.data.list.length == 0)
							{
								me.lock();
								$(".dropload-down").hide();
							}
							else
							{
								for (var i = 0; i < data.length; i++) {

									var imageurl = data[i].imgurl || xigou.default_img;
									var tid = specialid, sku = data[i].sku;
									var href='item.htm?tid=' + tid + '&sku' + sku;

									var Yan = "00", Fen = "00";
									var CHARS = data[i].price.split('.');
									if (CHARS.length > 0) {
										Yan = CHARS[0];
										if (CHARS.length > 1) {
											Fen = CHARS[1];
										}
									}

									htmlData.push('<div class="shop_other">');
									htmlData.push('		<a href="group_detail.html?tid=' + tid +'&sku=' + sku + '">');
									htmlData.push('		<span class="shop_logo"><img src="'+ imageurl +'"></span>');
									htmlData.push('	<div class="goods">');
									htmlData.push('<div class="goods_name">'+data[i].name+'</div>');
									htmlData.push('<div class="price">¥'+ data[i].price+'<del class="old_price">¥'+ data[i].oldprice+'</del><span class="sale">'+ data[i].salescountdesc+'</span></div>');
									htmlData.push('</div>');
									htmlData.push('		</a>');
									htmlData.push('</div>');
								}

								if (page == 1) $("#shop_products").empty();
								$("#shop_products").append(htmlData.join(" "));

								// //保持图片高度一致
								// var _imgWidth = $('#hd-products-id>li img').width();
								// $('#hd-products-id>li img').height(_imgWidth);

								$(".product_item")[xigou.events.click](function() {
									//var l_specialid = $(this).attr("specialid");
									//var l_productid = $(this).attr("productid");
									//xigou.setSessionStorage('productdetails_backurl', "ssale.html?sid=" + specialid);
									//window.location.href = "item.htm?sid=" + l_specialid + "&pid=" + l_productid+tmnotice;
								});

								$('.ui-imglazyload').imglazyload();

								$('.store_timer').each(function(index, el) {
									var s = $(el).attr('starttime'),
										e = $(el).attr('endtime');
									getTimeRest(s, e, $(el));
								});

								if (json.data.list.length > 0) {
									$('.ui-refresh-down').show();
									flgs = true;
								}

								if(json.data.totalpagecount == page) {
									specialsale_max = "Y";
								}




								if(shopMobileVal){
									$(".fy_price").show();
								}
							}
						}
						else
						{
							me.lock();
							$(".dropload-down").hide();
						}
					}
				}
			}
		}
	};
	xigou.activeSpecialsale.product(params);
};

function  hotsale() {
	var params={
		callback: function (response, status) {
			if(status != xigou.dictionary.success) return false;
			var json = response || null;
			if (null == json || json.length == 0) return false;

			if (json.code != 0) return false;
            var data = json.data;
            if(typeof(data) == "undefined" ||  data == "" || typeof(data) == "undefined" || data.length == 0) return;
            var hotHtml = [];
            for(var i = 0 ;i< data.length; i++){
                hotHtml.push(' <div class="like_other">');
                hotHtml.push('		<a href="group_detail.html?tid=' + data[i].tid +'&sku=' + data[i].sku + '">');
                hotHtml.push(' <span class="shop_logo"><img src="'+ data[i].imgurl+'"></span>');
                hotHtml.push('  <div class="like_goods">');
                hotHtml.push(' <div class="like_name">'+data[i].name+'</div>');
                hotHtml.push('<div class="like_price">¥'+data[i].price+'<del class="old_price">¥'+data[i].oldprice+'</del></div>');
                hotHtml.push('<div class="like_shop">'+ data[i].shopname+'<span class="sale">'+data[i].salescountdesc+'</span></div>');
                hotHtml.push(' </div>');
                hotHtml.push('</a>');
                hotHtml.push(' </div>');
            }

           $("#hotsales").empty();
            $("#hotsales").append(hotHtml.join(" "));
		}
	};
xigou.activeOfflinegb.hotsale(params);

};

//价格排序
function order_price(dropload) {
	$('.div_sort')[xigou.events.click](function() {
		//isascending
		//“0”表示不参考价格因素
		//“1”表示升序，价格从底到高
		//“2”表示降序，价格从高到底
		//默认为空
		switch (isascending) {
			case "0":
				isascending = "1";
				$(this).addClass("div_sort_down");
				$(this).removeClass("div_sort_up");
				break;
			case "1":
				isascending = "2";
				$(this).removeClass("div_sort_down");
				$(this).addClass("div_sort_up");
				break;
			case "2":
				isascending = "0";
				$(this).removeClass("div_sort_down");
				$(this).removeClass("div_sort_up");
				break;
			default:
				break;
		}
		specialsale_max = "N";
		listCount = 1;
		$("#hd-products-id").empty();
		dropload.unlock();
		dropload.resetload();
		product(listCount++, isascending, dropload);
	});
};


var shopOwer = null;
function getDssUserInfo(){

}

function InitWeixin(){
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

	var tid = xigou.getQueryString('id');
	var lineLink = location.href.split('?')[0] + '?id=' + tid;

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


// 初始化客服
function Initkf(){
	var name = xigou.getLocalStorage("show_name")|| xigou.getLocalStorage("login_name") || "西客会员";
	if (name) {
		var oScript= document.createElement("script");
		oScript.type = "text/javascript";
		oScript.src='https://qiyukf.com/script/6e39dddabff63d902f55df3961c2793d.js?name=' + name + '&mobile=' + name;
		$('body')[0].appendChild(oScript);
	}

	$('header').append('<div class="kf_btn" onclick="ysf.open();return false;"></div>');
}