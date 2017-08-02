var CONFIRM_ITEM_BAR = domain + "/topicItem/itemBarCode/confirmSearch";
var CONFIRM_ITEM_SKU = domain + "/topicItem/itemSKU/confirmSearch";
var ITEM_WH = domain + "/topicItem/wh?";
var ITEM_INVENTORY = domain + "/topicItem/wh/inventory";
var SAVE_GROUP_BUY = domain + "/groupbuy/saveGroupbuyInfo";
var SUB_GROUP_BUY = domain + "/groupbuy/subGroupbuyInfo";
var index = parent.layer.getFrameIndex(window.name);

var GROUPBUY_INVENTORY = "/groupbuy/groupbuyInventory";

$(function () {

    initData();

    //根据SKU获取商品信息
    $("#searchItemSKU").on("click", function () {
        var sku = $("#itemInfo").val();
        if (sku == undefined || sku == null || sku.trim() == '') {
            layer.alert("请输入sku");
            return;
        }
        clearItemInfo();
        getItemInfo(sku);
    });
    //修改库存
    $("#addInventory").on("click", function () {
        var sku = $("#sku").val();
        var topicId = $("#topicId").val();
        var warehouseId =$("#defWarehouseId").val();
        var supplierId =$("#supplierId").val();


        var _this = this;
        var groupId = $(_this).attr("groupId");

        var url = GROUPBUY_INVENTORY + "?sku="+sku+"&topicId="+topicId+"&wareHouseId="+warehouseId+"&supplierId="+supplierId;

        $.layer({
            type : 2,
            title : "修改库存",
            shadeClose : true,
            maxmin : true,
            fix : false,
            area : [ '600px', 400 ],
            iframe : {
                src : url
            },
            end : function(){

            }
        });



    });

    $("#subGroupBuy").on("click", function () {
        var groupbuyInfo = colData();
        $.post(SUB_GROUP_BUY, {params: JSON.stringify(groupbuyInfo)}, function (data) {
            if (data.success) {
                closeCurTab();
            } else {
                layer.alert(data.msg.message);
            }
        })
    });

    $("#saveGroupbuy").on("click", function () {
        var groupbuyInfo = colData();
        $.post(SAVE_GROUP_BUY, {params: JSON.stringify(groupbuyInfo)}, function (data) {
            if (data.success) {
                closeCurTab();
            } else {
                layer.alert(data.msg.message);
            }
        })

    });
    $("#cancel").on("click", function () {
        closeCurTab();
    });


});
function closeCurTab() {
    var index = parent.$("a[class='currenttab']").attr("id");
    var tv = {
        url: "/groupbuy/groupbuyList",
        tabId: "groubuyList",
        text: "团购列表",
        doc: false
    };
    parent.window.showTab(tv);
    parent.window.closeTab(index);
};

function colData() {
    var groupbuyInfo = new Object();
    groupbuyInfo.name = $("#name").val();
    groupbuyInfo.type = $("input[name='type']:checked").val();
    groupbuyInfo.memberLimit = $("#memberLimit").val();
    groupbuyInfo.duration = $("#duration").val();
    groupbuyInfo.startTime = $("#startTime").val();
    groupbuyInfo.endTime = $("#endTime").val();
    groupbuyInfo.sku = $("#sku").val();
    groupbuyInfo.barcode = $("#barcode").val();
    groupbuyInfo.salePrice = $("#salePrice").val();
    groupbuyInfo.itemName = $("#itemName").val();
    groupbuyInfo.itemPic = $("#itemPic").val();
    groupbuyInfo.warehouse = $("#warehouse").val();
    groupbuyInfo.warehouseId = $("#warehouse").val();
    groupbuyInfo.warehouseName = $("#warehouse").find('option:selected').text().trim();
    groupbuyInfo.applyInventory = $("#applyInventory").val();
    groupbuyInfo.groupPrice = $("#groupPrice").val();
    groupbuyInfo.itemId = $("#itemId").val();
    groupbuyInfo.supplierId = $("#supplierId").val();
    groupbuyInfo.supplierName = $("#supplierName").val();
    groupbuyInfo.detail = detailEditor.html();
    groupbuyInfo.brandId = $("#brandId").val();
    groupbuyInfo.countryId = $("#countryId").val();
    groupbuyInfo.countryName = $("#countryName").val();
    groupbuyInfo.spu = $("#spu").val();

    groupbuyInfo.categoryId = $("#categoryId").val();
    groupbuyInfo.limitAmount = $("#limitAmount").val();

    groupbuyInfo.whtype = $("#warehouse").find('option:selected').attr("whtype");
    groupbuyInfo.bondedArea = $("#warehouse").find('option:selected').attr("bondedarea");

    groupbuyInfo.topicId = $("#topicId").val();
    groupbuyInfo.topicItemId = $("#topicItemId").val();
    groupbuyInfo.sort = $("#sort").val();
    groupbuyInfo.introduce = $("#introduce").val();
    return groupbuyInfo;
}

function getItemInfo(sku) {
    $.get(CONFIRM_ITEM_SKU,
        {
            sku: sku,
            brandId: 0,
            spu: 0,
            supplierId: -1
        },
        function (data) {
            if (data.success) {
                if (data.data == null) {
                    layer.alert("商品信息错误");
                    return;
                }
                var skuInfo = data.data;
                setItemInfo(skuInfo);
                getWareHouseInfo(skuInfo, '');

                return;
            }
            layer.alert("没有查询到商品信息:<br\> 1.没有查询到该商品的相关信息<br\> 2.商品详细信息查询失败");
        }
    );
}

function setItemInfo(skuInfo) {
    $("#itemNameSpan").text(skuInfo.name);
    $("#itemName").val(skuInfo.name);
    $("#itemPic").val(skuInfo.topicImage);
    $("#pic").attr("src",skuInfo.imageFullPath);

    $("#skuSpan").text(skuInfo.sku);
    $("#sku").val(skuInfo.sku);

    $("#barcodeSpan").text(skuInfo.barCode);
    $("#barcode").val(skuInfo.barCode);

    $("#salePriceSpan").text(skuInfo.salePrice);
    $("#salePrice").val(skuInfo.salePrice);

    $("#itemId").val(skuInfo.itemId);

    $("#supplierId").val(skuInfo.supplierId);
    $("#supplierName").val(skuInfo.supplierName);

    $("#brandId").val(skuInfo.brandId);

    $("#countryId").val(skuInfo.countryId);
    $("#countryName").val(skuInfo.countryName);

    $("#spu").val(skuInfo.spu);

    $("#categoryId").val(skuInfo.categoryId);

    $("#bondedArea").val(skuInfo.bondedArea);


}

function getWareHouseInfo(skuInfo, defwhId) {
    $.get(ITEM_WH + new Date().getTime(),
        {
            supplierId: skuInfo.supplierId
        }, function (data) {
            $("#whList").html(data);
            var warehoseId = $("#warehouse").val();
            if (warehoseId == undefined || warehoseId == "") {
                return;
            }
            if (defwhId != '') {
                $("#warehouse").val($("#defWarehouseId").val());
                warehoseId = defwhId;
            }

            $("#warehouse").on("change", function () {
                var whId = $("#warehouse").val();
                var sku = $("#sku").val();
                getInventory(sku, whId);
            });
            getInventory(skuInfo.sku, warehoseId);
        }
    )

}

function getInventory(sku, wareHouseId) {
    $.get(ITEM_INVENTORY,
        {
            sku: sku,
            whId: wareHouseId
        }, function (data) {
            $("#currentInventory").text("");
            if (data.success) {
                if (data.data != null) {
                    $("#currentInventory").text(data.data.inventory);
                } else {
                    layer.alert("获取库存信息为空");
                }
                return;
            }
            layer.alert("获取库存信息失败");
        }
    );
}


function clearItemInfo() {
    $("#itemNameSpan").text("");
    $("#itemName").val("");

    $("#skuSpan").text("");
    $("#sku").val("");

    $("#barcodeSpan").text("");
    $("#barcode").val("");

    $("#salePriceSpan").text("");
    $("#salePrice").val("");

    $("#whList").html("");
    $("#currentInventory").text("");


    $("#itemId").val("");

    $("#supplierId").val("");
    $("#supplierName").val("");

    $("#brandId").val("");

    $("#countryId").val("");
    $("#countryName").val("");

    $("#spu").val("");

    $("#categoryId").val("");

    $("#bondedArea").val("");


}

function initData() {
    var supplierId = $("#supplierId").val();
    var warehouseId = $("#defWarehouseId").val();
    var sku = $("#sku").val();
    if (supplierId != '' && warehouseId != '') {
        var obj = new Object();
        obj.supplierId = supplierId;
        obj.sku = sku;
        getWareHouseInfo(obj, warehouseId);

    }
}
