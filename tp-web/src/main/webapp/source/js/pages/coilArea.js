var page = 1;
$(function(){
	if(isWeiXin()){
		$("header").hide();
		$("title").html("领取专区");
		$(".coils").css({"margin-top":"15px"});
		$(".coils").css({"margin-bottom":"87px"});
		$(".coupon_wx").show();
	}
})

$(function() {
    xigou.getLoginUserInfo({
        callback:function(userinfo,status){
            if(status==xigou.dictionary.success){
            	var telephone = userinfo.telephone;
            	$('.coils').dropload({
                    scrollArea : window,
                    loadDownFn : function(me){
                    	receiveCenterlist(telephone,page,me);
                    	me.resetload();
                        if(1 == page)
                        {
                            $(".dropload-down").hide();
                        }
                        page++;
                    }
                });
            }else{
                window.location.href="logon.html";
            }
        }
    });
});

var getWithdrawDetail_max = "N";

//用户-优惠券-未使用
function receiveCenterlist(telephone,page, me){
	if(getWithdrawDetail_max == "Y"){
		return false;
	}
	else{
		if(telephone == undefined){
			window.location.href="logon.html";
		}else{
			var params = {
					mobile: telephone,
					startPage:page
				};
			
				xigou.activeUser.receiveCenterlist({
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
										setHtmlView(response,telephone);	
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
		}
		
	}
};

//数据加载
function setHtmlView(response,telephone){
	var data = response.data;
	if(data.list && data.list != "[]"){
		for(var i = 0;i<data.list.length;i++){
			var coupon = data.list[i];
			var operate = coupon.operate;
			if(operate == "1"){
				ImmediatelyReceive(coupon,telephone)
			}else if(operate == "2"){
				ImmediatelyUse(coupon);
			}else if(operate == "3"){
				LootAll(coupon);
			}
		}
	}else {
		Nothing();
	}
}
//立即领取
function ImmediatelyReceive(data,telephone){
	var price = data.price;
	var couponImagePath = data.couponImagePath;
	var name = data.name;
	var needOverMon = data.needOverMon;
	var couponcode = data.couponcode;
	var htmlData = [];
		htmlData.push('<div class="coils_receive_list">');
		htmlData.push('   <div class="kinds coils_receive_kinds">');
		htmlData.push('       <div class="price"><span style="font-size:12px;padding-right: 3px;">¥</span>' + price + '</div>');
		htmlData.push('       <div class="types">优惠券</div>');
		htmlData.push('   	  <a class="now receive_now" onclick=receiveCoupon("' + couponcode + '","' + telephone + '")>立即领取</a>');
		htmlData.push('   </div>');
		htmlData.push('   <div class="list_img box-shadow"><img src="' + couponImagePath + '"/></div>');
		htmlData.push('   <div class="list_name">' + name + '</div>');
		htmlData.push('   <div class="line"></div>');
		if(needOverMon != "0"){
			htmlData.push('   <div class="list_condition">满' + needOverMon + '可用</div>');
		}
		htmlData.push('   <img class="redTop" src="img/coilArea/red_top.png"/>');
		htmlData.push('   <img class="redBottom" src="img/coilArea/red_bottom.png"/>');
		htmlData.push('</div>');
		
	$(".coils").append(htmlData.join(""));
}

//立即使用
function ImmediatelyUse(data){
	var price = data.price;
	var couponImagePath = data.couponImagePath;
	var name = data.name;
	var needOverMon = data.needOverMon;
	var couponid= data.couponUserId;
	var htmlData = [];
		htmlData.push('<div class="coils_use_list">');
		htmlData.push('   <div class="kinds coils_use_kinds">');
		htmlData.push('       <div class="price"><span style="font-size:12px;padding-right: 3px;">¥</span>' + price + '</div>');
		htmlData.push('       <div class="types">优惠券</div>');
		htmlData.push('   	  <a class="now use_now" href="search.html?couponid=' + couponid + '">去使用</a>');
		htmlData.push('   </div>');
		htmlData.push('   <div class="list_img box-shadow"><img src="' + couponImagePath + '"/></div>');
		htmlData.push('   <div class="list_name">' + name + '</div>');
		htmlData.push('   <div class="line"></div>');
		htmlData.push('   <div class="list_status"></div>');
		if(needOverMon != "0"){
			htmlData.push('   <div class="list_condition">满' + needOverMon + '可用</div>');
		}
		htmlData.push('   <img class="blueTop" src="img/coilArea/blue_top.png"/>');
		htmlData.push('   <img class="blueBottom" src="img/coilArea/blue_bottom.png"/>');
		htmlData.push('</div>');
		
	$(".coils").append(htmlData.join(""));
	
}

//已抢光
function LootAll(data){
	var price = data.price;
	var couponImagePath = data.couponImagePath;
	var name = data.name;
	var needOverMon = data.needOverMon;
	var htmlData = [];
		htmlData.push('<div class="coils_expire_list">');
		htmlData.push('   <div class="kinds coils_expire_kinds">');
		htmlData.push('       <div class="price"><span style="font-size:12px;padding-right: 3px;">¥</span>' + price + '</div>');
		htmlData.push('       <div class="types">优惠券</div>');
		htmlData.push('   	  <a class="expire_now">已抢光</a>');
		htmlData.push('   </div>');
		htmlData.push('   <div class="list_img box-shadow"><img src="' + couponImagePath + '"/></div>');
		htmlData.push('   <div class="list_name">' + name + '</div>');
		htmlData.push('   <div class="line"></div>');
		if(needOverMon != "0"){
			htmlData.push('   <div class="list_condition">满' + needOverMon + '可用</div>');
		}
		htmlData.push('   <img class="grayTop" src="img/coilArea/gray_top.png"/>');
		htmlData.push('   <img class="grayBottom" src="img/coilArea/gray_bottom.png"/>');
		htmlData.push('</div>');
		
	$(".coils").append(htmlData.join(""));
}

//领取优惠卷
function receiveCoupon(couponcode, telephone){
    var params = {
        "ccode": couponcode,
        "tel": telephone
    };
    xigou.activeUser.receiveCoupon({
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
                    		window.location.reload();
                        break;
                        case "-100":
                        	alert(2)
                            var dia=$.dialog({
                                    title:'',
                                    content:response.msg||"用户失效，请重新登录。",
                                    button:["确认"]
                                });

                                dia.on("dialog:action",function(e){
                                    xigou.removelocalStorage("token");
                                    window.location.href="logon.html";
                                });
                            break;
                        default:
                        	alert(3)
                            $.tips({
                                content:response.msg||"获取优惠券信息失败",
                                stayTime:2000,
                                type:"warn"
                            });
                            break;
                    }
                }
            } else {
                $.tips({
                    content:'请求失败，详见' + response,
                    stayTime:2000,
                    type:"warn"
                });
            }
        }
    });
};