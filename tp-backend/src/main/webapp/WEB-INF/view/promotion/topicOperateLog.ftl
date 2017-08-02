<#include "/common/common.ftl"/>
<@backend title="促销操作日志"
css=['/statics/backend/js/dateTime2/css/jquery-ui-1.8.17.custom.css',
'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css',
'/statics/supplier/component/date/skin/WdatePicker.css',
'/statics/backend/css/style.css']>
<form class="jqtransform" method="post" id="queryAttForm" action="${domain}/groupbuy/groupbuyGroupList.htm">
    <div class="box">
        <div class="box_border">

            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr align="center">

                    <th style="width: 120px">操作类型</th>
                    <th style=" width: 500px; word-break:break-all">变更内容(括号中为旧值)</th>
                    <th style="width: 40px">操作人</th>
                    <th style="width: 150px">操作时间</th>

                </tr>
                <#if (result.data??)>
                    <#list result.data as info>
                        <tr>

                            <td  class="td_center"> ${info.type}</td>
                            <td class="td_center"  style="500px; word-break:break-all">${info.content}</td>
                            <td class="td_center">${info.createUserName}(${info.createUserId})</td>
                            <td class="td_center"><#if info.createTime??>${info.createTime?string("yyyy-MM-dd HH:mm:ss")} </#if> </td>
                        </tr>
                    </#list>
                </#if>
            </table>
        </div>
    </div>

</form>

</@backend>