<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="达人审核管理" 
    js=['/statics/cms/js/common.js',
		'/statics/backend/js/jquery.min.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/common/time/js/jquery-ui-1.9.2.custom.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-addon.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-zh-CN.js',
		'/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/cms/js/base/masterUserList.js'] 
    css=['/statics/backend/css/style.css',
    	'/statics/cms/js/common/time/css/jquery-ui-1.8.17.custom.css',
    	'/statics/cms/js/common/time/css/jquery-ui-timepicker-addon.css']>
	<div class="mt10" id="forms">
	    <div class="box">
	        <div class="box_border">
	            <div class="box_top">
	                <b class="pl15">CMS管理->达人管理->达人审核管理</b> 
	            </div>
	            <div class="box_center">
	                <form id="contract_list_form" class="jqtransform" method="get" action="${domain}/cms/masterUser/list.htm">
	                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
	                        class="form_table pt15 pb15">
	                        <tbody>
	                            <tr>
	                                <td colspan="1" class="td_right" width="50" align="right">手机号：</td>
	                                <td class="" width="50" align="left">
	                                	<input type="text" name="cmmMobile" class="input-text lh30 name" value="${query.cmmMobile}"/>
	                                </td>
									<td colspan="1" class="td_right" width="50" align="right">状态：</td>
	                                <td class="" width="50" align="left">
		                                <div class="select_border">
	                                        <div class="select_containers">
	                                            <span class="fl"> 
	                                            	<select class="select status" name="cmmExamineStatus" style="width: 130px;">
	                                                    <option value="" <#if query.cmmExamineStatus??&&query.cmmExamineStatus==null>selected="true"</#if>>全部</option>
	                                                    <option value="1" <#if query.cmmExamineStatus??&&query.cmmExamineStatus?string=='1'>selected='selected'</#if>>待审核</option>
	                                                    <option value="2" <#if query.cmmExamineStatus??&&query.cmmExamineStatus?string=='2'>selected='selected'</#if>>审核通过</option>
	                                                    <option value="3" <#if query.cmmExamineStatus??&&query.cmmExamineStatus?string=='3'>selected='selected'</#if>>审核不通过</option>
	                                                    <option value="4" <#if query.cmmExamineStatus??&&query.cmmExamineStatus?string=='4'>selected='selected'</#if>>审核终止</option>
	                                            	</select>
	                                            </span>
	                                         </div>
	                                     </div>
	                                </td>
	                         	 </tr>
	                         	 <tr>
	                                <td colspan="1" class="td_right" width="50" align="right">申请时间：</td>
	                                <td class="" colspan="3" align="left">
	                                	<input type="text" name="cmmApplyStartTimeValue" id="cmmApplyStartTime" value="${query.cmmApplyStartTimeValue}" class="input-text lh30 name" size="20"/>
	                                	<span style="margin:0 20px;">至</span>
	                                	<input type="text" name="cmmApplyEndTimeValue" id="cmmApplyEndTime" value="${query.cmmApplyEndTimeValue}" class="input-text lh30 name" size="20"/>
	                                </td>
	                                
	                                <td colspan="1" class="td_right" width="50" align="right">活动id：</td>
	                                <td class="" width="50" align="left">
	                                	<input type="text" name="cmmActivityId" class="input-text lh30 cmmActivityId" value="${query.cmmActivityId}"/>
	                                </td>
	                         	 </tr>
	                            <tr>
	                                <td colspan="8" align="center">
	                                	<input type="reset" name="button" class="btn btn82 btn_res" value="重置">
	                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
	                                    <input type="button" name="button" id="search" class="btn btn82 btn_search" value="查询">
	                                </td>
	                            </tr>
	                            <!--虚线-->
	                            <tr>
	                                <td colspan="8">
	                                    <hr style="border: 0.1px dashed #247DFF;" />
	                                </td>
	                            </tr>
	                        </tbody>
	                    </table>
	                    <table width="100%" cellspacing="0" cellpadding="0" border="0"
						    class="list_table CRZ customertable" id="CRZ0">
						    <thead>
								<tr>
									<th>手机号</th>
						            <th>用户名</th>
						            <th>申请宣言</th>
						            <th>申请时间</th>
						            <th>状态</th>
						            <th>活动id</th>
						            <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<#if (memberPage?? && memberPage.getList()??)>
									<#list memberPage.getList() as masterUser>
										<tr class="temp_tr_advertise">
											<td class="td_center">${masterUser.cmmMobile}</td>
											<td class="td_center">${masterUser.cmmUserName}</td>
											<td class="td_center">${masterUser.cmmApplyDeclarationShort}</td>
											<td class="td_center">${masterUser.cmmApplyDate?string("yyyy-MM-dd HH:mm:ss")}</td>
											<td class="td_center">
												<#if (2 == masterUser.cmmExamineStatus)>
													审核通过
												<#elseif (3 == masterUser.cmmExamineStatus)>
													审核不通过
												<#elseif (4 == masterUser.cmmExamineStatus)>
													审核终止
												<#else>
													待审核
												</#if>
											</td>
											<td class="td_center">${masterUser.cmmActivityId}</td>
											<td class="td_center">
												<#if (1 == masterUser.cmmExamineStatus)>
													<a href="javascript:void(0);" name="examine" param='${masterUser.id}'>[审核]</a>
												</#if>
												<#if (2 == masterUser.cmmExamineStatus)>
													<a href="javascript:void(0);" name="terminate" param='${masterUser.id}'>[终止]</a>
												</#if>
												<#if (1 != masterUser.cmmExamineStatus)>
													<a href="javascript:void(0);" name="view" param='${masterUser.id}'>[查看]</a>
												</#if>
											</td>
										</tr>
									</#list>
								</#if>
							</tbody>
						</table>
						<#if (memberPage??)>
							<div style="margin-top:10px;width:100%">
								<div style="float:right;padding-right:10px;">
									<a href="javascript:void(0);" id="nextPage">下一页</a>
								</div>
								<div style="float:right;padding-right:10px;">
									<a href="javascript:void(0);" id="prePage">上一页</a>
								</div>
								<div style="float:right;padding-right:10px;">
									总：<span>${memberPage.totalCount!}</span>
									每页：
									<#if ("50" == "${query.pageSize!}")>
										<select id="perCount" name="pageSize">
											<option value="20">20</option>
											<option value="50" selected>50</option>
											<option value="100">100</option>
											<option value="200">200</option>
										</select>
									<#elseif ("100" == "${query.pageSize!}")>
										<select id="perCount" name="pageSize">
											<option value="20">20</option>
											<option value="50">50</option>
											<option value="100" selected>100</option>
											<option value="200">200</option>
										</select>
									<#elseif ("200" == "${query.pageSize!}")>
										<select id="perCount" name="pageSize">
											<option value="20">20</option>
											<option value="50">50</option>
											<option value="100">100</option>
											<option value="200" selected>200</option>
										</select>
									<#else>
										<select id="perCount" name="pageSize">
											<option value="20" selected>20</option>
											<option value="50">50</option>
											<option value="100">100</option>
											<option value="200">200</option>
										</select>
									</#if>
									<input type="hidden" id="pageNo" name="startPage" value="${query.startPage}" />
									<span id="currPage">${memberPage.pageNo!}</span>/<span id="totalPage">${memberPage.totalPageCount!}</span>页
								</div>
							</div>
						</#if>
	                </form>
	            </div>
	        </div>
	    </div>
	</div>
</@backend>
