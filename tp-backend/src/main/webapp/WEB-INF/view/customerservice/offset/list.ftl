<#include "/common/common.ftl"/> 
<@backend title="补偿管理" 
	js=['/statics/backend/js/layer/layer.min.js',
		'/statics/backend/js/customerservice/offset.js'
		] 
	css=[] >
	<form id="offsetlistform" action="${domain}/customerservice/offset/${auditType}/list.htm" method="post">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">补偿列表</b></div>
            <div class="box_center pt10 pb10">
              
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>订单编号</td>
                  <td><input type="text" name="orderCode" value="${query.orderCode}" class="input-text lh25" size="20"></td>
                  <td>补偿单号</td>
                  <td>
                   	<input type="text" name="offsetCode" value="${query.offsetCode}" class="input-text lh25" size="20">
                  </td>
                  <td>补偿原因</td>
                  <td>
                    <select class="select" name="offsetReason">
                    	<option value="">-- 全部 --</option>
                    	<#list reasonList as reason>
                    		<option value="${reason.code}" <#if query.offsetReason==reason.code>selected="selected"</#if>>${reason.cnName}</option>
                    	</#list>
                    </select>
                  </td>
                 </tr>
                 <tr>
                  <td>审核状态</td>
                  <td>
                    <select class="select" name="offsetStatus">
                    	<option value="">-- 全部 --</option>
                    	<#list offsetStatusList as offsetStatus>
                    		<option value="${offsetStatus.code}" <#if offsetStatus.code==query.offsetStatus>selected="selected"</#if>>${offsetStatus.cnName}</option>
                    	</#list>
                    </select>
                  </td>
                  <td>申请时间</td>
                  <td>
                  <input type="text" name="createDateBegin"  value="<#if query.createDateBegin??>${query.createDateBegin?string('yyyy-MM-dd')}</#if>" class="input-text lh25" size="20">
                   	至
                  <input type="text" name="createDateEnd"  value="<#if query.createDateEnd??>${query.createDateEnd?string('yyyy-MM-dd')}</#if>" class="input-text lh25" size="20">
                  </td>	
                </tr>
                <tr>
                	<td>金额：</td>
                  	<td><input type="text" name="offsetAmountStart" value="${query.offsetAmountStart}" class="input-text lh25" size="20">~<input type="text" name="offsetAmountEnd" value="${query.offsetAmountEnd}" class="input-text lh25" size="20"></td>
                </tr>
              </table>
			
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <input class="btn btn82 btn_search" onclick="$('#offsetlistform').submit();" type="button" value="查 询" name="button" />
 				 <input class="btn btn82 btn_add applyoffsetshowbtn" auditType="${auditType}" type="button" value="补偿申请" name="button" operate="apply"/>
 				 <input type="button" value="导出" class="btn btn82 btn_export exportoffsetlist" name="button">
 				 <#if auditType=='finalaudit'>
					<input type="button" value="导入" class="btn btn82 btn_export importoffsetlist" name="button">
 				 </#if>
              </div>
            </div>
          </div>
        </div>
    </div>
    <br>
    <div style="text-align:right;margin-right:20px;">补偿总额: <font color="red">${totalAmount?string('0.00')}</font></div>
   <div id="table" class="mt10">
    	<#include "/customerservice/offset/subPagelist.ftl"/> 
    	<@pager  pagination=offsetInfoPage  formId="offsetoldform"  />
	</div> 
	</form> 
</@backend>