<#include "/common/common.ftl"/>
<@backend title="团活动查询"
css=['/statics/backend/js/dateTime2/css/jquery-ui-1.8.17.custom.css',
'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css',
'/statics/supplier/component/date/skin/WdatePicker.css',
'/statics/backend/css/style.css']>
<form class="jqtransform" method="post" id="queryAttForm" action="${domain}/groupbuy/groupbuyGroupList.htm">
    <div class="box">
        <div class="box_border">

            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr align="center">

                    <th>用户Id</th>
                    <th>用户手机号</th>
                    <th>Leader</th>
                    <th>参团时间</th>

                </tr>
                <#if (result.data??)>
                    <#list result.data as info>
                        <tr>

                            <td  class="td_center"> ${info.memberId}</td>
                            <td class="td_center" >${info.memberName}</td>
                            <td class="td_center"><#if info.leader==1> 团长</#if></td>
                            <td class="td_center"><#if info.createTime??>${info.createTime?string("yyyy-MM-dd HH:mm:ss")} </#if> </td>
                        </tr>
                    </#list>
                </#if>
            </table>
        </div>
    </div>

</form>

</@backend>