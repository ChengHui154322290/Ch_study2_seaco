
var ADD_INVENTORY = domain + "/groupbuy/groupbuyInventoryAdd";
var index = parent.layer.getFrameIndex(window.name);

var GROUPBUY_INVENTORY = "/groupbuy/groupbuyInventory";

$(function () {


    $("#saveInventory").on("click", function () {
       var inventory = $("#modifyInventory").val();
        var topicId = $("#topicId").val();
        var sku = $("#sku").val();
        var wareHouseId = $("#wareHouseId").val();
        var supplierId = $("#supplierId").val();
        $.post(ADD_INVENTORY,{
            topicId:topicId,
            sku:sku,
            wareHouseId:wareHouseId,
            inventory:inventory,
            supplierId:supplierId
        },function(data){
            if(data.success){
                parent.location.reload();
            }else {
                layer.alert(data.msg.message);
            }
        });

    });



 $("#cancelInventory").on("click", function () {
     parent.layer.close(index);
    });


});
