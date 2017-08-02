/**
 * 校验非空
 */
function checkIsNotNull(obj){
    if(obj){
    	return true;
    } else {
    	return false;
    }
}

/**
 * 检查是否为整数
 */
function checkIntNum(txt){
	var numReg = /^[-+]?\d*$/;
	return numReg.test(txt);
}

/**
 * 校验价格
 * 
 * @param txt
 * @returns
 */
function checkPrice(txt){
	var reg = /^\d{1,10}(\.\d{1,2})?$/;
	return reg.test(txt);
}

/**
 * 校验移动电话
 * 
 * @param str
 * @returns {Boolean}
 */
function checkMobile(str) {
   if(!str){
       return true;
   }
   var re = /^1\d{10}$/
   if (re.test(str)) {
       return true;
   } else {
       return false;
   }
}

/**
 * 校验固定电话
 * 
 * @param str
 * @returns {Boolean}
 */
function checkPhone(str){
   if(!str){
       return true;
   }
   var re = /^0\d{2,3}-?\d{7,8}$/;
   if(re.test(str)){
       return true;
   }else{
       return false;
   }
}

/**
 * 校验是否是图片格式
 * 
 * @param obj
 */
function checkIsImage(obj){
	var supportFmt = ".jpg.jpeg.gif.png.bmp";
	if(!obj){
		return false;
	}
	var fmt = getFileFormat(obj);
	if(!fmt){
		return false;
	}
	return supportFmt.indexOf(fmt.toString().toLowerCase())>-1;
}

/**
 * 
 * 校验邮箱
 * 
 * @param str
 * @returns {Boolean}
 */
function checkEmail(str){
   if(!str){
	   return true;
   }
   var re = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/
   if(re.test(str)){
       return true;
   }else{
       return false;
   }
}

/**
 * 校验压缩文件
 */
function checkIsRarOrZip(obj){
	var supportFmt = ".rar.zip";
	if(!obj){
		return false;
	}
	var fmt = getFileFormat(obj);
	if(!fmt){
		return false;
	}
	return supportFmt.indexOf(fmt.toString().toLowerCase())>-1;
}

/*
 * 处理空的字段
 */
function handleFieldIsNull(obj){
	var errorMsg = "<font class='_errorMsg' color='red'>该字段非空。</font>";
	//错误信息展示
	jQuery(obj).after(jQuery(errorMsg));
}

/**
 * 处理邮箱格式不对信息提示
 * 
 * @param obj
 */
function handleFieldEmail(obj){
	var errorMsg = "<font class='_errorMsg' color='red'>邮箱格式不对。</font>";
	//错误信息展示
	jQuery(obj).after(jQuery(errorMsg));
}

/**
 * 处理应该是数字但却不是数字的字段
 */
function handleFieldNotNum(obj){
	var errorMsg = "<font class='_errorMsg' color='red'>该字段必须为整数。</font>";
	//错误信息展示
	jQuery(obj).after(jQuery(errorMsg));
}

/**
 * 处理移动电话格式不对信息提示
 */
function handleFieldMobile(obj){
	var errorMsg = "<font class='_errorMsg' color='red'>移动电话格式不对（正确格式如：13909090000）。</font>";
	//错误信息展示
	jQuery(obj).after(jQuery(errorMsg));
}

/**
 * 处理固定电话格式不对信息提示
 */
function handleFieldTel(obj){
	var errorMsg = "<font class='_errorMsg' color='red'>固定电话格式不对（正确格式如：021-60999999）。</font>";
	//错误信息展示
	jQuery(obj).after(jQuery(errorMsg));
}

/**
 * 注册验证事件
 */
function registeValidateEvent() {
	//先通过die()方法解除，再通过live()绑定
	var intNumObjs = jQuery("input._intnum");
	var priceObjs = jQuery("input._price");
	for(var i=0;i<intNumObjs.length;i++){
		jQuery(intNumObjs[i]).unbind("keyup");
		jQuery(intNumObjs[i]).keyup(function(){
			var intVal = jQuery(this).val();
			if(!checkIntnum(intVal)){
				jQuery(this).val(0);
			}
		});
	}
	
	for(var i=0;i<priceObjs.length;i++){
		jQuery(priceObjs[i]).unbind("keyup");
		jQuery(priceObjs[i]).keyup(function(){
			var priceVal = jQuery(this).val();
			if(!checkPrice(priceVal)){
				jQuery(this).val(0);
			}
		});
	}
}

/*
 * 校验表单的信息
 * 返回true或者false
 */
function validateInfo(){
	//1. 删除错误消息提示
	jQuery("font._errorMsg").remove();
	//获取非空字段
	var notNullObj = jQuery("._req");
	//获取数字字段
	var numObj = jQuery("._num");
	//获取email
	var emailObj = jQuery("input._email");
	//获取固定电话
	var telObj = jQuery("input._tel");
	//获取移动电话
	var mobileObj = jQuery("input._mobile");
	
	var retVal = true;
	try{
		//1. 校验非空
		notNullObj.each(function(key,obj){
			if(!checkIsNotNull(jQuery(obj).val())){
				handleFieldIsNull(obj);
				retVal = false;
			}
		});
		if(!retVal){ 
			//如果非空不通过先直接返回
			return false;
		}
		//2. 校验整数
		numObj.each(function(key,obj){
			if(!checkIntNum(jQuery(obj).val())){
				handleFieldNotNum(obj);
				retVal = false;
			}
		});
		//3. 校验邮箱
		emailObj.each(function(key,obj){
			if(!checkEmail(jQuery(obj).val())){
				handleFieldEmail(obj);
				retVal = false;
			}
		});
		if(!retVal){ 
			//如果不通过先直接返回
			return false;
		}
		
		//4. 校验移动电话
		mobileObj.each(function(key,obj){
			if(!checkMobile(jQuery(obj).val())){
				handleFieldMobile(obj);
				retVal = false;
			}
		});
		if(!retVal){ 
			//如果不通过先直接返回
			return false;
		}
		
		//5. 校验固定电话
		telObj.each(function(key,obj){
			if(!checkPhone(jQuery(obj).val())){
				handleFieldTel(obj);
				retVal = false;
			}
		});
		if(!retVal){ 
			//如果不通过先直接返回
			return false;
		}
		
	} catch(e){
		//console.log(e);
	}
	return retVal;
}

/**
 * 页面校验
 */
jQuery(document).ready(function(){
	//init..
	jQuery('input._imgPre').change(function(e) {
        var file = e.target.files[0]
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
        //1. 删除错误消息提示
    	jQuery(this).next("font._errorMsg").remove();
        //previewImage(file,jQuery(this).next("span"))
    })
});
