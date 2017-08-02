/*
* @Author: dongqian
* @Date:   2017-05-09 10:42:31
* @Last Modified by:   dongqian
* @Last Modified time: 2017-05-11 11:45:52
*/

'use strict';
$(function() {
    var page = 1;
    xigou.getLoginUserInfo({
        callback:function(userinfo,status){
            if(status==xigou.dictionary.success){
                $('.member-box').dropload({
                    scrollArea : window,
                    loadDownFn : function(me){
                        queryChildPromoters(page,me);
                        me.resetload();
                        if(1 == page){
                            $(".dropload-refresh").hide();
                        }
                        page++;
                    }
                });
            }else{
                window.location.href="../logon.html";
            }
        }
    });

});

function queryChildPromoters(page,me){
    var token = xigou.getToken();
    var createBeginTime = xigou.getSessionStorage('beginquerydate');
    var createEndTime = xigou.getSessionStorage('endquerydate');
    var params = {
            'token': token,
            'type': '2',
            'createBeginTime':createBeginTime,
            'createEndTime':createEndTime,
            'curpage':page,
            'pagesize':100//每次取回的数据条数
        };
    xigou.promoterFunc.queryChildPromoters({
        requestBody: params,
        callback: function(response, status) { //回调函数
            if (status == xigou.dictionary.success) {
                var json = response || null;
                if (null == json || json.length == 0)return false;
                if (json.code == 0) {
                    setChildPromotersHtmlView(response, page);
                    if(response.data.curpage >= response.data.totalpagecount){
                        me.lock();
                        $(".dropload-down").hide();
                    }
                }else{
                    me.lock();
                    $(".dropload-down").hide();
                    $.tips({
                        content:"查询失败，请稍后重试！",
                        stayTime:2000,
                        type:"warn"
                    });
                    return;
                }
            }
         }
    });
}
function setChildPromotersHtmlView(response, page){
    var json = response,
    length = response.data.list.length,   //订单总数量
    alllist = response.data.list,
    html = [];
    for(var i = 0;i<length;i++){

        if(alllist[i].mobile){
            alllist[i].mobile = alllist[i].mobile.substring(0,3)+'****'+ alllist[i].mobile.substring(7,11)
        }
        html.push('<li class="item_list">');
        html.push('<span class="user_name"> '+  alllist[i].name+ '  </span>');
        html.push('<span class="user_phone">  '+ alllist[i].mobile + '  </span>');
        html.push('<span class="user_time">  '+ alllist[i].passtime +'  </span>');
        html.push('</li>');
    }
    if (page == 1 && html.length == 0) {
        $(".member-box").empty();
        var htmlData = [];
        htmlData.push('<div class="order_empty"><div class="empty-tip">还没有新用户哦~</div></div>');
        $('.member-box').append(htmlData.join(''));
        $('item_title').hide();
    } else {
        if(response.data.totalcount && response.data.totalcount>=0){
            $('.count').find('.num').html(response.data.totalcount)
        }
        $('.item_lists').append(html.join(''));
    }

}