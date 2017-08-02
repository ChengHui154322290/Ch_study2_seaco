<#include "/common/common.ftl"/>
<@backend title="商品评论" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
	'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
	'/statics/backend/js/comment/review/list.js'
]
css=[
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css' 
] >

<form class="jqtransform" method="post" id="itemReviewForm" action="${domain}/comment/review/list.htm">
	<div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
          <div class="box_top"><b class="pl15">商品评论</b>
          </div>
    <div class="box_center pt10 pb10">
		<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15" >
			<tr>
         		<td ><span class="requiredField">*</span>SPU：</td>
         		<td >
					<input type="text" name="spu" value="${review.spu}" class="input-text lh25" size="20">
				</td>                                    
         		<td>评论日期:</td>
         		<td colspan="3">
         			<input type="text" name="createBeginTime" id="createBeginTime" value='<#if review.createBeginTime??>${review.createBeginTime?string("yyyy-MM-dd")}</#if>' class="_dateField input-text lh25" size="20" readonly/>
            		<span>到</span>
            		<input type="text" name="createEndTime" id="createEndTime" value='<#if review.createEndTime??>${review.createEndTime?string("yyyy-MM-dd")}</#if>' class="_dateField input-text lh25" size="20" readonly/>
         		</td>
			</tr>
			<tr>
         		<td>会员昵称:</td>
         		<td >
         			<input type="text" name="userName" placeholder="用户名" class="input-text lh25" size="20" value="${review.userName}"/>
         		</td>
			</tr>
			<tr>
				<td width="100">
				<td width="50"> 
					<input class="btn btn82 btn_res" type="button" value="重置" onclick="dataReset(this)"/>
				</td>
				<td width="50">
					 <a href="javascript:void(0);"><input class="btn btn82 btn_search itemReviewQuery" type="button" value="查询" name="button"/></a>
				</td>
			</tr>				
	</table>
	</div> 
	<div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
      <div class="search_bar_btn" style="text-align:right;">
        <a href="javascript:void(0);"><input class="btn btn82 btn_add itemReviewAdd" type="button" value="添加" name="button" /></a>
        <a href="javascript:void(0);"><input class="btn btn82 btn_export itemReviewImport"  type="button" value="导入" name="button" disabled="disabled"/></a>
      </div>
    </div> 
   	</div>
 </div>
 </div>
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="50">ID</th>
                       <th width="50">会员昵称</th>
	                  <th width="50">SPU</th>
	                  <th width="50">SKU</th>
	                  <th width="200">商品名称</th>
	                  <th width="30">评论分值</th>
	                  <th width="200">评论内容</th>
	                  <th width="100">评论日期</th>       
                      <th width="80">操作</th>
                </tr>
              <#list page.rows as reviewDo>               
                <tr class="tr" >
		              <td class="td_center">${reviewDo.id} </td>
		              <td class="td_center">${reviewDo.userName} </td>
		              <td class="td_center">${reviewDo.spu}</td>
		              <td class="td_center">${reviewDo.skuCode}</td>
		              <td class="td_center">${reviewDo.itemTitle}</td>
		              <td class="td_center">${reviewDo.mark}</td>
		              <td class="td_center">${reviewDo.content}</td>
		              <td class="td_center">${reviewDo.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
		              <td>
                      	<a href="javascript:void(0)" class="detailReview" param='${reviewDo.id}'>[查看]</a>
                      </td>                  
	             </tr>
	            </#list>
	            <#if  onData ?? >
				     <tr class="tr" >
				     	${onData}
				   	 </tr>
				</#if> 	               
              </table>
	     </div>
	</div> 
     <@pager  pagination=page  formId="itemReviewForm"  />  
   </form>
</@backend>