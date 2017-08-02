$(function(){
	$(document.body).on('click','.sendcouponbtn',function(){
		/**
		var promoterId = $(this).attr('promoterid');
		pageii = $.layer({
			type : 2,
			title : '发券给推广员',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 700 ],
			iframe : {
				src : domain + '/dss/promotercoupon/index?promoterId='+promoterId
			}
		});
		**/
		view($(this).attr('promoterid'));
	});
	if($(".promotername").is('input')){
		$( ".promotername" ).autocomplete({
			source: function (request, response) {
	            var term = request.term;
	            request.promoterName = term;
	            $.post(domain+'/dss/promoterinfo/querypromoterlistbylikepromotername.htm', {
	            	promoterName:request.promoterName
	              }, response,'json');
	        },
			max:10,
			minLength: 2,
			select: function( event, ui ) {
				 $("input[name='promoterId']").val(ui.item.promoterId);
				 $("input.promotername").val(ui.item.promoterName);
				 return false;
			}
		}).on('blur',function(){
			if($(this).val()=='' || $(this).val()==null){
				$(":hidden[name='promoterId']").val('');
			}
		});
		$(".promotername").data("autocomplete")._renderItem = function (ul, item) {
		    return $("<li></li>")
		        .data("item.autocomplete", item)
		        .append("<a>" + item.promoterName + ",类型："+item.promoterTypeCn+",手机："+item.mobile+"</a>")
		        .appendTo(ul);
		};
	}
	if($(".couponname").is('input')){
		$( ".couponname" ).autocomplete({
			source: function (request, response) {
	            $.post(domain+'/dss/promotercoupon/querycouponlist.htm', {
	            	couponName:request.term
	              }, response,'json');
	        },
			max:10,
			minLength: 2,
			select: function( event, ui ) {
				$("input.couponname").val('');
				 $("input[name='couponId']").val(ui.item.id);
				 $("input.couponname").val(ui.item.couponName);
				 return false;
			}
		}).on('blur',function(){
			var couponId = $(":hidden[name='couponId']");
			if($(this).val()=='' || $(this).val()==null){
				couponId.val('');
			}
			if(couponId.val()==''|| couponId.val()==null){
				$(this).val('');
			}
		}).focus(function () {
            $(this).autocomplete("search");
        });
		$(".couponname").data("autocomplete")._renderItem = function (ul, item) {
		    return $("<li></li>")
		        .data("item.autocomplete", item)
		        .append("<a>" + item.couponName + ",类型："+(item.couponType==0?'满减':'现金')+"</a>")
		        .appendTo(ul);
		};
	}
});
function view(code) {
	showTab("promoter_coupon_btn_" + code, "兑换码新增页面", '/topicCoupon/listStaticActivity');
	return false;
}

function showTab(id, text, url) {
	var tv = {};
	tv.linkId = id+"_link";
	tv.tabId =  id;
	tv.url = url;
	tv.text = text;
	try{
		window.parent.showTab(tv);
	} catch(e){
	}
}