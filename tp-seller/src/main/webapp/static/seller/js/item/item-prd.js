/***
 * 新增prd的逻辑 
 */
$(function(){
	
	
	$('.deletePrdBtn').on('click',function(){
		var $this = $(this);
		$this.closest("tr").detach();
	});
	
	$('#backBtn').on('click',function(){
		newTab("item_sku","商品管理列表","/seller/item/list.htm");
	});
	
	
	//新建prdid
	$('#newPrdBtn').on('click',function(){
		 var specGroupIds = $(this).attr('params');
		  pageii=$.layer({
            type : 2,
            title: '商品-选择规格',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['400px',350],    
            offset: ['0px', 'auto'],
            iframe: {
                src : domain+'/seller/item/getSpecByItemId.htm?specGroupIds='+specGroupIds
            } 
        });
	});
	
	//处理字段保存
	handSave();
	
	function handSave(){
		$("#saveItemPrdsBtn").click(function(){
			//校验	小类必须输入
			var smallId = $('#smallIdSel').val();
			if(smallId==""){
				alert("请选择商品类别");
				return ;
			}
			var detailAry = null;
			detailAry = getPrdIds();
			var infoJson = getInfo();
			var info= $.extend({}, info, infoJson);
			var infoJson=JSON.stringify(info);
			var detailJson=JSON.stringify(detailAry);
			$.post("/seller/item/saveItemPrds.htm",{info:infoJson,details:detailJson},showResponse,"text");
		});
	}
	
	
	
	/**
	 * 显示返回结果。
	 */
	function showResponse(data){
		var obj= eval('('+data+')');;
		newTab("item_sku","新增prd","/seller/item/queryPrd.htm?itemId="+obj.data);
		if(obj.success){//成功
			layer.alert('操作成功', 8);
	    }else{
	    	layer.alert(obj.msg.message, 8);
	    }			
	};
	
	
	//prd新增
	$("#chooseSpecBtn").on('click',function(){
		var barcode = $("#barcode").val();
		var mainTitle = parent.$("#mainTitle").val();
		var specTds = "";
		var flag = true;
		
		if(barcode == '') {
			alert('请输入barcode');
			$("#barcode").focus();
			return ;
		}
		
		var itemSpec = new Array();
		var selectSpec = new Array();
		//每一个prd的规格都比较一下
		parent.$('.prdidsList').each(function(){
			var specAry = new Array();
			$(this).find('[class=specIdInfo]').each(function(){
				var specGroupId = $(this).attr('name');
				specAry.push(specGroupId);
			});
			itemSpec.push(specAry);
		});
		
		$('.specGroupSelect').each(function(){
			if($(this).attr('name')=='supplierId'){
				var specId =$(this).find("option:selected").val() ;
				var specGroupId = $(this).find("option:selected").attr("specGroupId") ;
				if(specId == ''){
					alert('请选择规格！');
					flag = false;
					return ;
				}else{
					var temp = specGroupId+"_"+specId;
					selectSpec.push(temp);
				}
			}
		});
		if(flag && checkPrdSpec(selectSpec,itemSpec)){
			$('.specGroupSelect').each(function(){
				if($(this).attr('name')=='supplierId'){
					var specId =$(this).find("option:selected").val() ;
					specTds += newSpec($(this).find("option:selected").attr("specGroupId")
								,specId,$(this).find("option:selected").text());
					}
			});
			parent.$('#skuListTable').append(newPrd(barcode,mainTitle,specTds));
		}
		
	});
});


Array.prototype.indexOf = function (val) {  
    for (var i = 0; i < this.length; i++) {  
        if (this[i] == val) {  
            return i;  
        }  
    }  
    return -1;  
};

/**
 * 规格判断
 * @param selectSpec
 * @param itemSpec
 */
function checkPrdSpec(selectSpec,itemSpec){
	for(var i = 0 ; i < itemSpec.length; i ++){
		var specInfo = itemSpec[i];
		var m = 0 ;
		for(var j = 0 ; j < selectSpec.length; j ++){
			if(specInfo.indexOf(selectSpec[j])!=-1){
				m++;
			}
		}
		if(selectSpec.length == m){
			alert("规格已经存在");
			return false;
		}
		m=0;
	}
	
	return true;
	
}


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
	var infoJson={id:id,largeId:largeId,mediumId:mediumId,smallId:smallId,unitId:unitId,
			brandId:brandId,mainTitle:mainTitle,remark:remark,id:id};
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
		var id = $.trim($(this).find('[name=id]').val());
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
		var detailJson ={id:id,barcode:barcode,mainTitle:mainTitle,specGroupIds:specGroupIds,specIds:specIds};
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
		var detailJson ={detailId:id,barcode:barcode,mainTitle:mainTitle,specGroupIds:specGroupIds,specIds:specIds};
		prdIdAry.push(detailJson);
	});
	return prdIdAry;
}

function newSpec(specGroupId,specId,specName){
	var specTd = "<td width='100'><input type='hidden' class='specIdInfo' name='specIdInfo_"+specGroupId+"' value='"+specId+"' />"+specName+"</td> "
			   +'<input type="hidden" name="specGroupId" class="input-text lh30" size="5" value='+specGroupId+'-'+specId+'  />'
			   +'<input type="hidden" name="specId" class="input-text lh30" size="5"   value='+specId+'  /> '
			   +'<input type="hidden" class="specIdInfo" name="'+specGroupId+'_'+specId+'"  />';
	
	return specTd;
}

function newPrd (barcode,mainTitle,specTds) {
	var tr ="<tr class='prdidsList'> "+
		    "<td width='60' >"+
		    "<input type='hidden' name='detailId' value='' /> "+
		    "<input type='text' name='barcode' value='"+barcode+"' class='input-text lh30' size='25' /> </td> "+
		    "<td width='100'>系统自动生成</td> "+
		    "<td width='60' >"+
		    "<input type='text' name='mainTitle' value='"+mainTitle+"' class='input-text lh30' size='25' /> </td> "+
		    specTds+
		    "<td width='100'>未上架</td> "+
		    "<td width='100'><a href='javascript:void(0);' class='deletePrdBtn' >[删除]</a></td> "+
		    "</tr> ";
	return tr;
}
