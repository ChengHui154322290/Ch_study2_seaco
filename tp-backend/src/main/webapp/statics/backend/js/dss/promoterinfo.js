$(function(){
	if($(":text[name='birthday']").is('input')){
		$(":text[name='birthday']").datepicker({format: 'yyyy-mm-dd'});
	}
	$(document.body).on('click','.editpromoterinfobtn', function() {
		var id = $(this).attr('param');
		pageii = $.layer({
			type : 2,
			title : '编辑推广员',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 700 ],
			iframe : {
				src : domain + '/dss/promoterinfo/save?promoterId='+id
			}
		});
	});
	$(document.body).on('click','.editpromotercompanybtn', function() {
		var id = $(this).attr('param');
		pageii = $.layer({
			type : 2,
			title : '编辑分销公司',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 700 ],
			iframe : {
				src : domain + '/dss/promoterinfo/savecompany?promoterId='+id
			}
		});
	});
	$(document.body).on('click','.showpromoterinfobtn', function() {
		var promoterid = $(this).attr('promoterid');
		pageii = $.layer({
			type : 2,
			title : '显示营销人员信息',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 700 ],
			iframe : {
				src : domain + '/dss/promoterinfo/show?promoterId='+promoterid
			}
		});
	});
	
	$(document.body).on('click','.couponlistbtn', function() {
		var promoterid = $(this).attr('promoterid');
		var promoterName = $(this).attr('promotername');
		showTab("promoter_coupon_exchange_btn_" + promoterid, "卡券明细列表", '/topicCoupon/listExchangeCodeDetail?decode=1&promoterId='+promoterid+"&promoterName="+encodeURI(promoterName));
		return false;
	});
	
	$(document.body).on('click','.orderlistbtn', function() {
		var promoterid = $(this).attr('promoterid');
		var promoterName = $(this).attr('promotername');
		var promoterType = $(this).attr('promotertype');
		var param = "";
		if(promoterType==0){
			param = 'promoterId='+promoterid+"&promoterName="+encodeURI(promoterName);
		}else if(promoterType==1){
			param = 'shopPromoterId='+promoterid+"&shopPromoterName="+encodeURI(promoterName);
		}else if(promoterType==2){
			param = 'scanPromoterId='+promoterid+"&scanPromoterName="+encodeURI(promoterName);			
		}
		showTab("order_list_btn_" + promoterid, "订单管理", '/order/list?decode=1&'+param);
		return false;
	});
	$(document.body).on('click','.updatestatusbtn', function() {
		var current_btn = $(this);
		var promoterid = $(this).attr('promoterid');
		var promoterstatus = $(this).attr('promoterstatus');
		var status = 0;
		var statusOpert = '开通';
		var statusText = '未开通';
		if(promoterstatus==0){
			status = 1;
			statusOpert='禁用';
			statusText = '已开通';
		}else if(promoterstatus==1){
			status = 2;
			statusOpert='启用';
			statusText = '禁用';
		}else if(promoterstatus==2){
			status = 1;
			statusOpert='禁用';
			statusText = '已开通';
		}
		$.ajax({
			url:domain + '/dss/promoterinfo/updatepromoterstatus',
			data:{promoterId:promoterid,promoterStatus:status},
			type:'post',
			dataType:'json',
			success:function(result){
				if(result.success){
					current_btn.attr('promoterstatus',status);
					current_btn.attr('value',statusOpert);
					current_btn.parent().prev().prev().prev().text(statusText);
				}
			}
		});
	});
	$(document.body).on('click','.updatepageshowbtn', function() {
		var current_btn = $(this);
		var promoterid = $(this).attr('promoterid');
		var pageshow = $(this).attr('pageshow');
		$.ajax({
			url:domain + '/dss/promoterinfo/updatepageshow',
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
	
	$(document.body).on('click','.childrenlistbtn',function(){
		var promoterid = $(this).attr('promoterid');
		pageii = $.layer({
			type : 2,
			title : '显示下级分销人员信息',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 700 ],
			iframe : {
				src : domain + '/dss/promoterinfo/childrenpage?parentPromoterId='+promoterid
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
	    	passWord: {required:true,minlength:6,maxlength:20,nPassword:true},
	    	confirmPassWord: {required:true,minlength:6,maxlength:20,equalTo:'#passWord'},
	    	gender: {required:true},
	    	birthday: {required:true,date:true},
	    	mobile: {required:true,rangelength:[11,13],mobile:true},
	    	qq: {rangelength:[5,13],qq:true},
	    	weixin: {rangelength:[2,20]},
	    	credentialType: {required:true},
	    	credentialCode: {required:true,rangelength:[15,18],card:true},
	    	bankName: {minlength:4},
	    	bankAccount: {rangelength:[15,20],bankaccount:true}
	    },
	    messages: {
	    	promoterName: {required:'请输入真实姓名',minlength:'请输入真实姓名',maxlength:'请输入真实姓名'},
	    	passWord: {required:'请输入密码',minlength:'密码长度不能小于6个字符',maxlength:'密码长度不能有大于20个字符'},
	    	confirmPassWord: {required:'请输入密码',minlength:'密码长度不能小于6个字符',maxlength:'密码长度不能有大于20个字符',equalTo:'两次输入的密码不一致'},
	    	gender: {required:'请选择性别'},
	    	birthday: {required:'请选择出生日期',date:'出生日期不合法'},
	    	mobile: {required:'请输入手机号',rangelength:'手机号不合法'},
	    	qq: {rangelength:'qq号不合法'},
	    	weixin: {rangelength:'微信号不合法'},
	    	credentialType: {required:'请选择证件类型'},
	    	credentialCode: {required:'请输入证件号码',rangelength:'请输入合法的证件号码'},
	    	bankName: {minlength:'输入的开户银行不合法'},
	    	bankAccount: {rangelength:'输入的银行账号不合法'}
	    }
	});
	
	var validateCompany = $("#savepromotercompanyForm").validate({
		debug: false, //调试模式取消submit的默认提交功能   
        //errorClass: "label.error", //默认为错误的样式类为：error   
        onkeyup: false,   
        submitHandler: function(form){   //表单提交句柄,为一回调函数，带一个参数：form   
        	savePromoterInfo(form);  
        },   
	    rules: {
	    	promoterName: {required:true,minlength:2,maxlength:32},
	    	nickName:{required:true,minlength:2,maxlength:10},
	    	mobile: {required:true,rangelength:[11,13],mobile:true},
	    	bankName: {minlength:4},
	    	bankAccount: {rangelength:[15,20],bankaccount:true},
	    	channelCode:{required:true,rangelength:[2,8],channelCode:true},
	    	companyDssType:{required:true}
	    },
	    messages: {
	    	promoterName: {required:'请输入公司名称',minlength:'请输入公司名称',maxlength:'请输入公司名称'},
	    	nickName: {required:'请输入公司简称',minlength:'请输入公司简称',maxlength:'简称长度应小于10个字符'},
	    	mobile: {required:'请输入联系人手机号',rangelength:'手机号不合法'},
	    	bankName: {minlength:'输入的开户银行不合法'},
	    	bankAccount: {rangelength:'输入的银行账号不合法'},
	    	channelCode:{required:'请输入渠道代码',rangelength:'渠道代码为2~8个小字字母',channelCode:"渠道代码只能输入2~8个小字字母"},
	    	companyDssType:{required:'请选择是否开通分销'}
	    
	    }
	});
	
	$(document.body).on('keyup','#bankAccount',function(){
		var val = $(this).val();
		val = val.replace(/(.{4})/g,'$1 ');
		layer.tips(val,this,{
	        style: ['background-color:#0FA6D8; color:#fff', '#0FA6D8'],
	        maxWidth:280
	    });
	});
	$(document.body).on('keyup','#credentialCode',function(){
		var val = $(this).val();
		val = val.replace(/(.{6})/g,'$1 ');
		layer.tips(val,this,{
	        style: ['background-color:#0FA6D8; color:#fff', '#0FA6D8'],
	        maxWidth:280
	    });
	})
	$(document.body).on('click','.refreshpasswordbtn',function(){
		var password = $('input[name=passWord]').val();
		var confirmPassWord = $('input[name=confirmPassWord]').val();
		if(password==null || $.trim(password)==''){
			layer.alert("密码不能为空", 1);
			return false;
		}
		if(password!=confirmPassWord){
			layer.alert("两次密码输入不一致", 1);
			return false;
		}
		refreshPassword($('#savepromoterinfoForm'));
	});
	if($('.chilrentree').is("table")){
		var colM = [ 
		    {name:'promoterId',index:'promoterId', align:"center", hidden:true},
		    {name:'promoterName',index:'promoterName',align:"center",width:80},
	        {name:'mobile',index:'mobile',align:"center",width:100},
			{name:'orderCount',index:'orderCount',align:"center",width:80},
			{name:'orderAmount',index:'orderAmount',align:"center",width:100},
			{name:'orderCommision',index:'orderCommision',align:"center",width:100}
				];
		var colN = ['ID','姓名','手机', '订单总数','订单金额汇总','订单佣金汇总'];
		
		$("#chilrentree").jqGrid({  
		    treeGridModel: 'adjacency',  
		    //ExpandColumn: 'promoterId',  
		    //ExpandColClick: true,  
		    mtype:'post',
		    url: domain+'/dss/promoterinfo/childrenpage.htm?parentPromoterId='+$(':hidden[name=parentPromoterId]').val()+'&m=' + Math.random(),
		    datatype: 'json', 
		    viewrecords: true,
			loadonce:false,
		    height:'400',
		    width:"650",
		    colNames:colN,
			colModel:colM,
		    pager: "#gridpager",
		    multiselect : false,
		    pgbuttons:true,
		    pginput:true,
		    caption: "下级分销员信息列表", 
		    rowNum:10,
		    scrollrows:true,//使得选中的行显示到可视区域
		    subGrid: true,
		    sortname : 'promoterId',
		    rowList: [10,20,50], 
		    jsonReader: {  
		        root: "rows",
		        repeatitems: false  
		    },
			subGridRowExpanded: function(subgrid_id, row_id) {
		            var subgrid_table_id;  
		            subgrid_table_id = subgrid_id + "_t";
		            var subgrid_pager_id;  
		            subgrid_pager_id = subgrid_id + "_pgr" ; 
		            var rowData = $("#chilrentree").jqGrid('getRowData',row_id);
		            $("#" + subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll' width='96%'></table><div id='"+subgrid_pager_id+"' class='scroll' width='96%'></div>");  
		            $("#" + subgrid_table_id).jqGrid({  
		                url: domain+'/dss/promoterinfo/childrenpage.htm?parentPromoterId='+rowData.promoterId+'&m=' + Math.random(),
		                datatype: "json",
		                mtype:'post',
		    		    width:"600",
		                colNames: colN,  
		                colModel: colM,  
		                rowList: [10,20,50], 
		                jsonReader: {
		                    root:"rows",  
		                    records: "records",  
		    		        repeatitems: false  
		                },  
		                pager: subgrid_pager_id,  
		                viewrecords: true,  
		                height: "100%"
		           });  
		    }  
		});
		
		jQuery("#chilrentree").jqGrid('navGrid', '#gridpager', {
			add : false,
			edit : false,
			del : false
		});
	}
});

function savePromoterInfo(form){
	$.ajax({
        cache: true,
        type: "POST",
        url:domain + '/dss/promoterinfo/save',
        data:$(form).serialize(),
        async: false,
        error: function(request) {
        	layer.alert("Connection error");
        },
        success: function(result) {
        	if(result.success){
        		layer.alert("保存成功", 1, function(){
        			parent.window.location.reload();
        			parent.layer.close(parent.pageii);
                })
        	}else{
        		layer.alert(result.msg.message, 8);
        	}
        }
    });
}
function refreshPassword(form){
	$.ajax({
        cache: true,
        type: "POST",
        url:domain + '/dss/promoterinfo/updatepassword',
        data:$(form).serialize(),
        async: false,
        error: function(request) {
            alert("Connection error");
        },
        success: function(result) {
        	if(result.success){
        		layer.alert("更新密码成功", 1, function(){
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

function IsNum(){
	  var value=$("#commisionRate").val();
	  var reg=/^(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*$/i;
	  if(reg.test(value)){
	  	$("#commisionRate").val('');
	  	layer.alert("请输入数字！");
	  }
	}
