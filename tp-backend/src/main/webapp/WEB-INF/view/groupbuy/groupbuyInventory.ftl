<#include "/common/common.ftl"/>
<@backend title="团购信息"
js=['/statics/backend/js/jquery.min.js',
'/statics/backend/js/jquery.tools.js',
'/statics/backend/js/jquery.form.2.2.7.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/json2.js',
'/statics/backend/js/promotion/utils.js',
'/statics/supplier/component/date/WdatePicker.js',
'/statics/backend/js/editor/kindeditor-all.js',
'/statics/backend/js/groupbuy/groupbuy_editor_utils.js',
'/statics/backend/js/groupbuy/groupbuy_inventory.js'
]
css=['/statics/backend/js/dateTime2/css/jquery-ui-1.8.17.custom.css',
'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css',
'/statics/backend/css/style.css',
'/statics/supplier/component/date/skin/WdatePicker.css']>
<title>修改库存</title>
<form   method="POST" ID="submitTopic">
    <div class="box_border">
        <div class="box_top">
            <b class="pl15">修改库存</b>
        </div>
        <input type="hidden" id="topicId" value="${topicId}">
        <input type="hidden" id="sku" value="${sku}">
        <input type="hidden" id="wareHouseId" value="${wareHouseId}">
        <input type="hidden" id="supplierId" value="${supplierId}">
        <div class="box_center">

            <table id="tuanTopicTable" class="input tabContent">
                <tr>
                    <td class="td_right">
                        商品现有库存:
                    </td>
                    <td style="width:200px"><label>${info.inventory}</label>
                    </td>
                </tr>
                <tr>
                    <td class="td_right">
                       本次活动剩余库存:
                    </td>
                    <td style="width:200px">
                        <label> ${info.left}</label>
                    </td>
                    </tr>

                <tr>
                    <td class="td_right">
                        <strong style="color:red;">*</strong>本次申请库存:
                    </td>
                    <td style="width:200px">
                        <input type="text" id="modifyInventory" name="modifyInventory" maxlength="9" class="input-text lh30" value=""/>
                    </td>

                </tr>
            </table>

            <table id="tuanTopicButton" class="input tabContent">
                <tr>
                    <td align="center">
                        <input type="button" class="ext_btn ext_btn_submit" id="saveInventory" value="保存"/>
                        <input type="button" class="ext_btn ext_btn_submit" id="cancelInventory" value="取消"/>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</form>
</@backend>