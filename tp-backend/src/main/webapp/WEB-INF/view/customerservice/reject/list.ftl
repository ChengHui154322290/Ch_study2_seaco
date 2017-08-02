<#include "/common/common.ftl"/> 
<@backend title="退货拒收管理" 
	js=[
		'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
		'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
		'/statics/backend/js/layer/layer.min.js',
		'/statics/backend/js/customerservice/reject.js'
	  ] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
	<form id="rejectInfoForm" action="${domain}/customerservice/reject/list.htm" method="post">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">退货拒收列表</b></div>
            <div class="box_center pt10 pb10">
            
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>订单编号</td>
                  <td><input type="text" name="orderCode" value="${query.orderCode}" class="input-text lh25" size="20"></td>
                  <td>退货单号</td>
                  <td>
                   	<input type="text" name="rejectCode" value="${query.rejectCode}" class="input-text lh25" size="20">
                  </td>
                  <td>退货状态</td>
                  <td>
                    <select class="select" name="rejectStatus">
                    	<option value="">-- 全部 --</option>
                    	<#list rejectStatusList as rejectStatus>
                    		<option value="${rejectStatus.code}" <#if query.rejectStatus==rejectStatus.code>selected="selected"</#if>>${rejectStatus.cnName}</option>
                    	</#list>
                    </select>
                  </td>
                  <td>审核状态</td>
                  <td>
                    <select class="select" name="auditStatus">
                    	<option value="">-- 全部 --</option>
                    	<#list auditStatusList as auditStatus>
                    		<option value="${auditStatus.code}" <#if query.auditStatus==auditStatus.code>selected="selected"</#if>>${auditStatus.cnName}</option>
                    	</#list>
                    </select>
                  </td>
                </tr>
              </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="$('#rejectInfoForm').submit();" type="button" value="查 询" name="button" /></a>
                 <input type="button" value="导出" class="btn btn82 btn_export exportrejectlist" name="button">
              </div>
            </div>
          </div>
        </div>
    </div>
   <div id="table" class="mt10">
    	<#include "/customerservice/reject/subPagelist.ftl"/> 
    	<@pager  pagination=rejectPageInfo  formId="rejectInfoForm" />
	</div>
	</form>
</@backend>