<#include "/layout/inner_layout.ftl" />

<@sellContent title="首页" 
    js=[
    	'/static/scripts/item/sellerToBindSku.js'
    	]  
    css=[]>
    <script type="text/javascript" charset="utf-8">
    </script>
    <style>
    </style>
    
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">审核日志</h3>
	    </div>
    <div class="panel-body">   
        <input type="hidden" id="majorDetailId" name="majorDetailId" value="${majorDetailId}"/>  
	  	   	 <table width="200px" height="200px;" class="table table-bordered">   
	  	   	    <tr>
		  	   	 	<th width='150' style="text-align:center;">审核时间</th>
		  	   	 	<th width='80'  style="text-align:center;">审核人</th>
		  	   	 	<th width='75'  style="text-align:center;">审核结果</th>
		  	   	 	<th style="text-align:center;">审核描述</th>
		  	   	 	<th style="text-align:center;">驳回理由</th>
	  	   	 	</tr>
	  	   	 	<#list listOfJournal as journalResult>
	  	   	 	<tr>
                  <td align="center">${journalResult.auditDetailDO.auditTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                  <td align="center">${journalResult.auditDetailDO.auditUserName}</td>
                  <td align="center">
                     <#if "${journalResult.auditDetailDO.auditResult}"=="A">通过</#if>
				     <#if "${journalResult.auditDetailDO.auditResult}"=="R">未通过</#if>
				  </td>
                  <td align="center">${journalResult.auditDetailDO.auditDesc}</td>
                  <td align="center">
                      <#if "${journalResult.auditDetailDO.auditResult}"=="R">
                   <!--      <#if journalResult.rejectTypeList??&&journalResult.rejectTypeList?size gt 0> -->
                            <#list   journalResult.rejectTypeList as reject>
                               <#if rejectTypes?exists>
					                <#list rejectTypes?keys as key> 
					                 <#if key==reject>
					                   <div>
					                     ${rejectTypes[key]} 
					                   </div>
					                 </#if>   
					                </#list>
			                  </#if>   
			                </#list>  
			           <!-- </#if>	 -->				 	
					</#if>
                  </td>
                </tr>
	  	   	 	</#list>
	         </table>
    </div>
</div>
</@sellContent>
