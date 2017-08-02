var DEL_URL = domain + "/offlinegb/delConfig";
var LOAD_URL = domain +"/offlinegb/hsConfigList";
var ADD_URL = domain+"/offlinegb/addConfig";

$(function () {

    // /**
    //  * 查询topic
    //  */
    // $("#queryTopic").on("click", function () {
    //     var topicId = $("#topicId").val();
    //     if (topicId == "" || $.trim(topicId) == "") {
    //         layer.msg("请输入专场Id");
    //         return;
    //     }
    //     $.post(QUERY_URL, {type: 1, value: topicId}, function (data) {
    //         if (data.success && data.data != null) {
    //             $("#topicName").val(data.data);
    //         } else {
    //             layer.msg(data.msg.message);
    //         }
    //     });
    // });
    //
    //
    $("#close").on("click",function(){
        parent.layer.close(index);
    });

    $('.closebtn').on('click',function(){
        parent.layer.close(parent.pageii);
    });

    $('#datasubmit').on('click',function(){
       alert(3);
    });


    $('#addConfig').on('click',function(){
        pageii=$.layer({
            type : 2,
            title: '添加线下团购热门商品',
            shadeClose: true,
            maxmin: true,
            fix : false,
            area: ['600px', 400],
            iframe: {
                src : ADD_URL
            }
        });
    });

    loadList(-1);

});

function  loadList(target){
    $("#hsConfigList").load(LOAD_URL,{target:target});

}
function deleteConfig(id) {
    $.post(DEL_URL,{id:id},function (data) {
        if(data.success){
            loadList(-1);
        }else {
            layer.alert(data.msg.message);
        }

    })
}

