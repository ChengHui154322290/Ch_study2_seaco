<#include "/common/common.ftl"/> 
<@backend title="补偿管理" 
	js=[
   		'/statics/backend/js/formValidator/formValidator-4.0.1.min.js',
    	'/statics/backend/js/formValidator/formValidatorRegex.js',
    	'/statics/backend/js/layer/layer.min.js',
		'/statics/backend/js/customerservice/offset.js'] 
	css=['/statics/backend/js/layer/skin/layer.css',
		'/statics/backend/js/formValidator/style/validator.css'] >
	<div class="mt10" id="forms">
        <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">补偿单申请</b></div>
            <div class="box_center">
              <form id="offsetapplyform" class="offsetapplyform" action="${domain}/customerservice/offset/apply.htm" method="post">
              <input type="hidden" name="offsetId" value="${offset.offsetId}"/>
              <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15 cachoffset">
                 <#if apply??>
                 <tbody>
                 	<tr>
                 		<td class="td_right">订单编号：</td>
                 		<td colspan="5"><input type="text" class="input-text lh25 offsetorder" name="orderCode" type="text" size="20" />
                 		<input class="btn btn82 btn_search searchorderbtn" type="button" value="查询"/><span id="orderCodeTip"></span></td>
                 	</tr>
                </tbody>
                </#if>
				<tbody class="offsetapplyorderinfobody">               	
					<#include "/customerservice/offset/order.ftl">
				</tbody>
				<tbody>
                <tr>
                  <td class="td_right">补偿方式：</td>
                  <td colspan="5">
                  	<#list offsetTypeList as type>
                  		<input type="radio" id="offsetType" name="offsetType" value="${type.code}" <#if type_index==0 || type.code==offset.offsetType>checked</#if> text="${type.cnName}" class="<#if apply == null || apply==''>show</#if>">${type.cnName}　
                  	</#list>
                  	<span id="offsetTypeTip"></span>
                  </td>
                </tr>
                <tr>
                  <td class="td_right">补偿原因：</td>
                  <td colspan="5"><select id="offsetReason" name="offsetReason" class="select"><#list offsetReasonList as reason><option value="${reason.code}" <#if reason_index==0 || reason.code==offset.offsetReason>selected</#if>>${reason.cnName}</option></#list></select><span id="offsetReasonTip"></span></td>
                </tr>
                <tr>
                  <td class="td_right">补偿金额：</td>
                  <td colspan="5"><input id="offsetAmount" type="text" name="offsetAmount" value="<#if offset.offsetAmount??>${offset.offsetAmount?string('0.00')}</#if>" class="input-text lh30" size="40"/><span id="offsetAmountTip"></span></td>
                </tr>
				<tr>
                  <td class="td_right">补偿承担：</td>
                  <td colspan="5"><#list offsetBearList as offsetBear><input type="radio" id="bear" name="bear" value="${offsetBear.code}" <#if offsetBear_index==0 || offsetBear.code==offset.bear>checked</#if>  text="${offsetBear.cnName}">${offsetBear.cnName}　</#list><span id="bearTip"></span></td>
                </tr>
				<tr>
                  <td class="td_right">补偿承担：</td>
                  <td colspan="5"><#list paymentModelList as paymentModel><input type="radio" id="paymentModel" name="paymentModel" value="${paymentModel.code}" <#if paymentModel_index==0 || paymentModel.code==offset.bear>checked</#if>  text="${paymentModel.cnName}">${paymentModel.cnName}　</#list><span id="bearTip"></span></td>
                </tr>

                <tr>
                  <td class="td_right">收款人：</td>
                  <td colspan="5"><input type="text" id="payee" name="payee" value="${offset.payee}" maxlength="20" class="input-text lh30" size="40"/><span id="payeeTip"></span></td>
                </tr>
				<tr>
                  <td class="td_right">收款银行：</td>
                  <td colspan="5"><input type="text" id="payeeBank" name="payeeBank" value="${offset.payeeBank}" maxlength="20" class="input-text lh30" size="40"/><span id="payeeBankTip"></span></td>
                </tr>
                <tr>
                  <td class="td_right">银行账号：</td>
                  <td colspan="5"><input type="text" id="bankAccount" name="bankAccount" value="${offset.bankAccount}" maxlength="22" class="input-text lh30" size="40"/><span id="bankAccountTip"></span></td>
                </tr>
                <tr>
                  <td class="td_right">收款人手机：</td>
                  <td colspan="5"><input type="text" id="payeeMobile" name="payeeMobile" value="${offset.payeeMobile}" class="input-text lh30" size="40"/><span id="payeeMobileTip"></span></td>
                </tr>
                 <#if apply == null>
					<#if offset.offsetType==1>
					 <tr>
	                  <td class="td_right">交易流水号：</td>
	                  <td colspan="5"><input type="text" id="serialNo" name="offsetInfoDO.serialCode" value="${offset.serialNo}" class="input-text lh30 <#if audit!=null && auditType=='finalaudit'>audit</#if>" size="40"/><span id="serialCodeTip"></span></td>
	                </tr>
	                <#else>
					<tr>
	                  <td class="td_right">优惠券代码：</td>
	                  <td colspan="5"><input type="text" id="couponCode" name="offset.couponCode" value="${offset.couponCode}" class="input-text lh30 <#if audit!=null && auditType=='audit'>audit</#if>" size="40"/><span id="serialCodeTip"></span></td>
	                </tr>

					</#if>
                </#if>
                 <tr>
                  <td class="td_right">补偿说明：</td>
                  <td colspan="5">
                    <textarea class="textarea" rows="10" cols="30" name="remarks" value="${offset.remarks}"></textarea>
                  </td>
                 </tr>
                 <#if auditRemarks??>
                 <tr>
                   <td class="td_right">审核说明:</td>
                   <td colspan="5">
                    	<textarea class="textarea" rows="10" cols="30" name="auditRemarks" value="${offset.auditRemarks}"></textarea>
                   </td>
                 </tr>
                 </#if>
                 <#if apply??>
                 <tr>
                   <td class="td_right">&nbsp;</td>
                   <td colspan="5">
                    <input type="button" value="保存" class="btn btn82 btn_save2 applyoffsetbtn" id="applyoffsetbtn" name="button"> 
                   </td>
                 </tr>
                 </#if>
                 <#if audit??>
                 <tr>
                   <td class="td_right">&nbsp;</td>
                   <td colspan="5">
                    <input type="button" value="通过" class="btn btn82 btn_save2 auditoffset" offsetId="${offset.offsetId}" auditType="${auditType}" success="true" name="button"> 
                    <input type="button" value="不通过" class="btn btn82 btn_res auditoffset" offsetId="${offset.offsetId}" auditType="${auditType}" success="false" name="button"> 
                   </td>
                 </tr>
                 </#if>
               </tbody>
               </table>
               </form>
            </div>
            <#include "/customerservice/offset/log.ftl">
          </div>
        </div>
     </div>
</@backend>