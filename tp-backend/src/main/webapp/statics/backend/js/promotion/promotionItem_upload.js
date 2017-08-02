var UPLOAD_ITEM_IMAGE = domain + "/topicItem/uploadImage";
var index = parent.layer.getFrameIndex(window.name);

$(document).ready(function() {

	var bucketname= $("#bucketname").val();
	var bucketURL= $("#bucketURL").val();
	var imagenameattribute=$('[imagenameattribute]').attr('imagenameattribute');

	//图片上传
	QiniuUpload(true,imagenameattribute,bucketname,bucketURL,"pickfiles","container","imguplod",1,beforeOperation , callback);

	$("#close").on("click",function(){
		swfuItem = null;
		$("#spanButtonItemPic").remove();
		parent.layer.close(index);
	});
})


function beforeOperation(){
};

function callback(name,url,key) {

	var itemIndex = $("#selectItemIndex").val();
	var row = parent.$("#topicItemsList tr")[itemIndex];

	$(row).children().find("input[name='topicImage']").val(key);
	$(row).children().find("img[name='selectImage']").attr("src",url);

	parent.layer.close(index);

}

