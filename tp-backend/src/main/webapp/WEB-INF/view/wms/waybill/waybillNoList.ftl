<#include "/common/common.ftl"/>
<@backend title="运单号列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/select2/js/select2.js',
'/statics/select2/js/select2Util.js',
'/statics/select2/js/select2_locale_zh-CN.js',
'/statics/backend/js/item/item-select2.js',
'/statics/backend/js/wms/waybill.js'
] 
css=[
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/select2/css/select2.css'
] >

<form method="post" action="${domain}/wms/waybill/waybillno_list.htm" id="waybillNOListForm">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">运单号列表页面</b></div>
            <div class="box_center pt10 pb10">
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>物流企业：</td>
                  <td>
                	<select name="logisticsCode" class="select2" id="logisticsCode" style="width:150px; margin-left: 1px">
							<option value="" >--请选择物流--</option>
							<#if logisticsList??>
								<#list logisticsList as lc>
									<option value="${lc.code}" <#if "${lc.code}"=="${detail.logisticsCode}">selected</#if> >${lc.name}</option>
								</#list>
							</#if>
					</select>	
                  </td>
                  <td>快递单号：</td>
                  <td><input type="text" name="waybillNo" value="${detail.waybillNo}"  class="input-text lh25" size="20"></input></td>
                  <td>运单状态</td>
                  <td>
                  	 <select name="status" class="select" id="status" style="width:150px; margin-left: 1px">
							<option value="" >请选择</option>
							<#if waybillStatusList??>
								<#list waybillStatusList as ws>
									<option value="${ws.code}" <#if "${ws.code}"=="${detail.status}">selected</#if> >${ws.desc}</option>
								</#list>
							</#if>
					</select>
                  </td>
                </tr>
                <tr>
                  <td>订单号：</td>
                  <td><input type="text" name="orderCode" value="${detail.orderCode}"  class="input-text lh25" size="20"></input></td>
                  <td>申请单编号：</td>
                  <td><input type="text" name="applicationId" value="${detail.applicationId}"  class="input-text lh25" size="20"></input></td>
                </tr>
              </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="$('#waybillNOListForm').submit();" type="button" value="查询" name="button" /></a>
              </div>
            </div>
          </div>
        </div>
    </div>
   <div id="table" class="mt10">
    	<div class="box span10 oh">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table" id="waybillDetailList">
            <tr>
               <th width="100">运单号</th>
           	   <th width="120">申请单编号</th>
           	   <th width="120">物流公司</th>
           	   <th width="120">公司编号</th>
               <th width="100">使用状态</th>
               <th width="100">订单号</th>
               <th width="150">创建时间</th>
               <th width="200">操作</th>
            </tr>
            <#if page.rows?default([])?size !=0>
            <#list page.rows as wd>
	             <tr>
	               <td width="100">${wd.waybillNo} </td>
	               <td width="120">${wd.applicationId}</td>
	               <td width="120">${wd.logisticsName}</td>
	               <td width="120">${wd.logisticsCode}</td>
	               <td width="100">${wd.statusStr}</td>
	               <td width="100">${wd.orderCode}</td>
	               <td width="150">${wd.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	               <td>
	               	 <a  href="javascript:void(0);" waybill="${wd.waybillNo}" class="waybillLog">[日志]
	               </td>
	            </tr>
            </#list>
            </#if>
          </table>
    	</div>
</div>
   <@pager  pagination=page  formId="waybillNOListForm" /> 
</form>

</@backend>