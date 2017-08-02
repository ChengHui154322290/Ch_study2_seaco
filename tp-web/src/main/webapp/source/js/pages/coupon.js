/**
 * 
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-12 17:16:47
 * @version Ver 1.0.0
 */

$(function() {
	if(isWeiXin()){
		$("header").hide();
		$("title").html("我的优惠券");
	}
    var page = 1;
    xigou.getLoginUserInfo({
        callback:function(userinfo,status){
            if(status==xigou.dictionary.success){
                $('.coupon-unused').dropload({
                    scrollArea : window,
                    loadDownFn : function(me){
                        unused(page,me);
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
   /* var str =$.trim($('.current .coupon-unused').html());
    var str =$.trim($('.current .coupon-unused').html());
    if(str == "" || str == undefined){
    	$('.current .nocard').show();
    }else{
    	$('.current .nocard').hide();
    }*/
});

function lookrule() {
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

//焦点聚焦、移除事件
$("#search_code").blur(function(){
	$("#clear_input").hide();
});
$("#search_code").focus(function(){
	$("#clear_input").show()
});


function exCoupon(cid){
    var params = {
        "token": xigou.getToken(),
        "ccode":$("input.input-exchange").val(),
        "cid":cid,
        "point":"point"
    };

    xigou.activeUser.exchangCoupon({
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
                    		if(response.data.type=='EXCHANGE_SUCCESS'){
                    			var dia=$.dialog({
	                                title:'',
	                                content:response.msg||"兑换成功",
	                                button:["确认"]
	                            });
	
	                            dia.on("dialog:action",function(e){
	                                window.location.reload();
	                            });
                    		}else if(response.data.type=='EXCHANGE_POINT_SUCCESS'){
                    			var dia=$.dialog({
                                    title:'',
                                    content:"你的优惠券已经兑换成西客币，<br>请到“我的西客币”查看",
                                    button:["去查看","知道了"]
	                                });
	                            dia.on("dialog:action",function(eDlg){
	                                if(1 == eDlg.index){
	                                	window.location.reload();
	                                }else{
	                                	//链接
	                                	window.location.href="xgcoin.html";
	                                }
	                            });
                    		}
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

//用户-优惠券-未使用
function unused(page, me){
	var params = {
		token: xigou.getToken(),
		type:"1",
        status:"0",
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
							setHtmlView(response);						                           
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

function setHtmlView(data) {
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

    var empty_div = '<div class="empty_box nocard"><img src="./img/coupon/nocart.png"/>没有可使用的优惠券</div>';
    
    
  //未使用
	for (var i=0,len;i<couponList.coupon0.length; i++) {
		var unusedList = [];
		var obj = couponList.coupon0[i];
		if(obj.type =="0"){
			unusedList.push('<div class="ui-flex ui-flex-ver ui-flex-pack-center ui-flex-align-start">');
			unusedList.push('   <div class="coupon-item" >');
			unusedList.push('       <div class="coupon-unused-info">');
			unusedList.push('           <div class="item_price"><span>¥</span>' + obj.price + '</div>');
			unusedList.push('           <a class="ImmediatelyUse" href="search.html?couponid=' + obj.cid + '">立即使用</a>');
			unusedList.push('           <div class="coupon-introduce">');
			unusedList.push('               <div class="coupon-name">' + obj.name + '</div>');
			unusedList.push('               <ul>');
			if (obj.validtime && obj.validtime != '') {
			    unusedList.push('                   <li>' + obj.validtime + '</li>');
			}
			if (obj.rule && obj.rule != '') {
			    unusedList.push('                   <li>' + obj.rule + '</li>');
			}
			unusedList.push('               </ul>');
			unusedList.push('               <div class="application_scope">适用范围');
			unusedList.push('                   <div class="show_application_scope"></div>');
			unusedList.push('               </div>');
			unusedList.push('           </div>');
			unusedList.push('           <div class="item_bottom_line" id="info_bottom_line"></div>');
			unusedList.push('       </div>');
			unusedList.push('       <div class="application_scope_info" style="display: none">');
			unusedList.push('           <div class="application_scope_info_bg">')
			unusedList.push('               <ul>');
			if (obj.scope) {
			    for (var j = 0; j < obj.scope.length; j++) {
			        unusedList.push('                   <li>' + obj.scope[j] + '</li>');
			    }
			}
			unusedList.push('               </ul>');
			unusedList.push('           </div>');
			unusedList.push('           <div class="item_bottom_line"></div>');
			unusedList.push('       </div>');
			unusedList.push('   </div>');
			unusedList.push('</div>');
			if(unusedList.length == 0 || unusedList == undefined){
				$(".coupon-unused").html(empty_div);
			}else {
				$(".coupon-unused").append(unusedList.join(""));
			}
			
		}else{
			if(obj.exchangeXgMoney=="1"){
				unusedList.push('<div class="ui-flex ui-flex-ver ui-flex-pack-center ui-flex-align-start">');
				   unusedList.push('   <div class="card-item" >');
				   unusedList.push('       <div class="card-unused-info">');
				   unusedList.push('           <div class="item_price"><span>¥</span>' + obj.price + '</div>');
				   unusedList.push('           <a class="change" onclick="exCoupon('+obj.cid+')">立即兑换</a>');
				   unusedList.push('           <div class="card-introduce">');
				   unusedList.push('               <div class="card-name">' + obj.name + '</div>');
				   unusedList.push('               <ul>');
				   if (obj.validtime && obj.validtime != '') {
				       unusedList.push('                   <li>' + obj.validtime + '</li>');
				   }
				   unusedList.push('                   <li>可直接兑换成西客币</li>');
				   unusedList.push('               </ul>');
				   unusedList.push('               <div class="application_scope application_scope_card">使用条件');
				   unusedList.push('                   <div class="show_application_scope"></div>');
				   unusedList.push('               </div>');
				   unusedList.push('           </div>');
				   unusedList.push('           <div class="item_bottom_line" id="info_bottom_line"></div>');
				   unusedList.push('       </div>');
				   unusedList.push('       <div class="application_scope_info" style="display: none">');
				   unusedList.push('           <div class="application_scope_info_bg">')
				   unusedList.push('               <ul>');
				   unusedList.push('                   <li>1、100西客币抵扣1人民币，每单可用西客币进行抵扣，但是海外订单支付金额不能全由西客币抵扣；</li>');
				   unusedList.push('                   <li>2、消费时可使用的西客币的数量必须是自然数。</li>');
				   unusedList.push('               </ul>');
				   unusedList.push('           </div>');
				   unusedList.push('           <div class="item_bottom_line"></div>');
				   unusedList.push('       </div>');
				   unusedList.push('   </div>');
				   unusedList.push('</div>');
			}else{
				unusedList.push('<div class="ui-flex ui-flex-ver ui-flex-pack-center ui-flex-align-start">');
			    unusedList.push('   <div class="coupon-item" >');
			    unusedList.push('       <div class="coupon-unused-info">');
			    unusedList.push('           <div class="item_price"><span>¥</span>' + obj.price + '</div>');
			    unusedList.push('           <a class="ImmediatelyUse" href="search.html?couponid=' + obj.cid + '">立即使用</a>');
			    unusedList.push('           <div class="coupon-introduce">');
			    unusedList.push('               <div class="coupon-name">' + obj.name + '</div>');
			    unusedList.push('               <ul>');
			    if (obj.validtime && obj.validtime != '') {
			        unusedList.push('                   <li>' + obj.validtime + '</li>');
			    }
			    unusedList.push('               </ul>');
			    unusedList.push('               <div class="application_scope application_scope_card">使用条件');
			    unusedList.push('                   <div class="show_application_scope"></div>');
			    unusedList.push('               </div>');
			    unusedList.push('           </div>');
			    unusedList.push('           <div class="item_bottom_line item_bottom_line1" id="info_bottom_line"></div>');
			    unusedList.push('       </div>');
			    unusedList.push('       <div class="application_scope_info" style="display: none">');
			    unusedList.push('           <div class="application_scope_info_bg">')
			    unusedList.push('               <ul>');
			    if (obj.scope) {
				    for (var j = 0; j < obj.scope.length; j++) {
				        unusedList.push('                   <li>' + obj.scope[j] + '</li>');
				    }
				}
			    unusedList.push('               </ul>');
			    unusedList.push('           </div>');
			    unusedList.push('           <div class="item_bottom_line item_bottom_line1"></div>');
			    unusedList.push('       </div>');
			    unusedList.push('   </div>');
			    unusedList.push('</div>');
			}
			if(unusedList.length == 0){
				$(".card-unused").html(empty_div);
			}else {
				$(".card-unused").append(unusedList.join(""));
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
	$(".application_scope").unbind("click");
	$(".application_scope").bind("click",function() {
		var item =  $(this).parents('.coupon-item');
		var bottomItem = item.find('#info_bottom_line');
		var scopeinfo = item.find('.application_scope_info');
		
		var show = $(this).attr("show");
		if (1 == show) {
		    $(this).attr("show", 0);
		    bottomItem.show();
		    scopeinfo.hide();
		}
		else {
		    $(this).attr("show", 1);
		    bottomItem.hide();
		    scopeinfo.show();			
		}
	});		
	$(".application_scope_card").unbind("click");
	$(".application_scope_card").bind("click",function() {
		var item =  $(this).parents('.card-item');
		var item1 =  $(this).parents('.coupon-item');
		var bottomItem = item.find('#info_bottom_line');
		var scopeinfo = item.find('.application_scope_info');
		var bottomItem1 = item1.find('#info_bottom_line');
		var scopeinfo1 = item1.find('.application_scope_info');
		/* var show = $(this).attr("show");
		if (1 == show) {
		    $(this).attr("show", 0);
		    bottomItem.show();
		    scopeinfo.hide();
		}
		else {
		    $(this).attr("hide", 1);
		    bottomItem.hide();
		    scopeinfo.show();
		}*/
		if(!$('#info_bottom_line').hasClass("show")){
			bottomItem.show();
		    scopeinfo.hide();
		    bottomItem1.show();
		    scopeinfo1.hide();
		    $('#info_bottom_line').addClass("show");
		}else{
			bottomItem.hide();
		    scopeinfo.show();
		    bottomItem1.hide();
		    scopeinfo1.show();
		    $('#info_bottom_line').removeClass("show");
		}
	});
		   
    exchangecode();
}


//使用code换优惠券
function exchangecode(){
    $("#search_btn").on(xigou.events.click,bindClick);
    //根据内容改变查询按钮状态
    $("#search_code").on("input propertychange",function(){
        $("#search_btn").unbind();
       if($("#search_code").val().length > 0){
           $("#search_btn").on(xigou.events.click,bindClick);
           $(".btn-exchange").addClass("onSearch");
       }else{
           $(".btn-exchange").removeClass("onSearch");
       }
    })
    //点击清空按钮清空内容移除绑定时间
    $("#clear_input")[xigou.events.click](function(){
        $("#search_code").val("");
        $(".btn-exchange").removeClass("onSearch");
        $("#search_btn").unbind();
    });
}
//绑定提交事件
function bindClick(){
        var code = $('.input-exchange').val();
        if(typeof(code) == undefined || code == "")
        {
            $.tips({
                content:"兑换码不能为空哦~",
                stayTime:2000,
                type:"warn"
            });
            return false;
        }
        exCoupon();
}

// // 设定是否为提交订单页面优惠券
// function setSetRecvaddress(data){
//     var selectType = xigou.getQueryString("selectType");
//     if (1 == selectType) {
//         $(".coupon-item")[xigou.events.click](function(event) {
//             var nMaxLoopCount = 8;
//             var orderItem = $(event.target).parents('.coupon-item');
//             var cid = orderItem.attr("cid");

//             if (cid != -1) {
//                 for (var i = 0; i < data.length; i++) {
//                         if (data[i].cid == cid){
//                             xigou.setSessionStorage("clearing_select_coupon", data[i], true);
//                             history.go(-1);
//                         }
//                     };  
//             }
//         });
//     }
// }