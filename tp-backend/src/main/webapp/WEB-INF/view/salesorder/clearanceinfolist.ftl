<#include "/common/common.ftl"/>
<@backend title="清关单列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
'/statics/select2/js/select2.js',
'/statics/select2/js/select2Util.js',
'/statics/select2/js/select2_locale_zh-CN.js',
'/statics/backend/js/item/item-select2.js',
'/statics/backend/js/salesorder/clearance.js'
] 
css=[
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/select2/css/select2.css',
	'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css'
] >

<form method="post" action="${domain}/order/customs/clearanceinfolist.htm" id="clearanceInfoListForm">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">清关单列表</b></div>
            <div class="box_center pt10 pb10">
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>订单号：</td>
                  <td><input type="text" name="orderCode" value="${info.orderCode}"  class="input-text lh25" size="20"></input></td>
                  <td>录入号：</td>
                  <td><input type="text" name="preEntryNo" value="${info.preEntryNo}"  class="input-text lh25" size="20"></input></td>
                  <td>海关单号：</td>
                  <td><input type="text" name="personalgoodsNo" value="${info.personalgoodsNo}"  class="input-text lh25" size="20"></input></td>                  
                </tr>
                <tr>
                  <td>快递单号：</td>
                  <td><input type="text" name="expressNo" value="${info.expressNo}"  class="input-text lh25" size="20"></input></td>
                  <td>物流公司：</td>
                  <td>
                	<select name="companyNo" class="select2" id="companyNo" style="width:250px; margin-left: 1px">
							<option value="" >--请选择物流公司--</option>
							<#if expressList??>
								<#list expressList as ep>
									<option value="${ep.code}" <#if "${ep.code}"=="${info.companyNo}">selected</#if> >${ep.name}</option>
								</#list>
							</#if>
					</select>	
                  </td>
                  <td>进口类型：</td>
                  <td>
                  	<select name="importType" class="select" id="importType">
                  		<option value=''>请选择</option>
                  		<option value='0' <#if info.importType==0>selected="selected"</#if>>直邮</option>
                  		<option value='1' <#if info.importType==1>selected="selected"</#if>>保税</option>
                  	</select>
                  </td>
                </tr>
                <tr>
                	<td>下单时间</td>
					<td><input type="text" name="startTime" class="input-text lh25" size="16" value="<#if query.startTime??>${(query.startTime)?string("yyyy-MM-dd HH:mm")}</#if>"></td>
					<td>至</td>
					<td><input type="text" name="endTime" class="input-text lh25" size="16"  value="<#if query.endTime??>${(query.endTime)?string("yyyy-MM-dd HH:mm")}</#if>"></td>
                </tr>
              </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <a href="javascript:void(0);"><input class="ext_btn ext_btn_submit" onclick="$('#clearanceInfoListForm').submit();" type="button" value="查询" name="button" /></a>
              	 <a href="javascript:void(0);"><input class="ext_btn ext_btn_submit" type="button" value="直邮报关-输入" name="button" onclick="directMailClearance()"/></a>
              	 <a href="javascript:void(0);"><input class="ext_btn ext_btn_submit" type="button" value="直邮报关-导表" name="button" onclick="directMailClearanceImport()"/></a>
              	 <a href="javascript:void(0);"><input class="ext_btn ext_btn_submit" type="button" value="API日志" name="button" onclick="view('api')"/></a>
              	 <a href="javascript:void(0);"><input class="ext_btn ext_btn_submit" type="button" value="REST日志" name="button" onclick="view('rest')"/></a>              
              </div>
            </div>
          </div>
        </div>
    </div>
   <div id="table" class="mt10">
    	<div class="box span10 oh">
          <table width="100%" border="0" style="TABLE-LAYOUT:fixed;word-break:break-all" cellpadding="0" cellspacing="0" class="list_table" id="clearanceInfoList">
            <tr>
               <th width="15"><input type="checkbox" name ="orderCode" id="checkAll"/></th>
               <th width="15%">订单号</th>
               <th width="5%">类型</th>
           	   <th width="10%">申报海关</th>
           	   <th width="10%">申报单号</th>
           	   <th width="10%">录入编号</th>
           	   <th width="15%">海关生成编号</th>
               <th width="10%">物流公司编号</th>
               <th width="10%">物流公司名称</th>
               <th width="10%">运单号</th>
               <th width="10%">航班号</th>
               <th width="10%">总提运单号</th>
               <th width="10%">运输工具编号</th>
               <th width="10%">创建时间</th>
            </tr>
            <#if page.rows?default([])?size !=0>
            <#list page.rows as pg>
	             <tr>
	               <td width="15"><input type="checkbox" name ="pgOrderCode" class="pgOrderCodeCheckbox" value="${pg.orderCode}"/> </td>
	               <td width="15%">${pg.orderCode}</td>
	               <td width="5%"><#if pg.importType==0>直邮<#else>保税</#if></td>
	           	   <td width="10%">${pg.declareCustoms}</td>
	           	   <td width="15%">${pg.declareNo}</td>
	           	   <td width="15%">${pg.preEntryNo}</td>
	           	   <td width="15%">${pg.personalgoodsNo}</td>
	               <td width="10%">${pg.companyNo}</td>
	               <td width="10%">${pg.companyName}</td>
	               <td width="10%">${pg.expressNo}</td>
	               <td width="10%">${pg.voyageNo}</td>
	               <td width="10%">${pg.billNo}</td>
	               <td width="10%">${pg.trafNo}</td>
	               <td width="10%">${pg.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>	               
	            </tr>
            </#list>
            </#if>
          </table>
    	</div>
</div>
   <@pager  pagination=page  formId="clearanceInfoListForm" /> 
</form>

</@backend>