/**
 * 
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-12 17:16:47
 * @version Ver 1.0.0
 */
 var data;
$(function() {
    data=xigou.getSessionStorage("edit_address",true);

    xigou.getLoginUserInfo({
        callback:function(userinfo,status){
            if(status==xigou.dictionary.success){
                
                submits();

                initValiData();

                // var mydata=xigou.getLocalStorage("my_address",true)||{};
                var mydata=xigou.getLocalStorage("my_address",true);
                if(data){
                    $("#name").val(data.name);
                    $("#name").attr("isdefault", data.isdefault);
                    $("#telephone").val(data.tel);
                    $("#identityCard").val(data.identityCard);
                    if(typeof(mydata) == "undefined" || mydata == "")
                    {
                    	initAreaAddress(data);
                    }
                    else
                    {
                        if(mydata.street == "")
                        {
                        	initAreaAddress(data);
                        }
                        else
                        {
                            mydata.aid = data.aid;
                            initAreaAddress(data);
                        }
                    }

                    $("#addressinfo").val(data.info);
                    InitUpdataphoto(data);
                }
            }else{
                window.location.href="index.html";
            }
        }
    });

    /* $("#provinces")[0].onclick=function() {
         var _data={
             aid:data.aid,
             name:$("#name").val(),
             tel:$("#telephone").val(),
             addresscode:{},
             info:$("#addressinfo").val(),
             identityCard:$("#identityCard").val(),
             frontimg: $('#upload_img_itme1').attr('src'),
             backimg: $('#upload_img_itme2').attr('src'),
             isdefault:$("#name").attr("isdefault")
         };

         xigou.setSessionStorage("edit_address",_data,true);
     };*/
});

// function htmlDecode ( str ) {
// 	 var ele = document.createElement('span');
// 	    ele.innerHTML = str;
// 	    return ele.textContent || ele.innerText;
   
// }

function initAreaAddress(data){
	xigou.setLocalStorage("my_address",data, true);
    $("#rela_area").val(data.province +" "+ data.city+" " + data.district);
    $("#areaidlist").val(data.provinceid+"," + data.cityid+"," + data.districtid);
    $("#rela_area").attr('data-value',$("#areaidlist").val());
    $("#rela_street").val(data.street);
    $('#streetId').val(data.streetid); 
    $("#rela_street").attr('data-value',data.streetid);
    initArea();
    $.ajax({
		dataType: 'json',
		cache: true,
		url: xigou.activeHost + xigou.activeUrl.position.getareatree+"?id="+data.districtid,
		type: 'GET',
		success: function(result) {
			streetArea.data=result;
		},
		accepts: {
			json: "application/json, text/javascript, */*; q=0.01"
		}
	});
}
function initAreaList(){
	var params = {
			'token': xigou.getToken(),
		};
	xigou.redeemManager.paramskulist({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length == 0) return false;
				var code = json.code;
				if (code == 0) {
					var selectArea = new MobileSelectArea();
					selectArea.init({
						trigger:$('.saleskucodelist'),
						value:$('#skucode').val(),
						level:1,
						data:response.data,
						position:"bottom"});
				}else{
					me.lock();
					$(".dropload-down").hide();
				}
			}
		}
	});
}


//用户-收货地址-编辑
function submits(){
    $('.address-edit, .address-save_wx')[xigou.events.click](function() {

        if (!xigou.valiformdata.check(null, valiDataCallBack)) {
            return;
        }

        var address=xigou.getLocalStorage("my_address",true);
        var areaidlist=$("#areaidlist").val().split(",");
        var relalArealist=$("#rela_area").val().split(" ");
        var streetId=$("#streetId").val();
        var relaStreet=$("#rela_street").val();
        
        var params = {
            token: xigou.getToken(),
            aid:address.aid,
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
            isdefault:$("#name").attr("isdefault"),
            identityCard:$("#identityCard").val(),
            frontimg: $('#upload_img_itme1').attr('src'),
            backimg: $('#upload_img_itme2').attr('src')
        };

        if (params.frontimg.split('address/upload.png').length > 1) {
            params.frontimg = '';
        }
        if (params.backimg.split('address/upload.png').length > 1) {
            params.backimg = '';
        }

        if(params){
            xigou.activeUser.edit({
                requestBody: params,
                callback: function(response, status) { //回调函数
                    if (status == xigou.dictionary.success) {
                        if (null == response) {
                            $.tips({
                                content:xigou.dictionary.chineseTips.server.value_is_null,
                                stayTime:2000,
                                type:"warn"
                            })
                        } else {
                            switch (response.code) {
                                case "0":
                                    var dia=$.dialog({
                                        title:'',
                                        content:response.msg||"编辑收货地址成功",
                                        button:["确认"]
                                    });

                                    dia.on("dialog:action",function(e){
                                        console.log(e.index);
                                        xigou.removeSessionStorage("add_address");
                                        xigou.removelocalStorage("my_address");
                                        window.location.href="recvaddress.html";
                                    });
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
                                        content:response.msg||"编辑收货地址失败",
                                        stayTime:2000,
                                        type:"warn"
                                    })
                                    break;
                            }
                        }
                    } else {
                        $.tips({
                            content:'请求失败，详见' + response,
                            stayTime:2000,
                            type:"warn"
                        })
                    }
                }
            });
        }
    });
};


function valiDataCallBack(message, id, mname) {
    xigou.toast(message);
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
            "area": {
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
            "area": {
                required: "请选择所在区域"
            },
            "addressinfo":{
                required: "详细地址不能为空"
            }
        }
    };
    return xigou.valiformdata.initValiData(_form);
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