<#if queryExpressInfo?default([])?size !=0>
<#list queryExpressInfo as expInfo>
<table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
    <tr><td>快递公司：${(expInfo.companyName)!}</td><td>快递单号：${(expInfo.packageCode)!}</td></tr>
    <tr><td>处理时间</td> <td>处理信息</td></tr>
    <#if (expInfo.expressLogInfoList)?exists>
    <#list expInfo.expressLogInfoList as expLog>
    <tr><td>${(expLog.dataTime)!}</td> <td>${(expLog.context)!}</td></tr>
    </#list>
    </#if>
</table>
</#list>
<#else>
暂时没有可显示的物流信息
</#if>
