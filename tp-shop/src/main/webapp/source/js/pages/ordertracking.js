var ordercode=xigou.getSessionStorage("logisticordercode");
if(ordercode == null)
{
    window.location.href="orders.html";
}

$(function() {
    var ordercode=xigou.getSessionStorage("logisticordercode");
    var params = {
        'token': xigou.getToken(),
        'code':ordercode,
    };

    xigou.activeUser.logistics({
        requestBody: params,
        callback: function(response, status) { // 回调函数
            if (status == xigou.dictionary.success) {
                if (null == response) {
                    $.tips({
                        content:response.msg || "获取物流信息失败",
                        stayTime:2000,
                        type:"warn"
                    })
                } else  {
                    switch (response.code) {
                        case "0":
                            setHtml(response.data);
                            break;
                        default:
                            $.tips({
                                content:response.msg||"获取物流信息失败",
                                stayTime:2000,
                                type:"warn"
                            })
                            break;
                    }
                }
            }
        }
    })
});

function setHtml(data) {
    if (typeof(data) == "undefined" || data == "") {
        $.tips({
            content:"获取物流信息为空",
            stayTime:2000,
            type:"warn"
        })
        return;
    }

    $("#company")[0].innerHTML = '<div></div><span>物流公司:</span>' + data.company;
    $("#logcode")[0].innerHTML = '<div></div><span>快递单号:</span>' + data.logcode;

    if (!data.loglist || data.loglist.length == 0) {
        $(".logistic_detail_box").hide();
        $(".logistic_empty").show();
        return;
    }
    $(".logistic_detail_box").show();
    $(".logistic_empty").hide();

    var htmlData = [];

    if (1 == data.loglist.length){
        $('.logistic_detail_box').addClass('logistic_detail_box_one');
    }

    for (var i = 0; i < data.loglist.length; i++) {
        var item = data.loglist[i];

        htmlData.push('<li>');
        htmlData.push(' <div class="title_box"><div class="logistic_detail_title">' + item.title + '</div></div>');
        htmlData.push(' <div class="logistic_detail_time">' + item.time + '</div>');
        htmlData.push('</li>');
    }

    $(".logistic_detail_list").html(htmlData.join(" "));
}