
var fileCountlimit = 1;
var hasUploadCount = 0;
$(function(){
	var bucketname= $(":hidden[name=bucketname]").val();
	var bucketURL= $(":hidden[name=bucketURL]").val();
    var imagenameattribute=$('[imagenameattribute]').attr('imagenameattribute');
	QiniuUpload(true,imagenameattribute,bucketname,bucketURL,"pickfiles","container","imguplod",10,
		function(){
			//$("#imguplod").html('');
		},
		function(name,path,key){
			$("#logoPath").val(key);
			setTimeout(function(){$("#imglogoPath").attr('src',path);}, 1000);
	   
		});
});	