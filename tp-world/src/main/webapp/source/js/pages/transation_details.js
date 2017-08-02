$(function() {
	if(isWeiXin()){
		$(".cart-body").hide();
		$("title").html("订单详情");
	}
    var code = xigou.getQueryString('code');
    var res = xigou.getSessionStorage(code);
    console.log(res);
    var data = JSON.parse(res);
    console.log(data.data.status)
    if(data.data.status == "5"){
    	$(".pay_status > img").attr("src","img/xg_pay/pay_failed.png");
    }
$(".shop_title").find("p").text(data.data.itemdesc);
$(".shop_title").find("span").text(data.data.message);
$(".pay_num").find("span").text(data.data.ordercode);
$(".pay_for").find("span").text('-'+ data.data.consume);
$(".pay_time").find("span").text( data.data.time);

})