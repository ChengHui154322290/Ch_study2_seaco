<#include "/commons/base/page.ftl" />
<div class="widget-header header-color-blue">
	<h5 class="bigger lighter">
	<i class="icon-table"></i>
		总数：${(page.records)!0}
	</h5>												
</div>
<table class="table table-striped table-bordered table-hover">
    <thead>
        <tr>
            <th>订单编号</th>
            <th>支付单号</th>
            <th><i class="icon-time bigger-110 hidden-480"></i>支付时间</th>
            <th><i class="icon-time bigger-110 hidden-480"></i>发货时间</th>
            <th><i class="icon-time bigger-110 hidden-480"></i>下单时间</th>
            <th>订单金额</th>
            <th>订单类型</th>
            <th>订单状态</th>
            <th>操作</th>
        </tr>
    </thead>
    <tbody>
        <#if (page.rows)?default([])?size !=0>       
        <#list page.rows as order>
        <tr>
            <td>${(order.orderCode)!}</td>
            <td>${(order.payCode)!}</td>
            <td>${(order.payTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
            <td>${(order.deliveredTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
            <td>${(order.createTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
            <td>${(order.total?string("0.00"))!}</td>
            <td>${(order.typeStr)!}</td>
            <td>${(order.statusStr)!} <#if order.type==7 && order.orderStatus gte 3 && order.orderStatus lt 6>　<span class="fastorderoverspan" style="color:blue;"><#if order.overTime gt 0 >还剩<#else>超时</#if><span class="fastorderovertime">${order.overTime}</span>分</span></#if></td>
            <td>
            	<a href="javascript:void(0)" onclick="showOrderTab('${(order.orderCode)!}')">查看</a>
            </td>
        </tr>
        </#list>
        </#if>
    </tbody>
</table>
<#if page?exists>
<@p page=page.page totalpage=page.total />
</#if>
