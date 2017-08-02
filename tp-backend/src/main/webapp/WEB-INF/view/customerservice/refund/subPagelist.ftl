		<div class="box span10 oh refundlistdiv">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
            <tr>
               <th width="100"><input type="button" class="selectall btn btn82 btn_nochecked" value="全选"/></th>
           	   <th width="130">订单编号</th>
           	   <th width="130">退款编号</th>	
           	   <th width="100">退款类型</th>
           	   <th width="130">商品编号</th>
               <th width="200">商品名称</th>
               <th>退款金额</th>
               <th>创建时间</th>
               <th>退款状态</th>
               <th>支付方式</th>
               <th>客服审核人</th>
               <th>财务审核人</th>
               <th>操作</th>
            </tr>
            <#if refundInfoPageInfo.rows?default([])?size !=0>
            <#list refundInfoPageInfo.rows as refund>
             <tr>
               <td style="text-align:center">
                    <#if refund.refundStatus == 1  || refund.refundStatus==2 || refund.refundStatus==4>
               			<input type="checkbox" class="refundItem" name="refundCodeGroup" gatewayId="${refund.gatewayId}" value="${refund.refundCode}-${refund.gatewayId}"/>
               		</#if>
               </td>
               <td>${refund.orderCode}</td> 
               <td>${refund.refundCode}</td>
               <td>${refund.zhRefundType}</td>
			   <td>
			   		<#if refund.itemList ??>
						<#list refund.itemList as item>
			   			${item.itemSkuCode}
						</#list>
			   		</#if>
			   	</td>
               <td>
               		<#if refund.itemList ??>
						<#list refund.itemList as item>
			   			${item.itemName}
						</#list>
			   		</#if>
               </td>
               <td>${refund.refundAmount?string('0.00')}</td>
               <td>${refund.createTime?string('MM-dd HH:mm')}</td>
               <td>${refund.zhRefundStatus}</td>
               <td>${refund.gatewayName}</td>
               <td>${refund.createUser}</td>
               <td>${refund.updateUser}</td>
               <td>

               		<#if refund.refundStatus==1 || refund.refundStatus==2 || refund.refundStatus==4>
               		<a href="javascript:void(0);" class="showrefundinfo" operate="audit" refundid="${refund.refundId}">[打款]</a>
               		<a href="javascript:void(0);" class="showrefundinfo" operate="auditcomplete" refundid="${refund.refundId}">[退款结束]</a>					
               		</#if>
               		<a href="javascript:void(0);" class="showrefundinfo" operate="show" refundid="${refund.refundId}">[查看]</a>
               </td>
              </tr>
            </#list>
            </#if>
          </table>
    	</div>
	  