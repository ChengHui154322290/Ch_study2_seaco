<#include "/common/common.ftl"/> 
<@backend title="退货拒收管理" 
	js=[
	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
	'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/customerservice/refund.js'
	] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
	<form id="refundlistform" action="${domain}/customerservice/refund/list.htm" method="post">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">退款单列表</b></div>
            <div class="box_center pt10 pb10">
            
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>订单编号</td>
                  <td><input type="text" name="orderCode" value="${query.orderCode}" class="input-text lh25" size="20"></td>
                  <td>退款编号</td>
                  <td>
                   	<input type="text" name="refundCode" value="${query.refundCode}" class="input-text lh25" size="20">
                  </td>
                  <td>审核状态</td>
                  <td>
                    <select class="select" name="refundStatus">
                    	<option value="">-- 全部 --</option>
                    	<#list refundStatusList as refundStatus>
                    		<option value="${refundStatus.code}" <#if refundStatus.code==query.refundStatus>selected="selected"</#if>>${refundStatus.cnName}</option>
                    	</#list>
                    </select>
                  </td>
                </tr>
				<tr>
                  <td>创建时间</td>
                  <td>
                  <input type="text" name="createTimeBegin"  value="<#if query.createTimeBegin??>${query.createTimeBegin?string('yyyy-MM-dd')}</#if>" class="input-text lh25" size="20">
                   	至
                  <input type="text" name="createTimeEnd"  value="<#if query.createTimeEnd??>${query.createTimeEnd?string('yyyy-MM-dd')}</#if>" class="input-text lh25" size="20">
                  </td>
                  <td>支付网关</td>
                  <td>
                  	 <select class="select" name="gatewayId">
                    	<option value="">-- 全部 --</option>
                    	<#list paymentGatewayList as paymentGateway>
                    		<option value="${paymentGateway.gatewayId}" <#if paymentGateway.gatewayId==query.gatewayId>selected="selected"</#if>>${paymentGateway.gatewayName}</option>
                    	</#list>
                    </select>
                  </td>
                  <td>退款类型</td>
                  <td>
                  	 <select class="select" name="refundType">
                    	<option value="">-- 全部 --</option>
                    	<#list refundTypes as refundType>
                    		<option value="${refundType.code}" <#if refundType.code==query.refundType>selected="selected"</#if>>${refundType.cnName}</option>
                    	</#list>
                    </select>
                  </td>
				</tr>
				<tr>
                	<td>金额：</td>
                  	<td><input type="text" name="refundAmountStart" value="${query.refundAmountStart}" class="input-text lh25" size="20"> ~ <input type="text" name="refundAmountEnd" value="${query.refundAmountEnd}" class="input-text lh25" size="20"></td>
                </tr>
              </table>
             
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <input class="btn btn82 btn_search" onclick="$('#refundlistform').submit();" type="button" value="查 询" name="button" />
                 <input class="btn btn82 btn_search refundbatch" type="button" value="批量打款" name="button" />
                 <input type="button" value="导出" class="btn btn82 btn_export exportrefundlist" name="button">
              </div>
            </div>
          </div>
        </div>
    </div>
    <br>
   <div style="text-align:right;margin-right:20px;">退款总额: <font color="red"><#if totalAmount??>${totalAmount?string('0.00')}</#if></font></div>
   <div id="table" class="mt10">
    	<#include "/customerservice/refund/subPagelist.ftl"/>
    	<@pager  pagination=refundInfoPageInfo  formId="refundlistform"/>
	</div>
	 </form>
</@backend>