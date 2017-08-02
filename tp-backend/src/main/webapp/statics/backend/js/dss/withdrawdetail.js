$(function(){
	$(document.body).on('click','.withdrawlistbtn', function() {
		var promoterid = $(this).attr('promoterid');
		pageii = $.layer({
			type : 2,
			title : '个人提现明细信息',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 700 ],
			iframe : {
				src : domain + '/dss/withdrawdetail/list?withdrawor='+promoterid
			}
		});
	});
	
	if($('#withdrawdetailllist').is("table")){
		var colM = [ 
		    {name:'withdrawDetailCode',index:'withdrawDetailCode', align:"center",width:"8%"},
		    {name:'withdrawTime',index:'withdrawTime',formatter:farmatTimeByNumber,align:"center",width:"10%"},
	        {name:'withdrawAmount',index:'withdrawAmount',align:"center",width:"8%"},
			{name:'withdrawStatusCn',index:'withdrawStatusCn',align:"center",width:"8%"},
			{name:'withdraworName',index:'withdraworName',align:"center",width:"8%"},
			{name:'withdrawBank',index:'withdrawBank',align:"center",width:"8%"},
			{name:'withdrawBankAccount',index:'withdrawBankAccount',align:"center",width:"8%"},
			{name:'oldSurplusAmount',index:'oldSurplusAmount',align:"center",width:"8%"},
			{name:'payedTime',index:'payedTime',formatter:farmatTimeByNumber,align:"center",width:"8%"},
			{name:'paymentor',index:'paymentor',align:"center",width:"8%"}
				];
		var colN = ['提现明细编码','提现日期','提现金额 ','提现状态', '提现者','提现银行','银行卡号','提现前金额','打款日期','打款人'];
		
		$("#withdrawdetailllist").jqGrid({  
		    treeGridModel: 'adjacency',  
		    mtype:'post',
		    url: domain+'/dss/withdrawdetail/list.htm?m=' + Math.random(),
		    datatype: 'json', 
		    postData:{promoterName:$(':text[name=promoterName]').val(),withdrawStatus:$('select[name=withdrawStatus]').val(),withdrawor:$(':hidden[name=withdrawor]').val()},
		    height:'300',
		    width:"100%",
		    colNames:colN,
			colModel:colM,
		    pager: "#gridpager",
		    multiselect : false,
		    pgbuttons:true,
		    pginput:true,
		    caption: "提现明细信息", 
		    rowNum:10,
		    scrollrows:true,//使得选中的行显示到可视区域
		    sortname : 'withdrawDetailCode',
		    rowList: [10,20,50], 
		    jsonReader: {  
		        root: "rows",
		        repeatitems: false  
		    }
		});
		jQuery("#withdrawdetailllist").jqGrid('navGrid', '#gridpager', {
			add : false,
			edit : false,
			del : false
		});
	}
	$("#withdrawdetailllist").setGridWidth($(window).width()*0.98);
	
	if($('#withdrawauditlist').is("table")){
		var colM_audit= [ 
		    {name:'withdrawDetailId',index:'withdrawDetailId',  hidden:true},
		    {name:'withdrawDetailCode',index:'withdrawDetailCode', align:"center",width:130},
		    {name:'withdrawTime',index:'withdrawTime',align:"center",width:130, formatter:farmatTimeByNumber},
	        {name:'withdrawAmount',index:'withdrawAmount',align:"center",width:80, formatter:'currency', formatoptions:{decimalSeparator: '.'}},
			{name:'withdrawStatusCn',index:'withdrawStatusCn',align:"center",width:80},
			{name:'withdraworName',index:'withdraworName',align:"center",width:80},
			{name:'withdrawBank',index:'withdrawBank',align:"center",width:120},
			{name:'withdrawBankAccount',index:'withdrawBankAccount',align:"center",width:200},
			{name:'oldSurplusAmount',index:'oldSurplusAmount',align:"center",width:80, formatter:'currency', formatoptions:{decimalSeparator: '.'}},
			{name:'payedTime',index:'payedTime',align:"center",width:130,formatter:farmatTimeByNumber},
			{name:'paymentor',index:'paymentor',align:"center",width:80},
			{name:'withdrawDetailId2',index:'withdrawDetailId2', align:"center",formatter:function(cellValue, options, rowObject){
				if(rowObject.withdrawStatus<4){
					var	str="<a href='javascript:auditwithdrawbtn("+rowObject.withdrawDetailId+")'>审核</a>";
					return str;
				}
				return "";
			}}
				];
		var colN_audit = ['','提现明细编码','提现日期','提现金额 ','提现状态', '提现者','收款平台','收款卡号','提现前金额','打款日期','打款人','操作'];
		
		$("#withdrawauditlist").jqGrid({  
		    mtype:'post',
		    url: domain+'/dss/withdrawdetail/list.htm?m=' + Math.random(),
		    datatype: 'json', 
		    viewrecords: true,
			loadonce:false,
			postData:{createUser:$('createUser').val(),withdrawStatus:$('select[name=withdrawStatus]').val(),withdrawor:$(':hidden[name=withdrawor]').val()},
		    height:'300',
		    width:"1100",
		    colNames:colN_audit,
			colModel:colM_audit,
		    pager: "#gridpager",
		    multiselect : true,
		    pgbuttons:true,
		    pginput:true,
		    caption: "提现明细信息", 
		    rowNum:10,
		    scrollrows:true,//使得选中的行显示到可视区域
		    sortname : 'withdrawDetailId',
		    rowList: [10,20,50], 
			onSelectRow : function(ids) {
				var rowData = $("#withdrawauditlist").jqGrid('getRowData',ids);
				var url = domain+'/dss/withdrawdetail/loglist.htm?m=' + Math.random();
				if (ids == null) {
					ids = 0;
					if (jQuery("#withdrawloglist").jqGrid('getGridParam',
							'records') > 0) {
						jQuery("#withdrawloglist").jqGrid(
								'setGridParam',
								{
									url : url,
									postData:{withdrawDetailId:rowData.withdrawDetailId},
									page : 1
								});
						jQuery("#withdrawloglist").jqGrid('setCaption',
								"编码"+rowData.withdrawDetailCode+"日志 ").trigger(
								'reloadGrid');
					}
				} else {
					jQuery("#withdrawloglist").jqGrid('setGridParam', {
						url : url,
						postData:{withdrawDetailId:rowData.withdrawDetailId},
						page : 1
					});
					jQuery("#withdrawloglist").jqGrid('setCaption',
							"编码"+rowData.withdrawDetailCode+"日志 ").trigger(
							'reloadGrid');
				}
			},
		    jsonReader: {  
		        root: "rows",
		        repeatitems: false  
		    }
		});
		jQuery("#withdrawauditlist").jqGrid('navGrid', '#gridpager', {
			add : false,
			edit : false,
			del : false
		});
     
		$("#withdrawauditlist").setGridWidth($(window).width()*0.98);

		$(document.body).on('click','.querywithdrawpagebtn',function(){
			jQuery("#withdrawauditlist").jqGrid(
					'setGridParam',
					{
						url : domain+'/dss/withdrawdetail/list.htm?m=' + Math.random(),
						postData:{withdrawor:$(':hidden[name=withdrawor]').val(),withdrawStatus:$('select[name=withdrawStatus]').val()}
					}).trigger('reloadGrid');
		});
	}
	
	if($('#withdrawloglist').is('table')){
		var withdrawDetailId = $(':hidden[name=withdrawDetailId]').val();
		$("#withdrawloglist").jqGrid({  
		    mtype:'post',
		    url: domain+'/dss/withdrawdetail/loglist.htm?m=' + Math.random(),
		    postData:{withdrawDetailId:withdrawDetailId},
		    datatype: 'json', 
		    height:'300',
		    width:"700",
		    colNames:['日志时间','操作人','操作前状态','操作后状态','操作类型','备注'],
			colModel:[
			          {name:'createTime',index:'createTime',align:"center",width:150,formatter:farmatTimeByNumber},
			          {name:'createUser',index:'createUser',align:"center",width:80},
			          {name:'oldStatusCn',index:'oldStatusCn',align:"center",width:100},
			          {name:'currentStatusCn',index:'currentStatusCn',align:"center",width:100},
			          {name:'activeTypeCn',index:'activeTypeCn',align:"center",width:100},
			          {name:'remark',index:'remark',align:"center",width:200}
					 ],
		    caption: "提现明细信息", 
		    rowNum:100,
		    scrollrows:true,//使得选中的行显示到可视区域
		    sortname : 'createTime',
		    rowList: [10,20,50], 
		    jsonReader: {  
		        root: "rows",
		        repeatitems: false  
		    }
		});
	}
	$("#withdrawloglist").setGridWidth($(window).width()*0.98);
	$(document.body).on('click','.withdrawauditbtn',function(){
		$.ajax({
			url:domain+'/dss/withdrawdetail/audit',
			data:$('#withdrawauditform').serialize(),
			type:'post',
			dataType:'json',
			success:function(result){
				if(result.success){
					layer.alert("审核成功", 1, function(){
						layer.close();
						window.parent.location.reload();
                    });
				}else{
					layer.msg(result.msg.message,1,3);
				}
			}
		});
	});
	
	$(document.body).on('click','.querypagebtn',function(){
		
	});
	
	var validate = $("#withdrawauditform").validate({
		debug: false, 
        onkeyup: false,   
        submitHandler: function(form){   //表单提交句柄,为一回调函数，带一个参数：form   
        	$.ajax({
        		url:domain+'/dss/withdrawdetail/audit',
        		data:$("#withdrawauditform").serialize(),
        		type:'post',
        		dataType:'json',
        		success:function(resultInfo){
        			if(result.success){
    					layer.msg('审核成功', 1,1);
    					layer.close();
    					window.parent.location.reload();	
    				}else{
    					layer.msg(result.msg.message,1,3);
    				}
        		}
        	});  
        },   
	    rules: {
	    	withdrawStatus: {required:true},
	    	remark: {maxlength:512}
	    },
	    messages: {
	    	withdrawStatus: {required:'请选择审核状态'},
	    	remark: {maxlength:'备注信息不能超过500个字'}
	    }
	});
	
	if($(":text[name='payedTime']").is('input')){
		$(":text[name='payedTime']").datepicker({format: 'yyyy-MM-dd HH:mm:ss'});
	}
	
	if($( ".withdraworname" ).is('input')){
		$( ".withdraworname" ).autocomplete({
			source: function (request, response) {
		        $.post(domain+'/dss/promoterinfo/querypromoterlistbylikepromotername.htm', {
		        	promoterName:request.term
		          }, response,'json');
		    },
			max:10,
			minLength: 2,
			select: function( event, ui ) {
				 $("input[name='withdrawor']").val(ui.item.promoterId);
				 $("input.withdraworname").val(ui.item.promoterName);
				 return false;
			}
		}).on('blur',function(){
			if($(this).val()=='' || $(this).val()==null){
				$(":hidden[name='withdrawor']").val('');
			}
		});
		$(".withdraworname").data("autocomplete")._renderItem = function (ul, item) {
		    return $("<li></li>")
		        .data("item.autocomplete", item)
		        .append("<a>" + item.promoterName + ",手机："+item.mobile+",店铺类型："+item.promoterTypeCn+"</a>")
		        .appendTo(ul);
		};
	}
	
	$(document.body).on('click','.exportwithdrawbtn',function(){
		$('#withdrawdetaillistForm').attr('action',domain+'/dss/withdrawdetail/exportlist.htm');
		$('#withdrawdetaillistForm').submit();
	});
	
	$(document.body).on('click','.batchauditbtn',function(){
	    var s;
	    var withdrawDetailIdList = new Array();
	    s = jQuery("#withdrawauditlist").jqGrid('getGridParam', 'selarrrow');
	    if(s.length){
	    	for(var i=0;i<s.length;i++){
	    		var myrow = jQuery('#withdrawauditlist').jqGrid('getRowData',s[i]);
	    		withdrawDetailIdList.push(myrow.withdrawDetailId);
	    	}
		    batchauditwithdrawbtn(withdrawDetailIdList.toString(),false);
	    }else{
	    	layer.alert('没有选择提现信息');
	    }
	});
	$(document.body).on('click','.batchpaymentedbtn',function(){
	    var s;
	    var withdrawDetailIdList = new Array();
	    s = jQuery("#withdrawauditlist").jqGrid('getGridParam', 'selarrrow');
	    if(s.length){
	    	for(var i=0;i<s.length;i++){
	    		var myrow = jQuery('#withdrawauditlist').jqGrid('getRowData',s[i]);
	    		withdrawDetailIdList.push(myrow.withdrawDetailId);
	    	}
		    batchauditwithdrawbtn(withdrawDetailIdList.toString(),true);
	    }else{
	    	layer.alert('没有选择提现信息');
	    }
	});
	$(document.body).on('click','.withdrawbatchauditbtn',function(){
		$.ajax({
			url:domain+'/dss/withdrawdetail/batchaudit',
			data:$('#withdrawauditform').serialize(),
			type:'post',
			dataType:'json',
			success:function(result){
				if(result.success){
					layer.alert("审核成功", 1, function(){
						layer.close();
						window.parent.location.reload();
                    });
				}else{
					layer.msg(result.msg.message,1,3);
				}
			}
		});
	});
});
function auditwithdrawbtn(withdrawDetailId){
	pageii = $.layer({
		type : 2,
		title : '个人提现明细信息',
		shadeClose : true,
		maxmin : true,
		fix : false,
		area : [ '700px', 700 ],
		iframe : {
			src : domain + '/dss/withdrawdetail/audit?withdrawDetailId='+withdrawDetailId
		}
	});
}
function batchauditwithdrawbtn(withdrawDetailIds,payed){
	pageii = $.layer({
		type : 2,
		title : '个人提现打款审核',
		shadeClose : true,
		maxmin : true,
		fix : false,
		area : [ '700px', 400 ],
		iframe : {
			src : domain + '/dss/withdrawdetail/batchaudit?withdrawDetailIds='+withdrawDetailIds+"&payed="+payed
		}
	});
}
