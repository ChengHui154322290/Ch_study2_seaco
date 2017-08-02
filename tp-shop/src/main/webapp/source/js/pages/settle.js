var UserToken = xigou.getToken();
var fmaidcsv = "";//满减活动编号CSV格式
$(function() {
	xigou.getLoginUserInfo({
		callback: function(userinfo, status) {
			if (status == xigou.dictionary.success) {
				var uuid = xigou.getSessionStorage("buy_now_uuid");
				if(!uuid && xigou.getQueryString("from") != "cart") {
					window.location.href = "orders.html";
					return;
				}
				var gid = xigou.getSessionStorage("gid");
				if(uuid !=""){
					$(".back").attr("href", xigou.getSessionStorage("buy_now_details_url"));
				}

				authprice(uuid);
				
				select_pay(uuid,gid);
				
				//选择收货地址
				$("#li_recvaddress_info")[xigou.events.click](function() {
					window.location.href = "recvaddress.html?selectType=1";
				});

				//选择优惠券
				var preUrl = xigou.getSessionStorage("buy_now_details_url");
				if (preUrl.split('tuan.html').length > 1) {
					$('#li_coupon_info').hide();
				}
				else {
					$("#coupon")[xigou.events.click](function() {
						window.location.href = "couponorder.html";
					});
				}


				// 认证
				$("#select_auth")[xigou.events.click](function() {
					window.location.href = "auth.html";
				});
				
				$(".idNumber")[xigou.events.click](function() {
					window.location.href = "realname.html?backpage=haitao_clearing.html";
				});
				
				var itemType= xigou.getSessionStorage("itemType");
				if(itemType=="2"){//服务类型   手机接收
					$(".ui-border-address").hide();
					$(".ui-border-tel").show();
					$("#telephone").val(xigou.getLocalStorage("login_name"));
					$(".total-freight").hide();
				}else{//地址
					$(".ui-border-tel").hide();
					$(".ui-border-address").show();
					$(".total-freight").show();
				}
				
			} else {
				window.location.href = "index.html";
			}
			xigou.removeSessionStorage("gid");
		}
	    
	});
	//OnSeletPhoto();
	bindUploadImg();
	$('.floor_right_switch')[xigou.events.click](function(){
		if($(this).attr('usedpointsign')=='false' && (parseFloat($('.usedpoint[usedpoint]').text()).toFixed(2)>0)){
			$(this).attr('usedpointsign',"true");
			xigou.setSessionStorage("usedpointsign",true);
			$(this).html('<img alt="switch" src="img/settle/put.png">');
			var orderTotalAmount = xigou.getSessionStorage("orderTotalAmount");
			var usedpointamount = xigou.getSessionStorage("usedpointamount");
			var totalamount = parseFloat(parseFloat(orderTotalAmount)-parseFloat(usedpointamount)).toFixed(2);
			$("#totalPayments").html(createPriceHTML(totalamount)); //应付总额
			$("#totalPayments2").html(createPriceHTML(totalamount)); //应付总额
		}else{
			$(this).attr('usedpointsign',"false");
			xigou.setSessionStorage("usedpointsign",false);
			$(this).html('<img alt="switch" src="img/settle/close.png">');
			
			var orderTotalAmount = xigou.getSessionStorage("orderTotalAmount");
			var usedpointamount = xigou.getSessionStorage("usedpointamount");
			var totalamount = orderTotalAmount;
			$("#totalPayments").html(createPriceHTML(totalamount)); //应付总额
			$("#totalPayments2").html(createPriceHTML(totalamount)); //应付总额
		}
	});
});

var addressnum;
var is_tax_ok = true;
var is_price_ok = true;
var coupon;
var redenvelopes;
var cid_list = [];
var needcertified = false;

//初始化页面数据
function initPageData(data) {
	//默认优惠券显示-¥0
	$("#couponnum").text("-¥0");
    $("#pointType").val(data.data.pointType);
	//初始化收货地址
	var address = xigou.getSessionStorage("clearing_select_address",true);
	if (address) {
		// var addresscode = address.addresscode;
		if (!address.identityCard) {
			address.identityCard = '';
		}
		$("#auth-num-input").val(address.identityCard);
		$("#submit_auth").addClass("onClick");
		var htmls = [];
		htmls.push('<div class="div_name_tel">');
		htmls.push('	<span class="div_name">' + address.name + '</span>');
		if (address.iscertificated == 0) {
			htmls.push('	<div class="div_not_certified"></div>');
			needcertified = true;
		}
		htmls.push('	<div class="div_tel">' + address.tel + '</div>');
		htmls.push('</div>');
		htmls.push('<div class="div_add">收货地址:' + address.fullinfo + '</div>');
		$('.settle-address-info').html(htmls.join(""));
		//if(address.identityCard){
			//var idCardhtmls = [];
			//idCardhtmls.push('<span>姓名：</span><span id="realname">'+address.name+'</span><br>');
			//idCardhtmls.push('<span>证件号：</span><span id="idNumber">'+address.identityCard+'</span>');
			//$('.settle-auth-info').html(idCardhtmls.join(""));
			//xigou.setSessionStorage("clearing_ht_idNumber", address.identityCard);
			//xigou.setSessionStorage("clearing_ht_realname", address.name);
		//}
		addressnum=address.aid;
		$("#zipcode").text(address.zipcode);
		$('#li_recvaddress_info').attr("aid", address.aid);
	}
	else
	{
		defaultAddress();
	}

	//初始化优惠券
	coupon = xigou.getSessionStorage("clearing_select_coupon", true);
	if (coupon.length > 0) {
		for(var i = 0;i<coupon.length;i++){
			cid_list.push(coupon[i].cid);//优惠券cid
		}
	}

	//初始化红包
	redenvelopes = xigou.getSessionStorage("clearing_select_redenvelopes", true);
	if (redenvelopes) {
		$("#redenvelopes").text(redenvelopes.title);
		$("#redenvelopnum").text("-¥" + redenvelopes.price);// 红包金额
	}

	//初始化商品信息
	if (data) {
		var template = '<li class="ui-border-t clearfix">' +
					'<div class="ui-list-img floor_product_img"><img class="settle-product-icon" src="#img#" specialid="#specialid#" productid="#productid#"></div>' +
					'<div class="floor_product_title item_info">' +
						'<div  class="cart_product_name">'+
							'<div class="item_price">#price#</div>'+
							'<div class="item_name">#name#</div>' +
						'</div>' +
						'<div class="item_count">#count#</div>' +
						'<div class="item_count_price">#countPrice#</div>' +
					'</div>' +
				'</li>',
			htmls = [];
		if (data.data.productinfo) {
			var productlist=[];
			var productinfo = data.data.productinfo;
			for(var j = 0,jlen = productinfo.length; j < jlen; j++){
					var pInfo = productinfo[j];
					htmls.push('<div class="floor_product"><div class="floor_product_tap"><span>'+pInfo.channel+'</span></div>');
					htmls.push('<ul class="ui-list ui-list-one ui-border-tb">');
					for(var n = 0; n < pInfo.products.length; n++){
						var pItem = pInfo.products[n],
							norm = "",
							countPrice = parseFloat(pItem.price) * parseFloat(pItem.count);
						
						productlist.push(pItem);
						
						htmls.push(
								template.replace("#img#", pItem.imgurl)
								.replace("#specialid#", pItem.tid)
								.replace("#productid#", pItem.sku)
								.replace("#name#", pItem.name)
								// .replace("#norm#", norm)
								.replace("#quantity#", pItem.count)
								.replace("#price#", "¥" + pItem.price)
								.replace("#count#", "×" + pItem.count)
								.replace("#countPrice#", "¥" + pItem.lineprice)
							);
						
					}
					htmls.push('</ul>');
					var itemType= xigou.getSessionStorage("itemType");
					if(itemType!=="2"){
						htmls.push('<div class="floor_freight"><ul class="ui-list ui-list-one">');
						htmls.push('<li id="price_li"><div class="ui-list-info"><h4 class="floor_left" style="font-size: 12px;color: #666666;">运费:</h4><span class="floor_right" style="font-size: 12px;color: #666666;">¥' + pInfo.freight + '</span></div></li>');
						if(parseInt(pInfo.taxes) > 50){
							//国内直发(GNZF)或海外直邮(HWZY)，无需计算进口税
							if (pInfo.isfreetax == '1') {
								if( pInfo.channelcode=='HWZY'){
									htmls.push('<li id="price_li"><div class="ui-list-info"><h4 class="floor_left" style="font-size: 12px;color: #666666;">税金:</h4><del class="floor_right" style="font-size: 12px;color: #666666;">¥' + pInfo.taxes + '</del></div></li></ul>');
								}else{
									htmls.push('<li id="price_li"><div class="ui-list-info"><h4 class="floor_left" style="font-size: 12px;color: #666666;">税金:</h4><del class="floor_right" style="font-size: 12px;color: #666666;">¥' + pInfo.taxes + '</del></div></li></ul>');
								}
							}
							else {
								if( pInfo.channelcode=='HWZY'){
									htmls.push('<li id="price_li"><div class="ui-list-info"><h4 class="floor_left" style="font-size: 12px;color: #666666;">税金:</h4><span class="floor_right" style="font-size: 12px;color: #666666;">¥' + pInfo.taxes + '</span></div></li></ul>');
								}else{
									htmls.push('<li id="price_li"><div class="ui-list-info"><h4 class="floor_left" style="font-size: 12px;color: #666666;">税金:</h4><span class="floor_right" style="font-size: 12px;color: #666666;">¥' + pInfo.taxes + '</span></div></li></ul>');
								}
							}
	
						}
						//进口税<=50，无需显示进口税
						else{
							if (pInfo.isfreetax == '1') {
								htmls.push('<li id="price_li"><div class="ui-list-info"><h4 class="floor_left" style="font-size: 12px;color: #666666;">税金:</h4><del class="floor_right" style="font-size: 12px;color: #666666;">¥' + pInfo.taxes + '</del></div></li></ul>');
							}
							else {
								htmls.push('<li id="price_li"><div class="ui-list-info"><h4 class="floor_left" style="font-size: 12px;color: #666666;">税金:</h4><span class="floor_right" style="font-size: 12px;color: #666666;">¥' + pInfo.taxes + '</span></div></li></ul>');
							}
						}
						//htmls.push('<div class="floor_con ui-list-info xiaoji"><h4 class="floor_left">小计:</h4><span class="floor_right">¥' + pInfo.price + '</span></div>');
						//订单金额超过1000元
						//if((pInfo.products.length > 1 || parseInt(pInfo.products[0].count) > 1) && parseInt(pInfo.price) >= 1000){
						//	htmls.push('<div class="rules">本订单金额超过海关限额¥1000，请选择部分商品结算<span class="ico_jian2"></span><span class="ico_jian"></span></div>');
						//	is_price_ok = false;
						//}
						//海外直邮(HWZY) 国内直发(GNZF) 无需计算验证进口税 计算订单金额
						if(pInfo.channelcode=='GNZF' || pInfo.channelcode=='HWZY'){
							is_tax_ok = true;
						}
						//单个保税区商品进口税>50，需要验证进口税
						else{
							//if((pInfo.products.length > 1 || parseInt(pInfo.products[0].count) > 1) && (parseInt(pInfo.taxes) > 50 && parseInt(pInfo.price) < 1000)){
							//	htmls.push('<div class="rules">本订单已经超过个人关税免税额度，请前去购物车选择部分商品结算<span class="ico_jian2"></span><span class="ico_jian"></span></div>');
							//	is_tax_ok = false;
							//}
						}
						//海外直邮显示提示语
						if(pInfo.channelcode=='HWZY'){
							htmls.push('<div class="rules">本单为海外直邮商品，税费由商家承担</div>');
						}
	
						htmls.push('</div></div>');
					}
					// htmls.push('</li>');
				// }
			}
			//选择优惠券和红包需要传商品数据
			xigou.setSessionStorage("clearing_products_data",productlist,true);
		}
		// $(".floor_other").before(htmls.join(""));
		$(".settle-list").html(htmls.join(""));
		
		//初始化积分
		$("#agio_nmb").text(data.data.totalpoint);
		var usedpointamount = parseFloat(data.data.usedpoint).toFixed(2);
		$("#agio_price").text(usedpointamount);
		var orderTotalAmount = data.data.price;
		if("true"==data.data.usedpointsign){
			$('.floor_right_switch[usedpointsign]').attr('usedpointsign','true').html('<img alt="switch" src="img/settle/put.png">');
			orderTotalAmount = parseFloat(parseFloat(orderTotalAmount)+parseFloat(data.data.usedpoint)).toFixed(2);
		}
		xigou.setSessionStorage('ordertotalprice',data.data.price);
		xigou.setSessionStorage('usedpointamount',usedpointamount);
		xigou.setSessionStorage('orderTotalAmount',orderTotalAmount);
		
		$("#product_count").before(htmls.join(""));
		$("#productPrice").html(createPriceHTML(data.data.itemsprice));
		$("#freight").html(createPriceHTML(data.data.freight)); //运费
		$("#fmadisprice").html("-" +createPriceHTML(data.data.disprice));//满减优惠
		$("#taxes").html(createPriceHTML(data.data.taxes));// 进口税
		$("#totalPayments").html(createPriceHTML(data.data.price)); //应付总额
		$("#totalPayments2").html(createPriceHTML(data.data.price)); //应付总额
		if(data.data.totalcoupon){

			$("#couponnum").text("-¥" + data.data.totalcoupon);//优惠券金额
		}

		if(parseFloat(data.data.totalcoupon)){
			$("#coupon").text("-¥" + data.data.totalcoupon);//优惠券金额
		}else{
			//优惠券显示张数
			showCouponNo();
		}

		
		//点击图片，进入商品详情页面
		$(".floor_product_img img")[xigou.events.click](function(){
			var $this = $(this);
			window.location.href = "item.htm?tid="+$this.attr("specialid")+"&sku="+$this.attr("productid");
		});

		// 实名认证
		authentication();
	}
};

//支付
function select_pay(uuid,gid) {
	$(".submit-btn").click(function() {
		//验证
		var result = checkOrder();

		//提交订单
		if(result) {
			//order();
			//海外直邮订单特殊处理;
			if(checkidphoto == 1){
				var p = {
							text:'<p style="line-height: 22px;">应海关审核要求，请确认已上传真实的身份证正反面照片</p>',
							callback:function(res){
								if(res=="ok"){
							        window.location.href="realname.html?backpage=haitao_clearing.html";
								}else {
									order(uuid);
								}
							},
							params:{
								closeBtn: true,
								buttons: {
									"确认": function() {
										this.close();
										this.destroy();
										$.isFunction(p.callback) && p.callback("cancel");
									},
									"上传": function() {
										this.close();
										this.destroy();
										$.isFunction(p.callback) && p.callback("ok");
									}
								}

							}
						}
			    xigou.confirm(p);
			}else {
				order(uuid,gid);
			}
		}

	});
};

//删除两边的空格
String.prototype.trim=function(){
    return this.replace(/(^\s*)|(\s*$)/g, "");
};

var checkOrder=function(){
	//地址
	if(!addressnum){
		$('.settle-address-info').html('<span>添加收货地址</span>');
		$.dialog({
        title:'',
        content:'请先添加收货地址!',
        button:["确认"]
    	});
		return false;
	}
	/**
	if (needcertified) {
		//$('#auth-name-input')[0].value = '';
		$('#auth-num-input')[0].value = '';
		$('.div_tip_bg').show();
		$('.div_tip').show();
		$.tips({
			content:'结算前必须先提交实名认证',
			stayTime:2000,
			type:"warn"
		});
		return;
	}

	//实名认证
	if($("#idNumber").text() ==null || $("#idNumber").text().trim() == '' || $("#realname").text() == null || $("#realname").text().trim() == ''){
		$('.settle-auth-info').html('<span class="add_auth">添加实名认证</span>');
		$.dialog({
        title:'',
        content:'结算前必须先提交实名认证',
        button:["确认"]
    	});
		return false;
	}
	//金额和进口税
	if(!is_price_ok){
		$.dialog({
        title:'',
        content:'本订单金额超过海关限额¥1000，请选择部分商品结算',
        button:["确认"]
    	});
		return false;
	}
	if(!is_tax_ok){
		$.dialog({
        title:'',
        content:'本订单已经超过个人关税免税额度，请选择部分商品结算',
        button:["确认"]
    	});
		return false;
	}

	var name = $(".div_name")[0].innerText;
	var realName = $("#realname")[0].innerText;
	if (name != realName) {
		$.dialog({
			title:'',
			content:'收货人姓名需要和实名认证姓名保持一致',
			button:["确认"]
		});
		return false;
	}
	*/
	return true;
};

//结算-提交订单
function order(uuid,gid) {
	
	var aid = $(".ui-border-t").attr('aid');
	var cid = $("#li_coupon_info").attr('cid');

	var params = {};

	var shopMobile = null;
	var dssUser = xigou.getSessionStorage("dssUser");
	var usedpointsign = xigou.getSessionStorage('usedpointsign');
	if (dssUser) {
		dssUser = JSON.parse(dssUser);
		if (dssUser.mobile) {
			shopMobile = dssUser.mobile;
		}
		else if (dssUser.shopmobile) {
			shopMobile = dssUser.shopmobile;
		}
	}
	
	if (isWeiXin()) {
		params = {
			'token': UserToken,
			'aid': aid,
			'receiveTel':$(".receiveTel").val(),
			'cidlist': cid_list,
			'uuid':uuid,
			'gid':gid,
			'shopMobile': shopMobile,
			'usedpointsign':usedpointsign,
			'pointType':$("#pointType").val()
		};
	}
	else {
		params = {
			'token': UserToken,
			'aid': aid,
			'receiveTel':$(".receiveTel").val(),
			'cidlist': cid_list,
			'uuid':uuid,
			'payway':'mergeAlipay',
			'shopMobile': shopMobile,
			'usedpointsign':usedpointsign,
			'pointType':$("#pointType").val()
		}
	}

	var gid = xigou.getQueryString('gid');
	if (gid) {
		params.gid = gid;
	}
	params.pointType=$("#pointType").val();
	gotoPay(params);
};

function initOrderChannelTrack(params){
	var channelsource = xigou.getLocalStorage("channelsource");
	var clientcode = xigou.getLocalStorage("clientcode");
	var distributecode = xigou.getLocalStorage("distributecode");
	var channelcode = xigou.channelcode;
	var yiqifarddate = xigou.getLocalStorage("yiqifarddate");
	var currentDate = new Date();
	if((currentDate.getTime()-parseInt(yiqifarddate))<1000*60*60*24*30
	 && channelcode!=null && $.trim(channelcode)!=''){
		params.channelcode = channelcode;
		params.channelsource = channelsource;
		params.clientcode = clientcode;
		params.distributecode = distributecode;
	}
}
//获取可用优惠券数量
function showCouponNo(){

	var uuid = xigou.getSessionStorage("buy_now_uuid") || "";
	var params = {
		token: xigou.getToken(),
		type:"3",
		curpage:"1",
		uuid: uuid,
	};

	xigou.activeUser.queryordercouponcount({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				if (null == response) {
					xigou.alert(xigou.dictionary.chineseTips.server.value_is_null);
				} else {
					var couponNum = response.data ? response.data:0;
					$("#coupon").text(couponNum + "张" );//优惠券金额
				}
			} else {
				xigou.alert('请求失败，详见' + response);
			}
		}
	});
}

//支付
function gotoPay(params) {

	if (isWeiXin()) {
		xigou.activeHtClearing.submitseaorder({
			requestBody: params,
			callback: function(response, status) { //回调函数
				gotoPaycallback(response, status);
			}
		});
	}
	else {
		xigou.activeHtClearing.mergesubmit({
			requestBody: params,
			callback: function(response, status) { //回调函数
				gotoPaycallback(response, status);
			}
		});
	}


	function gotoPaycallback(response, status){
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
					{
						xigou.removeSessionStorage("buy_now_uuid");
						xigou.setSessionStorage("haitao_clearing_orders",response,true);
						if(isWeiXin()){
							var host = encodeURIComponent(xigou.pageHost);
							var param=response.data[0].payid+","+response.data[0].ordercode+","+response.data[0].orderprice+","+xigou.getToken()+","+xigou.channelcode+","+host;
							window.location.href = xigou.xgHost + "paymentbridge.html?param="+param;
						}else {
							window.location.href = "orderspay.html";
						}
						break;
					}

					case "-104":
					{
						$.tips({
							content:response.msg || "用户未认证",
							stayTime:2000,
							type:"warn"
						})
						OnSeletPhoto();
						break;
					}
					case "-105":
					{
						$.tips({
							content:response.msg || "用户未上传身份证照片",
							stayTime:2000,
							type:"warn"
						})
						OnSeletPhoto();
						break;
					}
					case "-1":
					{
						var dia=$.dialog({
							title:'',
							content:response.msg||"支付提交失败",
							button:["确认"]
						});

						dia.on("dialog:action",function(e){
							console.log(e.index);
							// xigou.removelocalStorage("token");
							window.location.href="cart.html";
						});
					}
					default:
						$.tips({
							content:response.msg || "支付提交失败",
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


var checkidphoto = 0;//海外直邮订单特殊处理;标识
//结算-获取订单总金额
function authprice(uuid) {
	var coupon = xigou.getSessionStorage("clearing_select_coupon", true);

	var cid_list =[];
	if (coupon.length > 0) {
		for(var i = 0;i<coupon.length;i++){
			cid_list.push(coupon[i].cid);//优惠券cid
		}
	}

	var redenvelopes = xigou.getSessionStorage("clearing_select_redenvelopes", true) || {
			num: ""
		}, //红包
		type = "0"; //类型 0西客 1海囤
	var usedpointsign = xigou.getSessionStorage('usedpointsign');
	var params = {
		'token': UserToken,
		'cid': coupon.cid,
		'cidlist':cid_list,
		'uuid':uuid||'',
		'shopMobile': xigou.getSessionStorage("dssUser") == "" ? null : xigou.getSessionStorage("dssUser", true).mobile,
		'usedpointsign':usedpointsign,
		'pointType':$("#pointType").val()
	};

	xigou.activeHtClearing.authprice({
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
							if (response.data.price) { //如果获取到了总金额

								initPageData(response); //初始化页面数据
								 //defaultAddress(); //默认收货地址
								//getUserIdNumber();//身份证号
								 //fmaidcsv = response.fmaidcsv;
							} else {

								var dia=$.dialog({
                                    title:'',
                                    content:"订单已提交,请进入全部订单查看",
                                    button:["确认"]
                                });

                                dia.on("dialog:action",function(e){
                                    console.log(e.index);
                                    window.location.href = "orders.html";
                                });
							}
							break;
						case "-1001":
								var dia=$.dialog({
                                    title:'',
                                    content:"订单支付失败",
                                    button:["确认"]
                                });

                                dia.on("dialog:action",function(e){
                                    console.log(e.index);
                                    window.location.href = "orders.html";
                                });				
							break;
						case "-100":

								var dia=$.dialog({
                                    title:'',
                                    content:"请先登录再提交订单",
                                    button:["确认"]
                                });

                                dia.on("dialog:action",function(e){
                                    console.log(e.index);
                                    window.location.href = "logon.html";
                                 });	
							break;
						default:
								var dia=$.dialog({
                                    title:'',
                                    content:response.msg || "订单支付失败,请进入待支付订单查看",
                                    button:["确认"]
                                });

                                dia.on("dialog:action",function(e){
                                    console.log(e.index);
                                    window.location.href = "orders.html";
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
};
//获取默认收货地址
function defaultAddress() {
	
	var address = xigou.getQueryString("clearing_ht_select_address");
	if (address) {
		return;
	}
	var params = {
		'token': UserToken
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
				} else {
					switch (response.code) {
						case "0":
							var data = response,
								checkAddress=false;
							if ((typeof(data) != "undefined") && (typeof(data.data) != "undefined") && data.data.length>0) {
								var hasdefault = false;
								for (var d in data.data) {
									checkAddress=true;
									var _d = data.data[d];
									if (_d.isdefault == "1") {
										hasdefault = true;
										var addresscode = _d.addresscode;
										xigou.setSessionStorage("clearing_select_address", _d, true);
										var htmls = [];
										htmls.push('<div class="div_name_tel">');
										htmls.push('	<span class="div_name">' + _d.name + '</span>');
										if (_d.iscertificated == 0) {	// 未认证
											htmls.push('	<div class="div_not_certified"></div>');
											div_not_certified = true;
										}
										htmls.push('	<div class="div_tel">' + _d.tel + '</div>');
										htmls.push('</div>');
										htmls.push('<div class="div_add">收货地址:' + _d.fullinfo + '</div>');
										$('.settle-address-info').html(htmls.join(""));
										$('#li_recvaddress_info').attr("aid", _d.aid);
										addressnum=_d.aid;
										if (!_d.identityCard) {
											_d.identityCard = '';
										}
										$("#auth-num-input").val(_d.identityCard);
										$("#submit_auth").addClass("onClick");
										break;
									}
								}
								//如果所有的地址都不是默认地址，就取第一个
								if(!hasdefault){
									var _d1 = data.addresslist[0];
									var addresscode = _d1.addresscode;
									xigou.setSessionStorage("clearing_select_address", _d1, true);
									var htmls = [];
									htmls.push('<span>收件人：</span><span class="floor_right ">'+_d1.name+'</span><br>');
									if (_d1.iscertificated == 0) {	// 未认证
										htmls.push('	<div class="div_not_certified"></div>');
										needcertified = true;
									}
									htmls.push('<span>联系方式：</span><span class="floor_right ">'+_d1.tel+'</span><br>');
									htmls.push('<span>收货地址：</span><span id="select_address">'+_d.info+'</span>');
									$('.settle-address-info').html(htmls.join(""));
									$('#li_recvaddress_info').attr("aid", _d.aid);
									addressnum=_d1.aid;
								}

							}
							if(!checkAddress){
								$('.settle-address-info').html('<div class="div_add_address">添加收货地址</div>');
								//选择收货地址
								$(".settle-address-info")[xigou.events.click](function() {
									window.location.href = "recvaddress.html?selectType=1";
								});
							}
							break;
						default:
							$.tips({
						        content:response.msg || "获取默认收货地址失败",
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
};

////获取身份证号码
//function getUserIdNumber() {
//
//	var idNumber = xigou.getSessionStorage("clearing_ht_idNumber");
//	var realname = xigou.getSessionStorage("clearing_ht_realname");
//	if((typeof(idNumber) != undefined) && (typeof(realname) != undefined) && (idNumber != "") && (realname != ""))
//	{
//		var htmls = [];
//		htmls.push('<span>姓名：</span><span id="realname">'+realname+'</span><br>');
//		htmls.push('<span>证件号：</span><span id="idNumber">'+idNumber+'</span>');
//		$('.settle-auth-info').html(htmls.join(""));
//		//$("#idNumber").text(idNumber);
//		//$("#realname").text(realname);
//		return;
//	}
//	var params = {
//		'token': UserToken
//	};
//
//	xigou.activeUser.auth_query({
//		requestBody: params,
//		callback: function(response, status) { //回调函数
//			if (status == xigou.dictionary.success) {
//				if (null == response) {
//					$.tips({
//				        content:xigou.dictionary.chineseTips.server.value_is_null,
//				        stayTime:2000,
//				        type:"warn"
//				   })
//				} else {
//					if (response.code == "0" && response.data.name && response.data.code) {
//						var htmls = [];
//						htmls.push('<span>姓名：</span><span id="realname">'+response.data.name+'</span><br>');
//						htmls.push('<span>证件号：</span><span id="idNumber">'+response.data.code+'</span>');
//						$('.settle-auth-info').html(htmls.join(""));
//						xigou.setSessionStorage("clearing_ht_idNumber", response.data.code);
//						xigou.setSessionStorage("clearing_ht_realname", response.data.name);
//					}
//					else {
//						$('.settle-auth-info').html('<span class="add_auth">添加实名认证</span>');
//					}
//				}
//			} else {
//				$.tips({
//				    content:'请求失败，详见' + response,
//				    stayTime:2000,
//				    type:"warn"
//				})
//			}
//		}
//	});
//};

// 创建价格的html
function createPriceHTML(price) {
	if (!price) {
		return "¥";
	}
	var Yan = "00", Fen = "00";
	var CHARS = price.split('.');
	if (CHARS.length > 0) {
		Yan = CHARS[0];
		if (CHARS.length > 1) {
			Fen = CHARS[1];
		}
	}
	else {
		return "¥";
	}

	htmlPrice = '<span class="product-price">¥<span class="product-price1">' + Yan + '</span>.' + Fen + '</span>';
	return htmlPrice;
}

// 实名认证
function authentication(){

	$('#closeBtn')[xigou.events.click](function(){
		$('.div_tip_bg').hide();
		$('.div_tip').hide();
	});

	$('#submit_auth')[xigou.events.click](function(e){
		var _this = $(this);
		if(!_this.hasClass("onClick")){
			return;
		}

		//if (!$('#auth-num-input')[0].value) {
		//	$.tips({
		//		content:'收货人姓名不能为空!',
		//		stayTime:2000,
		//		type:"warn"
		//	});
		//	return;
		//}
		if (!$('#auth-num-input')[0].value) {
			$.tips({
				content:'收货人身份证不能为空!',
				stayTime:2000,
				type:"warn"
			});
			return;
		}
		var address = xigou.getSessionStorage("clearing_select_address",true);
		var params = {
			token:xigou.getToken(),
			aid:address.aid,
			//name:$('#auth-name-input')[0].value,
			tel:address.tel,
			provid:address.provinceid,
			provname:address.province,
			cityid:address.cityid,
			cityname:address.city,
			districtid:address.districtid,
			districtname:address.district,
			streetid:address.streetid,
			streetname:address.street,
			info:address.info,
			identityCard:$('#auth-num-input')[0].value,
			frontimg: $('#upload_img_itme1').attr('src'),
			backimg: $('#upload_img_itme2').attr('src'),
			isdefault:address.isdefault
		}
		if (params.frontimg.split('address/upload.png').length > 1) {
			params.frontimg = '';
		}
		if (params.backimg.split('address/upload.png').length > 1) {
			params.backimg = '';
		}
		xigou.activeUser.edit({
			requestBody:params,
			callback:function(response, status){
				if (status == xigou.dictionary.success) {
					// 成功
					if (response.code == 0) {
						needcertified = false;
						$('.div_tip_bg').hide();
						$('.div_tip').hide();
						//$.tips({
						//	content:response.msg||'操作成功',
						//	stayTime:2000,
						//	type:"warn"
						//});
						//setTimeout(function(){},2000)
						$(".submit-btn")[0].click();
					}
					else {
						$.tips({
							content:response.msg||'实名认证失败,请检测网络连接!',
							stayTime:2000,
							type:"warn"
						});
						return;
					}
				}
			}
		})
	})
}

//图片上传回显
function OnSeletPhoto() {
	$(".div_tip_bg").show();
	$(".div_tip").show();
	//根据内容改变查询按钮状态
	$("#auth-num-input").on("input propertychange",function(){
		if($("#auth-num-input").val().length > 0){
			$("#submit_auth").addClass("onClick");
		}else{
			$("#submit_auth").removeClass("onClick");
		}
	})

}
function bindUploadImg(){
	$('.imgItem')[xigou.events.click](function() {
		var $input = $(this).find('input');
		var img = $(this).find('img');
		$input[0].click();
	})
	$('.input_photo').unbind("change");
	$('.input_photo').on("change",function() {
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
			//	$.tips({
			//		content:'请选择小于2M的图片',
			//		stayTime:2000,
			//		type:"warn"
			//	});
			//	return;
			//}

			var reader = new FileReader();
			var img = $(this).parent('.imgItem').find('img');

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