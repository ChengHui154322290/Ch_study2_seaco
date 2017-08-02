var orderListFnMap = null;
jQuery(function(){
	orderListFnMap = initQueryPage("queryFormId","/seller/order/orderQuery","contentShow");
	orderListFnMap.loadPage(1);
	
	jQuery("#order_list_query").click(function(e){
		e.preventDefault();
		orderListFnMap.loadPage(1);
		return false;
	});
	
	jQuery("#order_info_export").click(function(e){
		e.preventDefault();
		jQuery("#queryFormId").attr("action","/seller/order/orderExport");
		jQuery("#queryFormId").submit();
	});
	
	jQuery("#order_Types").change(function(){
		var thisVal = jQuery(this).val();
		if(thisVal && (thisVal.toString() == '4' || thisVal.toString() == '5')){
			jQuery("#deliveryWayId").html(deliveryWayStr);
			jQuery("#fhfsTd").html("发货方式：");
			jQuery("#deliveryWayId").show();
		} else {
			jQuery("#deliveryWayId").html("");
			jQuery("#fhfsTd").html("");
			jQuery("#deliveryWayId").hide();
		}
	});
	timer();
	setInterval("timer()",60000);
	
	$(document.body).on("click",".fastdeliveryorderbtn",function() {
		var code = $(this).attr("data_code");
		var htmlData = [];
		htmlData.push('<table class="form_table pt15 pb15 deliveryuserinfotable" style="width:395px;"><body>');
		htmlData.push('<tr><td>配送人员：</td>');
		htmlData.push('<td>'+$('.fastuserinfodiv').html()+'</td></tr>');
		htmlData.push('<tr><td>备注：</td><td><input type="text" class="input-text lh30" name="deliveryremark"/></td></tr>');
		htmlData.push('<tr><td>　</td><td> </td></tr>');
		htmlData.push('<tr><td></td><td><input type="button" data_code="'+code+'" class="btn btn82 btn_save2 deliveryusersavebtn" value="确定"/></td></tr>');
		htmlData.push('</body></table>');
		var _html=htmlData.join('');
			
		var pageii2 = $.layer({
			  type: 1,
			  title: '请选择配送人员',
			  area: [400, 400],
			  shadeClose: true,
			  maxmin: true,
			  fix : false,  
			  page: {
			    html: _html
			  }
			});
		$('.xubox_layer').css('top','200px');

		$(document.body).on('click','.deliveryusersavebtn',function(){
			var code = $(this).attr("data_code");
			var deliveryuserinfo = $('.deliveryuserinfotable .fastuserinfo').val();
			var remark = $('.deliveryuserinfotable [name=deliveryremark]').val();
			var fastuserid = $('.deliveryuserinfotable .fastuserinfo :selected').attr('fastuserid');
			if(deliveryuserinfo==null || $.trim(deliveryuserinfo)==''){
				layer.alert('请选择配送人员');
				return false;
			}
			var content=deliveryuserinfo+($.trim(remark).length>0?(' '+remark):'');
			 $.ajax({
				  url:domain+'/seller/order/deliveryorder',
				  data:{orderCode:code,content:content,fastUserId:fastuserid},
				  dataType:'json',
				  type:'post',
				  success:function(result){
					  if(result.success){
						  layer.msg('已配送', 1, 13);
						  layer.close(pageii2);
						  showOrderTab(code);
					  }else{
						  layer.alert('配送失败：'+result.msg.message);
					  }
				  }
			  });
		});
	});
	
	$(document.body).on("click",".fastreceivingorderbtn",function() {
		var code = $(this).attr("data_code");
		var dddd = layer.confirm('确认要接单？',function(){
		  $.ajax({
			  url:domain+'/seller/order/receivingorder',
			  data:{orderCode:code},
			  dataType:'json',
			  type:'post',
			  success:function(result){
				  if(result.success){
					  layer.msg('已接单', 1, 13);
					  layer.close(dddd);
					  showOrderTab(code);
				  }else{
					  layer.msg('接单失败：'+result.msg.message);
				  }
			  }
		  });
		}, function(){});
	});
	
	
	// 订单日志
	$(document.body).on('click',"#order-log",function() {
		$.layer({
			type : 2,
			title: '订单日志',
			shadeClose: true,
			maxmin: true,
			fix : false,  
			area: ['1024px', 500],                     
			iframe: {
				src : domain + "/seller/order/log.htm?subCode=" + $(this).attr("data_code")
			} 
		});
	});
	
	$(document.body).on('click',"#ordertrack",function() {
		$.layer({
			type : 2,
			title: '物流跟踪',
			shadeClose: true,
			maxmin: true,
			fix : false,  
			area: ['1024px', 500],                     
			iframe: {
				src : domain + "/seller/order/orderDelivery.htm?orderCode=" + $(this).attr("data_code")
			} 
		});
	});
});

/**
 * 
 * @param index
 */
function _gotoPage(index){
	orderListFnMap.gotoPage(index);
}

/**
 * 订单详情tab
 * 
 * @param orderId
 */
function showOrderTab(orderId){
	newTab("order_list_detail","订单详情","/seller/order/orderDetails?oid="+orderId);
}

function timer(){
	$('.fastorderovertime').each(function(){
		var _cur = $(this);
		var overTime = _cur.text();
		var _parent = _cur.parent();
		overTime = parseInt(overTime);
		if(overTime<1){
			_parent.html('超时<span class="fastorderovertime">'+(--overTime)+'</span>分');
		}else{
			_cur.text(--overTime);
		}
		var color = (256-(overTime*2))+",00,"+(overTime*2);
		if(overTime<1){
			color = "255,0,0";
		}
		_parent.css("color","rgb("+color+")");
	});
}

