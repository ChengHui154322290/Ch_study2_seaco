$(function() {
	if(!isWeiXin()){
		initgeetest();
	}
	
	modify();
	if(isWeiXin()){
		$(".ui-header").hide();
		$(".input_info_list").css({
			"margin-top":"0"
		})
		$("title").html("修改密码");
//		$("#embed-captcha").hide();
		$(".hide").hide();
    	
		$(".forgetpwd-btn").addClass("wx");
		$(".forgetpwd-btn").removeClass("h5");
		
		$("#hide-div").show();
		getCode();
	}else{
		$(".forgetpwd-btn").addClass("h5");
		$(".forgetpwd-btn").removeClass("wx");
		
	}
	
	//赋值即将修改的手机号码
	var userinfo = xigou.getSessionStorage("userinfo");
	$(".telnum").html(eval("(" + userinfo + ")").telephone);
    
    
    //密码消除
//    if($('#input-pwd')[0].value != ""){
//    	$('.div_clear_input2').show();
//    }
//    $('#input-pwd').keyup(function(e) {
//        var b = $(this).val();
//        if (b && b.length > 0) {
//            $('.div_clear_input2').show();
//        }
//        else {
//            $('.div_clear_input2').hide();
//        }
//    });
//    $('.div_clear_input2')[xigou.events.click](function() {
//        $('.div_clear_input2').hide();
//        $('#input-pwd')[0].value = "";
//    })

});


var handlerEmbed = function (captchaObj) {
	$('.h5,#achieve')[xigou.events.click](function () {
		var validate = captchaObj.getValidate();
		if (!validate) {
			$.tips({
				content: "请先完成验证",
				stayTime: 2000,
				type: "warn"
			});
		} else {
			var params = {
				'geetest_challenge': $("[name=geetest_challenge]").val(),
				'geetest_validate': $("[name=geetest_validate]").val(),
				'geetest_seccode': $("[name=geetest_seccode]").val(),
				'tel': $("#telnum").html(),
				'randid':$("#randid").val(),
				'type' : "2"
			};
			var dp = {
				requestBody: params,
				callback: function (response, status) { //回调函数
					if (status == xigou.dictionary.success) {
						if (null == response) {
							$.tips({
								content: xigou.dictionary.chineseTips.server.value_is_null,
								stayTime: 2000,
								type: "warn"
							})
						} else {
							if (response.code == "0") {
								$(".modifypwd-modifypwd").show();
								$("#hide-div").show();
								$(".input_info1").hide();
								$.tips({
									content: response.msg,
									stayTime: 2000,
									type: "warn"
								})
							} else {
								$.tips({
									content: response.msg || "获取验证码失败",
									stayTime: 2000,
									type: "warn"
								});
							}

						}
					} else {
						xigou.alert('请求失败，详见' + response);
					}
				}
			};
			xigou.activeUser.sendGeeCode(dp);

		}
	});
	// 将验证码加到id为captcha的元素里
	captchaObj.appendTo("#embed-captcha");
	captchaObj.onReady(function () {
	});
	// 更多接口参考：http://www.geetest.com/install/sections/idx-client-sdk.html
};

var allowGetTheCode=true;
var uuid="";//极验
var fanli = xigou.getQueryString("fanli");

function getImgCode(){
	$('input#name').change(function(){
		if (!xigou.valiformdata.checkSingleNode($("#name"), valiDataCallBack)) {
			return;
		}
		$('#codeImg').attr("src","../../mobile/init/captchaimage.htm?telephone="+$('input#name').val()+"&typeimage=1&time="+new Date().getTime());

	});
	$('#codeImg').click(function(){
		if (!xigou.valiformdata.checkSingleNode($("#name"), valiDataCallBack)) {
			return;
		}
		$('#codeImg').attr("src","../../mobile/init/captchaimage.htm?telephone="+$('input#name').val()+"&typeimage=1&time="+new Date().getTime());
		$('#imgcode').val('');
	});
}

function setCodeTime(time){
	var getTheCode=$('.h5');
	getTheCode.text(time);
	var _flag=setInterval(function(){
		if(time<=0){
			clearInterval(_flag);
			getTheCode.text("获取验证码");
			allowGetTheCode=true;
			return;
		}
		getTheCode.text(--time);
	},1000);

}



//注册
function modify() {

	//获取页面高度，设置body高度，解决输入法弹出把logo推上来问题
	var bh=$("body").height(),
		wh=$(window).height();
		$(".logo").css("top",(Math.max(bh,wh))*0.92-42+"px");
		
	$('.modifypwd-modifypwd')[xigou.events.click](function() {
		
		var params = {
			'loginname': $("#telnum").html(),
			'captcha': $("#input-code").val(),
			'pwd': $("#input-pwd").val(),
		};
		
		
		var dp = {
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
						var stauts = response.code;
						xigou.debugPrint("修改成功返回的code值：" + stauts);
						switch (stauts) {
							case "0":
								var loginjump_url = xigou.getSessionStorage("loginjump_url");
								xigou.setLocalStorage("login_name", $("#telnum").html());
								var back = {
									'token': response.data.token,
									'telephone': response.data.tel,
									'name': response.data.name
								};

								var dia=$.dialog({
                                    title:'',
                                    content:response.msg||"修改成功",
                                    button:["确认"]
                                });

                                dia.on("dialog:action",function(e){
                                    console.log(e.index);
                                    xigou.removeSessionStorage('realname');
									xigou.removeSessionStorage('realnum');
									xigou.setSessionStorage("userinfo", back, true);
									xigou.setLocalStorage("login_name", $("#telnum").html());
									xigou.setLocalStorage("show_name", response.data.tel || response.data.name);
									xigou.setLocalStorage("token", response.data.token);
									window.location.href = "home.html";
                                });
								break;
							case "-2":
								$.tips({
									content:response.msg||"验证码错误",
									stayTime:2000,
									type:"warn"
								})
								break;
							default:
								$.tips({
									content:response.msg||"修改失败",
									stayTime:2000,
									type:"warn"
								})
								break;
						}
					}
				} else {
					xigou.alert('请求失败，详见' + response);
				}
			}
		};
		xigou.activeUser.modifypassword(dp);
	});
};


function valiDataCallBack(message, id, mname) {
	$.tips({
		content:message,
		stayTime:2000,
		type:"warn"
	})
	$("#"+id).focus();
};

function initValiData() {
    var _form = {
        methods: {
            "code":{
                required: "required"
            },
            "pwd": {
                required: "required",
                password:true
            },
			"imgcode":{
				required: "required"
			}
        },
        errors: {
            "code":{
                required: "请输入验证码"
            },
            "pwd": {
                required: "请输入密码",
                password:"请输入6-30位密码，可以为数字、字母和下划线"
            },
			"imgcode":{
				required:"请输入验证码"
			}
        }
    };
    return xigou.valiformdata.initValiData(_form);
};

/**
 * 微信用户绑定手机号，发送手机验证码，不用图片校验
 */
function getCode(){
	$('.wx')[xigou.events.click](function () {
	    var params = {
			'tel': $("#telnum").text(),
	        'randid':$("#randid").val(),
	        'type' : "2"
	    };
	    var dp = {
	        requestBody: params,
	        callback: function (response, status) { //回调函数
				if (status == xigou.dictionary.success) {
					if (null == response) {
						$.tips({
							content: xigou.dictionary.chineseTips.server.value_is_null,
							stayTime: 2000,
							type: "warn"
						})
					} else {
						if (response.code == "0") {
							$(".modifypwd-modifypwd").show();
							$(".input_info1").hide();
							$.tips({
								content: response.msg,
								stayTime: 2000,
								type: "warn"
							})
						} else {
							$.tips({
								content: response.msg || "获取验证码失败",
								stayTime: 2000,
								type: "warn"
							});
						}

					}
				} else {
					xigou.alert('请求失败，详见' + response);
				}
	        }
	    };
	    xigou.activeUser.getCaptcha(dp);
	});
}