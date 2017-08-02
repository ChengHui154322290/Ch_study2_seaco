$(function() {
	var leftLength = ($(window).width())/2 - 40;
	$(".tuan_img").css({
		"left":leftLength
	})
	if(isWeiXin()){
		$(".ui-header").hide();
		$(".content").css({
			"margin-top":"0"
		})
		$(".bottom_mytuan").show();
		$("title").html("西客拼团");
	}
    var CurPage = 1;
    var bMaxPage = false;

    //$(".dropload-refresh").hide();

    window.onbeforeunload = function(){
        xigou.removeSessionStorage('tuan_gid');
    }

    var dropload = $(".tuan_list").dropload({
        scrollArea: window,
        loadDownFn: function (me) {
            if (bMaxPage) {
                return;
            }
            me.resetload();
            LoadTuanList(me);
        }
    })

    function LoadTuanList(me) {
        if (CurPage == 0) {
            $('.tuan_list').empty();
        }

        var param = {
            page: CurPage++,
            callback: function(response, status) {
                if (status == xigou.dictionary.success) {
                    if (response.code == 0) {
                        LoadTuanListData(me, response.data);
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
        };
        xigou.groupbuy.list(param);
    }

    function LoadTuanListData(me, data) {
        if (!data || !data.list || data.list.length == 0) { // 请求数据移除
            bMaxPage = true;
            if (me) {
                me.lock();
            }
            $(".dropload-down").hide();
            return;
        }

        var htmlData = [];
        for (var i = 0; i < data.list.length; i++) {
            htmlData.push('<div class="div_tuan_info">');
            htmlData.push(' <a href="tuan.html?gbid=' + data.list[i].gbid + '">');
            htmlData.push('     <div class="divsale">');
            htmlData.push('         <img src="' + data.list[i].imgurl + '">');
            htmlData.push('     </div>');
            htmlData.push('     <div class="div_content">');       
/*            htmlData.push('         <div class="country">');
            if (data.list[i].countryimg) {
                htmlData.push('<img class="contry-img" src="' + data.list[i].countryimg + '">');
            }
            if (data.list[i].countryname) {
                htmlData.push('<span class="country-info">' + data.list[i].countryname + '</span>')
            }
            if (data.list[i].channel) {
                htmlData.push('<span class="country-info">' + data.list[i].channel + '</span>')
            }
            htmlData.push('         </div>');*/
            htmlData.push('         <div class="ssaleTitle">'+ data.list[i].name + '</div>');
            /*htmlData.push('         <div class="ssale_desc">' + data.list[i].feature + '</div>');*/
            htmlData.push('     	<div class="tuan_item">');
            htmlData.push('     		<div class="tuan_item_count">'+ data.list[i].pa + '人团<span class="groupPrice"><i>¥</i>'+ data.list[i].groupprice + '</span><del class="salePrice"><i>¥</i>'+ data.list[i].saleprice + '</del>' );
            htmlData.push('         	<div class="div_ssale_price">去开团</div>');
            htmlData.push('     		</div>');
            htmlData.push('     	</div>');
            htmlData.push('     </div>');
            htmlData.push(' </a>');
            htmlData.push('</div>');
        }

        $('.tuan_list').append(htmlData.join(' '));

        if (data.curpage == data.totalpagecount) {  // 获取到最大页数据
            bMaxPage = true;
            if (me) {
                me.lock();
            }
            $(".dropload-down").hide();
        }
    }
})

