<#include "/common/common.ftl"/>
<@backend title="" js=['/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
	'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
	'/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	'/statics/backend/js/user/review.js',
	'/statics/backend/js/user/upload-file.js',
	'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js']
    css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css']  >
<form id="importLogForm" action="import" method="post" >
	  <input type="hidden" name="logId" id="logId" value="${importLog.id}" />
	  <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top">
             <b class="pl15">商品评论导入</b>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
           	  <div  style="text-align:right; float:left">
           	  	 上传 ：
                 <input id="uploadFileBtn" class="btn btn82 btn_export" type="button" value="上传" name="button"  />		
              </div>
              <div class="search_bar_btn" style="text-align:right;">
               	 <select class="select" name="status">
                    	<option value="0">-- 全部 --</option>
                    	<option value="1" <#if "${status}"=="1"> selected </#if>>导入成功</option>
                    	<option value="2" <#if "${status}"=="2"> selected </#if>>导入失败</option>
                 </select>
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search"  type="button" id="queryImportLogBtn"  value="查询" name="button" onclick="$('#importLogForm').attr('action','import');$('#importLogForm').submit();" /></a>
                 <input class="btn btn82 btn_export" type="button" value="导出"    id="exportFileBtn" />
              </div>
              <div class="mt10" > 
    			  <span style="margin-right:15px;">总数量：${importLog.sumCount}</span>
    			  <span style="margin-right:15px;">导入成功：${importLog.successCount}</span>
    			  <span style="margin-right:15px;">导入失败：${importLog.failCount}  </span>
    			  <span style="margin-right:15px;">状态：<#if importLog.status==1> 等待处理</#if> <#if importLog.status==2> 正在处理</#if> <#if importLog.status==4> 处理完成 </#if>  </span>
    			  <#-->
    	 	 	  <span>导入未处理：${importLog.sumCount - importLog.failCount - importLog.successCount}  </span>
    	 	 	  </#-->
    	 	 </div>
            </div>
          </div>
        </div>
    </div>
     <div id="table" class="mt10">
    	<div class="box span10 oh">
    	<div style="overflow:auto;">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table list_table_no_lh" style="min-width:1300px;">
            <tr>
              <th  width="60">状态</th>
           	  <th width="300">原因</th>
           	  <th width="60">PRD</th>
           	  <th width="60">用户名(手机号)</th>
           	  <th width="60">评论内容</th>  	  
           	  <th width="60">订单号</th>
           	  <th width="60">分值</th>
           	  <th width="100">置顶</th>
           	  <th width="60">隐藏</th>
            </tr>
            <#if page.list?default([])?size !=0>
            <#list page.list as l>
             <tr>
               <#if l.status == 2>
                <td width="60">失败</td>
               </#if>
               <#if l.status == 1>
                <td width="60">成功</td>
               </#if>
               <td width="300">${l.opMessage}</td>
               <td width="60">${l.pid}</td>
               <td width="60">${l.userName}</td>
               <td width="60">${l.content}</td>       
               <td width="60">${l.orderCode}</td>
               <td width="60">${l.mark}</td>
               <#if l.isTop == true>
               	<td width="60">置顶</td>     
               <#else>
                <td width="60">置底</td>
               </#if>
               
               <#if l.isHide == true>
                <td width="60">隐藏</td>
               <#else>
                <td width="60">仅自己可见</td>
               </#if>
            </tr>
            </#list>
            <#else>
       	  		<tr style="text-align:center">
           	 	  <td width="100" colspan=13>无数据</td>
          	    </tr>
            </#if>
          </table>
         <div> 
    	</div>
    	<@pager  pagination=page  formId="importLogForm"  /> 
</form>
</@backend>
