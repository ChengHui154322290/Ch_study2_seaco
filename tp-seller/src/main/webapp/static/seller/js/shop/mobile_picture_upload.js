var fileCountlimit = 1;
var hasUploadCount = 0;
$(function(){
	var bucketname= $(":hidden[name=bucketname]").val();
	var bucketURL= $(":hidden[name=bucketURL]").val();
    var imagenameattribute=$('[imagenameattribute]').attr('imagenameattribute');
	QiniuUpload(true,imagenameattribute,bucketname,bucketURL,"mobilePickfiles","container","mobileImguplod",10,
		function(){
			//$("#imguplod").html('');
		},
		function(name,path,key){
			$("#mobileImage").val(key);
			setTimeout(function(){$("#imgMobileImage").attr('src',path);}, 1000);
	   });
});	