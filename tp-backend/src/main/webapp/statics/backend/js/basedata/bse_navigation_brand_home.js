var pageii;
var index = parent.layer.getFrameIndex(window.name);

var ADD_CATEGORY = domain+'/basedata/navigation/doAddBrandAjax.htm';

var UPDATE_CATEGORY_PUBLISH_STATUS = domain +'/basedata/navigation/doUpdatePublishStatusAjax.htm';

var LOAD_SECOND_LEVEL_CATEGORY = domain + '/basedata/navigation/navSecLevelList.htm';

var DEL_CATEGORY = domain + '/basedata/navigation/delCategory.htm';

$(function(){

    jQuery("#cat_table").jqGrid({
        url:'navBrandHomeJSON.htm',
        datatype: "json",
        colNames:['Id','品牌分类名称', '状态','顺序','操作'],
        colModel:[
            {name:'id',index:'id', width:120},
            {name:'name',index:'name', width:200,sortable:false},
            {name:'status',index:'status', width:120, align:"right",sortable:false,formatter:function(cellValue, options, rowObject){
                return cellValue ==0 ?'无效':'有效';
            }},
            {name:'sort',index:'sort', width:120, align:"right"},

            {name:'op',index:'op', width:200, sortable:false,formatter: function(cellValue,options,rowObject){
                var str = '[<a href="javascript:editBrand('+rowObject.id+')"> 编辑</a>]';
                str += '[<a href="javascript:editBrandRange('+rowObject.id+')"> 管理品牌</a>]';
                str +=  '[<a href="javascript:del('+rowObject.id+',\''+'cat_table'+ '\')"> 删除</a>]';
                return str;
            }}
        ],


        loadonce:true,
        viewrecords: true,
        sortorder: "desc",
        caption:"分类导航品牌类目管理",
        subGrid: false,

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
            title: '添加品牌类目',
            shadeClose: true,
            maxmin: true,
            fix : false,
            area: ['600px', 500],
            iframe: {
                src : domain+'/basedata/navigation/navAddBrand.htm'
            }
        });
    });





});
function  editBrand (id){
    pageii=$.layer({
        type : 2,
        title: '编辑品牌分类',
        shadeClose: true,
        maxmin: true,
        fix : false,
        area: ['600px', 500],
        iframe: {
            src : domain+'/basedata/navigation/navEditBrand.htm?id='+id
        }
    });

}
function editBrandRange(id,subgrid_table_id){

    pageii=$.layer({
        type : 2,
        title: '管理筛选条件',
        shadeClose: true,
        maxmin: true,
        fix : false,
        area: ['800px', 500],
        iframe: {
            src : domain+'/basedata/navigation/navBrandRangeAdd.htm?catId='+id+'&subTableId='+subgrid_table_id
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