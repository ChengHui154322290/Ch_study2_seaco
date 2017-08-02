$(function(){
	$(document.body).on('click','.editpromoterinfobtn', function() {
		var id = $(this).attr('param');
		pageii = $.layer({
			type : 2,
			title : '编辑扫码推广员',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 700 ],
			iframe : {
				src : domain + '/dss/scan/save?promoterId='+id
			}
		});
	});
	$(document.body).on('click','.updatepageshowbtn', function() {
		var current_btn = $(this);
		var promoterid = $(this).attr('promoterid');
		var pageshow = $(this).attr('pageshow');
		$.ajax({
			url:domain + '/dss/scan/save',
			data:{promoterId:promoterid,pageShow:pageshow==0?1:0},
			type:'post',
			dataType:'json',
			success:function(result){
				if(result.success){
					current_btn.attr('pageshow',pageshow==0?1:0);
					current_btn.attr('value',pageshow==0?'关闭':'开启');
				}
			}
		});
	});
	
	
	var validate = $("#savepromoterinfoForm").validate({
		debug: false, //调试模式取消submit的默认提交功能   
        //errorClass: "label.error", //默认为错误的样式类为：error   
        onkeyup: false,   
        submitHandler: function(form){   //表单提交句柄,为一回调函数，带一个参数：form   
        	savePromoterInfo(form);  
        },   
	    rules: {
	    	promoterName: {required:true,minlength:2,maxlength:32,realName:true},
	    	promoterLevel:{required:true},
	    	referralUnit: {required:true,money:true},
	    	commisionRate:{required:true,max:90,money:true}
	    },
	    messages: {
	    	promoterName: {required:'请输入公司名或姓名',minlength:'请输入公司名或姓名',maxlength:'请输入公司名或姓名'},
	    	passWord: {required:'请选择推广员类型'},
	    	referralUnit: {required:'请输入拉新佣金'},
	    	commisionRate: {required:'请输入返佣比例',max:'返佣比例不能大于90'}
	    }
	});
	
	
	if($('#promoterinfolist').is("table")){
		var colM = [ 
		    {name:'promoterId',index:'promoterId', align:"center", hidden:true},
		    {name:'promoterName',index:'promoterName',align:"center",width:80},
	        {name:'promoterLevelCn',index:'promoterLevelCn',align:"center",width:100},
	        {name:'inviteCode',index:'inviteCode',align:"center",width:100},
			{name:'inviteCodeUsedCn',index:'inviteCodeUsedCn',align:"center",width:80}
				];
		var colN = ['ID','公司名或姓名','类型', '邀请码','使用类型'];
		
		$("#promoterinfolist").jqGrid({
			treeGridModel: 'adjacency',  
		    mtype:'post',
		    url: domain+'/dss/scan/list.htm',
		    datatype: 'json', 
		    postData: {
		    		promoterName:$(':text[name=promoterName]').val(),
		    		inviteCodeUsed:$('select[name=inviteCodeUsed]').val()
		    		},
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
		    caption: "扫码分销员列表", 
		    rowNum:10,
		    scrollrows:true,//使得选中的行显示到可视区域
		    sortname : 'surplusAmount',
		    rowList: [10,20,50], 
		    jsonReader: {  
		        root: "rows",
		        repeatitems: false  
		    }
		});
		
		jQuery("#promoterinfolist").jqGrid('navGrid', '#gridpager', {
			add : false,
			edit : false,
			del : false
		});
	}
	$("#promoterinfolist").setGridWidth($(window).width()*0.98);
	$(document.body).on('click','.querypagebtn',function(){
		jQuery("#promoterinfolist").jqGrid(
				'setGridParam',
				{
					url : domain+'/dss/scan/list.htm?m=' + Math.random(),
					postData:{
						promoterName:$(':text[name=promoterName]').val(),
						inviteCodeUsed:$('select[name=inviteCodeUsed]').val()
			    	}
				}).trigger('reloadGrid');
	});
});

function savePromoterInfo(form){
	$.ajax({
        cache: true,
        type: "POST",
        url:domain + '/dss/scan/save',
        data:$(form).serialize(),
        async: false,
        error: function(request) {
        	layer.alert("Connection error");
        },
        success: function(result) {
        	if(result.success){
        		layer.alert("生成邀请码为<br/>"+result.data.inviteCode, 1, function(){
        			parent.window.location.reload();
        			parent.layer.close(parent.pageii);
                })
        	}else{
        		layer.alert(result.msg.message, 8);
        	}
        }
    });
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
