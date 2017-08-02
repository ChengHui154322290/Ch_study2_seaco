<#include "/common/common.ftl"/>
<@backend title="退货拒收管理" 
	js=[
		'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
		'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
		'/statics/backend/js/textinput_common.js',
		'/statics/backend/js/layer/layer.min.js',
		'/statics/backend/js/customerservice/reject.js'
	  ] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
	<div class="mt10" id="forms">
        <div class="box">
          <div class="box_border">
            <#if errorMessage??><div class="box_top"><b class="pl15">${errorMessage}</b></div></#if>
            <div class="box_center">
              <form class="jqtransform" action="" id="rejectaudit">
               <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                 <tbody>
                 <tr>
                  <td class="td_right">退货状态：</td>
                  <td>${rejectInfo.zhRejectStatus}</td>
                  <td class="td_right">客户账号：</td>
                  <td>${customerUser.nickName}</td>
                  <td class="td_right">客户姓名：</td>
                  <td width="80">${userDetail.trueName}</td>
                  <td class="td_right">手机：</td>
                  <td>${customerUser.mobile}</td>
                </tr>
                <tr>
                  <td class="td_right">订单编号：</td>
                  <td>${rejectInfo.orderCode}</td>
                  <td class="td_right">订单类型：</td>
                  <td>${order.typeStr}</td>
                  <td class="td_right">订单状态：</td>
                  <td>${order.statusStr}</td>
                  <td class="td_right">购买时间：</td>
                  <td>${order.createTime?string('yy-MM-dd HH:mm:ss')}</td>
                </tr>
                <tr>
                  <td class="td_right">退货单号：</td>
                  <td>
                  	${rejectInfo.rejectCode}
					<input type="hidden" name="rejectCode" value="${rejectInfo.rejectCode}">
					<input type="hidden" name="rejectId" value="${rejectInfo.rejectId}">
					<input type="hidden" name="rejectItemId" value="${rejectItem.rejectItemId}">
					<input type="hidden" name="success" value="${rejectItem.success}">
				  </td>
                  <td class="td_right">申请退货时间：</td>
                  <td>${rejectInfo.createTime?string('yy-MM-dd HH:mm:ss')}</td>
                  <td class="td_right">退货原因：</td>
              <!--    <td>${rejectInfo.rejectReason}</td>	-->
                  <td>${rejectreasons[rejectInfo.rejectReason]}</td>			  
                  <td class="td_right">商家名称：</td>
                  <td>${rejectInfo.supplierName}</td>
                </tr>
                <tr>
                  <td class="td_right">商品编号：</td>
                  <td>${rejectItem.itemSkuCode}</td>
                  <td class="td_right">商品名称：</td>
                  <td>${rejectItem.itemName}</td>
                  <td class="td_right">退货数量：</td>
                  <td>${rejectItem.itemRefundQuantity}</td>
                  <td class="td_right">退货金额：</td>
                <#if rejectInfo.auditStatus == 3 || rejectInfo.auditStatus == 6>
				  <input type="hidden" name="amount" value="${rejectInfo.refundAmount?string('0.00')}">
				  <td>${rejectInfo.refundAmount?string('0.00')}</td>
				<#else>	
                  <td><input type="text" name="amount" value="${rejectInfo.refundAmount?string('0.00')}" class="input-text lh25" moneytext=""/></td>
				</#if>				
                </tr>
                <tr>
                  <td class="td_right"> 买家联系人：</td>
                  <td>${rejectInfo.linkMan}</td>
                  <td class="td_right">买家联系人手机：</td>
                  <td>${rejectInfo.linkMobile}</td> 
                  <td class="td_right">退回积分：</td>
                  <td colspan="2">${rejectInfo.points}<input type="hidden" name="points" value="${rejectInfo.points}"/></td>              
                </tr>
				
                <tr>
                  <td class="td_right"><font color="red">*</font>卖家联系人：</td>
                  <td>
					<input type="text" style="width:100px;" name="returnContact" value="${rejectInfo.returnContact}" class="input-text lh25" />
				  </td>
                  <td class="td_right"><font color="red">*</font>卖家联系人手机：</td>
                  <td>
					<input type="text" style="width:100px;" name="returnMobile" value="${rejectInfo.returnMobile}" class="input-text lh25" />
				  </td>
                </tr>
				
				<tr>
                  <td class="td_right"><font color="red">*</font>寄回地址：</td>
                  <td colspan="7">
						<input type="text" style="width:350px;" name="returnAddress" value="${rejectInfo.returnAddress}" class="input-text lh25" />
                  </td>				  				 				  
				</tr>				
                <#if rejectInfo.expressCode??>
				<tr>
					<td class="td_right">快递单号：</td>
				    <td colspan="7" class="show_bar_btn">${(rejectInfo.expressCode)!} <a class="showdelivery" href="javascript:void(0)"  expressCode="${(rejectInfo.expressCode)!}" rejectCode="${(rejectInfo.rejectCode)!}">全程跟踪</a></td>
				</tr>
				</#if>
				<tr>
                  <td class="td_right">问题描述：</td>
                  <td colspan="7">${rejectInfo.buyerRemarks}</td>
				</tr>
				<tr>
                  <td class="td_right">用户上传图片：</td>
                  <td colspan="7">
                  
					<#if rejectInfo.buyerImgUrl??&&rejectInfo.buyerImgUrl?length gt 0>
						<#list  rejectInfo.buyerImgUrl?split(",") as url>
			                  <img src="${url!}" data-pic="${url!}' width="640" />">
						</#list>
					</#if>
                        </tr>
                <tr>
                  <td class="td_right">商家上传图片：</td>
                  <td colspan="7">
					<#if rejectInfo.sellerImgUrl??&&rejectInfo.sellerImgUrl?length gt 0>
						<#list  rejectInfo.sellerImgUrl?split(",") as sellerUrl>
			                  <img src="${sellerUrl!}" data-pic="${sellerUrl!}">
						</#list>
					</#if>
				</td>
                </tr>
                <#if auditshow??>
                 <tr>
                  <td class="td_right">审核说明：</td>
                  <td colspan="7">
                    <textarea class="textarea" rows="10" cols="30" name="remark"></textarea>
                  </td>
                 </tr>
                 <tr>
                   <td class="td_right">&nbsp;</td>
                   <td class="rejectbtnlist" colspan="7">
                    <input type="button" value="通过" class="btn btn82 btn_save2 submitreject" suc="true"  name="button"> 
                    <input type="button" value="不通过" class="btn btn82 btn_res submitreject" suc="false" name="button">
                    <#if rejectInfo.auditStatus==2> 
                    <input type="button" value="强制审核" class="btn btn82 btn_save2 forceaudit" name="button">
					</#if>
                    <input type="button" value="工单申请" class="btn btn82 btn_save2 workorderapply" orderCode="${(rejectInfo.orderCode)!}" name="button">
                   	<input type="button" value="申请补偿" class="btn btn82 btn_save2 offsetapply" orderCode="${(rejectInfo.orderCode)!}"name="button" >
                   </td>
                 </tr>
				<#else>
                 <tr>
                  <td class="td_right">审核说明：</td>
                  <td colspan="7">
                    ${(rejectInfo.remarks)!}
                  </td>
                 </tr>
				</#if>
               </tbody>
               </table>
               <input type="hidden" value="" name="auditType" id="auditTypeId" />
               </form>
            </div>
            <#include "/customerservice/reject/log.ftl"/>
          </div>
        </div>
     </div>
</@backend>