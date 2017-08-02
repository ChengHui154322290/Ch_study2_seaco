$(function() {
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				initAddBtn();
			}else{
				window.location.href="index.html";
			}
		}
	});
	
	if(!isWeiXin()){
		$("header").show();
	}
});

/**新增*/
function initAddBtn(){
	var btn = $('.btn');
	btn[xigou.events.click](function(){
		var usermobile = $('.usermobile').val();
		if($.trim(usermobile)==''){
			$.tips({
                content: '请输入员工手机号',
                stayTime:2000,
                type:"warn"
            });
			return false;
		}
		if(!/^\d{11}$/.test(usermobile)){
			$.tips({
                content: '请输入正确手机号',
                stayTime:2000,
                type:"warn"
            });
			return false;
		}
		var params = {
				'token': xigou.getToken(),
				'userMoible':usermobile
		};
		xigou.redeemManager.usersave({
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
								var dia =$.dialog({
							        title:'',
							        content:'添加店员成功',
							        button:["确认"]
							    });
								dia.on("dialog:action",function(e){
									if(e.index == 1)
									{
										window.location.href="store_manager.html";
									}
								});
								break;
							default:
								$.tips({
					                content: response.msg || "添加失败",
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
				}
			}
		});
	});
}