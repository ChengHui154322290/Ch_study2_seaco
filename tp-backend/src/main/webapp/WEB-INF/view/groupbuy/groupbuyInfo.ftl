<#include "/common/common.ftl"/>
<@backend title="团购信息"
js=['/statics/backend/js/jquery.min.js',
'/statics/backend/js/jquery.tools.js',
'/statics/backend/js/jquery.form.2.2.7.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/json2.js',
'/statics/backend/js/promotion/utils.js',
'/statics/supplier/component/date/WdatePicker.js',
'/statics/qiniu/js/plupload/plupload.full.min.js',
'/statics/qiniu/js/plupload/plupload.dev.js',
'/statics/qiniu/js/plupload/moxie.js',
'/statics/qiniu/js/plupload/moxie.js',
'/statics/qiniu/src/qiniu.js',
'/statics/qiniu/js/highlight/highlight.js',
'/statics/qiniu/js/ui.js',
'/statics/qiniu/xgUplod.js',

'/statics/backend/js/editor/kindeditor-all.js',
'/statics/backend/js/groupbuy/groupbuy_editor_utils.js',
'/statics/backend/js/groupbuy/groupbuy_info.js',
'/statics/backend/js/groupbuy/groupbuy_upload.js'

]
css=['/statics/backend/js/dateTime2/css/jquery-ui-1.8.17.custom.css',
'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css',
'/statics/backend/css/style.css',
'/statics/supplier/component/date/skin/WdatePicker.css']>
<title>团购信息</title>
<form method="POST" ID="submitTopic">
    <div class="box_border">
        <div class="box_top">
            <b class="pl15">团购信息</b>
        </div>
        <div class="box_center">


            <input type="hidden" id="supplierId" value="${groupbuy.supplierId}"/>
            <input type="hidden" id="supplierName" value="${groupbuy.supplierName}"/>

            <input type="hidden" id="itemId" value="${groupbuy.itemId}"/>
            <input type="hidden" id="topicId" value="${groupbuy.topicId}"/>
            <input type="hidden" id="topicItemId" value="${groupbuy.topicItemId}"/>

            <input type="hidden" id="brandId" value="${groupbuy.brandId}"/>

            <input type="hidden" id="warehouseName" value="${groupbuy.warehouseName}"/>

            <input type="hidden" id="countryId" value="${groupbuy.countryId}"/>
            <input type="hidden" id="countryName" value="${groupbuy.countryName}"/>

            <input type="hidden" id="categoryId" value="${groupbuy.categoryId}"/>

            <input type="hidden" id="bondedArea" value="${groupbuy.bondedArea}"/>
            <input type="hidden" id="spu" value="${groupbuy.spu}"/>

            <input type="hidden" id="defWarehouseId" value="${groupbuy.warehouseId}">


            <input type="hidden" id="bucketname" name="bucketname" value="${bucketname}"/>
            <input type="hidden" id="bucketURL" name="bucketURL" value="${bucketURL}"/>


            <table id="tuanTopicTable" class="input tabContent">
                <tr>
                    <td class="td_right">
                        <strong style="color:red;">*</strong>活动名称:
                    </td>
                    <td style="width:700px"><input type="text" id="name" name="name" class="input-text lh30"
                                                   value="${groupbuy.name}"/>
                    </td>

                    <td class="td_right">
                        <strong style="color:red;">*</strong>活动类型:
                    </td>
                    <td style="width:700px">
                        <label><input type="radio" id="type" name="type"
                                      class="input-text lh30" <#if groupbuy.type==2> <#else > checked </#if> value="1"/>普通团</label>
                        <label><input type="radio" id="type" name="type" class="input-text lh30" <#if groupbuy.type==2>
                                      checked </#if> value="2"/>新人团</label>
                    </td>

                <tr>
                <tr>
                    <td class="td_right">
                        <strong style="color:red;">*</strong>成团人数:
                    </td>
                    <td style="width:700px"><input type="text" id="memberLimit" name="memberLimit" maxlength="3"
                                                   class="input-text lh30" value="${groupbuy.memberLimit}"/>
                    </td>

                    <td class="td_right">
                        <strong style="color:red;">*</strong>持续时间:
                    </td>
                    <td style="width:700px">
                        <input type="text" id="duration" maxlength="5" name="duration" class="input-text lh30"
                               value="${groupbuy.duration}"/>(单位:小时)
                    </td>

                <tr>

                <tr>
                    <td class="td_right">
                        <strong style="color:red;">*</strong>活动开始时间:
                    </td>
                    <td style="width:700px"><input type="text" id="startTime" name="startTime"
                                                   class="input-text lh30"
                                                   value="<#if groupbuy.startTime??>${groupbuy.startTime?string("yyyy-MM-dd hh:mm:ss")}</#if>"
                                                   onClick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                    </td>

                    <td class="td_right">
                        <strong style="color:red;">*</strong>活动结束时间:
                    </td>
                    <td style="width:700px">
                        <input type="text" id="endTime" name="endTime" class="input-text lh30"
                               value="<#if groupbuy.endTime??>${groupbuy.endTime?string("yyyy-MM-dd hh:mm:ss")}</#if>"
                               onClick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                    </td>

                <tr>
                <tr>
                    <td class="td_right">活动介绍:
                    </td>
                    <td style="width:700px" colspan="3"><input placeholder="在副标题中展示,选填" class="input-text lh30" style="width: 700px" type="text" id="introduce" value="${groupbuy.introduce}" name="introduce"/>

                    </td>


                <tr>

                <tr>
                    <td class="td_right">
                        <strong style="color:red;">*</strong>商品:
                    </td>
                    <td style="width:700px" colspan="1">
                        <#if mode == 0>
                            <input type="text" id="itemInfo" name="itemInfo" placeholder="输入SKU" class="input-text lh30"
                                   value="${groupbuy.sku}"/>
                            <input type="button" id="searchItemSKU" class="ext_btn " value="查询SKU"></input>
                        <#else >
                            <input type="text" id="itemInfo" name="itemInfo" readonly placeholder="输入SKU"
                                   class="input-text lh30" value="${groupbuy.sku}"/>
                        </#if>

                    <#--<#if groupbuy.status ==3><input type="button"  id="addInventory" topicId=""  class="ext_btn " value="加库存"></input></#if>-->
                    </td>
                    <td class="td_right">排序:
                    </td>
                    <td style="width:700px" colspan="">
                        <input type="text" id="sort" name="sort" placeholder="默认0,小号在前" class="input-text lh30" maxlength="8"
                               value="${groupbuy.sort}"/>
                    </td>

                <tr>
                    <td class="td_right">商品名称:</td>
                    <td colspan="3">
                        <span id="itemNameSpan">${groupbuy.itemName}</span>
                        <input type="hidden" id="itemName" name="itemName" value="${groupbuy.itemName}">
                        <input type="hidden" id="itemPic" name="itemPic" value="${groupbuy.itemPic}">
                    </td>

                </tr>
                <tr>
                    <td class="td_right">商品SKU:</td>
                    <td colspan="3">
                        <span id="skuSpan">${groupbuy.sku}</span>
                        <input type="hidden" id="sku" name="sku" value="${groupbuy.sku}">
                    </td>


                </tr>
                <tr>
                    <td class="td_right">商品条码:</td>
                    <td colspan="3">
                        <span id="barcodeSpan">${groupbuy.barcode}</span>
                        <input type="hidden" name="barcode" id="barcode" value="${groupbuy.barcode}">
                    </td>
                </tr>
                <tr>
                    <td class="td_right">商品原价:</td>
                    <td colspan="3">
                        <span id="salePriceSpan">${groupbuy.salePrice}</span>
                        <input type="hidden" id="salePrice" name="salePrice" value="${groupbuy.salePrice}"/>
                    </td>
                </tr>
                <tr>
                    <td class="td_right">图片:</td>
                    <td colspan="3">
                    <#--<div id="container">-->
                    <#--<img width="80px" height="80px" id="pic" src="${pic}"></img>-->
                    <#--<input type="button" class="btn btn-default btn-lg " id="pickfiles" href="#" imagenameattribute="logo">-->
                    <#--<i class="glyphicon glyphicon-plus"></i>-->
                    <#--<button>选择文件</button>-->
                    <#--</input>-->
                    <#--</div>-->
                        <div id="container"
                        ">
                        <img width="80px" height="80px" id="pic" src="${pic}"></img>
                        <a class="btn btn-default btn-lg " id="pickfiles" href="#" imagenameattribute="logo">
                            <i class="glyphicon glyphicon-plus"></i>
                            <button>选择文件</button>
                        </a>(尺寸:750*328px)
        </div>
        </td>
        </tr>
        <tr>
            <td class="td_right" width="50" align="right">仓库:</td>
            <td colspan="3">
                <div id="whList">
                    <select class="select" style="width:150px;" id="warehouse">
                        <#if (info.warehouseList??)>
                            <#list info.warehouseList as warehouse>
                                <option value="${warehouse.id}" bondedArea="${warehouse.bondedArea}"
                                        whType="${warehouse.type}">
                                ${warehouse.name}
                                </option>
                            </#list>
                        </#if>
                    </select>
                </div>
            </td>
        </tr>

        <tr>
            <td class="td_right">商品现有库存:</td>
            <td colspan="3">
                <span id="currentInventory"></span>
            </td>


        </tr>
        <tr>
            <td class="td_right"><strong style="color:red;">*</strong>活动申请库存:</td>
            <td colspan="3">
                <input type="text" class="input-text lh30" id="applyInventory" name="applyInventory"
                       value="${groupbuy.applyInventory}"></input><#if inventoryInfo >(${inventoryInfo})</#if>
            </td>
        </tr>

        <tr>
            <td class="td_right"><strong style="color:red;">*</strong>商品团购价:</td>
            <td colspan="3">
                <input type="text" class="input-text lh30" id="groupPrice" name="groupPrice"
                       value="${groupbuy.groupPrice}"></input>
            </td>
        </tr>
        <tr>
            <td class="td_right"><strong style="color:red;">*</strong>团购限购数量:</td>
            <td colspan="3">
                <input type="text" class="input-text lh30" id="limitAmount" name="limitAmount"
                       value="${groupbuy.limitAmount}"></input>
            </td>
        </tr>
        <tr>
            <td class="td_right"><strong style="color:red;">*</strong>活动详情:</td>
            <td colspan="3">
                <textarea id="detail" name="detail">${groupbuy.detail}</textarea>
            </td>
        </tr>


        </table>
        <#if (info.auditInfoList??)>
            <table id="topicAuditTable" class="input tabContent">
                <tr align="center">
                    <th>姓名</th>
                    <th>操作</th>
                    <th>备注</th>
                    <th>时间</th>
                </tr>
                <#list info.auditInfoList as auditLog>
                    <tr align="center" style="background-color: rgb(255, 255, 255);">
                        <td>
                            <span>${auditLog.auditName!}</span>
                        </td>
                        <td>
                            <span>${auditLog.auditOperation!}</span>
                        </td>
                        <td>
                            <span>${auditLog.remark!}</span>
                        </td>
                        <td>
                            <#if (auditLog.createTime??)>
                                <span>${auditLog.createTime?string("yyyy-MM-dd HH:mm:ss")!}</span>
                            </#if>
                        </td>
                    </tr>
                </#list>
            </table>
        </#if>
        <table id="tuanTopicButton" class="input tabContent">
            <tr>
                <td align="center">

                    <#if mode==0>
                        <input type="button" class="ext_btn ext_btn_submit" id="saveGroupbuy" value="保存"/>

                        <input type="button" class="ext_btn ext_btn_submit" id="subGroupBuy" value="提交"/>
                    <#else >
                    	<!--
                        <input type="button" class="ext_btn ext_btn_submit" id="subGroupBuy" value="提交"/>
                        -->
                    </#if>

                    <input type="button" class="ext_btn ext_btn_submit" id="cancel" value="取消"/>
                </td>
            </tr>
        </table>
    </div>
    </div>
</form>
</@backend>