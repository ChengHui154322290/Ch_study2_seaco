var desc = "";

$(function(){
	if (isWeiXin()) {
		$("header").hide();
		$(".group-share1").show();
		$(".goods").css({
			"margin-top":"0"
		});
		InitWeixin();
	}else{
		$("header").show();
	}
	
	//分享链接
	$(".group-share, .group-share1").click(function() {
		$('.share_friends').show();
	});
	$(".share_friends").click(function() {
		$('.share_friends').hide();
	});
	
	/*$("#tel").click(function(){
		$(".alert_body").show();
		$(".shop_alert").show();
	})
	$(".close").click(function(){
		$(".alert_body").hide();
		$(".shop_alert").hide();
	})*/



	details();
	product();
	hotsale();
	buy();
	
	var beforeUrl=document.referrer;
	if(beforeUrl.indexOf("51seaco.com")>0){
		$(".group-back").attr("href", 'javascript:history.go(-1);');
	}else{
		$(".group-back").attr("href", 'index.html');
	}
})
var showLoading = true;
var specialid = xigou.getQueryString('tid'); //专场(编号)ID
var productid = xigou.getQueryString('sku'); //商品ID
var limitcount_p=1;
var imgUrl = '';
function details() {
	var shopMobile = null;
	if(xigou.getLocalStorage("dssUser")){
		var dssUser =JSON.parse(xigou.getLocalStorage("dssUser"));
		var shopMobile = dssUser ?(dssUser.mobile && dssUser.token ?dssUser.mobile :null):null;
	}
	var params = {
		p: 'sku=' + productid + '&tid=' + specialid ,
		showLoading : showLoading,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length == 0) return false;
				if (json.code == 0) {

					window._detailsData=json;//使用全局变量存储数据,方便调用

/*					var imageurl = json.data.imglist; //商品效果图,返回数组
					imgUrl = imageurl[0] || "";
*/
					var price = isNaNDefaultInt(json.data.price); //优惠后价格
					var oldprice = isNaNDefaultInt(json.data.oldprice); //原价
					
					//传出itemType值
					xigou.setSessionStorage("itemType",json.data.itemType);//商品类型
					
					if(json.data.name == "undefined"){
						json.data.name = "";
					}
					var name = json.data.name; //商品名称
					if(json.data.feature == "undefined"){
						json.data.feature = "";
					}
					var feature = json.data.feature; //特色
					var specs = json.data.specs; //规格（包括颜色，尺寸等），返回数组
					var skulist = json.data.skus; //规格对应sku数值
					var count = json.data.stock; //库存数量

					var restrictcount = json.data.limitcount;//限购数量;
					var status = json.data.status;//商品状态

					$(".item_shop_opt").cartshopopt(function(val, num, e) {
						if (val>= restrictcount){
							$.tips({
								content: "限购 "+restrictcount+"张",
								stayTime:2000,
								type:"info"
							})
						}
					});

					desc = feature;

					if(!json.data.shopName || json.data.shopName.length == 0){
						$('.shop_information').hide();
					}else{
						$('.shop_information').show();
					}
					$('.goods_name').html(name);
					$('.price').html("¥"+price);
					$('#op').html("¥"+oldprice);
					$('title').text(json.data.name);
					$('.sale').html(json.data.salescountdesc);
					$('#list_name').html(name);
					$('.list_num').html(1+json.data.unit);
					$('.list_price').html("¥"+price);
					$('#logo').attr('src',json.data.logoPath);
					$('.shop_info_name').text(json.data.shopName);
					$('#prestime').text(json.data.prestime);
					$('#shop_locals').text(json.data.addr);
					$('#itemimg').attr("src",json.data.imgurl);
					$('.tel').html(json.data.tel);
					$(".call").attr("href","tel:" +json.data.tel);
					$(".click_enter").attr("href","group_shop.html?id=" + json.data.tid);
					var limitcount = json.data.limitcount;
					xigou.setSessionStorage("ShopInfoName", json.data.shopName);
					imgUrl = json.data.imgurl;

					if(json.data.dummyattr != null && json.data.dummyattr.length>0){
						var dumHtml = [];
						var darray = json.data.dummyattr;
						for(var i = 0;i<darray.length; i++){
							dumHtml.push("<li>");
							dumHtml.push("<ol>");
							dumHtml.push('<li>【');
							dumHtml.push(darray[i].name);
							dumHtml.push('】</li>');
							if(darray[i].cols !=null && darray[i].cols .length>0){
								for(var j = 0 ;j<darray[i].cols.length; j++){
									dumHtml.push('<li class="even">-');
									dumHtml.push(darray[i].cols[j]);
									dumHtml.push('</li>');

								}
							}
							dumHtml.push('</ol>');
							dumHtml.push('</li>');
						}
						 $("#tips").empty();
						 $("#tips").append(dumHtml.join(" "));
					}

					//价格、数量等等
					var h1 = [];
					var h2 = [];
					h2.push('<h6>' + name + '</h6>');
					h2.push('<p>' + feature + '</p>');
					$('#content').empty().html(h2.join(''));

					//规格（包括颜色，尺寸等）




					//加减
					if(restrictcount){
						$('#quantity').attr('maxNum', restrictcount);
						$('#restrictcount').removeClass('hide').find('span').html(restrictcount);
					}

					//ONLY 只展示立即购买 BOTH展示立即购买和加入购物车

					if(status == 1){
						var purchasepage = json.data.purchasepage;
						if(purchasepage == "ONLY"){
							$('.item-addshop').hide();
							//		$(".item-addshop").hide();
							//		$(".item-buy").show();
							//		$(".item-buy").css({width:"80%"});
							//		$(".item-buy").css({width:"100%"});
							//	}else if(purchasepage == "BOTH"){
							//		$(".item-addshop").show();
							//		$(".item-buy").show();
							//	}else{
							//		$(".item-buy").hide();
							//		$(".item-addshop").css({width:"80%"});
							//		$(".item-addshop").css({width:"100%"});
						}
					}

					if(restrictcount=="") {
						$('#quantity').attr('maxNum', (count > 99 ? 99 : count));
					}

					//已抢光
					if(status == "2"){
						$(".item-shopcar").hide();
						$(".item-buy").hide();
						$(".item-none").show();
						$(".item-none").text("已抢光");
					}
					//已下架
					if(status == "3"){
						$(".item-shopcar").hide();
						$(".item-buy").hide();
						$(".item-none").show();
						$(".item-none").text("已下架");
					}
					//已作废
					if(status == "4"){
						$(".item-shopcar").hide();
						$(".item-buy").hide();
						$(".item-none").show();
						$(".item-none").text("已作废");
					}
					// 活动未开始
					if(status == "5"){
						$(".item-shopcar").hide();
						$(".item-buy").hide();
						$(".item-none").show();
						$(".item-none").text('活动未开始');
					}
					// 活动已结束
					if(status == "6"){
						$(".item-shopcar").hide();
						$(".item-buy").hide();
						$(".item-none").show();
						$(".item-none").text('活动已结束');
					}

					//显示footer
					$('div.footer').removeClass('hide');

					var size_color = "";
					$("#parameters_norm li[class*=active]").each(function(index, element) {
						size_color += $(this).text() + " ";
					});
					size_color += "数量"+ $('.quantity_txt').text();
					$("#size_color").text(size_color);

					//记录保持size_color状态
					var havesku = xigou.getSessionStorage('havesku');
					if(havesku == "1") {
						$("#mask").show();
						$("#productModal").show();
						var _height = $("#productModal").height() - 27;
						$(".p_close").show().css({
							bottom:_height+"px"
						});
						xigou.setSessionStorage('havesku',0);
					}

					//
					if (0 == count) {
						$(".car").hide();
						$(".no_item").show();
					}

					if (!json.data.tags || json.data.tags.length == 0) {
						$('.div_promotion').hide();
					}
					else {
						$('.div_promotion').show();
						var htmlData = [];
						for (var i = 0; i < json.data.tags.length; i++) {
							var tagItem = json.data.tags[i];
							if (tagItem && tagItem.tag) {
								var fontcolor = tagItem.fontcolor || '#ffffff';
								var bgcolor = tagItem.bgcolor || '#f57584';
								var tag = tagItem.tag || " ";
							}
							htmlData.push('<span style="color: ' + fontcolor + ';background-color: ' + bgcolor + '">' + tag + '</span>');
						}
						$('.div_promotion_desc').empty();
						$('.div_promotion_desc').append(htmlData.join(' '));
					}
				}
			}
		}
	};

	xigou.activeProduct.details(params);
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
				hotHtml.push('<div class="like_others">');
				hotHtml.push('		<a href="group_detail.html?tid=' + data[i].tid +'&sku=' + data[i].sku + '">');
				hotHtml.push(' <span class="shop_other_logo"><img src="'+ data[i].imgurl+'"></span>');
				hotHtml.push('  <div class="like_goods">');
				hotHtml.push(' <div class="like_name">'+data[i].name+'</div>');
				hotHtml.push('<div class="like_price">¥'+data[i].price+'<del class="shop_other_old_price">¥'+data[i].oldprice+'</del></div>');
				hotHtml.push('<div class="like_shop">'+ data[i].shopname+'<span class="shop_other_sale">'+data[i].salescountdesc+'</span></div>');
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


function product( ) {

	var pa = [];
	pa.push('tid=' + specialid);
	pa.push('curpage=1' );
	pa.push('sort=0'  );
	pa.push('shopmobile=' );
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
								var count = 0;
								if (data.length ==1) {
									$(".shop_other_group").hide();
									return;
								}						
								for (var i = 0; i < data.length; i++) {
									
									if(productid == data[i].sku) continue;
									 count++;
									if(count>3) break;

									var imageurl = data[i].imgurl || xigou.default_img;
									var tid = specialid, sku = data[i].sku;
									var href='item.html?tid=' + tid + '&sku' + sku;

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
									htmlData.push('		<span class="shop_other_logo"><img src="'+ imageurl +'"></span>');
									htmlData.push('	<div class="shop_other_goods">');
									htmlData.push('<div class="shop_other_name">'+data[i].name+'</div>');
									htmlData.push('<div class="shop_other_price">¥'+ data[i].price+'<del class="shop_other_old_price">¥'+ data[i].oldprice+'</del><span class="shop_other_sale">'+ data[i].salescountdesc+'</span></div>');
									htmlData.push('</div>');
									htmlData.push('		</a>');
									htmlData.push('</div>');
								}

								 $("#shop_products").empty();
								$("#shop_products").append(htmlData.join(" "));

								$('.ui-imglazyload').imglazyload();



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

function  buy() {


$("#shopcart-settle")[xigou.events.click](function() {
	xigou.removeClearingData();
	var sku = productid;
	//检验规格选项是否符合
	if ($("#parameters_norm li").length > 0) {
		var selKey = "";
		$("#parameters_norm li[class*=active]").each(function(index, element) {
			selKey = selKey + "~" + $(this).parent().attr("groupid") + "_" + $(this).attr("specid");
		});
		if (m_skulistObj[selKey] == undefined) {
			//alert("请选择正确的规格！");
			var dia=$.dialog({
		        title:'',
		        content:"请选择正确的规格？",
		         button:["确认"]
		    });

		    dia.on("dialog:action",function(eDlg){
		    	if(0 == eDlg.index)
		    	{
		    		this.close();
					this.destroy(); 
		    	}
		    });
			return;
		}
		sku = m_skulistObj[selKey].sku;
	}
	var addressData = xigou.getLocalStorage("address", true) || xigou.defaultAddress;
	//商品信息-添加购物信息
	var params = {
		'token': xigou.getLocalStorage('token'),
		'tid': specialid,
		'sku': sku,
		'count': $('.quantity_txt').html(),
		'shopMobile': xigou.getLocalStorage("dssUser") == "" ? null : xigou.getLocalStorage("dssUser", true).mobile
	};

	xigou.setSessionStorage("buy_now_details_url",window.location.href);

	//if(xigou.getSessionStorage("userinfo")==""){//没有登录跳到 登录页面,保存提交的数据
	//	xigou.setSessionStorage("buy_now_refer_data", params, true);
	//	xigou.setSessionStorage("buy_now_tun_type",tuntype);
	//	//加入购物车登录成功后，直接到结算页面
	//	xigou.setSessionStorage("refer", "home.html", false);
	//	window.location.href="login.html?backurl="+window.encodeURIComponent(window.location.href);//
	//
	//	return;
	//}
	//跳转登录界面
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status!=xigou.dictionary.success){
				/*xigou.setSessionStorage("refer_data", params, true);
				//加入购物车登录成功后，直接到结算页面
				xigou.setSessionStorage("refer", "home.html", false);*/
				window.location.href="logon.html?backurl="+window.encodeURIComponent(window.location.href);//

				return;
			}
		}
	});

	xigou.activeProduct.buynow({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length == 0) return false;
				if (json.code == 0) {
					xigou.setSessionStorage("buy_now_uuid", json.data.uuid);
					window.location.href="settle.html";
				} else {
					$.tips({
						content:json.msg || "立即购买操作失败",
						stayTime:2000,
						type:"info"
					})
				}
			} else {
				$.tips({
					content:'请求失败，详见' + response,
					stayTime:2000,
					type:"info"
				})
			}
		}
	});
});

};


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
	var tid = xigou.getQueryString('tid');
	var sku = xigou.getQueryString('sku');
	lineLink = lineLink + '?tid=' + tid + '&sku='+sku+'&img='+imgUrl;

	var title =$('.goods_name')[0].innerText || '';

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
