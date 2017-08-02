<#include "/common/common.ftl"/> 
<#include "/supplier/common/page.ftl" />
<@backend title="合同维护" 
    js=['/statics/supplier/js/common.js',
        '/statics/supplier/js/web/contract_list.js',
        '/statics/supplier/js/validator.js',
        '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
        '/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js'
       ] 
    css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
            <div class="box_top">
                <b class="pl15">采购管理->供应商->合同维护</b> 
            </div>
           <form id="contract_list_form" class="jqtransform" action="${domain}/supplier/contract/list.htm" method="post" enctype="multipart/form-data">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td class="td_right" width="50" align="right">供应商编号：</td>
                                <td class="" width="50" align="left"><input type="text"
                                    name="supplierId" class="input-text lh30" size="17" value=${(contract.supplierId)!}></td>

                                <td class="td_right" width="50" align="left">供应商名称：</td>
                                <td class="" width="50" align="left"><input type="text"
                                    name="supplierName" class="input-text lh30" size="17" value=${(contract.supplierName)!}></td>
                                    
                                <td class="td_right" width="50" align="left">类型：</td>
                                <td align="left"  width="50">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> 
                                            	<select class="select" name="contractType" style="width: 140px;" >
                                            		<option value="">全部</option>
							                         <#if contractTypesMap?exists>
                                                        <#list contractTypesMap?keys as key>
															<#if contract?? && contract.contractType == key>
	                                                        	<option selected="selected" value="${key}">${contractTypesMap[key]}</option>
									                        <#else>  
	                                                        	<option value="${key}">${contractTypesMap[key]}</option>
									                        </#if>   
                                                        </#list>
                                                    </#if>
                                            	</select>
                                            </span>
                                        </div>
                                    </div>
                               </td>
                                    
                                <td class="td_right" width="50" align="left">状态：</td>
                                <td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> <select class="select" name="auditStatus"
                                                style="width: 130px;">
                                                <option value="">全部</option>
                                                    <#if contractStatusMap?exists>
						                                <#list contractStatusMap?keys as key>
						                                   <#if contract?? && contract.auditStatus == key>
						                                   <option selected="selected" value="${key}">${contractStatusMap[key]}</option>
						                                   <#else>
						                                   <option value="${key}">${contractStatusMap[key]}</option>
						                                   </#if>
						                                </#list>
						                            </#if>
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                                <tr>
                                
                                <tr>
	                                <td class="td_right" width="50" align="right">合同编号：</td>
	                                <td class="" width="50" align="left">
	                                    <#-- <input type="text" name="contractId" class="input-text lh30" size="17" value=${(contract.id)!}> -->
	                                    <input type="text" name="contractCode" class="input-text lh30" size="17" value="${(contract.contractCode)!}">
	                                </td> 
	                                <td class="td_right" width="50" align="right">合同名称：</td>
	                                <td class="" width="50" align="left"><input type="text"
	                                    name="contractName" class="input-text lh30" size="17" value=${(contract.contractName)!}></td>
	                                                                   
	                                <td class="td_right" width="50" align="right" >时间：</td>
	                                <td class="" align="left" colspan="3">
	                                	<input type="text" name="startDate" id="c_starDate_1" class="_dateField input-text lh30" size="20" value="<#if contract.startDate??>${contract.startDate?string("yyyy-MM-dd")}</#if>">&nbsp;~&nbsp;<input type="text" name="endDate" class="_dateField input-text lh30" id="c_endDate_1" size="20" value="<#if contract.endDate??>${contract.endDate?string("yyyy-MM-dd")}</#if>">
	                                </td>     
	                                	                                    
                            </tr>

                            <tr>
                                <td colspan="8" align="center"><input type="reset" id="contractListQueryReset"
                                    name="button" class="btn btn82 btn_res" value="重置">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="button"
                                    name="button" id="contractListQuery" class="btn btn82 btn_search" value="查询">
                                </td>
                            </tr>
                            </tbody>
                    </table>
                    <div class="search_bar_btn  pt10 pb10 box_border" style="padding-left:5px;">
                		<input type="button" name="button" id="contract_list_add" class="btn btn82 btn_add" value="新增">
           		 	</div>
           		 	<div class="box_center  pt10 pb10">
                <table cellspacing="0" cellpadding="0" border="0" width="100%"
                    class="form_table pt15 pb15">
                    <tbody>
                        <tr>
                            <td colspan="8">
                                <table width="100%" cellspacing="0" cellpadding="0" border="0"
                                    class="list_table CRZ" id="CRZ0">
                                    <tbody>
                                        <tr align="center">
                                            <th width="8%">合同编号</th>
                                            <th width="24%">合同名称</th>
                                            <th width="24%">供应商名称</th>
                                            <th width="8%">类型</th>
                                            <th width="16%">有效期</th>
                                            <th>状态</th>
                                            <th width="12%">操作</th>
                                        </tr>
                                	  <#if page.rows?default([])?size !=0>       
                                	  <#list page.rows as c>
                                        <tr align="center" class="tr"
                                            style="background-color: rgb(255, 255, 255);">
                                            <td>${c.contractCode}</td>
                                            <td>${c.contractName}</td>
                                            <td>${c.supplierName}</td>
                                            <td>${contractTypesMap[c.contractType]}</td>
                                            <td>${(c.startDate?string("yyyy-MM-dd"))!} 至 ${(c.endDate?string("yyyy-MM-dd"))!}</td>
                                            <td>${contractStatusMap[c.auditStatus?string]}</td>
                                            <td>   
                                                <#if c.auditStatus==1 || c.auditStatus==5>
                                                <a href="javascrpt:void(0)" onclick="showEditPage(${c.id})">编辑</a>
                                                <#else>
                                                <a href="javascript:void(0)" onclick="showDetailPage(${c.id})">详情</a>
                                                </#if>
                                            </td>
                                        </tr>
                                       </#list>
                               		   </#if>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                    </tbody>
                </table>           
            </div>
            <@pager  pagination=page  formId="contract_list_form"  /> 
          </form>     
            

</@backend>
