var index = parent.layer.getFrameIndex(window.name);

var ADD_CATEGORY = domain + '/basedata/navigation/doAddAjax.htm';

var ADD_BRAND_RANGE = domain + '/basedata/navigation/doNavBrandRangeAddAjax.htm';


$(function () {

    initData();

    initSelect2();

    $("#close").on("click", function () {
        swfuItem = null;
        parent.layer.close(index);
    });
    $(".closebtn").on("click", function () {
        swfuItem = null;
        parent.layer.close(index);
    });

    $('select.largeIdSel').live("change", function () {
        var _this = $(this);
        var val = $(_this).val();
        if (val == null || val.length == 0) {
            return;
        }
        categoryChange(val, $(_this).parent().find('select.mediumIdSel'));
    });

    $('select.mediumIdSel').live("change", function () {
        var _this = this;
        var val = $(_this).val();
        if (val == null || val.length == 0) {
            return;
        }
        categoryChange(val, $(_this).parent().find('select.smallIdSel'));
    });

    $("[name=add_category]").live('click', function () {
        var newC = $("#category_span_copy").html();
        $("#category_span_1").append(newC);
    });

    $("#add_brand").live('click', function () {
        var newC = $("#brand_span_copy").html();
        newC =  newC.replace("select2copy"," select2add");
        $("#brand_span_1").append(newC);
        initSelect2Add();
        $("select.select2add").each(function(){
            var _this = this;
            $(_this).prop('class',"select2");
        });

    });
    $("#add_sku").live('click', function () {
        var newC = $("#sku_span_copy").html();
        $("#sku_span_1").append(newC);

    });

    $("[name=remove_category]").live('click', function () {
        if ($("[name=remove_category]").length > 2) {
            var _this = this;
            $(_this).parent().remove();
        } else
            alert("不能继续删除，至少保留一行");
    });
    $("[name=remove_sku]").live('click', function () {
        if ($("[name=remove_sku]").length > 2) {
            var _this = this;
            $(_this).parent().remove();
        } else
            alert("不能继续删除，至少保留一行");
    });
    $("[name=remove_brand]").live('click', function () {
        if ($("[name=remove_brand]").length > 2) {
            var _this = this;
            $(_this).parent().remove();
        } else
            alert("不能继续删除，至少保留一行");
    });


    $("#submit_nav_range").on("click", function () {


        var brandArray = new Array();
        $("#brand_span_1").find("span[name='brand_span_2']").each(function () {
            var brand_span = this;
            var brand_id = $(brand_span).find("select[name='brand']").val();
            if (brand_id == null || brand_id == undefined || brand_id == '') {
                return true;
            }
            var sort =  $(brand_span).find("input[name='sort']").val();

            var obj = new Object();
            obj.brandId = brand_id;
            obj.sort = sort;

            brandArray.push(obj);
        });

        if (brandArray.length == 0 ) {
            layer.alert("请至少选择一个品牌");
            return;
        }
        var categoryId = $("#categoryId").val();

        $.post(ADD_BRAND_RANGE,
            {brands: JSON.stringify(brandArray), categoryId: categoryId},
            function (data) {
                if (data.success) {
                    var type = $("#type").val();
                    if (type == 1) {
                        var subTableId = $("#subTableId").val();
                        var targetTable = subTableId + '_' + categoryId + '_t';
                        var subTableEle = parent.$("#" + targetTable);
                        if (subTableEle.length == 0) {
                            parent.$("tr[id='" + categoryId + "']").find("span.ui-icon-plus").click();
                        } else {
                            parent.$("#" + targetTable).jqGrid('setGridParam', {
                                datatype: 'json',
                                postData: {}
                            }).trigger("reloadGrid");
                        }
                    }
                    parent.layer.close(index);
                } else {
                    layer.alert(data.msg.message);
                }
            });
    });


});
function categoryChange(id, target) {
    var url = domain + "/item/category-cld.htm";
    $.get(url, {catId: id}, function (data) {
        if (data) {
            target.html('');
            target.append("<option value=''>--请选择分类--</option>");
            $.each(data, function (i, n) {
                var id = n.id;
                var name = n.name;
                var opt = $("<option />");
                opt.val(id);
                opt.html(name);
                target.append(opt);
            });
        }
    });
}


function initData() {
    var brands = $("#defBrands").val();
    var rangeList =  $("#rangeList").val();

    var barndList = eval(rangeList);

    $.each(barndList, function (i, val) {
        if (val == '') {
            return true;
        }
        if (i == 0) {
            $("#brand_span_2").find('select.select2').val(val.content);
            $("#brand_span_2").find('input[name="sort"]').val(val.sort);
        } else {
            var newB = $('#brand_span_copy').html();
            var idName = "brand_span_edit_" + i;
            newB = newB.replace("brand_span_2", idName).replace("select2copy","select2");
            $("#brand_span_1").append(newB);
            $("#" + idName).find('select.select2').val(val.content);
            $("#" + idName).find('input[name="sort"]').val(val.sort);
        }
    });


}


function initSelect2(){
    $(".select2").select2();
    // $(".select2").css("margin-left","1px");
}
function initSelect2Add(){
    $(".select2add").select2();
    // $(".select2").css("margin-left","1px");
}