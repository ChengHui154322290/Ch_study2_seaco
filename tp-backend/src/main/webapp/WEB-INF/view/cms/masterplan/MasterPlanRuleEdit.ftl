<#include "/common/common.ftl"/> 
<@backend title="编辑达人活动"
		  js=['/statics/cms/js/common.js',
	    	'/statics/backend/js/layer/layer.min.js',
	        '/statics/cms/js/common/hi-base.js',
	        '/statics/cms/js/common/hi-util.js',
	        '/statics/cms/js/layerly/layer.js',
	        
			'/statics/cms/js/common/time/js/jquery-ui-1.9.2.custom.js',
			'/statics/cms/js/common/time/js/jquery-ui-timepicker-addon.js',
			'/statics/cms/js/common/time/js/jquery-ui-timepicker-zh-CN.js',
			
	        '/statics/cms/js/jquery/jquery.json-2.4.min.js',
			
			'/statics/cms/js/jquery/jquery.ui.core.js',
			'/statics/cms/js/jquery/jquery.form.js',
		
			'/statics/cms/js/masterplan/masterPlanRuleEdit.js']
		css=['/statics/backend/css/style.css',
    		'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css',
    		'/statics/cms/js/common/time/css/jquery-ui-timepicker-addon.css',
    		'/statics/cms/js/common/time/css/jquery-ui-1.8.17.custom.css']>
<style type="text/css">
	.container{margin-top:30px}
	.table{text-align:center}
	.table .form-control{box-shadow:none;border:0;cursor:pointer}
</style>
		<form action="" method="POST" ID="submitMasterPlan">
		<input type="hidden" id="masterId" name="master.id" value="${masterDetail.master.id!}"/><!-- 主表id -->
		<input type="hidden" id="cmaActivityStatus" name="master.cmaActivityStatus" 
				value="${masterDetail.master.cmaActivityStatus!}"/><!-- 主表状态 -->
			<div class="box_border">
				<div class="box_top">
					<b class="pl15">达人活动规则详情</b>
				</div>
				<div class="box_center">
					<table id="topicTable" class="input tabContent">
						<!-- 基本信息块 -->
						<tr>
							<td class="td_right"><strong style="color:red;">*</strong>编号:</td>
							<td>${masterDetail.master.id!}</td>
							<td colspan="1"></td>
							
							<td class="td_right">状态:</td>
							
							<td>
								<#if (1 == masterDetail.master.cmaActivityStatus)>
									编辑中
								<#elseif (2 == masterDetail.master.cmaActivityStatus)>
									审核中
								<#elseif (3 == masterDetail.master.cmaActivityStatus)>
									已取消
								<#elseif (4 == masterDetail.master.cmaActivityStatus)>
									审核通过
								<#elseif (5 == masterDetail.master.cmaActivityStatus)>
									已驳回
								<#elseif (6 == masterDetail.master.cmaActivityStatus)>
									已终止
								<#else>
									编辑中
								</#if>
							</td>
							
						</tr>
						<tr>
							<td class="td_right">活动名称:</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 cmaActivityName" 
										name="topic.cmaActivityName" value="${masterDetail.master.cmaActivityName!}" />
								<#else>
									<span>${masterDetail.master.cmaActivityName!}</span>
								</#if>
							</td>
							<td colspan="1"></td>
							
							<td class="td_right">招募人数:</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 cmaPlanRecruit" 
										name="master.cmaPlanRecruit" value="${masterDetail.master.cmaPlanRecruit!}" />
								<#else>
									<span>${masterDetail.master.cmaPlanRecruit!}</span>
								</#if>
							</td>
							
						</tr>
						
						<tr>
							<td class="td_right"><strong style="color:red;">*</strong>招募时间:</td>
							<td>从：
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 cmaRecruitStartdate" 
										name="masterDetail.cmaRecruitStartdate" value="${masterDetail.cmaRecruitStartdateStr!}" />
								<#else>
									<span>${masterDetail.cmaRecruitStartdateStr!}</span>
								</#if>
							</td>
							<td>至：
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 cmaRecruitEnddate" 
										name="master.cmaRecruitEnddate" value="${masterDetail.cmaRecruitEnddateStr!}" />
								<#else>
									<span>${masterDetail.cmaRecruitEnddateStr!}</span>
								</#if>
							</td>
							
							<td class="td_right">优惠券批次号:</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 tmpCmaCouponId" style="width:50px;" 
										name="master.tmpCmaCouponId" value="${masterDetail.master.cmaCouponId!}" />
									<input type="text" class="input-text lh30 cmaCouponName" style="width:150px;" 
										name="master.cmaCouponName" readonly="readonly" value="${masterDetail.master.cmaCouponName!}" />
									<input type="hidden" class="cmaCouponId" value="${masterDetail.master.cmaCouponId!}"/>
									<input type="button" class="btn btn82 btn_save2" id="confirmBrand" value="确定" />
									<input type="button" class="btn btn82 btn_search" id="searchBrand" value="查询" />
								<#else>
									<span>${masterDetail.master.cmaCouponName!}</span>
								</#if>
							</td>
							<td colspan="2"></td>
							
						</tr>
						<tr>
							<td class="td_right"><strong style="color:red;">*</strong>活动时间:</td>
							<td>从：
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 cmaActivityStartdate" 
										name="master.cmaActivityStartdate"  value="${masterDetail.cmaActivityStartdateStr!}" />
								<#else>
									<span>${masterDetail.cmaActivityStartdateStr!}</span>
								</#if>
							</td>
							<td>至：
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 cmaActivityEnddate" 
										name="master.cmaActivityEnddate"  value="${masterDetail.cmaActivityEnddateStr!}" />
								<#else>
									<span>${masterDetail.cmaActivityEnddateStr!}</span>
								</#if>
							</td>
							
							<td class="td_right">备注:</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 cmaRemarks" 
										name="master.cmaRemarks" value="${masterDetail.master.cmaRemarks!}" />
								<#else>
									<span>${masterDetail.master.cmaRemarks!}</span>
								</#if>
							</td>
							
							
						</tr>
						
						<tr>
							<td class="td_right"><strong style="color:red;">*</strong>结算时间:</td>
							<td>从：
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 cmaSettleStartdate" 
										name="master.cmaSettleStartdate" value="${masterDetail.cmaSettleStartdateStr!}" />
								<#else>
									<span>${masterDetail.cmaSettleStartdateStr!}</span>
								</#if>
							</td>
							<td>至：
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 cmaSettleEnddate" 
										name="master.cmaSettleEnddate"  value="${masterDetail.cmaSettleEnddateStr!}" />
								<#else>
									<span>${masterDetail.cmaSettleEnddateStr!}</span>
								</#if>
							</td>
							
							<td class="td_right"></td>
							<td></td>
							
							
						</tr>
						
					</table>
					
					
					<!-- 达人奖励 -->
					<table id="masterRuleRward" class="input tabContent">
						<tr>
							<td class="td_left" colspan="8">
								<h4>达人奖励:</h4>
							</td>
						</tr>
						
						<#if (masterDetail.rewardrualList??)>
							<#list masterDetail.rewardrualList as reward>
								<tr class="tr_reward">
									<#if reward_index = 0>
										<td width="8%"><strong style="color:red;">*</strong>类型:</td>
										<td >
			                                <div class="select_border">
			                                    <div class="select_containers">
			                                        <span class="fl"> <select class="select cmrType" name="reward.cmrType" 
			                                            style="width: 80px;">
			                                                <option value="1"<#if "${reward.cmrType}"=="1"> selected</#if>>订单数</option>
			                                                <option value="2"<#if "${reward.cmrType}"=="2"> selected</#if>>用户数</option>
			                                                <option value="3"<#if "${reward.cmrType}"=="3"> selected</#if>>订单金额</option>
			                                        </select>
			                                        </span>
			                                     </div>
			                                 </div>
			                                 <input type="hidden" class="rewardId" name="reward.id" value="${reward.id!}"/>
			                           	 </td>
									</#if>
									
									<#if reward_index != 0>
										<td width="8%" colspan="2"><input type="hidden" class="rewardId" name="reward.id" value="${reward.id!}"/></td>
									</#if>
									
		                           	 
		                           	 <td>始：
										<#if ("view" != "${mode}")>
											<input type="text" class="input-text lh30 cmrThresholdStart" 
												name="reward.cmrThresholdStart" value="${reward.cmrThresholdStart!}" />
										<#else>
											<span>${reward.cmrThresholdStart!}</span>
										</#if>
									</td>
									
									<td>到：
										<#if ("view" != "${mode}")>
											<input type="text" class="input-text lh30 cmrThresholdEnd" 
												name="reward.cmrThresholdEnd" id="name" value="${reward.cmrThresholdEnd!}" />
										<#else>
											<span>${reward.cmrThresholdEnd!}</span>
										</#if>
									</td>
		                               	 
		                            <td width="8%"><strong style="color:red;">*</strong>奖励:</td>
		                            <td>
										<#if ("view" != "${mode}")>
											<input type="text" class="input-text lh30 cmrAmount" name="reward.cmrAmount"
												 value="${reward.cmrAmount!}" />
										<#else>
											<span>${reward.cmrAmount!}</span>
										</#if>
									</td>
									
									<td >
		                                <div class="select_border">
		                                    <div class="select_containers">
		                                        <span class="fl"> <select class="select cmrWay" name="reward.cmrWay" 
		                                            style="width: 80px;">
		                                                <option value="1"<#if "${reward.cmrWay}"=="1"> selected</#if>>现金</option>
		                                                <option value="2"<#if "${reward.cmrWay}"=="2"> selected</#if>>海淘币</option>
		                                        </select>/PCS
		                                        </span>
		                                 </div>
		                           	 </td>
		                           	 
		                           	 <#if reward_index = 0>
										<td class="td_center">
											<#if ("view" != "${mode}")>
												<a href="javascript:;" class="addReward" param='${att.id}' >添加</a>
												<a href="javascript:;" style="display:none" class="delReward" param='${att.id}' >删除</a>
											</#if>
										</td>
									</#if>
									
									<#if reward_index != 0>
										<td class="td_center">
											<#if ("view" != "${mode}")>
												<a href="javascript:;" class="addReward" param='${att.id}' >添加</a>
												<a href="javascript:;" class="delReward" param='${att.id}' >删除</a>
											</#if>
										</td>
									</#if>
									
								</tr>
							</#list>
						</#if>
						
						<#if !(masterDetail.rewardrualList??)>
							<tr class="tr_reward">
								<td width="8%"><strong style="color:red;">*</strong>类型:</td>
								<td >
	                                <div class="select_border">
	                                    <div class="select_containers">
	                                        <span class="fl"> <select class="select cmrType" name="reward.cmrType" 
	                                            style="width: 80px;">
	                                                <option value="1"<#if "${reward.cmrType}"=="1"> selected</#if>>订单数</option>
	                                                <option value="2"<#if "${reward.cmrType}"=="2"> selected</#if>>用户数</option>
	                                                <option value="3"<#if "${reward.cmrType}"=="3"> selected</#if>>订单金额</option>
	                                        </select>
	                                        </span>
	                                     </div>
	                                 </div>
	                                 <input type="hidden" class="rewardId" name="reward.id" value="${reward.id!}"/>
	                           	 </td>
	                           	 
	                           	 <td>始：
									<#if ("view" != "${mode}")>
										<input type="text" class="input-text lh30 cmrThresholdStart" 
											name="reward.cmrThresholdStart" value="${reward.cmrThresholdStart!}" />
									<#else>
										<span>${reward.cmrThresholdStart!}</span>
									</#if>
								</td>
								
								<td>到：
									<#if ("view" != "${mode}")>
										<input type="text" class="input-text lh30 cmrThresholdEnd" 
											name="reward.cmrThresholdEnd" id="name" value="${reward.cmrThresholdEnd!}" />
									<#else>
										<span>${reward.cmrThresholdEnd!}</span>
									</#if>
								</td>
	                               	 
	                            <td width="8%"><strong style="color:red;">*</strong>奖励:</td>
	                            <td>
									<#if ("view" != "${mode}")>
										<input type="text" class="input-text lh30 cmrAmount" name="reward.cmrAmount"
											 value="${reward.cmrAmount!}" />
									<#else>
										<span>${reward.cmrAmount!}</span>
									</#if>
								</td>
								
								<td >
	                                <div class="select_border">
	                                    <div class="select_containers">
	                                        <span class="fl"> <select class="select cmrWay" name="reward.cmrWay" 
	                                            style="width: 80px;">
	                                                <option value="1"<#if "${reward.cmrWay}"=="1"> selected</#if>>现金</option>
	                                                <option value="2"<#if "${reward.cmrWay}"=="2"> selected</#if>>海淘币</option>
	                                        </select>/PCS
	                                        </span>
	                                 </div>
	                           	 </td>
	                           	 
								<td class="td_center">
									<#if ("view" != "${mode}")>
										<a href="javascript:;" class="addReward" param='${att.id}' >添加</a>
										<a href="javascript:;" style="display:none" class="delReward" param='${att.id}' >删除</a>
									</#if>
								</td>
								
							</tr>
						</#if>
						
						<tr class="temp_tr_reward" style="display:none">
							<td width="8%" colspan="2"><input type="hidden" class="rewardId" name="reward.id" /></td>
                           	 <td>始：
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 cmrThresholdStart" name="reward.cmrThresholdStart" />
								<#else>
									<span>${masterDetail.reward.cmrThresholdStart!}</span>
								</#if>
							</td>
							<td>到：
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 cmrThresholdEnd" name="reward.cmrThresholdEnd" id="name"  />
								<#else>
									<span>${masterDetail.reward.cmrThresholdEnd!}</span>
								</#if>
							</td>
                            <td width="8%"><strong style="color:red;">*</strong>奖励:</td>
                            <td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 cmrAmount" name="reward.cmrAmount" id="name"  />
								<#else>
									<span>${masterDetail.reward.cmrAmount!}</span>
								</#if>
							</td>
							<td >
                                <div class="select_border">
                                    <div class="select_containers">
                                        <span class="fl"> <select class="select cmrWay" name="reward.cmrWay" 
                                            style="width: 80px;">
                                                <option value="1">现金</option>
                                                <option value="2">海淘币</option>
                                        </select>/PCS
                                        </span>
                                 </div>
                           	 </td>
                           	 <td class="td_center">
								<a href="javascript:;" class="addReward" param='${att.id}' >添加</a>
								<a href="javascript:;" class="delReward" param='${att.id}' >删除</a>
							</td>
						</tr>
						
					</table>
					<!-- 消费优惠 -->
					<table id="topicArea" class="input tabContent">
						<tr>
							<td class="td_left" colspan="8">
								<h4>消费优惠</h4>
							</td>
						</tr>
						<#if (masterDetail.consulerualList??)>
							<#list masterDetail.consulerualList as consulerual>
								<tr class="tr_consule">
									<td width="8%"><strong style="color:red;">*</strong>类型:</td>
									<td >
		                                <div class="select_border">
		                                    <div class="select_containers">
		                                        <span class="fl"> <select class="select cmcType" name="consuler.cmcType" 
		                                            style="width: 80px;">
		                                                <option value="1" <#if "${consulerual.cmcType}"=="1"> selected</#if>>订单抵扣</option>
		                                        </select>
		                                        </span>
		                                     </div>
		                                 </div>
		                                 <input type="hidden" class="consulerualId" name="consulerual.id" value="${consulerual.id!}"/>
		                           	 </td>
		                           	 
		                           	 <td>
										<!--<#if ("view" != "${mode}")>
											<input type="text" class="input-text lh30 cmcAmount" name="consuler.cmcAmount" 
												value="${consulerual.cmcAmount!}" />
										<#else>
											<span>${consulerual.cmcAmount!}</span>
										</#if>元，-->
										<#if ("view" != "${mode}")>
											<input type="text" class="input-text lh30 cmcNum" name="consuler.cmcNum" 
												value="${consulerual.cmcNum!}" />
										<#else>
											<span>${consulerual.cmcNum!}</span>
										</#if>次
									</td>
									
								</tr>
							</#list>
						</#if>
						
						<#if !(masterDetail.consulerualList??)>
							<tr class="tr_consule">
								<td width="8%"><strong style="color:red;">*</strong>类型:</td>
								<td >
	                                <div class="select_border">
	                                    <div class="select_containers">
	                                        <span class="fl"> <select class="select cmcType" name="consuler.cmcType" 
	                                            style="width: 80px;">
	                                                <option value="1" <#if "${consulerual.cmcType}"=="1"> selected</#if>>订单抵扣</option>
	                                        </select>
	                                        </span>
	                                     </div>
	                                 </div>
	                                 <input type="hidden" class="consulerualId" name="consulerual.id" value="${consulerual.id!}"/>
	                           	 </td>
	                           	 
	                           	 <td>
									<!--<#if ("view" != "${mode}")>
										<input type="text" class="input-text lh30 cmcAmount" name="consuler.cmcAmount" 
											value="${consulerual.cmcAmount!}" />
									<#else>
										<span>${consulerual.cmcAmount!}</span>
									</#if>元，-->
									<#if ("view" != "${mode}")>
										<input type="text" class="input-text lh30 cmcNum" name="consuler.cmcNum" 
											value="${consulerual.cmcNum!}" />
									<#else>
										<span>${consulerual.cmcNum!}</span>
									</#if>次
								</td>
								
							</tr>
						</#if>
						
					</table>
					
					<!-- 达人标示 -->
					<table id="topicArea" class="input tabContent">
						<tr>
							<td class="td_left" colspan="8">
								<h4>达人标示</h4>
							</td>
						</tr>
						<tr>
							<td width="8%"><strong style="color:red;">*</strong>类型:</td>
							<td >
                                <div class="select_border">
                                    <div class="select_containers">
                                        <span class="fl"> <select class="select cmaIdent" name="masterDetail.cmaIdent" 
                                            style="width: 80px;">
                                                <option value="1" <#if "${masterDetail.master.cmaIdent}"=="1"> selected</#if>>手机号码</option>
                                        </select>
                                        </span>
                                     </div>
                                 </div>
                           	 </td>
                           	 
						</tr>
					</table>
					
					<table id="topicAuditInfo" class="input tabContent">
						<tr>
							<td align="center">
								<#if ("view" != "${mode}")>
									<input type="button" class="btn btn82 btn_save2" id="saveMasterRule" value="保存" />
									<input type="button" class="btn btn82 btn_submit" width="120px" id="submMasterRule" value="提交" />
								</#if>
								<input type="button" class="btn btn82 btn_del closebtn" width="120px" id="cancelMasterRule" value="取消" />
							</td>
						</tr>
					</table>
					
					<table id="topicRecord" class="input tabContent">
						<tr>
							<td class="td_left">
								<h4>审核记录</h4>
							</td>
						</tr>
						<tr>
							<td>
								<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="topicAuditLogsList">
									<thead>
										<tr>
											<th>姓名</td>
											<th>操作</th>
											<th>意见</th>
											<th>时间</th>
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
					</table>
					
						
				</div>
			</div>
		</form>
</@backend>