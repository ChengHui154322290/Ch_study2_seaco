/**
 * 商品控制...
 */
var pageii;
$(function(){
	$('.itemSkubtn').on('click',function(){
		  pageii=$.layer({
            type : 2,
            title: '商品对应的Sku信息->编辑',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['400px', 400],                     
            iframe: {
                src : domain+'/seller/item/skuAdd.htm'
            } 
        });
	}); 
	
	//新建prdid
	$('#newPrdidBtn').on('click',function(){
		  var smallId = $("#smallIdSel").val();
		  if(smallId==null||smallId==""){
			  alert("请先选择商品分类");
			  return;
		  }
		  pageii=$.layer({
            type : 2,
            title: '商品-选择规格',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['600px', 400],                     
            iframe: {
                src : domain+'/seller/item/addSpec.htm?smallId='+smallId
            } 
        });
	});
	//清空prdid列表
	$('#clearPrdidListBtn').on('click',function(){
		$('#skuListTable').empty();
	});
	
	//取消按钮
	$('.closebtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
	// 删除sku行
	$(".deleteSkuListItem").on("click", function() {
		var $this = $(this);
		$this.closest("tr").detach();
	});
	
	$(".newStorageBtn").on("click", function() {
		var $this = $(this);
		var resHtml = appendToStorageList($this.attr('color'),$this.attr('size'),$this.attr('id').split('_')[1],_storageListIndex);
		_storageListIndex++;
		$('#storageListTable').append(resHtml);
	});
		
	// 增加商品属性
	var attrListIndex = 0 ;
	$('#newAttrItem').click(function() {
		 //添加前清空列表
		 $('#clearPrdidListBtn').click();
			var trHtml = 
			'<tr>'
				/*+'<td width="15">'
				+(attrListIndex+1)
				+'</td>'*/
				+'<td width="60">'
				+	'<input type="text" name="attrList['+attrListIndex+'].attrKey" class="input-text lh30" size="20" />'
				+'</td>'
				+'<td width="100">'
				+	'<input type="text" name="attrList['+attrListIndex+'].attrValue" class="input-text lh30" size="20" />'
				+'</td>'
				+'<td width="200">'
				+'	<a href="javascript:;" class="deleteAttrListItem">删除</a>'
				+'</td>'+
			'</tr>';
		$('#attrListTable').append(trHtml);
		attrListIndex ++;
	});	
	
	
	// 删除商品对应的供应商 
	$(".deleteSuppListItem").on("click", function() {
		var $this = $(this);
		$this.closest("tr").remove();
	});
	
	
	//处理字段保存
	handSave();
	/**
	 * 处理保存事件。
	 */
	function handSave(){
		$("#dataFormSave").click(function(){
			//校验	小类必须输入
			var smallId = $('#smallIdSel').val();
			if(smallId==""){
				alert("请选择商品类别");
				return ;
			}
			var spuId = $('#spuId').val();
			var detailAry = null;
			if(spuId!==undefined){
				detailAry = getUpdPrdIds();				
			}else{
			    detailAry = getPrdIds();
			}
			var infoJson = getInfo();
			var info= $.extend({}, info, infoJson);
			var infoJson=JSON.stringify(info);
			var detailJson=JSON.stringify(detailAry);
			$.post("/seller/item/save.htm",{info:infoJson,details:detailJson},showResponse,"text");
		});
	}
	
	/**
	 * 显示返回结果。
	 */
	function showResponse(data){
		var obj=eval('('+data+')');
		newTab("item_sku","新增prd","/seller/item/queryPrd.htm?itemId="+obj.data);
		if(obj.success){//成功
			layer.alert('操作成功', 8);
	    }else{
	    	layer.alert(obj.msg.message, 8);
	    }			
	};
	
});

/**
 * 获取info头信息
 * @returns
 */
function getInfo(){
	var id=$.trim($("#spuId").val());
	var largeId=$.trim($("#largeIdSel").val());
	var mediumId=$.trim($("#mediumIdSel").val());
	var smallId=$.trim($("#smallIdSel").val());
	var unitId=$.trim($("#unitId").val());
	var brandId=$.trim($("#brandIdSel").val());
	var mainTitle=$.trim($('#mainTitle').val());
	var remark=$.trim($('#spuRemark').val());
	var supplierId=$.trim($('#supplierId').val());
	var infoJson={largeId:largeId,mediumId:mediumId,smallId:smallId,unitId:unitId,
			brandId:brandId,mainTitle:mainTitle,remark:remark,id:id,supplierId:supplierId};
	var info= $.extend({}, info, infoJson);
	return info;
	
}

/**
 * 新增
 * @returns {Array}
 */
function getPrdIds(){
	var prdIdAry = new Array();
	$('.prdidsList').each(function(){
		var barcode = $.trim($(this).find('[name=barcode]').val());
		var mainTitle = $.trim($(this).find('[name=mainTitle]').val());
		var specGroupIdArray = new Array();
		$(this).find('[name=specGroupId]').each(function(){
			specGroupIdArray.push($(this).val());
		});
		var specGroupIds = specGroupIdArray.join(',');
		
		var specIdArray = new Array();
		$(this).find('[name=specId]').each(function(){
			specIdArray.push($.trim($(this).val()));
		});
		var specIds = specIdArray.join(',');
		var detailJson ={barcode:barcode,mainTitle:mainTitle,specGroupIds:specGroupIds,specIds:specIds};
		prdIdAry.push(detailJson);
	});
	return prdIdAry;
}

/**
 * 修改
 * @returns {Array}
 */
function getUpdPrdIds(){
	var prdIdAry = new Array();
	$('.prdidsList').each(function(){
		var barcode = $.trim($(this).find('[name=barcode]').val());
		var mainTitle = $.trim($(this).find('[name=mainTitle]').val());
		var id = $.trim($(this).find('[name=id]').val());
		var detailJson ={barcode:barcode,mainTitle:mainTitle,id:id};
		prdIdAry.push(detailJson);
	});
	return prdIdAry;
}
