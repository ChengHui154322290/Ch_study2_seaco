$(function() {
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				initManager();
				initUserInfoList();
			}else{
				window.location.href="index.html";
			}
		}
	});
	
	if(!isWeiXin()){
		$("header").show();
		$(".manager_info").addClass("manager_wx");
	}
});

function initManager(){
	$('.managermobile').text(xigou.getLocalStorage('show_name'));
}
function initUserInfoList(){
	var params = {
			'token': xigou.getToken()
	};
	xigou.redeemManager.userlist({
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
							initHtml(response.data);
							break;
						default:
							$.tips({
				                content:response.msg || "获取员工列表失败",
				                stayTime:2000,
				                type:"warn"
				            });
							break;
					}
				}
			} else {
				$('.add').hide();
				$.tips({
	                content:'请求失败，详见' + response,
	                stayTime:2000,
	                type:"warn"
	            })
			}
		}
	});
}

function initHtml(data){
	length = data.length,   //订单总数量
	html = [];

	//DOM
	for(var i=0; i<length; i++){
		html.push('<li class="child_num">');
		html.push('<span class="list"></span>');
		html.push(data[i].mobile);
		html.push('<span class="delete deleteuser" userid="'+data[i].fastUserId+'"><img src="img/store/delete.png"></span>');
		html.push('</li>');	
	}
	if (html.length == 0) {
		$('.nothing').show();
		$('.all_child').hide();
	} else {
		$('.nothing').hide();
		$('.all_child').show();
		$('.userinfolist').empty();
		$('.userinfolist').append(html.join(''));
	}
	deleteuser();
}
function deleteuser(){
	$('.deleteuser')[xigou.events.click](function(e){
		e.stopPropagation();
		var userid = $(this).attr('userid');
		var params = {
				'token':xigou.getToken(),
				'userid':userid
			}
		setTimeout(function(){
			var dia=$.dialog({
				title:'',
				content:"您确认删除员工信息？",
				button:["取消","确认"]
			});
			dia.on("dialog:action",function(e){
				if(e.index == 1)
				{
					xigou.redeemManager.userdelete({
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
											$.tips({
								                content:'删除员工成功',
								                stayTime:2000,
								                type:"warn"
								            });
											initUserInfoList();
											break;
										default:
											$.dialog({
										        title:'',
										        content:response.msg || "删除失败",
										        button:["确认"]
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
				}
			});
		});
	});
}