// JavaScript Document
var time = new Date().getTime();
xigou.removeSessionStorage('buy_now_details_url');
$(function () {
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				initCart()
		}else{
			xigou.setSessionStorage("loginjump_url", "cart.html");
			window.location.href = "logon.html";
		}
	}
	});
	//getDssUserInfo();
	if(isWeiXin()){
		$('.cart-body').hide();
		$("title").html("购物车");
		$(".content").css({
			"margin-top":"0"
		})
	}
});
/**
 * 购物车初始化
 */
function initCart() {
	$('#origin_tab li').removeClass('active').eq(1).addClass('active');
	$('.floor_origin').removeClass('hide').eq(0).addClass('hide');
	$('.total_fixed_left').removeClass('hide').eq(0).addClass('hide');
	loadCart();
}
/**
 * 获取店铺主人信息
 */
function getDssUserInfo(){
	xigou.getDssUserInfo({
		callback: function(response, status) {
			if (status == xigou.dictionary.success) {
				if (response != null && response.pageShow && response.pageShow == 1) {
					//var html = $(".cart_tip").html();
					//$(".cart_tip").html(html.replace("小西提醒", response.name+"的店铺提醒"));
				}
			}
		}
	});
}
//金额计算
function GetTabGroupMoney() {
	return "seaGroupMoney";
};


function goHome(){
	var $goHome = $(".ui-btn-primary");
	$goHome[xigou.events.click](function(){
		window.location.href="index.html";
	})
}

//点击购物车 去结算
function shopcart() {
	var $shopcart = $('#shopcart-settle');
	$shopcart.unbind( "ontouchend" in document ? "tap" : "click");
	$shopcart[xigou.events.click](function() {
		xigou.removeSessionStorage('itemType');
		//获取当前选中的所有商品
		var $shopCartCheck = $('#cart-effective-list input:checked'),
			$shopCartProid = $shopCartCheck.parents(".div_check_box").siblings(".ui-list-item-info").find(".cart_shop_opt"),
			productsJsons = [];
		$.each($shopCartProid,function(){
			var productsJson = {};
			productsJson.sku = $(this).attr("sku");
			productsJson.tid = $(this).attr("tid");
			productsJson.count = $(this).find('.car_item').text();
			productsJsons.push(productsJson);
		});

		if(productsJsons.length == 0){
			console.log(productsJsons.length);
			var dia=$.dialog({
				title:'',
				content: '请选择至少一款商品' ,
				button:["确认"]
			});
			return;
		}
			if ($("#shopcart-settle").hasClass("disabled") == false) {
				var params = {
					"products": productsJsons,
					"type": "7",
					"token": xigou.getToken(),
				};
				xigou.activeShoppingCart.authprice({
					requestBody: params,
					callback: function (response, status) { //回调函数
						if (status == xigou.dictionary.success) {
							var json = response || null;
							if (null == json || json.length == 0) return false;
							if (json.code == 0) {
								//先清除结算数据
								xigou.removeClearingData();
									window.location.href = "settle.html?selectType=2";
							} else {
								var dia=$.dialog({
									title:'',
									content: json.msg ,
									button:["确认"]
								});

								dia.on("dialog:action",function(eDlg){
									if(1 == eDlg.index)
									{
										//location.reload();

									}
								});
							}
						}
					}
				});
			}
	});
};


//购物车
function loadCart() {
	var params = {
		'token': xigou.getToken()
	};
	xigou.activeShoppingCart.loadCart({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length == 0)return false;

				if (json.code == 0) {
					GroupBackFun(json.data);
					needSelectAll(this);
				}
			}
		}
	});
};

//购物车-回调方法调用
/*
*/
function GroupBackFun(json) {
	var htmlData = [], htmlInvalid = [];

	// 拆分商品
	for (var i = 0; i < json.mergeitems.length; i++) {
		var mergeitemsItem = json.mergeitems[i];
		var selectCount = parseInt(0);
		htmlData.push('<li class="clearfix div_channel" channelcode="' + mergeitemsItem.channelcode + '" warehouseid="' +mergeitemsItem.warehouseid + '">');
		htmlData.push('	<div class="div_item_channel" channelcode="' + mergeitemsItem.channelcode + '">' + mergeitemsItem.channel + '</div>');
		htmlData.push('	<ul class="ui-list">');
		for (var j = 0; j < mergeitemsItem.products.length; j++) {
			var item = mergeitemsItem.products[j];
			var itemhref = 'item.html?sku='+ item.sku + '&tid=' + item.tid + '&url=cart.html';

			htmlData.push('<li class="li-item-info" tid="' + item.tid + '" sku="' + item.sku + '">');
			htmlData.push('	<div class="div_check_box">');
			htmlData.push('		<div class="ui-checkbox">');
			if (item.selected == '1') {
				htmlData.push('			<label class="ui-checkbox-s"><input type="checkbox" checked="checked" onclick="productSelectChange(this)"></label>');
			}
			else {
				htmlData.push('			<label class="ui-checkbox-s"><input type="checkbox" onclick="productSelectChange(this)"></label>');
			}
			htmlData.push('		</div>');
			htmlData.push('	</div>');
			htmlData.push('	<div class="ui-list-item-info">');
			htmlData.push('		<div class="ui-list-img">');
			htmlData.push('			<a href="' + itemhref + '"><img src="' + item.imgurl + '" alt="" ></a>');
			var desc = '';
			/*if (item.taxrate) {
				desc = '税率:' + parseInt(item.taxrate) + '% ';
			}*/
			if (item.limitcount) {
				desc = '限购:' + solveNull(item.limitcount, "") + '件';
			}
			htmlData.push('		<div class="ui-list-img-gray2">' + desc + '</div>');
			htmlData.push('		</div>');
			htmlData.push('		<div class="ui-list-info">');
			htmlData.push('			<div class="delete"><img src="img/delete.png" sku="' + item.sku + '" tid="' + item.tid + '" alt="" onclick="del(this,0);"></div>');
			htmlData.push('			<span class="delete_span"></span>');
			htmlData.push('			<div class="cart_product_name">');
			htmlData.push('				<a href="' + itemhref + '">' + item.name + '</a>');
			htmlData.push('			</div>');

			if(item.itemspecs && item.itemspecs != ""){
				htmlData.push('			<div class="cart_product_spe">' + item.itemspecs + '</div>');
			}else{
				htmlData.push('			<div class="cart_product_spe"></div>');
			}
			htmlData.push('			<div>');
			var maxNum = item.limitcount?item.limitcount:((parseInt(data[i].stock) > 99 ? 99 : data[i].stock));
			htmlData.push('				<div class="cart_shop_opt" maxnum="' + maxNum + '" price="' + item.lineprice + '" oldprice="' + item.oldprice + '" sku="' + item.sku + '" tid="' + item.tid + '">');
			if (item.count == '1') {
				htmlData.push('<a href="javascript:void(0);" class="quantity_decrease disabled">-</a>')
			}
			else {
				htmlData.push('<a href="javascript:void(0);" class="quantity_decrease">-</a>')
			}
			htmlData.push('					<intput class="car_item quantity_txt" maxlength="' + item.count + '" readyonly="readyonly">' + item.count + '</intput>');
			if (parseInt(item.count) == parseInt(maxNum)) {
				htmlData.push('<a href="javascript:void(0);" class="quantity_increase disabled">+</a>');
			}
			else {
				htmlData.push('<a href="javascript:void(0);" class="quantity_increase">+</a>');
			}
			htmlData.push('					</div>');
			var Yuan = item["lineprice"].split('.')[0] || '00';
			var Jiao = item["lineprice"].split('.')[1] || '00';
			htmlData.push('					<div class="line_price"><span class="product-price">¥<span class="product-price1">' + Yuan + '</span>.' + Jiao + '</span>');
			htmlData.push('				</div>');
			htmlData.push('			</div>');
			htmlData.push('		</div>');
			htmlData.push('	</div>');
			htmlData.push('</li>');

			if (item.selected == '1') {
				selectCount += parseInt(item.count);
			}
		}
		htmlData.push('	</ul>');
		htmlData.push('	<div class="div_sel_channel_info">');
		htmlData.push('		<div class="sel-count">已选' + selectCount + '件</div>');
		htmlData.push('		<div class="sel_info">');
		htmlData.push('			<div class="sel_info_tax">税费<span>¥' + mergeitemsItem.taxes + '</span></div>');
		htmlData.push('			<div class="sel_info_price">总计<span>¥' + mergeitemsItem.price + '</span></div>');
		htmlData.push('		<div>');
		htmlData.push('	<div>');
		htmlData.push('</li>');
	}

	// 失效商品
	//json.invaliditems = json.products;
	for (var i = 0; i < json.invaliditems.length; i++) {
		var invalidItems = json.invaliditems[i];
		var tempSku = invalidItems.sku;
		var tempTid = invalidItems.tid;
		var itemhref = 'item.html?sku='+ invalidItems.sku + '&tid=' + invalidItems.tid + '&url=cart.html';
		var itemspecs = invalidItems.itemspecs;

		var Yuan ;
		var Jiao ;
		if(invalidItems.price && invalidItems.oldprice){
			Yuan = invalidItems.price.split('.')[0];
			Jiao = invalidItems.price.split('.')[1];
		}else if(invalidItems.oldprice){
			Yuan = invalidItems.oldprice.split('.')[0];
			Jiao = invalidItems.oldprice.split('.')[1];
		}else{
			Yuan =  '00';
			Jiao =  '00';
		}
		htmlInvalid.push('<li class="ui-border-t clearfix">');
		htmlInvalid.push('	<div class="invalid_label"></div>');
		htmlInvalid.push('	<div class="ui-list-img">');
		htmlInvalid.push('		<a href="' + itemhref +  '">');
		htmlInvalid.push('			<img src="' + invalidItems.imgurl + '" alt="alt" width="90" height="90"/>');
		htmlInvalid.push('		</a>');
		var desc = '';
		/*if (item.taxrate) {
			desc = '税率:' + parseInt(item.taxrate) + '% ';
		}*/
		if (invalidItems.limitcount) {
			desc = '限购:' + solveNull(invalidItems.limitcount, "") + '件';
		}
		htmlInvalid.push('		<div class="ui-list-img-gray2">' + desc + '</div>');
		htmlInvalid.push('	</div>');
		htmlInvalid.push('	<div class="ui-list-info">');
		htmlInvalid.push('		<div class="delete">');
		htmlInvalid.push('			<img src="img/delete.png" style="width: 12px;height: 12px;display: block"  sku='+tempSku+' tid='+tempTid+' alt="" onclick="del(this,0);"/>');
		htmlInvalid.push('		</div>');
		htmlInvalid.push('		<span class="delete_span"></span>');
		htmlInvalid.push('		<div class="cart_product_name">');
		htmlInvalid.push('			<a href="' + itemhref + '">' + invalidItems.name + '</a>');
		htmlInvalid.push('		</div>');
		if(itemspecs && itemspecs != ""){
			htmlInvalid.push('			<div class="cart_product_spe">' + itemspecs + '</div>');
		}else{
			htmlInvalid.push('			<div class="cart_product_spe"></div>');
		}
		htmlInvalid.push('		<div>');
		htmlInvalid.push('			<span class="product-invalid-price">¥<span class="product-price1">' + Yuan + '</span>.' + Jiao + '</span>');
		htmlInvalid.push('		</div>');
		htmlInvalid.push('	</div>');
		htmlInvalid.push('</li>');
	}

	if (json.mergeitems.length != 0 && json.invaliditems.length == 0) {	// 有效商品不为空
		$("#cart-effective-list").append(htmlData.join(" "));
		$("#cart-effective-list").show();
		$(".cartnull").hide();
	}

	if (json.invaliditems.length != 0 && json.mergeitems.length == 0) { //无效商品不为空
		$('#cart-invalid-list').append(htmlInvalid.join(' '));
		$('.car_invalid').show();
		$(".cartnull").show();
		$('.cartbar').hide();
		goHome();
	}

	if(json.invaliditems.length != 0 && json.mergeitems.length != 0){ //既有有效商品又有无效商品
		$("#cart-effective-list").append(htmlData.join(" "));
		$("#cart-effective-list").show();
		$('#cart-invalid-list').append(htmlInvalid.join(' '));
		$('.car_invalid').show();
		$(".cartnull").hide();
	}

	if(json.mergeitems == 0 && json.invaliditems == 0){		// 购物车为空
	    $(".cartnull").show();
	    $('.cartbar').hide();
		$("#cart-effective-list").hide();
		$('.car_invalid').hide();
		goHome();
	}

	//数量加减cart_shop_opt
	$(".cart_shop_opt").cartshopopt(function(val, num, e) {
		editNumFun(e, val); //num 加：1  减：-1
	});

	//总金额 赋值
	setTotalMoney(json.totalprice, json.disprice);
}

//购物车-商品数量改变
function editNumFun(e, num) {
	var $parent = $(e).parent(),
		$carItem = $(e).parents(".car_item"),
		$carData = $(e).parents(".cart_shop_opt");

	//购物车-获取总金额 ---更新商品数量
	var params = {
		"type": "5",
		"products":
			[{
				"tid": $carData.attr("tid"),
				"sku": $carData.attr("sku"),
				"count": num
			}],
		"token": xigou.getToken(),
	};
	xigou.activeShoppingCart.authprice({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json) return false;

				changeNum(json.data.mergeitems, params);
				setTotalMoney(json.data.totalprice, json.data.disprice);
				var code = json.code;
				if (code != 0) {
					$.tips({
						content:json.msg,
						stayTime:2000,
						type:"warn"
					})
				}
			}
		}
	});
}
//修改小计数量
function changeNum(mergeitems, params) {
	if (params.type == 1 || params.type == 2) {
		changeAllNum(mergeitems);
		return;
	}

	var IndexArray = [];
	var taxesInfo = {};
	for (var i = 0; i < params.products.length; i++) {
		var sku = params.products[i].sku;
		var tid = params.products[i].tid;
		var channelItem = $('li.li-item-info[sku="'+sku+'"][tid="'+tid+'"]');
		var index = channelItem.parents('li.div_channel').index();

		for (var j = 0; j < mergeitems.length; j++) {
			var bBreak = false;
			for (var k = 0; k < mergeitems[j].products.length; k++) {
				if ( mergeitems[j].products[k].sku == sku && mergeitems[j].products[k].tid == tid) {
					// 修改商品总价
					var $total_price = channelItem.find("div.line_price");
					$total_price.html(createPriceHTML(mergeitems[j].products[k].lineprice));

					var checkbox = channelItem.find('label.ui-checkbox-s input')[0];
					if (mergeitems[j].products[k].selected == '1') {
						checkbox.checked = true;
					}
					else {
						checkbox.checked = false;
					}

					if ($.inArray(IndexArray, index) == -1) {
						IndexArray.push(IndexArray, index);
						taxesInfo[index] = j;
					}

					bBreak = true;
					break;
				}
			}
			if (bBreak == true) {
				break;
			}
		}
	}

	$.each(taxesInfo, function(index, mergeitemsindex) {
		var count = parseInt(0);
		var mergeitemsItem = mergeitems[mergeitemsindex];
		for (var i = 0; i < mergeitemsItem.products.length; i++ ) {
			if (mergeitemsItem.products[i].selected == '1') {
				count += parseInt(mergeitemsItem.products[i].count);
			}
		}

		var divChannelItem = $($('#cart-effective-list li.div_channel')[index]);
		divChannelItem.find('.sel-count').html('已选' + count + '件');
		divChannelItem.find('.sel_info_tax span').html('¥' + mergeitemsItem.taxes);
		divChannelItem.find('.sel_info_price span').html('¥' + mergeitemsItem.price);
	})
}

function changeAllNum(mergeitems) {
	var IndexArray = [];
	var taxesInfo = {};
	$.each(mergeitems, function(mergeitemsIndex, mergeitemsItem) {
		$.each(mergeitemsItem.products, function(productsIndex, productsItem) {
			var sku = productsItem.sku;
			var tid = productsItem.tid;
			var channelItem = $('li.li-item-info[sku="'+sku+'"][tid="'+tid+'"]');
			var index = channelItem.parents('li.div_channel').index();

			var $total_price = channelItem.find("div.line_price");
			$total_price.html(createPriceHTML(productsItem.lineprice));

			var checkbox = channelItem.find('label.ui-checkbox-s input')[0];
			if (productsItem.selected == '1') {
				checkbox.checked = true;
			}
			else {
				checkbox.checked = false;
			}

			if ($.inArray(IndexArray, index) == -1) {
				IndexArray.push(IndexArray, index);
				taxesInfo[index] = mergeitemsIndex;
			}
		})
	})

	$.each(taxesInfo, function(index, mergeitemsindex) {
		var count = parseInt(0);
		var mergeitemsItem = mergeitems[mergeitemsindex];
		for (var i = 0; i < mergeitemsItem.products.length; i++ ) {
			if (mergeitemsItem.products[i].selected == '1') {
				count += parseInt(mergeitemsItem.products[i].count);
			}
		}

		var divChannelItem = $($('#cart-effective-list li.div_channel')[index]);
		divChannelItem.find('.sel-count').html('已选' + count + '件');
		divChannelItem.find('.sel_info_tax span').html('¥' + mergeitemsItem.taxes);
		divChannelItem.find('.sel_info_price span').html('¥' + mergeitemsItem.price);
	})
}

//存储合计金额、合计优惠
function setTotalMoney(price, disprice) {

    var $totalMoney = $( " .totalMoney");
	var $totalFreeMoney = $( " .totalFreeMoney");
	$totalMoney.html('总计：¥' + (price || '0'));
	$totalFreeMoney.html('已优惠：¥' + (disprice || '0'));
    shopcart();
}

// 单击delete按钮删除一行
//index 0:正常商品 1：失效商品
function del(e, index) {
	var dia=$.dialog({
        title:'',
        content:"确定删除此商品吗？",
         button:["取消","确认"]
    });

    dia.on("dialog:action",function(eDlg){
    	if(1 == eDlg.index)
    	{
    		getstoreid(e, index);
    	}
    });
}


//购物车-获取总金额 ---全选
function selectAll(type) {
	var params = {
		"type": type,
		"token": xigou.getToken()
	};
	var checkBox = document.getElementById("cart-effective-list").getElementsByTagName('input');
	if (checkBox.length == 0) {
		$.tips({
            content:'购物车内无商品',
            stayTime:2000,
            type:"warn"
        })
		return false;
	}
	if (type == "1") {
		for (var i = 0; i < checkBox.length; i++) {
			checkBox[i].checked = true;
		}
	} else {
		for (var i = 0; i < checkBox.length; i++) {
			checkBox[i].checked = false;
		}
	}
	xigou.activeShoppingCart.authprice({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json) return false;

				changeNum(json.data.mergeitems, params);
				setTotalMoney(json.data.totalprice, json.data.disprice);
				var code = json.code;
				if (code != 0) {
					$.tips({
						content:json.msg,
						stayTime:2000,
						type:"warn"
					})
				}
			}
		}
	});
};

//购物车-获取总金额 ---单选
function authpriceSelect(e) {
    var $this = $(e);
    var $liItem = $this.parents("li");
    var $carItem = $liItem.find(".car_item")[0];
    var $carData = $liItem.find(".cart_shop_opt")[0];
	var count = 0;
	if($carItem){
		count= $carItem.innerHTML;
	}
	var params = {
		"products":
			[{
			    "sku": $carData.getAttribute("sku"),
			    "tid": $carData.getAttribute("tid"),
			    "count":count
			}]
		,
		"type": "3",
		"token": xigou.getToken()
	};
	xigou.activeShoppingCart.authprice({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json) return false;

				changeNum(json.data.mergeitems, params);
				setTotalMoney(json.data.totalprice, json.data.disprice);
				var code = json.code;
				if (code != 0) {
					$.tips({
						content:json.msg,
						stayTime:2000,
						type:"warn"
					})
				}
			}
		}
	});
};

//购物车-获取总金额 ---单选取消
function authpriceSelectCancel(e) {

	var $this = $(e);
	var $liItem = $this.parents("li");
	var $carItem = $liItem.find(".car_item")[0];
	var $carData = $liItem.find(".cart_shop_opt")[0];
	var count = 0;
	if($carItem){
	 count = $carItem.innerHTML
	}
	var params = {
		"type": 4,
		"products":
			[{
				"sku": $carData.getAttribute("sku"),
				"tid": $carData.getAttribute("tid"),
				"count": count
			}],
		"token": xigou.getToken()
	};
	xigou.activeShoppingCart.authprice({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json) return false;

				changeNum(json.data.mergeitems, params);
				setTotalMoney(json.data.totalprice, json.data.disprice);
				var code = json.code;
				if (code != 0) {
					$.tips({
						content:json.msg,
						stayTime:2000,
						type:"warn"
					})
				}
			}
		}
	});
};

//购物车--商品选择改变（单选）
function productSelectChange(e) {
	if ($(e).prop("checked")) {
		authpriceSelect(e);
	} else {
		authpriceSelectCancel(e);
	}
	needSelectAll(this);
}

//购物车--商品选择改变（全选）
function allproductSelectChange(e) {
	if ($(e).prop("checked")) {
		selectAll("1");
	} else {
		selectAll("2");
	}
}
//如果所有单选都已经选中，则全选也选中 否则全选不选
function needSelectAll(e){
	var checkBox = document.getElementById("cart-effective-list").getElementsByTagName('input');
	var checkBoxAll = document.getElementById(GetTabGroupMoney()).getElementsByTagName('input');
	checkBoxAll = checkBoxAll[0];
	if(checkBox.length>0){
		var count = 0;
		for (var i = 0; i < checkBox.length; i++) {
			if(checkBox[i].checked){
				count ++;
			}
		}

		if(count==checkBox.length){
			checkBoxAll.checked =true;
		}else{
			checkBoxAll.checked =false;
		}
	}else{
		checkBoxAll.checked =false;
	}
}


//购物车-删除
//index 0:正常商品 1：失效商品
function getstoreid(e, index) {

	var pJsons = [];
	var pJson = {};
	pJson.sku = $(e).attr("sku");
	pJson.tid = $(e).attr("tid");
	pJsons.push(pJson);

	var params = {
		'token': xigou.getToken(),
		'products': pJsons,
		'type' : "6",
	};

	xigou.activeShoppingCart.authprice({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json) return false;
				var code = json.code;
				var output = "";
				if (code == 0) {
					location.reload();
				}
				else
				{
					$.tips({
						content:"删除失败!",
						stayTime:2000,
						type:"warn"
					})
				}
			}
		}
	});
};

//处理空值
function solveNull(d, n) {
	return (d) == null || typeof(d) == "undefined" ? n : d
}

// 全选
function onSelAllProductItem(checkbox) {
	var checked = checkbox.checked;
	var checkBox = document.getElementById('cart-effective-list').getElementsByTagName('input');
	for (var i = 0; i < checkBox.length; i++) {
			checkBox[i].checked = checked;
	}
	allproductSelectChange(checkbox);
};

// 创建价格的html
function createPriceHTML(price) {
    var Yan = "00", Fen = "00";
    var CHARS = price.split('.');
    if (CHARS.length > 0) {
        Yan = CHARS[0];
        if (CHARS.length > 1) {
            Fen = CHARS[1];
        }
    }

    htmlPrice = '<span class="product-price">¥<span class="product-price1">' + Yan + '</span>.' + Fen + '</span>';
    return htmlPrice;
}