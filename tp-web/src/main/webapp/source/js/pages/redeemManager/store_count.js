var UserToken = xigou.getToken();
var page = 1;
var orders_max = "N";
$(function(){
	xigou.getLoginUserInfo({
		callback: function(userinfo, status) {
			if (status == xigou.dictionary.success) {
				initStatics();
				$('.contents').dropload({
			    	scrollArea : window,
			    	loadDownFn : function(me){
						initPageList(page,me);
                        me.resetload();
                        if(1 == page){
                            $(".dropload-refresh").hide();
                        }
                        page++;
			    	}
				});
			} else {
				window.location.href = "store_sales.html";
			}
		}
	});
	$('.title')[xigou.events.click](function(){
		window.location.href="store_sales.html";
	});
});

/**获取统计数据*/
function initStatics(){
	var params = initParams();
	
	xigou.redeemManager.count({
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
							 var json = response.data;
							 $('.expectincome').text('+'+json.expectIncome);
							 $('.refunedamount').text('-'+json.refunedAmount);
							 $('.actualincome').text('+'+json.actualIncome);
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
}
/**获取团购券列表*/
function initPageList(page,me){
	if(orders_max == "Y"){
		me.disable("down",false);
		$('.ui-refresh-down>span:first-child').removeClass();
		return false;
	}
	var params = initParams();
	params.curpage= page;
	xigou.redeemManager.list({
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
							initHtml(response.data,page);
							if(response.data.page >= response.data.total)
							{
								me.lock();
								$(".dropload-down").hide();
							}
							break;
						default:
							me.lock();
							$(".dropload-down").hide();
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
}

/**获取查询参数*/
function initParams(){
	var skuCode = xigou.getSessionStorage('skucode');
	var redeemCodeState = xigou.getSessionStorage('redeemstatus');
	var redeemCode = xigou.getSessionStorage('redeemcode');
	var beginDate = xigou.getSessionStorage('beginquerydate');
	var endDate = xigou.getSessionStorage('endquerydate');
	if($.trim(skuCode)==''){
		skuCode=null;
	}
	if($.trim(redeemCode)==''){
		redeemCode=null;
	}
	var param = {
		'token': UserToken,	
		'skuCode':skuCode,
		'redeemCodeState':redeemCodeState,
		'redeemCode':redeemCode,
		'beginDate':beginDate,
		'endDate':endDate
	}
	return param;
}

function initHtml(data,page){
	var o = data,
		length = o.rows.length,   //订单总数量
		allpage = o.total,   //总页数
		count = o.page,       //当前页数量  
		alllist = o.rows,   // 全部订单
		flgs = false,
		size 
		html = [];
	if(length>0){
		for(var i=0; i<length; i++){
			var salesPrice = parseFloat(alllist[i].salesPrice).toFixed(2);
			var redeemCodeState = alllist[i].redeemCodeState;
			var refunedAmount = '';
			var amount='+'+salesPrice;
			html.push('<div class="content_items">');
			html.push('<div class="date">'+alllist[i].createTimeCn.replace(/(.*):\d{2}$/,'$1')+'</div>');
			html.push('<div class="goods_info">');
			html.push('<div class="goods_info_left">');
			html.push('<h3 class="goods_name">'+alllist[i].spuName+'</h3>');	
			html.push('<div class="goods_price">¥'+salesPrice+'</div>');			
			html.push('<div class="goods_num">券号 '+alllist[i].redeemCode+'</div>');			
			html.push('<div class="goods_state">'+alllist[i].redeemCodeStateCn+'　'+alllist[i].updateTimeCn.replace(/\d{4}\-(.*):\d{2}$/,'$1')+'</div>');		
			html.push('</div>');		
			html.push('<div class="goods_info_right">');	
			html.push('<ul>');	
			html.push('<li class="li">+'+salesPrice+'<i></i></li>');
			if(redeemCodeState!=1 && redeemCodeState!=2){
				refunedAmount = salesPrice;
				amount = '-';
			}
			html.push('<li class="li li2">-'+refunedAmount+'</li>');
			html.push('<li>'+amount+'</li>');
			html.push('</ul>');	
			html.push('</div>');
			html.push('</div>');
			html.push('</div>');
		}
	}
	if(length == 0 || data.page == data.total) {
		orders_max = "Y";
	}
	if (page == 1 && html.length == 0) {
		$(".contents").empty();
		var htmlData = [];
		htmlData.push('<div class="nothing"><div class="nothing_img"><img src="img/store/sales_no.png"></div><div class="nothing_word">还没有销量数据哟</div></div>');
		$('.contents').append(htmlData.join(''));
		$('.nothing').show();
	} else {
		$('.contents').append(html.join(''));
	}
}