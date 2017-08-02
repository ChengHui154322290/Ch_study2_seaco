var pageii;
var index = parent.layer.getFrameIndex(window.name);

var ADD_CATEGORY = domain+'/basedata/navigation/doAddAjax.htm';

var UPDATE_CATEGORY_PUBLIST_STATUS = domain +'/basedata/navigation/doUpdatePublishStatusAjax.htm';

var LOAD_SECOND_LEVEL_CATEGORY = domain + '/basedata/navigation/navSecLevelList.htm';

var DEL_CATEGORY = domain + '/basedata/navigation/delCategory.htm';

$(function(){

    jQuery("#cat_table").jqGrid({
        url:'navHomeJSON.htm',
        datatype: "json",
        colNames:['Id','类目名称', '类目级别', '状态','顺序','图片','操作'],
        colModel:[
            {name:'id',index:'id', width:120},
            {name:'name',index:'name', width:200,sortable:false},
            {name:'level',index:'level', width:120,sortable:false},
            {name:'status',index:'status', width:120, align:"right",sortable:false,formatter:function(cellValue, options, rowObject){
                return cellValue ==0 ?'无效':'有效';
            }},
            {name:'sort',index:'sort', width:120, align:"right"},

            {name:'pic',index:'pic', width:120, sortable:false},
            {name:'op',index:'op', width:250, sortable:false,formatter: function(cellValue,options,rowObject){
                var str = '[<a href="javascript:editFirstLevel('+rowObject.id+')"> 编辑</a>]';
                str += '[<a href="javascript:addSecondLevel('+rowObject.id+')"> 添加二级分类</a>]';
                str += '[<a href="javascript:addRange('+rowObject.id+ '\,'+ '\'\''+  ')"> 筛选信息</a>]';
                str +=  '[<a href="javascript:del('+rowObject.id+',\''+'cat_table'+ '\')"> 删除</a>]';

                return str;
            }}
        ],


        loadonce:true,
        viewrecords: true,
        sortorder: "desc",
        caption:"分类导航类目管理",
        subGrid: true,
        subGridRowExpanded: function(subgrid_id, row_id) {
            // we pass two parameters
            // subgrid_id is a id of the div tag created whitin a table data
            // the id of this elemenet is a combination of the "sg_" + id of the row
            // the row_id is the id of the row
            // If we wan to pass additinal parameters to the url we can use
            // a method getRowData(row_id) - which returns associative array in type name-value
            // here we can easy construct the flowing
            var subgrid_table_id, pager_id;
            subgrid_table_id = subgrid_id+"_t";
            pager_id = "p_"+subgrid_table_id;
            $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
            jQuery("#"+subgrid_table_id).jqGrid({
                url:"navHomeJSON.htm?q=2&parentId="+row_id,
                datatype: "json",
                viewrecords: true,
                colNames:[],
                colModel:[
                    {name:'id',index:'id', width:84},
                    {name:'name',index:'name', width:199},
                    {name:'level',index:'level', width:118},
                    {name:'status',index:'status', width:117, align:"right", formatter:function(cellValue, options, rowObject){
                      return cellValue ==0 ?'无效':'有效';
                    }},
                    {name:'sort',index:'sort', width:115, align:"right"},

                    {name:'op',index:'op', width:195, sortable:false, formatter : function (cellValue,options,rowObject) {
                        var str = '[<a href="javascript:editFirstLevel('+rowObject.id+')"> 编辑</a>]';
                        str += '[<a href="javascript:addRange('+rowObject.id+',\''+subgrid_table_id+'\')"> 筛选信息</a>]';
                        str+= '[<a href="javascript:del('+rowObject.id+',\''+subgrid_table_id+'\')"> 删除</a>]';

                        return str;
                    }}
                ],
                loadonce:true,
                forceFit:true,
                scrolling:false,
                height: '100%',
                width:'110%',
                subGrid: true,
                subGridRowExpanded: function(subgrid_id, row_id) {
                    // we pass two parameters
                    // subgrid_id is a id of the div tag created whitin a table data
                    // the id of this elemenet is a combination of the "sg_" + id of the row
                    // the row_id is the id of the row
                    // If we wan to pass additinal parameters to the url we can use
                    // a method getRowData(row_id) - which returns associative array in type name-value
                    // here we can easy construct the flowing
                    var subgrid_table_id, pager_id;
                    subgrid_table_id = subgrid_id+"_t";
                    pager_id = "p_"+subgrid_table_id;
                    $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
                    jQuery("#"+subgrid_table_id).jqGrid({
                        url:"navRangeJSON.htm?q=2&cateId="+row_id,
                        datatype: "json",
                        viewrecords: true,
                        colNames:['Id','条件信息'],
                        forceFit:true,
                        colModel:[
                            {name:'id',index:'id',hidden:true, width:55},
                            {name:'content',index:'content', width:300, align:"left"},

                        ],
                        loadonce:true,
                        height: '90%',
                        gridComplete: function () {//this对象为数据列表表格，所以找到数据内容表格和表头的容器后在查找ui-jqgrid-hdiv，表头容器
                            $(this).closest('.ui-jqgrid-view').find('div.ui-jqgrid-hdiv').hide();
                            jQuery("#id").closest(".ui-jqgrid-bdiv").css({ 'overflow-x' : 'hidden' });
                        }
                    });
                },
                subGridRowColapsed: function(subgrid_id, row_id) {
                    // this function is called before removing the data
                    //var subgrid_table_id;
                    //subgrid_table_id = subgrid_id+"_t";
                    //jQuery("#"+subgrid_table_id).remove();
                },
                gridComplete: function () {//this对象为数据列表表格，所以找到数据内容表格和表头的容器后在查找ui-jqgrid-hdiv，表头容器
                    $(this).closest('.ui-jqgrid-view').find('div.ui-jqgrid-hdiv').hide()
                }
            });
        },
        subGridRowColapsed: function(subgrid_id, row_id) {
            // this function is called before removing the data
            //var subgrid_table_id;
            //subgrid_table_id = subgrid_id+"_t";
            //jQuery("#"+subgrid_table_id).remove();
        }

    });

    $("#cat_table").setGridHeight('600px');

    $("#close").on("click",function(){
        parent.layer.close(index);
    });

    $('.closebtn').on('click',function(){
        parent.layer.close(parent.pageii);
    });


    $('.add_nav_cat').live('click',function(){
        pageii=$.layer({
            type : 2,
            title: '添加分类',
            shadeClose: true,
            maxmin: true,
            fix : false,
            area: ['600px', 500],
            iframe: {
                src : domain+'/basedata/navigation/navAddFirstLevel.htm'
            }
        });
    });
   $('.add_nav_cat_sec').live('click',function(){
       var th = this;
       var id =  $(th).attr('parentid');
        pageii=$.layer({
            type : 2,
            title: '添加二级分类',
            shadeClose: true,
            maxmin: true,
            fix : false,
            area: ['600px', 500],
            iframe: {
                src : domain+'/basedata/navigation/navAddSecLevel.htm?parentId='+id
            }
        });
    });




    $('.for_edit').live('click',function(){
        var th = this;
       var id =  $(th).attr('param');
        pageii=$.layer({
            type : 2,
            title: '添加分类',
            shadeClose: true,
            maxmin: true,
            fix : false,
            area: ['600px', 500],
            iframe: {
                src : domain+'/basedata/navigation/navEditFirstLevel.htm?id='+id
            }
        });

    });

    $('.for_pub').live("click" ,function(){
        var th = this;
        var c_id = $(th).attr("param");
        var processingLayer = layer.load("处理中...");
        $.post(UPDATE_CATEGORY_PUBLIST_STATUS,{id:c_id,publish:1},function(data){
            layer.close(processingLayer);
           if(data.success){
               parent.window.location.reload();
           }else {
               layer.alert(data.msg.message);
           }
        });
    });

    $('.for_un_pub').live("click" ,function(){
        var th = this;
        var c_id = $(th).attr("param");
        var processingLayer = layer.load("处理中...");
        $.post(UPDATE_CATEGORY_PUBLIST_STATUS,{id:c_id,publish:0},function(data){
            layer.close(processingLayer);
            if(data.success){
                parent.window.location.reload();
            }else {
                layer.alert(data.msg.message);
            }
        });
    });

    $('.for_load_sec_level').on("click" ,function(){
        var th = this;
        var c_id = $(th).attr("param");
        var show = $(th).attr("showSec");

       var a =  $("#tr_sec_level_"+c_id);
        var div_load = $("#sec_level_"+c_id);
        var display_status = a.css("display");
        console.log("display_status:::"+display_status);
        if(show==undefined|| show ==null||show =="" ||show=="0"){
            var show = $(th).attr("showSec","1");
            div_load.load(LOAD_SECOND_LEVEL_CATEGORY,{parentId:c_id});
            a.show();
        }else {
            var show = $(th).attr("showSec","0");
            a.hide();
        }

    });

});
function  editFirstLevel (id){
    pageii=$.layer({
        type : 2,
        title: '添加分类',
        shadeClose: true,
        maxmin: true,
        fix : false,
        area: ['600px', 500],
        iframe: {
            src : domain+'/basedata/navigation/navEditFirstLevel.htm?id='+id
        }
    });

}
function addSecondLevel(parentId){

    pageii=$.layer({
        type : 2,
        title: '添加二级分类',
        shadeClose: true,
        maxmin: true,
        fix : false,
        area: ['600px', 500],
        iframe: {
            src : domain+'/basedata/navigation/navAddSecLevel.htm?parentId='+parentId
        }
    });
};
function addRange(id,subgrid_table_id){

    pageii=$.layer({
        type : 2,
        title: '添加条件',
        shadeClose: true,
        maxmin: true,
        fix : false,
        area: ['800px', 500],
        iframe: {
            src : domain+'/basedata/navigation/navRangeAdd.htm?catId='+id+'&subTableId='+subgrid_table_id
        }
    });


};


function del(id,table_id){
    layer.confirm("确认删除?",function( index){
        $.post(DEL_CATEGORY,{id:id},function(data){
            if(data.success){
                $("#"+table_id).jqGrid('setGridParam',{
                    datatype:'json',
                    postData:{}
                }).trigger("reloadGrid");
                layer.close(index);
            }else {
                layer.alert(data.msg.message);
            }

        })

    })
}