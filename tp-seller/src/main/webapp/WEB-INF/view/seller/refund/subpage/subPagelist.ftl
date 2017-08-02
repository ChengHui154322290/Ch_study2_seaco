<#include "/commons/base/page.ftl" />

<table class="table table-striped table-bordered table-hover">
           	<thead>
            <tr>
               <th width="7%">订单编号</th>
           	   <th width="7%">退货编号</th>
           	   <th width="7%">商品编号</th>
           	   <th width="7%">供应商SKU</th>
           	   <#if isHaitao?exists>
           	    <th width="7%">备案号</th>
           	   </#if>
               <th width="20%">商品名称</th>
               <th width="5%">退货数量</th>
               <th width="5%">退款金额</th>
               <th width="12%">申请时间</th>
               <th width="8%">退货状态</th>
               <th width="10%">审核状态</th>
               <th width="5%">操作</th>
            </tr>
            </thead>
            <tbody>
            <#if (rejectPage.rows)?exists>
             <#list rejectPage.rows as reject>
             <tr>
               <td>${(reject.orderCode)!}</td>
               <td>${(reject.rejectCode)!}</td>
               <#if reject.rejectItemList?default([])?size !=0>
			    <#list reject.rejectItemList as item>
				 <td>${(item.itemSkuCode)!}</td>
				 <td>${(item.productCode)!}</td>
				 <#if isHaitao?exists>
				 	<td>${(reject.customCode)!}</td>
				 </#if>
                 <td>${(item.itemName)!}</td>
                 <td>${(item.itemRefundQuantity)!}</td>
                </#list>
                 <td>${reject.refundAmount?string('0.00')}</td>
			   <#else>
				<td></td>
				<td></td>
				<#if isHaitao?exists>
				<td></td>
				</#if>
 				<td></td>
	            <td></td>
	            <td></td>
               </#if>
               <td>${reject.createTime?string('yy-MM-dd HH:mm:ss')}</td>
               <td>${reject.zhRejectStatus}</td>
               <td>${reject.zhAuditStatus}</td>
               <td class="search_bar_btn">
               	<#if reject.auditStatus == 2>             
               	<a class="queryrejectinfobtn" href="javascript:void(0);" rejectid="${reject.rejectId}" rejectno="${reject.rejectCode}" operate="auditshow">[审核]</a> 
               	<#else>
				<a class="queryrejectinfobtn" href="javascript:void(0);" rejectid="${reject.rejectId}" rejectno="${reject.rejectCode}" operate="show">[查看]</a>               	
				</#if>
			   </td>
              </tr>
            </#list>
            </#if>
            </tbody>
          </table>
          
<#if (rejectPage)?exists>
   <@p page=rejectPage.page totalpage=rejectPage.total />
</#if>

<script>
    jQuery(function(){
        $('.search_bar_btn').on('click','.queryrejectinfobtn',function(){
			  var rejectId = $(this).attr('rejectid');
			  var rejectNo = $(this).attr('rejectno');						
			  var operate = $(this).attr('operate');
			  showPopPage('/seller/refund/'+operate+'.htm?rejectId='+rejectId,{},'退货编号 【'+rejectNo+'】');
		}); 
    });
</script>
