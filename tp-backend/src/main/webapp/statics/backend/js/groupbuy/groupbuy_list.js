var CONFIRM_ITEM_BAR = domain + "/topicItem/itemBarCode/confirmSearch";
var CONFIRM_ITEM_SKU = domain + "/topicItem/itemSKU/confirmSearch";
var ITEM_WH = domain + "/topicItem/wh?";
var ITEM_INVENTORY = domain + "/topicItem/wh/inventory";
var ADD_GROUP_BUY_PAGE = "/groupbuy/groupbuyInfo"
var EDIT_GROUP_BUY_PAGE = "/groupbuy/groupbuyInfo?topicId="

var UPDATE_GROUPBUY_STATUS = domain+"/groupbuy/updateGroupbuyStatus";

$(function () {

   $(".btn_add").on("click",function(){
       var index = parent.$("a[class='currenttab']").attr("id");
       show("groupbuyInfo", ADD_GROUP_BUY_PAGE,"新增团购");
       parent.window.closeTab(index);
   });

   $("#reset").on("click",function(){
       $("#topicId").val("");
       $("#name").val("");
       $("#status").val("");
       $("#startTime").val("");
       $("#endTime").val("");

   });

    $("a[name='editTopic']").on("click",function(){

        var topicId = $(this).attr('topicid');
        var url = EDIT_GROUP_BUY_PAGE+topicId;
        var index = parent.$("a[class='currenttab']").attr("id");
        show("groupbuyInfo"+topicId,url,"编辑团购信息");
        parent.window.closeTab(index);
    });
    $("a[name='viewTopic']").on("click",function(){

        var topicId = $(this).attr('topicid');
        var url = EDIT_GROUP_BUY_PAGE+topicId +'&mode='+'1';
        var index = parent.$("a[class='currenttab']").attr("id");
        show("groupbuyInfo"+topicId,url,"查看团购信息");
        parent.window.closeTab(index);
    });

    $("a[name='approveTopic']").on("click",function(){
        var topicId = $(this).attr('topicid');
        layer.confirm('确认审批?', function(){
            $.post(UPDATE_GROUPBUY_STATUS,{
                topicId:topicId,
                status:3
            },function(data){
                if(data.success){
                    location.reload();
                }else {
                    layer.alert(data.msg.message);
                }
            })
        },function(){
        });

    });
    $("a[name='refuseTopic']").on("click",function(){
        var topicId = $(this).attr('topicid');
            $.post(UPDATE_GROUPBUY_STATUS,{
                topicId:topicId,
                status:4
            },function(data){
                if(data.success){
                    location.reload();
                }else {
                    layer.alert(data.msg.message);
                }
            })
    });

    $("a[name='cancelTopic']").on("click",function(){
        var topicId = $(this).attr('topicid');
        $.post(UPDATE_GROUPBUY_STATUS,{
            topicId:topicId,
            status:2
        },function(data){
            if(data.success){
                location.reload();
            }else {
                layer.alert(data.msg.message);
            }
        })
    });

    $("a[name='terminateTopic']").on("click",function(){
        var topicId = $(this).attr('topicid');
        layer.confirm('确认终止?', function(){
            $.post(UPDATE_GROUPBUY_STATUS,{
                topicId:topicId,
                status:5
            },function(data){
                if(data.success){
                    location.reload();
                }else {
                    layer.alert(data.msg.message);
                }
            })
        },function(){
        });
    });



    $("#search").on("click",function(){
       $("form").submit();

    });


});
function show(id,url,text){
    var date = new Date();
    var tv={
        url:url,
        tabId:id,
        text:text,
        doc:false
    };
    parent.window.showTab(tv);
}