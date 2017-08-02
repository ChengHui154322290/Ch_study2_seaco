var isChoice = false;     //判断规格是否选择
var isSpecs = false;

var specialSku = xigou.getQueryString('sku'); //专场号
if(specialSku.indexOf('#') > -1){
	specialSku = specialSku.substring(0,specialSku.indexOf('#'));
}
//记录锚点,定位免税店首页
xigou.setSessionStorage("specialSku",specialSku);
var tmnotice=xigou.getQueryString('tmnotice');//明日预告过来的
var imagethreeurl;//商品详情
var tuntype;//是否是免税店商品0,1
var comefromAd = xigou.getSessionStorage("comeFromAd");
var imgUrl = "";
var showLoading = true;
$(function() {
	var from = xigou.getQueryString('from');
	var isappinstalled = xigou.getQueryString('isappinstalled');
	if (from || isappinstalled) {
		var tid = xigou.getQueryString('tid');
		var shop = xigou.getQueryString("shop");
		var link = 'item.htm?tid=' + tid + '&sku=' + specialSku;
		if (shop) {
			link += '&shop=' + shop;
		}
		window.location.href = link;
		return;
	}

	if (isWeiXin()) {
		InitWeixin();
		$("header").hide();
		$(".content").css({
			"margin-top":"0px"
		})
		$(".header_wx").show();
		$('.goShare_wx')[xigou.events.click](function(){
			$('.share_friends').show();
		})
		$('.share_friends')[xigou.events.click](function(){
			$('.share_friends').hide();
		})
		$('title').text("商品详情");
	}
	

	//设置返回路径
	if (backurl) {
		$("a.back").attr("href", backurl);
		console.log($("a.goBack").attr("href"));
	} else {
		backurl = xigou.getSessionStorage('productdetails_backurl') || "index.html";
		if (backurl) {
			$(".goBack").attr("href", backurl);
		}
		console.log($("a.goBack").attr("href"));
	}

	details();
	//getstoreid();   待确认
	add_shopping_cart(); //加入购物车
	getshopcount(); //购物车-美图和海囤数量

	$("#pdlist")[xigou.events.click](function(){
		$("#arrow_state",this).toggleClass("down-arrow").toggleClass("up-arrow");
		//$("#imagethreeurl").toggleClass("imagethree");
		$('#imagethreeurl').html(imagethreeurl);

		//=====start
		var _script = $('script[src*="bokecc.com"]');
		if(_script && _script.length != 0){
			var sc = document.createElement("script");
			sc.src = _script[0].src;
			var _hImage =$("#imagethreeurl")[0];
			_hImage.appendChild(sc);
			_script.remove();
		}
		//=====end.

		var imagethreeurlDom = $("#imagethreeurl")[0];
		if(imagethreeurlDom.style.display == "block") {
			imagethreeurlDom.style.display = "none";
			$(".pull-up-detail").show();
		}else {
			imagethreeurlDom.style.display = "block";
		}
	});

	$(".pull-up-detail")[xigou.events.click](function(){
		$('#imagethreeurl').html(imagethreeurl);
		//=====start
		var _script = $('script[src*="bokecc.com"]');
		if(_script && _script.length != 0){
			var sc = document.createElement("script");
			sc.src = _script[0].src;
			var _hImage =$("#imagethreeurl")[0];
			_hImage.appendChild(sc);
			_script.remove();
		}
		//=====end.

		$("#imagethreeurl").show();
		$("#arrow_state").removeClass("down-arrow").addClass("up-arrow");
		$(".pull-up-detail").hide();
	});
	
	$("#selectSizeColor")[xigou.events.click](function() {
		ChoiceSpecifications();
	});
	
	$(".div_desc")[xigou.events.click](function() {
		$("#mask").show();
		$("body").attr("scroll","no");
		$("body").css("overflow-y","hidden");
		$("#detailsDescription").show();
		var _height = $("#detailsDescription").height() - 27;
		$(".p_close").show().css({
			bottom:_height+"px"
		});
	});
	
	$("#btn_confirm")[xigou.events.click](function() {
		if(isChoice == false){
			$("#productModal").hide();
			$("#mask").hide();		
			$("#taxationModal").hide();
			$(".p_close").hide();
			
			var size_color = "";
			$("#parameters_norm li[class*=active]").each(function(index, element) {
				size_color += $(this).text() + " ";
			});
			size_color += "数量"+ $('.quantity_txt').text();
			$("#size_color").text(size_color);
			isChoice = true;
		}
		var isAddshop = xigou.getSessionStorage("isAddshop");
		if(isAddshop && isAddshop == 1){
			Addshop();
			xigou.removeSessionStorage("isAddshop");
			$("#productModal").hide();
			$("#mask").hide();		
			$("#taxationModal").hide();
			$(".p_close").hide();
			
			var size_color = "";
			$("#parameters_norm li[class*=active]").each(function(index, element) {
				size_color += $(this).text() + " ";
			});
			size_color += "数量"+ $('.quantity_txt').text();
			$("#size_color").text(size_color);
		}
		var isBuy = xigou.getSessionStorage("isBuy");
		if(isBuy && isBuy == 1){
			Buy();
			xigou.removeSessionStorage("isBuy");
		}
	});
	
	$(".p_close")[xigou.events.click](function() {
		$("#productModal").hide();
		$("#mask").hide();
		$("body").attr("scroll","yes");
		$("body").css("overflow-y","visible");
		$("#taxationModal").hide();
		$("#detailsDescription").hide();
		$(".p_close").hide();
		
		var size_color = "";
		$("#parameters_norm li[class*=active]").each(function(index, element) {
			size_color += $(this).text() + " ";
		});
		size_color += "数量"+ $('.quantity_txt').text();
		$("#size_color").text(size_color);
		
		var isAddshop = xigou.getSessionStorage("isAddshop");
		if(isAddshop && isAddshop == 1){
			xigou.removeSessionStorage("isAddshop");
			
			$("#productModal").hide();
			$("#mask").hide();		
			$("#taxationModal").hide();
			$(".p_close").hide();
			
			var size_color = "";
			$("#parameters_norm li[class*=active]").each(function(index, element) {
				size_color += $(this).text() + " ";
			});
			size_color += "数量"+ $('.quantity_txt').text();
			$("#size_color").text(size_color);
		}
		var isBuy = xigou.getSessionStorage("isBuy");
		if(isBuy && isBuy == 1){
			xigou.removeSessionStorage("isBuy");
		}
		
		isChoice = false;
	});
	
	if(size_color <= 1 ){
		$(".quantity_decrease").css({
			"color":"#000"
		})
	}
	
	$(".fee,.fee-icon")[xigou.events.click](function(){
		$("#mask").show();
		$("#taxationModal").show();
		var _height = $("#taxationModal").height() - 27;
		$(".p_close").show().css({
			bottom:_height+"px"
		});

	});
	
	getDssUserInfo();
	// 民生银行
	Initkf();
	showItemDetail();
});

var specialid = xigou.getQueryString('tid'); //专场(编号)ID
var productid = xigou.getQueryString('sku'); //商品ID


if(productid.indexOf('#') > -1){
	productid = productid.substring(0,productid.indexOf('#'));
}
//记录锚点,定位专场
xigou.setSessionStorage("productid",productid);

var backurl = xigou.getQueryString('url'); //返回地址
// 获取店铺主人信息

var shopOwer = null;
function getDssUserInfo(){
	var dssUser = xigou.getLocalStorage("dssUser");
	if (dssUser && dssUser != "{}") {
		dssUser = JSON.parse(dssUser);
		if (dssUser.token && dssUser.token == xigou.getToken()) {
			shopOwer = dssUser.mobile || dssUser.shopmobile;
			return;
		}
		else if (!xigou.getQueryString("shop")) {
			shopOwer = dssUser.mobile || dssUser.shopmobile;
			return;
		}
	}

	xigou.getDssUserInfo({
		requestBody: {
			'shop': xigou.getQueryString("shop"),
			'token':xigou.getToken(),
			'priority':0
		},
		callback: function(response, status) {
			if (status == xigou.dictionary.success) {
				if (response != null && response.mobile) {
					shopOwer = response.mobile;
					if (parseInt(response.source) == 0) {
						response.token = xigou.getToken();
					}
					else {
						response.shop = xigou.getQueryString("shop");
					}
					if(xigou.getLocalStorage("dssUser", true) == null || xigou.getLocalStorage("dssUser", true) == "" || xigou.getLocalStorage("dssUser", true) == "{}" || xigou.getLocalStorage("dssUser", response, true).mobile != response.mobile)
						xigou.setLocalStorage("dssUser", response, true);
				}
			}
		}
	});
}
//加入购物车
function add_shopping_cart() {
	if(tmnotice){//明日预告过来的
		$(".item-addshop-text").text("开团提醒")[xigou.events.click](function() {
			openGroup();
		});
		$(".item-buy").hide();
		$(".item-addshop").css({width:"80%"});
		$(".item-addshop").css({width:"100%"});
		return;
	}

	$(".item-addshop-text")[xigou.events.click](function() {
		if($(".item-addshop-text").attr("disabled") == "disabled")
		{
			return;
		}
		if(isChoice == false && isSpecs == true){
			ChoiceSpecifications();
			xigou.setSessionStorage("isAddshop",1);
			isChoice = true;
		}else{
			Addshop();
		}
	});
	

	$(".item-buy")[xigou.events.click](function() {
		if(isChoice == false && isSpecs == true){
			ChoiceSpecifications();
			xigou.setSessionStorage("isBuy",1);
			isChoice = true;
		}else{
			Buy();
		}
	});
	
};

//立即结算
function clearing() {
	$('#clearing')[xigou.events.click](function() {
		if(tuntype == "1"){
			window.location.href = "cart.html?tuntype=1";
		}else {
			window.location.href = "cart.html";
		}
	});
};

var m_skulistObj = {};
//商品信息-商品信息
function details() {
	var shopMobile = null;
	if(xigou.getLocalStorage("dssUser")){
		var dssUser =JSON.parse(xigou.getLocalStorage("dssUser"));
		var shopMobile = dssUser ?(dssUser.mobile && dssUser.token ?dssUser.mobile :null):null;
	}
	var params = {
		p: 'sku=' + productid + '&tid=' + specialid + '&shopmobile=' + shopMobile,
		showLoading : showLoading,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length == 0) return false;
				if (json.code == 0) {

					window._detailsData=json;//使用全局变量存储数据,方便调用

					var imageurl = json.data.imglist; //商品效果图,返回数组
					imgUrl = imageurl[0] || "";

					var price = isNaNDefaultInt(json.data.price); //优惠后价格
					var oldprice = isNaNDefaultInt(json.data.oldprice); //原价
					var commision = json.data.commision; //返佣
					var discount = xigou.subDiscount(price, oldprice); //折扣
					if(json.data.name == "undefined"){
						json.data.name = "";
					}
					var name = json.data.name; //商品名称
					if(json.data.feature == "undefined"){
						json.data.feature = "";
					}

					var sp = json.data.salespattern;
					if(sp== 9){
						window.location.href="group_detail.html?tid="+specialid+"&sku="+productid;
					}

					var feature = json.data.feature; //特色
					var specs = json.data.specs; //规格（包括颜色，尺寸等），返回数组
					var skulist = json.data.skus; //规格对应sku数值
					var count = json.data.stock; //库存数量

					var restrictcount = json.data.limitcount;//限购数量;
					var status = json.data.status;//商品状态
					var countryimagepath = json.data.countryimg;//国家图片;

					//商品图片
					xigou.setLocalStorage("itemdetail",json.data.detail);
					xigou.setSessionStorage("itemType",json.data.itemType);//商品类型
					$('.detail-content').html(json.data.detail);
					slider_show(imageurl);

					if (json.data.taxdesc) {
						$("#tax_desc_span")[0].innerHTML = json.data.taxdesc;
					}
					if (json.data.taxfee) {
						$(".tax_arr")[0].innerHTML = '¥'+json.data.taxfee;
					}
					if(!json.data.shopName || json.data.shopName.length == 0){
						$('.shop_information').hide();
					}else{
						$('.shop_information').show();
					}
					
					$('#img_Logo').attr('src',json.data.logoPath);//店铺logo
					$(".shop_name").html(json.data.shopName); // 店铺名称
					$("#shop_information").attr("href","shop_detail.html?tid=" + json.data.tid);
					$("#into_shop").attr("href","shop_detail.html?tid=" + json.data.tid);
					// 商品名称  标题 等等
					$(".dec-title")[0].innerHTML = name;
					$(".dec-title")[0].innerHTML = name;
					$(".dec-info")[0].innerHTML = feature;
					$("#contry-img").attr("src",json.data.countryimg);
					if (json.data.channel) {
						$(".warehouse")[0].innerHTML = json.data.channel;
					}
					if (json.data.countryname) {
						$(".country-name")[0].innerHTML = json.data.countryname;
					}

					if (json.data.salescountdesc) {
						$(".sale-count")[0].innerHTML = json.data.salescountdesc;
					}
					if(commision){
						$("#fy_price").show().find("span").html('¥&nbsp;'+commision);
					}

					SetPrice(price);

					//价格、数量等等
					var h1 = [];
					var h2 = [];
					h2.push('<h6>' + name + '</h6>');
					h2.push('<p>' + feature + '</p>');
					$('#content').empty().html(h2.join(''));
					
					//弹窗产品图
					var product_info = [];
					product_info.push('<div class="cart_product"> ');
					if(imageurl != null){
						product_info.push('<img src="'+imageurl[0]+'" alt=""> ');
					}
					product_info.push('</div> ');
					product_info.push('<div class="cart_name_opt" style="width:65%;">');
					product_info.push('<div class="cart_product_name">'+name+'</div> ');
					product_info.push('<div class="price_box" style="left: 35%;"> ');
					var itemPrice = GetPriceHtml(price, 'iem-price1', 'iem-price2');
					product_info.push(itemPrice);
					// product_info.push('<span class="money"><em>¥</em><strong>'+price+'</strong></span>');
					product_info.push('    <del><span>&nbsp;&nbsp;</span>¥' + oldprice + '</del>');
					/*product_info.push('<span class="cheap">特价</span>');
					if(discount)product_info.push('    <i class="discount">' + discount + '</i>');*/
					product_info.push('<i class="postinfo">请挑选合适的 颜色 尺寸</i>');
					product_info.push('</div> ');
					product_info.push('</div> ');
	
					$('.car_item_core').empty().append(product_info.join(''));

					//规格（包括颜色，尺寸等）
					var h3 = [];
					if(specs && specs != ""){
						isSpecs = true;
						for (var i = 0; i < specs.length; i++) {
							var item = specs[i],
								groupid = item.groupid,
								groupname = item.groupname || "",
								groupdetails = item.groupdetails;

							h3.push('<div class="parameters_box">');
							h3.push('<h2>' + groupname + '</h2>');
							h3.push('<ul groupid="' + groupid + '" id="ul_group">');
							for (var j = 0; j < groupdetails.length; j++) {
								//h3.push('<li specid="' + groupdetails[j].specid + '"><a href="'+xigou.changeURLPar(window.location.href,'productid', 11) +'">' + groupdetails[j].specname + '</a></li>')
								h3.push('<li specid="' + groupdetails[j].specid + '" id="li_spec">' + groupdetails[j].specname + '</li>');
							};
							h3.push('</ul>');
							h3.push('</div>');
						};
					}
					$('#parameters_norm').empty().append(h3.join(''));
					$('#old_price').text('¥' + oldprice);

					m_skulistObj = {};
					var skudetails = [];
					var skukey = "";
					if(skulist != null && skulist != "undefined"){
						for (var i = 0; i < skulist.length; i++) {
							skukey = "";
							skudetails = skulist[i].skudetails;
							for (var j = 0; j < skudetails.length; j++) {
								skukey = skukey + "~" + skudetails[j].groupid + "_" + skudetails[j].specid;
							}
							m_skulistObj[skukey] = skulist[i];
							m_skulistObj[skukey].key = skukey;
						}
					}
                    //默认选项
                    defaultOption();
					//规格（包括颜色，尺寸等） 添加事件+事件处理
					parametersNormSelClick();

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
						$(".parameters_box").hide();
						$(".item-addshop").hide();
						$(".item-buy").hide();
						$(".item-none").show();
						$(".item-none").text("已抢光");
					}
					//已下架
					if(status == "3"){
						$(".parameters_box").hide();
						$(".item-addshop").hide();
						$(".item-buy").hide();
						$(".item-none").show();
						$(".item-none").text("已下架");
					}
					//已作废
					if(status == "4"){
						$(".parameters_box").hide();
						$(".item-addshop").hide();
						$(".item-buy").hide();
						$(".item-none").show();
						$(".item-none").text("已作废");
					}
					// 活动未开始
					if(status == "5"){
						$(".parameters_box").hide();
						$(".item-addshop").hide();
						$(".item-buy").hide();
						$(".item-none").show();
						$(".item-none").text('活动未开始');
					}
					// 活动已结束
					if(status == "6"){
						$(".parameters_box").hide();
						$(".item-addshop").hide();
						$(".item-buy").hide();
						$(".item-none").show();
						$(".item-none").text('活动已结束');
					}
					// 活动已结束
					if(status == "7"){
						$(".parameters_box").hide();
						$(".item-addshop").hide();
						$(".item-buy").hide();
						$(".item-none").show();
						$(".item-none").text('备货中');
					}
					//数量加减cart_shop_opt	
					$("#quantity").cartshopopt(function() {});
					
					//修改title
					if(!isWeiXin()){
						document.title = name + "-西客商城";
					}else{
						document.title =  "商品详情";
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

//规格（包括颜色，尺寸等） 添加事件+事件处理
function parametersNormSelClick() {
	$('#parameters_norm li')[xigou.events.click](function() {
		var me = $(this);
		//disabled情况下退出事件，不处理
		if (me.hasClass("disabled")) return;

		//当前选择项切换
		me.parent().find("li").removeClass('active');
		me.addClass('active');

		//所有其他选择项不可用
		//me.parents(".parameters_box").siblings().find("li").addClass("disabled");

		//遍历可选组合数据
		var objUl;
		$.each(m_skulistObj, function(i, obj) {
			//包含当前选项的组合
			if (obj.key.indexOf(me.parent().attr("groupid") + "_" + me.attr("specid")) != -1) {
				for (var m = 0; m < obj.skudetails.length; m++) {
					objUl = $("#parameters_norm ul[groupid='" + obj.skudetails[m].groupid + "']")
					objUl.find("li[specid='" + obj.skudetails[m].specid + "']").removeClass("disabled");
				}
            }
        });

		//获取sku值
        var sku=getSku();
        if(sku){
        	//如果获取到，跳转页面
        	xigou.setSessionStorage('havesku',1);
        	window.location.href=xigou.changeURLPar(window.location.href, 'sku', sku);
        }

		//去掉不可用选项的active
		$("#parameters_norm li[class*=active][class*=disabled]").removeClass("active");
	});
}

function getSku(){
	var uls=$("ul[groupid]");//获取所有规格对象

	//匹配当前选中的规则
	function check(groupid,specid){
		var result=false;
		uls.each(function(){
			if($(this).attr("groupid")===groupid){
				if($(this).find("li.active").attr("specid")===specid){
					result=true;
				}
			}
		});
		return result;
	}

	var skulist=window._detailsData.data.skus;//直接获取全局变量数据

	//循环skulist集合
    for (var m = 0; m < skulist.length; m++) {
    	if(skulist[m]&&skulist[m].skudetails){

    		//
    		var skudetails=skulist[m].skudetails;
    		var count=0,//规格总数
    			suCount=0;//匹配成功规格总数

    		//循环skudetails
    		for(var t in skudetails){
    			count++;
    			if(check(skudetails[t].groupid,skudetails[t].specid)){
    				suCount++;
    			}
    		}

    		//如果规格匹配成功，返回sku
    		if(count==suCount&&count!=0){
    			return skulist[m].sku;
    		}
    	}
    }
}

//默认选项
function defaultOption() {
    var sku = xigou.getQueryString('sku');
    var me = $('#parameters_norm li');
    $.each(m_skulistObj, function (i, obj) {
        for (var m = 0; m < obj.skudetails.length; m++) {
            for (var z = 0; z < me.length; z++) {
                var flag_obj = obj.skudetails[m].groupid == me.eq(z).parent().attr("groupid") && obj.skudetails[m].specid == me.eq(z).attr("specid");
                if (obj.sku == sku) {
                    if (flag_obj) {
                        me.eq(z).addClass('active');
                    }
                }
            }

        }
    });
}

function slider_show(imageurl) {
	if (imageurl == null || imageurl.length == 0) return false;
	var html = [];
	html.push('<div class="ui-slider">');
	html.push('<ul class="ui-slider-content">');
	for (var i = 0; i < imageurl.length; i++) {
		html.push('<li>');
		html.push('	<a href="javascript:void(0);"><img src="' + imageurl[i] + '"/></a>');
		html.push('</li>');
	};
	html.push('</ul>');
	html.push('<a href="itemdetail.html" class="more_details"></a>');
	html.push('</div>');
	$('#slider').empty().html(html.join(''));
	var slider = new fz.Scroll('.ui-slider', {
    	role: 'slider',
    	indicator: true,
    	autoplay: true,
    	interval: 3000
    });
};


//商品详情-仓库(暂时不处理)
function getstoreid() {
	var data = {
		'token': xigou.getLocalStorage('token'),
		'num': '123456',
		'productid': productid,
		'ordersubid': '109'
	};

	var params = {
		requestBody: data,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length == 0) return false;
				if (json.rescode.code == 0) {

				}
			}
		}
	};

	xigou.activeProduct.getstoreid(params);
};

//附加. 购物车-美图和海囤数量
function getshopcount() {
	var params = {
		'token': xigou.getLocalStorage('token')
	};

	xigou.activeGotoCart.getshopcount({
		requestBody: params,
		showLoading : showLoading,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length==0) return false;
				if (json.code == 0) {
					var cartCount = parseInt(json.data.count);
					if (0 != cartCount) {
						$('.ui-badge').html(cartCount);
						$('.ui-badge').show();
					}
					if (cartCount == 0) {
						$("#footerCar").attr("href", "cart.html");
					}
					
				}else{
					$("#footerCar").attr("href", "logon.html?backurl="+ window.encodeURIComponent(window.location.href) +"");
				}
			}
		}
	});

};
//处理空值
function solveNull(d, n) {
	return (d) == null || typeof(d) == "undefined" || d == "undefined" ? n : d
}

//首页-明日预告-开团提醒
function openGroup() {
	if(xigou.getSessionStorage("userinfo")==""){//没有登录跳到 登录页面
		window.location.href="logon.html?backurl="+window.encodeURIComponent(window.location.href);
		return;
	}
	var dp = {
		requestBody: {
			token:xigou.getLocalStorage('token'),
			specialid:specialid,
			productid:productid
		},
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length == 0) return false;
				switch (json.rescode.code){
					case "0":
						xigou.alert(json.rescode.info||"提醒成功");
						break;
					case "-100":
						xigou.alert({
							message:json.rescode.info||"用户失效，请重新登录。",
							callback:function(){
								xigou.removelocalStorage("token");
								window.location.href="logon.html"
							}
						});
						break;
					default :
						xigou.alert(json.rescode.info||"登录信息错误，请重新登录");
						break;
				}
			}
		}
	}
	xigou.activeIndex.openGroup(dp);
};
//分享列表
;
$(function() {

	var $share = $('.share'),
		$share_list = $('.share_list');
	$share.click(function() {
		$share_list.toggleClass('hide');
	});
	window._bd_share_config = {
		share: []
	};
	with(document) 0[(getElementsByTagName('head')[0] || body).appendChild(createElement('script')).src = 'http://bdimg.share.baidu.com/static/api/js/share.js?cdnversion=' + ~(-new Date() / 36e5)];
});

//<span class="className1">¥<span class="className2">96</span>.69</span>
// 拼接价格的字符串
function GetPriceHtml(itemPrice, className1, className2){
	if(typeof(itemPrice) == undefined || itemPrice == 0)
	{
		return;
	}

	var Yan = "00", Fen = "00";
	var CHARS = itemPrice.split('.');
	if (CHARS.length > 0) {
		Yan = CHARS[0];
		if (CHARS.length > 1) {
			Fen = CHARS[1];
		}
	}

	var html = '<span class="' + className1 + '">¥<span class="' + className2 + '">' + Yan + '</span>.' + Fen + '</span>';
	return html;
}

// 设置价格
function SetPrice(itemPrice){
	if(typeof(itemPrice) == undefined || itemPrice == 0)
	{
		return;
	}

	var Yan = "00", Fen = "00";
	var CHARS = itemPrice.split('.');
	if (CHARS.length > 0) {
		Yan = CHARS[0];
		if (CHARS.length > 1) {
			Fen = CHARS[1];
		}
	}

	// ¥<span class="iem-price2">96</span>.69
	var innerHTML = '¥<span class="iem-price2">' + Yan + '</span>.' + Fen;
	$('#iem-price')[0].innerHTML = innerHTML;
	// $('#tax_price_price')[0].innerHTML = innerHTML;
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
	

	var lineLink = location.href.split('?')[0];
	var tid = xigou.getQueryString('tid');
	var sku = xigou.getQueryString('sku');
	lineLink = lineLink + '?tid=' + tid + '&sku='+sku;
	if(shopOwer != null) {
		lineLink = lineLink + "&shop=" + shopOwer;
	}
	var title = $('.dec-title')[0].innerText || '';
	var desc = $('.dec-info')[0].innerText || '';

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
	if(isWeiXin()){
		$('div.top_menu_wx').append('<div class="kf_btn_wx" onclick="ysf.open();return false;"></div>');		
	}else{
		$('div.top_menu').append('<div class="kf_btn" onclick="ysf.open();return false;"></div>');
	}
	
}

// 显示/隐藏商品详情
function showItemDetail() {
	var $this = $('.div_show_detail');
	var detail = $('.detail-content');
	$('.div_show_detail')[xigou.events.click](function(){
		if ($this.hasClass('up')) {
			$this.removeClass('up').addClass('down');
			detail.show();
		} else {
			$this.removeClass('down').addClass('up');
			detail.hide();
		}
	})
}

//加入购物车点击事件
function Addshop(){
	var sku = productid;
	//检验规格选项是否符合
	if ($("#parameters_norm li").length > 0) {
		var selKey = "";
		var size_color = "";
		$("#parameters_norm li[class*=active]").each(function(index, element) {
			selKey = selKey + "~" + $(this).parent().attr("groupid") + "_" + $(this).attr("specid");
			size_color += $(this).text() + " ";
		});
		size_color += "数量"+ $('.quantity_txt').text();
		$("#size_color").text(size_color);
		if (m_skulistObj[selKey] == undefined) {
			//alert("请选择正确的规格！");
				$.dialog({
                    title:'',
                    content:"请选择正确的规格",
                    button:["确认"]
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
		'sku': sku, //productid,
		'count': $('.quantity_txt').html()
	};

	xigou.setSessionStorage("details_url",window.location.href);
	//if(xigou.getSessionStorage("userinfo")==""){//没有登录跳到 登录页面,保存提交的数据
	//	xigou.setSessionStorage("refer_data", params, true);
	//	//加入购物车登录成功后，直接到结算页面
	//	xigou.setSessionStorage("refer", "home.html", false);
	//	window.location.href="login.html?backurl="+window.encodeURIComponent(window.location.href);//
	//
	//	return;
	//}
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status!=xigou.dictionary.success){
				xigou.setSessionStorage("refer_data", params, true);
				//加入购物车登录成功后，直接到结算页面
				xigou.setSessionStorage("refer", "home.html", false);
				// if(isWeiXin()){
				// 	window.location.href="forcebindtel.html?backurl="+window.encodeURIComponent(window.location.href);//
				// }else{
					window.location.href="logon.html?backurl="+window.encodeURIComponent(window.location.href);//
				// }
				return;
			}
		}
	});
	
	xigou.activeProduct.addshopping({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length == 0) return false;
				if (json.code == 0) {
					var n = parseInt($('#catCount span').html());
					n = n + parseInt($(".quantity_txt").html());
					$('#catCount span').html(n);
					$('.add_shopcar').removeClass('hide');
					$('.add_shopcar_bg').removeClass('hide');
					clearing();
					
					//修改购物车链接
					$("#footerCar").attr("href", "cart.html");
					getshopcount(); //更新购物车角标数量
					//todo update cart count
					$.tips({
		                content:'添加购物车成功!',
		                stayTime:2000,
		                type:"info"
	            	})
				} else {
					$.tips({
		                content:json.msg || "添加失败",
		                stayTime:2000,
		                type:"info"
	            	})
	            	if(isWeiXin()){
					//	window.location.href="forcebindtel.html?backurl="+window.encodeURIComponent(window.location.href);//
					}
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
}

//购买点击事件
function Buy(){
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
			xigou.Dialog({
				content: "请选择正确的规格",
				buttons: {
					'确定': function() {
						this.close();
						this.destroy();
					}
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
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status!=xigou.dictionary.success){
				xigou.setSessionStorage("refer_data", params, true);
				//加入购物车登录成功后，直接到结算页面
				xigou.setSessionStorage("refer", "home.html", false);
				// if(isWeiXin()){
				// 	window.location.href="forcebindtel.html?backurl="+window.encodeURIComponent(window.location.href);//
				// }else{
					window.location.href="logon.html?backurl="+window.encodeURIComponent(window.location.href);//
				// }
				
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
					// if(isWeiXin()){
					// 	window.location.href="forcebindtel.html?backurl="+window.encodeURIComponent(window.location.href);//
					// }
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
}

//规格选择弹框
function ChoiceSpecifications(){
	$("#mask").show();
	$("body").attr("scroll","no");
	$("body").css({
		"overflow-y":"hidden"
	});
	$("#productModal").show();
	var _height = $("#productModal").height() - 27;
	$(".p_close").show().css({
		bottom:_height+"px"
	});
}
/*
xigou.removeSessionStorage("isBuy");		//刷新时移除
xigou.removeSessionStorage("isAddshop");			//刷新时移除*/