<#include "/common/common.ftl"/>
<@backend title="" js=['/statics/backend/js/layer/layer.min.js',
		'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
		'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
		'/statics/backend/js/comment/viewpoint/viewpoint-search.js',
		'/statics/backend/js/comment/viewpoint/viewpoint.js'
		] 
	css=[
		'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
	] >
	
    <form method="post" action="${domain}/comment/viewpoint/list.htm" id="viewPointSearchForm">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">西客观点</b>

            </div>
            <div class="box_center pt10 pb10">
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
             	<tr>
				<td ><span class="requiredField">*</span>SPU：</td>
				<td >
					<input type="text" name="spu" value="${query.spu}" class="input-text lh25" size="20">
				</td>
				<td ><span class="requiredField">*</span>条形码：</td>
				<td >
					<input type="text" name="barcode" value="${query.barcode}" class="input-text lh25" size="20">
				</td>
				<td >商品名称：</td>
				<td >
					<input type="text" name="itemTitle" value="${query.itemTitle}" class="input-text lh25" size="20">
				</td>
				
				</tr>
				<tr>
				<td >会员账户：</td>
				<td >
					<input type="text" name="memLoginName" value="${query.memLoginName}" class="input-text lh25" size="20">
				</td>
				<tr>
				<td >会员昵称：</td>
				<td >
					<input type="text" name="memNickName" value="${query.memNickName}" class="input-text lh25" size="20">
				</td>
				<td >评论日期：</td>
				<td colspan="3">
                  	<input type="text" name="createStartDate" id="createStartDate" value="<#if query.createStartDate??>${query.createStartDate?string("yyyy-MM-dd HH:mm:ss")}</#if>" class="_dateField input-text lh25" size="20">
                  	<span>到</span>
                  	<input type="text" name="createEndDate" id="createEndDate" value="<#if query.createEndDate??>${query.createEndDate?string("yyyy-MM-dd HH:mm:ss")}</#if>" class="_dateField input-text lh25" size="20">
                </td>
				</tr>
				
              </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <a href="list.htm">
                  <input class="btn btn82 btn_export itemImportBtn" type="button" value="重置" name="button" />
                 </a>
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search"  onclick="$('#viewPointSearchForm').submit();" type="button" value="查询" name="button" /></a>
                 <a href="add.htm">
                 	<input class="btn btn82 btn_add itemeditbtn" type="button" value="新增" name="button"
                 		onclick='window.open(this.parentNode.href,"_self");'  />
                 </a>
              </div>
            </div>
          </div>
        </div>
    </div>
   <div id="table" class="mt10">
    	<div class="box span10 oh">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
            <tr>
               <th width="50">编号</th>
           	   <th width="100">是否置顶</th>
           	   <th width="50">排序</th>
           	   <th width="50">是否隐藏</th>
           	   <th width="50">条形码</th>
           	   <th width="50">SPU</th>
           	   <th width="200">商品名称</th>
           	   <th width="50">分值</th>
           	   <!--<th width="200">观点</th>-->
               <th width="100">日期</th>
               <th width="50">操作</th>
            </tr>
            <#if page.rows?default([])?size !=0>
            <#list page.rows as item>
             <tr class="tr">
             	<td>${item.id}</td>	
               <td width="15">
               <#if item.stickSign =="2">置顶
               <#elseif item.stickSign =="0">置底
               <#elseif item.stickSign == "1">默认
               <#else>
               </#if>
               	</td>
               <td width="50">${item.sort}</td>	
               <td width="15">
               <#if item.hideSign =="0">不隐藏
               <#elseif item.hideSign =="1">隐藏
               <#else>
               </#if>
               	</td>
               <td width="50">${item.barcode}</td>
               <td width="50">${item.spu}</td>
               <td width="200">${item.itemTitle}</td>
               <td width="50">${item.score}</td>
               <!--<td width="200">${item.content}</td>-->
               <td width="100"><#if item.viewpointTime??>${item.viewpointTime?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
               <td width="50"><a href="${domain}/comment/viewpoint/edit.htm?id=${item.id}">[修改]</a>
               </td>
            </tr>
            </#list>
            </#if>
          </table>
    	</div>
</div>
   <@pager  pagination=page  formId="viewPointSearchForm"  /> 
</form>
</@backend>