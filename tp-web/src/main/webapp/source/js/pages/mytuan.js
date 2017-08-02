$(function() {
    xigou.getLoginUserInfo({
        callback:function(userinfo,status){
            if(status != xigou.dictionary.success){
                xigou.setSessionStorage("loginjump_url", "mytuan.html");
                window.location.href = "logon.html";
            }
            else {
                GetUserTuanInfo();

                var nDefaulePage = parseInt(xigou.getSessionStorage("tundetails_backurl"));
                if (nDefaulePage == 1)
                {
                    setdefaulesel(1);
                }
            }
        }
    });
    if(isWeiXin()){
    	$(".ui-header").hide();
    	$(".ui-tab-nav").css({
    		"top":"10px"
    	})
    	$(".ui-tab").css({
    		"margin-top":"0"
    	})
    	$("title").html("我的团购");
    }
});

function GetUserTuanInfo() {
    var params = {
        token: xigou.getToken(),
    }

    //params.token = 'b987c1223f4dacac27deb4408d4569c5';

    xigou.groupbuy.my({
        requestBody:params,
        callback: function(response, status) {  // 回调函数
            if (status == xigou.dictionary.success) {
                if (response.code == 0) {
                    InitLaunchPage(response.data.launch);
                    InitJoinPage(response.data.join);
                }
                else {
                    $.tips({
                        content:response.msg||'获取数据失败,请检测网络连接!',
                        stayTime:2000,
                        type:"warn"
                    });
                }
            }
            else if (!response) {
                $.tips({
                    content:'获取数据失败,请检测网络连接!',
                    stayTime:2000,
                    type:"warn"
                });
            }
            else {
                $.tips({
                    content:response.msg||'获取数据失败,请检测网络连接!',
                    stayTime:2000,
                    type:"warn"
                });
            }
        }
    })
}

function InitLaunchPage(data) {
    if (!data || data.length == 0) {
        $('.tuan_launch').empty().addClass('div_item_none');
    }
    else {
        var htmlData = [];
        for (var i = 0; i < data.length; i++) {
            htmlData.push('<div class="tuan_item" gbid="' + data[i].gbid + '" gid="' + data[i].gid + '">');
            htmlData.push(' <div class="item_img">');
            if (data[i].pic) {
                htmlData.push('<img src="' + data[i].pic + '">');
            }
            htmlData.push(' </div>');
            htmlData.push(' <div class="item_info">');
            htmlData.push('     <div class="div_name">' + data[i].name + '</div>');
            htmlData.push('     <div class="div_info1">');
            if (data[i].gprice) {
                var Yuan = data[i].gprice.split('.')[0] || ' ';
                var Jiao = data[i].gprice.split('.')[1] || ' ';
                htmlData.push('         <div class="item_price">¥' + Yuan + '.<span>' + Jiao + '</span></div>');
            }
            switch (data[i].status) {
                case '1':
                    htmlData.push('<div class="tuan_status_desc">进行中</div>');
                    break;
                case '2':
                    htmlData.push('<div class="tuan_status_desc">参团成功</div>');
                    break;
                default:
                    htmlData.push('<div class="tuan_status_desc">参团失败</div>');
                    break;
            }
            htmlData.push('     </div>');
            htmlData.push('     <div class="div_info2">');
            // if (data[i].sprice) {
            //     htmlData.push('         <div class="div_old_price">¥' + data[i].sprice + '</div>');
            // }
            var percent = parseInt(100*data[i].fa/data[i].pa);
            htmlData.push('         <div class="div_progress"><div style="width: ' + percent + '%"></div></div>');
            htmlData.push('         <span class="tuan_peson_coun">' + data[i].fa + '/' + data[i].pa + '</span>');
            htmlData.push('     </div>');
            htmlData.push(' </div>');
            htmlData.push('</div>');
        }
        $('.tuan_launch').append(htmlData.join(' '));
        $('.tuan_launch .tuan_item')[xigou.events.click](function(){
            var gbid = $(this).attr('gbid');
            var gid = $(this).attr('gid');
            xigou.removeSessionStorage('tuan_gid');
            var backurl = xigou.setSessionStorage('tundetails_backurl', 0);
            window.location.href = 'tuan.html?gbid=' + gbid + '&gid=' + gid;
        })
    }
}

function InitJoinPage(data) {
    if (!data || data.length == 0) {
        $('.tuan_join').empty().addClass('div_item_none');
    }
    else {
        var htmlData = [];
        for (var i = 0; i < data.length; i++) {
            htmlData.push('<div class="tuan_item" gbid="' + data[i].gbid + '" gid="' + data[i].gid + '">');
            htmlData.push(' <div class="item_img">');
            if (data[i].pic) {
                htmlData.push('<img src="' + data[i].pic + '">');
            }
            htmlData.push(' </div>');
            htmlData.push(' <div class="item_info">');
            htmlData.push('     <div class="div_launch_people">' + data[i].memberName + '发起了此团</div>')
            htmlData.push('     <div class="div_name">' + data[i].name + '</div>');
            htmlData.push('     <div class="div_info1">');
            if (data[i].gprice) {
                var Yuan = data[i].gprice.split('.')[0] || ' ';
                var Jiao = data[i].gprice.split('.')[1] || ' ';
                htmlData.push('         <div class="item_price">¥' + Yuan + '.<span>' + Jiao + '</span></div>');
            }
            switch (data[i].status) {
                case '1':
                    htmlData.push('<div class="tuan_status_desc">进行中</div>');
                    break;
                case '2':
                    htmlData.push('<div class="tuan_status_desc">参团成功</div>');
                    break;
                default:
                    htmlData.push('<div class="tuan_status_desc">参团失败</div>');
                    break;
            }
            htmlData.push('     </div>');
            htmlData.push('     <div class="div_info2">');
            // if (data[i].sprice) {
            //     htmlData.push('         <div class="div_old_price">¥' + data[i].sprice + '</div>');
            // }
            var percent = parseInt(100*data[i].fa/data[i].pa);
            htmlData.push('         <div class="div_progress"><div style="width: ' + percent + '%"></div></div>');
            htmlData.push('         <span class="tuan_peson_coun">' + data[i].fa + '/' + data[i].pa + '</span>');
            htmlData.push('     </div>');
            htmlData.push(' </div>');
            htmlData.push('</div>');
        }
        $('.tuan_join').append(htmlData.join(' '));
        $('.tuan_join .tuan_item')[xigou.events.click](function(){
            var gbid = $(this).attr('gbid');
            var gid = $(this).attr('gid');
            var backurl = xigou.setSessionStorage('tundetails_backurl', 1);
            xigou.removeSessionStorage('tuan_gid');
            window.location.href = 'tuan.html?gbid=' + gbid + '&gid=' + gid;
        })
    }
}

// 切换当前选中List
function setdefaulesel(npage)
{
    var scroller = $('.ui-tab-content')[0];
    var nav = $('.ui-tab-nav')[0];

    $(scroller.children[0]).removeClass('current');
    $(nav.children[0]).removeClass('current');
    $(scroller.children[npage]).addClass('current');
    $(nav.children[npage]).addClass('current');
    var width = -1 * scroller.clientWidth * npage / 2;
    scroller.style.transform = "translate(" + width + "px, 0px)";
    $(scroller.children)[0].height = "0px";
}