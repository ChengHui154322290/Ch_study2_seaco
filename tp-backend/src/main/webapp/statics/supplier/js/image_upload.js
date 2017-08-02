/**
 * 删除图片
 * 
 * @param objIndex
 */
function deleteImg(objIndex){
	jQuery("#image_span_"+objIndex).remove();
	jQuery("#hidden_field_"+objIndex).remove();
	jQuery("#text_field_"+objIndex).remove();
	jQuery("#desc_field_"+objIndex).remove();
}

/**
 * 根据类型获取展示的内容
 */ 
function getShowImageInfoByType(typeObj,index,retUrl,absUrl){
	/*
	 * 定义一下基本元素
	 */
	//第一个字段定义   默认为input
	var preField = "<input type='text' id='pre_text_field_"+index+"' value='' class='input-text lh30 _req' />";
	//描述字段
	var preDescField = "<input type='text' value='' id='pre_desc_field_"+index+"' class= 'input-text lh30' />";
	//预览按钮
	var previewBtnStr = "<input type='button' value='预览' onclick='showImage(\"brandImgView\",\""+absUrl+"\")' class='ext_btn'>";
	//删除按钮
	var deleteBtnStr = "<a href='javascript:deleteImg("+index+")'>删除</a>";
	
	/*
	 * 特殊业务逻辑队基本元素处理
	 */
	var brandoptionselectid = $('#brandoptionselectid').children();
	if(typeof optionBrands != 'undefined' && optionBrands){
		preField = '<select id="pre_text_field_'+index+'" class="select _req" style="width:120px;">'+optionBrands+'</select>';
	}
	
	/*
	 * 具体元素所有定制化 定义
	 */
	var brandDescFieldStr = "销售商品类别描述（如：食品，服装）："+preDescField;
	var brandPreField = "品牌<font color='red'>*</font>："+preField;
	var commonDescField = "说明："+preDescField;
	
	/*
	 * 最终的html封装  并根据类型进行返回
	 */
	var brandTypeHtml = "<div style='padding:5px;width: 800px;' id='image_span_"+index+"'>"+brandPreField+"&nbsp;&nbsp;&nbsp;&nbsp;"+brandDescFieldStr+"&nbsp;&nbsp;"+previewBtnStr+"&nbsp;&nbsp;"+deleteBtnStr+"<br></div>";
	var commonTypeHtml = "<div style='padding:5px;width: 800px;' id='image_span_"+index+"'>"+commonDescField+"&nbsp;&nbsp;"+previewBtnStr+"&nbsp;&nbsp;"+deleteBtnStr+"<br></div>";
	if(1 == parseInt(typeObj)){
		return commonTypeHtml;
	} else if(2 == parseInt(typeObj)){
		return brandTypeHtml;
	}
}
/**
 * 注册文字描述
 * 
 * @param index
 */
function registeImgTextEvent(index){
	jQuery("#pre_desc_field_"+index).change(function(){
		jQuery("#desc_field_"+index).val(jQuery(this).val());
	});
	jQuery("#pre_text_field_"+index).change(function(){
		jQuery("#text_field_"+index).val(jQuery(this).val());
	});
}

/**
 * 获取隐藏区域要添加的内容
 * 
 * @param typeObj
 * @param index
 * @param retUrl
 * @returns {String}
 */
function getHiddenFieldHtmlByType(typeObj,index,retUrl,name){
	var brandHtml = "<input type='hidden' id='text_field_"+index+"' value='' name='brandName' /><input type='hidden' id='desc_field_"+index+"' value='' name='imageDesc' />";
	var type1 = "<input type='hidden' id='desc_field_"+index+"' value='' name='"+name+"Desc' />";
	var commonTypeHtml = "<input type='hidden' id='hidden_field_"+index+"' value='"+retUrl+"' name='"+name+"' />";
	if(1== parseInt(typeObj)){
		return commonTypeHtml+type1;
	} else if(2 == parseInt(typeObj)){
		return brandHtml + commonTypeHtml;
	} else if(3 == parseInt(typeObj)){
		return "<input type='hidden' id='hidden_field_"+index+"' value='"+retUrl+"' name='"+name+"' />";
	}
}

var fileMap = {};
function addFile(key,file){
	fileMap[key] = file;
}

function getFile(key){
	return fileMap[key];
}

/**
 * 
 * @param idFile
 */
function showImage(idFile,imgUrl){
	var file = jQuery("#"+idFile);
	if(!file.val() && !imgUrl){
		return;
	}
	window.parent.showImageDiv(getFile(idFile),imgUrl);
}

jQuery(document).ready(function(){
	/**
	 * 文件上传
	 */
	jQuery("input._mulupload").change(function(e){
		var uploadObj = jQuery(this);
		var imgPath = jQuery(this).val();
		var suppix = "imgSpan_";
		if(!checkIsImage(imgPath)){
			jQuery(this).val('');
			alertMsg('必须上传图片格式文件。');
			return;
		}
		var name = jQuery(this).attr("showTag");
		var showType = jQuery(this).attr("showType");
		if(!name){
			return;
		}
		name = name.toString().replace("_","");
		jQuery("#imageForm_"+name).ajaxSubmit({
			beforeSend:function(){
				window.parent.popWaitDivMap.showWaitDiv();
			},
			dataType:'text',
			success:function(data){
				try {
					var datas = (data.split("</pre>")[0]);
					// by zhs 0127
					// datas = datas.split('<pre>');					
					datas = datas.split('>');
					if(datas.length==2){
						data=datas[1];
					}else{
						data = datas[0];
					}
				} catch(e){
				}
				data = jQuery.parseJSON(data);
				if(!data.success || 'false' == data.success){
					alertMsg(data.msg.message);
				} else {
					var index = jQuery("#fileUploadIndex").val();
					var retUrl = data.data.fileUrl;
					var absUrl = data.data.absUrl;
					jQuery("#"+suppix+name).append(getShowImageInfoByType(showType,index,retUrl,absUrl));
					jQuery("#hiddenFieldId").append(getHiddenFieldHtmlByType(showType,index,retUrl,name));
					jQuery("#fileUploadIndex").val(parseInt(index)+1);
					registeImgTextEvent(index);
				}
				uploadObj.val("");
			},
			complete:function(){
				window.parent.popWaitDivMap.hideWaitDiv();
			}
		});
	});
	
	jQuery("input._fileupload").change(function(e){
		var uploadObj = jQuery(this);
    	var file = e.target.files[0];
    	var fileFormat = jQuery(this).attr("fileFomater");
        if(!checkIsRarOrZip(file.name,fileFormat)){
        	jQuery(this).val('');
        	if(fileFormat){
        		alertMsg('必须上传'+fileFormat+'文件。');
        		return false;
        	}
        	alertMsg('必须压缩文件（rar或者zip）。');
        	return false;
        }
        //1. 删除错误消息提示
    	var name = jQuery(this).attr("showTag");
		var showType = jQuery(this).attr("showType");
		if(!name){
			return;
		}
		name = name.toString().replace("_","");
		jQuery("#fileForm_"+name).ajaxSubmit({
			beforeSend:function(){
				window.parent.popWaitDivMap.showWaitDiv();
			},
			dataType:'text',
			success:function(data){
				try {
					var endIndex = data.toString().lastIndexOf("<!--");
				    var len = data.toString().length;
				    var endTag = data.toString().substring(len-3,len);
				    if('-->'==endTag){
				    	data = data.toString().substring(0,endIndex);
				    }
				} catch(e){
				}
				// by zhs 0127
				try {
					var datas = (data.split("</pre>")[0]);
					// by zhs 0127
					// datas = datas.split('<pre>');					
					datas = datas.split('>');
					if(datas.length==2){
						data=datas[1];
					}else{
						data = datas[0];
					}
				} catch(e){
				}
				///////////////
				var data = jQuery.parseJSON(data);
				if(!data.success || 'false' == data.success){
					uploadObj.val("");
					alertMsg(data.message);
				} else {
					var index = jQuery("#fileUploadIndex").val();
					// by zhs 0128
					//var retUrl = data.fileUrl;
					var retUrl = data.data.fileUrl;
					jQuery("#"+name+"Id").val(retUrl);
					jQuery("#fileUploadIndex").val(parseInt(index)+1);
				}
			},
			complete:function(){
				window.parent.popWaitDivMap.hideWaitDiv();
			}
		});
    });
	
	jQuery("input._imgPre").change(function(e){
		var file = e.target.files[0];
		addFile(jQuery(this).attr("id"),file);
	});
	
});
/**
 * 注册图片预览事件
 * 
 * @param obj
 */
function registeImgPreEvent(obj){
	jQuery(obj).change(function(e){
		var file = e.target.files[0];
		addFile(jQuery(this).attr("id"),file);
		
		var imgFileSize=Math.round(file.size/1024*100)/100;
	    if(imgFileSize>1024){
	    	jQuery(this).val("");
	    	alertMsg('图片文件大小不超过1M。');
	    	return;
	    }
	    if(!checkIsImage(file.name)){
	    	jQuery(this).val('');
	    	alertMsg('必须上传图片文件。');
	    	return false;
	    }
		
	});
}