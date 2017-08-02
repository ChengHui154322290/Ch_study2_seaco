<#include "/commons/js_and_css.ftl" />
 <div style="width: 821px; background: none repeat scroll 0% 0% rgb(255, 255, 255);">
 <div class="box_center" style="overflow: auto;height: 400px;">
<table class="table table-bordered table-striped table-hover"  width="100%" style="height:50px;overflow:scroll;">
	<#if message??>
		<td colspan="2">订单导入失败，原因如下:</td>
	    <tr>
           <td colspan="2">${message}</td>
       	<tr>
    <#elseif orderOperatorErrorList?default([])?size !=0>
    <thead>
    	<tr>
    		<#if size??>
	            <td colspan="2">有<font color="red">${size}</font>个订单导入失败，见下方列表:</td>
            </#if>
       	</tr>
        <tr>
            <th width="20%">订单编号</th>
            <th>失败原因</th>
        </tr>
    </thead>
    <tbody>
        <#list orderOperatorErrorList as errorInfo>
		    <tr>
	            <td>${(errorInfo.subOrderCode)!}</td>
	            <td>${(errorInfo.errorMessage)!}</td>
	        </tr>
		</#list>
    </tbody>
    <#else>
       <tr>
           <td colspan="2">发货成功</td>
       <tr>
    </#if>
</table>
</div>
</div>