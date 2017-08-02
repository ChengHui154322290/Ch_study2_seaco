var IMAGE_SAVE = domain + "/app/imageSave";
var IMAGE_LOAD = domain + "/app/imageLoad";

var image_id;

var index = parent.layer.getFrameIndex(window.name);
$(function () {

    $("#close").on("click", function () {
        swfuItem = null;
        parent.layer.close(index);
    });

    var bucketname = $("#bucketname").val();
    var bucketURL = $("#bucketURL").val();
    var imagenameattribute = $('[imagenameattribute]').attr('imagenameattribute');

    //图片上传
    QiniuUpload(true, imagenameattribute, bucketname, bucketURL, "pickfiles", "container", "imguplod", 1, beforeOperation, callback);

    loadImage();


    $("#load-more").on("click", function () {
        $("#load-more").hide();
        loadImage();
        $("#load-more").show();

    })
});

function loadImage() {
    $.post(IMAGE_LOAD, {id: image_id}, function (data) {
        if (data != null ) {

            if(data.length ==0){
                $("#load-more").unbind("click");
                $("#load-more").text("....");
                return
            }
            console.log(data);
            $.each(data, function (index) {
                var url = data[index].image;
                image_id = data[index].id;
                var temp = a + url + b + url + c;
                $("#img-list").append(temp);
            })
        }
    })

}

function beforeOperation() {

};
var a = ' <div class="main"> <div class="picture"><img src="';
var b = '"   width="200px" height="200px"  /></div><div class="picture_url"><span id="url">';
var c = '   <span></div></div>';

function callback(name, url, key) {
    // $("#pic" ).val(key); // 文件提取码
    // $("#img").attr("src",url);// 文件全路径
    $("#img").css("display", "block");

    var temp = a + url + b + url + c;


    console.log(temp);

    $("#img-list").prepend(temp);
    $.post(IMAGE_SAVE, {image: key}, function () {
        //do nothing...
    })

}
