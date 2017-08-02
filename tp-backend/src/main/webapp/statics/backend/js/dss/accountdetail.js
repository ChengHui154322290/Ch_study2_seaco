$(function(){
	$(document.body).on('click','.accountdetailbtn',function(){
		var promoterid = $(this).attr('promoterid');
		pageii = $.layer({
			type : 2,
			title : '流水帐信息',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 700 ],
			iframe : {
				src : domain + '/dss/accountdetail/list?userId='+promoterid
			}
		});
	});
	
	if($('#accountdetaillist').is("table")){
		var colM = [ 
		    {name:'surplusAmount',index:'surplusAmount', align:"center",width:80},
		    {name:'amount',index:'amount',align:"center",width:100},
	        {name:'accountTime',index:'accountTime',formatter:farmatTimeByNumber,align:"center",width:80},
			{name:'accountTypeCn',index:'accountTypeCn',align:"center",width:80},
			{name:'bussinessTypeCn',index:'bussinessTypeCn',align:"center",width:80},
			{name:'bussinessCode',index:'bussinessCode',align:"center",width:80},
			{name:'createTime',index:'createTime',formatter:farmatTimeByNumber,align:"center",width:80}
				];
		var colN = ['账户余额','金额','日期 ','账务类型', '业务类型','业务编码','创建时间'];
		$("#accountdetaillist").jqGrid({  
		    treeGridModel: 'adjacency',  
		    mtype:'post',
		    url: domain+'/dss/accountdetail/list.htm?m=' + Math.random(),
		    datatype: 'json',
		    postData: {userId:$(':hidden[name=userId]').val()},
		    viewrecords: true,
			loadonce:false,
		    height:'300',
		    width:"700",
		    colNames:colN,
			colModel:colM,
		    pager: "#gridpager",
		    multiselect : false,
		    pgbuttons:true,
		    pginput:true,
		    caption: "流水帐", 
		    rowNum:10,
		    scrollrows:true,//使得选中的行显示到可视区域
		    sortname : 'surplusAmount',
		    rowList: [10,20,50], 
		    jsonReader: {  
		        root: "rows",
		        repeatitems: false  
		    }
		});
		jQuery("#accountdetaillist").jqGrid('navGrid', '#gridpager', {
			add : false,
			edit : false,
			del : false
		});
	}
});