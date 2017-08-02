<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="新增兑换码" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
        
		'/statics/cms/js/common/time/js/jquery-ui-1.9.2.custom.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-addon.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-zh-CN.js',
		
        '/statics/cms/js/jquery/jquery.json-2.4.min.js',
        
		'/statics/backend/js/swfupload/swfupload.js',
		'/statics/backend/js/swfupload/js/fileprogress.js',
		'/statics/backend/js/swfupload/js/handlers.js',
		'/statics/backend/js/swfupload/js/swfupload.queue.js',
		'/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
		'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',

		'/statics/backend/js/promotion/promotionAddExchangeCode.js',
		'/statics/backend/js/dss/promotercoupon.js',
		'/statics/cms/js/jquery/jquery.ui.core.js',
		'/statics/cms/js/jquery/jquery.form.js'
       ] 
    css=['/statics/backend/css/style.css',
    	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css',
    	'/statics/cms/js/common/time/css/jquery-ui-timepicker-addon.css',
    	'/statics/cms/js/common/time/css/jquery-ui-1.8.17.custom.css'] >
<style type="text/css">
	.container{margin-top:30px}
	.table{text-align:center}
	.table .form-control{box-shadow:none;border:0;cursor:pointer}
</style>
<input type="hidden" value="${sessionId}" id="sessionId" />
<form id="inputForm" action="" method="post" enctype="multipart/form-data">	

	<input type="hidden" id="actNameBak" 	value="${actNameBak}" >

	<div class="mt10" id="forms">
	    <div class="box">
	        <div class="box_border">
	            <div class="box_top">
	                <b class="pl15">促销管理->新增兑换码页面</b> 
	            </div>
	            <div class="box_center">
	                
	                <div class="box_border">
						<table class="input tabContent">
							<tr>
								<td class="td_right" ><span class="requiredField">*</span>活动名称：</td>
								<td >
									<input type="text" name="actName" maxlength="50" value="${detail.actName}" 
										size="50" class="input-text lh30 actName" >
									<input type="hidden" name="actId" value="${detail.id}"  class="actId">
								</td>
								<td >
								</td>
								<td >
								</td>
								
							</tr>
							
							<tr>
								<td class="td_right"><span class="requiredField">*</span>活动开始日期:</td>
								<td class="">
									<input type="text" readonly="readonly" name="startdate"  <#if detail.startDate??> value="${detail.startDate?string("yyyy-MM-dd HH:mm:ss")}" </#if>
									class="input-text lh30 startdate" size="20">
								</td>
								
								<td class="td_right"><span class="requiredField">*</span>活动结束日期:</td>
								<td class="">
									<input type="text" readonly="readonly" name="enddate" <#if detail.endDate??>value="${detail.endDate?string("yyyy-MM-dd HH:mm:ss")}" </#if>
                                           class="input-text lh30 enddate" size="20">
								</td>
							</tr>
							
							<tr>
								<td class="td_right" style="display: none"><span class="requiredField">*</span>活动平台:</td>
								<td class="" style="display: none">
									<#if (1 == detail.channel)>
										<input type="radio" name="channel" value="1" checked="checked" />百度
									<#else>
										<input type="radio" name="channel" value="1" checked="checked"/>百度
									</#if>
								</td>
								
								<td class="td_right" style="display: none"><span class="requiredField">*</span>类型:</td>
								<td class="" style="display: none">
									<#if (1 == detail.type)>
										<input type="radio" name="type" value="1" checked="checked"/>宝盒
									<#else>
										<input type="radio" name="type" value="1" checked="checked"/>宝盒
									</#if>
								</td>
							</tr>
							
							<tr>
								<td class="td_right"><span class="requiredField">*</span>兑换码总数:</td>
								<td class="">
									<input type="text" name="num" maxlength="50" value="${detail.num}" 
										size="50" class="input-text lh30 num">
								</td>
								
								<td class="td_right"><span class="requiredField">*</span>已兑换数量：</td>
								<td class="">
									<#if (detail.useNum??)>
										<input type="text" name="useNum" maxlength="50" value="${detail.useNum}" 
											readonly="readonly" size="50" class="input-text lh30 useNum">
									<#else>
										<input type="text" name="useNum" maxlength="50" value="0" 
											readonly="readonly" size="50" class="input-text lh30 useNum">
									</#if>
									
								</td>
								
							</tr>
							
							<tr>
								<td class="td_right"><span class="requiredField">*</span>优惠券名称:</td>
								<td class="">
									<input type="text" class="input-text lh30 couponname ui-autocomplete-input" size="40" autocomplete="off">
									<input type="hidden" name="couponId"/>
								</td>
								
								<td class="td_right"><span class="requiredField">*</span>本次兑换数量：</td>
								<td class="">
									<input type="text" name="currentnum" maxlength="50" 
										size="50" class="input-text lh30 currentnum">
								</td>
								
							</tr>
							
						</table>
					</div>
					
					<div class="tc">
						<#if (detail.id??)>
							<input type="button" id="genertExchangeCode" value="生成兑换码" class="ext_btn ext_btn_submit m10">
							<input type="button" id="exportData"  value="导出兑换码" class="ext_btn m10 codeExport">
						</#if>
					</div>
					
					<!--table id="topicRecord" class="input tabContent">
						<tr>
							<td class="td_left">
								<h4>兑换码记录</h4>
							</td>
						</tr>
						<tr>
							<td>
								<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="topicAuditLogsList">
									<thead>
										<tr>
											<th>时间</td>
											<th>操作</th>
											<th>操作人</th>
											<th>日志</th>
										</tr>
									</thead>
									<#if (masterDetail.examineList??)>
										<tbody>
											<#list masterDetail.examineList as auditLog>
												<tr align="center" style="background-color: rgb(255, 255, 255);">
													<td>
														<span>${auditLog.cmeUserName!}</span>
													</td>
													<td>
														<span>${auditLog.cmeResult!}</span>
													</td>
													<td>
														<span>${auditLog.cmeSuggestion!}</span>
													</td>
													<td>
														<#if (auditLog.cmeDate??)>
															<span>${auditLog.cmeDate?string("yyyy-MM-dd HH:mm:ss")!}</span>
														</#if>
													</td>
												</tr>
											</#list>
										</tbody>
									</#if>
								</table>
							</td>
						</tr>
					</table-->
					
					<!--虚线-->
                   <div class="tc">
                       <hr style="border: 0.1px dashed #247DFF;" />
                   </div>
                            
					<div class="tc">
							<input type="button" id="sumbitExchangeCode" value="提交" class="ext_btn ext_btn_submit m10">
							<input type="button" id="returnPage" value="返回" class="ext_btn m10">
					</div>
					<div id="exchangeDetail"></div>

	           </div>
	        </div>
	    </div>
	</div>
</form>
</@backend>
