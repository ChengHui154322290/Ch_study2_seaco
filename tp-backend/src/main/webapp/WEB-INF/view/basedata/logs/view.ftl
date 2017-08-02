<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js'] 
	css=[
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
	] >
  <form class="jqtransform" method="post" id="logsForm" action="${domain}/basedata/logs/view.htm"> 
    <div id="table" class="mt10">
        <div class="box span10 oh">
            <input type="hidden" name='id' value=${id} />
            <input type="hidden" name='type' value=${type} />
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">发生时间</th>
                      <th width="40">操作人</th>
                      <th width="40">操作类型</th>
                      <th width="40">字段名称</th>
	                  <th width="300">内容</th>     
                </tr>
           
          <#list queryAllLogsByPage.rows as logss> 
                <tr class="tr">
		              <td class="td_center">${logss.optionTime?string("yyyy-MM-dd HH:mm:ss")}<input type="hidden" value=${logss.id} /></td>
		              <td class="td_center">
		                <#list userList as user>
		                   <#if logss.userId==user.id>${user.userName}</#if>
		                </#list>
		              </td>
		              
		              <td class="td_center"><#if logss.optionType==1>增加<#else>更新</#if></td> 
		              <td class="td_center">${logss.optionFieldName}</td>
		              <#if logss.optionType==1>
		                   <#if logss.optionFieldName='品牌Logo' >
		                       <td class="td_center"><img src="<@itemImageDownload code="${logss.afterwardsValue}" width="60" />" /></td>
		                   <#elseif logss.optionFieldName='所属地区'>
		                       <td class="td_center">
		                       <#if allCountryAndAllProvince?exists>
			                       <#list allCountryAndAllProvince?keys as key> 
			                          <#if logss.afterwardsValue==key>${allCountryAndAllProvince[key]}</#if>
			                       </#list>
			                   </#if>	                       
		                       </td>
		                   <#else>
		                       <td class="td_center">${logss.afterwardsValue}</td> 
		                   </#if>    
		              <#else>
		                   <#if logss.optionFieldName='品牌Logo'>
		                    <td class="td_center">原值:<img src="<@itemImageDownload code="${logss.previousValue}" width="60" />" />
		                                                                                                                              新值:<img src="<@itemImageDownload code="${logss.afterwardsValue}" width="60" />" /></td> 
		                   <#elseif logss.optionFieldName='所属地区'>
		                       <td class="td_center">原值:
		                       <#if allCountryAndAllProvince?exists>
			                       <#list allCountryAndAllProvince?keys as key> 
			                          <#if logss.previousValue==key>${allCountryAndAllProvince[key]}</#if>
			                       </#list>
			                   </#if>
			                                                                                                                         新值:	
			                    <#if allCountryAndAllProvince?exists>
			                       <#list allCountryAndAllProvince?keys as key> 
			                          <#if logss.afterwardsValue==key>${allCountryAndAllProvince[key]}</#if>
			                       </#list>
			                   </#if>                                                                                                                            
		                       </td>
		                   <#else>
		                       <td class="td_center">原值:${((logss.previousValue)?length>0)?string((logss.previousValue),"空")};新值:${((logss.afterwardsValue)?length>0)?string((logss.afterwardsValue),"空")}</td>
		                   </#if>
		              </#if>                
	             </tr>
	      </#list>
              </table>
	     </div>
	</div>
     <@pager  pagination=queryAllLogsByPage  formId="logsForm" />  
   </form>
</@backend>