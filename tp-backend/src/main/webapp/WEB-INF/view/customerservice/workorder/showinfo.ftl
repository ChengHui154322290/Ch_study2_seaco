<div class="box_top">
		<b class="pl15">工单信息</b>
</div>
<div class="box_center">
   <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
     <tr>
      <td class="td_right">订单编号：</td>
      <td>${workorderInfo.orderCode}</td>
      <td class="td_right">业务类型：</td>
      <td>${workorderInfo.cnBizType}</td>
   
    </tr>
    <tr>   <td class="td_right">原因：</td>
      <td>${workorderInfo.reason}</td>
      <td class="td_right">工单状态：</td>
      <td>${workorderInfo.cnStatus}</td>
     
    </tr>
    <tr> 
      <td class="td_right">处理状态：</td>
      <td>${workorderInfo.cnTranslateStatus}</td>
      <td class="td_right">创建人：</td>
      <td>${workorderInfo.owner}</td>
    </tr>
    <tr> 
      <td class="td_right">供应商处理方式：</td>
      <td>${workorderInfo.cnSupplierMethodId}</td>
      <td class="td_right">组别：</td>
      <td>${workorderInfo.cnGroupType}</td>
    </tr>
    <tr>    
    <td class="td_right">问题描述：</td>
      <td colspan="3">${logfirst.context}<br> 
      <#if logfirst.imgsUrl??>	
			<#list logfirst.imgsUrl?split(",") as imgUrl>
			       <img src="<@itemImageDownload code='${imgUrl!}' width="80" />" data-pic="<@itemImageDownload code='${imgUrl!}' width="640" />">
			</#list>
	  </#if>
      </td>
    </tr>
   </table>
   <input type="hidden" name="workorderCode" value="${workorderInfo.workorderCode}" />
   <input type="hidden" name="translateStatus" value="${workorderInfo.translateStatus}" />
   <input type="hidden" name="supplierMethodId" value="${workorderInfo.supplierMethodId}" />
   <input type="hidden" name="id" value="${workorderInfo.id}" />
  


   
   <div class="box_center">
		<table id="CRZ0" class="list_table CRZ customertable" width="100%" cellspacing="0" cellpadding="0" border="0">
		<thead>
		<tr>
		<th>退货单号</th>
		<th>退货商品</th>
		<th>退货金额</th>
		<th>创建时间</th>
		<th>退货状态</th>
		</tr>
		</thead>
		<tbody>
		 <#if rejectPage.list?default([])?size !=0>
            <#list rejectPage.list as reject>
             <tr>
               <td>${reject.rejectCode}</td>
               <#if reject.rejectItemList?default([])?size !=0>
			 	<#list reject.rejectItemList as item>
               <td>${item.itemName}</td>
               </#list>
               <td>${reject.refundAmount?string('0.00')}</td>
				<#else>
				<td>-</td>
 				<td>-</td>
	            <td>-</td>
	            <td>-</td>
               </#if>
               <td>${reject.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
               <td>${reject.zhRejectStatus}</td>
              </tr>
            </#list>
            </#if>
		</tbody>
		</table>
	</div>
	<br>
	
	