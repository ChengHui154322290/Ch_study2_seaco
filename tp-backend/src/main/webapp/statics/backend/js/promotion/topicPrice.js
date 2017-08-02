$(function(){
	function topicPriceformat(value) {
		if(!/^[0-9]*(\.[0-9]{1,2})?$/.test(value)){
			return [false,"请输入正确价格","0.00"]; 
		}
		
		if(parseFloat(value) >= 0 && parseFloat(value)<=100000) { 
			return [true,"",""]; 
		} else {
			return [false,"请输入正确价格[0~100000内]","0.00"]; 
		} 
	}
	var colM = [
	    {name:'id',index:'id', hidden:true},
	    {name:'spuName',index:'spuName', align:"center",width:200},
        {name:'spu',index:'spu',align:"center",width:80},
		{name:'detailName',index:'detailName',align:"center",width:200},
		{name:'sku',index:'sku',align:"center",width:100},
		{name:'basicPrice',index:'basicPrice',align:"center",width:80,formatter:'currency', formatoptions:{decimalSeparator: '.'}},
		{name:'topicPrice',index:'topicPrice',align:"center",width:80,editable:true,formatter:'currency', formatoptions:{decimalSeparator: '.'},editrules:{custom:true,custom_func:topicPriceformat}},
		{name:'discount',index:'discount',align:"center",width:80,formatter:'currency', formatoptions:{decimalSeparator: '.'}},
		{name:'statusDesc',index:'statusDesc',align:"center",width:80},
		{name:'id',index:'id', width:80, fixed:true, sortable:false, resize:false, formatter:'actions',formatoptions:{keys:true,delbutton:false}}
			];
	var colN = ['','SPU名称','SPU','商品名称 ','SKU', '市场价','促销统一价','折扣','发布状态','操作'];
	$("#skulist").jqGrid({  
	    treeGridModel: 'adjacency',  
	    mtype:'post',
	    url: domain+'/promotion/topicprice/list.htm?m=' + Math.random(),
	    datatype: 'json', 
	    postData:{spuName:$(':text[name=spuName]').val(),spu:$(':text[name=spu]').val(),detailName:$(':text[name=detailName]').val(),sku:$(':text[name=sku]').val(),status:$('select[name=status]').val()},
	    height:'350',
	    width:"1100",
	    colNames:colN,
		colModel:colM,
	    pager: "#gridpager",
	    multiselect : true,
	    pgbuttons:true,
	    pginput:true,
	    caption: "商品SKU促销价格列表", 
	    rowNum:10,
	    scrollrows:true,//使得选中的行显示到可视区域
	    rowList: [10,20,50,100], 
	    jsonReader: {  
	        root: "rows",
	        repeatitems: false  
	    },
	    editurl : domain+'/promotion/topicprice/updatetopicprice'
	});
	jQuery("#skulist").jqGrid('navGrid', '#gridpager', {
		add : false,
		edit : false,
		del : false
	});
	$(document.body).on('click','.querypagebtn',function(){
		jQuery("#skulist").jqGrid(
				'setGridParam',
				{
					url : domain+'/promotion/topicprice/list.htm?m=' + Math.random(),
					postData:{spuName:$(':text[name=spuName]').val(),spu:$(':text[name=spu]').val(),detailName:$(':text[name=detailName]').val(),sku:$(':text[name=sku]').val(),status:$('select[name=status]').val()}
				}).trigger('reloadGrid');
	});
	
	// 批量设置促销价格
	$(document.body).on('click','.batchupdatepricebtn',function(){
		var s;
	    var skuIdList = new Array();
	    s = jQuery("#skulist").jqGrid('getGridParam', 'selarrrow');
	    if(s.length){
	    	for(var i=0;i<s.length;i++){
	    		var myrow = jQuery('#skulist').jqGrid('getRowData',s[i]);
	    		skuIdList.push(myrow.sku);
	    	}
	    }else{
	    	layer.alert('没有选择提现信息');
	    }
		 layer.prompt({title: '请输入折扣比率(1.00-10.00)', formType: 2}, function(text){
			 if(text==null || text=='' || $.trim(text).length==0 || !/^[0-9]0?(\.[0-9]{1,2})?$/.test(text) || parseFloat(text) <= 0 || parseFloat(text) > 10){
				 layer.msg('请输入折扣比率(0.01-10.00)'); 
			 }else{
				var data = syncPost(domain+'/promotion/topicprice/batchupdatetopicprice.htm', {skuList: skuIdList,discount:text});
				if (data.success) {
					layer.msg("促销价格批量设置成功！",1,1);
					jQuery("#skulist").jqGrid('setGridParam').trigger('reloadGrid');
				} else {
					layer.alert("促销价格批量设置失败：" + data.data);
				}
			 }
		 });
	});
});