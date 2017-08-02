var min_date = new Date(Date.parse('2016/10/01'));
$(function() {
	removeQueryParams();
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				initSkuList();
				initDate();
				initQueryBtn();
			}else{
				window.location.href="index.html";
			}
		}
	});
	
	$(".time").click(function(){
		$(this).addClass('time_change').siblings().removeClass('time_change');
		initDateByTime($(this).attr('datavalue'));
	});
	$(".dates").click(function(){
		$(".time").removeClass('time_change');
	});
	
	if(!isWeiXin()){
		$("header").show();
		$(".sales_items").css({
			"margin-top":"60px"
		})
	}
});
/**查询*/
function initQueryBtn(){
	var querybtn = $('a.btn');
	querybtn[xigou.events.click](function(){
		var beginquerydate = $('#beginquerydate').val();
		var endquerydate = $('#endquerydate').val();
		if($.trim(beginquerydate).lenght>0 && $.trim(endquerydate).lenght>0
		 && (new Date(beginquerydate.replace(/\-/g,'/').getTime())>(new Date(endquerydate.replace(/\-/g,'/').getTime())))){
			$.tips({
		        content:'起始时间应小于结束时间',
		        stayTime:2000,
		        type:"warn"
		    });
			return;
		 }
		xigou.setSessionStorage('beginquerydate',beginquerydate);
		xigou.setSessionStorage('endquerydate',endquerydate);
		xigou.setSessionStorage('skucode',$('#skucode').val());
		xigou.setSessionStorage('redeemstatus',$('#redeemstatus').val());
		xigou.setSessionStorage('redeemcode',$('.redeemcode').val());
		window.location.href='store_recordResult.html';
	});
}
/**获取团购券项目*/
function initSkuList(){
	var params = {
			'token': xigou.getToken(),
		};
	xigou.redeemManager.paramskulist({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length == 0) return false;
				var code = json.code;
				if (code == 0) {
					var selectArea = new MobileSelectArea();
					selectArea.init({
						trigger:$('.saleskucodelist'),
						value:$('#skucode').val(),
						level:1,
						data:response.data,
						position:"bottom"});
				}else{
					me.lock();
					$(".dropload-down").hide();
				}
			}
		}
	});
}
/**点击日期段设置日期*/
function initDate(){
	var currentDate = new Date();
	var beginDate = new Date();
	beginDate.setTime((beginDate.getTime()-1000*60*60*24*7));
	$('#beginquerydate').val(formatDate(beginDate,'yyyy-MM-dd'));
	$('#beginquerydate').mdater({
		minDate : min_date,
		maxDate : currentDate
	});
	$('#endquerydate').val(formatDate(currentDate,'yyyy-MM-dd'));
	$('#endquerydate').mdater({
		minDate : min_date,
		maxDate : currentDate
	});
}

function initDateByTime(dateType){
	var endDate = new Date();
	var beginDate = new Date();
	if(dateType=='month'){
		beginDate.setMonth(beginDate.getMonth()-1);
	}else if(dateType=='quarter'){
		beginDate.setMonth(beginDate.getMonth()-3);
	}else{
		beginDate.setDate(beginDate.getDate()-7);
	}
	if(min_date.getTime()>beginDate.getTime()){
		beginDate = min_date;
	}
	$('#beginquerydate').val(formatDate(beginDate,'yyyy-MM-dd'));
	$('#beginquerydate').mdater({
		minDate : min_date,
		maxDate : endDate
	});
	$('#endquerydate').val(formatDate(endDate,'yyyy-MM-dd'));
	$('#endquerydate').mdater({
		minDate : min_date,
		maxDate : endDate
	});
}
function removeQueryParams(){
	xigou.removeSessionStorage('beginquerydate');
	xigou.removeSessionStorage('endquerydate');
	xigou.removeSessionStorage('skucode');
	xigou.removeSessionStorage('redeemstatus');
	xigou.removeSessionStorage('redeemcode');
}