// JavaScript Document
$(function() {
	var page = 1;
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				$('.orders-ship').dropload({
			    	scrollArea : window,
			    	loadDownFn : function(me){
						init(page,me);
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


function init(page,me) 
{
	/*待收货*/
	xigou.activeUser.allorders({
		requestBody:{
			'eshopPromoterId':xigou.getLocalStorage('promoterid'),
			token:xigou.getToken(),
			type:"3",
			curpage:page
		},
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
							setHtml(response,page);
							if(response.data.curpage >= response.data.totalpagecount)
							{
								me.lock();
								$(".dropload-down").hide();
							}
							break;
						case "-100":
							$.tips({
				                content:response.msg||"用户失效，请重新登录。",
				                stayTime:2000,
				                type:"warn"
				            })

							xigou.removelocalStorage("token");
							window.location.href="logon.html"

							break;
						default:
							$.tips({
				                content:response.msg||"获取待收货数据失败",
				                stayTime:2000,
				                type:"warn"
				            })
				            me.lock();
							$(".dropload-down").hide();
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

//解析时间
function  formatDate(now)   {     
	var time = new Date(now),
		year = time.getFullYear(),
		month = time.getMonth()+1,
		date = time.getDate(),
		hour = time.getHours(),
		minute = time.getMinutes(),
		second = time.getSeconds();
		
	   
	return   year+"-"+addtime(month)+"-"+addtime(date)+" "+addtime(hour)+":"+addtime(minute)+":"+addtime(second);     
} 

// 补0
function  addtime(s){
	return s < 10 ? '0'+s : s ; 
}  

function setHtml(data,page){
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
				html.push('			<div>' + item.specs[0] + '</div>');
			}
			html.push('		</div>');
			html.push('	</div>');
			html.push('</div>');
		};
		html.push('	<div class="order_totle_count_price">');
		html.push('		<span class="float_right ">共 ' + alllist[i].linecount + ' 件 实付：<span class="order_lineprice">¥' + alllist[i].orderprice + '</span></span>');
		html.push('	</div>');
		html.push('	<div class="order_btns">');
		html.push('<a class="bottom_btn ship_btn_pay_look" ordercode="'+alllist[i].ordercode+'" id="ship_btn_pay_look">订单追踪</a> ');
		html.push('<a class="bottom_btn ship_btn_pay_sure" ordercode="'+alllist[i].ordercode+'" id="ship_btn_pay_sure">确认收货</a> ');
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
			$('.orders-ship').append(htmlData.join(''));
		}
		else {
			$('.orders-ship').append(html.join(''));
		}

	setTimeout(function(){
		var st=xigou.getQueryString("st")||"0";
		window.scrollTo(0,parseInt(st));
	},100);

	$('.order_info').off(xigou.events.click);
	$(".order_info")[xigou.events.click](function(){
		var orderItem = $(event.target).parents('.orders-item');
		var ordercode = orderItem.attr("ordercode");
		xigou.setSessionStorage("ordercode", ordercode);

		xigou.setSessionStorage('orderdetails_backurl',"orders.html");
		window.location.href='orderdetails.html?ordercode='+ordercode;
	});	

	shipconfirmReceipt();
	shippaylook();
}

//跳转到订单详情页面
function gotoOrderDetails(num,anchor){
	anchor=anchor||"";
	xigou.setSessionStorage('orderdetails_backurl',"inbound.html?st="+$(window).scrollTop());
	window.location.href="orderdetails.html?ordernum="+num;
}

//收货
function receiving(_this,ordernum){
	xigou.confirm({
		text:"您是否确认收货?",
		callback:function(res){
			if(res=="ok"){

				/*确认收货*/
				xigou.activeUser.confirmshipment({
					requestBody:{
						token:xigou.getToken(),
						ordernum:ordernum
					},
					callback: function(response, status) { //回调函数
						if (status == xigou.dictionary.success) {
							if (null == response) {
								$.tips({
								    content:xigou.dictionary.chineseTips.server.value_is_null,
								    stayTime:2000,
								    type:"warn"
								})
								// xigou.alert(xigou.dictionary.chineseTips.server.value_is_null);
							} else {
								switch (response.code) {
									case "0":
										if(response.ordernum){
											var _t=$(_this).parent().parent().parent();
											_t.hide();
											_t.parent().find(".ready_receiving").text("已完成").addClass("completed");
											//
											$.tips({
											    content:response.msg||"确认收货成功",
											    stayTime:2000,
											    type:"warn"
											})
										}else{
											$.tips({
											    content:response.msg||"确认收货失败",
											    stayTime:2000,
											    type:"warn"
											})
										}
										break;
									case "-100":
										$.tips({
											    content:response.msg||"用户失效，请重新登录。",
											    stayTime:2000,
											    type:"warn"
											})

										xigou.removelocalStorage("token");
										window.location.href="logon.html"
										break;
									default:
										$.tips({
											    content:response.msg||"确认收货失败",
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
		}
	});
}

//确认收货
function shipconfirmReceipt(){
	$('.ship_btn_pay_sure').off(xigou.events.click);
	$('.ship_btn_pay_sure')[xigou.events.click](function() {
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

						window.location.href = "orders.html?defaule=2";
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

// 订单追踪
function shippaylook(){
	$('.ship_btn_pay_look').off(xigou.events.click);
	$('.ship_btn_pay_look')[xigou.events.click](function() {
		var ordercode=$(this).attr("ordercode");
		xigou.setSessionStorage("logisticordercode", ordercode);
		window.location.href = "ordertracking.html?ordercode="+ordercode;
	});
};