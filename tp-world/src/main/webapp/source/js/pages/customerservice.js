// 申请售后
var _aftersalesparam = xigou.getLocalStorage("aftersalesparam");
var aftersalesparam = {};
if (_aftersalesparam != "") {
    aftersalesparam = JSON.parse(_aftersalesparam);
}

$(function(){
    if (!aftersalesparam || !aftersalesparam.itemname) {
        history.go(-1);
        return;
    }

    InitPage(aftersalesparam);
    InitUpdataphoto(aftersalesparam);
    SelectReturnReson(aftersalesparam);
    ClickConfirm();
});

function InitPage(pageInfo){
    $('#id_item_img').attr('src', pageInfo.imgurl);         // 商品图片
    $('.ui_list_name').text(pageInfo.itemname);             // 商品名称
    if (pageInfo.returninfo) {
        $('.return_desc')[0].value = pageInfo.returninfo;
    }
    InitTotlePrice(pageInfo.buycount, aftersalesparam.maxcount, pageInfo.itemprice);

    if (pageInfo.reasonCodeDesc) {
        $('.span_return_reson').text(pageInfo.reasonCodeDesc);
    }


    //数量加减cart_shop_opt
    $(".cart_shop_opt").cartshopopt(function(val, num, e) {
        editNumFun(val, pageInfo.itemprice); //num 加：1  减：-1
    });

    OnTextChange();
}

// 初始化数量和总价信息
function  InitTotlePrice(count, maxcount, itemprice) {
    $('.lineprice').text('¥' + itemprice);        // 商品单价
    $('.cart_shop_opt').attr('maxnum', maxcount);

    if (1 == count) {
        $('.quantity_decrease').addClass('disabled');
    }
    if (count == maxcount) {
        $('.quantity_increase').addClass('disabled');
    }
    $('.car_item').text(count);

    SetTotalCountPrice(count, itemprice);
}

// 设置数量和总价
function  SetTotalCountPrice(count, itemprice) {
    var fitemprice = parseFloat(itemprice);
    if (!fitemprice) {
        fitemprice = 0;
    }

    fitemprice = fitemprice*count;
    var TotalPriceYuan = String(fitemprice).split('.')[0] || '00';
    var TotalPriceJiao = String(fitemprice).split('.')[1] || '00';
    $('.ui_price_info').text('¥' + TotalPriceYuan + '.' + TotalPriceJiao);
    $('.total_price_info').html('共' + count + '件 退款金额：<span class="price_fen">¥<span class="price_yuan">' + TotalPriceYuan + '.</span>' + TotalPriceJiao + '</span>');
}

//购物车-商品数量改变
function editNumFun(num, itemprice) {
    SetTotalCountPrice(num, itemprice);
}

function SelectReturnReson(aftersalesparam) {
    $('.return_reson')[xigou.events.click](function(){
        aftersalesparam.buycount = $('.car_item').text();

        xigou.setLocalStorage("aftersalesparam",JSON.stringify(aftersalesparam));
        window.location.href = 'returnreson.html';
    })
}

// 初始化上传照片
function  InitUpdataphoto(params) {
    var itemWidth = document.getElementById("upload_itme1").offsetWidth || 60;
    $('.div_updata_photo_item').css(
        "height",itemWidth
    );

    var index = 1;
    if (params.imgList) {
        for (; index <= 6; index++) {
            if (!params.imgList[index - 1]) {
                break;
            }
            $('#upload_itme' + index).css("visibility", "visible");
            $('#upload_img_itme' + index).attr('src', params.imgList[index - 1]);
        }
    }

    $('#upload_itme' + index).css("visibility", "visible");

    OnSeletPhoto();
}

// 上传图片
function OnSeletPhoto(){$('.div_updata_photo_item')[xigou.events.click](function(){
        var indxe = this.getAttribute('index');
        $("#file_photo_" + indxe)[0].click();
    })

    $('.input_photo').change(function(){
        var files = event.target.files;

        for (var i = 0; i < files.length; i++) {
            var file = files[i];

            // 只支持小于2M的图片
            if (!file.type.match('image.*')) {
                $.tips({
                    content:'请选择图片文件',
                    stayTime:2000,
                    type:"warn"
                });
                return;
            }
            if (file.size > 2*1024*1024) {
                $.tips({
                    content:'请选择小于2M的图片',
                    stayTime:2000,
                    type:"warn"
                });
                return;
            }

            var reader = new FileReader();
            var inputId = this.getAttribute('id');
            var index = inputId[inputId.length - 1];

            reader.onload = (function(){
                var imageStr = this.result;
                uploadImageUrl(imageStr, index);
            })

            reader.readAsDataURL(file);
        }
    })
}

function uploadImageUrl(imageStr, index){
    var params = {
        imgstream: imageStr.toString().split(",")[1],
        token: xigou.getToken(),
    }

    xigou.activeUser.uploadimage({
        requestBody: params,
        callback: function(response, status) {
            if (status == xigou.dictionary.success) {
                if (!response || !response.data || !response.data.path || response.code != 0) {
                    $.tips({
                        content:response.msg || '上传图片失败,请检查网络连接',
                        stayTime:2000,
                        type:"warn"
                    });
                    return;
                }

                // 上传图片成功
                $('#upload_img_itme' + index).attr('src', imageStr);
                if (!aftersalesparam.imgList) {
                    aftersalesparam.imgList = {};
                }
                aftersalesparam.imgList[index] = response.data.path;
                $('#upload_itme' + (parseInt(index) + 1 )).css("visibility", "visible");
            }
            else {
                $.tips({
                    content:response.msg || '上传图片失败,请检查网络连接',
                    stayTime:2000,
                    type:"warn"
                });
            }
        }
    })
}

function OnTextChange(){
    var name = document.getElementById('input_name').value;
    var number = document.getElementById('input_way').value;

    if (name && number) {
        $('.confirm').addClass('confirm_enable');
    }
    else {
        $('.confirm').removeClass('confirm_enable');
    }
}

// 提交订单
function ClickConfirm(){
    $('.confirm')[xigou.events.click](function(){
        if (!$('.confirm').hasClass('confirm_enable')) {
            return;
        }

        var params = {
            'token': xigou.getLocalStorage('token'),
            'returncount': $('.car_item').text(),
            'ordercode':aftersalesparam.ordercode,
            'returnreason':aftersalesparam.reasonCode,
            'returninfo': $('.return_desc')[0].value,
            'linkname': document.getElementById('input_name').value,
            'linktel': document.getElementById('input_way').value,
            'lineid': aftersalesparam.lineid,
        };

        if (aftersalesparam.imgList) {
            params.imageone = aftersalesparam.imgList[1] || "";
            params.imagetwo = aftersalesparam.imgList[2] || "";
            params.imagethree = aftersalesparam.imgList[3] || "";
            params.imagefour = aftersalesparam.imgList[4] || "";
            params.imagefive = aftersalesparam.imgList[5] || "";
        }

        xigou.aftersales.apply({
            requestBody: params,
            callback:function(response, status) {
                if (status == xigou.dictionary.success) {
                    if (response.code != 0) {
                        $.tips({
                            content:response.msg || '提交售后申请失败',
                            stayTime:2000,
                            type:"warn"
                        });
                        return;
                    }

                    xigou.removelocalStorage("aftersalesparam");
                    window.location.href = 'orders.html';
                }
                else {
                    $.tips({
                        content:response.msg || '提交售后申请失败',
                        stayTime:2000,
                        type:"warn",
                    });
                }

            }
        })
    })
}