		<div class="box span10 oh">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
            <tr>
               <th width="130">订单编号</th>
           	   <th width="130">退货编号</th>
           	   <th width="120">商品编号</th>
               <th width="150">商品名称</th>
               <th width="80">退货数量</th>
               <th width="80">退款金额</th>
               <th width="130">申请时间</th>
               <th width="100">退货状态</th>
               <th width="100">审核状态</th>
               <th>操作</th>
            </tr>
            <#if rejectPageInfo.rows?default([])?size !=0>
            <#list rejectPageInfo.rows as reject>
             <tr>
               <td>${reject.orderCode}</td>
               <td>${reject.rejectCode}</td>
               <#if reject.rejectItemList?default([])?size !=0>
			 	<#list reject.rejectItemList as item>
				<td>${item.itemSkuCode}</td>
               <td>${item.itemName}</td>
               <td>${item.itemRefundQuantity}</td>
               </#list>
               <td>${reject.refundAmount?string('0.00')}</td>
				<#else>
				<td>-</td>
 				<td>-</td>
	            <td>-</td>
	            <td>-</td>
               </#if>
               <td>${reject.createTime?string('yy-MM-dd HH:mm:ss')}</td>
               <td>${reject.zhRejectStatus}</td>
               <td>${(reject.zhAuditStatus)!}</td>
               <td class="search_bar_btn">
               	<#if reject.rejectStatus == 0 >
               	<a class="queryrejectinfobtn" href="javascript:void(0);" rejectid="${reject.rejectId}" rejectno="${reject.rejectCode}" operate="auditshow">[审核]</a> 
               	</#if>
				<a class="queryrejectinfobtn" href="javascript:void(0);" rejectid="${reject.rejectId}" rejectno="${reject.rejectCode}" operate="show">[查看]</a>
				<#if reject.auditStatus == 3 || reject.auditStatus == 6 || reject.auditStatus == 2>
				<a class="queryrejectinfobtn" href="javascript:void(0);" rejectid="${reject.rejectId}" rejectno="${reject.rejectCode}" operate="auditshow">[确认审核]</a>
			    </#if>
			   </td>
              </tr>
            </#list>
            </#if>
          </table>
    	</div>
	   