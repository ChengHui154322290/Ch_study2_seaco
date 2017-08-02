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
		'/statics/cms/js/templet/templetList.js'
       ] 
    css=['/statics/backend/css/style.css',
    		'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css']>
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">CMS管理->模板管理</b> 
            </div>
            <div class="box_center">
            	<form id="contract_list_form" class="jqtransform" method="post" action="${domain}/cms/queryTempleList.htm">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
								<td colspan="1" class="td_right" width="50" align="right">页面名称：</td>
                                 <td class="" width="50" align="left"><input type="text" value="${query.pageName}"
                                    name="pageName" class="input-text lh30 pageName" size="20"></td>
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
                                    <input type="button" name="button" id="templet_add" class="btn btn82 btn_add" value="创建">
                                </td>
                                <td >
                                    <input type="button" name="button" id="templet_upd" class="btn btn82 btn_add" value="修改">
                                </td>
                                <td >
                                    <input type="button" name="button" id="templet_del" class="btn btn82 btn_del" value="删除">
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
                                                <th>模块名称</th>
                                                <th>模块编码</th>
                                                <th>状态</th>
                                                <th>顺序</th>
                                                <th>元素类型</th>
                                                <th>元素数量</th>
											</tr>
										</thead>
										<tbody>
											<#if (pageList?? && pageList.getRows()??)>
												<#list pageList.getRows() as page>
													<tr class="temp_tr_master">
														<td><input class="dev_ck" type="checkbox" name="pop_announce_check" /></td>
														<td class="td_center"><span class="pop_pageName">${page.pageName}</span></td>
														<td class="td_center"><span class="pop_templeName">${page.templeName}</span></td>
														<td class="td_center"><span class="pop_templeCode">${page.templeCode}</span></td>
														<td class="td_center">
															<span class="pop_status">
																<#if (0 == page.status)>
																	启用
																<#elseif (1 == page.status)>
																	停用
																</#if>
															</span>
														</td>
														<td class="td_center"><span class="pop_seq">${page.seq}</span></td>
														<td class="td_center"><span class="pop_elementType">
																<#if (1 == page.elementType)>
																	活动
																<#elseif (2 == page.elementType)>
																	图片
																<#elseif (3 == page.elementType)>
																	文字
																<#elseif (4 == page.elementType)>
																	自定义编辑
																<#elseif (5 == page.elementType)>
																	SEO
																<#elseif (6 == page.elementType)>
																	公告
																<#elseif (7 == page.elementType)>
																	资讯
																</#if>
														</span></td>
														<td class="td_center"><span class="pop_elementNum">${page.elementNum}</span></td>
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
