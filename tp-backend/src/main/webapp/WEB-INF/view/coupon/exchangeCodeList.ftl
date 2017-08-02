<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="兑换码活动列表管理" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
        
        '/statics/cms/js/common/time/js/jquery-ui-1.9.2.custom.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-addon.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-zh-CN.js',
		
		'/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/backend/js/promotion/promotionListExchangeCode.js'
       ] 
    css=['/statics/backend/css/style.css',
    	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css',
    	'/statics/cms/js/common/time/css/jquery-ui-timepicker-addon.css',
    	'/statics/cms/js/common/time/css/jquery-ui-1.8.17.custom.css'] >
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">优惠券管理->兑换码活动列表管理</b> 
            </div>
            <div class="box_center">
                <form id="contract_list_form" class="jqtransform" method="post" action="${domain}/topicCoupon/queryExchangeCodeList.htm">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td colspan="1" class="td_right" width="50" align="right">活动名称：</td>
                                <td class="" width="50" align="left"><input type="text" id="actNameBak"
                                    name="actName" value="${query.actName}" class="input-text lh30 actName" size="20"></td>
								
                         	 </tr>
                         	 
                            <tr>
                                <td colspan="8" align="center">
                                	<input type="reset" name="button" id="cancelAct" class="btn btn82 btn_res" value="重置">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                    <input type="button" name="button" id="advertiseListQuery" class="btn btn82 btn_search" value="查询">
                                </td>
                            </tr>

                            <!--虚线-->
                            <tr>
                                <td colspan="8">
                                    <hr style="border: 0.1px dashed #247DFF;" />
                                </td>
                            </tr>

                            <tr>
                                <td>
                                    <input type="button" name="advertise_list_add" id="advertise_list_add" class="btn btn82 btn_add" value="创建">
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
												<th>活动名称</th>
                                                <th>上线时间</th>
                                                <th>下线时间</th>
                                                <th>数量</th>
                                                <th>已兑换数量</th>
                                                <th>状态</th>
                                                <th>操作</th>
											</tr>
										</thead>
										<tbody>
											<#if (pageList?? && pageList.getRows()??)>
												<#list pageList.getRows() as page>
													<tr class="temp_tr_advertise">
														<td><input class="dev_ck" type="checkbox" name="pop_announce_check" /></td>
														<td class="td_center"><span class="pop_actName">${page.actName}</span></td>
														<td class="td_center"><span class="pop_startdate">${page.startDate?string("yyyy-MM-dd HH:mm:ss")} </span></td>
														<td class="td_center"><span class="pop_enddate">${page.endDate?string("yyyy-MM-dd HH:mm:ss")} </span></td>
														<td class="td_center"><span class="pop_num">${page.num}</span></td>
														<td class="td_center"><span class="pop_useNum">${page.useNum}</span></td>
														<td class="td_center"><span class="pop_status">
															<span class="pop_status">
																<#if (0 == page.status)>
																	正常
																<#elseif (1 == page.status)>
																	终止
																</#if>
															</span></td>
		                   								<td class="td_center"><a href="javascript:;" class="editAtt" param='${att.id}'>编辑</a>&nbsp;&nbsp;
		                   										<a href="javascript:;" class="stopAtt" param='${att.id}'>终止</a></td>
		                   								
		                   								<td style="display:none;"><span type="hidden" class="pop_Id" />${page.id}</td>
														</tr>
													</#list>
											</#if>
										</tbody>
                                            
                                    </table>
                                </td>
                            </tr>
                        </tbody>
                    </table>

					<@pager  pagination=pageList  formId="contract_list_form"  />

				</form>
            </div>
        </div>
    </div>
</div>

</@backend>
