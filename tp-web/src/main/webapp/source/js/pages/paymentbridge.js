var UserToken = xigou.getToken();

var param = xigou.getQueryString("param");
var arr = param.split(",");
var payid= arr[0];
var ordercode = arr[1];
var price =  arr[2];
var token =  arr[3];
var channelCode = arr[4];
var tarhost = decodeURIComponent(arr[5]);
var backurl = arr[6];

var TmpPayWayId = 0;	// 记录支付方式 临时用
$(function() {
	if (isWeiXin()) {
		var openid = xigou.getSessionStorage("openid");
		if (openid== null || !openid) {
			xigou.getwxOpenidUnion(1);
			openid = xigou.getQueryString('openid');
			xigou.setSessionStorage("openid", openid);
		}
		$("header").hide();
		$("title").html("支付中心");
	}
	
	 	//装载订单列表
				if (isWeiXin()) {
					getOrderlist();
					//支付
					$('.go_payment')[xigou.events.click](function(e) {
						gotoPay(e);
					});
					$('.go_payment').click();
				}
				else {
					InitPage();
				}

				if(tarhost){
					$(".goBack").attr("href",tarhost + "orders.html");
					$(".goIndex").attr("href",tarhost + "index.html");
				}
	if(backurl != undefined && backurl!=null){
		$(".goBack").attr("href",backurl  );
	}
	gotoPay();
});

function getorderdata(){
	var order = new Object();
	order.orderprice = price;
	order.ordercode = ordercode;
	order.payid = payid;
	return order;
}

var getOrderlist = function(){
	var data = getorderdata();
	if(data){


			var ordercount = 1;
			// var sumprice = data.sumprice;
			$('.ordercount').html(ordercount);
			// $('.price').html("¥"+sumprice);
			var orders = new Array();
			orders.push(data);
			//订单列表
			setHtml(orders);
			////支付方式页面
			//$(".select_pay")[xigou.events.click](function(e) {
			//	var payid = e.target.id || $(e.target).parent().attr('id');
			//	window.location.href = "orderspay.html?orders="+JSON.stringify(data['seaorderlineist'])+"&payid="+payid;
			//});
			////订单详情
			//$(".ordernum")[xigou.events.click](function(e) {
			//	var ordernum = $(e.target).attr('name') || $(e.target).parent().attr('name');
			//	window.location.href = "orderdetails.html?ordernum="+ordernum+"&backpage=haitao_orderspay.html";
			//});
			return;

	}

	$.tips({
        content:"您还没有提交订单，请先回到购物车页面提交订单",
        stayTime:2000,
        type:"warn"
    })
};

function setHtml(data){
	
	var html = [];
	var isShowToIndex = false;
	var sumPrice = 0;
   
	/**创建每一条数据**/
	for(var i=0; i<data.length; i++){
		var item= data[i];//每一个单一的数据

		//判断是否0元订单
		var isNeedToPay = true;
		if(parseFloat(item.orderprice) == 0||item.orderprice.toString() == '0.0'){
			isNeedToPay = false;
		}
		
		html.push('<ul class="ui-list ui-list-text ui-border-tb item">');
		html.push('<li>');
		html.push('<h4 class="ordernum" name="'+item.ordercode+'">订单编号：</h4>');
		html.push('<div class="ui-list-text">'+item.ordercode+'</div>');
		html.push('</li>');
		html.push('<li>');
		// html.push('<div class="list_next"></div>');
		html.push('<h4 class="list_next">应付金额：</h4>');
		html.push('<div class="price'+i+'" data="'+item.orderprice+'">¥'+item.orderprice+'</div>');
		html.push('</li>');
		
		//需要支付
		if(isNeedToPay){
			html.push('<li class="">');
			html.push('<h4 class="select_pay" id="'+item.payid+'">支付方式：</h4>');
			var paycode,payname;
			if (isWeiXin()) {
				TmpPayWayId = 0;
				paycode = "weixin"
				payname = "微信支付"
			}
			else {
				TmpPayWayId = 1;
				paycode = "alipayDirect";
				payname = "支付宝支付";
			}
			html.push('<div class="ui-list-text">'+payname+'</div>');
			html.push('</li>');

			// html.push('<div class="btn"><a href="javascript:void(0);" class="go_payment" name="'+i+'" data="'+paycode+'">立即支付</a></div></ul>');
			html.push('<div class="btn"><a href="javascript:void(0);" class="go_payment" name="'+i+'" data="'+paycode+'">立即支付</a></div></ul>');
		}else
		{
			html.push('<div class="ui-border-t btn haspay">已付款</div></ul>');
		}
		
		html.push('<input type="hidden" name="payid'+i+'" value="'+item.payid+'" />');
	}
	//  if(sumPrice == 0){
	// 	 html.push('<div class="null_go"><a href="index.html">去逛逛</a></div></ul>');
	// }
	
	$('.success-block').after(html.join(''));
	
}

//支付 use less....
function gotoPay(e) {

	//var bWeixin = isWeiXin();
	//if (!bWeixin) {
	//	window.location.href = "weixinTip.html";
	//	return;
	//}

	var i = $(e.target).attr('name');
	var payid = $('input[name="payid'+i+'"]').val();
	var paytype = $(e.target).attr('data');

    var price = $(".price"+i).attr('data');
	var ordercode = $(".item").eq(i).find('.ordernum').attr('name');
	var openid = xigou.getSessionStorage("openid");

	var params = {
			'token': token,
			'ordercode': ordercode,
			'payid': payid,
			'payway': paytype,
			'openid': openid,
			'channelcode':channelCode
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
							if(isWeiXin()) {
								onUnionBridgeReady(response.data, payid, ordercode,channelCode,tarhost,backurl);
							}
							else {
								if(response.data.actionurl) {
										var submit_pay = $("#submit_pay");
										var result=xigou.getPaymentForm({
											el:submit_pay,
											datas:response.data,
											payway:TmpPayWayId
										} );
										if(result){
											submit_pay.submit();
										}
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
};


//支付
function gotoPay() {

	//var bWeixin = isWeiXin();
	//if (!bWeixin) {
	//	window.location.href = "weixinTip.html";
	//	return;
	//}


	var openid = xigou.getSessionStorage("openid");

	var params = {
		'token': token,
		'ordercode': ordercode,
		'payid': payid,
		'payway': 'weixin',
		'openid': openid,
		'channelcode':channelCode
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
							if(isWeiXin()) {
								onUnionBridgeReady(response.data, payid, ordercode,channelCode,tarhost,backurl);
							}
							else {
								if(response.data.actionurl) {
									var submit_pay = $("#submit_pay");
									var result=xigou.getPaymentForm({
										el:submit_pay,
										datas:response.data,
										payway:TmpPayWayId
									} );
									if(result){
										submit_pay.submit();
									}
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
};


function InitPage() {
	var data = getorderdata();
	if(data){

		$('.ordercount').html('1');

		//订单列表
		var html = [];
		var sumPrice = 0;

		//判断是否0元订单
		var isNeedToPay = true;
		if(parseFloat(data.price) == 0||data.price.toString() == '0.0'){
			isNeedToPay = false;
		}

		html.push('<ul class="ui-list ui-list-text ui-border-tb item">');
		html.push('<li>');
		html.push('<h4 class="ordernum" name="'+data.ordercode+'">订单编号：</h4>');
		html.push('<div class="ui-list-text">'+data.ordercode+'</div>');
		html.push('</li>');
		html.push('<li>');
		// html.push('<div class="list_next"></div>');
		html.push('<h4 class="list_next">应付金额：</h4>');
		html.push('<div class="price" data="'+data.price+'">¥'+data.price+'</div>');
		html.push('</li>');

		//需要支付
		if(isNeedToPay){
			html.push('<li class="">');
			html.push('<h4 class="select_pay" id="'+data.payid+'">支付方式：</h4>');
			var paycode,payname;
			if (isWeiXin()) {
				TmpPayWayId = 0;
				paycode = "weixin";
				payname = "微信支付"
			}
			else {
				TmpPayWayId = 1;
				paycode = "mergeAlipay";
				payname = "支付宝支付";
			}
			html.push('<div class="ui-list-text">'+payname+'</div>');
			html.push('</li>');

			html.push('<div class="btn"><a href="javascript:void(0);" class="go_payment" data="'+paycode+'">立即支付</a></div></ul>');
		}
		else {
			html.push('<div class="ui-border-t btn haspay">已付款</div></ul>');
		}

		html.push('<input type="hidden" value="'+data.payid+'" />');

		$('.success-block').after(html.join(''));

		gotopaymerge(data);
		return;
	}

	$.tips({
		content:"您还没有提交订单，请先回到购物车页面提交订单",
		stayTime:2000,
		type:"warn"
	})
}

// 合并订单支付
function gotopaymerge(data){
	$('.go_payment')[xigou.events.click](function(e) {
		var submit_pay = $("#submit_pay");
		var result=xigou.getPaymentForm({
			el:submit_pay,
			datas:data,
			payway:'mergeAlipay'
		} );
		if(result){
			submit_pay.submit();
		}
	});
}