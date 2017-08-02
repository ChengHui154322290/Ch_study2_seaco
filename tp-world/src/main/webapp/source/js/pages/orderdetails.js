/**
 * 
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-14 13:41:54
 * @version Ver 1.0.0
 */
var  orderType;
var ordercode=xigou.getQueryString("ordercode");
if(ordercode == null)
{
	window.location.href="home.html";
}

$(function() {
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				orderinfo();  // 订单详情
			}else{
				window.location.href="index.html";
			}
		}
	});
	
	if(isWeiXin()){
		$("header").hide();
		$(".top_nav_list").css({
			"top":"0"
		})
		$("title").html("我的订单");
	}
});

//用户-订单详情
function orderinfo(){
	var ordercode=xigou.getQueryString("ordercode");
	var params = {
		'token': xigou.getToken(),
		'ordercode':ordercode
	};

	xigou.activeUser.orderinfo({
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
							setHtml(response);
							break;
						default:
							$.tips({
	                            content:response.msg||"获取订单信息失败",
	                            stayTime:2000,
	                            type:"warn"
	                        });
							break;
					}
				}
			} else {
				$.tips({
					content:response.msg||"获取订单信息失败",
	                stayTime:2000,
	                type:"warn"
	            });
			}
		}
	});
}; 
var isOverType = false;
//创建订单详情
function setHtml(json){
	var  isOverDue=false;//是否过期
	var data = json.data;
	var orderRedeemItemList = data.orderRedeemItemList;
	orderType=data.ordertype ;
	if(data.ordertype != 8 ){
		$(".coupon_code").hide();
		$(".div_add_tel").hide();
		$(".div_add_info").show();
		$(".div_name").html('收货人：' + data.name);		// 收货人
		$(".div_tel").html(data.tel);						// 电话
		$('.div_add').html("收货地址：" + data.address);	// 收获地址
	}else if(data.ordertype == 8){
		$(".div_add_info").hide();
		if((data.status == 5 || data.status==6 || data.status==0) && orderRedeemItemList.length>0){
			$(".div_add_tel").hide();
			$(".coupon_code").show();
			
			//获取Datatime格式的到期时间
			var effectTimeEnd = orderRedeemItemList[0].effectTimeEnd;
			var timeEnd = new Date(effectTimeEnd);
			function FormatDate (strTime) {
			    var date = new Date(strTime);
			    return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
			}
			var expiretime = FormatDate(timeEnd);
			
			$(".couponcode_message_name").html(orderRedeemItemList[0].spuName);     //劵码店铺名
			$(".couponcode_message_time").html(expiretime);			//劵码到期时间
			Initkf();
			var html = [];
		    //动态加载劵码
			for(var i = 0;i<orderRedeemItemList.length;i++){
				if(orderRedeemItemList[i].redeemCodeState == 3){
					  isOverDue=true;
				}
				if(orderRedeemItemList[i].redeemCodeState!="1"){
					html.push('<li><p><span class="redeem_name">'+orderRedeemItemList[i].redeemName+'</span><span class="redeem_code_nub" style="text-decoration:line-through">'+orderRedeemItemList[i].redeemCode+'</span></p></li>');
				}else{
					html.push('<li><p><span class="redeem_name">'+orderRedeemItemList[i].redeemName+'</span><span class="redeem_code_nub">'+orderRedeemItemList[i].redeemCode+'</span><span qr_code="'+orderRedeemItemList[i].qrCode+'" class="redeem_qrcode" ></span></p></li>');
				}
			}
			$("#redeem_code").html(html.join(""));
			
			//二维码点击事件
			$(".redeem_qrcode")[xigou.events.click](function() {
				var ercode = $(this).attr("qr_code");
				$(".redeem_qrcode_img").css("background-image","url('data:image/png;base64,"+ercode+"')")
				$(".redeem_qrcode_popup").show();
			});
			$(".redeem_qrcode_closeBtn")[xigou.events.click](function() {
				$(".redeem_qrcode_popup").hide();
			});
			
			var myDate = new Date();
			var localtime=myDate.getTime();
			//本地的时间和过期时间作比较
			if(localtime>timeEnd){
				$(".redeem_code_nub").css("text-decoration","line-through");
				$(".redeem_qrcode").hide();
				isOverDue=true;
			}
		}else if((data.status == 2 || data.status == 0) && orderRedeemItemList.length<=0){
			$(".coupon_code").hide();
			$(".div_add_tel").show();
			$("#telephone").html(data.receiveTel);
			isOverType = true;
		}
		
		$(".couponcode_message_judge").css("display","none");        //劵码时运费与税金取消
	}
	$('#ordercode').html(data.ordercode);				// 订单号
	//判断是否过期
	if(isOverDue==false){
		$('#statusdesc').html(data.statusdesc);		// 订单状态
	}else{
		$('#statusdesc').html("劵过期已退款");		
	}
	$('#orderprice').html(data.orderprice);				// 订单金额
	$('#paywaydesc').html(data.paywaydesc);				// 支付方式

	
	
	// 商品列表
	var itemsList = data.lines;
	var htmlData = [];
	htmlData.push('<div class="div_time">下单时间：' + data.ordertime + '</div>'); // 下单时间
	for (var i = 0; i < itemsList.length; i++) {
		var item = itemsList[i];
		htmlData.push('<div class="div_items">');
		htmlData.push('	<div class="item-img">');
		//item.htm?sku=07020700020101&amp;tid=12004
		if(data.ordertype != 8){
			var href = 'item.htm?sku=' + item.sku + '&tid=' + item.tid;
		}else{
			var href = 'group_detail.html?sku=' + item.sku + '&tid=' + item.tid;
		}
		htmlData.push('		<a href="' + href + '">');
		htmlData.push('			<img class="item-img-img" src="' + item.imgurl + ' ">');
		htmlData.push('		</a>');
		htmlData.push('	</div>');
		htmlData.push('	<div class="item_info">');
		htmlData.push('		<span class="item_price">' + createPriceHTML(item.price, "item_price_yuan") + '</span>');
		htmlData.push('		<div class="item_name_price">');
		htmlData.push('			<div class="item_name">' + item.name + '</div>');
		htmlData.push('		</div>');
		htmlData.push('		<div class="order_size_count">');
		if (item.specs.length > 0) {
			htmlData.push('		<div class="item_count">×' + item.count + '</div>');
			htmlData.push('			<div style="width: 185px;">' + item.specs + '</div>');
		}else{
			htmlData.push('		<div class="item_count1">×' + item.count + '</div>');
		}
		htmlData.push('		</div>');
		htmlData.push('		<div class="item_lineprice">' + createPriceHTML(item.lineprice, "item_lineprice_yuan") + '</div>');
		if (item.isreturn == 0  &&  data.status ==6 && data.ordertype != 8) {
			htmlData.push('		<div class="item_aftersale" index = "' + i + '">申请售后</div>');
		}
		htmlData.push('	</div>');
		htmlData.push('</div>');
	};

	$(".items_info").html(htmlData.join(" "));

	$('#freight').html('¥' + data.freight);	// 运费
	$('#disprice').html('-¥' + data.disprice);	// 优惠金额
	$('#taxes').html('¥' + data.taxes);		// 税金
	$('#points').html('-¥'+data.totalpoint);//积分
	$('.totle_price_fen').html(createPriceHTML(data.baseprice, 'totle_price_yuan'));	// 应付款

	// 底部按钮
	var htmlButtomData = [];
	//statusCN = status == '0'? '已取消':status == '2'? '待支付':status == '4'? '已付款':status == '5'? '已发货':status == '6'? '已收货':"",
	switch(data.status){
		case "0": 	// 已取消
			htmlButtomData.push('<a class="bottom_btn orders_btn_del" ordercode="'+data.ordercode+'" payid="'+data.payid+'">删除订单</a>');
			break;
		case "2":    //待支付
			htmlButtomData.push('<a class="bottom_btn detail_btn_pay" ordercode="'+data.ordercode+'" payid="'+data.payid+'">立即支付</a>');
			htmlButtomData.push('<a class="bottom_btn detail_btn_pay_cancal" ordercode="'+data.ordercode+'" payid="'+data.payid+'">取消订单</a>');
			break;
		case "4":  // 已付款
			if (data.iscancancel && data.iscancancel == 1) {
				htmlButtomData.push('<a class="bottom_btn detail_btn_pay_cancal" ordercode="'+data.ordercode+'" payid="'+data.payid+'">取消订单</a>');
			}
			break;
		case '5':  // 已发货
			if(data.ordertype != 8){
				htmlButtomData.push('<a class="bottom_btn orders_btn_pay_look" ordercode="'+data.ordercode+'">订单追踪</a> ');
				htmlButtomData.push('<a class="bottom_btn orders_btn_pay_sure" ordercode="'+data.ordercode+'">确认收货</a> ');
			}else{
				if(isOverDue==false){
					htmlButtomData.push('<a class="bottom_btn detail_btn_pay_cancal" ordercode="'+data.ordercode+'" payid="'+data.payid+'">取消订单</a>');
				}else{
					htmlButtomData.push('<a class="bottom_btn orders_btn_del" ordercode="'+data.ordercode+'" payid="'+data.payid+'">删除订单</a>');
				} 
			};
			break;
		case '6':  // 已收货
			if(data.ordertype!=8){
				htmlButtomData.push('<a class="bottom_btn orders_btn_del" ordercode="'+data.ordercode+'" payid="'+data.payid+'">删除订单</a>');
				htmlButtomData.push('<a class="bottom_btn orders_btn_pay_look" ordercode="'+data.ordercode+'" payid="'+data.payid+'">订单追踪</a>');
			}else {
				htmlButtomData.push('<a class="bottom_btn orders_btn_del" ordercode="'+data.ordercode+'" payid="'+data.payid+'">删除订单</a>');
			}
			
			break;
		default:
			break;
	}
	$(".div_bottom").html(htmlButtomData.join(" "));

	
	detailgotoPay();
	ordersdel();
	detailcancel(data);
	orderpaylook();
	ordersconfirmReceipt();
	aftersales(data);
}

// 立即支付
function detailgotoPay() {
	$('.detail_btn_pay')[xigou.events.click](function(e) {
		var token = xigou.getToken();
		var ordercode=$(this).attr("ordercode");
		var payid=$(this).attr("payid");
		var openid = xigou.getLocalStorage("openid");
			
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
				'openid': openid,
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
									//onBridgeReady(response.data, payid, ordercode);
									var orderprice = $('#orderprice').html();
									var host = encodeURIComponent(xigou.pageHost);
									var param= payid+","+ ordercode+","+orderprice+","+xigou.getToken()+","+"world"+","+host;
									window.location.href = xigou.xgHost + "paymentbridge.html?param="+param;
									return;
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
						content:response.msg || "支付提交失败",
		                stayTime:2000,
		                type:"warn"
		            })
				}
			}
		});
	});
};

// 删除订单
function ordersdel() {
	$('.orders_btn_del')[xigou.events.click](function() {
		var ordercode=$(this).attr("ordercode");
		var dia=$.dialog({
			title:'',
			content:"是否删除订单？",
			button:["否","是"]
		});

		dia.on("dialog:action",function(e) {
			if(e.index == 1) {
				var token = xigou.getToken();
				var params = {
					'token': token,
					'ordercode': ordercode,
					"type": "2"
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
								})
								$('.orders_btn_del').text("已删除");
								$('.orders_btn_del').off(xigou.events.click);
								setTimeout(function(){
									var url = "orders.html";
									var nDefaulePage = xigou.getQueryString("defaule");
									if (nDefaulePage)
									{
										url += '?defaule=' + nDefaulePage;
									}
									window.location.href = url;
								},"2000");
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
		})
	})
}

//取消
function detailcancel(data){
	$('.detail_btn_pay_cancal')[xigou.events.click](function() {

		var ordercode=$(this).attr("ordercode");
		if(data.ordertype == 8){
			if(data.status == 5){
				var dia=$.dialog({
		            title:'',
		            content:"小西将会为你退款，在一至七天内返还到您的支付账户！",
		            button:["取消","确认"]
		        });
			}else{
				var dia=$.dialog({
					title:'',
					content:"是否取消订单？",
					button:["否","是"]
				});
			}
		}else{
			var dia=$.dialog({
				title:'',
				content:"是否取消订单？",
				button:["否","是"]
			});
		}
				

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
								$('.detail_btn_pay_cancal').text("已取消");
								$('.detail_btn_pay_cancal').off(xigou.events.click);
								setTimeout(function(){
									var url = "orders.html";
									var nDefaulePage = xigou.getQueryString("defaule");
									if (nDefaulePage)
									{
										url += '?defaule=' + nDefaulePage;
									}
									window.location.href = url;
								},"2000");
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
	});	
}

// 订单追踪
function orderpaylook(){
	$('.orders_btn_pay_look')[xigou.events.click](function() {
		var ordercode=$(this).attr("ordercode");
		xigou.setSessionStorage("logisticordercode", ordercode);
		window.location.href = "ordertracking.html?ordercode="+ordercode;
	});
}

//确认收货
function ordersconfirmReceipt(){
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
						$('.orders_btn_pay_sure').text("已收货");
						$('.orders_btn_pay_sure').off(xigou.events.click);
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
}

//处理空值
function solveNull(d, n) {
    return (d) == null || typeof(d) == "undefined" ? n : d;
}

// 拼接价格发字符串
function createPriceHTML(price, classname) {
    var htmlPrice = '';
    var Yan = "00", Fen = "00";
    var CHARS = price.split('.');
    if (CHARS.length > 0) {
        Yan = CHARS[0];
        if (CHARS.length > 1) {
            Fen = CHARS[1];
        }
    }

    htmlPrice = '¥<span class="' + classname + '">' + Yan + '</span>.' + Fen;
    return htmlPrice;
}

// 申请售后
function aftersales(data) {
	$('.item_aftersale')[xigou.events.click](function(e) {
		var Index = this.getAttribute('index');
		var param = {};
		param.lineid = data.lines[Index].lineid;
		param.itemname = data.lines[Index].name;
		param.itemprice = data.lines[Index].lineprice;
		param.buycount = data.lines[Index].count;
		param.maxcount = data.lines[Index].count;
		param.imgurl = data.lines[Index].imgurl;
		param.ordercode = data.ordercode;

		xigou.setLocalStorage("aftersalesparam",JSON.stringify(param));
		window.location.href="customerservice.html";
	});
}

//初始化客服
function Initkf(){
	var name = xigou.getLocalStorage("show_name")|| xigou.getLocalStorage("login_name") || "西客会员";
	if (name) {
		var oScript= document.createElement("script");
		oScript.type = "text/javascript";
		oScript.src='https://qiyukf.com/script/6e39dddabff63d902f55df3961c2793d.js?name=' + name + '&mobile=' + name;
		$('body')[0].appendChild(oScript);
	}

	$('div.coupon_code').append('<div class="kf_btn" onclick="ysf.open();return false;"></div>');
}