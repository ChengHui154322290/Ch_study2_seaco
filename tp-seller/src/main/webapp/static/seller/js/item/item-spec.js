$(function() {
	/**
	 * 是否均码
	 */
	$('#selectSpec').click(function() {
		if ($(this).attr('checked')) {
			$('input[type=checkbox] :gt(0)').attr("checked", false);
			$('input[type=checkbox] :gt(0)').attr("disabled", "disabled");
		} else {
			$('input[type=checkbox] :gt(0)').removeAttr("disabled");
		}
	});

	/**
	 * 父选子
	 * 全选/取消全选
	 */
	$('.specGroup').click(function() {
		var specClassName = $(this).attr('name');
		if ($(this).attr('checked')) {
			$("input[class='" + specClassName + "']").attr("checked", true);
		} else {
			$("input[class='" + specClassName + "']").attr("checked", false);
		}
	});
	
	/**
	 * 子选父
	 */
	$('input[type="checkbox"]').each(function(){
		if($(this).attr('class')!=="specGroup"&&($(this).attr('id')!=="selectSpec")){
			$(this).click(function(){
				var className = $(this).attr("class");
				if ($(this).attr('checked')) {
					$('input[name="'+className+'"]').attr("checked", true);
				}else{
					var childSelect = false;
					$('.'+className).each(function(){
						var checked = $(this).attr("checked");
						if(checked !==undefined){
							childSelect = true;
							return childSelect;
						}
					});
					if(!childSelect){
						$('input[name="'+className+'"]').attr("checked", false);
					}
					
				}
			});
		}
		
	});
	/**
	 判断是否选择
	*/
	function judgeCheckBoxSelected(){
		var flag = false;
		$('input[type="checkbox"]').each(function(){
			var checked = $(this).attr("checked");
			if(checked !==undefined){
				flag = true;
				return flag;
			}
		});
		return flag;
	}


	/**
	 * 选择规格
	 */
	$('#selectAttributesbtn').click(function() {
		var flag = judgeCheckBoxSelected();
		if(!flag){
			alert("请选择规格或者均码");
			return ;
		}
			
		//初始化清空列表
		parent.$('#clearPrdidListBtn').click();
		var allSpec = [];
		var allSpecGroup = [] ;
		var length = $("input[class='specGroup']:checked").length;
		if (length > 3) {
			alert('规格组不能超过3组');
			return;
		}
		$("input[class='specGroup']:checked").each(function() {
			var specGroupObj = new Object();
			specGroupObj.id=$(this).val();
			specGroupObj.name=$(this).attr("specGroupName");
			allSpecGroup.push(specGroupObj);
			var specClassName = $(this).attr('name'); //规格组的name属性  关联 规格的class属性，此处已经约定了.
			var specArray = new Array();
			$("input[class='" + specClassName + "']:checked").each(function() {
				var specObj = new Object();
				specObj.id = $(this).val();
				specObj.name = $(this).attr('specName');
				specObj.pid = $(this).attr('pid');
				specArray.push(specObj);
			});
			allSpec.push(specArray);
		});
		//所有匹配的规格项
		var result = new Array();
		//循环得出resutl的值
		for(var i =0 ;i <allSpec.length;i ++){
			var flag = false;
			if(i==2){//第一组选择一个规格分支
				flag = true;
			}
			result = reso(result, allSpec[i],flag);
		}
		
		//均码处理
		if(allSpecGroup.length==1){
			var res = new Array();
			for(var i =0 ;i < result[0].length;i++){
				var resTmp = new Array();
				resTmp.push(result[0][i]);
				res.push(resTmp);
			}
			
			result = res;
		}
		
		//console.log(allSpecGroup.length);
		//console.log(result.length);
		// 追加到prdid中去
		parent.$('#skuListTable').append(appendToPrdidHead(allSpecGroup));//生成 
		if(allSpecGroup.length>0){
			for(var i =0 ;i <result.length;i++){
				parent.$('#skuListTable').append(appendToPrdidList(allSpecGroup,result[i]));//生成 
			}
		}else{
			var i = 0;
			parent.$('#skuListTable').append(appendToPrdidList(allSpecGroup,result[i]));//生成 
		}
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		parent.layer.close(index); //执行关闭
	});
	
	var appendToPrdidHead = function (specGroupArray){
		var theadHtml = 
		'<tr>'+
		'<th width="60">条形码</th>'+
		'<th width="100">PRDID</th>'+
		'<th width="100">商品名称</th>';
		for(var i= 0; i < specGroupArray.length;i++){
			theadHtml+= '<th width="100">'+specGroupArray[i].name+'</th>';
		}
		theadHtml+='<th width="100">操作</th></tr>';
		return theadHtml;
	};
	
	//sku列表 
	var appendToPrdidList = function (specGroupArray,spec){
		var mainTitleVal = parent.$('#mainTitle').val();
		var trHtml = 
			'<tr class="prdidsList" >'
			
				+'<td width="60">'
				+	'<input type="text" name="barcode" class="input-text lh30" size="15" maxlength=20 />'
				+'</td>'
				+'<td width="60">'
				+	'<input type="text" name="prdid" class="input-text lh30" size="20" value="系统自动生成"  readonly=readonly/>'
				+'</td>'
				
				+'<td width="60">'
				+	'<input type="text" name="mainTitle" class="input-text lh30" size="15" value= "'+mainTitleVal+'"  />'
				+'</td>';
				
				for(var i= 0; i < specGroupArray.length;i++){
					trHtml +='<td width="60">'
						   +'<input type="hidden" name="specGroupId" class="input-text lh30" size="5"   readonly=readonly value='+specGroupArray[i].id+'-'+spec[i].id+'  />'
						   +'<input type="hidden" name="specId" class="input-text lh30" size="5"   readonly=readonly value='+spec[i].id+'  />'
					       +'<input type="text" name="specName" class="input-text lh30" size="5"  readonly=readonly value='+spec[i].name+'  />'
					       +'</td>';
				}
				trHtml +='<td width="100">'
						+'	<a href="javascript:;" class="deleteSkuListItem" >删除</a>'
						+'</td>'+
					'</tr>';
		return trHtml;
		
	};
	
	/**
	 * 匹配选择项
	 */
	function reso(result, oneSpec,flag) {
		// result刚开始的时候
		if (result.length == 0) {
			result.push(oneSpec);
		} else {
			var res = new Array();
			for (var i = 0; i < result.length; i++) {
				for (var j = 0; j < oneSpec.length; j++) {
					if(result.length==1){
						for (var k = 0; k < result[i].length; k++) {
							var resTmp = new Array();
							if(flag){
								resTmp.push(result[i][k]);
								k++;
								resTmp.push(result[i][k]);
							}else{
								resTmp.push(result[i][k]);
							}
							resTmp.push(oneSpec[j]);
							res.push(resTmp);
						}
					}else{
						var res1 = new Array();
						for(var k = 0 ;k<result[i].length;k++){
							res1.push(result[i][k]);
						}
						res1.push(oneSpec[j]);
						res.push(res1);
					}
					
				}
			}
			return res;
		}
		return result;
	}
	
});
