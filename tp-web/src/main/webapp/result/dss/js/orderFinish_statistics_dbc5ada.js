"use strict";function finishOrders(e,s){if("Y"==finishorder_max)return s.disable("down",!1),$(".ui-refresh-down>span:first-child").removeClass(),!1;if("Y"!=finishorder_max){var i=xigou.getToken(),r=xigou.getSessionStorage("beginquerydate"),a=xigou.getSessionStorage("endquerydate"),d={token:i,orderstatus:6,curpage:e,type:type,createBeginTime:r,createEndTime:a};xigou.promoterFunc.queryOrders({requestBody:d,callback:function(i,r){if(r==xigou.dictionary.success)if(null==i)$.tips({content:xigou.dictionary.chineseTips.server.value_is_null,stayTime:2e3,type:"warn"});else{var a=i.code;0==a?(setFinishOrderHtmlView(i,e),i.data.curpage>=i.data.totalpagecount&&(s.lock(),$(".dropload-down").hide())):(s.lock(),$(".dropload-down").hide())}}})}}function setFinishOrderHtmlView(e,s){for(var i=e,r=i.data.list.length,a=(i.allpage,i.count,i.data.list),d=[],t=0;r>t;t++){d.push('<div class="orders-item" ordercode="'+a[t].ordercode+'">'),d.push(' <div class="order_time_status">'+a[t].ordertime),a[t].statusdesc||(a[t].statusdesc=" "),d.push('     <span class="order_status">'+a[t].statusdesc+"</span></div>");for(var o=0;o<a[t].lines.length;o++){var n=a[t].lines[o];d.push('<div class="order_info" sku="'+n.sku+'" tid = '+n.tid+">"),d.push(' <div class="order_image">'),d.push('     <img src="'+n.imgurl+'">'),d.push(" </div>"),d.push(' <div class="oder_info2">'),d.push('     <div class="order_name_price">'),d.push('         <div class="order_price">¥'+n.price+"</div>"),d.push('         <div class="order_name">'+n.name+"</div>"),d.push("     </div>"),d.push('     <div class="order_size_count">'),d.push('         <div class="order_count">×'+n.count+"</div>"),n.specs.length>0&&d.push("         <div>"+n.specs[0]+"</div>"),d.push("     </div>"),d.push(" </div>"),d.push("</div>")}d.push(' <div class="order_totle_count_price">'),d.push('     <span class="float_right ">共 '+a[t].linecount+' 件 实付：<span class="order_lineprice">¥'+a[t].orderprice+"</span></span>"),d.push(" </div>"),d.push("</div>")}if((""==e.data.pagesize||0==e.data.pagesize||"0"==e.data.pagesize||e.data.curpage==e.data.totalcount)&&(finishorder_max="Y"),1==s&&0==d.length){$(".orders-finish").empty();var l=[];l.push('<div class="order_empty"><div class="empty-tip">还木有订单哟~</div></div>'),$(".orders-finish").append(l.join(""))}else $(".orders-finish").append(d.join(""))}function setdefaulesel(e){var s=$(".ui-tab-content")[0],i=$(".ui-tab-nav")[0];$(s.children[0]).removeClass("current"),$(i.children[0]).removeClass("current"),$(s.children[e]).addClass("current"),$(i.children[e]).addClass("current");var r=-1*s.clientWidth*e/3;s.style.transform="translate("+r+"px, 0px)",$(s.children)[0].height="0px"}var type=xigou.getQueryString("type");null==type&&(window.location.href="../logon.html"),$(function(){var e=1;xigou.getLoginUserInfo({callback:function(s,i){if(i==xigou.dictionary.success){$(".orders-finish").dropload({scrollArea:window,loadDownFn:function(s){finishOrders(e,s),s.resetload(),1==e&&$(".dropload-refresh").hide(),e++}});var r=xigou.getQueryString("defaule");r&&setdefaulesel(r)}else window.location.href="../logon.html"}})});var finishorder_max="N";