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
    imgPre.attr("width","360");
    imgPre.attr("height","360");
    img.onload = function() {
        URL.revokeObjectURL(url)
        tagetImg.html(imgPre);
    }
}

/**
 * 弹出table层
 */
function showTableDivproduct(popType,dataHtml,iframeId) {
	jQuery("#layAllDiv").show();
	var pageii = $.layer({
	    type: 1,
	    title: false,
	    area: ['auto', 'auto'],
	    border: [0], //去掉默认边框
	    shade: [0], //去掉遮罩
	    closeBtn: [0, false], //去掉默认关闭按钮
	    shift: 'left', //从左动画弹出
	    page: {
	    	// style="width:420px; height:260px; padding:20px; border:1px solid #ccc; background-color:#eee;" <button id="imgPopShowbtn" class="btns" onclick="">关闭</button>
	        html: '<div style="width:651px; height:260px; padding:20px; border:1px solid #ccc; background-color:#eee;"><p id="tableShowArea"></p></div>'
	    }
	});
	generateDataproduct(dataHtml,popType,iframeId,pageii);
	return false;
}

/**
 * 生成页面数据
 */
function generateDataproduct(htmlObj,genType,iframeId,pageii){
	if(htmlObj){
		jQuery("#tableShowArea").html(htmlObj);
	}
	refreshPageproduct(genType,iframeId,pageii);
}

/**
 * 设置页面数据
 */
function setSuppDataToPageproduct(value,zjvalue,newvalue,iframeId,pageii){
	jQuery("#"+iframeId).contents().find("#value").val(value);
	jQuery("#"+iframeId).contents().find("#zjvalue").val(zjvalue);
	jQuery("#"+iframeId).contents().find("#newvalue").val(newvalue);
	jQuery('#popClosebtn').click();
}

function refreshPageproduct(genType,iframeId,pageii){
	//自设关闭
	$('#popClosebtn').on('click', function(){
	    layer.close(pageii);
	    jQuery("#layAllDiv").hide();
	})
}
