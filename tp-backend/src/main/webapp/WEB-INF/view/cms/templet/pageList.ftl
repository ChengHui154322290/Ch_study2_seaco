<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="模板管理" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
        '/statics/cms/js/jquery/jquery.ui.core.js',
        '/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/cms/js/jquery/jquery.form.js',
		'/statics/cms/js/templet/pageList.js'
       ] 
    css=['/statics/backend/css/style.css',
    		'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css']>
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">CMS管理->模板页面管理</b> 
            </div>
            <div class="box_center">
            	<form id="contract_list_form" class="jqtransform" method="post" action="${domain}/cms/queryPageList.htm">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td colspan="1" class="td_right" width="50" align="right">页面编号：</td>
                                <td class="" width="50" align="left"><input type="text" value="${query.pageCode}"
                                    name="pageCode" class="input-text lh30 pageCode" size="20"></td>

								<td colspan="1" class="td_right" width="50" align="right">页面名称：</td>
                                 <td class="" width="50" align="left"><input type="text" value="${query.pageName}"
                                    name="pageName" class="input-text lh30 pageName" size="20"></td>
                                
                                <td colspan="1" class="td_right" width="50" align="left">状态：</td>
                            	<td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> <select class="select status" name="status" 
                                                style="width: 150px;">
                                                    <option value="" <#if query.status??&&query.status==null>selected="true"</#if>>全部</option>
                                                    <option value="0" <#if query.status??&&query.status?string=='1'>selected='selected'</#if>>启用</option>
                                                    <option value="1" <#if query.status??&&query.status?string=='2'>selected='selected'</#if>>停用</option>
                                            </select>
                                            </span>
                                         </div>
                                     </div>
                               	 </td>
                               	 
                               </tr>
                               
                         	 
                            <tr>
                                <td colspan="8" align="center">
                                	<input type="reset" name="button" class="btn btn82 btn_res" value="重置">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                    <input type="button" name="button" id="templetListQuery" class="btn btn82 btn_search" value="查询">
                                </td>
                            </tr>

                            <!--虚线-->
                            <tr>
                                <td colspan="8">
                                    <hr style="border: 0.1px dashed #247DFF;" />
                                </td>
                            </tr>

							<tr>
                                <td >
                                    <input type="button" name="button" id="page_add" class="btn btn82 btn_add" value="创建">
                                </td>
                                <td >
                                    <input type="button" name="button" id="page_upd" class="btn btn82 btn_add" value="修改">
                                </td>
                                <td >
                                    <input type="button" name="button" id="page_del" class="btn btn82 btn_del" value="删除">
                                </td>
                            </tr>

                            <!--虚线-->
                            <tr>
                                <td colspan="8">
                                    <hr style="border: 0.1px dashed #247DFF;" />
                                </td>
                            </tr>
                            
                            <tr>
                                <td colspan="8">
                                    <table width="100%" cellspacing="0" cellpadding="0" border="0"
                                        class="list_table CRZ customertable" id="CRZ0">
                                        
                                        <thead>
											<tr>
												<th width="2%"><input type="checkbox" id="cust_check"></th>
												<th>页面名称</th>
                                                <th>页面编码</th>
                                                <th>状态</th>
                                                <th>顺序</th>
											</tr>
										</thead>
										<tbody>
											<#if (pageList?? && pageList.getRows()??)>
												<#list pageList.getRows() as page>
													<tr class="temp_tr_master">
														<td><input class="dev_ck" type="checkbox" name="pop_announce_check" /></td>
														<td class="td_center"><span class="pop_pageName">${page.pageName}</span></td>
														<td class="td_center"><span class="pop_pageCode">${page.pageCode}</span></td>
														<td class="td_center">
															<span class="pop_status">
																<#if (0 == page.status)>
																	启用
																<#elseif (1 == page.status)>
																	禁用
																</#if>
															</span>
														</td>
														<td class="td_center"><span class="pop_seq">${page.seq}</span></td>
														<td style="display:none;"><span class="pop_Id">${page.id}</span></td>
													</tr>
												</#list>
											</#if>
								
										</tbody>
                                    </table>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <#if (pageList??)>
							<div style="margin-top:10px;width:100%">
								<div style="float:right;padding-right:10px;">
									<a href="javascript:void(0);" id="nextPage">下一页</a>
								</div>
								<div style="float:right;padding-right:10px;">
									<a href="javascript:void(0);" id="prePage">上一页</a>
								</div>
								<div style="float:right;padding-right:10px;">
									总：<span>${pageList.total!}</span>
									每页：
									<#if ("20" == "${query.pageSize!}")>
										<select id="perCount" name="pageSize">
											<option value="10">10</option>
											<option value="20" selected>20</option>
											<option value="50">50</option>
											<option value="100">100</option>
											<option value="200">200</option>
										</select>
									<#elseif ("50" == "${query.pageSize!}")>
										<select id="perCount" name="pageSize">
											<option value="10">10</option>
											<option value="20">20</option>
											<option value="50" selected>50</option>
											<option value="100">100</option>
											<option value="200">200</option>
										</select>
									<#elseif ("100" == "${query.pageSize!}")>
										<select id="perCount" name="pageSize">
											<option value="10">10</option>
											<option value="20">20</option>
											<option value="50">50</option>
											<option value="100" selected>100</option>
											<option value="200">200</option>
										</select>
									<#elseif ("200" == "${query.pageSize!}")>
										<select id="perCount" name="pageSize">
											<option value="10">10</option>
											<option value="20">20</option>
											<option value="50">50</option>
											<option value="100">100</option>
											<option value="200" selected>200</option>
										</select>
									<#else>
										<select id="perCount" name="pageSize">
											<option value="10" selected>10</option>
											<option value="20">20</option>
											<option value="50">50</option>
											<option value="100">100</option>
											<option value="200">200</option>
										</select>
									</#if>
									<input type="hidden" id="pageNo" name="startPage" value="${query.startPage}" />
									<span id="currPage">${pageList.page!}</span>/<span id="totalPage">${pageList.total!}</span>页
								</div>
							</div>
						</#if>
                    
                    <!--@pagination value=page /-->
                </form>
            </div>
        </div>
    </div>
</div>

</@backend>
