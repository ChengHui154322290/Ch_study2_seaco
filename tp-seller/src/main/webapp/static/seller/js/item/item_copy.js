/**
 * 主要实现商品复制
 */
var pageii;
$(function(){
	$('#copyPrdBtn').bind('click',function(){
	  var picLength = parent.$('.item-picture').find("input[name='picList']").length;
	  if(picLength>10||picLength<3){
		  alert("请选择上传3-10张图片,再复制");
		  return ;
	  }
	  parent.mobileEditor.sync(); 
	  var mobileEditorVal = $.trim(parent.$("#mobileEditor").val());
	  if(mobileEditorVal==""){
		  alert("请填写手机模板的内容,再复制");
		  return ;
	  }
	  parent.editor.sync(); 
	  var editorVal = $.trim(parent.$("#editor").val());
	  if(editorVal==""){
		  alert("请填写PC模板的内容,再复制");
		  return ;
	  }
	  
	  if($("input[name='detailId']:checked").length < 1){
		 alert("请选择PRDID,再复制");
		 return ;
	  }
	  
	  var itemId = parent.$('#spuId').val();
	  var detailId = $('#detailId').val();
	  var detailIdAry = new Array();
	  $("input[name='detailId']:checked").each(function(){
		  detailIdAry.push($(this).val());
	  });
	  var detailIds=JSON.stringify(detailIdAry);
	  
	  var picAry = new Array();
	  parent.$('.item-picture').find("input[name='picList']").each(function(){
		  var pic = $(this).val();
		  picAry.push(pic);
	  });
	  var pics=JSON.stringify(picAry);
	  $.post("copyPicAndDetail.htm",{itemId:itemId,detailId:detailId,detailIds:detailIds,pics:pics,desc:editorVal,descMobile:mobileEditorVal},showResponse,"text");
	 
	});

	
	$('#copyPicAndDetail').on('click',function(){
		  var detailId = $("#detailId").val();
		  pageii=$.layer({
            type : 2,
            title: '复制图片和详情到同一Spu下面的不同Prd',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['900px', 500],                     
            iframe: {
                src : domain+'/seller/item/listPrdid.htm?detailId='+detailId
            } 
        });
	}); 
	
	/**
	 * 父选子
	 * 全选/取消全选
	 */
	$('#chkall').on('click',function() {
		if (this.checked == true) {
			$('.chkall').attr("checked", true);
		    var isChecked = $(this).prop("checked");
			    $("input[name='detailId']").prop("checked", isChecked);
			
		} else {
			$('.chkall').attr("checked", false);
			$("input[name='detailId']").attr("checked", false);
		}
	});
	
	/**
	 * 显示返回结果。
	 */
	function showResponse(data){
		var obj=eval('('+data+')');
		if(obj.success){//成功
			layer.alert('操作成功', 4,function(){
				location.href='listPrdid.htm?detailId='+obj.data;
			});
	    }else{
	    	layer.alert(obj.msg.message, 8);
	    }			
	};
	
});

