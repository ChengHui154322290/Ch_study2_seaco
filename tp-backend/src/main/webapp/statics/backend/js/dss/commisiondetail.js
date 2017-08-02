$(function(){
	$(document.body).on('click','.commisionlistbtn', function() {
		var promoterid = $(this).attr('promoterid');
		pageii = $.layer({
			type : 2,
			title : '个人佣金明细信息',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '900px',600 ],
			iframe : {
				src : domain + '/dss/commisiondetail/list?fixed=1&promoterId='+promoterid
			}
		});
	});
	
	$(document.body).on('click','.querypagebtn',function(){
		jQuery("#commisiondetaillist").jqGrid(
				'setGridParam',
				{
					url : domain+'/dss/commisiondetail/list.htm?m=' + Math.random(),
					postData:{
			    		promoterId:$(":hidden[name='promoterId']").val(),
			    		orderCode:$(":text[name='orderCode']").val(),
			    		collectStatus:$("select[name='collectStatus']").val(),
			    		memberId:$(":hidden[name='memberId']").val()
			    	}
				}).trigger('reloadGrid');
	});
	if($('#commisiondetaillist').is("table")){
		var colM = [
		    {name:'bizCode',index:'bizCode', align:"center",width:120},
		    {name:'bizTypeCn',index:'bizTypeCn',align:"center",width:100},
	        {name:'bizAmount',index:'bizAmount',align:"center",width:80,formatter:'currency', formatoptions:{decimalSeparator: '.'}},
			{name:'commision',index:'commision',align:"center",width:80, formatter:'currency', formatoptions:{decimalSeparator: '.'}},
			{name:'commisionRate',index:'commisionRate',align:"center",width:80, formatter:'currency', formatoptions:{decimalSeparator: '.'}},
			{name:'orderAmount',index:'orderAmount',align:"center",width:80, formatter:'currency', formatoptions:{decimalSeparator: '.'}},
			{name:'couponAmount',index:'couponAmount',align:"center",width:80, formatter:'currency', formatoptions:{decimalSeparator: '.'}},
			{name:'orderReceiptTime',index:'orderReceiptTime',align:"center",width:80,formatter:farmatTimeByNumber},
			{name:'orderCode',index:'orderCode',align:"center",width:80},
			{name:'orderStatusCn',index:'orderStatusCn',align:"center",width:80},
			{name:'collectStatusCn',index:'collectStatusCn',align:"center",width:80},
			{name:'createTime',index:'createTime',align:"center",width:80,formatter:farmatTimeByNumber},
			{name:'memberAccount',index:'memberAccount',align:"center",width:80}
			];
		var colN = ['业务编码','业务类型','业务金额','佣金', '比率','订单支付金额','订单卡金额','收货日期','订单编码','订单状态','做帐状态','发货日期','会员账号'];
		
		$("#commisiondetaillist").jqGrid({  
		    treeGridModel: 'adjacency',  
		    mtype:'post',
		    url: domain+'/dss/commisiondetail/list.htm?m=' + Math.random(),
		    postData:{
		    		promoterId:$(":hidden[name='promoterId']").val(),
		    		orderCode:$(":text[name='orderCode']").val(),
		    		collectStatus:$("select[name='collectStatus']").val(),
		    		memberId:$(":hidden[name='memberId']").val()
		    	},
		    datatype: 'json', 
		    viewrecords: true,
			loadonce:false,
		    height:'300',
		    width:"860",
		    colNames:colN,
			colModel:colM,
		    pager: "#gridpager",
		    multiselect : false,
		    pgbuttons:true,
		    pginput:true,
		    caption: "佣金明细信息", 
		    rowNum:10,
		    scrollrows:true,//使得选中的行显示到可视区域
		    sortname : 'bizCode',
		    rowList: [10,20,50], 
		    jsonReader: {  
		        root: "rows",
		        repeatitems: false  
		    }
		});
		jQuery("#commisiondetaillist").jqGrid('navGrid', '#gridpager', {
			add : false,
			edit : false,
			del : false
		});
	}
	$("#commisiondetaillist").setGridWidth($(window).width()*0.98);
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
		
		$( ".memberaccount" ).autocomplete({
			source: function (request, response) {
	            $.post(domain+'/dss/commisiondetail/querymemberinfobylikememberaccount.htm', {
	            	promoterId:$(':hidden[name=promoterId]').val(),
	            	memberAccount:request.term
	              }, response,'json');
	        },
			max:10,
			minLength: 2,
			select: function( event, ui ) {
				 $("input[name='memberId']").val(ui.item.id);
				 $("input.memberaccount").val(ui.item.nickName);
				 return false;
			}
		}).on('blur',function(){
			if($(this).val()=='' || $(this).val()==null){
				$(":hidden[name='memberId']").val('');
			}
		});
		$(".memberaccount").data("autocomplete")._renderItem = function (ul, item) {
		    return $("<li></li>")
		        .data("item.autocomplete", item)
		        .append("<a>" + item.nickName + ",手机："+item.mobile+"</a>")
		        .appendTo(ul);
		};
	}
});