var fileCountlimit = 1;
var hasUploadCount = 0;
$(function(){
	var bucketname= $(":hidden[name=bucketname]").val();
	var bucketURL= $(":hidden[name=bucketURL]").val();
    var imagenameattribute=$('[imagenameattribute]').attr('imagenameattribute');
	QiniuUpload(true,imagenameattribute,bucketname,bucketURL,"couponImagePickfiles","container","mobileImguplod",10,
		function(){
			//$("#imguplod").html('');
		},
		function(name,path,key){
			$("#couponImagePath").val(key);
			setTimeout(function(){$("#imgShowCouponImagePath").attr('src',path);}, 1000);
	   });
});	