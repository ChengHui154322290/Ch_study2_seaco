<#include "/layout/inner_layout.ftl" />

<@sellContent title="首页" 
    js=['/static/scripts/web/order_list.js']
    css=[]>
    <script type="text/javascript" charset="utf-8">
        var deliveryWayStr = ${deliveryWayStr};
    </script>
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">商家订单查询</h3>
    </div>
    <div class="panel-body">
        <form class="form-inline" role="form" id="queryFormId">
            <table width="100%" height="200">
                <tr>
                    <td>订单编号：</td>
                    <td><input type="text" name="orderCode" class="form-control" ></td>
                    <td>下单时间：</td>
                    <td><input id="startDate"  name="startTime" class="form-control" onfocus="setDate()" type="text"></td>
                    <td width="10%">至</td>
                    <td><input id="endDate" name="endTime"  class="form-control" onfocus="setDate()" type="text"></td>
                    </tr>
                    
                    <tr>
                    <td>订单状态：</td>
                    <td>
                        <select class="form-control" name="orderStatus">
                            <option value="">全部</option>
                            <#list orderStatusMap?keys as key>
                            <option value="${key}">${orderStatusMap[key]}</option>
                            </#list>
                        </select>
                    </td>
                    
                    <#if isHaitao == 1>
                        <td>订单类型：</td>
	                    <td>
	                    	 <select class="form-control" name="orderType" id="order_Types">
	                    	    <option value="">全部</option>
	                            <#list orderTypeMap?keys as key>
	                            <option value="${key}">${orderTypeMap[key]}</option>
	                            </#list>
	                        </select>
	                    </td>
	                    
	                    <td id="fhfsTd"><#--发货方式：--></td>
	                    <td>
	                    	<select class="form-control" id="deliveryWayId" style="display:none;" name="deliveryWay">
	                        </select>
	                    </td><#---->
                    <#else>
                        <td>订单类型：</td>
	                    <td colspan="3">
	                    	 <select class="form-control" name="orderType" id="order_Types">
	                    	    <option value="">全部</option>
	                            <#list orderTypeMap?keys as key>
	                            <option value="${key}">${orderTypeMap[key]}</option>
	                            </#list>
	                        </select>
	                    </td>
                    </#if>
                    
                </tr>
                
                <tr>
                    <td colspan="6" align="center">
                        <button class="btn btn-primary btn-sm" id="order_list_query">查询</button>
                        <button class="btn btn-primary btn-sm" id="order_info_export">导出</button>
                    </td>
                </tr>
            </table>
        </form>
        <div id="contentShow">
        
        </div>
    </div><#-- panel-body -->
</div>
</@sellContent>
