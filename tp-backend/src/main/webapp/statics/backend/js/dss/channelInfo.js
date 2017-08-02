$(function(){
	$(document.body).on('click','.querypagebtn',function(){
		jQuery("#channelinfolist").jqGrid(
				'setGridParam',
				{
					url : domain+'/dss/channelinfo/list.htm?m=' + Math.random()/**,
					postData:{
			    		promoterId:$(":hidden[name='promoterId']").val(),
			    		orderCode:$(":text[name='orderCode']").val(),
			    		collectStatus:$("select[name='collectStatus']").val(),
			    		memberId:$(":hidden[name='memberId']").val()
			    	}*/
				}).trigger('reloadGrid');
	});
	var colM = [ 
    		{name:'channelId',index:'channelId', align:"center",width:120},
    	    {name:'channelCode',index:'channelCode',align:"center",width:80},
    	    {name:'channelName',index:'channelName',align:"center",width:80},
    	    {name:'token',index:'token',align:"center",width:300},
    		{name:'remark',index:'remark',align:"center",width:80},
    		{name:'disabledStr',index:'disabledStr',align:"center",width:80},
    		{name:'channelId',index:'channelId',align:"center",width:80,
	    		formatter:function(cellValue, options, rowObject){
	    			var	str="<a href='javascript:creatToken("+rowObject.channelId+")'>[token]</a>";
					return str;
				}
    		}
    		];
    var colN = ['渠道ID','渠道代码','渠道名称','token','备注', '禁用','操作'];
    if($('#channelinfolist').is('table')){
    	$("#channelinfolist").jqGrid({  
    	    treeGridModel: 'adjacency',  
    	    mtype:'post',
    	    url: domain+'/dss/channelinfo/list.htm?m=' + Math.random(),
    	    datatype: 'json', 
    	    //postData:{channelName:$(':hidden[name=channelName]').val(),memberId:$(':hidden[name=memberId]').val()},
    	    viewrecords: true,
    		loadonce:false,
    	    height:'300',
    	    width:"800",
    	    colNames:colN,
    		colModel:colM,
    	    pager: "#gridpager",
    	    multiselect : false,
    	    pgbuttons:true,
    	    pginput:true,
    	    caption: "渠道信息管理", 
    	    rowNum:10,
    	    scrollrows:true,//使得选中的行显示到可视区域
    	    rowList: [10,20,50], 
    	    jsonReader: {  
    	        root: "rows",
    	        repeatitems: false  
    	    }
    	});
    	jQuery("#channelinfolist").jqGrid('navGrid', '#gridpager', {
    		add : false,
    		edit : false,
    		del : false
    	});
    }
});
function creatToken(channelId){
	$.ajax({
		url:domain+"/dss/channelinfo/createtoken",
		data:{channelId:channelId},
		type:'post',
		dataType:'json',
		success:function(result){
			if(result.success){
				layer.msg("生成成功", 1,1000);
				$('.querypagebtn').trigger('click');
			}else{
				layer.alert(result.msg.message);
			}
		}
		
	});
}