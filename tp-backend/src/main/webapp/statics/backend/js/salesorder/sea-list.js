$(document).ready(function() {

	var DATA_URL = domain + "/order/data.htm";
	
	var CANCEL_URL = domain + "/order/cancel.htm";
	// 取消订单
	$(document.body).on('click','.btn-cancel',function(){
		var code = $(this).attr("data_code");
		 layer.prompt({title: '请输入取消原因', formType: 2}, function(text){
			 if(text==null || text=='' || $.trim(text).length==0){
				 layer.msg('请输入取消原因'); 
			 }else{
				var data = syncPost(CANCEL_URL, {code: code,cancelReason:text});
				if (data.success) {
					layer.msg("取消成功！");
					location.reload();
				} else {
					layer.alert("取消失败：" + data.data);
				}
			 }
		 });
	});
	
	function parseDate(text) {
		var dateArr = text.split(" ")[0].split("-");
		var timeArr = text.split(" ")[1].split(":");
		var date = new Date();
		date.setFullYear(dateArr[0]);
		date.setMonth(dateArr[1] - 1)
		date.setDate(dateArr[2]);
		date.setHours(timeArr[0]);
		date.setMinutes(timeArr[1]);
		date.setSeconds(0);
		date.setMilliseconds(0);
		return date;
	}
	
	$(".clearanceinfolist").on("click", function(){
		showTab("clearance_info", "清关单列表", "/order/customs/clearanceinfolist");
	});
});

function showTab(id, text, url) {
	var tv = {};
	tv.linkId = id + "_link";
	tv.tabId = id;
	tv.url = url;
	tv.text = text;
	try {
		window.parent.showTab(tv);
	} catch (e) {
	}
}

