$(function() {
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				initChangeBtn();
				initRecentChangeList();
				InitWeixin();
			}else{
				window.location.href="index.html";
			}
		}
	});
	
	if(!isWeiXin()){
		$("header").show();
		$(".index_scan").css({
			"top":"63px"
		})
		$(".search").css({
			"margin-top":"61px"
		})
	}
});
/**兑换*/
function initChangeBtn(){
	var btn = $('.btn');
	btn[xigou.events.click](function(){
		var redeemCode = $('.redeemcode').val();
		if($.trim(redeemCode)==''){
			$.dialog({
		        title:'',
		        content:'请输入兑换码',
		        button:["确认"]
		    });
			return false;
		}
		var params = {
				'token': xigou.getToken(),
				'ccode':redeemCode,
				'sendtype':1//明码
		};
		xigou.redeemManager.exchange({
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
								setExchangeInfo(response.data);
								initRecentChangeList();
								break;
							default:
								$.dialog({
							        title:'',
							        content:response.msg || "验证失败",
							        button:["确认"]
							    });
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
}

function InitWeixin(){
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
					'scanQRCode',
				]
			});
		}
	});
	xigou.loading.close();
}

wx.ready(function () {
	// 1 判断当前版本是否支持指定 JS 接口，支持批量判断
	wx.checkJsApi({
		jsApiList: [
			'getNetworkType',
			'previewImage',
			'scanQRCode'],
		success: function (res) {
		}
	});
	scancode();
	xigou.loading.close();
	xigou.wxreadyalert();
});

wx.error(function (res) {
});


function scancode(){
	if(isWeiXin()){
		$('.index_scan')[xigou.events.click](function(){
			var dia=$.dialog({
				title:'',
				content:"确认扫码核销？",
				button:["取消","确认"]
			});
			dia.on("dialog:action",function(e){
				if(e.index == 1){
					wx.scanQRCode({
					    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
					    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
					    success: function (res) {
					    	var redeemCode = res.resultStr;
					    	var params = {
									'token': xigou.getToken(),
									'ccode':redeemCode,
									'sendtype':2 //二维码
							};
					    	xigou.redeemManager.exchange({
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
													setExchangeInfo(response.data);
													initRecentChangeList();
													break;
												default:
													$.dialog({
												        title:'',
												        content:response.msg || "验证失败",
												        button:["确认"]
												    });
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
					    }
					});
				}
			});
		});
	}
}
/**设置到核销列表*/
function setExchangeInfo(data){
	var list = xigou.getLocalStorage('redeemcodechangelist');
	if(list){
		list =eval('('+list.replace(/:null/g, ":\"\"")+')');
	}else{
		list = [];
	}
	list.unshift(data);
	if(list.length>4){
		list = list.slice(0,4);
	}
	list = JSON.stringify(list);
	xigou.setLocalStorage('redeemcodechangelist',list);
}
/**近期核销列表*/
function initRecentChangeList(){
	var list = xigou.getLocalStorage('redeemcodechangelist');
	if(list){
		list = JSON.parse(list.replace(/:null/g, ":\"\""));
	}
	var list = $(list);
	var html=[];
	for(var i=0; i<list.length; i++){
		var salesPrice = parseFloat(list[i].salesPrice).toFixed(2);
		var redeemCodeState = list[i].redeemCodeState;
		var refunedAmount = '';
		var amount='+'+salesPrice;
		html.push('<div class="history_record">');
		html.push('<div class="date">核销时间  '+list[i].updateTimeCn.replace(/(.*):\d{2}$/,'$1')+'</div>');
		html.push('<div class="history_item">');
		html.push('<div class="goods_name">'+list[i].spuName+'</div>');
		html.push('<div class="goods_num">兑换码 '+list[i].redeemCode+'<span class="cust">操作账号  '+list[i].updateUser+'</span></div>');	
		html.push('</div>');			
		html.push('</div>');
	}
	$('.histroy').empty().append('<h3>一   近期核销记录   一</h3>').append(html.join(''));
}