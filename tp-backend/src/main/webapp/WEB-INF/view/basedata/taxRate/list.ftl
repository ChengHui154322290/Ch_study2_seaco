<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/taxRate.js'] 
	css=[] >
 <form class="jqtransform" method="post" id="queryForm" action="${domain}/basedata/taxRate/list.htm">
    <div>	      
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="td_right">税率编号:</td>
                  <td><input type="text" name="myCode" class="input-text lh25" size="20" value='${taxRateDO.code}'></td>
                  <td class="td_right">税率:</td>
                  <td><input type="text" name="myRate" class="input-text lh25" size="20" value='${taxRateDO.rate}'></td>
                  <td class="td_right">税率类型:</td>
	              <td>
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="type" class="select"> 
                         <option value="">-全部税率类型-</option> 
                         <#if taxRateTypes?exists>
			                <#list taxRateTypes?keys as key> 
			                  <option value="${key}"  <#if key==taxRateDO.type>selected='selected'</#if>>${taxRateTypes[key]}</option> 
			                </#list>
			            </#if>
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                  &nbsp; &nbsp; &nbsp;
                  <td>状态</td>
                 <td>
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers ">
                        <select name="status" class="select"> 
                            <option value=''      <#if  taxRateDO.status==null>selected='selected'</#if>>全部</option> 
	                        <option value="1"  <#if taxRateDO.status??&&taxRateDO.status?string=='1'>selected='selected'</#if>>有效</option> 
	                        <option value="0" <#if taxRateDO.status??&&taxRateDO.status?string=='0'>selected='selected'</#if>>无效</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                </tr>
              </table>
	   </div>
 
    <div >
	  <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:center;">
                  <input class="btn btn82 btn_search" type="submit" value="查询" />
                  <input class="btn btn82 btn_res " type="button" value="重置"  onclick="dataReset(this)" />
                 <input class="btn btn82 btn_add addcatabtn taxRateaddbtn" type="button" value="新增" name="button">
              </div>
        </div>
    </div>
    
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">税率ID</th>
	                  <th width="100">税率编号</th>
	                  <th width="100">类型</th>
	                  <th width="100">税率</th>
	                  <th width="100">完税金额</th>
	                  <th width="150">状态</th>
	                  <th width="100">创建时间</th>
	                  <th width="100">更新时间</th>
					  <th width="200">备注</th>
					  <th width="100">操作</th>
                </tr>
            <#list queryAllTaxRateByPage.rows as taxRate>
                <tr class="tr" >
		              <td class="td_center">${taxRate.id}
                      <input type="hidden" value=${taxRate.id} />
                      </td>
		              <td class="td_center">${taxRate.code}</td>
		              <td class="td_center">
		               <#if taxRateTypes?exists>
			                <#list taxRateTypes?keys as key> 
			                   <#if taxRate.type==key>${taxRateTypes[key]}</#if>
			                </#list>
			            </#if>	              
		              </td>
		              <td class="td_center">${taxRate.rate}</td>
		              <td class="td_center">${taxRate.dutiableValue}</td>
		              <td class="td_center"><#if taxRate.status=='1'>有效<#else>无效</#if></td>		            
		              <td class="td_center">${taxRate.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                      <td class="td_center">${taxRate.modifyTime?string("yyyy-MM-dd HH:mm:ss")}</td>  
                      <td class="td_center">${taxRate.remark}</td>					  
		              <td class="td_center"><a href="javascript:void(0);"  class="editcatabtn taxRateeditbtn" param='${taxRate.id}'>[编辑]</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="">[日志]</a></td>	
	             </tr>
	            </#list>
              </table>
	     </div>
	</div>
	 <@pager  pagination=queryAllTaxRateByPage  formId="queryForm"  /> 
	</form>
</@backend>