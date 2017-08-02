var increaseType ="1";
var decreaseType ="2";   
var saveUrl = "/storage/inventoryAdjust/adjustLog.htm"; 
$(function(){
	$("#searthAtt").on('click',function(){
			queryAttForm.submit();     
	});
	
	
	$(".exportButton").on('click',function(){
		
		    $("#queryAttForm").attr("action",domain+"/storage/inventoryAdjust/export.htm");
			queryAttForm.submit();   
		    $("#queryAttForm").attr("action",domain+"/storage/inventoryAdjust/list.htm");
	});
	
	$('.logAddButton').live('click',function(){				
		  pageii=$.layer({
          type : 2,
          title: '库存盘点->盘点',
          shadeClose: true,
          maxmin: true,
          fix : false,  
          area: ['450px', 300],                     
          iframe: {
              src : domain+'/storage/inventoryAdjust/add.htm'
          } 
      });
	}); 
	
	
	
	$("#adjustIncrease").on('click',function(){
		$("#action").val(increaseType);
		if(validationParam()){
				adjustSubmit();
		}
	});
	
	$("#adjustDecrease").on('click',function(){  
		$("#action").val(decreaseType);
		if(validationParam()){
				adjustSubmit();
		}
	});
	
	
	function validationParam(){
		if( $("#sku").val()== null ||  $("#sku").val() ==""){
			layer.alert("请输入sku.");
			return false;
		}
		if($("#warehouseId").length==0){
			layer.alert("请输入仓库.");
			return false;
		}
		if($("#quantity").val() == null  || $("#quantity").val()==""){
			layer.alert("请输入盘点数量。");
			return false;
		} 
		var pattern=/^[1-9]\d*$/;
		if(!pattern.test($("#quantity").val())){
			layer.alert("请输入正确盘点数量。");   
			return false;
		}
		
		if(+$("#quantity").val() > 2147483647){  
			layer.alert("输入数量超过最大值。");   
			return false;
		}
		   
		return true;
	}
	
	
	function adjustSubmit(){
		$.ajax({
	        cache: true,
	        type: "POST",
	        url:domain+saveUrl,
	        data:$('#adjustLog').serialize(),
	        async: false,
	        error: function(request) {
	            alert("Connection error");
	        },
	        success: function(data) {    	
	        	if(data.success){
	        		alert(data.data);
	        	   	//清苦input
	        		$("input").not(".btn").val("");
		        	$("#remark").val("");
		        	$("#warehouseList").html('请选择仓库');
	        	}else{
	        		layer.alert(data.msg.message, 8);		
	        	}
	        }
	         
	    });
	}
	
	$( "#startDate" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	    }
	});
	
	$( "#endDate" ).datepicker({ 
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	    }
	});

	$("#selectWarehouseBtn").click(function(){
		var sku = $("#sku").val();
		if(sku.length==0){
			layer.alert("请输入sku", 8);		
			return;
		}
		var url=domain+"/storage/inventoryAdjust/queryWarehouseBySkuCode.htm";
		$.get(url,{sku:sku},function(data){
			if(data){
				var warehouseList = $("#warehouseList");
				var selectWarehouse = $("<select />");
				selectWarehouse.attr('name','warehouseId');
				selectWarehouse.attr('id','warehouseId');
				$.each(data,function(i,n){
					var opt = $("<option />");
					opt.val(n.warehouseId);
					var inventory = n.inventory;
					if(inventory==null){
						inventory = "无";
					}
					opt.html(n.warehouseName+" 当前库存："+inventory);
					selectWarehouse.append(opt);
				});
				warehouseList.html(selectWarehouse);
			}
		});
	});
	
}); 