function initgeetest() {
    var params = {};
    var dp = {
        requestBody: params,
        callback: function (response, status) { //回调函数
            if (status == xigou.dictionary.success) {
                if (null == response) {

                    // xigou.alert(xigou.dictionary.chineseTips.server.value_is_null);
                } else {
                    if (response.code == "0") {
                        $("#randid").val(response.data.randid);
                        initGeetest({
                            gt: response.data.gt,
                            challenge: response.data.challenge,
                            product: "embed", // 产品形式，包括：float，embed，popup。注意只对PC版验证码有效
                            width: "100%",
                            offline: !response.data.success,
                        }, handlerEmbed);
                    } else {
                        xigou.alert(response.data.msg);
                    }
                }
            } else {
                xigou.alert('请求失败，详见' + response);
            }
        }
    };
    xigou.activeUser.getPreGeetest(dp);
}

function showpopgeetest() {
    var htmlData = [];
    htmlData.push("<div class='img_code_bg' style=' height:" + $(window).height() + "px'></div>");
    htmlData.push('<div class="div_img_code" style="width: ' + ($(window).width() - 40) + 'px; margin: 0px;padding: 0px;left: 20px;top:6%;height: auto;"> ');
    // htmlData.push('<div class="div_code_title">验证码</div>');
    htmlData.push('<div class="div_code_info" style="height: auto">');
    // htmlData.push('<div class="div_code_img">');
    // htmlData.push('<img src="' + xigou.activeHost +  'captcha/graphic.htm?tel=' + tel + '&type=' + type + '">');
    // htmlData.push('</div>');
    // htmlData.push('<div class="div_code_input">');
    // htmlData.push('<input class="input_img_code" type="text" placeholder="请输入验证码">')
    // htmlData.push('</div>');
    htmlData.push('<div id="embed-captcha" style="padding: 4px 0px; "  ></div><input type="hidden" id="randid">');
    htmlData.push('<div class="div_code_btn">');
    htmlData.push('<div class="div_code_btn1">取消</div>');
    htmlData.push('<div class="div_code_btn2">确认</div>');
    htmlData.push('</div>');
    htmlData.push('</div>');
    htmlData.push('</div>');
    $("body").append(htmlData.join(''));


    initgeetest();

    // 取消
    $('.div_img_code .div_code_btn1').click(function () {
        destroy();
    });
}
function destroy() {
    $(".img_code_bg").hide().remove();
    $(".div_img_code").hide().remove();
}

function setCodeTime(time,$btn){
    $btn.text(time);
    $btn.addClass('disable');
    var _flag=setInterval(function(){
        if(time<=0){
            clearInterval(_flag);
            $btn.text("获取验证码");
            $btn.removeClass('disable');
            return;
        }
        $btn.text(--time);
    },1000);
}