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
                $('.coupon-used').dropload({
                    scrollArea : window,
                    loadDownFn : function(me){
                        used(page,me);
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

function usedlookrule() {
    var ruleStr = "1.本券可与红包叠加使用<br>"+
                "2.本券不可与其他优惠券叠加使用<br>"+
                "3.本券使用后不可找零<br>"+
                "4.本券不可抵扣运费及税金";
    $('.list .r>span:last-child')[xigou.events.click](function() {
        xigou.alert({
            message:'<p style="text-align:left;margin-left:15px;color: #333;font-size: 1.4rem;">'+ruleStr+'</p>',
            params:{
                title: '使用规则'
            }
        });
    });
}

//用户-优惠券-已使用
function used(page, me){
	var params = {
		token: xigou.getToken(),
		type:"1",
        status:"1",
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
							//setHtml(response);
							
							usedsetHtmlView(response);
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

function usedsetHtmlView(data) {
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

    var empty_div = '<div class="empty_box">没有已使用的优惠券</div>';

    var usedTpl0 ='<div class="ui-flex ui-flex-ver ui-flex-pack-center ui-flex-align-start">'
        +'<div class="coupon-item" >'
            +'<div class="coupon-used-info">'
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
    var usedTpl1 ='<div class="ui-flex ui-flex-ver ui-flex-pack-center ui-flex-align-start">'
        +'<div class="card-item" >'
            +'<div class="card-used-info">'
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
		
			
	//已使用
	for (var j=0,len;j<couponList.coupon1.length; j++) {
	    var usedList = [];	
		var obj = couponList.coupon1[j];
		if(obj.type =="0"){
			var usedStr = $.tpl(usedTpl0,obj);
			usedList.push(usedStr);  
			if(usedList.length == 0){
				$(".coupon-used").html(empty_div);
			}else{
				 $(".coupon-used").append(usedList.join(""));
			}
		}else if(obj.type =="1"){
			if(obj.exchangeXgMoney == "1"){
				var usedStr = $.tpl(usedTpl1,obj);
				usedList.push(usedStr);
				if(usedList.length == 0){
					$(".card-used").html(empty_div);
				}else{
					 $(".card-used").append(usedList.join(""));
				}
			}else{				
				var usedStr = $.tpl(usedTpl0,obj);
				usedList.push(usedStr);
				if(usedList.length == 0){
					$(".card-used").html(empty_div);
				}else{
					 $(".card-used").append(usedList.join(""));				
				}
			}
			
		}
	} 
	if(couponList.coupon0.length == 0 && $('.card-unused').find('.ui-flex').length == 0 && $('.coupon-unused').find('.ui-flex').length == 0){
    	$('.nocard0').show();
    }else{
    	$('.nocard0').hide();
    }
    if(couponList.coupon1.length == 0 && $('.card-unused').find('.ui-flex').length == 0 && $('.coupon-unused').find('.ui-flex').length == 0){
    	$('.nocard1').show();
    }else{
    	$('.nocard1').hide();
    }
    if(couponList.coupon2.length == 0 && $('.card-expired').find('.ui-flex').length == 0 && $('coupon-expired').find('.ui-flex').length == 0){
    	$('.nocard2').show();
    }else{
    	$('.nocard2').hide();
    }
}
