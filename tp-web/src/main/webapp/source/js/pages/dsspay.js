/**
 * 
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-14 13:41:54
 * @version Ver 1.0.0
 */

//与安卓或者IOS交互时隐藏头部
$(function() {
	
	var Type = xigou.getQueryString("type");
	if (1 != Type) {
		$('.bottom').show();
		webViem();
	}else{
		$('img').css("margin-bottom","-7px");
	}
});
function webViem() {
	var page = 1;
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				ordersgotoPay();
				var nDefaulePage = xigou.getQueryString("defaule");
				if (nDefaulePage)
				{
					setdefaulesel(nDefaulePage);
				}
			}else{
				window.location.href="../logon.html";
			}
		}
	});
};

function ordersgotoPay() {

	$("#open_discount_btn").click(function(e) {

		var token = xigou.getToken();
		var ordercode=xigou.getSessionStorage("ordercode");
		var payid=xigou.getSessionStorage("payid");
		var payway=xigou.getSessionStorage("payway");
		var openid = xigou.getLocalStorage("openid");
					
		if (!isWeiXin())
		{
			payway = "alipayDirect";
		}else{
			payway = "weixin";
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
									onDssBridgeReady(response.data, payid, ordercode);
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
								window.location.href = "../payresult.html?pid="+payid+"&ordercode="+ordercode;
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



// 订单追踪
function orderpaylook(){
	$('.orders_btn_pay_look')[xigou.events.click](function() {
		var ordercode=$(this).attr("ordercode");
		xigou.setSessionStorage("logisticordercode", ordercode);
		window.location.href = "../ordertracking.html?ordercode="+ordercode;
	});
};