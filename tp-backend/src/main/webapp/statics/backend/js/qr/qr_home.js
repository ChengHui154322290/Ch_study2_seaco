var QR_LIST = domain + "/qr/qrList";
var QR_PDF = domain + "/qr/qrPDF";

$(function () {
    initSelect2();
    /**
     * 查询topic
     */
    $("#genQR").on("click", function () {
        var tid = $("#ti").val();
        if (tid == "" || $.trim(tid) == ""   ) {
            layer.msg("请选择专场信息");
            return;
        }
        var v = $("#version").val();
        var s = $("#size").val();
       loadList(tid,v,s);
    });

  /**
     * 查询topic
     */
    $("#toPDF").on("click", function () {
        var tid = $("#ti").val();
        if (tid == "" || $.trim(tid) == ""   ) {
            layer.msg("请选择专场信息");
            return;
        }
        var v = $("#version").val();
        var s = $("#size").val();
        window.location=QR_PDF+"?tid="+tid+"&s="+s;
      // $.get(QR_PDF,{tid:tid,v:v,s:s});
    });

    /**
     * 高级选项
     */
    $("#uAdv").on("click", function () {
      $("#adv").fadeToggle();
    });


});

function  loadList(tid,v,s){
    $("#qrList").load(QR_LIST,{tid:tid,v:v,s:s});

}


function initSelect2(){
    $(".select2").select2();

};
