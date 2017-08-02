/**
 * 图片预览
 * 
 * @param file
 * @param tagetImg
 */
function previewImage(file,tagetImg) {
	var img = new Image();
	var url = img.src = URL.createObjectURL(file);
    var imgPre = jQuery(img)
    imgPre.attr("width","180");
    imgPre.attr("height","180");
    img.onload = function() {
        URL.revokeObjectURL(url);
        tagetImg.html(imgPre);
    }
}

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
	//var inputField = "<select style='width: 130px;' id='pre_text_field_"+index+"' class= 'input-text lh30 _req' ><option>请选择</option></select>";
	var inputField = "<input type='text' id='pre_text_field_"+index+"' value='' class='input-text lh30 _req' />";
	if(typeof optionBrands != 'undefined' && optionBrands){
		inputField = '<select id="pre_text_field_'+index+'" class="input-text lh30 _req" style="width:120px;">'+optionBrands+'</select>';
	}
	var brandTypeHtml = "<span id='image_span_"+index+"'><img width='80' height='80' id='img_show_"+index+"' src='"+absUrl+"'/><br/><a href='javascript:deleteImg("+index+")'>删除</a><br>品牌<font color='red'>*</font>：" +
        	inputField+
			"&nbsp;&nbsp;销售商品类别描述（如：食品，服装）11：<input type='text' value='' id='pre_desc_field_"+index+"' class= 'input-text lh30' /><br><br></span>";
	var commonType = "<span id='image_span_"+index+"'><img width='80' height='80' id='img_show_"+index+"' src='"+absUrl+"'/><br>说明：<input type='text' value='' id='pre_desc_field_"+index+"' class= 'input-text lh30' /><br/><a href='javascript:deleteImg("+index+")'>删除</a><br></span>";
	var commonType3 = "<div class='col-md-2' id='image_span_"+index+"'><img width='100' height='80' id='img_show_"+index+"' src='"+absUrl+"'/><br/><a href='javascript:deleteImg("+index+")'>删除</a></div>";
	if(1 == parseInt(typeObj)){
		return commonType;
	} else if(2 == parseInt(typeObj)){
		return brandTypeHtml;
	} else if(3 == parseInt(typeObj)){
		return commonType3;
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

jQuery(document).ready(function(){
	
	/**
	 * 校验图片数量
	 */
	function checkImageCount(maxLoadSize,name){
		var maxCount = parseInt(maxLoadSize);
		var countImage = jQuery("#imgSpan_"+name).find("img").length;
		if(countImage>=maxCount){
			return false;
		}
		return true;
	}
	
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
			return false;
		}
		var name = jQuery(this).attr("showTag");
		var showType = jQuery(this).attr("showType");
		var maxLoadSize = jQuery(this).attr("maxLoad");
		if(maxLoadSize && !checkImageCount(maxLoadSize,name)){
			alertMsg('最多上传'+maxLoadSize+'张图片。');
			return false;
		}
		if(!name){
			return;
		}
		name = name.toString().replace("_","");
		jQuery("#imageForm_"+name).ajaxSubmit({
			dataType:'json',
			success:function(data){
				if(!data.success || 'false' == data.success){
					uploadObj.val("");
					alertMsg(data.message);
				} else {
					var index = jQuery("#fileUploadIndex").val();
					var retUrl = data.data['fileUrl'];
					var absUrl = data.data['absUrl'];
					jQuery("#"+suppix+name).append(getShowImageInfoByType(showType,index,retUrl,absUrl));
					jQuery("#hiddenFieldId").append(getHiddenFieldHtmlByType(showType,index,retUrl,name));
					jQuery("#fileUploadIndex").val(parseInt(index)+1);
					registeImgTextEvent(index);
				}
			}
		});
	});
	
	jQuery("input._fileupload").change(function(e){
		var uploadObj = jQuery(this);
    	var file = e.target.files[0]
        if(!checkIsRarOrZip(file.name)){
        	jQuery(this).val('');
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
			dataType:'json',
			success:function(data){
				if(!data.success || 'false' == data.success){
					uploadObj.val("");
					alertMsg(data.message);
				} else {
					var index = jQuery("#fileUploadIndex").val();
					var retUrl = data.fileUrl;
					jQuery("#"+name+"Id").val(retUrl);
					jQuery("#fileUploadIndex").val(parseInt(index)+1);
				}
			}
		});
    });
	
	jQuery("input._imgPre").change(function(e){
		previewImage(e.target.files[0], tagetImg);
		/*var file = e.target.files[0];
		addFile(jQuery(this).attr("id"),file);*/
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
	});
}