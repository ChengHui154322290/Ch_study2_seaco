var type=xigou.getQueryString("type");
if(type == null)
{
    window.location.href="../logon.html";
}
$(function() {
	if(isWeiXin()){
		$("header").hide();
		$(".confirmBtn_wx").show();
		 if(type == 1){
			 $("title").html("微信");
		 }
		 if(type == 2){
			 $("title").html("QQ");
		 }
		 if(type == 3){
			 $("title").html("邮箱");
		 }
	}
    if(type == 1){
        $(".update_name").text("微信");
        $(".update_value").attr("placeholder","请输入微信号");
        var weixin=xigou.getQueryString("weixin");
        $(".update_value").val(weixin);
        $(".confirmBtn, .confirmBtn_wx").on("click",function(){
            if(checkWx()){
                var update_value = $(".update_value").val();
                var params = {
                    token:xigou.getToken(),
                    type:1,
                    weixin:update_value
                }
                console.log(xigou.promoterFunc);
                xigou.promoterFunc.updatepromoter({
                    requestBody : params,
                    callback : success
                })
            }else{
                $.tips({
                    content:"请输入正确微信号",
                    stayTime:2000,
                    type:"warn"
                })
            }

        })
    }else if(type == 2){
        $(".update_name").text("QQ");
        $(".update_value").attr("placeholder","请输入QQ号");
        var qq=xigou.getQueryString("qq");
        $(".update_value").val(qq);
        $(".confirmBtn, .confirmBtn_wx").on("click",function(){
            if(checkNum()){
                var update_value = $(".update_value").val();
                var params = {
                    token:xigou.getToken(),
                    type:1,
                    qq:update_value
                }
                console.log(xigou.promoterFunc);
                xigou.promoterFunc.updatepromoter({
                    requestBody : params,
                    callback : success
                })
            }else{
                $.tips({
                    content:"请输入正确QQ号",
                    stayTime:2000,
                    type:"warn"
                })
            }

        })
    }else if(type == 3){
        $(".update_name").text("邮箱");
        $(".update_value").attr("placeholder","请输入正确格式的邮箱");
        var email=xigou.getQueryString("email");
        $(".update_value").val(email);
        $(".confirmBtn, .confirmBtn_wx").on("click",function(){
            var update_value = $(".update_value").val();
            var params = {
                token:xigou.getToken(),
                type:1,
                email:update_value
            }
            console.log(xigou.promoterFunc);
            xigou.promoterFunc.updatepromoter({
                requestBody : params,
                callback :success
            })
        })
    }
});
function checkNum(){
    var reg = /^\d+$/;
    if(reg.test($(".update_value").val())){
        return true;
    }else{
        return false;
    }
}
function checkWx(){
    var reg = /[\u4E00-\u9FA5\uF900-\uFA2D]/;
    if(!reg.test($(".update_value").val())){
        return true;
    }else{
        return false;
    }
}
function success(response, status) {
    if (status == xigou.dictionary.success) {
        if (response.code == 0) {
            $.tips({
                content:response.msg || "修改成功",
                stayTime:2000,
                type:"warn"
            })
            setTimeout(function(){
                window.location.href = "../dssaboutus.html";
            },1000)
        }
        else
        {
            $.tips({
                content:response.msg || "修改失败",
                stayTime:2000,
                type:"warn"
            })
        }
    }
}