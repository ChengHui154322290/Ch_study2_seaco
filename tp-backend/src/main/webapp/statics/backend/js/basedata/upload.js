$(function(){
	
	  var bucketname= $("#bucketname").val();
      var bucketURL= $("#bucketURL").val();
      var imagenameattribute=$('[imagenameattribute]').attr('imagenameattribute');
      //图片上传
      QiniuUpload(true,imagenameattribute,bucketname,bucketURL,"pickfiles","container","imguplod");
      areaQiniuUpload(true,"ChildImageUrls",bucketname,bucketURL,"pickfiles0","container0");
});