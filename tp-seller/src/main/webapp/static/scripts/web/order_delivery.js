jQuery(function(){
		$("#expressSelect").select2({
		 	placeholder: "请选择快递公司",
		 	allowClear: true
		});
	jQuery("#exportOrder").click(function(){//点击导出按钮时将所有选中的checked的值放入数组中
		if(!jQuery("#colKeySize").val()){
			alertMsg("请新增导出列！");
			return false;
		}
		if(!jQuery("#expressSelect").val()){
			alertMsg("请选择物流。");
			return false;
		}
		if(jQuery("#totalCount").val()==0){
			alertMsg("暂无未发货订单");
			return false;
		}
		
		var chk_value =[];
		$('input[name="sellerbox"]:checked').each(function(){    
            chk_value.push($(this).val()); 
        }); 
        var expressCode = jQuery("#expressSelect").val();
        var expressName = jQuery("#expressSelect option:selected").text();
        var orderStr = chk_value.join(",");
        
        var form=$("<form>");//定义一个form表单
        form.attr("style","display:none");
        //form.attr("target","");
        form.attr("method","post");
        form.attr("action","/seller/delivery/exportOrder.htm");
        $("body").append(form);//将表单放置在web中
        var input1=$("<input>");
        input1.attr("type","hidden");
        input1.attr("name","orderStr");
        input1.attr("value",orderStr);
        form.append(input1);
        input1=$("<input>");
        input1.attr("type","hidden");
        input1.attr("name","expressCode");
        input1.attr("value",expressCode);
        form.append(input1);
        input1=$("<input>");
        input1.attr("type","hidden");
        input1.attr("name","expressName");
        input1.attr("value",expressName);
        form.append(input1);
        form.submit();//表单提交 
	});
	
	jQuery("#importOrder").click(function(e){
		e.preventDefault();
		var filePath = jQuery("#ftltemple").val();
		if(!filePath){
			alertMsg("请选择要上传的XLS文件。");
			return false;
		}
		var format = getFileFormat(filePath);
		if(!format || "XLS" != (format.toString().toUpperCase())){
			alertMsg("文件格式必须为XLS。");
			return false;
		}
		jQuery("#importForm").ajaxSubmit({
			beforeSend: function(XMLHttpRequest){
				popWaitDivMap.showWaitDiv('正在导入，请稍候...');
			},
			dataType:'html',
			success:function(data){
				popWaitDivMap.hideWaitDiv();
				$.layer({
				    type: 1,   
				    area: ['821px', '400px'],
//				    shade: [0],  //不显示遮罩
				    border: [10], //不显示边框
				    title: [
				        '导入发货订单信息',
				        //自定义标题风格，如果不需要，直接title: '标题' 即可
				    ],
				    bgcolor: '#eee', //设置层背景色
				    page: {
				        html: '<div>'+data+'</div>'
				    },
//				    shift: 'top' //从上动画弹出
				}); 
				
			},
			complete: function(XMLHttpRequest, textStatus){
		    }
		});
		return false;
	});		
	jQuery("#exportExpress").click(function(){//点击导出按钮时将所有选中的checked的值放入数组中
        
        var form=$("<form>");//定义一个form表单
        form.attr("style","display:none");
        //form.attr("target","");
        form.attr("method","post");
        form.attr("action","/seller/delivery/exportExpress.htm");
        $("body").append(form);//将表单放置在web中
        form.submit();//表单提交 
	});
	jQuery("#hrefEditCols").click(function(){
		var exportInfoId =$("#exportInfoId").val();
		jQuery.ajax({
			type: 'post',
			url: '/seller/delivery/editCol.htm',
			data:{"exportInfoId":exportInfoId},
			dataType:"html",
			success: function(data){
				var layerTab = $.layer({
				    type: 1,   
				    area: ['auto', 'auto'],
				    border: [10],
				    title: [
				        '导出列名称表',
				    ],
				    bgcolor: '#eee', 
				    page: {
				        html: '<div>'+data+'</div>'
				    },
				}); 
				registeSubPageEvent(layerTab);
			}
		});
		
	});
	jQuery("#hrefAddCols").click(function(){
		var exportInfoId =$("#exportInfoId").val();
		jQuery.ajax({
			type: 'post',
			url: '/seller/delivery/editCol.htm',
			data:{"exportInfoId":exportInfoId},
			dataType:"html",
			success: function(data){
				var layerTab = $.layer({
				    type: 1,   
				    area: ['auto', 'auto'],
				    border: [10],
				    title: [
				        '导出列名称表',
				    ],
				    bgcolor: '#eee', 
				    page: {
				        html: '<div>'+data+'</div>'
				    },
				}); 
				registeSubPageEvent(layerTab);
			}
		});
		
	});
	
	function registeSubPageEvent(layerTab){
		jQuery("#saveCol").click(function(){
			var chk_value =[];
			$('input[name="sellerbox"]:checked').each(function(){    
	            chk_value.push($(this).val()); 
	        }); 
	        var exportCol = chk_value.join(",");
	        
	    	var exportInfoId =$("#exportInfoId").val();
	    	jQuery.ajax({
	    		url: '/seller/delivery/saveCol.htm',
	    		type: 'post',
	    		data:{"exportInfoId":exportInfoId,"exportCol":exportCol},
	    		dataType:"json",
	    		success: function(data){
	    			if(data.success){
	    				layer.close(layerTab);
	    				refreshTab("tab3","/seller/delivery/deliveryIndex");
	    			};
	    		}
	    	});
		});
		jQuery("#cancelCol").click(function(){
			layer.close(layerTab);
		});
	}	
});



