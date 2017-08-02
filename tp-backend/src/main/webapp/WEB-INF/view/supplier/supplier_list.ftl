	<#include "/common/common.ftl"/>
	<@backend title="供应商列表" 
	    js=[
	    	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
			'/statics/select2/js/select2.js','/statics/select2/js/select2Util.js','/statics/select2/js/select2_locale_zh-CN.js',
			'/statics/backend/js/layer/layer.min.js',
	    	'/statics/supplier/js/common.js',
	        '/statics/supplier/js/web/supplier_list.js'
	       ] 
	    css=[
	    	'/statics/select2/css/select2.css',
			'/statics/select2/css/common.css',
			'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
	<div class="box_top">
	    <b class="pl15">采购管理->供应商->供应商列表</b> 
	</div>
	<form id="supplier_list_form" class="jqtransform" action="${domain}/supplier/supplierList.htm" method="post">
	        <table cellspacing="0" cellpadding="0" border="0" width="100%"
	            class="form_table pt15 pb15">
	            <tbody>
	                <tr>
	                    <td class="td_right" width="50" align="right">供应商编号：</td>
	                    <td class="" width="50" align="left">
	                    	<input type="text" name="id" class="input-text lh30" size="17" value=${(supplierInfo.id)!}>
						</td>
	
	                    <td class="td_right" width="50" align="left">供应商名称：</td>
	                    <td class="" width="50" align="left"><input type="text"
	                        name="name" class="input-text lh30" size="17" value="${(supplierInfo.name)!}"></td>
	
	                    <td class="td_right" width="50" align="left">类型：</td>
	                    <td align="left" width="50">
	                        <div class="select_border">
	                            <div class="select_containers">
	                                <span class="fl"> 
	                                <select class="select" name="supplierType" style="width: 130px;">
	                                        <option value="">全部</option>
	                                        <#if supplierTypeMap?exists>
	                                            <#list supplierTypeMap?keys as key>
													<#if "${supplierInfo.supplierType}" == "${key?if_exists}">  
	                                                	<option selected="selected" value="${key}">${supplierTypeMap[key]}</option>
							                        <#else>  
	                                                	<option value="${key}">${supplierTypeMap[key]}</option>
							                        </#if>   
	                                            </#list>
	                                        </#if>
	                                </select>
	                                </span>
	                            </div>
	                        </div>
	                    </td>
	                    
	                    <td class="td_right" width="50" align="left">状态：</td>
	                    <td align="left" width="150">
	                        <div class="select_border">
	                            <div class="select_containers">
	                                <span class="fl"> 
	                                <select class="select" name="auditStatus"
	                                    style="width: 130px;">
	                                        <option value="">全部</option>
	                                        <#if auditStatusMap?exists>
	                                            <#list auditStatusMap?keys as key>
													<#if "${supplierInfo.auditStatus}" == "${key?if_exists}">  
	                                                	<option selected="selected" value="${key}">${auditStatusMap[key]}</option>
							                        <#else>  
	                                               		<option value="${key}">${auditStatusMap[key]}</option>
							                        </#if>   
	                                            </#list>
	                                        </#if>
	                                </select>
	                                </span>
	                            </div>
	                        </div>
	                    </td>
	                </tr>
	                
	                <tr>
	                    <td class="td_right" width="50" align="left">品牌：</td>
	                    <td align="left">
	                        <input type="text" name="key1" class="input-text lh30" size="17" value="${supplierInfo.key1!}" id="sp_brandname">
	                    </td>
	                    <td class="td_right" width="50" align="left">是否海淘：</td>
	                    <td align="left" width="150" colspan="7">
	                        <div class="select_border">
	                            <div class="select_containers">
	                                <span class="fl"> 
	                                <select class="select" name="isSea" style="width: 130px;">
                            			<option value="">全部</option>
                            			<option <#if supplierInfo.isSea==1>selected="selected"</#if> value="1">是</option>
                                        <option <#if supplierInfo.isSea==0>selected="selected"</#if> value="0">否</option>
	                                </select>
	                                </span>
	                            </div>
	                        </div>
	                    </td>
	                </tr>
	                <tr>
	                    <td colspan="10" align="center"><input id="supplierListQueryReset" type="reset"
	                        name="button" class="btn btn82 btn_res" value="重置">
	                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
	                        <input type="button"
	                            name="button" id="supplierListQuery" class="btn btn82 btn_search" value="查询">
	                    </td>
	                </tr>
	            </tbody>
	        </table>
	        <input id="pageIndexId" type="hidden" value="1" name="index" />
	<div class="search_bar_btn  pt10 pb10 box_border" style="padding-left:5px;">
	    <input type="button" name="button" id="supplier_list_add" class="btn btn82 btn_add" value="新增">
	    <#-- <input type="button" value="？" class="ext_btn ext_btn_submit"> -->
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
	                                <th width="8%">供应商编号</th>
	                                <th width="26%">供应商名称</th>
	                                <th width="8%">类型</th>
	                                <th>备注</th>
	                                <th width="8%">状态</th>
	                                <th width="16%">操作</th>
	                            </tr>
	                            
	                        <#if page.rows?default([])?size !=0>       
	                        <#list page.rows as sl>
	                            <tr align="center" class="tr"
	                                style="background-color: rgb(255, 255, 255);">
	                                <td>${sl.id}</td>
	                                <td>${sl.name}</td>
	                                <td>${supplierTypeMap[sl.supplierType]}</td>
	                                <td>${sl.supplierDesc}</td>
	                                <td>
	                                    <#if sl.auditStatus?exists>
	                                        ${auditStatusMapAll[sl.auditStatus?c]}
	                                    <#else>
	                                    </#if>
	                                </td>
	                                <td>
	                                    <#if sl.auditStatus==0>
	                                    <a href="javascript:void(0)" onclick="showLicenEditPage(${sl.id})">编辑</a>
	                                    <#elseif sl.auditStatus == 1>
	                                    <a href="javascript:void(0)" onclick="showEditPage(${sl.id})">编辑</a>
	                                    <#elseif sl.auditStatus == 6>
	                                    <a href="javascript:void(0)" onclick="showDetailPage(${sl.id})">查看</a>&nbsp;&nbsp;
	                                    <a href="javascript:void(0)" onclick="showEditPage(${sl.id})">编辑</a>
	                                    <#elseif sl.auditStatus == 5>
	                                    <a href="javascript:void(0)" onclick="showEditPage(${sl.id})">编辑</a>
	                                    <#elseif sl.id==0>
	                                    <a href="javascript:void(0)" style="display:none;">查看</a>
	                                    <#elseif sl.auditStatus==4>
	                                    <a href="javascript:void(0)" onclick="showDetailPage(${sl.id})">查看</a>&nbsp;&nbsp;
	                                    <a href="javascript:void(0)" onclick="showEditPage(${sl.id})">编辑</a>
	                                    <#else>
	                                    <a href="javascript:void(0)" onclick="showDetailPage(${sl.id})">查看</a>&nbsp;&nbsp;
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
	<@pager  pagination=page  formId="supplier_list_form"  />                   
	</form>    
	
	</@backend>
