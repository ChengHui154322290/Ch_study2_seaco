var type=xigou.getQueryString("type");
if(type == null)
{
	window.location.href="../logon.html";
}
$(function() {
	
	if(isWeiXin()){
		$(".account-body").hide();
		$(".info-list").css({
			"margin-top":"0"
		})
	}
	
	var page = 1;
	if(type == 0 || type == '0'){
		$(".goBack").attr("href", "dsscouponhome.html");
	}else if(type == 1 || type == '1'){
		$(".goBack").attr("href", "dsshome.html");
	}else if(type == 2 || type == '2'){
		$(".goBack").attr("href", "dssScancodeHome.html");
	}else{
		$(".goBack").attr("href", "../home.html");
	}
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				$('.info-list').dropload({
			    	scrollArea : window,
			    	loadDownFn : function(me){
						allbillinfo(page,me);
			            me.resetload();
			            if(1 == page)
			            {
			                $(".dropload-refresh").hide();
			            }
			            page++;
			    	}
				});
				var nDefaulePage = xigou.getQueryString("defaule");
				if (nDefaulePage)
				{
					setdefaulesel(nDefaulePage);
				}
			}else{
				window.location.href="../logon.html";
			}	
		}
	});
});


var bills_max = "N";

function allbillinfo(page, me){

	if(bills_max == "Y"){
		me.disable("down",false);
		$('.ui-refresh-down>span:first-child').removeClass();
		return false;
	}
	if(bills_max != "Y"){
		var token = xigou.getToken();
		var params = {
			'type' : type,
			'token': token,
			'curpage' : page
		};

		xigou.promoterFunc.queryBillInfo({
			requestBody : params,
			callback : function(response, status) { // 回调函数
			if (status == xigou.dictionary.success) {
				if (null == response) {
					$.tips({
						content : xigou.dictionary.chineseTips.server.value_is_null,
						stayTime : 2000,
						type : "warn"
					});
				} else {
					var code = response.code;
					if(0 == code){
						setHtmlView(response, page);
						if(response.data.curpage >= response.data.totalpagecount){
							me.lock();
							$(".dropload-down").hide();
						}
					}else{
						me.lock();
						$(".dropload-down").hide();
					}
				}
			}
		}
	});
}
};


function setHtmlView(json, page) {
	var bussinessType = ["订单提佣","新人注册","退款","提现"];
	var accountDetailTpl = '<div class="info-item">' + '<img class="info-icon" src="img/accoutinfo-icon.png" />' + 
							'<div class="info-content">' + 
								'<div class="info-content-top">' + 
									'<span class="info-content-text"><%=bussinesstype%></span>' +
									'<span class="info-content-value"><%=amount%></span>' +
								'</div>' +
								'<div class="info-content-bottom">' + 
									'<span class="info-content-text"><%=accounttime%></span>' +
									'<span class="info-content-value">余额:¥<%=surplusamount%></span>' + 
								'</div>' +
							'</div>' + '</div>';
    var html = [];
    for (var j=0,len;j<json.data.list.length; j++) {
		var obj = json.data.list[j];
		if(obj.bussinesstype == 2){
			obj.bussinesstype = obj.refereeName ?obj.refereeName:obj.refereeNickName;
		}else{
			obj.bussinesstype = bussinessType[--obj.bussinesstype];
		}
		var detailItemStr = $.tpl(accountDetailTpl,obj);
		html.push(detailItemStr);
    }

    if(json.data.pagesize == "" || json.data.pagesize == 0 || json.data.pagesize == '0' || json.data.curpage == json.data.totalcount) {
    	bills_max = "Y";
	}
    
    if (page == 1 && html.length == 0) {
		$("#accountInfoList").empty();
		$("#accountInfoList").hide();
		$("#infoNull").show();
	} else {
		$("#accountInfoList").show();
		$("#infoNull").hide();
		$('#accountInfoList').append(html.join(''));
	}
}

//切换当前选中List
function setdefaulesel(npage)
{
	var scroller = $('.ui-tab-content')[0];
	var nav = $('.ui-tab-nav')[0];

	$(scroller.children[0]).removeClass('current');
	$(nav.children[0]).removeClass('current');
	$(scroller.children[npage]).addClass('current');
	$(nav.children[npage]).addClass('current');
	var width = -1 * scroller.clientWidth * npage / 3;
	scroller.style.transform = "translate(" + width + "px, 0px)";
	$(scroller.children)[0].height = "0px";
}