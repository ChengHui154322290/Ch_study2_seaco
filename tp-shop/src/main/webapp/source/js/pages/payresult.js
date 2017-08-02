/**
 *
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-14 13:41:54
 * @version Ver 1.0.0
 */
$(function() {
	$(".content").hide();
    var params = {
    	'token': xigou.getToken(),
    	'ordercode': xigou.getQueryString("ordercode"),
        'payid': xigou.getQueryString("pid"),
    };
    xigou.activePayresults.status({
        requestBody: params,
        callback:function(response, status){
            if (status == xigou.dictionary.success) {
                var json = response || null;
                if (json == null || json.length == 0) {
                    $.tips({
                        content:"网络错误123!",
                        stayTime:2000,
                        type:"warn"
                    })
                }
                else
                {
                    $.tips({
                        content:json.msg || "网络错误！",
                        stayTime:2000,
                        type:"warn"
                    })

                    pay_success(json);
                    $("#div_payrest").html(response.data.statusdesc);
                }
            }
            else
            {
            	window.location.href = "orders.html";
            }
        }
    })
});

function pay_success(json) {
	$(".content").show();
	$('.viewshop')[xigou.events.click](function() {
		window.location.href = "index.html";
	});
	$('.vieworder')[xigou.events.click](function() {
		window.location.href = "orderdetails.html?ordercode="+xigou.getQueryString("ordercode");
	});
	
	
	//json
	//动态加载出团购数据
	var data = json.data;
	var orderRedeemItemList = data.orderRedeemItemList;
	if(orderRedeemItemList.length>0){
		//获取Datatime格式的到期时间
		var effectTimeEnd = orderRedeemItemList[0].effectTimeEnd;
		var timeEnd = new Date(effectTimeEnd);
		function FormatDate (strTime) {
		    var date = new Date(strTime);
		    return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
		}
		var expiretime = FormatDate(timeEnd);
		
		$(".div_add_info").hide();
		$(".coupon_code").show();
		$(".couponcode_message_name").html(orderRedeemItemList[0].shopName);     //劵码店铺名
		$(".couponcode_message_time").html(expiretime);			//劵码到期时间
		var html = [];
	    //动态加载劵码
		for(var i = 0;i<orderRedeemItemList.length;i++){
			
			html.push('<li><p><span class="redeem_name">'+orderRedeemItemList[i].redeemName+'</span><span class="redeem_code_nub">'+orderRedeemItemList[i].redeemCode+'</p></li>');
		}
		$("#redeem_code").html(html.join(""));
	}
};
