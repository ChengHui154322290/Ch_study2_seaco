/**
 * 
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-14 13:41:54
 * @version Ver 1.0.0
 */

$(function() {
	if(isWeiXin()){
		$("header").hide();
		$(".top_nav_list").css({
			"top":"0"
		})
		$(".ui-tab").css({
			"margin-top":"36px"
		})
		$("title").html("我的订单");
	}
	var page = 1;
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				// allorders(page);
				$('.orders-list').dropload({
			    	scrollArea : window,
			    	loadDownFn : function(me){
						allorders(page,me);
                        me.resetload();
                        if(1 == page)
                        {
                            $(".dropload-refresh").hide();
                        }
                        page++;
			    	}
				});
				var nDefaulePage = xigou.getQueryString("defaule");
				if (nDefaulePage)
				{
					setdefaulesel(nDefaulePage);
				}
			}else{
				window.location.href="index.html";
			}
		}
	});
});

var orders_max = "N";

//用户-全部订单
function allorders(page,me){
	if(orders_max == "Y"){
		me.disable("down",false);
		$('.ui-refresh-down>span:first-child').removeClass();
		return false;
	}
	
	if(orders_max != "Y"){
		var params = {
				'token': xigou.getToken(),
				'type': '1',//“”全部订单 “2”待付款 “3”待转移 “4”待发货 “5”待收货 “6”已取消 “100”退款完成
				'curpage': page
			};
		
		xigou.activeUser.allorders({
			requestBody: params,
			callback: function(response, status) { //回调函数
				if (status == xigou.dictionary.success) {
					var json = response || null;
					if (null == json || json.length == 0) return false;
					var code = json.code;
					if (code == 0) {
						createList(json,page);
						if(response.data.curpage >= response.data.totalpagecount)
						{
							me.lock();
							$(".dropload-down").hide();
						}
						// if(me) {
						// 	me.afterDataLoading("down");
						// }
					}
					else
					{
						me.lock();
						$(".dropload-down").hide();
					}
				}
			}
		});
	}
}; 

//确认收货
function ordersconfirmReceipt(){
	$('.orders_btn_pay_sure').off(xigou.events.click);
	$('.orders_btn_pay_sure')[xigou.events.click](function() {
		var token = xigou.getToken();
		var ordercode=$(this).attr("ordercode");

		var params = {
			'token': token,
			'ordercode': ordercode,
		};

		xigou.activeUser.confirmshipment({
			requestBody: params,
			callback: function(response, status) { //回调函数
				if (status == xigou.dictionary.success) {
					if (response.code == 0) {
						$.tips({
		                    content:response.msg || "已确认收货",
		                    stayTime:2000,
		                    type:"warn"
		                 })
						 $(this).innerHTML="已收货";
					}
					else
					{
						$.tips({
		                    content:response.msg || "确认订单失败",
		                    stayTime:2000,
		                    type:"warn"
		                 })
					}
				}
			}
		});
	});	
};

//删除
function ordersdel(){
	$('.orders_btn_del').off(xigou.events.click);
	$('.orders_btn_del')[xigou.events.click](function() {

		var ordercode=$(this).attr("ordercode");
		setTimeout(function(){
			var dia=$.dialog({
				title:'',
				content:"是否删除订单？",
				button:["否","是"]
			});

			dia.on("dialog:action",function(e){
				if(e.index == 1)
				{
					var token = xigou.getToken();

					var params = {
						'token': token,
						'ordercode': ordercode,
						"type": "2",
					};

					xigou.activeUser.deleteorder({
						requestBody: params,
						callback: function(response, status) { //回调函数
							if (status == xigou.dictionary.success) {
								if (response.code == 0) {
									$.tips({
										content:response.msg || "已删除订单",
										stayTime:2000,
										type:"warn"
									});
									var url = "orders.html";
									var nDefaulePage = xigou.getQueryString("defaule");
									if (nDefaulePage)
									{
										url += '?defaule=' + nDefaulePage;
									}
									window.location.href = url;
								}
								else
								{
									$.tips({
										content:response.msg || "删除订单失败",
										stayTime:2000,
										type:"warn"
									})
								}
							}
						}
					});
				}
			});
		},50)

	});	
};

//取消
function orderscancel(data){
	$('.orders_btn_pay_cancal').off(xigou.events.click);
	$('.orders_btn_pay_cancal')[xigou.events.click](function(e) {
		e.stopPropagation();
		var ordercode=$(this).attr("ordercode");
		var tage = this;
		setTimeout(function(){
			var dia=$.dialog({
				title:'',
				content:"是否取消订单？",
				button:["否","是"]
			});
			dia.on("dialog:action",function(e){
				if(e.index == 1)
				{
					var token = xigou.getToken();

					var params = {
						'token': token,
						'ordercode': ordercode,
						"type": "1",
					};
					xigou.activeUser.deleteorder({
						requestBody: params,
						callback: function(response, status) { //回调函数
							if (status == xigou.dictionary.success) {
								if (response.code == 0) {
									$.tips({
										content:response.msg || "已取消订单",
										stayTime:2000,
										type:"warn"
									})

									$(tage).first().unbind("ontouchend" in document ? "tap" : "click");
									var url = "orders.html";
									var nDefaulePage = xigou.getQueryString("defaule");
									if (nDefaulePage)
									{
										url += '?defaule=' + nDefaulePage;
									}
									window.location.href = url;
								}
								else
								{
									$.tips({
										content:response.msg || "取消订单失败",
										stayTime:2000,
										type:"warn"
									})
								}
							}
						}
					});
				}
			});
		},250)

	});	
};

//订单状态


//创建列表
function createList(data,page){
	var o = data,
		length = o.data.list.length,   //订单总数量
		allpage = o.allpage,   //总页数
		count = o.count,       //当前页数量  
		alllist = o.data.list,   // 全部订单
		flgs = false,
		html = [];

		//DOM
		for(var i=0; i<length; i++){
			html.push('<div class="orders-item" ordercode="' + alllist[i].ordercode + '">');
			html.push('	<div class="order_time_status">' + alllist[i].ordertime);
			if (!alllist[i].statusdesc) {
				alllist[i].statusdesc = " ";
			}
			html.push('		<span class="order_status">' + alllist[i].statusdesc + '</span></div>');
			for (var j = 0; j < alllist[i].lines.length; j++) {
				var item = alllist[i].lines[j];
				html.push('<div class="order_info" sku="' + item.sku + '" tid = ' + item.tid + '>');
				html.push('	<div class="order_image">');
				html.push('		<img src="' + item.imgurl + '">');
				html.push('	</div>');
				html.push('	<div class="oder_info2">');
				html.push('		<div class="order_name_price">');
				html.push('			<div class="order_price">¥' + item.price + '</div>');
				html.push('			<div class="order_name">' + item.name + '</div>');
				html.push('		</div>');
				html.push('		<div class="order_size_count">');
				html.push('			<div class="order_count">×' + item.count + '</div>');
				if (item.specs.length > 0) {
					html.push('			<div style="width: 185px;">' + item.specs + '</div>');
				}
				html.push('		</div>');
				html.push('	</div>');
				html.push('</div>');
			};
			html.push('	<div class="order_totle_count_price">');
			html.push('		<span class="float_right ">共 ' + alllist[i].linecount + ' 件 实付：<span class="order_lineprice">¥' + alllist[i].orderprice + '</span></span>');
			html.push('	</div>');
			html.push('	<div class="order_btns">');
			//判断订单状态
			switch(alllist[i].status){
				case '0':  // 已取消
						html.push('<a class="bottom_btn orders_btn_del" ordercode="'+alllist[i].ordercode+'" payway="'+item.payway+'">删除订单</a> ');
				break;
				case '2':  //待支付
						html.push('<a class="bottom_btn orders_btn_pay" ordercode="'+alllist[i].ordercode+ '" orderprice="'+alllist[i].orderprice +  '">立即支付</a> ');
						html.push('<a class="bottom_btn orders_btn_pay_cancal" ordercode="'+alllist[i].ordercode+'">取消订单</a>');
				break;
				case '4':  //已支付 待发货
					if (alllist[i].iscancancel && alllist[i].iscancancel == 1) {
						html.push('<a class="bottom_btn orders_btn_pay_cancal" ordercode="'+alllist[i].ordercode+'">取消订单</a>');
					}
				break;
				case '5':  //待收货
					  if(alllist[i].ordertype!=8){
						    html.push('<a class="bottom_btn orders_btn_pay_look" ordercode="'+alllist[i].ordercode+'">订单追踪</a> ');
							html.push('<a class="bottom_btn orders_btn_pay_sure" ordercode="'+alllist[i].ordercode+'">确认收货</a> ');  
					  }else{
							html.push('<a class="bottom_btn orders_btn_pay_immediatelyuse" ordercode="'+alllist[i].ordercode+'">立即使用</a>');
					  }
						
				break;
				case '6': //已完成
					 if(alllist[i].ordertype!=8){
						 html.push('<a class="bottom_btn orders_btn_del" ordercode="'+alllist[i].ordercode+'" payway="'+item.payway+'">删除订单</a> ');
						 html.push('<a class="bottom_btn orders_btn_pay_look" ordercode="'+alllist[i].ordercode+'">订单追踪</a> ');
					 }else{
						 html.push('<a class="bottom_btn orders_btn_del" ordercode="'+alllist[i].ordercode+'" payway="'+item.payway+'">删除订单</a> '); 
					 }
						
				break;
			};
			html.push('	</div>');
			html.push('</div>');
		};
		
		if(data.data.pagesize == "" || data.data.pagesize == 0 || data.data.pagesize == '0' || data.data.curpage == data.data.totalcount) {
			orders_max = "Y";
		}
		if (page == 1 && html.length == 0) {
			$("#return_list").empty();
			var htmlData = [];
			htmlData.push('<div class="order_empty">还木有订单哟~</div>');
			$('.orders-list').append(htmlData.join(''));
		} else {
			$('.orders-list').append(html.join(''));
		}


		$('.order_info').off(xigou.events.click);
		$(".order_info")[xigou.events.click](function(){
			var orderItem = $(event.target).parents('.orders-item');
			var ordercode = orderItem.attr("ordercode");
			xigou.setSessionStorage("ordercode", ordercode);

			xigou.setSessionStorage('orderdetails_backurl',"orders.html");
			window.location.href='orderdetails.html?ordercode='+ordercode;
		});
		
		orderscancel(alllist);
		ordersdel();
		ordersconfirmReceipt();
		ordersgotoPay();
		orderpaylook();
		orderImmediatelyuse();   //立即支付
}

function ordersgotoPay() {
	$('.orders_btn_pay').off(xigou.events.click);
	$('.orders_btn_pay')[xigou.events.click](function(e) {

		var token = xigou.getToken();
		var ordercode=$(this).attr("ordercode");
		var payid=$(this).attr("payid");
		var payway=$(this).attr("payway");
		//var openid = xigou.getLocalStorage("openid");
		var price = $(this).attr("orderprice");

		if(isWeiXin()){
			var host = encodeURIComponent(xigou.pageHost);

			var param= payid+","+ ordercode+","+price+","+xigou.getToken()+","+ "world"+","+host;

			window.location.href = xigou.xgHost + "paymentbridge.html?param="+param;
			return;
		}

		var payway = "weixin";
		if (!isWeiXin())
		{
			payway = "mergeAlipay";
		}

		var params = {
				'token': token,
				'ordercode': ordercode,
				'payid': payid,
				'payway': payway,
		};
		
		xigou.activeHtClearing.payNow({
			requestBody: params,
			callback: function(response, status) { //回调函数
				if (status == xigou.dictionary.success) {
					if (null == response) {
						$.tips({
	                            content:xigou.dictionary.chineseTips.server.value_is_null,
	                            stayTime:2000,
	                            type:"warn"
	                        })
					} else {
						switch (response.code) {
							case "0":
								if (isWeiXin()) {
									onBridgeReady(response.data, payid, ordercode);
								}
								else {
									var submit_pay = $("#submit_pay");
									var result=xigou.getPaymentForm({
										el:submit_pay,
										datas:response.data,
										payway:payway
									} );
									if(result){
										submit_pay.submit();
									}
								}
									break;
							case "-1003":
								window.location.href = "payresult.html?pid="+payid+"&ordercode="+ordercode;
								break;
							default:
								$.tips({
		                            content:response.msg || "支付提交失败",
		                            stayTime:2000,
		                            type:"warn"
		                        })
								break;
						}
					}
				} else {
					$.tips({
		                content:'请求失败，详见' + response,
		                stayTime:2000,
		                type:"warn"
		            })
				}
			}
		});
	});
};

// 切换当前选中List
function setdefaulesel(npage)
{
	//var scroller = $('.ui-tab-content')[0];
	//var nav = $('.ui-tab-nav')[0];
    //
	//$(scroller.children[0]).removeClass('current');
	//$(nav.children[0]).removeClass('current');
	//$(scroller.children[npage]).addClass('current');
	//$(nav.children[npage]).addClass('current');
	//var width = -1 * scroller.clientWidth * npage / 3;
	//scroller.style.transform = "translate(" + width + "px, 0px)";
	//$(scroller.children)[0].height = "0px";
	order_tab._autoplay(1,npage);
}

// 订单追踪
function orderpaylook(){
	$('.orders_btn_pay_look').off(xigou.events.click);
	$('.orders_btn_pay_look')[xigou.events.click](function() {
		var ordercode=$(this).attr("ordercode");
		xigou.setSessionStorage("logisticordercode", ordercode);
		window.location.href = "ordertracking.html?ordercode="+ordercode;
	});
};

//立即支付
function orderImmediatelyuse(){
	$('.orders_btn_pay_immediatelyuse').off(xigou.events.click);
	$(".orders_btn_pay_immediatelyuse")[xigou.events.click](function(){
		var orderItem = $(event.target).parents('.orders-item');
		var ordercode = orderItem.attr("ordercode");
		xigou.setSessionStorage("ordercode", ordercode);

		xigou.setSessionStorage('orderdetails_backurl',"orders.html");
		window.location.href='orderdetails.html?ordercode='+ordercode;
	});
};