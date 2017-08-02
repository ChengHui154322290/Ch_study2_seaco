var pageii;

var saveUrl = "/item/itemDetailSales/adjustDetailSales.htm"; 
var saveCopyPRDIDUrl="/item/itemDetailSales/insertWithPRDIDS.htm";
var saveCopyBarcodeUrl="/item/itemDetailSales/insertWithBarcodes.htm";
$(function(){
	/**销售信息查询**/
	$("#searthAtt").on('click',function(){
			queryAttForm.submit();      
	});
	
	$("#reset").on('click',function(){
		console.log(111);  
		$("input[name='barcode']").val("");  
		$("input[name='prdid']").val("");   
	});
	
	$(".exportButton").on('click',function(){
		
		if($(":checkbox[name='exportIds']:checked").length >0){
			  $("#queryAttForm").attr("action",domain+"/item/itemDetailSales/exportWithCondition.htm");
				queryAttForm.submit();   
		    $("#queryAttForm").attr("action",domain+"/item/itemDetailSales/list.htm");
		}else{
			alert("请选择需要导出的数据");
			return
		}
	});
	$(".exportButtonAll").on('click',function(){
		  $("#queryAttForm").attr("action",domain+"/item/itemDetailSales/exportAll.htm");
			queryAttForm.submit();   
	    $("#queryAttForm").attr("action",domain+"/item/itemDetailSales/list.htm");
	});
	
	$("a.checkAll").on('click',function(){
		$(":checkbox").attr("checked", true);  
	});
	$("a.disCheck").on('click',function(){
		$(":checkbox").attr("checked", false);  
	});
	
	$('a.viewDetail').live('click',function(){
		var viewId = $(this).attr('viewid');
		if($(this).attr('viewid').length > 0){
			  pageii=$.layer({
		          type : 2,
		          title: '已囤数据->查看',
		          shadeClose: true,
		          maxmin: true,
		          fix : false,  
		          area: ['800px', 500],                            
		          iframe: {
		              src : domain+'/item/itemDetailSales/viewDetailSales.htm?id='+viewId
		          } 
		      });
		}
	}); 
	
	
	$('a.updateDetail').live('click',function(){
		var viewId = $(this).attr('viewid');
		if($(this).attr('viewid').length > 0){
			  pageii=$.layer({
		          type : 2,
		          title: '已囤数据->修改',
		          shadeClose: true,
		          maxmin: true,
		          fix : false,  
		          area: ['800px', 500],                            
		          iframe: {
		              src : domain+'/item/itemDetailSales/viewDetailSalesForUpdate.htm?id='+viewId
		          } 
		      });
		}
	}); 
	$('.barcodecopy').live('click',function(){
		  pageii=$.layer({
	          type : 2,
	          title: '粘贴导入（<font color="red">按条码</font>）',
	          shadeClose: true,
	          maxmin: true,
	          fix : false,  
	          area: ['800px', 500],                            
	          iframe: {
	              src : domain+'/item/itemDetailSales/copyBarcode.htm'
	          } 
	      });
	});
	$('.prdidcopy').live('click',function(){
		  pageii=$.layer({
	          type : 2,
	          title: '粘贴导入（<font color="red">按PRDID</font>）',
	          shadeClose: true,
	          maxmin: true,
	          fix : false,  
	          area: ['800px', 500],                            
	          iframe: {
	              src : domain+'/item/itemDetailSales/copyPRDID.htm'
	          } 
	      });
	});
	
	
	$(".updateDetailBtn").on('click',function(){
		$.ajax({
	        cache: true,
	        type: "POST",
	        url:domain+saveUrl,
	        data:$('#updateDetailSalesForm').serialize(),
	        async: false,
	        error: function(request) {
	            alert("Connection error");
	        },
	        success: function(data) {    	
	        	if(data.success == false){
	        		alert(data.msg.detailMessage);  
	        		parent.layer.close(parent.pageii);
	        		parent.reload();  
	        	}else{
	        		layer.alert(data.data, 8);		
	        	}
	        }
	         
	    });
	});
	
	$(".saveCopyPRDID").on('click',function(){
		$.ajax({
	        cache: true,
	        type: "POST",
	        url:domain+saveCopyPRDIDUrl,
	        data:$('#saveCopyPRDID').serialize(),
	        async: false,
	        error: function(request) {
	            alert("Connection error");
	        },
	        success: function(data) {    	
	        	if(data.success==false){
	        		alert(data.msg.detailMessage);  
	        		parent.layer.close(parent.pageii);
	        	}else{
	        		layer.alert(data.data, 8);		
	        	}
	        }
	         
	    });
	});
	
	
	$(".saveBarcodeList").on('click',function(){  
		$.ajax({
	        cache: true,
	        type: "POST",
	        url:domain+saveCopyBarcodeUrl,
	        data:$('#saveCopyBarcodeForm').serialize(),
	        async: false,
	        error: function(request) {
	            alert("Connection error");
	        },
	        success: function(data) {    	
	        	if(data.success==false){
	        		alert(data.msg.detailMessage);  
	        		parent.layer.close(parent.pageii);
	        	}else{
	        		layer.alert(data.data, 8);		
	        	}
	        }
	         
	    });
	});
	
	
	//取消按钮
	$('.closelayerbtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
	
}); 