<#include "/common/common.ftl"/>
<@backend title="出库报检信息列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/wms/stockoutdecl.js'
] css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] > 
	<!-- 查询条件开始 -->
<form method="post" action="${domain}/wms/stockoutdecl/list.htm" id="stockOutDeclForm">	
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">出库报检管理</b></div>
            <div class="box_center pt10 pb10">
	       	<table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  	<td>货主编码：</td>    
                  	<td><input type="text" id="storerQo" name="storerQo" class="input-text lh25" size="20" value='${stockOutQO.storerQo}'></td>
                  	<td>电子口岸编号：</td>    
                  	<td><input type="text" id="externalNoQo" name="externalNoQo" class="input-text lh25" size="20" value='${stockOutQO.externalNoQo}'></td>
                  	<td>订单编号：</td>
                  	<td><input type="text" id="externalNo2Qo" name="externalNo2Qo" class="input-text lh25" size="20" value='${stockOutQO.externalNo2Qo}'></td>
                  	<td>推送时间：</td>
                  	<td>
	                  	<input type="text" name="startCreateTimeQo" id="startCreateTimeQo" readonly value='${stockOutQO.startCreateTimeQo?if_exists}' class="input-text lh25" size="20">
	                  	<span>到</span>
	                  	<input type="text" name="endCreateTimeQo" id="endCreateTimeQo" readonly  value='${stockOutQO.endCreateTimeQo?if_exists}' class="input-text lh25" size="20">
                  	</td>
                   <td>推送状态：</td>
                   <td>
                  	<div class="select_border"> 
                        <div class="select_containers "> 
		                    <select name="statusQo" class="select">
		                    	<option value=""  <#if '${stockOutQO.statusQo?if_exists}'==''>selected</#if>>全部状态</option>
		                    	<option value="0" <#if '${stockOutQO.statusQo?if_exists}'=='0'>selected</#if>>推送失败</option>
		                    	<option value="1" <#if '${stockOutQO.statusQo?if_exists}'=='1'>selected</#if>>推送成功</option>
		                    </select>
                    	</div>
                    </div>
                   </td>
                </tr>
            </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
              	 <a href="javascript:void(0);"><input id="uploadFileBtn"  class="btn btn82 btn_export" type="button" value="导入" name="button"  /></a>
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="$('#stockOutDeclForm').submit();" type="button" value="查询" name="button" /></a>
              </div>
            </div>
          </div>
        </div>
    </div>
    <!-- 查询条件结束 -->
    
    <!-- 列表开始 -->
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table" id="list_table">
                <tr>
           	   <th width="20">序号</th>
           	   <th width="80">货主编码</th>
           	   <th width="80">电子口岸编号</th>
               <th width="80">订单编号</th>
               <th width="50">推送数量</th>
               <th width="80">推送时间</th>
               <th width="50">推送状态</th>
               <th width="250">备注</th>
               <th width="80">操作</th>
                </tr>
            <#if page.rows?default([])?size !=0>
            <#list page.rows as stockOutQO>
	             <tr>
	               <td width="20" align="center">${stockOutQO_index+1}</td>
	               <td width="80">${stockOutQO.storer?if_exists}</td>
	               <td width="80">${stockOutQO.externalNo?if_exists}</td>
	               <td width="80">${stockOutQO.externalNo2?if_exists}</td>
	               <td width="50" align="right">${stockOutQO.tdq?if_exists}</td>
	               <td width="80" align="center">${stockOutQO.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	               <td width="50" align="center">
	               <#if '${stockOutQO.status?if_exists}' = '1' >
	               	成功
	               <#else>
					失败
	               </#if>
	               </td>
	               <td width="250">${stockOutQO.notes?if_exists}</td>
	               <td width="80" align="center">
	               <a href="javascript:void(0)" onclick="viewItem(${stockOutQO.id})">[查看明细]</a>
				   </td>
	            </tr>
            </#list>
            </#if>
              </table>
              
	     </div>
	</div><!-- 列表结束 -->
	<@pager  pagination=page  formId="stockOutDeclForm" /> 
</form>
</@backend>