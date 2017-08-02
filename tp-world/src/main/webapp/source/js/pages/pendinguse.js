// JavaScript Document
$(function() {
	var page = 1;
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				$('.orders-use').dropload({
			    	scrollArea : window,
			    	loadDownFn : function(me){
						initUnUse(page,me);
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

function initUnUse(page,me) 
{
	/*待使用*/
	xigou.activeUser.allorders({
		requestBody:{
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
							setUseHtml(response,page);
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
				                content:response.msg||"获取待使用数据失败",
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

function setUseHtml(data,page){
	$('.ui-refresh-down').remove();
	var o=data.data.list; //数据集合
	var alllist = data.data.list;
	var length = alllist.length;
	var html = [];
	/**创建每一条数据**/
	for(var i=0; i<length; i++){
		if(alllist[i].ordertype=='8'){  //团购兑换
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
			html.push('<a class="bottom_btn pending_btn_pay_immediatelyuse" ordercode="'+alllist[i].ordercode+'" id="pending_btn_pay_immediatelyuse">立即使用</a>');
			html.push('	</div>');
			html.push('</div>');
		}
	};
	var flgs=false;
	if (data.allpage > page) {
			flgs = true;
		}
		if (page == 1 && html.length == 0) {
			$("#return_list").empty();
			var htmlData = [];
			htmlData.push('<div class="order_empty">还木有订单哟~</div>');
			$('.orders-use').append(htmlData.join(''));
		}
		else {
			$('.orders-use').append(html.join(''));
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

	useImmediatelyuse();   //立即支付
}

//跳转到订单详情页面
function gotoOrderDetails(num,anchor){
	anchor=anchor||"";
	xigou.setSessionStorage('orderdetails_backurl',"inbound.html?st="+$(window).scrollTop());
	window.location.href="orderdetails.html?ordernum="+num;
}

//立即支付
function useImmediatelyuse(){
	$('.pending_btn_pay_immediatelyuse').off(xigou.events.click);
	$(".pending_btn_pay_immediatelyuse")[xigou.events.click](function(){
		var orderItem = $(event.target).parents('.orders-item');
		var ordercode = orderItem.attr("ordercode");
		xigou.setSessionStorage("ordercode", ordercode);

		xigou.setSessionStorage('orderdetails_backurl',"orders.html");
		window.location.href='orderdetails.html?ordercode='+ordercode;
	});
};