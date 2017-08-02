var UserToken = xigou.getToken();
var page = 1;
var orders_max = "N";
$(function(){
		if(!isWeiXin()){
			$("header").show();
			$(".histroy").css({
				"margin-top":"65px"
			});
		};
	xigou.getLoginUserInfo({
		callback: function(userinfo, status) {
			if (status == xigou.dictionary.success) {
				$('.histroy').dropload({
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
				window.location.href = "store_record.html";
			}
		}
	});
	$('.title')[xigou.events.click](function(){
		window.location.href="store_record.html";
	});
});
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
	var redeemCodeState = 2;
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
		allpage = o.allpage,   //总页数
		count = o.page,       //当前页数量  
		alllist = o.rows,   // 全部订单
		flgs = false,
		html = [];
	if(o!=null && length>0){
		for(var i=0; i<length; i++){
			var salesPrice = parseFloat(alllist[i].salesPrice).toFixed(2);
			var redeemCodeState = alllist[i].redeemCodeState;
			var refunedAmount = '';
			var amount='+'+salesPrice;
			html.push('<div class="history_record">');
			html.push('<div class="date">核销时间  '+alllist[i].updateTimeCn.replace(/(.*):\d{2}$/,'$1')+'</div>');
			html.push('<div class="history_item">');
			html.push('<div class="goods_name">'+alllist[i].spuName+'</div>');	
			html.push('<div class="goods_num">券号 '+alllist[i].redeemCode+'<span class="cust">操作账号  '+alllist[i].updateUser+'</span></div>');			
			html.push('</div>');
			html.push('</div>');
		}
	}
	if(length == 0 || data.page == data.total) {
		orders_max = "Y";
	}
	if (page == 1 && html.length == 0) {
		$('.nothing').show();
		$('.histroy').empty().hide();
	} else {
		$('.nothing').hide();
		$('.histroy').append(html.join(''));
	}
}