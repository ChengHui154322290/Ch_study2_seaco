
var index = parent.layer.getFrameIndex(window.name);

var ADD_CATEGORY = domain+'/basedata/navigation/doAddAjax.htm';

var ADD_CATEGORY_BRAND = domain+'/basedata/navigation/doAddBrandAjax.htm';


var LOAD_SECOND_LEVEL_CATEGORY = domain + '/basedata/navigation/navSecLevelList.htm';

$(function(){

    $("#close").on("click",function(){
        swfuItem = null;
        parent.layer.close(index);
    });$(".closebtn").on("click",function(){
        swfuItem = null;
        parent.layer.close(index);
    });


    $("#datasubmit").on("click" , function(){
        var nav = new Object();
        nav.name = $("#name").val();
        nav.status =  $("input[name='status']:checked").val()
        nav.isHighlight = $("input[name='isHighlight']:checked").val()
        nav.sort = $("#sort").val();
        nav.level = $("#level").val();
        nav.pic = $("#pic").val();
        nav.parentId = $("#parentId").val();
        nav.isPublish = 0;
        nav.id = $("#id").val();
        nav.type = $("#type").val();

        if(nav.name ==null || nav.name.trim()==""){
            layer.alert("分类名称为空");
            return;
        }
        if(  nav.pic=="" || nav.pic== null){
            layer.alert("请上传图片");
            return;
        }

        var str = JSON.stringify(nav);

        var url;
        if(nav.type ==1){
            url = ADD_CATEGORY;
        }else {
            url = ADD_CATEGORY_BRAND;
        }

        $.post(url,{param: str},function(data){
            if(data.success){
                if (nav.type == 1) {
                    if (nav.level == 1) {
                        parent.$("#cat_table").jqGrid('setGridParam', {
                            datatype: 'json',
                            postData: {}
                        }).trigger("reloadGrid");
                        parent.layer.close(index);
                    } else if (nav.level == 2) {
                        var tar_table = 'cat_table_' + nav.parentId + '_t';
                        var tar_table_len = parent.$('#' + tar_table);
                        if (tar_table_len.length == 0) {
                            parent.$("#cat_table").find("tr[id='" + nav.parentId + "']").find("td[aria-describedby='cat_table_subgrid']").click();
                        } else {
                            parent.$("#" + tar_table).jqGrid('setGridParam', {
                                datatype: 'json',
                                postData: {}
                            }).trigger("reloadGrid");
                        }
                        parent.layer.close(index);
                    }
                }else if(nav.type==2){
                    parent.$("#cat_table").jqGrid('setGridParam', {
                        datatype: 'json',
                        postData: {}
                    }).trigger("reloadGrid");
                    parent.layer.close(index);
                }



            }else {
            layer.alert(data.msg.message);

            }
        });

    });

});

function beforeOperation(){

};

function callback(name,url,key) {
    $("#pic" ).val(key); // 文件提取码
    $("#img").attr("src",url);// 文件全路径
    $("#img").css("display","block");

}
