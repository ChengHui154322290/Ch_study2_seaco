
/**
 * 获取文件格式
 */
function getFileFormat(obj){
	if(!obj){
		return "";
	}
	var objStr = obj.toString();
	if(objStr.indexOf(".")>-1){
		var lIndex = objStr.lastIndexOf(".");
		return objStr.substring(lIndex+1,objStr.length);
	} else {
		return "";
	}
}

/**
 * 页面跳转
 */
function jumpToPage(objUrl){
    window.location.href=objUrl;
}

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
        URL.revokeObjectURL(url)
        tagetImg.html(imgPre)
    }
}

