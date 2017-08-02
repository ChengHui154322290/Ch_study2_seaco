var index = parent.layer.getFrameIndex(window.name);

var ADD_CATEGORY = domain + '/basedata/navigation/doAddAjax.htm';

var ADD_CATEGORY_RANGE = domain + '/basedata/navigation/doNavRangeAddAjax.htm';

var LOAD_SECOND_LEVEL_CATEGORY = domain + '/basedata/navigation/navSecLevelList.htm';

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
        var cateArray = new Array();
        $("#category_span_1").find("span[name='category_span_2']").each(function () {
            var cate_span = this;
            var l_cate = $(cate_span).find("select.largeIdSel").val();
            var m_cate = $(cate_span).find("select.mediumIdSel").val();
            var s_cate = $(cate_span).find("select.smallIdSel").val();

            if (l_cate == null || l_cate == undefined || l_cate == '') {
                return true;
            }
            if (m_cate == null || m_cate == undefined || m_cate == '') {
                s_cate = '';
            }
            var to = l_cate + "," + m_cate + "," + s_cate;
            cateArray.push(to);
        });

        var brandArray = new Array();
        $("#brand_span_1").find("span[name='brand_span_2']").each(function () {
            var brand_span = this;
            var brand_id = $(brand_span).find("select[name='brand']").val();
            if (brand_id == null || brand_id == undefined || brand_id == '') {
                return true;
            }
            brandArray.push(brand_id);
        });

        if (brandArray.length == 0 && cateArray.length == 0) {
            layer.alert("请至少选择一个品牌或分类");
            return;
        }
        var categoryId = $("#categoryId").val();

        $.post(ADD_CATEGORY_RANGE,
            {categories: JSON.stringify(cateArray), brands: JSON.stringify(brandArray), categoryId: categoryId},
            function (data) {
                if (data.success) {
                    var type = $("#type").val();
                    var subTableId = $("#subTableId").val();
                    if (type == 1 && subTableId != undefined && subTableId != '') {
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
    var categories = $("#defCategories").val();

    var brandArray = brands.split(",");
    var categoryArray = categories.split(",");
    $.each(categoryArray, function (i, val) {
        if (val == "") {
            return true;
        }
        var cateAr = val.split("-");
        var l_cat = cateAr[0];
        var m_cat = cateAr[1];
        var s_cat = cateAr[2];
        if (l_cat == "") {
            return true;
        }
        if (i == 0) {
            $("#category_span_1").find("select.largeIdSel").val(l_cat);
            var m = $("#category_span_1").find("select.mediumIdSel");

            $("#category_span_0").find("select.largeIdSel").val(l_cat);
            var url = domain + "/item/category-cld.htm";
            $.ajax({
                url: url,
                type: 'get',
                data: {catId: l_cat},
                success: function (data) {
                    if (data) {
                        $("#category_span_0").find('select.mediumIdSel').html('');
                        $("#category_span_0").find('select.mediumIdSel').append("<option value=''>--请选择分类--</option>");
                        $.each(data, function (i, n) {
                            var id = n.id;
                            var name = n.name;
                            var opt = $("<option />");
                            opt.val(id);
                            opt.html(name);
                            $("#category_span_0").find('select.mediumIdSel').append(opt);
                        });
                        if (m_cat != null && m_cat != "") {
                            $("#category_span_0").find("select.mediumIdSel").val(m_cat);
                            $.ajax({
                                url: url,
                                data: {catId: m_cat},
                                success: function (data) {
                                    if (data) {
                                        $("#category_span_0").find('select.smallIdSel').html('');
                                        $("#category_span_0").find('select.smallIdSel').append("<option value=''>--请选择分类--</option>");
                                        $.each(data, function (i, n) {
                                            var id = n.id;
                                            var name = n.name;
                                            var opt = $("<option />");
                                            opt.val(id);
                                            opt.html(name);
                                            $("#category_span_0").find('select.smallIdSel').append(opt);
                                        });
                                        if (s_cat != null && s_cat != "") {
                                            $("#category_span_0").find("select.smallIdSel").val(s_cat);
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });
        } else {
            var newC = $("#category_span_copy").html();
            var idName = "category_span_edit_" + i;
            newC = newC.replace("category_span_0", idName);
            $("#category_span_1").append(newC);
            $("#" + idName).find("select.largeIdSel").val(l_cat);
            var url = domain + "/item/category-cld.htm";
            $.ajax({
                url: url,
                type: 'get',
                data: {catId: l_cat},
                success: function (data) {
                    if (data) {
                        $("#" + idName).find('select.mediumIdSel').html('');
                        $("#" + idName).find('select.mediumIdSel').append("<option value=''>--请选择分类--</option>");
                        $.each(data, function (i, n) {
                            var id = n.id;
                            var name = n.name;
                            var opt = $("<option />");
                            opt.val(id);
                            opt.html(name);
                            $("#" + idName).find('select.mediumIdSel').append(opt);
                        });
                        if (m_cat != null && m_cat != "") {
                            $("#" + idName).find("select.mediumIdSel").val(m_cat);
                            $.ajax({
                                url: url,
                                type: 'get',
                                data: {catId: m_cat},
                                success: function (data) {
                                    if (data) {
                                        $("#" + idName).find('select.smallIdSel').html('');
                                        $("#" + idName).find('select.smallIdSel').append("<option value=''>--请选择分类--</option>");
                                        $.each(data, function (i, n) {
                                            var id = n.id;
                                            var name = n.name;
                                            var opt = $("<option />");
                                            opt.val(id);
                                            opt.html(name);
                                            $("#" + idName).find('select.smallIdSel').append(opt);
                                        });
                                        if (s_cat != null && s_cat != "") {
                                            $("#" + idName).find("select.smallIdSel").val(s_cat);
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
    });

    $.each(brandArray, function (i, val) {
        if (val == '') {
            return true;
        }
        if (i == 0) {
            $("#brand_span_2").find('select.select2').val(val);
        } else {
            var newB = $('#brand_span_copy').html();
            var idName = "brand_span_edit_" + i;
            newB = newB.replace("brand_span_2", idName).replace("select2copy","select2");
            $("#brand_span_1").append(newB);
            $("#" + idName).find('select.select2').val(val);
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