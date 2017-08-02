var index = parent.layer.getFrameIndex(window.name);
var QUERY_URL = domain + "/offlinegb/query";
var DO_ADD_URL = domain + "/offlinegb/doAddConfig";
var DEL_URL = domain + "/offlinegb/blacklist/deleteBlacklist";
var SYN_DATA_URL = domain + "/offlinegb/blacklist/synData";
var LOAD_URL = domain +"/offlinegb/hsConfigList";


$(function () {


    $("#close").on("click",function(){
        parent.layer.close(index);
    });

    $('.closebtn').on('click',function(){
        parent.layer.close(parent.pageii);
    });

    $('#datasubmit').on('click',function(){
       var topicId = $("#topicId").val();
        var sku = $("#sku").val();
        var sort = $("#sort").val();
        if(topicId==null || topicId.trim()=="" ){
            layer.alert("请输入专场Id");
            return;
        }

        if(sku==null||sku.trim()==""){
        layer.alert("请输入SKU");
        return;
        }

        if(sort ==null || sort.trim()==""){
            sort = 1;
        }
        $.post(DO_ADD_URL,{topicId:topicId,sku:sku,sort:sort},function (data) {
            if(data.success){
                parent.loadList(-1);
                parent.layer.close(index);
            }else {
                layer.alert(data.msg.message);
            }

        })


    });



    loadList(-1);

});

function  loadList(target){
    $("#hsConfigList").load(LOAD_URL,{target:target});

}

