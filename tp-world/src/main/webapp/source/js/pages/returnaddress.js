$(function() {
    // 读取当前售后详情
    var asid = xigou.getQueryString("asid");
    var _aftersaleslist = xigou.getLocalStorage("aftersaleslist");
    var datalist = JSON.parse(_aftersaleslist);

    for (var i = 0; i < datalist.length; i++) {
        var data = datalist[i];
        if (data.asid == asid) {
            if (data.returnaddress) {   // 退货地址
                $('.returnaddress').text(data.returnaddress);
            }
            else {
                $('.div_address_detail').hide();
            }
            if (data.linkname) { // 联系人
                $('.div_name span').text(data.linkname);
            }
            else {
                $('.div_name').hide();
            }

            if (data.linktel) { // 联系电话
                $('.div_tel span').text(data.linktel);
            }
            else {
                $('.div_tel').hide();
            }
            return;
        }

    }
    
    if(isWeiXin()){
    	$(".ui-header").hide();
    	$(".div_desc").css({
    		"margin-top":"0"
    	})
    	$("title").html("退货地址");
    }
});
