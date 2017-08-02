<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="达人订单明细" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
        
        '/statics/cms/js/common/time/js/jquery-ui-1.9.2.custom.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-addon.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-zh-CN.js',
		
		'/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/cms/js/masterplan/masterPlanOrderEdList.js'
       ] 
    css=['/statics/backend/css/style.css',
    		'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css',
    		'/statics/cms/js/common/time/css/jquery-ui-timepicker-addon.css',
    		'/statics/cms/js/common/time/css/jquery-ui-1.8.17.custom.css']>
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">CMS管理->首页管理->达人订单明细管理</b> 
            </div>
            <div class="box_center">
                <form id="contract_list_form" class="jqtransform" action="">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td colspan="8">
                                    <table width="100%" cellspacing="0" cellpadding="0" border="0"
                                        class="list_table CRZ customertable" id="CRZ0">
                                        
                                        <thead>
											<tr>
												<th>序号</th>
												<th>订单编号</th>
                                                <th>订单金额</th>
                                                <th>订单日期</th>
                                                <th>订单状态</th>
                                                <th>会员ID</th>
                                                <th>预计收益</th>
                                                <th>实际收益</th>
                                                <th>备注</th>
											</tr>
										</thead>
										<tbody>
											<#if (masterOderPage??)>
													<#list masterOderPage as masterOder>
														<tr class="temp_tr_master">
															<td class="td_center">${masterOder_index+1}</td>
															<td class="td_center">${masterOder.cmoOrderCode}</td>
															<td class="td_center">${masterOder.cmoOrderPrice}</td>
															<td class="td_center">${masterOder.cmoOrderDate?string("yyyy-MM-dd HH:mm:ss")}</td>
															<td class="td_center">${masterOder.cmoOrderType}</td>
															<td class="td_center">${masterOder.cmoMasterAccount}</td>
															<td class="td_center">${masterOder.cmoPredictPrice}</td>
			                   								<td class="td_center">${masterOder.cmoActualPrice}</td>
			                   								<td class="td_center">${masterOder.remark}</td>
														</tr>
													</#list>
												</#if>
										</tbody>
										
                                    </table>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    
                <table id="topicAuditInfo" class="input tabContent">
					<tr>
						<td align="center">
							<input type="button" class="btn btn82 btn_del closebtn" width="120px" id="cancelMasterRule" value="取消" />
						</td>
					</tr>
				</table>
					
                    <!--@pagination value=page /-->
                </form>
            </div>
        </div>
    </div>
</div>

</@backend>
