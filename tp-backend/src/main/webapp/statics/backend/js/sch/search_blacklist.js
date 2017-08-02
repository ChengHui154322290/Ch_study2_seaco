var QUERY_URL = domain + "/search/blacklist/query"
var ADD_URL = domain + "/search/blacklist/addBlackList"
var DEL_URL = domain + "/search/blacklist/deleteBlacklist"
var SYN_DATA_URL = domain + "/search/blacklist/synData"

$(function () {

    /**
     * 查询topic
     */
    $("#queryTopic").on("click", function () {
        var topicId = $("#topicId").val();
        if (topicId == "" || $.trim(topicId) == "") {
            layer.msg("请输入专场Id");
            return;
        }
        $.post(QUERY_URL, {type: 1, value: topicId}, function (data) {
            if (data.success && data.data != null) {
                $("#topicName").val(data.data);
            } else {
                layer.msg(data.msg.message);
            }
        });
    });

    /**
     * 添加topic
     */
    $("#addTopic").on("click", function () {
        var topicId = $("#topicId").val();
        if (topicId == "" || $.trim(topicId) == "") {
            layer.msg("请输入专场Id");
            return;
        }
        $.post(ADD_URL, {type: 1, value: topicId}, function (data) {
            if (data.success) {
                loadList(topicId);
            } else {
                layer.msg(data.msg.message);
            }
        });
    });

    /**
     * 查询sku
     */
    $("#queryItemSku").on("click", function () {
        var itemSku = $("#itemSku").val();
        if (itemSku == "" || $.trim(itemSku) == "") {
            layer.msg("请输入SKU");
            return;
        }
        $.post(QUERY_URL, {type: 2, value: itemSku}, function (data) {
            if (data.success && data.data != null) {
                $("#itemName").val(data.data);
            } else {
                layer.msg(data.msg.message);
            }
        });
    });

    /**
     * 添加sku
     */
    $("#addItemSku").on("click", function () {
        var itemSku = $("#itemSku").val();
        if (itemSku == "" || $.trim(itemSku) == "") {
            layer.msg("请输入SKU");
            return;
        }
        $.post(ADD_URL, {type: 2, value: itemSku}, function (data) {
            if (data.success) {
                loadList(itemSku);
            } else {
                layer.msg(data.msg.message);
            }
        });
    });

    $("#synData").on("click",function(){
        $("#synData").attr("disabled",true);
        $("#synData").val("同步中..");
        $.post(SYN_DATA_URL,function(data){
           if(data.success){
               $("#synData").val("同步成功");
               $("#synData").attr("disabled",false);
           } else {
               $("#synData").val(data.msg.message);
           }
        });
    });


    loadList(-1);

});

function  loadList(target){
    $("#searchBlacklistList").load("/search/blacklist/searchBlacklistList",{target:target});

}

function deleteBlacklist(id){
  layer.confirm("确认删除?",function(index){
      $.post(DEL_URL,{id:id},function(data){
          if(data.success){
              layer.close(index);
              loadList(-1);
          }else {
              layer.msg(data.msg.message);
          }
      })
  });

};
