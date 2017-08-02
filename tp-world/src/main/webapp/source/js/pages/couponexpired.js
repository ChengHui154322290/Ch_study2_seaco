/**
 * 
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-12 17:16:47
 * @version Ver 1.0.0
 */

$(function() {
    var page = 1;
    xigou.getLoginUserInfo({
        callback:function(userinfo,status){
            if(status==xigou.dictionary.success){
                $('.coupon-expired').dropload({
                    scrollArea : window,
                    loadDownFn : function(me){
                        expired(page,me);
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

//用户-优惠券-已过期
function expired(page, me){
	var params = {
		token: xigou.getToken(),
		type:"1",
        status:"2",
        curpage:page
	};

	xigou.activeUser.haved({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				if (null == response) {
					$.tips({
                        content:xigou.dictionary.chineseTips.server.value_is_null,
                        stayTime:2000,
                        type:"warn"
                    });
				} else {
					switch (response.code) {
						case "0":
							/*if(!response.count){
								window.location.href="couponget.html";
							}*/
							//setHtml(response);}	
							expiredsetHtmlView(response);
							break;
						case "-100":
                            var dia=$.dialog({
                                    title:'',
                                    content:response.msg||"用户失效，请重新登录。",
                                    button:["确认"]
                                });

                                dia.on("dialog:action",function(e){
                                    console.log(e.index);
                                    xigou.removelocalStorage("token");
                                    window.location.href="logon.html";
                                });
							break;
						default:
                            $.tips({
                                content:response.msg||"获取优惠券信息失败",
                                stayTime:2000,
                                type:"warn"
                            });
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
                });
                me.lock();
                $(".dropload-down").hide();
			}
		}
	});
};


function expiredsetHtmlView(data) {
    //“0”可用 “1”已使用 “2”已过期
    var couponList = {
        coupon0:[],
        coupon1:[],
        coupon2:[]
    };

    if(data && data.data.list){
        for(var d in data.data.list){
            var c=data.data.list[d];
            couponList["coupon"+(c.status)].push(c);
        }
    }

    // var btn_img = __uri("../../images/coupon/btn.png"),
    //     btn_gray_img = __uri("../../images/coupon/btn_gray.png");

    var btn_img = "img/coupon/dot.png",
        btn_gray_img = "img/coupon/dot.png";

    var empty_div = '<div class="empty_box">没有可使用的优惠券</div>';
    
    var expiredTpl0 ='<div class="ui-flex ui-flex-ver ui-flex-pack-center ui-flex-align-start">'
        +'<div class="coupon-item" >'
            +'<div class="coupon-expired-info">'
                +'<div class="item_price"><span>¥</span><%=price%></div>'
                +'<div class="coupon-introduce">'
                    +'<div class="coupon-name"><%=name%></div>'
                    +'<ul>'
                        +'<li><%=validtime%></li>'
                        +'<li><%=rule%></li>'
                    +'</ul>'
                +'</div>'
            +'</div>'
        +'</div>'
    +'</div>';
    
    var expiredTpl1 ='<div class="ui-flex ui-flex-ver ui-flex-pack-center ui-flex-align-start">'
        +'<div class="card-item" >'
            +'<div class="card-expired-info">'
                +'<div class="item_price"><span>¥</span><%=price%></div>'
                +'<div class="card-introduce">'
                    +'<div class="card-name"><%=name%></div>'
                    +'<ul>'
                        +'<li><%=validtime%></li>'
                        +'<li>可直接兑换成西客币</li>'
                    +'</ul>'
                +'</div>'
            +'</div>'
        +'</div>'
    +'</div>';
    
					
	//已过期
	for (var k=0,len;k<couponList.coupon2.length; k++) {
		var expiredList = [];	
		var obj = couponList.coupon2[k];
		if(obj.type== "0"){
			var expiredStr = $.tpl(expiredTpl0,obj);
			expiredList.push(expiredStr);
			if(expiredList.length == 0){
				$(".coupon-expired").html(empty_div);
			}else {
				$(".coupon-expired").append(expiredList.join(""));
			} 
		}else{
			if(obj.exchangeXgMoney == "1"){
				var expiredStr = $.tpl(expiredTpl1,obj);
				expiredList.push(expiredStr);
				if(expiredList.length == 0){
					$(".card-expired").html(empty_div);
				}else {
					$(".card-expired").append(expiredList.join(""));
				} 
			}else{
				var expiredStr = $.tpl(expiredTpl0,obj);
				expiredList.push(expiredStr);
				if(expiredList.length == 0){
					$(".card-expired").html(empty_div);
				}else {
					$(".card-expired").append(expiredList.join(""));
				} 
			}
			
		}
		            
	}
	   
}