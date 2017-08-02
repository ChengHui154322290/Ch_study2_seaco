var fromPage = xigou.getQueryString("fromPage");
$(function() {

    xigou.classify.navigation({
        callback: function(response, status) {  // 回调函数
            if (status == xigou.dictionary.success) {
                if (response.code == 0) {
                    InitPage(response.data);
                }
                else {
                    $.tips({
                        content:response.msg||'请求导航失败,请检测网络连接!',
                        stayTime:2000,
                        type:"warn"
                    });
                }
            }
            else if (!response) {
                $.tips({
                    content:'请求导航失败,请检测网络连接!',
                    stayTime:2000,
                    type:"warn"
                });
            }
            else {
                $.tips({
                    content:response.msg||'请求导航失败,请检测网络连接!',
                    stayTime:2000,
                    type:"warn"
                });
            }
        }
    })
});

/*$(".div_group").click(function(){
	$('.choose').show();
});*/
$(".li1").click(function(){
	$('.choose').hide();
	$(".div_group").html($(this).children("span").html());
	$(".div_group").attr('data','');

	$('.input_search').attr('placeholder','请输入相关产品名称');
});
$(".li2").click(function(){
	$('.choose').hide();
	$(".div_group").html($(this).children("span").html());
    $(".div_group").attr('data',9);
	$('.input_search').attr('placeholder','请输入您想要团购的商品');
});


function InitPage(data) {
    $('.ui-tab-content').show();
    $("form").submit(function(e){
        //e.preventDefault();
        return false;
    });
    Initcategories(data.categories);
    Initbrands(data.brands);

    InitSearchHistory();
}

function Initbrands(data) {
    var htmlData = [];

    for (var i = 0; i < data.length; i++) {
        htmlData.push('<div class="div_brands_name">' + data[i].name + '</div>');
        htmlData.push('<div class="div_brands_child">');

        if (!data[i].child) {
            continue;
        }

        for (var j = 0; j < data[i].child.length; j++) {
            htmlData.push('<div class="div_brands_child_item_box">');
            var name = data[i].child[j].name || " ";
            htmlData.push(' <div class="div_brands_child_item" brandid="' + data[i].child[j].brandid + '" name="' + name + '">');
            htmlData.push('     <img src="' + data[i].child[j].imgurl + '">');
            htmlData.push(' </div>');
            htmlData.push('</div>');
        }

        htmlData.push('</div>');
    }

    $('.div_brands').append(htmlData.join(' '));


    var brandsItems = $('.div_brands_child_item_box');
    if (brandsItems && brandsItems.length > 0) {
        var itemWidth = brandsItems[0].offsetWidth || 60;
        $('.div_brands_child_item_box').css(
            "height",itemWidth
        );
    }

    $('.div_brands_child_item').click(function() {
        window.location.href='search.html?brandid=' + $(this).attr("brandid") + '&name=' + $(this).attr("name");
    })
}

function Initcategories(data) {
    var MaxWidth = document.body.clientWidth - 30;
    var MaxCount = Math.floor(MaxWidth/13) * 2;
    var TmpLength = 0;

    var htmlData = [];
    for (var i = 0; i < data.length; i++) {
        if (!data[i].name) {
            data[i].name = ' ';
        }
        htmlData.push('<div class="div_img" categoryid="' + data[i].categoryid + '" + name="' + data[i].name + '">');
        htmlData.push(' <img src="' + data[i].imgurl + '">');
        htmlData.push('</div>');

        if (!data[i].child) {
            continue;
        }

        var NoArr = [];
        NoArr.push(0);
        var TmpLength = 0;
        for (var j = 0; j < data[i].child.length; j++) {
            TmpLength += GetStrLen(data[i].child[j].name);
            if (TmpLength >= MaxCount) {
                NoArr.push(j);
                TmpLength = GetStrLen(data[i].child[j].name)  + 6;
            }
            else {
                TmpLength += 6;
            }
        }
        NoArr.push(data[i].child.length);

        htmlData.push('<div class="div_child">')
        for (var j = 0; j < NoArr.length - 1; j++) {
            htmlData.push('<div class="div_child_item">')
            for (var k = NoArr[j]; k < NoArr[j+1] - 1; k++) {
                if (data[i].child[k].ishighlight == 1) {
                    htmlData.push('<div class="div_child_item_name highlight" categoryid="' + data[i].child[k].categoryid + '">' + data[i].child[k].name + '</div>');
                }
                else {
                    htmlData.push('<div class="div_child_item_name" categoryid="' + data[i].child[k].categoryid + '">' + data[i].child[k].name + '</div>');
                }
                htmlData.push('<div class="div_child_item_split"></div>');
            }
            if (data[i].child[NoArr[j+1] - 1].ishighlight == 1) {
                htmlData.push('<div class="div_child_item_name highlight" categoryid="' + data[i].child[NoArr[j+1] - 1].categoryid + '">' + data[i].child[NoArr[j+1] - 1].name + '</div>');
            }
            else {
                htmlData.push('<div class="div_child_item_name" categoryid="' + data[i].child[NoArr[j+1] - 1].categoryid + '">' + data[i].child[NoArr[j+1] - 1].name + '</div>');
            }
            htmlData.push('</div>');
        }
        htmlData.push('</div>');
    }

    $('.div_categories').append(htmlData.join(" "));

    $('.div_img').click(function() {
        window.location.href='search.html?categoryid=' + $(this).attr("categoryid") + '&name=' + $(this).attr("name");
    })

    $('.div_child_item_name').click(function() {
        window.location.href='search.html?categoryid=' + $(this).attr("categoryid") + '&name=' + this.innerHTML;
    })
}

// 获取字符串长度 中文字计2 英文字符计1
function GetStrLen(str) {
    var len = 0;
    for (var i = 0; i < str.length; i++) {
        var c = str.charCodeAt(i);
        if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {
            len++;
        }
        else {
            len += 2;
        }
    }

    return len;
}

function InitSearchHistory() {


    if(fromPage == "index"){
        $('.div_search_history').show();
    }else{
        $('.div_search_history').hide();
    }
    var _history = xigou.getLocalStorage("searchHistory");
    var historyList = [];
    if (_history != "") {
        historyList = JSON.parse(_history);
    }

    if (historyList.length == 0) {
        $('.div_history_list').hide();
        $('.div_noHistory').show();
    }
    else {
        $('.div_history_list').show();
        $('.div_noHistory').hide();

        //var htmlData = [];
        for (var i = 0; i < historyList.length; i++) {
            $("#id_ul_history").append('<li class="li_history_item"></li>');
            $("#id_ul_history li").eq(i).text( historyList[i]);
        }
        //$("#id_ul_history").append(htmlData.join(" "));

        $(".li_history_item")[xigou.events.click](function() {
            Search(this.innerHTML, historyList);
        })
    }

    $(".div_search")[xigou.events.click](function(){
        $('.ui-tab-content').hide();
        $('#input_search')[0].value = "";
        $('.div_search_history').show();

        $('#input_search').focus().select();
    })

    $('.cancal_search')[xigou.events.click](function() {
        $('.ui-tab-content').show();
        $('#input_search')[0].value = "";
        $('.div_search_history').hide();
        $('#input_search').blur();
    })

    $('#input_search').keyup(function(e) {
        var a = $(this).val();
        if (a && a.length > 0) {
            $('.div_clear_input').show();
            if (13 == e.keyCode) {  // 开始搜索
                a = a.replace(/(^\s*)|(\s*$)/g, "");
                if (a.length > 0) {
                    $('.ui-tab-content').show();
                    Search(a, historyList);
                }
            }
        }
        else {
            $('.div_clear_input').hide();
        }
    });

    $('.div_clear_input')[xigou.events.click](function() {
        $('.div_clear_input').hide();
        $('#input_search')[0].value = "";
    })

    $('.div_clear_history')[xigou.events.click](function() {
        var dia=$.dialog({
            title:'',
            content:"确认清空输入的搜索历史吗？ ",
            button:["取消","确认"]
        });

        dia.on("dialog:action",function(eDlg){
            if(1 == eDlg.index)
            {
                $('#id_ul_history').empty();
                $('.div_history_list').hide();
                $('.div_noHistory').show();
            }
        });

        historyList = [];
        xigou.removelocalStorage("searchHistory");


    })
}

function Search(keyWords, historyList) {
    // 记录搜索记录 最多记录10条
    var index = historyList.indexOf(keyWords);
    if (-1 != index) {
        historyList.splice(index, 1);
    }

    historyList.unshift(keyWords);
    if (historyList.length > 10) {
        historyList.pop();
    }
    xigou.setLocalStorage("searchHistory",JSON.stringify(historyList));
    $('.div_search_history').hide();
     var sp = $(".div_group").attr('data');

    if(fromPage == "index"){
        window.location.href="search.html?key=" + keyWords + "&fromPage=index"+"&sp="+sp;
    }else{
        window.location.href="search.html?key=" + keyWords+"&sp="+sp;
    }

}