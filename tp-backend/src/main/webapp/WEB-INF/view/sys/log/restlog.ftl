<#include "/common/common.ftl"/>
<@backend title="API日志列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
'/statics/select2/js/select2.js',
'/statics/select2/js/select2Util.js',
'/statics/select2/js/select2_locale_zh-CN.js',
'/statics/backend/js/item/item-select2.js',
'/statics/backend/js/sys/log/rest.js'
] 
css=[
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/select2/css/select2.css',
	'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css'
] >

<form method="post" action="${domain}/sys/log/restloglist.htm" id="restfulLogListForm">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">REST请求日志列表</b></div>
            <div class="box_center pt10 pb10">
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>日志类型：</td>
                  <td>
                	<select name="type" class="select2" id="type" style="width:250px; margin-left: 1px">
							<option value="" >--请选择类型--</option>
							<#if restTypeList??>
								<#list restTypeList as rt>
									<option value="${rt.code}" <#if "${rt.code}"=="${log.type}">selected</#if> >${rt.desc}</option>
								</#list>
							</#if>
					</select>	
                  </td>
                  <td>请求时间从：</td>
                  <td><input type="text" name="startTime" class="input-text lh25" size="16" value="<#if log.startTime??>${(log.startTime)?string("yyyy-MM-dd HH:mm:ss")}</#if>"></td>
                  <td>至</td>
                  <td><input type="text" name="endTime" class="input-text lh25" size="16" value="<#if log.endTime??>${(log.endTime)?string("yyyy-MM-dd HH:mm:ss")}</#if>"></td>
                </tr>
              </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="$('#restfulLogListForm').submit();" type="button" value="查询" name="button" /></a>
              	 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="view('aes');" type="button" value="AES" name="button" /></a>
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="view('rsa');" type="button" value="RSA" name="button" /></a>
              </div>
            </div>
          </div>
        </div>
    </div>
   <div id="table" class="mt10">
    	<div class="box span10 oh">
          <table width="100%" border="0" style="TABLE-LAYOUT:fixed;word-break:break-all" cellpadding="0" cellspacing="0" class="list_table" id="restLogList">
            <tr>
               <th width="15%">编号</th>
           	   <th width="10%">类型</th>
           	   <th width="15%">方法</th>
           	   <th width="15%">URL</th>
           	   <th width="15%">内容</th>
               <th width="10%">返回数据</th>
               <th width="10%">请求时间</th>
               <th width="10%">备注</th>
            </tr>
            <#if page.rows?default([])?size !=0>
            <#list page.rows as rl>
	             <tr>
	               <td width="15%">${rl.id}</td>
	           	   <td width="10%">${rl.typeStr}</td>
	           	   <td width="15%">${rl.methodStr}</td>
	           	   <td width="15%">${rl.url}</td>
	           	   <td width="15%">${rl.content}</td>
	               <td width="10%">${rl.result}</td>
	               <td width="10%">${rl.requestTime?string('yyyy-MM-dd HH:mm:ss')}</td>
	               <td width="10%">${rl.remain1}</td>
	            </tr>
            </#list>
            </#if>
          </table>
    	</div>
</div>
   <@pager  pagination=page  formId="restfulLogListForm" /> 
</form>

</@backend>