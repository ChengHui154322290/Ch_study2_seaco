$(function(){
	if($('#userlist').is("table")){
		var colM = [ 
		    {name:'userName',index:'userName', align:"center",width:"100"},
		    {name:'mobile',index:'mobile',align:"center",width:"120"},
	        {name:'warehouseName',index:'warehouseName',align:"center",width:"240"},
			{name:'userTypeCn',index:'userType',align:"center",width:"80"},
			{name:'shopTypeCn',index:'shopType',align:"center",width:"80"},
			{name:'enabledCn',index:'enabled',align:"center",width:"80"},
			{name:'fastUserId',index:'fastUserId',align:"center",width:"100"
				,formatter:function(cellValue, options, rowObject){
					var _void = "javascript:void(0);";
					return "<a href='"+_void+"' class='savebtn' fastuserid='"+rowObject.fastUserId+"'>编辑</a>　" +
						   "<a href='"+_void+"' class='enabledbtn' fastuserid='"+rowObject.fastUserId+"' userenabled='"+rowObject.enabled+"'>"+(1==rowObject.enabled?"禁用":"启用")+"</a>";
				}
			}
		   ];
		var colN = ['用户姓名','手机号','店铺名称','用户类型','店铺类型', '是否可用','操作'];
		
		$("#userlist").jqGrid({  
		    treeGridModel: 'adjacency',  
		    mtype:'post',
		    url: domain+'/fast/user/list.htm?m=' + Math.random(),
		    datatype: 'json', 
		    postData:{
		    	userType:$('select[name=userType]').val(),
		    	shopType:$('select[name=shopType]').val(),
		    	warehouseId:$('select[name=warehouseId]').val(),
		    	enabled:$('select[name=enabled]').val()},
		    height:'350',
		    width:"800",
		    colNames:colN,
			colModel:colM,
		    pager: "#gridpager",
		    multiselect : false,
		    pgbuttons:true,
		    pginput:true,
		    caption: "速递用户管理", 
		    rowNum:10,
		    scrollrows:true,//使得选中的行显示到可视区域
		    rowList: [10,20,50], 
		    jsonReader: {  
		        root: "rows",
		        repeatitems: false  
		    }
		});
		jQuery("#userlist").jqGrid('navGrid', '#gridpager', {
			add : false,
			edit : false,
			del : false
		});
	}
	$(document.body).on('click','.searchbtn',function(){
		jQuery("#userlist").jqGrid(
				'setGridParam',
				{
					url : domain+'/fast/user/list.htm?m=' + Math.random(),
					postData:{
				    	shopType:$('select[name=shopType]').val(),
						userType:$('select[name=userType]').val(),
						warehouseId:$('select[name=warehouseId]').val(),
						enabled:$('select[name=enabled]').val()
						},
				}).trigger('reloadGrid');
	});

	$(document.body).on('click','.savebtn', function() {
		var fastuserid = $(this).attr('fastuserid');
		pageii = $.layer({
			type : 2,
			title : '速递用户编辑',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 400 ],
			iframe : {
				src : domain + '/fast/user/save?fastUserId='+fastuserid
			}
		});
	});
	
	$(document.body).on('click','.enabledbtn', function() {
		var currentbtn = $(this);
		var fastuserid = currentbtn.attr('fastuserid');
		var enabled = currentbtn.attr("userenabled");
		$.ajax({
			url:domain+'/fast/user/enableduser',
			type:'post',
			data:{'fastUserId':fastuserid,enabled:(1==enabled?"0":"1")},
			dataType:'json',
			success:function(result){
				if(result.success){
					$("#userlist").jqGrid('setGridParam').trigger('reloadGrid');
				}else{
					layer.alert(result.msg.message,2);
				}
			}
		});
	});
	$(document.body).on('click','.editbtn', function() {
		var currentbtn = $(this);
		$.ajax({
			url:domain+'/fast/user/save',
			type:'post',
			data:$('#saveuserform').serialize(),
			dataType:'json',
			success:function(result){
				if(result.success){
					layer.msg("编辑成功",1,2);
					parent.location.reload();
        			parent.layer.close(parent.pageii);
				}else{
					layer.alert(result.msg.message);
				}
			}
		});
	});
	$(document.body).on('change','select[name=shopType]',function(){
		var shopType = $(this).val();
		$.ajax({
			url:domain+'/fast/user/querywarehouse',
			type:'post',
			data:{shopType:shopType},
			dataType:'json',
			success:function(result){
				if(result.success){
					$('select[name=warehouseId]').empty();
					$('select[name=warehouseId]').append('<option value="">请选择</option>');
					for(var i=0;i<result.data.length;i++){
						$('select[name=warehouseId]').append('<option value="'+result.data[i].id+'">'+result.data[i].name+'</option>');
					}
				}else{
					$('select[name=warehouseId]').empty();
					$('select[name=warehouseId]').append('<option value="">请选择</option>');
				}
			}
		});
	})
});
