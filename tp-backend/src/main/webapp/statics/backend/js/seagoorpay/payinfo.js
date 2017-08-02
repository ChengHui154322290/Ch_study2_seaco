var CONFIRM_ITEM_BAR = domain + "/topicItem/itemBarCode/confirmSearch";
var CONFIRM_ITEM_SKU = domain + "/topicItem/itemSKU/confirmSearch";
var ITEM_WH = domain + "/topicItem/wh?";
var ITEM_INVENTORY = domain + "/topicItem/wh/inventory";
var ADD_GROUP_BUY_PAGE = "/groupbuy/groupbuyInfo"
var EDIT_GROUP_BUY_PAGE = "/groupbuy/groupbuyInfo?topicId="

var UPDATE_GROUPBUY_STATUS = domain+"/groupbuy/updateGroupbuyStatus";

$(function () {
    initSelect2();



   $("#reset").on("click",function(){
       $("#memberId").val("");
       $("#memberMobile").val("");
       $("#status").val("");
       $("#merchantId").select(0);
       $("#paymentCode").val("");
       $("#merPayCode").val("");

       $("#startTime").val("");
       $("#endTime").val("");
       $("#refundCode").val("");
       $("#merRefundCode").val("");

   });

    $("#search").on("click",function(){
      $("#queryAttForm").submit();

    });


});


function initSelect2(){
    $(".select2").select2();

};