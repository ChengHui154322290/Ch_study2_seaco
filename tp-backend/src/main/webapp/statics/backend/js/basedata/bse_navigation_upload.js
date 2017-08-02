
var index = parent.layer.getFrameIndex(window.name);
$(function(){

    $("#close").on("click",function(){
        swfuItem = null;
        parent.layer.close(index);
    });

	  var bucketname= $("#bucketname").val();
      var bucketURL= $("#bucketURL").val();
      var imagenameattribute=$('[imagenameattribute]').attr('imagenameattribute');

      //图片上传
      QiniuUpload(true,imagenameattribute,bucketname,bucketURL,"pickfiles","container","imguplod",1,beforeOperation , callback);
});

function beforeOperation(){

};

function callback(name,url,key) {
    $("#pic" ).val(key); // 文件提取码
    $("#img").attr("src",url);// 文件全路径
    $("#img").css("display","block");

}
