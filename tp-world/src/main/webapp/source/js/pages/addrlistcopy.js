/**
 *
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-10 23:43:43
 * @version Ver 1.0.0
 */
var floor = 0,
	floor_code = ['', '', '', ''],
	old_adr = JSON.parse(xigou.getLocalStorage("address")),
	my_address = {
		"provinceid": "",
		"province": "",
		"cityid": "",
		"city": "",
		"districtid": "",
		"district": "",
		"streetid": "",
		"street": ""
	},
	flag = xigou.getQueryString("showChooseArea"),
	backUlr = xigou.getQueryString("backurl") || "index.html",
	selectUrl = xigou.getQueryString("selecturl") || "";
if(selectUrl == 'select_shippingaddress.html'){
	backUlr = backUlr +'?selecturl=' +selectUrl;
}
if (backUlr == 'lottery.html') {
	var form = xigou.getQueryString("form");
	if (form) {
		backUlr = backUlr + '?form=' + form;
	}
}
$(function() {
	getProvince();
	// if (flag != "false") {
	// 	chooseArea();
	// }

	$('#back')[xigou.events.click](function() {
		switch (floor) {
			case 0:
				xigou.setLocalStorage("address", old_adr, true);
				xigou.setLocalStorage("my_address", {}, true);
				window.location.href = backUlr;
				break;
			case 1:
				floor--;
				getProvince();
				break;
			case 2:
			case 3:
				floor--;
				getList(floor_code[floor]);
				break;
			default:
				window.location.href = backUlr;
				break;
		}
	});
});


//获得省份列表
function getProvince() {
	var params = {
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length == 0) window.location.href = backUlr;

				var code = json.code;
				var output = "";
				if (code == 0) {
					var provincelist = json.data || null;
					if (null == provincelist || provincelist.length == 0) window.location.href = backUlr;
					for (var j = 0; j < provincelist.length; j++) {
						var region = provincelist[j].region;
						output += '<div>';
						output += '<a>' + region + '</a>';
						var info = provincelist[j].info || null;
						if (null == info || info.length == 0) window.location.href = backUlr;
						output += '<ul class="ui-list ui-list-text ui-list-link ui-border-tb addrlist-ul">';
						for (var i = 0; i < info.length; i++) {
							output += '<li class="ui-border-t chooseNext province" name="' + info[i].provcode + '" pro="' + provincelist[j].regcode + '" area="' + region + '"><span class="floor_city">' + info[i].province + '</span></li>';
						}
						output += '</ul>';
						output += '</div>';
					}
					$('#area').html(output);
					getcity();
				} else {
					window.location.href = backUlr;
				}
			}
		}
	};
	xigou.activeArea.getProvinces(params);
};


//遍历其他市级、区县、街道
function getList(code) {
	var params = {
		p: 'id=' + code,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length == 0) window.location.href = backUlr;

				var code = json.code;
				var output = "";
				if (code == 0) {
					var arealist = json.data || null;
					if (null == arealist || arealist.length == 0) window.location.href = backUlr;

					output += '<ul class="ui-list ui-list-text ui-list-link ui-border-tb addrlist-ul">';
					for (var i = 0; i < arealist.length; i++) {
						output += '<li class="ui-border-t chooseNext" name="' + arealist[i].code + '"><span class="floor_city">' + arealist[i].name + '</span><span class="choose_next"></span></li>';
					}
					output += '</ul>';
					$('#area').html(output);
					getcity();
				} else {
					window.location.href = backUlr;
				}
			}
		}
	};

	xigou.activeArea.getCity(params);
};


//获取市列表
function getcity() {
	$('.chooseNext')[xigou.events.click](function() {
		floor++;
		var code = $(this).attr('name');
		var name = $(this).find('span').eq(0).text();
		floor_code[floor] = code;
		if (null != code || '' != code) {
			//保存收货地址名称的编号	
			switch (floor) {
				case 1:
					my_address.provinceid = code;
					my_address.province = name;
					break;
				case 2:
					my_address.cityid = code;
					my_address.city = name;
					break;
				case 3:
					my_address.districtid = code;
					my_address.district = name;
					break;
				case 4:
					my_address.streetid = code;
					my_address.street = name;
					break;
			}
			xigou.setLocalStorage("my_address", my_address, true);
			//保存商品地址(首页)		
			if ($(this).hasClass('province')) {
				var adr = JSON.parse(xigou.getLocalStorage("address"));
				adr.area.code = $(this).attr('pro');
				adr.area.name = $(this).attr('area');

				adr.city.code = code;
				adr.city.name = name;
				//xigou.setLocalStorage("address", adr, true);暂时隐藏（西北248错误COD，目前得先华东）
				if (backUlr == "index.html") {
					window.location.href = backUlr;
					return false;
				}
			}
			getList(code);
		}
	});
};

//弹出选择框
function chooseArea() {
	var html = new Array();
	html.push('<div class="empty_box_des">');
	html.push('	<h2></h2>');
	html.push('	<p>您的位置地上海</p>');
	html.push('</div>');
	html.push('<div style="width:100%; padding:15px 0 10px; float:left">');
	html.push('<div style="width:50%; padding:15px 0 10px; float:left">');
	html.push('	<a id="modify" style="display:block;width:90%; margin:0 auto; height:45px; line-height:45px; text-align:center; color:#fff; font-size:1.6rem; background-color:#19b4ad; border-radius:5px;">更改</a>');
	html.push('</div>');
	html.push('<div style="width:50%; padding:15px 0 10px; float:left">');
	html.push('	<a id="into" style="display:block;width:90%; margin:0 auto;height:45px; line-height:45px; text-align:center; color:#fff; font-size:1.6rem; background-color:#c8597a; border-radius:5px;">进入</a>');
	html.push('</div>');

	var dia=$.dialog({
        title:'',
        content:html.join(''),
        button:["确认"]
        });

	// xigou.showModalDialog({
	// 	html: html.join('')
	// });

	//更改
	$('#modify')[xigou.events.click](function(e) {
		$('.ui-dialog').remove();
		$('.ui-mask').remove();
		$(window).off('ortchange', e.preventDefault());
		$(document).off('touchmove', e.preventDefault());
	});

	//进入
	$('#into')[xigou.events.click](function() {
		window.location.href = backUlr;
	});

};