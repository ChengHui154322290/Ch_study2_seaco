<#include "/common/common.ftl"/> 
<@backend title="退款管理" 
	js=[
	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
	'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/customerservice/refund.js'
	] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >

	<div class="mt10" id="forms">
        <div class="box">
          <div class="box_border">
            <#if errorMessage??><div class="box_top"><b class="pl15">${errorMessage}</b></div></#if>
            <div class="box_top"><b class="pl15">退货单</b></div>
            <div class="box_center">
              <form class="refundinfoform" action="${domain}/customerservice/refund/audit.htm" method="POST">
               <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                 <tbody>
                 <tr>
                  <td class="td_right">订单编号：</td>
                  <td>${subOrder.orderCode} </td>
                  <td class="td_right">订单状态：</td>
                  <td>${subOrder.zhOrderStatus}</td>
                  <td class="td_right">订单类型：</td>
                  <td>${subOrder.zhType} </td>
                  <td class="td_right">下单时间：</td>
                  <td><#if subOrder.createTime??>${subOrder.createTime?string('yyyy-MM-dd HH:mm:ss')}</#if></td>
                </tr>
                <tr>
                  <td class="td_right">退款编号：</td>
                  <td>${refundInfo.refundCode} </td>
                  <td class="td_right">退款状态：</td>
                  <td>${refundInfo.zhRefundStatus} </td>
                  <td class="td_right">退款金额：</td>
                  <td>${refundInfo.refundAmount?string('0.00')} </td>
                  <td class="td_right">实付金额：</td>
                  <td>${itemAmount?string('0.00')} </td>
                </tr>
                <tr>
                  <td class="td_right">客户账号：</td>
                  <td class="">${userObj.nickName}</td>
                  <td class="td_right">付款交易号：</td>
                  <td>${paymentInfo.gatewayTradeCode}</td>
                  <td class="td_right">退款交易号：</td>
                  <td>${refundPayInfo.gatewayTradeCode}</td>
                  <td class="td_right">支付方式：</td>
                  <td>${refundInfo.zhRefundType}-${refundInfo.gatewayName}</td>
                </tr>
                
                <tr>
                  <td class="td_right">创建时间：</td>
                  <td>${refundInfo.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                  <td class="td_right">客服审核人：</td>
                  <td>${refundInfo.createUser}</td>
                  <td class="td_right">财务审核人：</td>
                  <td>${refundInfo.updateUser}</td>
                </tr>
                <tbody>
                <tr>
                   <td colspan="8">
                   	   <div>
                   	   	  <#if rejectInfoDO??>
                   	   	  	<#include "/customerservice/refund/rejectInfo.ftl"/>
                   	   	  <#else>
                   	   	  	<#include "/customerservice/refund/cancelInfo.ftl"/>
                   	   	  </#if>
                   	   </div>
                   </td>
                </tr>
                <#if audit??>
                 <tr>
                   <td class="td_right">&nbsp;</td>
                   <td colspan="5">
                    <input type="button" value="打款" class="btn btn82 btn_save2 auditrefund" refundId="${refundInfo.refundId}" gatewayId="${refundInfo.gatewayId}" success="true" name="button"/> 
                <!--    <input type="button" value="不通过" class="btn btn82 btn_res auditrefund" refundId="${refundInfo.refundId}" gatewayId="${refundInfo.gatewayId}" success="false" name="button"/> -->
                   </td>
                 </tr>
                 <#elseif auditcomplete??> 
                 <tr>
                   <td class="td_right">&nbsp;</td>
                   <td colspan="5"> 
                    <input type="button" value="结束" class="btn btn82 btn_save2 auditrefund"  refundId="${refundInfo.refundId}" gatewayId="${refundInfo.gatewayId}" success="true" isAuditComplete="true" name="button"/> 
                   </td>
                 </tr>				 
				 </#if>				 
               </tbody>
               </table>
               </form>
            </div>
            <#include "/customerservice/refund/log.ftl"/>
          </div>
        </div>
     </div>
</@backend>