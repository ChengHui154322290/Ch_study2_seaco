<#include "/common/common.ftl"/> 
<@backend title="工单管理" 
	js=[
		'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
		'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
		'/statics/backend/js/layer/layer.min.js',
		'/statics/backend/js/customerservice/workorder.js',
		'/statics/backend/js/tab.js'
	  ] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
	<form id="workorderInfoForm" action="${domain}/customerservice/workorder/list.htm" method="post">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">工单列表</b></div>
            <div class="box_center pt10 pb10">
            
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>订单编号</td>
                  <td><input type="text" name="orderCode" value="${query.orderCode}" class="input-text lh25" size="20"></td>
                  <td>工单状态 </td>
                  <td>
                   	<select class="select" name="status">
                    	<option value="">-- 全部 --</option>
                    	<#list statusList as status>
                    		<option value="${status.code}" <#if query.status==status.code>selected="selected"</#if>>${status.cnName}</option>
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
				  <td>业务类型</td>
                  <td>
                    <select class="select" name="bizType">
                    	<option value="">-- 全部 --</option>
                    	<#list bizTypeList as bizType>
                    		<option value="${bizType.code}" <#if query.bizType==bizType.code>selected="selected"</#if>>${bizType.cnName}</option>
                    	</#list>
                    </select>
                  </td>
                  <td>组别</td>
                  <td>
                    <select class="select" name="groupId">
                    	<option value="">-- 全部 --</option>
                    	<#list groupList as groupId>
                    		<option value="${groupId.code}" <#if query.groupId==groupId.code>selected="selected"</#if>>${groupId.cnName}</option>
                    	</#list>
                    </select>
                  </td>
                  <td>供应商处理方式</td>
                  <td>
                    <select class="select" name="supplierMethodId">
                    	<option value="">-- 全部 --</option>
                    	<#list disposeList as supplierMethodId>
                    		<option value="${supplierMethodId.code}" <#if query.supplierMethodId==supplierMethodId.code>selected="selected"</#if>>${supplierMethodId.cnName}</option>
                    	</#list>
                    </select>
                  </td>
                </tr>
				<tr>
                  <td>处理状态</td>
                  <td>
                   	<select class="select" name="translateStatus">
                    	<option value="">-- 全部 --</option>
                    	<#list translateStatusList as translateStatus>
                    		<option value="${translateStatus.code}" <#if query.translateStatus==translateStatus.code>selected="selected"</#if>>${translateStatus.cnName}</option>
                    	</#list>
                    </select>
				  </td>
                  <td>创建时间</td>
                  <td rowspan="3">
                    <input type="text" name="createDateBegin" id="createDateBegin" value="<#if query.createDateBegin??>${query.createDateBegin?string("yyyy-MM-dd")}</#if>" class="input-text lh25" size="20">
                  	<span>至</span>
                  	<input type="text" name="createDateEnd" id="createDateEnd" value="<#if query.createDateEnd??>${query.createDateEnd?string("yyyy-MM-dd")}</#if>" class="input-text lh25" size="20">
                  </td>
                </tr>
              </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
         		 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="itemFormSubmit('list')" type="button" value="查询" name="button" /></a>
         		 <!--
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="window.open('${domain}/customerservice/workorder/showadd.htm','_self');" type="button" value="新 增" name="button" /></a>
                 -->
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="addWorkOrder()" type="button" value="新 增" name="button" /></a>
              </div>
            </div>
          </div>
        </div>
    </div>
   <div id="table" class="mt10">
    	<#include "/customerservice/workorder/subPagelist.ftl"/> 
    	<@pager  pagination=page  formId="workorderInfoForm" />
	</div>
	</form>

</@backend>