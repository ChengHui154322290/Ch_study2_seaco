<#include "/common/common.ftl"/> 
<@backend title="报价单" 
    js=[
		'/statics/backend/js/layer/layer.min.js'
		'/statics/supplier/js/common.js',
        '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
        '/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
        '/statics/supplier/js/validator.js',
        '/statics/supplier/js/web/quotation_list.js'
       ] 
    css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
    <div class="box_top">
                <b class="pl15">采购管理->供应商->报价单</b> 
            </div>
                <form id="quotation_list_form" class="jqtransform" action="${domain}/supplier/quotationList.htm" method="post">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td class="td_right" style="width:100px;" align="right">供应商编号：</td>
                                <td class="" width="50" align="left"><input type="text"
                                    name="supplierId" class="input-text lh30" size="17" value=${(quotationDO.supplierId)!}></td>

                                <td class="td_right" style="width:100px;" align="left">供应商名称：</td>
                                <td class="" width="50" align="left"><input type="text"
                                    name="supplierName" class="input-text lh30" size="17" value=${(quotationDO.supplierName)!}></td>
                                <td class="td_right" style="width:100px;" align="left">状态：
                                </td>
                                <td style="width:120px;">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> 
                                            <select class="select" name="auditStatus"
                                                style="width: 130px;">
                                                    <option value="">全部</option>
                                             		<#list auditStatusMapStr?keys as key>
					                                   <#if quotationDO?? && quotationDO.auditStatus == key>
					                                   <option selected="selected" value="${key}">${auditStatusMapStr[key]}</option>
					                                   <#else>
					                                   <option value="${key}">${auditStatusMapStr[key]}</option>
					                                   </#if>
					                                </#list>
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                         
                                <td class="td_right" style="width:100px;" align="left">类型：</td>
                                <td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> 
                                            <select class="select" name="contractType"
                                                style="width: 130px;">
                                                    <option value="">全部</option>
                                                    <#list supplierTypes?keys as key>
					                                   <#if quotationDO?? && quotationDO.contractType == key>
					                                   <option selected="selected" value="${key}">${supplierTypes[key]}</option>
					                                   <#else>
					                                   <option value="${key}">${supplierTypes[key]}</option>
					                                   </#if>
					                                </#list>
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                             </tr>
                             
                             <tr>
	                             <td class="td_right" style="width:100px;" align="right">报价单编号：</td>
	                                <td class="" width="50" align="left"><input type="text"
	                                    name="id" class="input-text lh30" size="17" value=${(quotationDO.id)!}></td>
	
	                                <td class="td_right" style="width:100px;" align="left">报价单名称：</td>
	                                <td class="" width="50" align="left"><input type="text"
	                                    name="quotationName" class="input-text lh30" size="17" value=${(quotationDO.quotationName)!}></td>
	                                <td class="td_right" style="width:100px;" align="left">时间：
	                                </td>
	                                <td style="width:100px;" align="left" colspan="3">
	                                	<input type="text" name="startDate" id="c_starDate_2" class="_dateField input-text lh30" size="14" value="<#if quotationDO.startDate??>${quotationDO.startDate?string("yyyy-MM-dd")}</#if>">&nbsp;~&nbsp;<input type="text" id="c_endDate_2" name="endDate" class="_dateField input-text lh30" size="14" value="<#if quotationDO.endDate??>${quotationDO.endDate?string("yyyy-MM-dd")}</#if>">
	                                </td>
                              </tr>

                            <tr>
                                <td colspan="8" align="center"><input type="reset" id="quotationListQueryReset"
                                    name="reset" class="btn btn82 btn_res" value="重置">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                    <input type="button"
                                        name="button" id="quotationListQuery" class="btn btn82 btn_search" value="查询">
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="search_bar_btn  pt10 pb10 box_border" style="padding-left:5px;">
                		<input type="button" name="button" id="quotation_list_add" class="btn btn82 btn_add" value="新增">
						<!-- by zhs 0303-->
                		<input type="button" name="button" id="quotation_list_import" class="btn btn82 btn_add" value="导入">
						
						<input type="button" name="button" class="btn btn82 btn_export" value="导出" />


						<!--
					    <a href="quotationImport.htm">
							<input class="btn btn82 btn_export itemImportBtn" type="button" value="导入" name="button"
							onclick='window.open(this.parentNode.href,"_self");'  />																			
						</a>
						-->
            		</div>
            		<div class="box_center  pt10 pb10">
                <table width="100%" cellspacing="0" cellpadding="0" border="0"
                    class="list_table CRZ" id="CRZ0">
                    <tbody>
                        <tr align="center">
                        	<th width="6%">报价单编号</th>
                            <th width="20%">报价单名称</th>
                            <th width="6%">合同编号</th>
                            <th width="20%">合同名称</th>
                            <th width="20%">供应商名称</th>
                            <th width="6%">类型</th>
                            <th width="8%">状态</th>
                            <th>操作</th>
                        </tr>
                        
                <#if page.rows?default([])?size !=0>       
                    <#list page.rows as qu>
                        <tr align="center" class="tr"
                            style="background-color: rgb(255, 255, 255);">
                            <td>${qu.id}</td>
                            <td>${qu.quotationName}</td>
                            <td>${qu.contractCode}</td>
                            <td>${qu.contractName}</td>
                            <td>${qu.supplierName}</td>
                            <td>${supplierTypes[qu.contractType]}</td>
                            <td>
                            	<#if qu.auditStatus?exists>
                                    ${auditStatusMapStr[qu.auditStatus?c]}
                                <#else>
                                </#if>
                            </td>
                            <td>
                                <#if qu.auditStatus==1>
                                <a href="javascript:void(0)" onclick="showEditPage(${qu.id})">编辑</a>
                                <#elseif qu.auditStatus==5>
                                <a href="javascript:void(0)" onclick="showEditPage(${qu.id})">编辑</a>
                                <#else>
                                <a href="javascript:void(0)" onclick="showDetailPage(${qu.id})">查看</a>
                                </#if>
                        </tr>
                         </#list>
                   </#if>
                    </tbody>
                </table>
            </div>
                   <@pager  pagination=page  formId="quotation_list_form"  /> 
                </form>        
</@backend>
