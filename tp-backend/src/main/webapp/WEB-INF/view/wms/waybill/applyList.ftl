<#include "/common/common.ftl"/>
<@backend title="运单申请列表" js=[
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

<form method="post" action="${domain}/wms/waybill/apply_list.htm" id="applyListForm">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">运单申请列表页面</b></div>
            <div class="box_center pt10 pb10">
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>物流企业：</td>
                  <td>
                	<select name="logisticsCode" class="select2" id="logisticsCode" style="width:150px; margin-left: 1px">
							<option value="" >--请选择物流--</option>
							<#if logisticsList??>
								<#list logisticsList as lc>
									<option value="${lc.code}" <#if "${lc.code}"=="${application.logisticsCode}">selected</#if> >${lc.name}</option>
								</#list>
							</#if>
					</select>	
                  </td>
                </tr>
              </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="$('#applyListForm').submit();" type="button" value="查询" name="button" /></a>
                 <a href="javascript:void(0);">
                 	<input class="btn btn82 btn_add waybillapplybtn" type="button" value="申请" name="button" onclick='window.open(this.parentNode.href,"_self");'/>
                 </a>
              </div>
            </div>
          </div>
        </div>
    </div>
   <div id="table" class="mt10">
    	<div class="box span10 oh">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table" id="wareHouseDODataList">
            <tr>
               <th width="80">编号</th>
           	   <th width="120">物流公司</th>
           	   <th width="120">公司编号</th>
               <th width="100">申请总数</th>
               <th width="100">单号下限</th>
               <th width="100">单号上限</th>
               <th width="100">实际数量</th>
               <th width="80">状态</th>
               <th width="150">申请时间</th>
            </tr>
            <#if page.rows?default([])?size !=0>
            <#list page.rows as applyDo>
	             <tr>
	               <td width="80">${applyDo.id} </td>
	               <td width="120">${applyDo.logisticsName}</td>
	               <td width="120">${applyDo.logisticsCode}</td>
	               <td width="100">${applyDo.amount}</td>
	               <td width="100">${applyDo.waybillLow}</td>
	               <td width="100">${applyDo.waybillUp}</td>
	               <td width="100">${applyDo.actualAmount}</td>
	               <td width="80">${applyDo.statusStr}</td>
	               <td width="150">${applyDo.applyTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	            </tr>
            </#list>
            </#if>
          </table>
    	</div>
</div>
   <@pager  pagination=page  formId="applyListForm" /> 
</form>

</@backend>