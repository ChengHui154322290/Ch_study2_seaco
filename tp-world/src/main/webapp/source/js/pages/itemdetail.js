
var content = xigou.getLocalStorage("itemdetail");
if(typeof(content) != "undefined")
{
	$('.detail-content').html(content);
}

if(isWeiXin()){
	$("header").hide();
	$(".detail-content").css({
		"margin-top":"0"
	})
	$("title").html("图文详情");
}