function initPageData(e){$("#couponnum").text("-¥0"),$("#pointType").val(e.data.pointType);var i=xigou.getSessionStorage("clearing_select_address",!0);if(i){i.identityCard||(i.identityCard=""),$("#auth-num-input").val(i.identityCard),$("#submit_auth").addClass("onClick");var t=[];t.push('<div class="div_name_tel">'),t.push('	<span class="div_name">'+i.name+"</span>"),0==i.iscertificated&&(t.push('	<div class="div_not_certified"></div>'),needcertified=!0),t.push('	<div class="div_tel">'+i.tel+"</div>"),t.push("</div>"),t.push('<div class="div_add">收货地址:'+i.fullinfo+"</div>"),$(".settle-address-info").html(t.join("")),addressnum=i.aid,$("#zipcode").text(i.zipcode),$("#li_recvaddress_info").attr("aid",i.aid)}else defaultAddress();if(coupon=xigou.getSessionStorage("clearing_select_coupon",!0),coupon.length>0)for(var o=0;o<coupon.length;o++)cid_list.push(coupon[o].cid);if(redenvelopes=xigou.getSessionStorage("clearing_select_redenvelopes",!0),redenvelopes&&($("#redenvelopes").text(redenvelopes.title),$("#redenvelopnum").text("-¥"+redenvelopes.price)),e){var s='<li class="ui-border-t clearfix"><div class="ui-list-img floor_product_img"><img class="settle-product-icon" src="#img#" specialid="#specialid#" productid="#productid#"></div><div class="floor_product_title item_info"><div  class="cart_product_name"><div class="item_price">#price#</div><div class="item_name">#name#</div></div><div class="item_count">#count#</div><div class="item_count_price">#countPrice#</div></div></li>',t=[];if(e.data.productinfo){for(var a=[],n=e.data.productinfo,r=0,l=n.length;l>r;r++){var c=n[r];t.push('<div class="floor_product"><div class="floor_product_tap"><span>'+c.channel+"</span></div>"),t.push('<ul class="ui-list ui-list-one ui-border-tb">');for(var d=0;d<c.products.length;d++){{var u=c.products[d];parseFloat(u.price)*parseFloat(u.count)}a.push(u),t.push(s.replace("#img#",u.imgurl).replace("#specialid#",u.tid).replace("#productid#",u.sku).replace("#name#",u.name).replace("#quantity#",u.count).replace("#price#","¥"+u.price).replace("#count#","×"+u.count).replace("#countPrice#","¥"+u.lineprice))}t.push("</ul>");var p=xigou.getSessionStorage("itemType");"2"!==p&&(t.push('<div class="floor_freight"><ul class="ui-list ui-list-one">'),t.push('<li id="price_li"><div class="ui-list-info"><h4 class="floor_left" style="font-size: 12px;color: #666666;">运费:</h4><span class="floor_right" style="font-size: 12px;color: #666666;">¥'+c.freight+"</span></div></li>"),t.push(parseInt(c.taxes)>50?"1"==c.isfreetax?"HWZY"==c.channelcode?'<li id="price_li"><div class="ui-list-info"><h4 class="floor_left" style="font-size: 12px;color: #666666;">税金:</h4><del class="floor_right" style="font-size: 12px;color: #666666;">¥'+c.taxes+"</del></div></li></ul>":'<li id="price_li"><div class="ui-list-info"><h4 class="floor_left" style="font-size: 12px;color: #666666;">税金:</h4><del class="floor_right" style="font-size: 12px;color: #666666;">¥'+c.taxes+"</del></div></li></ul>":"HWZY"==c.channelcode?'<li id="price_li"><div class="ui-list-info"><h4 class="floor_left" style="font-size: 12px;color: #666666;">税金:</h4><span class="floor_right" style="font-size: 12px;color: #666666;">¥'+c.taxes+"</span></div></li></ul>":'<li id="price_li"><div class="ui-list-info"><h4 class="floor_left" style="font-size: 12px;color: #666666;">税金:</h4><span class="floor_right" style="font-size: 12px;color: #666666;">¥'+c.taxes+"</span></div></li></ul>":"1"==c.isfreetax?'<li id="price_li"><div class="ui-list-info"><h4 class="floor_left" style="font-size: 12px;color: #666666;">税金:</h4><del class="floor_right" style="font-size: 12px;color: #666666;">¥'+c.taxes+"</del></div></li></ul>":'<li id="price_li"><div class="ui-list-info"><h4 class="floor_left" style="font-size: 12px;color: #666666;">税金:</h4><span class="floor_right" style="font-size: 12px;color: #666666;">¥'+c.taxes+"</span></div></li></ul>"),("GNZF"==c.channelcode||"HWZY"==c.channelcode)&&(is_tax_ok=!0),"HWZY"==c.channelcode&&t.push('<div class="rules">本单为海外直邮商品，税费由商家承担</div>'),t.push("</div></div>"))}xigou.setSessionStorage("clearing_products_data",a,!0)}$(".settle-list").html(t.join("")),$("#agio_nmb").text(e.data.totalpoint);var g=parseFloat(e.data.usedpoint).toFixed(2);$("#agio_price").text(g);var h=e.data.price;"true"==e.data.usedpointsign&&($(".floor_right_switch[usedpointsign]").attr("usedpointsign","true").html('<img alt="switch" src="img/settle/put.png">'),h=parseFloat(parseFloat(h)+parseFloat(e.data.usedpoint)).toFixed(2)),xigou.setSessionStorage("ordertotalprice",e.data.price),xigou.setSessionStorage("usedpointamount",g),xigou.setSessionStorage("orderTotalAmount",h),$("#product_count").before(t.join("")),$("#productPrice").html(createPriceHTML(e.data.itemsprice)),$("#freight").html(createPriceHTML(e.data.freight)),$("#fmadisprice").html("-"+createPriceHTML(e.data.disprice)),$("#taxes").html(createPriceHTML(e.data.taxes)),$("#totalPayments").html(createPriceHTML(e.data.price)),$("#totalPayments2").html(createPriceHTML(e.data.price)),e.data.totalcoupon&&$("#couponnum").text("-¥"+e.data.totalcoupon),parseFloat(e.data.totalcoupon)?$("#coupon").text("-¥"+e.data.totalcoupon):showCouponNo(),$(".floor_product_img img")[xigou.events.click](function(){var e=$(this);window.location.href="item.htm?tid="+e.attr("specialid")+"&sku="+e.attr("productid")}),authentication()}}function select_pay(e,i){$(".submit-btn").click(function(){var t=checkOrder();if(t)if(1==checkidphoto){var o={text:'<p style="line-height: 22px;">应海关审核要求，请确认已上传真实的身份证正反面照片</p>',callback:function(i){"ok"==i?window.location.href="realname.html?backpage=haitao_clearing.html":order(e)},params:{closeBtn:!0,buttons:{"确认":function(){this.close(),this.destroy(),$.isFunction(o.callback)&&o.callback("cancel")},"上传":function(){this.close(),this.destroy(),$.isFunction(o.callback)&&o.callback("ok")}}}};xigou.confirm(o)}else order(e,i)})}function order(e,i){var t=$(".ui-border-t").attr("aid"),o=($("#li_coupon_info").attr("cid"),{}),s=null,a=xigou.getSessionStorage("dssUser"),n=xigou.getSessionStorage("usedpointsign");a&&(a=JSON.parse(a),a.mobile?s=a.mobile:a.shopmobile&&(s=a.shopmobile)),o=isWeiXin()?{token:UserToken,aid:t,receiveTel:$(".receiveTel").val(),cidlist:cid_list,uuid:e,gid:i,shopMobile:s,usedpointsign:n,pointType:$("#pointType").val()}:{token:UserToken,aid:t,receiveTel:$(".receiveTel").val(),cidlist:cid_list,uuid:e,payway:"mergeAlipay",shopMobile:s,usedpointsign:n,pointType:$("#pointType").val()};var i=xigou.getQueryString("gid");i&&(o.gid=i),o.pointType=$("#pointType").val(),gotoPay(o)}function initOrderChannelTrack(e){var i=xigou.getLocalStorage("channelsource"),t=xigou.getLocalStorage("clientcode"),o=xigou.getLocalStorage("distributecode"),s=xigou.channelcode,a=xigou.getLocalStorage("yiqifarddate"),n=new Date;n.getTime()-parseInt(a)<2592e6&&null!=s&&""!=$.trim(s)&&(e.channelcode=s,e.channelsource=i,e.clientcode=t,e.distributecode=o)}function showCouponNo(){var e=xigou.getSessionStorage("buy_now_uuid")||"",i={token:xigou.getToken(),type:"3",curpage:"1",uuid:e};xigou.activeUser.queryordercouponcount({requestBody:i,callback:function(e,i){if(i==xigou.dictionary.success)if(null==e)xigou.alert(xigou.dictionary.chineseTips.server.value_is_null);else{var t=e.data?e.data:0;$("#coupon").text(t+"张")}else xigou.alert("请求失败，详见"+e)}})}function gotoPay(e){function i(e,i){if(i==xigou.dictionary.success)if(null==e)$.tips({content:xigou.dictionary.chineseTips.server.value_is_null,stayTime:2e3,type:"warn"});else switch(e.code){case"0":if(xigou.removeSessionStorage("buy_now_uuid"),xigou.setSessionStorage("haitao_clearing_orders",e,!0),isWeiXin()){var t=encodeURIComponent(xigou.pageHost),o=e.data[0].payid+","+e.data[0].ordercode+","+e.data[0].orderprice+","+xigou.getToken()+","+xigou.channelcode+","+t;window.location.href=xigou.xgHost+"paymentbridge.html?param="+o}else window.location.href="orderspay.html";break;case"-104":$.tips({content:e.msg||"用户未认证",stayTime:2e3,type:"warn"}),OnSeletPhoto();break;case"-105":$.tips({content:e.msg||"用户未上传身份证照片",stayTime:2e3,type:"warn"}),OnSeletPhoto();break;case"-1":var s=$.dialog({title:"",content:e.msg||"支付提交失败",button:["确认"]});s.on("dialog:action",function(e){console.log(e.index),window.location.href="cart.html"});default:$.tips({content:e.msg||"支付提交失败",stayTime:2e3,type:"warn"})}else $.tips({content:"请求失败，详见"+e,stayTime:2e3,type:"warn"})}isWeiXin()?xigou.activeHtClearing.submitseaorder({requestBody:e,callback:function(e,t){i(e,t)}}):xigou.activeHtClearing.mergesubmit({requestBody:e,callback:function(e,t){i(e,t)}})}function authprice(e){var i=xigou.getSessionStorage("clearing_select_coupon",!0),t=[];if(i.length>0)for(var o=0;o<i.length;o++)t.push(i[o].cid);var s=(xigou.getSessionStorage("clearing_select_redenvelopes",!0)||{num:""},xigou.getSessionStorage("usedpointsign")),a={token:UserToken,cid:i.cid,cidlist:t,uuid:e||"",shopMobile:""==xigou.getSessionStorage("dssUser")?null:xigou.getSessionStorage("dssUser",!0).mobile,usedpointsign:s,pointType:$("#pointType").val()};xigou.activeHtClearing.authprice({requestBody:a,callback:function(e,i){if(i==xigou.dictionary.success)if(null==e)$.tips({content:xigou.dictionary.chineseTips.server.value_is_null,stayTime:2e3,type:"warn"});else switch(e.code){case"0":if(e.data.price)initPageData(e);else{var t=$.dialog({title:"",content:"订单已提交,请进入全部订单查看",button:["确认"]});t.on("dialog:action",function(e){console.log(e.index),window.location.href="orders.html"})}break;case"-1001":var t=$.dialog({title:"",content:"订单支付失败",button:["确认"]});t.on("dialog:action",function(e){console.log(e.index),window.location.href="orders.html"});break;case"-100":var t=$.dialog({title:"",content:"请先登录再提交订单",button:["确认"]});t.on("dialog:action",function(e){console.log(e.index),window.location.href="logon.html"});break;default:var t=$.dialog({title:"",content:e.msg||"订单支付失败,请进入待支付订单查看",button:["确认"]});t.on("dialog:action",function(e){console.log(e.index),window.location.href="orders.html"})}else $.tips({content:"请求失败，详见"+e,stayTime:2e3,type:"warn"})}})}function defaultAddress(){var e=xigou.getQueryString("clearing_ht_select_address");if(!e){var i={token:UserToken};xigou.activeUser.lists({requestBody:i,callback:function(e,i){if(i==xigou.dictionary.success)if(null==e)$.tips({content:xigou.dictionary.chineseTips.server.value_is_null,stayTime:2e3,type:"warn"});else switch(e.code){case"0":var t=e,o=!1;if("undefined"!=typeof t&&"undefined"!=typeof t.data&&t.data.length>0){var s=!1;for(var a in t.data){o=!0;var n=t.data[a];if("1"==n.isdefault){s=!0;{n.addresscode}xigou.setSessionStorage("clearing_select_address",n,!0);var r=[];r.push('<div class="div_name_tel">'),r.push('	<span class="div_name">'+n.name+"</span>"),0==n.iscertificated&&(r.push('	<div class="div_not_certified"></div>'),div_not_certified=!0),r.push('	<div class="div_tel">'+n.tel+"</div>"),r.push("</div>"),r.push('<div class="div_add">收货地址:'+n.fullinfo+"</div>"),$(".settle-address-info").html(r.join("")),$("#li_recvaddress_info").attr("aid",n.aid),addressnum=n.aid,n.identityCard||(n.identityCard=""),$("#auth-num-input").val(n.identityCard),$("#submit_auth").addClass("onClick");break}}if(!s){{var l=t.addresslist[0];l.addresscode}xigou.setSessionStorage("clearing_select_address",l,!0);var r=[];r.push('<span>收件人：</span><span class="floor_right ">'+l.name+"</span><br>"),0==l.iscertificated&&(r.push('	<div class="div_not_certified"></div>'),needcertified=!0),r.push('<span>联系方式：</span><span class="floor_right ">'+l.tel+"</span><br>"),r.push('<span>收货地址：</span><span id="select_address">'+n.info+"</span>"),$(".settle-address-info").html(r.join("")),$("#li_recvaddress_info").attr("aid",n.aid),addressnum=l.aid}}o||($(".settle-address-info").html('<div class="div_add_address">添加收货地址</div>'),$(".settle-address-info")[xigou.events.click](function(){window.location.href="recvaddress.html?selectType=1"}));break;default:$.tips({content:e.msg||"获取默认收货地址失败",stayTime:2e3,type:"warn"})}else $.tips({content:"请求失败，详见"+e,stayTime:2e3,type:"warn"})}})}}function createPriceHTML(e){if(!e)return"¥";var i="00",t="00",o=e.split(".");return o.length>0?(i=o[0],o.length>1&&(t=o[1]),htmlPrice='<span class="product-price">¥<span class="product-price1">'+i+"</span>."+t+"</span>"):"¥"}function authentication(){$("#closeBtn")[xigou.events.click](function(){$(".div_tip_bg").hide(),$(".div_tip").hide()}),$("#submit_auth")[xigou.events.click](function(){var e=$(this);if(e.hasClass("onClick")){if(!$("#auth-num-input")[0].value)return void $.tips({content:"收货人身份证不能为空!",stayTime:2e3,type:"warn"});var i=xigou.getSessionStorage("clearing_select_address",!0),t={token:xigou.getToken(),aid:i.aid,tel:i.tel,provid:i.provinceid,provname:i.province,cityid:i.cityid,cityname:i.city,districtid:i.districtid,districtname:i.district,streetid:i.streetid,streetname:i.street,info:i.info,identityCard:$("#auth-num-input")[0].value,frontimg:$("#upload_img_itme1").attr("src"),backimg:$("#upload_img_itme2").attr("src"),isdefault:i.isdefault};t.frontimg.split("address/upload.png").length>1&&(t.frontimg=""),t.backimg.split("address/upload.png").length>1&&(t.backimg=""),xigou.activeUser.edit({requestBody:t,callback:function(e,i){if(i==xigou.dictionary.success){if(0!=e.code)return void $.tips({content:e.msg||"实名认证失败,请检测网络连接!",stayTime:2e3,type:"warn"});needcertified=!1,$(".div_tip_bg").hide(),$(".div_tip").hide(),$(".submit-btn")[0].click()}}})}})}function OnSeletPhoto(){$(".div_tip_bg").show(),$(".div_tip").show(),$("#auth-num-input").on("input propertychange",function(){$("#auth-num-input").val().length>0?$("#submit_auth").addClass("onClick"):$("#submit_auth").removeClass("onClick")})}function bindUploadImg(){$(".imgItem")[xigou.events.click](function(){{var e=$(this).find("input");$(this).find("img")}e[0].click()}),$(".input_photo").unbind("change"),$(".input_photo").on("change",function(){for(var e=event.target.files,i=0;i<e.length;i++){var t=e[i];if(!t.type.match("image.*"))return void $.tips({content:"请选择图片文件",stayTime:2e3,type:"warn"});var o=new FileReader,s=$(this).parent(".imgItem").find("img");o.onload=function(){var e=this.result,i=15,t=function(i){e=i.src,uploadImageUrl(e,s)};e=xigou.compress2(e,i,t)},o.readAsDataURL(t)}})}function uploadImageUrl(e,i){var t={imgstream:e.toString().split(",")[1],token:xigou.getToken()};xigou.activeUser.uploadimage({requestBody:t,callback:function(e,t){if(t==xigou.dictionary.success){if(!e||!e.data||!e.data.path||0!=e.code)return void $.tips({content:e.msg||"上传图片失败,请检查网络连接",stayTime:2e3,type:"warn"});i.attr("src",e.data.path)}else $.tips({content:e.msg||"上传图片失败,请检查网络连接",stayTime:2e3,type:"warn"})}})}var UserToken=xigou.getToken(),fmaidcsv="";$(function(){xigou.getLoginUserInfo({callback:function(e,i){if(i==xigou.dictionary.success){var t=xigou.getSessionStorage("buy_now_uuid");if(!t&&"cart"!=xigou.getQueryString("from"))return void(window.location.href="orders.html");var o=xigou.getSessionStorage("gid");""!=t&&$(".back").attr("href",xigou.getSessionStorage("buy_now_details_url")),authprice(t),select_pay(t,o),$("#li_recvaddress_info")[xigou.events.click](function(){window.location.href="recvaddress.html?selectType=1"});var s=xigou.getSessionStorage("buy_now_details_url");s.split("tuan.html").length>1?$("#li_coupon_info").hide():$("#coupon")[xigou.events.click](function(){window.location.href="couponorder.html"}),$("#select_auth")[xigou.events.click](function(){window.location.href="auth.html"}),$(".idNumber")[xigou.events.click](function(){window.location.href="realname.html?backpage=haitao_clearing.html"});var a=xigou.getSessionStorage("itemType");"2"==a?($(".ui-border-address").hide(),$(".ui-border-tel").show(),$("#telephone").val(xigou.getLocalStorage("login_name")),$(".total-freight").hide()):($(".ui-border-tel").hide(),$(".ui-border-address").show(),$(".total-freight").show())}else window.location.href="index.html";xigou.removeSessionStorage("gid")}}),bindUploadImg(),$(".floor_right_switch")[xigou.events.click](function(){if("false"==$(this).attr("usedpointsign")&&parseFloat($(".usedpoint[usedpoint]").text()).toFixed(2)>0){$(this).attr("usedpointsign","true"),xigou.setSessionStorage("usedpointsign",!0),$(this).html('<img alt="switch" src="img/settle/put.png">');var e=xigou.getSessionStorage("orderTotalAmount"),i=xigou.getSessionStorage("usedpointamount"),t=parseFloat(parseFloat(e)-parseFloat(i)).toFixed(2);$("#totalPayments").html(createPriceHTML(t)),$("#totalPayments2").html(createPriceHTML(t))}else{$(this).attr("usedpointsign","false"),xigou.setSessionStorage("usedpointsign",!1),$(this).html('<img alt="switch" src="img/settle/close.png">');var e=xigou.getSessionStorage("orderTotalAmount"),i=xigou.getSessionStorage("usedpointamount"),t=e;$("#totalPayments").html(createPriceHTML(t)),$("#totalPayments2").html(createPriceHTML(t))}})});var addressnum,is_tax_ok=!0,is_price_ok=!0,coupon,redenvelopes,cid_list=[],needcertified=!1;String.prototype.trim=function(){return this.replace(/(^\s*)|(\s*$)/g,"")};var checkOrder=function(){return addressnum?!0:($(".settle-address-info").html("<span>添加收货地址</span>"),$.dialog({title:"",content:"请先添加收货地址!",button:["确认"]}),!1)},checkidphoto=0;