$(function() {
    // 读取当前售后详情
    var asid = xigou.getQueryString("asid");
    var _aftersaleslist = xigou.getLocalStorage("aftersaleslist");
    var datalist = JSON.parse(_aftersaleslist);

    for (var i = 0; i < datalist.length; i++) {
        var data = datalist[i];
        if (data.asid == asid) {
            InitPage(data);
            return;
        }
    }
});

function InitPage(data) {
    switch (data.status) {
        case '1':     // 待售后审核
            $('.div_splite').hide();
            $('.div_return_desc_info').hide();
            $('#id_logistics_info').hide();
            $('#modify_return').hide();
            $('.ui-footer').hide();
            $('#tuihuo').hide();

            OnClickCancalReturn(data);
            break;
        case '2':   // 审核不通过
            $('#id_opt_div').hide();
            $('.div_tip_address').hide();
            $('#div_return_info').removeClass('div_add_address');
            $('#div_return_info').addClass('div_no_address');
            $('#id_logistics_info').hide();
            $('.ui-footer').hide();
            $('.div_updata_photo_box').addClass('no_logistics_info');
            $('#div_return_desc').text('驳回理由');
            break;
        case '3':   // 审核通过
            $('#id_opt_div').hide();
            CheckPass(data.asid);
            OnClickconfirm(data);
            break;
        case '4':   // 取消退货
            $('.complete').hide();
            $('#id_opt_div').show();
            $('#cancal_return').hide();
            $('#modify_return').css("margin-left", "0px");
            $('#id_logistics_info').hide();
            OnClickModifyReturn(data);
            break;
        case '5':   // 退货中
        case '6':   // 退款中
        case '7':   // 退款完成
            $('.complete').hide();
            $('#id_logistics_info').removeClass('div_no_complete');
            $('#id_logistics_info').addClass('div_with_complete');
            $('#input_no').attr("disabled", "disabled");
            break;
        case '8':   // 退货失败
            $('#id_opt_div').hide();
            $('.div_tip_address').hide();
            $('#div_return_info').removeClass('div_add_address');
            $('#div_return_info').addClass('div_no_address');
            $('#id_logistics_info').hide();
            $('.ui-footer').hide();
            $('.div_updata_photo_box').addClass('no_logistics_info');
            $('#div_return_desc').text('驳回理由');
            if (parseInt(data.historycount) < 3) {
                $('#id_opt_div').show();
                $('#cancal_return').hide();
            }
            break;
        default:
            break;
    }

    // 售后单号
    if (data.ascode) {
        $('#id_returnNo').text(data.ascode);
    }

    // 申请时间
    if (data.applydate) {
        $('#id_time').text(data.applydate);
    }

    // 售后状态
    if (data.statusdesc) {
        $('#id_returnresult').text(data.statusdesc);
    }

    // 驳回理由
    if (data.kfinfo) {
        $('#div_return_info').text(data.kfinfo);
    }

    // 商品图片
    if (data.itemimg) {
        $('#id_item_img').attr("src", data.itemimg);
    }

    // 商品名称
    if (data.itemname) {
        $('.ui_list_name').text(data.itemname);
    }

    // 商品单价
    if (data.itemprice) {
        var linepriceYan = data.itemprice.split('.')[0] || '00';
        var linepriceFen = data.itemprice.split('.')[1] || '00';
        document.getElementById("id_line_price").innerHTML = '¥<span>' + linepriceYan + '</span>.' + linepriceFen;
    }

    // 退货数量
    if (data.returncount) {
        $('#id_count').text('×' + data.returncount);
    }

    // 退货总价和数量
    if (data.returnprice) {
        var totalriceYan = data.returnprice.split('.')[0] || '00';
        var totalpriceFen = data.returnprice.split('.')[1] || '00';
        $('.item_totle_price').html('¥<span>' + totalriceYan + '</span>.' + totalpriceFen);

        if (data.returncount) {
            $('.total_price_info').html('共' + data.returncount + '件 退款金额：<span class="price_fen">¥<span class="price_yuan">' + totalriceYan + '.</span>' + totalpriceFen + '</span>');
        }
    }

    // 退货原因
    if (data.returnreasondesc) {
        $('#id_span_reson').text(data.returnreasondesc);
    }

    // 退货说明
    if (data.returninfo && data.returninfo.length > 0) {
        $('#id_span_explain').text(data.returninfo);
    }
    else {
        $('.div_return_explain').hide();
    }

    if (data.returnimg && data.returnimg.length > 0) {
        for (var i = 0; i < data.returnimg.length && i < 5; i++) {
            var divId = 'upload_itme' + (i + 1);
            var ImgId = 'id_img_photo' + (i + 1);
            $('#' + divId).show();
            $('#' + ImgId).attr('src', data.returnimg[i]);
        }
    }

    // 快递公司
    if (data.company) {
        $('#input_company').attr('value', data.company);
    }

    // 快递单号
    if (data.logisticcode) {
        $('#input_no').attr('value', data.logisticcode);
    }

    OnClickAddress();
    OnTextChange();
};

function CheckPass(asid) {
    $('#id_logistics_company')[xigou.events.click](function(){
        window.location.href = 'selectcompany.html?asid=' + asid;
    })
}

// 撤销售后申请
function OnClickCancalReturn(data) {
    $('#cancal_return')[xigou.events.click](function(){
        params = {
            'token': xigou.getLocalStorage('token'),
            'asid':data.asid,
        }

        xigou.aftersales.cancal({
            requestBody: params,
            callback:function(response, status) {
                if (status == xigou.dictionary.success) {
                    if (response.code != 0) {
                        $.tips({
                            content:response.msg || '撤销售后申请失败',
                            stayTime:2000,
                            type:"warn"
                        });
                        return;
                    }

                    history.go(-1);
                }
                else {
                    $.tips({
                        content:response.msg || '撤销售后申请失败',
                        stayTime:2000,
                        type:"warn",
                    });
                }
            }
        })
    })
}

// 修改售后申请
function OnClickModifyReturn(data) {
    $('#modify_return')[xigou.events.click](function() {
        var pageInfo = {};
        pageInfo.imgurl = data.itemimg;
        pageInfo.itemname = data.itemname;
        pageInfo.itemprice = data.itemprice;
        pageInfo.buycount = data.returncount;
        pageInfo.maxcount = data.buycount;
        if (data.returnreasondesc){
            pageInfo.reasonCodeDesc = data.returnreasondesc;
            pageInfo.reasonCode = data.returnreason;
        }
        if (data.returninfo) {
            pageInfo.returninfo = data.returninfo;
        }

        if (data.returnimg && data.returnimg.length > 0) {
            pageInfo.imgList = {};
            for (var i = 0; i < data.returnimg.length && i < 5; i++) {
                pageInfo.imgList[i] = data.returnimg[i];
            }
        }

        if (data.returninfo) {
            pageInfo.returninfo = data.returninfo;
        }

        if (data.linkname) {
            pageInfo.linkname = data.linkname;
        }

        if (data.linktel) {
            pageInfo.linktel = data.linktel;
        }

        if (data.ordercode) {
            pageInfo.ordercode = data.ordercode;
        }

        if (data.lineid) {
            pageInfo.lineid = data.lineid;
        }

        xigou.setLocalStorage("aftersalesparam",JSON.stringify(pageInfo));
        window.location.href = 'customerservice.html';
    })
}

// 提交退货物流信息
function OnClickconfirm(data){
    $('.confirm')[xigou.events.click](function(){
        if (!$(this).hasClass("confirm_enable")) {
            return;
        }

        var params = {
            'token':  xigou.getToken(),
            'asid': data.asid,
            'logisticcode': $("#input_no")[0].value,
            'company': data.company,
            'companycode': data.companycode,
        };

        xigou.aftersales.submitlogistic({
            requestBody: params,
            callback:function(response, status) {
                if (status == xigou.dictionary.success && response) {
                    if (response.code == 0) {
                        history.go(-1);
                    }
                    else {
                        $.tips({
                            content:response.msg || "提交物流单号失败",
                            stayTime:2000,
                            type:"warn"
                        });
                    }
                }
                else if (!response) {
                    $.tips({
                        content:"提交物流单号失败",
                        stayTime:2000,
                        type:"warn"
                    });
                }
                else {
                    $.tips({
                        content:response.msg || "提交物流单号失败",
                        stayTime:2000,
                        type:"warn"
                    });
                }
            }
        })
    })
}

function OnClickAddress(){
    $('.div_tip_address')[xigou.events.click](function(){
        var asid = xigou.getQueryString("asid");
        window.location.href="returnaddress.html?asid=" + asid;
    })
}

function OnTextChange(){
    var name = document.getElementById('input_company').value;
    var number = document.getElementById('input_no').value;

    if (name && number) {
        $('.confirm').addClass('confirm_enable');
    }
    else {
        $('.confirm').removeClass('confirm_enable');
    }
}