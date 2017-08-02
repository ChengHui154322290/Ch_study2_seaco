/**
 * 
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-12 17:16:47
 * @version Ver 1.0.0
 */
$(function() {
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				setbackpage();
				haved(1);
			}
		}	
	});
});
var addressData;//数据
var tuntype = xigou.getQueryString('tuntype');

//用户-优惠券-已有
function haved(page){

	var uuid = xigou.getSessionStorage("buy_now_uuid") || "";
	var params = {
		token: xigou.getToken(),
		type:"3",
		curpage:"1",
		uuid: uuid,
	};

	xigou.activeUser.haved({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				if (null == response) {
					xigou.alert(xigou.dictionary.chineseTips.server.value_is_null);
				} else {
					switch (response.code) {
						case "0":
                            addressData=response.data.list;
							if(addressData && addressData.length>0){
								$('.null_go').show();
								$('.coupon-data').show();
								setHtmlView(response);
								selectCoupon(response.data);
							}else{
								$('div.coupon-data').remove();
								$('div.null_go').remove();
								$('#gotop').remove();
								$('.warm_tip').hide();
								$('<div class="empty_box">'+
									    '<div class="empty_box_des">'+
								    	'<h2 class="empty_box_des_img coupon"></h2>'+
								    	'<p>此订单暂无可用的优惠券</p>'+
								    '</div>'+
								'</div>').insertAfter('header');
							}
							break;
						case "-100":
							xigou.alert({
								message:response.rescode.info||"用户失效，请重新登录。",
								callback:function(){
									xigou.removelocalStorage("token");
									window.location.href="logon.html"
								}
							});
							break;
						default:
							xigou.alert(response.rescode.info||"获取优惠券数据失败");
							break;
					}
				}
			} else {
				xigou.alert('请求失败，详见' + response);
			}
		}
	});
};


function setHtmlView(data) {
	//“0”可用 “1”已使用 “2”已过期
	var couponList = {
		coupon0:[]
	};

	if(data && data.data.list){
		for(var d in data.data.list){
			var c=data.data.list[d];
			couponList["coupon0"].push(c);
		}
	}

	// var btn_img = __uri("../../images/coupon/btn.png"),
	//     btn_gray_img = __uri("../../images/coupon/btn_gray.png");

	var btn_img = "img/coupon/dot.png",
		btn_gray_img = "img/coupon/dot.png";

	var empty_div = '<div class="empty_box">没有可使用的优惠券</div>';
	var unusedList = [];

	//未使用
	for (var i=0,len;i<couponList.coupon0.length; i++) {
		var obj = couponList.coupon0[i];

		unusedList.push('<div class="ui-flex ui-flex-pack-center ui-flex-align-start">');
		unusedList.push('<div class="coupon_selectBtn"><label class="ui-checkbox-s"><input type="checkbox" name="checkbox" onclick="checkSelect(this)" ></label></div>');
		unusedList.push('   <div class="coupon-item" cid="' + obj.cid + '">');
		unusedList.push('<div class="coupon_i coupon_type'+ obj.type +'" cpid = "'+ obj.type +'"></div>');
		unusedList.push('       <div class="coupon-unused-info">');
		unusedList.push('           <div class="item_price"><span>¥</span>' + obj.price + '</div>');
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
	}
	if(unusedList.length == 0){
		$(".coupon-unused").html(empty_div);
	}else {
		$(".coupon-unused").html(unusedList.join(""));
	}

	if(unusedList.length == 0){
		$(".coupon-unused").html(empty_div);
	}else {
		$(".coupon-unused").html(unusedList.join(""));
	}

	//setSetRecvaddress(couponList.coupon0);
	$(".application_scope")[xigou.events.click](function() {
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
}

//现金券和优惠券的选择校验、
function checkSelect(e){
	var checked = e.checked;
	var checkBox = document.getElementById('coupon-unused').getElementsByTagName('input');
	var cpType = $(e).parents(".coupon_selectBtn").siblings(".coupon-item").find(".coupon_i").attr("cpid");
	var mj_select = document.getElementsByClassName("coupon_type0");
	if(cpType == "0" ){
		for (var i = 0; i < checkBox.length; i++) {
			if(e !=checkBox[i]){
				checkBox[i].checked = false;
			}
		}
	}else if(cpType == "1"){
		for (var i = 0; i < mj_select.length; i++) {
			mj_select[i].parentNode.parentNode.getElementsByTagName('input')[0].checked = false;
		}
	}

}

// 选优惠券
function selectCoupon(data){

		var select_coupon_check = [];
		$(".confirm_coupon").on("click",function(event) {

			var cid_arr = [];
			var coupon_check = $('.coupon-unused input:checked');
			var coupon_check_pd = coupon_check.parents(".coupon_selectBtn").siblings(".coupon-item");
			for (var i = 0; i < coupon_check_pd.length; i++) {
				var current_cid = $(coupon_check_pd[i]).attr("cid");
				if (current_cid){
					for (var j = 0; j < data.list.length; j++) {
						if (data.list[j].cid == current_cid){
							cid_arr.push(data.list[j]);
						}
					};
				}
			};
			xigou.setSessionStorage("clearing_select_coupon", cid_arr, true);

			var backurl = document.referrer;
			if (backurl) {
				window.location.href = backurl;
			}
			else {
				window.location.href = 'settle.html';
			}


			//if (cid ) {
			//	for (var i = 0; i < data.list.length; i++) {
			//		if (data.list[i].cid == cid){
			//			xigou.setSessionStorage("clearing_select_coupon", data.list[i], true);
			//			window.location.href='settle.html';
			//		}
			//	};
			//}
		});

}



function setHtml(data){
	var couponList={
			coupon0:[],
			coupon1:[],
			coupon2:[]
		};

	//“0”可用 “1”已使用 “2”已过期
    var dfNum=xigou.getSessionStorage("clearing_select_coupon",true);

	if(data&&data.data.list){
		for(var d in data.data.list){
			var c=data.data.list[d];
                couponList["coupon0"].push(c);

		}
	}

	var templateNormal='<div class="list" num="#cid#">'
            +'<div class="detail">'
                +'<div class="l">'
                    +'<span>'
                        +'<label class="one_radio-button radio">'
                             +'<input type="radio" name="lnvoice" #checked#>'
                             +'<div class="one_radio-button__checkmark"></div>'
                        +'</label>'
                        +' <b>#name#</b>'
                    +'</span>'
                    +'<i>#rule#</i>'
                +'</div>'
                +'<div class="">'
                    +'<span>#validtime#</span>'
                +'</div><br class="clear">'
            +'</div>'
        +'</div>',
        expiredList=[],
        normalList=[];
       
        var checked="";
        for (var i = 0; i < couponList.coupon0.length; i++) {
            checked="";
            if(dfNum){
                checked=dfNum.num==couponList.coupon0[i].num?'checked="true"':"";
            }
        	normalList.push(
        		templateNormal.replace("#name#",couponList.coupon0[i].name)
        				.replace("#rule#",couponList.coupon0[i].rule)
        				.replace("#cid#",couponList.coupon0[i].cid)
                        .replace("#validtime#",couponList.coupon0[i].validtime)
    		);
        }
        $("#normal").html(normalList.join(""));

        $("#normal .list")[xigou.events.click](function () {
            var _input=$(this).find("input[name='lnvoice']");
            var checked=_input.prop("checked");
            _input.prop("checked",!checked);
        });
        
        $(".cfmBtn")[xigou.events.click](function () {
        	xigou.removeSessionStorage("clearing_select_coupon");
            var _input=$('#normal').find("input[name='lnvoice']");
            for(var i = 0;i< _input.length;i++){
	            var checked=$(_input[i]).prop("checked");
	            if(checked){
	            	var num=$(_input[i]).parents(".list").attr("num");
	                xigou.setSessionStorage("clearing_select_coupon",getAddData(num),true);
	                break;
	            }
            }
            
            var targetUrl = xigou.getQueryString("backpage");
            if(!targetUrl){
            	window.location.href="clearing.html?holdTime=1#redenvelopes";
            }else{
            	window.location.href=targetUrl+"?holdTime=1#redenvelopes";
            }
            return;
        });
        
        $("[name='lnvoice']")['change'](function () {
            this.checked=!this.checked;
            return false
        });
}

function getAddData(num){
    var result=null;
    for(var d in addressData){
        if(addressData[d].num==num){
            result=addressData[d];
            break;
        }
    }
    return result;
}

var backpage;
var setbackpage = function(){
	backpage = xigou.getQueryString("backpage");
	if(backpage){
		$('a.back').attr('href',backpage+"?holdTime=1");
	}
};