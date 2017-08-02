<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="达人订单" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
        
        '/statics/cms/js/common/time/js/jquery-ui-1.9.2.custom.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-addon.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-zh-CN.js',
		
		'/statics/cms/js/jquery/jquery.form.js',
		'/statics/cms/js/masterplan/masterPlanOrderList.js',
		'/statics/cms/js/common/upload-cms-file.js'
       ] 
    css=['/statics/backend/css/style.css',
    		'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css',
    		'/statics/cms/js/common/time/css/jquery-ui-timepicker-addon.css',
    		'/statics/cms/js/common/time/css/jquery-ui-1.8.17.custom.css']>
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">CMS管理->首页管理->达人订单管理</b> 
            </div>
            <div class="box_center">
            	<form id="contract_list_form" class="jqtransform" method="get" action="${domain}/cms/queryMasterPlanOrderList.htm">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td colspan="1" class="td_right" width="50" align="right">活动编号：</td>
                                <td class="" width="50" align="left"><input type="text" value="${query.cmaNum}"
                                    name="cmaNum" class="input-text lh30 cmaNum" size="20"></td>

								<td colspan="1" class="td_right" width="50" align="right">活动名称：</td>
                                 <td class="" width="50" align="left"><input type="text" value="${query.cmaActivityName}"
                                    name="cmaActivityName" class="input-text lh30 cmaActivityName" size="20"></td>
                                
                                <td colspan="1" class="td_right" width="50" align="right">达人账号：</td>
                                 <td class="" width="50" align="left"><input type="text" value="${query.cmaAccount}"
                                    name="cmaAccount" class="input-text lh30 cmaAccount" size="20"></td>
                                
                                <td colspan="1" class="td_right" width="50" align="left">结算：</td>
                            	<td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> <select class="select cmaSettleStatus" name="cmaSettleStatus" 
                                                style="width: 150px;">
                                                    <option value="" <#if query.cmaSettleStatus??&&query.cmaSettleStatus==null>selected="true"</#if>>全部</option>
                                                    <option value="1" <#if query.cmaSettleStatus??&&query.cmaSettleStatus?string=='1'>selected='selected'</#if>>已结</option>
                                                    <option value="2" <#if query.cmaSettleStatus??&&query.cmaSettleStatus?string=='2'>selected='selected'</#if>>未结</option>
                                            </select>
                                            </span>
                                         </div>
                                     </div>
                               	 </td>
                               	 
                               </tr>
                               
                               <tr>
                               
                               	 <td colspan="1" class="td_right" width="50" align="right">订单日期：</td>
                                <td class="" width="50" align="left"><input type="text" value="${query.orderStartDate}"
                                    name="orderStartDate" class="input-text lh30 orderStartDate" size="20"></td>

								<td colspan="1" class="td_right" width="50" align="right">到</td>
                                 <td class="" width="50" align="left"><input type="text" value="${query.orderEndDate}"
                                    name="orderEndDate" class="input-text lh30 orderEndDate" size="20"></td>
                                    
                                 <td></td><td></td>
                               	 <td colspan="1" class="td_right" width="50" align="left">收益类型：</td>
                            	 <td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> <select class="select incomeType" name="incomeType" 
                                                style="width: 150px;">
                                                    <option value="" <#if query.incomeType??&&query.incomeType==null>selected="true"</#if>>全部</option>
                                                    <option value="1" <#if query.incomeType??&&query.incomeType?string=='1'>selected='selected'</#if>>现金</option>
                                                    <option value="2" <#if query.incomeType??&&query.incomeType?string=='2'>selected='selected'</#if>>海淘币</option>
                                            </select>
                                            </span>
                                         </div>
                                     </div>
                               	  </td>
                               	 
                         	 </tr>
                         	 
                            <tr>
                                <td colspan="8" align="center">
                                	<input type="reset" name="button" id="importSettleFile" class="btn btn82 btn_add" value="导入结算">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                    <input type="reset" name="button" id="exportSettle" class="btn btn82 btn_add" value="导出明细">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                	<input type="reset" name="button" class="btn btn82 btn_res" value="重置">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                    <input type="button" name="button" id="mastListQuery" class="btn btn82 btn_search" value="查询">
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
												<th>达人账号</th>
                                                <th>达人名称</th>
                                                <th>达人活动编号</th>
                                                <th>达人活动名称</th>
                                                <th>收益类型</th>
                                                <th>预计收益</th>
                                                <th>实际收益</th>
                                                <th>操作</th>
											</tr>
										</thead>
										<tbody>
											<#if (masterOderPage?? && masterOderPage.getList()??)>
												<#list masterOderPage.getList() as masterOder>
													<tr class="temp_tr_master">
														<td class="td_center"><span class="pop_cmoMasterAccount">${masterOder.cmoMasterAccount}</span></td>
														<td class="td_center"><span class="pop_cmoMasterName">${masterOder.cmoMasterName}</span></td>
														<td class="td_center"><span class="pop_cmoMastactCode">${masterOder.cmoMastactCode}</span></td>
														<td class="td_center"><span class="pop_cmoMastactName">${masterOder.cmoMastactName}</span></td>
														<td class="td_center">
															<#if (1 == masterOder.cmoRewardType)>
																订单数
															<#elseif (2 == masterOder.cmoRewardType)>
																用户数
															<#else>
																订单金额
															</#if>
														</td>
														<td class="td_center" style="display:none;">
															<span class="pop_cmoRewardType">${masterOder.cmoRewardType}</span>
														</td>
														<td class="td_center">${masterOder.cmoPredictPrice}</td>
														<td class="td_center">${masterOder.cmoActualPrice}</td>
														<td class="td_center">
															<a href="javascript:void(0);" name="detailsMaster">【查看】</a>
														</td>
													</tr>
												</#list>
											</#if>
								
											<!--tr class="temp_tr_master">
												<td class="td_center"><span class="pop_account"></span></td>
												<td class="td_center"><span class="pop_name"></span></td>
												<td class="td_center"><span class="pop_num"></span></td>
												<td class="td_center"><span class="pop_actname"></span></td>
												<td class="td_center"><span class="pop_income_type"></span></td>
												<td class="td_center"><span class="pop_predict_income"></span></td>
												<td class="td_center"><span class="pop_reality_income"></span></td>
                   								<td class="td_center">
                   									<a href="javascript:;" class="detailsMaster" param='${att.id}'>【查看】</a>
                   								</td>
											</tr-->
											
										</tbody>
                                    </table>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <#if (masterOderPage??)>
							<div style="margin-top:10px;width:100%">
								<div style="float:right;padding-right:10px;">
									<a href="javascript:void(0);" id="nextPage">下一页</a>
								</div>
								<div style="float:right;padding-right:10px;">
									<a href="javascript:void(0);" id="prePage">上一页</a>
								</div>
								<div style="float:right;padding-right:10px;">
									总：<span>${masterOderPage.totalCount!}</span>
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
									<span id="currPage">${masterOderPage.pageNo!}</span>/<span id="totalPage">${masterOderPage.totalPageCount!}</span>页
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
