var page = 1;
var totalpagecount=1;
var codeInterval;
var statusInterval;
var num_status  = xigou.getLocalStorage("num");
$(function() {
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				pointinfolist();  // 积分详情
			}else{
				window.location.href="index.html";
			}
		}
	});

 	xigou.switches.swt({
		callback:function (data,status) {
			var show = true;
			if(status ==xigou.dictionary.success && data.code =="0" ){
				if(data.data !=null){
					var ar = data.data;
					for(var i = 0;i<ar.length;i++){
						if(ar[i].code =="sgp" && ar[i].value=="0"){
							show = false;break;
						}
					}
				}

			//hide pay button
			}
			if(show) {
				$(".xg_pay").show();
			}else {
				$(".xg_pay").hide();
			}
		}
	})

	$('.pointdetaillist').dropload({
		scrollArea : window,
		loadDownFn : function(me){
			me.resetload();
			pointinfolist(page++,me);
		}
	});
	
	var payleft = $(window).width()/2 - 80;
	var coinshowleft = $(window).width()/2 - 18;
	var sealeft = $(window).width()/2 - 11;

	$(".xg_coinshow").css({
		"left":coinshowleft + "px"
	})
	$(".xg_pay").css({
		"left":payleft + "px"
	})
	$(".seagoor").css({
		"left":sealeft + "px"
	})
	if(isWeiXin()){
		$('.cart-body').hide();
		$(".header_wx").show();
		$("title").html("西客币");
	}

	
	$('.xg_pay').bind("touchstart",function(event){
		$(".xg_pay").addClass("xg_pay_change").removeClass("xg_pay_change1");
	  });
	bindpay();



	$(".go_Back").click(function(){
		$(".coin_pay").addClass("out");
		$(".coin_pay").hide();
		$('.xg_pay').show();
		$(".content").show();
		$(".cart-body").show();
		/*$(".pay_content").show();
		$(".pay_content_code").hide();*/
		$("body").css({
			"overflow-y":"visible"
		})
		clearIntervals();
		bindpay();

	})

	
	$('.close').bind("touchstart",function(event){
		$(".close > img").addClass("close_change").removeClass("close_change1");
		clearIntervals();
		bindpay();
	});
	$('.close').bind("touchend",function(event){		
		$(".close > img").addClass("close_change1").removeClass("close_change");
		$(".coin_pay").addClass("out");
		$(".coin_pay").hide();
		$('.xg_pay').show();
		$(".content").show();
		$(".cart-body").show();
		/*$(".pay_content").show();
		$(".pay_content_code").hide();*/
		$("body").css({
			"overflow-y":"visible"
		})
	  });
	
	$('.btn').bind("touchstart",function(event){
		$(".btn > img").addClass("btn_change").removeClass("btn_change1");
	  });
	$('.btn').bind("touchend",function(event){
		$(".btn > img").addClass("btn_change1").removeClass("btn_change");
		$(".pay_content").hide();
		$(".pay_content_code").show();
		 paycodegen();

		xigou.setLocalStorage("seagoor_pay_sign",'1234');
	});
});

function bindpay() {
	$('.xg_pay').bind("touchend",function(event){
		$('.xg_pay').unbind("touchend");
		var sign  = xigou.getLocalStorage("seagoor_pay_sign");

		if(sign !=null && sign !="" ){
			$(".pay_content").hide();
			$(".pay_content_code").show();
			paycodegen();
		}else{

		}
		$('.xg_pay').hide();
		$(".content").hide();
		$(".cart-body").hide();
		$(".coin_pay").show();
		$(".coin_pay").removeClass("out");
		$(".xg_pay").addClass("xg_pay_change1").removeClass("xg_pay_change");
	});
}

function paycodegen(){
	getpaycode();
	codeInterval = setInterval("getpaycode()",60000);
	statusInterval = setInterval("querypaystatus()",3000);
}

function clearIntervals(
) {
	clearInterval(codeInterval);
	clearInterval(statusInterval);
}

function getpaycode(){
	var params = {
		'token': xigou.getToken(),

	};
	xigou.seagoorpay.code({
		showLoading: false,
		requestBody: params,
		callback:function(codes,status){
			if(status == xigou.dictionary.success){
				if(codes.code == "0"){
					$(".bar_code_nub").html("<label   "+"'>"+codes.data.code+"</labe>" );
					$(".qr_code").html("<img width='100%'　 src='data:image/png;base64,"+codes.data.qrcode+"'/>" );
					$(".bar_code").html("<img width='100%'　 src='data:image/png;base64,"+codes.data.barcode+"'/>" );
				}else {
					$.tips({
						content:'获取支付码失败!',
						stayTime:2000,
						type:"info"
					})
				}

			}else{
				$.tips({
					content:'系统异常!',
					stayTime:2000,
					type:"info"
				})
			}
		}
	});
}



function querypaystatus(){
	var code = $(".bar_code_nub").find("label").text();
	var params = {
		'token': xigou.getToken(),
		'code' :code,


	};
	xigou.seagoorpay.querypaystatus({
		showLoading: false,
		requestBody: params,
		callback:function(res,status){
			if(status == xigou.dictionary.success) {
				if (res.code == "0") {
					if (res.data.status != "1" && res.data.status != "-1") {
						xigou.setSessionStorage(code,JSON.stringify(res));
						window.location.href = "transaction_details.html?code="+code;
					}
				}
			}
		}
	});
}



//用户-积分详情
function pointinfolist(){
	var params = {
		'token': xigou.getToken(),
		'curpage': page
	};
	if(totalpagecount==0 || page>(totalpagecount+1)){
		return;
	}
	xigou.activeUser.pointlist({
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
							setHtml(response);
							break;
						default:
							$.tips({
	                            content:response.msg||"获取西客币信息失败",
	                            stayTime:2000,
	                            type:"warn"
	                        });
							break;
					}
				}
			} else {
				$.tips({
					content:response.msg||"获取西客币信息失败",
	                stayTime:2000,
	                type:"warn"
	            });
			}
		}
	});
}; 

function setHtml(response){
	var data = response.data;
	var pointDetailPage =data.pointDetailPage;
	if(pointDetailPage!=null){
		var list = pointDetailPage.list;	
		if(list!=null && list.length>=0){
			totalpagecount = data.pointDetailPage.totalpagecount;
			if(totalpagecount>0 || $(".pointdetaillist .product-item").length>0){
				if(list.length>0){
					$('.div_xgcoin_nmb').text(data.count);
					$("div.cartnull").remove();
					var html = [];
					for(var i=0;i<list.length;i++){
						html.push('<div class="product-item">');
						html.push('<div class="product-left-name">'+list[i].title+'</div>');
						html.push('<div><span class="product-left-nmb">'+list[i].bizTypeName+"&nbsp;&nbsp;"+list[i].bizId+'</span><span class="'+(list[i].pointTypeName == '+'?'xg_plus':'xg_minus')+'">'+list[i].pointTypeName+list[i].point+'</span></div>');
						html.push('<div class="product-left-date">'+list[i].createTime+'</div>');
						html.push('</div>');
					}
					$('.pointdetaillist').append(html.join(''));
					$('.dropload-down').hide();
				}
			}else{
				$(".pointdetaillist").empty();
				var htmlData = [];
				htmlData.push('<div class="cartnull">');
				htmlData.push('<img src="img/xgcoin2.png" style="width: 27%;margin: 0 auto;display: block;margin-top: 70px;" />');
				htmlData.push('<p  style="font-size:14px;text-align: center;color: #ccc;margin-top: 25px;" >还没有使用过西客币哟~</p>');
				htmlData.push('</div>');
				$('.pointdetaillist').append(htmlData.join(''));
			}
		}			
	}
}
