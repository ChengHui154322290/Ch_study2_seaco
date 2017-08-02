var totalpagecount=1;
var type=xigou.getQueryString("type");
if(type == null)
{
	window.location.href="../logon.html";
}

$(function(){
	var page = 1;
	/*xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				$('.contents1').dropload({
			    	scrollArea : window,
			    	loadDownFn : function(me){
			    		getWithdrawDetail(page, me);
			            me.resetload();
			            if(1 == page)
			            {
			                $(".dropload-refresh").hide();
			            }
			    	}
				});
			}else{
				window.location.href="../login.html";
			}
		}
	});*/
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				getWithdrawDetail();  
			}else{
				window.location.href="../login.html";
			}
		}
	});
	$('.contents1').dropload({
		scrollArea : window,
		loadDownFn : function(me){
			me.resetload();
			getWithdrawDetail(page++,me);
		}
	});
	
	/*Initkf();*/
});

var getWithdrawDetail_max = "N";
var code =1;
/*
 * 获取提现详情
 */
function getWithdrawDetail(page, me){
	if(getWithdrawDetail_max == "Y"){
		me.disable("down",false);
		$('.ui-refresh-down>span:first-child').removeClass();
		return false;
	}
	if(getWithdrawDetail_max != "Y"){
		var token = xigou.getToken();
		var params = {
			'token' : token,
			'curpage' : page,
			'type' : type
		};
		xigou.withdrawFunc.withdrawalsRecord({
			requestBody: params,
			callback: function(response, status) { //回调函数
				if (status == xigou.dictionary.success) {
					if (null == response) {
						$.tips({
							content : xigou.dictionary.chineseTips.server.value_is_null,
							stayTime : 2000,
							type : "warn"
						});
					} else {
						switch (response.code) {
							case "0":
								withdrawalsRecordHtml(response);
								break;
						}
					}
				}
			}
			
		});
	}
}
	
function withdrawalsRecordHtml(json) {
	
	var data = json.data;
	var rows = data.rows;
	if(data!=null){
		if(rows != null && rows.length>=0){
			total = data.total;
			if(total > 0 || $(".contents1 .record").length>0){
				if(rows.length>0){
					var htmlData = [];
					for(var i=0;i<rows.length;i++){
						htmlData.push('<div class="record">');
						htmlData.push('	<div class="time">' + rows[i].withdrawTime + '</div>');
						htmlData.push('	<div class="record_details">');
						htmlData.push('		<div class="record_state">');
						htmlData.push('			<span class="money"><span class="yuan">' + rows[i].withdrawAmount + '</span>元</span>');
						htmlData.push('			<span class="state">' + rows[i].withdrawStatus + '</span>');
						htmlData.push('		</div>');
						if(rows[i].remark == undefined){
							$(".additional").hide();
						}else{
							htmlData.push('		<div class="additional">' + rows[i].remark + '</div>');
						}
						htmlData.push('	</div>');
						htmlData.push('</div>');
					}
					$('.contents1').append(htmlData.join(''));
					$('.dropload-down').hide();
				}
				$(".service").show();
			}else{
					$(".contents1").empty();
					var html = [];
					html.push('<div class="withdrawalsrecord_empty"><div class="empty-tip">还木有提现记录哟~</div></div>');
					$('.contents1').append(html.join(''));
				
			}
		}
	}
}
	/*var htmlData = [];
	for(var i=0;i<rows.length;i++){
		htmlData.push('<div class="record">');
		htmlData.push('	<div class="time">' + rows[i].withdrawTime + '</div>');
		htmlData.push('	<div class="record_details">');
		htmlData.push('		<div class="record_state">');
		htmlData.push('			<span class="money"><span class="yuan">' + rows[i].withdrawAmount + '</span>元</span>');
		htmlData.push('			<span class="state">' + rows[i].withdrawStatus + '</span>');
		htmlData.push('		</div>');
		if(rows[i].remark == undefined){
			$(".additional").hide();
		}else{
			htmlData.push('		<div class="additional">' + rows[i].remark + '</div>');
		}
		htmlData.push('	</div>');
		htmlData.push('</div>');
		if(i == rows.length-1){
			htmlData.push('<div class = "service">没有更多数据了哟~</div>');
		}
	}
	
	if(json.data.pagesize == "" || json.data.pagesize == 0 || json.data.pagesize == '0' || json.data.curpage == json.data.totalcount) {
    	deliveryorder_max = "Y";
	}
	
	if (rows.length > 0 ) {
		$(".contents1").empty();
		var html = [];
		html.push('<div class="withdrawalsrecord_empty"><div class="empty-tip">还木有提现记录哟~</div></div>');
		$('.contents1').append(html.join(''));
	} else {
		$('.contents1').append(htmlData.join(''));
	}
}	*/

