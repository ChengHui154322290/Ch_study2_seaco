function showCodeImg(telid, type, $btn){
    var tel = $('#'+telid).val();
    if ($btn.hasClass('disable')) {
        return;
    }

    initValiData();

    if (!xigou.valiformdata.check(null, valiDataCallBack)) {
        return;
    }

    if ($(".div_img_code").size() <= 0) {
        var htmlData = [];
        htmlData.push("<div class='img_code_bg' style='height:" + $(window).height() + "px'></div>");
        htmlData.push('<div class="div_img_code">');
        htmlData.push('<div class="div_code_title">验证码</div>');
        htmlData.push('<div class="div_code_info">');
        htmlData.push('<div class="div_code_img">');
        htmlData.push('<img src="' + xigou.activeHost +  'captcha/graphic.htm?tel=' + tel + '&type=' + type + '">');
        htmlData.push('</div>');
        htmlData.push('<div class="div_code_input">');
        htmlData.push('<input class="input_img_code" type="text" placeholder="请输入验证码">')
        htmlData.push('</div>');
        htmlData.push('<div class="div_code_btn">');
        htmlData.push('<div class="div_code_btn1">取消</div>');
        htmlData.push('<div class="div_code_btn2">确认</div>');
        htmlData.push('</div>');
        htmlData.push('</div>');
        htmlData.push('</div>');
        $("body").append(htmlData.join(''));
    }

    // 刷新验证码
    $('.div_img_code .div_code_img').click(function(){
        $('.div_img_code .div_code_img img').attr('src', xigou.activeHost +  'captcha/graphic.htm?tel=' + tel + '&type=' + type);
    });

    // 取消
    $('.div_img_code .div_code_btn1').click(function(){
        destory();
    });

    // 获取验证码
    $('.div_img_code .div_code_btn2').click(function(){
        var params = {
    		'tel': tel,
    		'type': type,
            'graphic' : $('.input_img_code').val()
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
    				} else {
    					var stauts = response.code;
    					switch (stauts) {
    						case "0":
                                destory();
    							setCodeTime(60);
    							break;
    						default:
    							$.tips({
    					            content:response.msg||"获取验证码失败",
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
    	};
    	xigou.activeUser.getTheCode(dp);
    });


  function destory(){
    $(".img_code_bg").hide().remove();
    $(".div_img_code").hide().remove();
  }

  function setCodeTime(time){
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

    function initValiData() {
        var _form = {
            methods: {
            },
            errors: {
            }
        };
        _form.methods[telid] = { required: "required", mobile:true};
        _form.errors[telid] = { required: "请输入手机号", mobile:"手机号码不正确，请重新输入"};
        return xigou.valiformdata.initValiData(_form);
    }

    function valiDataCallBack(message, id, mname) {
        $.tips({
            content:message,
            stayTime:2000,
            type:"warn"
        })
        $("#"+id).focus();
    }
}