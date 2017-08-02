/**
 * 
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-12 17:16:47
 * @version Ver 1.0.0
 */

     
xigou.setLocalStorage("my_address", {}, true);

$(function () {
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status != xigou.dictionary.success){
				window.location.href = "logon.html";
			}
		}
	});
});

$(function() {
	lists();
	$(document).on(xigou.events.click,"a.delete",function(){
		var _n=$(this).attr("n");
		var dia=$.dialog({
			title:'',
			content:"确定删除此地址?",
			button:["取消","确认"]
		});

		dia.on("dialog:action",function(e){
			if(1 == e.index)
			{
				del(_n);
			}
		});
	});
	$(document).on("change","input.recvadress-set-icon",function(){
		edit($(this).attr("aid"));
	});
	$("#back").on(xigou.events.click,function(){
		window.history.back()
	});
});

var addressData;//地址列表

//用户-收货地址-列表
function lists(){
	var params = {
		'token': xigou.getToken()
	};

	xigou.activeUser.lists({
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
							addressData=response.data;
							if(typeof(addressData) != "undefined"){
								if(addressData.length==5)
								{
									$('.add').attr('href','javascript:void(0)');
									$('.add')[xigou.events.click](function(){
										$.tips({
							                content:'收货地址已满，无法添加',
							                stayTime:2000,
							                type:"warn"
							            })
									});
								}
							}
							setHtml(response);
							break;
						default:
							$.tips({
						        content:response.msg||'获取收货地址失败',
						        stayTime:2000,
						        type:"warn"
						    })
							// xigou.alert(response.rescode.info||"获取收货地址失败");
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
};


//用户-收货地址-删除
function del(n){
	var params = {
		'token': xigou.getToken(),
		'aid': n
	};

	xigou.activeUser.del({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				if (null == response) {
					$.tips({
						content:xigou.dictionary.chineseTips.server.value_is_null,
						stayTime:2000,
						type:"warn"
					});
				} else {
					switch (response.code) {
						case "0":
							$.tips({
								content:response.msg||"删除成功",
								stayTime:2000,
								type:"warn"
							});
							history.go(0);
							break;
						default:
							$.tips({
								content:response.msg||"删除失败",
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
};

//用户-收货地址-编辑
function edit(n){
	var params =getAddData(n);
	if(params){
		params.token=xigou.getToken(),
		params.isdefault="1";//“0”不是默认地址 “1”默认地址

		xigou.activeUser.edit({
			requestBody: params,
			callback: function(response, status) { //回调函数
				if (status == xigou.dictionary.success) {
					if (null == response) {
						$.tips({
							content:xigou.dictionary.chineseTips.server.value_is_null,
							stayTime:2000,
							type:"warn"
						});
					} else {
						switch (response.code) {
							case "0":
								$.tips({
									content:response.msg||"设置成功",
									stayTime:2000,
									type:"warn"
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
									content:response.msg||"设置失败",
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
					// xigou.alert('请求失败，详见' + response);
				}
			}
		});
	}
};


function setHtml(data){

	var template='<li class="ui-border-t clearfix" aid="#aid#">'
		            +'<div class="ui-list-info recvaddress-item" aid="#num#">'
		                +'<div class="recvaddress-item-top" style = "font-size: 16px;">'
		                    +'<span>#name#</span>'
		                    +'<b>#phone#</b>'
		                +'</div>'
						+'<div class="recvaddress-item-idnum">'
						+'<div class="#certified#"></div>'
						+'<div style="display: inline-block;">身份证号码：#identityCard#</div>'
						+'</div>'
		                +'<div class="recvaddress-item-top">'
		                    +'<i>#address#</i>'
		                +'</div>'
		                +'<div class="recvaddress-item-bottom">'
		                    +'<div class="recvadress-set">'
		                    	+'<p>'
					                +'<label class="ui-checkbox-s">'
					                    +'<input class="recvadress-set-icon" aid="#num#" type="radio" name="checkbox" #default#>'
					                +'</label>'
					                +'设为默认地址'
					            +'</p>'
		                    +'</div>'
		                    +'<div class="recvadress-edit">'
								+'<a n="#num#" class="edit">编辑</a>'
								+'<a href="javascript:void(0);" n="#num#" class="delete">删除</a>&nbsp;&nbsp;'
		                    +'</div>'
		                +'</div>'
		            +'</div>'
		        +'</li>',
        htmls=[];

	if(data&&data.data){
		for(var d in data.data){
			var _d=data.data[d];
			var address=_d.fullinfo;
			if(_d.identityCard==undefined){
				_d.identityCard="";
			}
        	htmls.push(
        		template.replace("#name#",_d.name)
        				.replace("#phone#",_d.tel)
        				.replace("#address#",address)
        				.replace("#aid#",_d.aid)
        				.replace("#default#",_d.isdefault=="1"?'checked=""':"")
        				.replace("#identityCard#",_d.identityCard)
						.replace(/#num#+/g,_d.aid)
						.replace("#certified#",_d.iscertificated=="1"?'certified':'notcertified')
    		);
		}
	}
	if(htmls.length<1){
		$(".recvaddress-none").show();
	}else{
    	$("#recvaddress-list-id").html(htmls.join(""));
    	$(".recvaddress-none").hide();
    	bindEdit();
    }
    setSetRecvaddress(data.data);
}

function getAddData(num){
	var result=null;
	for(var d in addressData){
		if(addressData[d].aid==num){
			result=addressData[d];
			break;
		}
	}
	return result;
}

//绑定收货地址编辑处理
function bindEdit(){
	$(".recvaddress-data a.edit")[xigou.events.click](function() {
		var num=$(this).attr("n");
        xigou.setSessionStorage("edit_address",getAddData(num),true);
        xigou.removelocalStorage("my_address");
		window.location.href="editaddress.html";
	});
}

// 设定是否为提交订单页面选择地址
function setSetRecvaddress(data){
	var selectType = xigou.getQueryString("selectType");
	if (1 == selectType) {
		$(".recvaddress-item-bottom").hide();
		//$(".recvaddress-add").hide();
		$(".recvaddress-item-idnum, .recvaddress-item-top")[xigou.events.click](function(event) {
			var orderItem = $(event.target).parents('.recvaddress-item');
			var aid = $(orderItem).attr("aid");
			//tager = orderItem.parent().parent();
			//aid = $(event.target).attr("aid");

			if (aid != -1) {
				for (var i = 0; i < data.length; i++) {
						if (data[i].aid == aid){
							xigou.setSessionStorage("clearing_select_address", data[i], true);
							history.go(-1);
							return;
						}
					};	
			}
		});
		$('.goBack').attr('href', 'javascript:history.go(-1);');
	}
}