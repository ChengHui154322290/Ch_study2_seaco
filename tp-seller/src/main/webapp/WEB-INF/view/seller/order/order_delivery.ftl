<#if expressLogs?default([])?size !=0>
<#list expressLogs as expInfo>
<dl class="dl-horizontal">
    <dt>快递公司：${(expInfo.companyName)!}</dt><dd>快递单号：${(expInfo.packageNo)!}</dd>
    <dt>处理时间</dt> <dd>处理信息</dd>
    <#if (expInfo.expressLogInfoList)?exists>
    <#list expInfo.expressLogInfoList as expLog>
    <dt>${(expLog.dataTime)!}</dt> <dd>${(expLog.context)!}</dd>
    </#list>
    </#if>
</dl>
</#list>
<#else>
暂时没有可显示的物流信息
</#if>快递公司：