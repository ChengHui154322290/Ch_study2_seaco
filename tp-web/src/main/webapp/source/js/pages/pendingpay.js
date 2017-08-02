// JavaScript Document
$(function() {
	var page = 1;
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				$('.orders-pending').dropload({
			    	scrollArea : window,
			    	loadDownFn : function(me){
						payinit(page,me);
                        me.resetload();
                        if(1 == page)
                        {
                            $(".dropload-refresh").hide();
                        }
                        page++;
			    	}
				});
			}else{
				window.location.href="index.html";
			}
		}
	});
});

function payinit(page,me) 
{
	/*待付款*/
	xigou.activeUser.allorders({
		requestBody:{
			token:xigou.getToken(),
			type:"2",
			curpage:page
		},
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				if (null == response) {
					// xigou.alert(xigou.dictionary.chineseTips.server.value_is_null);
					$.tips({
		                content:xigou.dictionary.chineseTips.server.value_is_null,
		                stayTime:2000,
		                type:"warn"
		            })
				} else {
					switch (response.code) {
						case "0":
							setPayHtml(response,page);
							if(response.data.curpage >= response.data.totalpagecount)
							{
								me.lock();
								$(".dropload-down").hide();
							}
							break;
						case "-100":
							$.tips({
				                content:response.msg||"用户失效，请重新登录",
				                stayTime:2000,
				                type:"warn",
				            })

							xigou.removelocalStorage("token");
							window.location.href="logon.html";

							break;
						default:
							$.tips({
				                content:response.msg||"获取待付款订单失败",
				                stayTime:2000,
				                type:"warn"
				            })
							me.lock();
							$(".dropload-down").hide();
							// xigou.alert(response.rescode.info||"获取待付款订单失败");
							break;
					}
				}
			} else {
				$.tips({
				    content:'请求失败，详见' + response,
				    stayTime:2000,
				    type:"warn"
				})
				// xigou.alert('请求失败，详见' + response);
			}
		}
	});
};

//解析时间
function  formatDate(now)   {     
	var time = new Date(now),
		year = time.getFullYear(),
		month = time.getMonth()+1,
		date = time.getDate(),
		hour = time.getHours(),
		minute = time.getMinutes(),
		second = time.getSeconds();setHtml
	   
	return   year+"-"+addtime(month)+"-"+addtime(date)+"　"+addtime(hour)+":"+addtime(minute)+":"+addtime(second);     
} 
// 补0
function  addtime(s){
	return s < 10 ? '0'+s : s ; 
}
function setPayHtml(data,page){
	$('.ui-refresh-down').remove();
	var o=data.data.list; //数据集合
	var alllist = data.data.list;
	var length = alllist.length;
	var html = [];
	/**创建每一条数据**/
	for(var i=0; i<length; i++){
		html.push('<div class="orders-item" ordercode="' + alllist[i].ordercode + '">');
		html.push('	<div class="order_time_status">' + alllist[i].ordertime);
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
		html.push('<a class="bottom_btn pending_btn_pay" ordercode="'+alllist[i].ordercode+'" id="pending_btn_pay">立即支付</a> ');
		html.push('<a class="bottom_btn pending_btn_pay_cancal" ordercode="'+alllist[i].ordercode+'" id="pending_btn_pay_cancal">取消订单</a>');
		html.push('	</div>');
		html.push('</div>');
	};
	var flgs=false;
	if (data.allpage > page) {
		flgs = true;
	}
	if (page == 1 && html.length == 0) {
		$("#return_list").empty();
		var htmlData = [];
		htmlData.push('<div class="order_empty">还木有订单哟~</div>');
		$('.orders-pending').append(htmlData.join(''));
	}
	else {
		$('.orders-pending').append(html.join(''));
	}

	$('.order_info').off(xigou.events.click);
	$(".order_info")[xigou.events.click](function(){
		var orderItem = $(event.target).parents('.orders-item');
		var ordercode = orderItem.attr("ordercode");
		xigou.setSessionStorage("ordercode", ordercode);

		xigou.setSessionStorage('orderdetails_backurl',"orders.html");
		window.location.href='orderdetails.html?ordercode='+ordercode;
	});

	pendingcancel();
	pendinggotoPay();
}

//跳转到订单详情页面
function gotoPendingPay(ordernum){
	xigou.setSessionStorage('orderdetails_backurl',"pendingpay.html");
	window.location.href="orderdetails.html?ordernum="+ordernum;
}

function go_payment(ordernum,tuntype,ordertype,channelid){
	var href = "payagain.html?tuntype="+tuntype+"&ordernum="+ordernum;
	//window.location.href="payagain.html?tuntype="+tuntype+"&ordernum="+ordernum+"&ordertype="+ordertype+"&channelid="+channelid;
	if(ordertype)
		href += "&ordertype="+ordertype;
	if(channelid)
		href += "&channelid="+channelid;
	window.location.href = href;
}

function pendinggotoPay() {

	$('.pending_btn_pay').off(xigou.events.click);
	$('.pending_btn_pay')[xigou.events.click](function(e) {

		var token = xigou.getToken();
		var ordercode=$(this).attr("ordercode");
		var payid=$(this).attr("payid");
		var payway=$(this).attr("payway");
		var openid = xigou.getLocalStorage("openid");

		var payway = "weixin";
		if (!isWeiXin())
		{
			payway = "mergeAlipay";
		}
		
		var pay_openid="";//全球购openID  用于支付
		if (isWeiXin()) {//微信支付
			pay_openid=xigou.getQueryString("pay_openid");
			if (pay_openid == "" || pay_openid == null) {
				pay_openid = xigou.getLocalStorage("pay_openid");// 微信支付openId
				if (pay_openid == "" || pay_openid == null) {
					callURL = getXgOpenIdUrl + "&redictBackUrl="
							+ encodeURIComponent(location.href);
					window.location.href = callURL;
				}
			} else {
				xigou.setLocalStorage("pay_openid", pay_openid)
				
			}
			openid=pay_openid;
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

//取消
function pendingcancel(){
	$('.pending_btn_pay_cancal').off(xigou.events.click);
	$('.pending_btn_pay_cancal')[xigou.events.click](function() {
		var ordercode=$(this).attr("ordercode");
		var tar = this;
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
									tar.innerHTML="已取消";
									$(tar).first().unbind("ontouchend" in document ? "tap" : "click");

									window.location.href = "orders.html?defaule=1";
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
		},50)

	});	
};