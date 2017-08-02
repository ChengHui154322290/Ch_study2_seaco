
var index = parent.layer.getFrameIndex(window.name);
$(function(){

	  var bucketname= $("#bucketname").val();
      var bucketURL= $("#bucketURL").val();
      var imagenameattribute=$('[imagenameattribute]').attr('imagenameattribute');

    console.log(bucketname);
    console.log(bucketURL);
    console.log(imagenameattribute);
      //图片上传
      QiniuUpload(true,imagenameattribute,bucketname,bucketURL,"pickfiles","container","imguplod",1,beforeOperation , callback);
});

function beforeOperation(){

};

function callback(name,url,key) {
    $("#itemPic" ).val(key); // 文件提取码
    $("#pic").attr("src",url);// 文件全路径
    $("#img").css("display","block");

}
