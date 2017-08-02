<#include "/common/common.ftl"/>
<@backend title="团购列表"
js=['/statics/backend/js/jquery.min.js',
'/statics/backend/js/jquery.tools.js',
'/statics/backend/js/jquery.form.2.2.7.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/json2.js',
'/statics/backend/js/promotion/utils.js',
'/statics/supplier/component/date/WdatePicker.js',
'/statics/backend/js/editor/kindeditor-all.js',
'/statics/backend/js/groupbuy/groupbuy_editor_utils.js',
'/statics/backend/js/groupbuy/groupbuy_list.js'
]
css=['/statics/backend/js/dateTime2/css/jquery-ui-1.8.17.custom.css',
'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css',
'/statics/backend/css/style.css',
'/statics/supplier/component/date/skin/WdatePicker.css']>
<title>团购列表</title>
<form class="jqtransform" method="post" id="queryAttForm" action="${domain}/groupbuy/groupbuyList.htm">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">团购列表</b>
            </div>
            <table id="topicSearch" cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                <tbody>
                <tr>
                    <td class="td_right" width="50" align="right">TopicId:</td>
                    <td class="" width="150" align="left"><input type="text" id="topicId" name="topicId" class="input-text lh30 topicInteger" value="${query.topicId!}"/></td>
                    <td class="td_right" width="50" align="right">活动名称:</td>
                    <td class="" width="150" align="left"><input type="text" id="name" name="name" class="input-text lh30" value="${query.name!}" /></td>

                    <td class="td_right" width="50" align="right">状态:</td>
                    <td class="" width="150" align="left">
                        <select name="status" class="select" id="status" style="width:150px">
                            <option value="" <#if ("${query.status}"??)>selected</#if>>全部</option>
                            <option value="0" <#if ("${query.status}" == "0")>selected</#if>>编辑中</option>
                            <option value="1" <#if ("${query.status}" == "1")>selected</#if>>审核中</option>
                            <option value="2" <#if ("${query.status}" == "2")>selected</#if>>已取消</option>
                            <option value="3" <#if ("${query.status}" == "3")>selected</#if>>审核通过</option>
                            <option value="4" <#if ("${query.status}" == "4")>selected</#if>>已驳回</option>
                            <option value="5" <#if ("${query.status}" == "5")>selected</#if>>已终止</option>
                        </select>
                    </td>
                </tr>
                <tr style="display: none">

                    <td class="td_right" width="50" align="right">SKU:</td>
                    <td class="" width="150" align="left"><input type="text" id="sku" name="sku" class="input-text lh30" value="${query.sku!}"/></td>
                    <td class="td_right" width="50" align="right">条码:</td>
                    <td class="" width="150" align="left"><input type="text" id="barcode" name="barcode" class="input-text lh30" value="${query.barcode!}" /></td>
                    <td class="td_right" width="50" align="right">商品名称:</td>
                    <td class="" width="150" align="left"><input type="text" id="itemName" name="itemName" class="input-text lh30" value="${query.itemName!}" /></td>
                </tr>
                <tr>
                    <td class="td_right" width="50" align="right">有效期:</td>

                    <td class="" width="150" align="left" colspan="4">
                        <input type="text" class="input-text dateInput" width="20px" name="startTime" readonly="true" id="startTime"   value="<#if query.startTime??>${query.startTime?string('yyyy-MM-dd hh:mm:ss')}</#if>"  onClick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>至
                        <input type="text" class="input-text dateInput" width="20px" name="endTime" readonly="true"  id="endTime" value="<#if query.endTime??>${query.endTime?string('yyyy-MM-dd hh:mm:ss')}</#if>"  onClick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="8" align="center">
                        <input type="button" id="reset" class="btn btn82 btn_res" value="重置" style="margin-right:50px;" />
                        <input type="button" id="search" class="btn btn82 btn_search" value="查询"/>
                    </td>
                </tr>
                </tbody>
            </table>
            <div class="search_bar_btn  pt10 pb10 box_border" style="padding-left:5px;">
                <input type="button" id="add" class="btn btn82 btn_add" sytle="margin-left:20px;margin-right:20px;" value="新增" />
            </div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr align="center">
                    <th>团购序号</th>
                    <th>topicId</th>
                    <th>团活动名称</th>
                    <th>商品条形码</th>
                    <th>SKU</th>
                    <th>商品名称</th>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th>排序</th>
                    <th>状态</th>
                    <th>进度</th>
                    <th>操作</th>
                </tr>
                <#if (result.data??)>
                    <#list result.data.rows as info>
                        <tr>
                            <td class="td_center" style="width:30px;text-align:center;">
                                <span>${info.groupbuyId}</span>
                            </td> <td class="td_center" style="width:30px;text-align:center;">
                                <span>${info.topicId}</span>
                            </td>
                            <td class="td_center" style="text-align:center;">
                                <span>${info.name}</span>
                            </td>
                            <td style="width:80px;text-align:center;">
                            ${info.barcode}
                            </td>
                            <td class="td_center" style="text-align:center;">${info.sku}</td>
                            <td class="td_center" style="text-align:center;">${info.itemName}</td>
                            <td class="td_center" style="text-align:center;"><#if (info.startTime??)>${info.startTime?string("yyyy-MM-dd HH:mm:ss")!}</#if></td>
                            <td class="td_center" style="text-align:center;"><#if (info.endTime??)>${info.endTime?string("yyyy-MM-dd HH:mm:ss")!}</#if></td>
                            <td class="td_center" style="text-align:center; width: 30px"> ${info.sort}</td>
                            <td class="td_center" style="text-align:center; width: 50px">
                                <#assign sta="${info.status}" />
                                <#if sta=='1'>
                                    审核中
                                <#elseif sta=='2'>
                                    已取消
                                <#elseif sta=='3'>
                                    审核通过
                                <#elseif sta=='5'>
                                    终止
                                <#else>
                                    编辑中
                                </#if>
                            </td>
                            <td  class="td_center" style="text-align:center; width: 50px">
                                <#if (1 == info.progress)>
                                    进行中
                                <#elseif (2 == info.progress)>
                                    已结束
                                <#else>
                                    未开始
                                </#if>
                            </td>
                            <td class="td_center" style="text-align:center;">
                                <#if (info.status == 0 || info.status == 4)>
                                    <a style="padding-right:5px;" name="editTopic" topicId="${info.topicId}" href="javascript:void(0);">[编辑]</a>
                                    <a style="padding-right:5px;" name="cancelTopic" topicId="${info.topicId}" href="javascript:void(0);">[取消]</a>
                                </#if>
                                <#if (info.status == 1)>
                                    <a style="padding-right:5px;" name="approveTopic" topicId="${info.topicId}" href="javascript:void(0);">[批准]</a>
                                    <a style="padding-right:5px;" name="refuseTopic" topicId="${info.topicId}" href="javascript:void(0);">[驳回]</a>
                                </#if>
                                <#if (info.status == 3)>
                                    <a style="padding-right:5px;" name="terminateTopic" topicId="${info.topicId}" href="javascript:void(0);">[终止]</a>
                                </#if>
                                <a style="padding-right:5px;" name="viewTopic" topicId="${info.topicId}" href="javascript:void(0);">[详细]</a>
                            </td>
                        </tr>
                    </#list>
                </#if>
            </table>
        </div>
    </div>


    <div style="text-align: left;width: 600px"> <@pager  pagination=result.data  formId="queryAttForm"  /></div>
</form>
</@backend>