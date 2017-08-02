var GROUP_JOIN_LIST_URL ='/groupbuy/groupbuyJoinList.htm?groupId=';
$(function () {

   $("#reset").on("click",function(){
       $("#groupbuyId").val("");
       $("#topicName").val("");
       $("#status").val("");
       $("#startTime").val("");
       $("#endTime").val("");
       $("#memberId").val("");
       $("#memberName").val("");
   });

    $("#search").on("click",function(){
       $("#queryAttForm").submit();
    });


    $('a[name="groupJoin"]').on("click",function(){
       var _this = this;
        var groupId = $(_this).attr("groupId");
        $.layer({
            type : 2,
            title : "团员信息",
            shadeClose : true,
            maxmin : true,
            fix : false,
            area : [ '800px', 750 ],
            iframe : {
                src : GROUP_JOIN_LIST_URL+groupId
            },
            end : function(){

            }
        });

    });

});