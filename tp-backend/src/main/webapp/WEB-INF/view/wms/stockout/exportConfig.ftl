<#include "/common/common.ftl"/>
<@backend title="出库信息导出" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
'/statics/backend/js/wms/stockout.js'
] css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >
<#if '${msg}' == 'userError'>
当前用户异常
<#else>
<div id="table" class="mt10">
    <div class="box span10 oh">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
<#assign colEnumSize = stockOutColsList?size>
<#list stockOutColsList as col>
<#assign disabled = 'false'>
<!--
<#if '${col.beanCode}'=='storer' || '${col.beanCode}'=='wmwhseid' || '${col.beanCode}'=='externalNo' || '${col.beanCode}'=='externalNo2'>
<#assign disabled = 'true'>
</#if>
-->
	<#assign colspanNum = 7 - colEnumSize % 6>
	<#if col_index % 6 == 0>
		<#if col_index == colEnumSize -1 >
			<tr align="left">
				<td colspan="${colspanNum}">
				${col.beanName}：<input type="checkBox" name="beanCodeCol" value="${col.beanCode}" <#if "${configColMap['${col.beanCode}']}" != "">checked</#if> <#if disabled == 'true'>disabled</#if> />
				</td>
			</tr>
		<#else>
			<tr align="left">
				<td>
				${col.beanName}：<input type="checkBox" name="beanCodeCol" value="${col.beanCode}" <#if "${configColMap['${col.beanCode}']}" != "">checked</#if> <#if disabled == 'true'>disabled</#if> />
				</td>
		</#if>
	<#elseif col_index % 6 == 5>
				<td>
           		${col.beanName}：<input type="checkBox" name="beanCodeCol" value="${col.beanCode}" <#if "${configColMap['${col.beanCode}']}" != "">checked</#if> <#if disabled == 'true'>disabled</#if> />
           		</td>
        	</tr>
	<#else>
		<#if col_index == colEnumSize -1 >
          		<td colspan="${colspanNum}">
          		${col.beanName}：<input type="checkBox" name="beanCodeCol" value="${col.beanCode}" <#if "${configColMap['${col.beanCode}']}" != "">checked</#if> <#if disabled == 'true'>disabled</#if> />
          		</td>
          	</tr>
		<#else>
         		<td>
         		${col.beanName}：<input type="checkBox" name="beanCodeCol" value="${col.beanCode}" <#if "${configColMap['${col.beanCode}']}" != "">checked</#if> <#if disabled == 'true'>disabled</#if> />
         		</td>
       	</#if>
     </#if>
</#list>
				<tr align="center">
					<td colspan="6">
						<button class="btn btn-primary btn-sm" id="doExportBtn" >&nbsp; &nbsp;<B>导 出</B>&nbsp; &nbsp;</button>
					</td>
				</tr>

        </table>
     </div>
</div>
</#if>
</@backend>