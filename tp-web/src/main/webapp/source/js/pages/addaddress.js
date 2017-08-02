/**
 *
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-12 17:16:47
 * @version Ver 1.0.0
 */
var selectType = xigou.getQueryString("selectType");
$(function() {
    initArea();
    xigou.getLoginUserInfo({
        callback:function(userinfo,status){
            if(status==xigou.dictionary.success){

                setbackpage();

                newAdd();

                initValiData();

                var data=xigou.getSessionStorage("add_address",true),
                    address=xigou.getLocalStorage("my_address",true)||{};

                if(data){
                    $("#name").val(data.name);
                    $("#telephone").val(data.telephone);
                    $("#addressinfo").val(data.addressinfo);
                    $("#identityCard").val(data.identityCard);
                }
                //InitUpdataphoto(data);
                if(address.provinceid){
                    // $("#area").text(address.province+" "+address.city+" "+address.district+" "+address.street);
                    // $("#rela_area").val(address.province+" "+address.city+" "+address.district+" "+address.street);
                    $("#area").text(address.province+" "+address.city+" "+address.district+" "+address.street);
                    $("#rela_area").val(address.province+" "+address.city+" "+address.district+" "+address.street);
                }
            }else{
                window.location.href="index.html";
            }
        }
    });
    var selectBackurl = "addrlist.html?showChooseArea=false&backurl=addaddress.html&selecturl="+xigou.getQueryString("selecturl")||"addrlist.html?showChooseArea=false&backurl=addaddress.html";
    $("#provinces").attr("href",selectBackurl);
    if($("#provinces").length > 0)
    {
        $("#provinces")[0].onclick=function() {
        var data={
            name:$("#name").val(),
            telephone:$("#telephone").val(),
            addresscode:{},
            addressinfo:$("#addressinfo").val(),
            isdefault:$("#isdefault").prop("checked")?"1":"0",
            identityCard:$("#identityCard").val()
            // frontimg: $('#upload_img_itme1').attr('src'),
            // backimg: $('#upload_img_itme2').attr('src')
        };

        xigou.setSessionStorage("add_address",data,true);
    };
    }
    $('.goBack').click(function(event) {
        var selectType = xigou.getQueryString("selectType");
        setSetRecvaddress(selectType)
    });
});

//用户-收货地址-新增
function newAdd(){
    $('.address-save,.address-save_wx')[xigou.events.click](function() {
        if (!xigou.valiformdata.check(null, valiDataCallBack)) {
            return;
        }
        if($("#identityCard").val() == "" || $("#identityCard").val() == undefined || $("#identityCard").val() == null){
            $.tips({
                content:"身份证号码不能为空",
                stayTime:2000,
                type:"warn"
            });
            return;
        }else{
             if(!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test($("#identityCard").val()))){
                console.log($("#identityCard").val(),'$("#identityCard").val()')
                $.tips({
                    content:"请添加正确的身份证号码",
                    stayTime:2000,
                    type:"warn"
                });
                return;
             }
        }
        // var address=xigou.getLocalStorage("my_address",true)||{
        //     "provid": "",
        //     "cityid": "",
        //     "districtid": "",
        //     "streetid": "",
        // };

        var address=xigou.getLocalStorage("my_address",true);
        var areaidlist=$("#areaidlist").val().split(",");
        var relalArealist=$("#rela_area").val().split(" ");
        var streetId=$("#streetId").val();
        var relaStreet=$("#rela_street").val();
        var params = {
            token: xigou.getToken(),
            name:$("#name").val(),
            tel:$("#telephone").val(),
            provid: areaidlist[0],
            provname:relalArealist[0],
            cityid: areaidlist[1],
            cityname:relalArealist[1],
            districtid: areaidlist[2],
            districtname:relalArealist[2],
            streetid: streetId,
            streetname:relaStreet,
            info:$("#addressinfo").val(),
            isdefault:$("#isdefault").prop("checked")?"1":"0",
            identityCard:$("#identityCard").val()
            // frontimg: $('#upload_img_itme1').attr('src'),
            // backimg: $('#upload_img_itme2').attr('src')
        };

        // if (params.frontimg.split('address/upload.png').length > 1) {
        //     params.frontimg = '';
        // }
        // if (params.backimg.split('address/upload.png').length > 1) {
        //     params.backimg = '';
        // }

        xigou.activeUser.newAdd({
            requestBody: params,
            callback: function(response, status) { //回调函数
                if (status == xigou.dictionary.success) {
                    if (null == response) {
                        $.tips({
                            content:xigou.dictionary.chineseTips.server.value_is_null,
                            stayTime:2000,
                            type:"warn"
                        })
                        // xigou.alert(xigou.dictionary.chineseTips.server.value_is_null);
                    } else {
                        switch (response.code) {
                            case "0":
                                var dia=$.dialog({
                                    title:'',
                                    content:response.msg||"新增收货地址成功",
                                    button:["确认"]
                                });

                                dia.on("dialog:action",function(e){
                                    xigou.removeSessionStorage("add_address");
                                    xigou.removelocalStorage("my_address");

                                    if(xigou.getQueryString("selecturl")&&$('#isdefault').is(":checked")){
                                        xigou.removeSessionStorage("clearing_select_address");
                                        window.location.href="clearing.html";
                                    }
                                    if(backpage && backpage == "shippingaddressnull.html"){
                                        if(selectType == "1") {
                                            window.location.href="recvaddress.html?selectType=1";
                                        }else if(selectType == "2"){
                                            window.location.href = "recvaddress.html?selectType=2";
                                        }else{
                                            window.location.href = "recvaddress.html";
                                        }
                                    }else if(backpage){
                                        if(selectType == "1") {
                                            window.location.href="recvaddress.html?selectType=1";
                                        }else if(selectType == "2"){
                                            window.location.href="recvaddress.html?selectType=2";
                                        }else{
                                            window.location.href=backpage;
                                        }
                                    }
                                    else{
                                        if(selectType == "1") {
                                            window.location.href="recvaddress.html?selectType=1";
                                        }else if(selectType == "2"){
                                            window.location.href="recvaddress.html?selectType=2";
                                        }else{
                                            window.location.href=xigou.getQueryString("selecturl")||"recvaddress.html";
                                        }
                                    }
                                });

                                // xigou.alert({
                                //     message:response.msg||"新增收货地址成功",
                                //     callback:function(){

                                //         xigou.removeSessionStorage("add_address");
                                //         xigou.removelocalStorage("my_address");

                                //         if(xigou.getQueryString("selecturl")&&$('#isdefault').is(":checked")){
                                //             xigou.removeSessionStorage("clearing_select_address");
                                //             window.location.href="clearing.html";
                                //         }
                                //         if(backpage && backpage == "shippingaddressnull.html"){
                                //          window.location.href = "shippingaddress.html";
                                //         }else if(backpage){
                                //             window.location.href=backpage;
                                //         }
                                //         else{
                                //             window.location.href=xigou.getQueryString("selecturl")||"shippingaddress.html";
                                //         }
                                //     }
                                // });
                                break;
                            case "-100":
                                var dia=$.dialog({
                                    title:'',
                                    content:response.msg||"用户失效，请重新登录。",
                                    button:["确认"]
                                });

                                dia.on("dialog:action",function(e){
                                    console.log(e.index);
                                    xigou.removelocalStorage("token");
                                    window.location.href="logon.html";
                                });
                                break;
                            default:
                                $.tips({
                                    content:response.msg||"新增收货地址失败",
                                    stayTime:2000,
                                    type:"warn"
                                });
                                break;
                        }
                    }
                } else {
                    $.tips({
                        content:'请求失败，详见' + response,
                        stayTime:2000,
                        type:"warn"
                    })
                    // xigou.alert('请求失败，详见' + response);
                }
            }
        });
    });
};


function valiDataCallBack(message, id, mname) {
    $.tips({
        content:message,
        stayTime:2000,
        type:"warn"
    });

    $("#"+id).focus();
};

function initValiData() {
    var _form = {
        methods: {
            "name": {
                required: "required",
                checkXss:"checkXss"
            },
            "telephone":{
                required: "required",
                mobile:"mobile"
            },
            "rela_area": {
                required: "required"
            },
            "addressinfo":{
                required: "required"
            }
        },
        errors: {
            "name": {
                required: "收货人姓名不能为空",
                checkXss:"请正确填写收货人姓名"
            },
            "telephone":{
                required: "手机号不能为空",
                mobile:"请填写正确的手机号"
            },
            "rela_area": {
                required: "请选择所在区域"
            },
            "addressinfo":{
                required: "详细地址不能为空"
            }
        }
    };
    return xigou.valiformdata.initValiData(_form);
};

var backpage;
var setbackpage = function(){
    backpage = xigou.getQueryString("selecturl") || xigou.getSessionStorage("select_address_backpage",true);
    if(backpage){
        $('a.back').attr('href',backpage);
    }
};

function InitUpdataphoto(data) {
    //var itemWidth = document.getElementsByClassName("div_updata_photo_item")[0].offsetWidth || 170;
    //var itemHeight = Math.round(itemWidth * 217 / 341);
    //$('.div_up_photo_area').css("height",itemHeight);

    if (data && data.frontimg) {
        $('#upload_img_itme1').attr('src', data.frontimg);
    }
    if (data && data.backimg) {
        $('#upload_img_itme2').attr('src', data.backimg);
    }

    OnSeletPhoto();
}

function OnSeletPhoto() {
    $('.div_updata_photo_item')[xigou.events.click](function() {
        var $input = $(this).find('input');
        var img = $(this).find('img');
        $input[0].click();
    })

    $('.input_photo').change(function() {
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

            //if (file.size > 2*1024*1024) {
            //    $.tips({
            //        content:'请选择小于2M的图片',
            //        stayTime:2000,
            //        type:"warn"
            //    });
            //    return;
            //}

            var reader = new FileReader();
            var img = $(this).parent('.div_updata_photo_item').find('img');

            reader.onload = (function(){
                var imageStr = this.result;
                var quality =  15;
                var loadCallback = function(image_obj){
                    imageStr = image_obj.src;
                    uploadImageUrl(imageStr, img);
                };

                imageStr = xigou.compress2(imageStr, quality, loadCallback);
            });

            reader.readAsDataURL(file);
        }
    })
}

function uploadImageUrl(imageStr, img) {
    var params = {
        imgstream: imageStr.toString().split(",")[1],
        token: xigou.getToken()
    };

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
                img.attr('src', response.data.path);
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
//设定是否为提交订单页面选择地址
function setSetRecvaddress(selectType){
    if(selectType == "1"){
        $('.goBack').attr('href', 'recvaddress.html?selectType=1');
    }else if(selectType == "2"){
        $('.goBack').attr('href', 'recvaddress.html?selectType=2');
    }else{
        $('.goBack').attr('href', 'recvaddress.html');
    }
}