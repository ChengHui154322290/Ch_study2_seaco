<#include "/common/common.ftl"/>
<@backend title="团活动查询"
js=['/statics/backend/js/jquery.min.js',
'/statics/backend/js/jquery.tools.js',
'/statics/backend/js/jquery.form.2.2.7.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/supplier/component/date/WdatePicker.js',
'/statics/backend/js/groupbuy/groupbuy_group_list.js',
'/statics/backend/js/json2.js'
]
css=['/statics/backend/js/dateTime2/css/jquery-ui-1.8.17.custom.css',
'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css',
'/statics/supplier/component/date/skin/WdatePicker.css',
'/statics/backend/css/style.css']>
<form class="jqtransform" method="post" id="queryAttForm" action="${domain}/groupbuy/groupbuyGroupList.htm">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">促销管理->团购活动->组团列表</b>
            </div>
            <table id="topicSearch" cellspacing="0" cellpadding="0" border="0" width="100%"
                   class="form_table pt15 pb15">
                <tbody>
                <tr>
                    <td class="td_right" width="50" align="right">活动编号:</td>
                    <td class="" width="150" align="left"><input type="text" id="groupbuyId" name="groupbuyId"
                                                                 class="input-text lh30 topicInteger"
                                                                 value="${query.groupbuyId!}"/></td>
                    <td class="td_right" width="50" align="right">活动名称:</td>
                    <td class="" width="150" align="left"><input type="text" id="topicName" name="topicName"
                                                                 class="input-text lh30" value="${query.topicName!}"/>
                    </td>
                </tr>
                <tr>
                    <td class="td_right" width="50" align="right">有效期:</td>
                    <td class="" width="150" align="left">
                        <input type="text" class="input-text dateInput" width="20px" name="startTime" readonly="true"
                               id="startTime"
                               value="<#if query.startTime??>${query.startTime?string("yyyy-MM-dd hh:ss:ss")}</#if>"
                               onClick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>至
                        <input type="text" class="input-text dateInput" width="20px" name="endTime" readonly="true"
                               id="endTime"
                               value="<#if query.endTime??>${query.endTime?string("yyyy-MM-dd hh:ss:ss")}</#if>"
                               onClick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                    </td>
                    <td class="td_right" width="50" align="right">状态:</td>
                    <td class="" width="150" align="left">
                        <select name="status" class="select" id="status" style="width:150px">
                            <option value=""> 全部</option>
                            <option value="1" <#if ("${query.status}" == "1")>selected</#if>>部分组团</option>
                            <option value="2" <#if ("${query.status}" == "2")>selected</#if>>满额组团</option>
                            <option value="3" <#if ("${query.status}" == "3")>selected</#if>>组团失败</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="td_right" width="50" align="right">用户Id:</td>
                    <td class="" width="150" align="left"><input type="text" id="memberId" name="memberId"
                                                                 class="input-text lh30 topicInteger"
                                                                 value="${query.memberId!}"/></td>
                    <td class="td_right" width="50" align="right">用户手机号:</td>
                    <td class="" width="150" align="left"><input type="text" id="memberName" name="memberName"
                                                                 class="input-text lh30" value="${query.memberName!}"/>
                    </td>
                </tr>
                <tr>
                <tr>
                    <td colspan="8" align="center">
                        <input type="button" id="reset" class="btn btn82 btn_res" value="重置"
                               style="margin-right:50px;"/>
                        <input type="button" id="search" class="btn btn82 btn_search" value="查询"/>
                    </td>
                </tr>
                </tbody>
            </table>
            <div class="search_bar_btn  pt10 pb10 box_border" style="padding-left:5px;">
                <input type="button" id="batchApprove" class="ext_btn" style="display: none" value="批量成团"/>
            </div>
            <input type="hidden" id="batchApprove"/>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr align="center">
                    <th><input type="checkbox" id="allTuan"/></th>
                    <th>团序号</th>
                    <th>团编码</th>
                    <th>活动编号</th>
                    <th>活动名称</th>

                    <th>发起人手机</th>
                    <th>计划人数</th>
                    <th>实际人数</th>
                    <th>状态</th>
                    <th>团开始时间</th>
                    <th>团结束时间</th>
                    <th>操作</th>
                </tr>
                <#if (result.data??)>
                    <#list result.data.rows as info>
                        <tr>
                            <td align="center">
                                <#if (info.status == 1)>
                                    <input type="checkbox" name="selectTuan"/>
                                </#if>
                                <input type="hidden" name="groupid" value="${info.id}"/>
                            </td>
                            <td style="text-align:center;">
                            ${info.id}
                            </td>
                            <td class="td_center" style="width:120px;text-align:center;">${info.code}</td>
                            <td class="td_center">
                                <span>${info.groupbuyId}</span>
                            </td>
                            <td class="td_center" style="width:120px;text-align:center;">
                                <span>${info.topicName}</span>
                            </td>

                            <td class="td_center">${info.memberName}</td>
                            <td class="td_center">${info.planAmount}</td>
                            <td class="td_center">${info.factAmount}</td>
                            <td class="td_center">
                                <#assign sta="${info.status}" />
                                <#if sta=='2'>
                                    满额组团
                                <#elseif sta=='3'>
                                    组团失败
                                <#else>
                                    部分组团
                                </#if>
                            </td>
                            <td><#if (info.startTime??)>${info.startTime?string("yyyy-MM-dd HH:mm:ss")!}</#if></td>
                            <td><#if (info.endTime??)>${info.endTime?string("yyyy-MM-dd HH:mm:ss")!}</#if></td>
                            <td>
                                <a style="padding-right:5px;" name="groupJoin" groupId="${info.id}"
                                   href="javascript:void(0);">[查看团员]</a>
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