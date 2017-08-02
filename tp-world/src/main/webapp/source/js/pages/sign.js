var specialid = xigou.getQueryString('tid');
//var specialid = 427; //专场号
var listCount = 1;
var isascending = "0";//“0”表示不参考价格因素“1”表示升序，价格从底到高“2”表示降序，价格从高到底
var shopMobileVal = null;
var showLoading = true;
var haitao = xigou.getQueryString('haitao');
var listCount = 1;
var me = this;
$(function(){
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
//				initCart()
			}else{
				xigou.setSessionStorage("loginjump_url", "sign.html?tid="+specialid);
				window.location.href = "logon.html";
			}
		}
	});
//	if(xigou.getToken()==""||xigou.getToken()==null){
//		window.location.href='logon.html';
//	}
	if(isWeiXin()){
		$("header").hide();
		$(".header_wx").show();
		$(".sign").css({
			"margin-top":"0"
		});
		$("title").html("每日签到");
	}
	initCalendar();//初始化日历
	show();//初始化展示签到信息
	//点击弹出签到框
	$('.sign_state').bind("touchstart",function(event){
		$(".sign_state_img").addClass("touch_change").removeClass("touch_change1");
		$(".sign_state_img_ed").addClass("touch_change").removeClass("touch_change1");
	  });
	$('.sign_state').bind("touchend",function(event){
		showCalendar();
		$(".cover").show();
		$("body").attr("scroll","no");
		$("body").css("overflow-y","hidden");
		$(".sign_state_img").addClass("touch_change1").removeClass("touch_change");
		$(".sign_state_img_ed").addClass("touch_change1").removeClass("touch_change");
	  });
	/*$(".sign_state").click(function(){
		showCalendar();
		$(".cover").show();
		$("body").attr("scroll","no");
		$("body").css("overflow-y","hidden");
	});*/
	$(".close").click(function(){
		$(".cover").hide();
		$("body").css("overflow-y","scroll");
		$(".sign_state_img").hide();
		$(".sign_state_img_ed").show().css({
			"display":"block"
		});
		show();
	});
//	$(".day").click(function(){
//		$(this).addClass("signed");
//	});
	
		
	
	

	var dropload = $('.body').dropload({
    	scrollArea : window,
    	loadDownFn : function(me){
			if (listCount == 1 && xigou.getSessionStorage("hdlist") && specialid == xigou.getSessionStorage("hdlistid")) {
				loadHistoryData();
			}
			else {
				product(listCount,isascending,me);
			}
			listCount++;
			me.resetload();
    	}
	});

	details(dropload); //专场-详情
	xigou.setSessionStorage('productdetails_backurl', 'hd.htm?tid=' + specialid);

	if (xigou.getQueryString('from') == 'shop') {
		$('a.hd-back').attr("href", 'index.html');
	}
});

/*
 * 签到信息
 */
function show(){
	var params={'token': xigou.getToken()};
	xigou.point.show({
		requestBody: params,
		callback: function(response, status) { //回调函数
//			alert(1);
			if( response.data.pointResult.length!=0  ){
				$(".totalPoint").text(response.data.pointResult[0].totalPoint);//"我的西客币"处展示的积分数量
			}else{
				$(".totalPoint").text(0);//"我的西客币"处展示的积分数量
			}
			
			if(!response.data.signResult[0].enabled){
				if(response.data.signResult[0].point<10){
					$(".nub.point").text(response.data.signResult[0].point+1);//明日可领积分
				}else{
					$(".nub.point").text(response.data.signResult[0].point);//明日可领积分
				}
				$(".word.point").text("明日可领币(个)")
				$(".sequenceDay").text(response.data.signResult[0].days);//已连续签到天数
				$(".sign_state_img").hide();
				$(".sign_state_img_ed").show().css({
					"display":"block"
				});
				$(".alert_top").text("今日已签到！");
			}else{
				if(response.data.signResult[0].point!=null && response.data.signResult[0].point!="" && response.data.signResult[0].point!= undefined){
					$(".nub.point").text(response.data.signResult[0].point);//今日可领积分
				}else{
					$(".nub.point").text(response.data.signResult[0].point);//今日可领积分
				}
				$(".sequenceDay").text(response.data.signResult[0].days-1);//已连续签到天数
				$(".alert_top").text("签到成功！");
			}
		}
	});
}
/*
 * 签到日历
 */
function showCalendar(){
	var year = new Date().getFullYear();
    var month = new Date().getMonth() + 1;
    var day = new Date().getDate();
    if(day<10){
    	day = "0" + day;
    }
    var today = year+""+month+""+day;
    
	var params={'token': xigou.getToken()};
	xigou.point.showcalendar({
		requestBody: params,
		callback: function(response, status) { //回调函数
			var dataList = response.data;
			var len = dataList.length;
			for(var i=0;i<dataList.length;i++){
				var arrSignDate = dataList[i].signDateStr;
				var point = dataList[i].point-5;
				if(point==0){
					point="";
				}
				if(today==arrSignDate){
					
//					$("#"+today).attr("class","");
					$("#"+today).addClass("signedcheck");
					setTimeout(function(){
						$("#"+today).removeClass("signedcheck");
						$("#"+today).addClass("signed"+point);
					},1000);
				
				}else{
					$("#"+arrSignDate).addClass("signed"+point);
				}
			}
			
			$(".record_color.days").text(dataList[len-1].days);
			$(".record_color.points").text(dataList[len-1].point);
		}
	});
}
/*
 * 初始化日历
 */
function initCalendar(){
	var year = new Date().getFullYear();
    var month = new Date().getMonth() + 1;
    var first_day = new Date(year, month - 1, 1).getDay(); 
    var final_date = new Date(year, month, 0).getDate(); 
    var StartPoint = first_day - 1 >= -1 ? first_day  : 6;

    if(month<10){
    	month = "0"+month;
    }
    var html = [];
    var mathResult = Math.ceil((StartPoint+final_date)/7);
   
    for(var j=1;j<=mathResult;j++){
   	 html.push('<tr>');
   	 for(var k=1;k<=7;k++){
   		 if(k+7*(j-1)>StartPoint && k+7*(j-1)<=StartPoint+final_date){
   			 
			 if(new Date().getDate()==k+7*(j-1)-StartPoint){
				 if(k+7*(j-1)-StartPoint<10){
					 html.push('<td class="day" id="'+year+month+"0"+(k+7*(j-1)-StartPoint)+'"><span></span><img src="img/sign/sign_today.png" class="coin_today"></td>');
	   			 }else{
	   				 html.push('<td class="day" id="'+year+month+(k+7*(j-1)-StartPoint)+'"><span></span><img src="img/sign/sign_today.png" class="coin_today"></td>');
	   			 }
   				
   			 }else{
   				if(k+7*(j-1)-StartPoint<10){
   					html.push('<td class="day" id="'+year+month+"0"+(k+7*(j-1)-StartPoint)+'"><span></span></td>');
   				}else{
   					html.push('<td class="day" id="'+year+month+(k+7*(j-1)-StartPoint)+'"><span></span></td>');
   				}
   			 }
   		 }else{
   			 html.push('<td><span></span></td>');
   		 }
   	 }
   	 html.push('</tr>');
    }
    $(".table tbody").empty().append(html.join(''));
    var ttd = $("tbody td");
    for (var i = StartPoint; i < final_date + StartPoint; i++) {
   	 	ttd[i].getElementsByTagName("span")[0].innerText = i - StartPoint +1;
    }
};

var check = function(){
	var year = new Date().getFullYear();
    var month = new Date().getMonth() + 1;
    var day = new Date().getDate();
    var today = year+""+month+""+day;
    
    setTimeout(function(){
		$("#"+today).attr("class","");
		$("#"+today).addClass("signedcheck11");
	},500);
}


/**===================================================*/
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

					/*if (title == '西客商城' && json.data.name) {
						title = json.data.name;
					}*/

					if(typeof(tophtml) != undefined)
					{
						$('.hd-tophtml').append(json.data.tophtml);
					}

					$('title').text(json.data.name);
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
					$('#details').append(htmlData.join(''));
					$('.ui-imglazyload').imglazyload();
					$("#headerTitle").html(json.data.name); // 专场名称
//					order_price(dropload);

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
								var len = 6;
								if(data.length<6) len = data.length;
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

									htmlData.push('<div class="product_item">');
									htmlData.push('	<div class="product_item_box" tid="' + tid + '" sku="' + sku + '">');
									htmlData.push('		<a href="item.htm?tid=' + tid +'&sku=' + sku + '">');
									htmlData.push('			<img class="product_img" src="' + imageurl + '" >');
									htmlData.push('			<div class="product_name">' + data[i].name + '</div>');
									htmlData.push('			<div class="product_price">¥<span class="product_price_yuan">' + Yan +'</span>.' + Fen + '&nbsp;');
									htmlData.push('				<span class="product_old_price">¥' + data[i].oldprice + '</span>');
									htmlData.push('			</div>');
									htmlData.push('			<div class = "fy_price" style = "display: none;">返佣:¥<span>' + data[i].commision + '</span></div>');
									htmlData.push('		</a>');
									htmlData.push('	</div>');
									htmlData.push('</div>');
								}

								if (page == 1) $("#hd-products-id").empty();
								$("#hd-products-id").append(htmlData.join(" "));

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
								
								//专场
								if(window.location.href.indexOf("=6885")>-1||window.location.href.indexOf("=7498")>-1||window.location.href.indexOf("=7348")>-1||window.location.href.indexOf("=7707")>-1){
									xigou.setSessionStorage('comeFromAd','1');
									$(".floor_top_filter").addClass('hide');
									$(".products_list").addClass('hide');
								}

								//记录锚点=========================
								var hdhistory = xigou.getSessionStorage("hdlistid");
								var dataStr = JSON.stringify(data);
								if(page == 1 || hdhistory != specialid) {
									xigou.setSessionStorage("hdlist","");
								};
								var _speciallist = xigou.getSessionStorage("hdlist");
								if(_speciallist) {
									var _oldData = JSON.parse(_speciallist);
									for(var m = 0,len = data.length;m < len ;m++) {
										_oldData.push(data[m]);
									}
									xigou.setSessionStorage("hdlist",JSON.stringify(_oldData));
								}else {
									xigou.setSessionStorage("hdlist",dataStr);
								}
								xigou.setSessionStorage("historyhdpage",page);
								xigou.setSessionStorage("hdlistid",specialid);

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

function loadHistoryData(){
	listCount = xigou.getSessionStorage("historyhdpage");
	var _speciallist = xigou.getSessionStorage("hdlist");

	if(_speciallist) {
		var data = JSON.parse(_speciallist);
		var htmlData = [];
		var len = 6;
		if(data.length<6) len=data.length;
		for(var i = 0;i<len;i++) {
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

			htmlData.push('<div class="product_item">');
			htmlData.push('	<div class="product_item_box" tid="' + tid + '" sku="' + sku + '">');
			htmlData.push('		<a href="item.htm?tid=' + tid +'&sku=' + sku + '">');
			htmlData.push('			<img class="product_img" src="' + imageurl + '" >');
			htmlData.push('			<div class="product_name">' + data[i].name + '</div>');
			htmlData.push('			<div class="product_price">¥<span class="product_price_yuan">' + Yan +'</span>.' + Fen + '&nbsp;');
			htmlData.push('				<span class="product_old_price">¥' + data[i].oldprice + '</span>');
			htmlData.push('			</div>');
			htmlData.push('			<div class = "fy_price" style = "display: none;">返佣:¥<span>' + data[i].commision + '</span></div>');
			htmlData.push('		</a>');
			htmlData.push('	</div>');
			htmlData.push('</div>');
		}
		$("#hd-products-id").empty().append(htmlData.join(" "));
		if(shopMobileVal){
			$(".fy_price").show();
		}

		var tid = xigou.getSessionStorage("specialid");
		var sku = xigou.getSessionStorage("specialSku");
		if (tid == specialid && sku) {
			var _scrollEl = $('a[href$="'+sku+'"]')[0];
			if (_scrollEl) {
				setTimeout(function(){
					_scrollEl.scrollIntoViewIfNeeded();
				}, 500);
			}
		}
	}
}
