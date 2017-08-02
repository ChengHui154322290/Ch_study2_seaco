<#include "/common/common.ftl"/>
<@backend title="" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/item/item-search.js',
'/statics/backend/js/item/item-copy.js',
'/statics/backend/js/item/item-category.js',
'/statics/select2/js/select2.js',
'/statics/select2/js/select2Util.js',
'/statics/select2/js/select2_locale_zh-CN.js',
'/statics/backend/js/item/item-select2.js'] 
	css=[
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/select2/css/select2.css',
	'/statics/select2/css/common.css'] >
	<input type="hidden" id="largeIdHidden" value="${largeId}" />
	<input type="hidden" id="mediumIdHidden" value="${mediumId}" />
	<input type="hidden" id="smallIdHidden" value="${smallId}" />
	<input type="hidden" id="supplierIdHidden" value="${supplierId}" />
	<input type="hidden" id="domainURL" value="${domain}" />
	
    <form method="post" action="${domain}/item/list.htm" id="waybillDeliveryForm">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_center pt10 pb10">
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
             	<tr>
				<td style="width:100px" nowrap='nowrap' >物流公司：</td>  
				<td colspan = 6 class=""> 
				<span class="fl"> 
					<select
						name="supplierId" class="select2" style="width:350px; margin-left: 1px" >
							<option value=""  >请选择</option>
						<#list logisticsList as ls>
							<option value="${ls.code}" <#if "${ls.id}"="${supplierId}"> selected </#if> >${s.name}</option>
						</#list>
					</select>
				</span>
				</td>
                  <td>SPU名称:</td>
                  <td><input type="text" name="spuName" value="${query.spuName}"  class="input-text lh25" size="20"></td>
                  <td>spu:</td>
                  <td><input type="text" name="spu" value="${query.spu}"  class="input-text lh25" size="20"></td>
                  <td>网站显示名称:</td>
                  <td>
                    <input type="text" name="name" value="${query.name}" class="input-text lh25" size="20">
                  </td>
                  <td>
	                <select class="select2" name="status" style="width:100px; margin-left: 1px" >
	                	<option value="">-- 全部 --</option>
	                	<option value="0" <#if "${status}"=="0">selected</#if> >未上架</option>
	                	<option value="1" <#if "${status}"=="1">selected</#if> >已上架</option>
	                	<option value="2" <#if "${status}"=="2">selected</#if> >作废</option>
	                </select>
	              </td>
                </tr>
              </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="$('#waybillDeliveryForm').submit();" type="button" value="查询" name="button" /></a>
              </div>
            </div>
          </div>
        </div>
    </div>
   <div id="table" class="mt10">
    	<div class="box span10 oh">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
            <tr>
           	   <th width="15"> </th>
           	   <th width="100">SPU名称</th>
           	   <th width="100">大类</th>
           	   <th width="100">中类</th>
           	   <th width="100">小类</th>
           	   <th width="100">spu</th>
           	   <th width="100">prdid</th>
           	   <th width="100">条形码</th>
               <th width="450">网站显示名称</th>
               <th width="150">类型</th>
               <th width="200">发布</th>
               <th width="200">创建日期</th>
               <th width="200">操作</th>
            </tr>
            <#if page.rows?default([])?size !=0>
            <#list page.rows as item>
             <tr class="tr">
               <td width="15"><input type="checkbox" name ="detailId"  class="detailIdCheckbox" value="${item.detailId}"/> </td>
               <td width="200">${item.spuName}</td>
               <td width="200">${item.largeName}</td>
               <td width="200">${item.mediumName}</td>
               <td width="200">${item.categoryName}</td>
               
               <td width="100">${item.spu}</td>
               <td width="100">${item.prdid}</td>
               <td width="100">${item.barcode}</td>
               <td width="500">${item.mainTitle}</td> 
               <td width="100"><#if item.wavesSign ==1>国内<#elseif item.wavesSign==2>海淘</#if></td> 
               <td width="100">${item.statusDesc}</td>
               <td width="200"><#if item.createTime??>${item.createTime?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
               <td width="200"><a  href="javascript:void(0);" onclick="show(${item.detailId});">[编辑]<a  href="javascript:void(0);" onclick="newPrd(${item.itemId});"> [PRD]</a>
               </td>
            </tr>
            </#list>
            </#if>
          </table>
    	</div>
</div>
   <@pager  pagination=page  formId="itemSearchForm"  /> 
</form>
</@backend>