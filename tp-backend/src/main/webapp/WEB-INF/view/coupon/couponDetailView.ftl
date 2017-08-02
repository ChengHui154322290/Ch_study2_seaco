<#include "/common/common.ftl"/> 
<@backend title="查看明细" 
			js=['/statics/backend/js/coupon/couponDetail.js']
			css=[]>
	<div class="box_border">
		<div class="box_center">
			<table cellspacing="0" cellpadding="0" border="0" width="100%"
	            class="form_table pt15 pb15" id="topicTable">
				<tr>
					<td class="td_right">
						<span style="margin-right:20px;">优惠券:</span>
					</td>
					<td align="left">
						<span style="margin-right:20px;">
							${couponUserInfo.couponId}
						</span>
						<span style="margin-right:20px;">
							${couponUserInfo.couponName}
						</span>
					</td>
					<td class="td_right">
						<span style="margin-right:20px;">状态:</span>
					</td>
					<td align="left">
						<#if (0 == couponUserInfo.couponUserStatus)>
							<span>有效</span>
						<#elseif (1 == couponUserInfo.couponUserStatus)>
							<span>已使用</span>
						<#elseif (2 == couponUserInfo.couponUserStatus)>
							<span>已过期</span>
						<#elseif (3 == couponUserInfo.couponUserStatus)>
							<span>作废</span>
						<#elseif (4 == couponUserInfo.couponUserStatus)>
							<span>删除</span>
						</#if>
					</td>
				</tr>
				<tr>
					<td class="td_right">
						<span style="margin-right:20px;">有效期:</span>
					</td>
					<td align="left">
						<span style="margin-right:20px;"><#if (couponUserInfo.couponUseStime??)>${couponUserInfo.couponUseStime?string("yyyy-MM-dd HH:mm:ss")!}</#if></span>
						<span style="margin-right:20px;">至</span>
						<span style="margin-right:20px;"><#if (couponUserInfo.couponUseEtime??)>${couponUserInfo.couponUseEtime?string("yyyy-MM-dd HH:mm:ss")!}</#if></span>
					</td>
					<td colspan="2">
					</td>
				</tr>
				<#if (couponUserInfo.sourceFrom??)>
					<tr>
						<td class="td_right">
							<span style="margin-right:20px;">来源类型:</span>
						</td>
						<td align="left">
							<span style="margin-right:20px;">
								${couponUserInfo.sourceFrom}
							</span>
						</td>
						<td colspan="2">
						</td>
					</tr>
				</#if>
				<#if (couponUserInfo.sourceName??)>
					<tr>
						<td class="td_right">
							<span style="margin-right:20px;">来源编号:</span>
						</td>
						<td align="left">
							<span style="margin-right:20px;">
								${couponUserInfo.sourceName}
							</span>
						</td>
						<td colspan="2">
						</td>
					</tr>
				</#if>
				<#if (couponUserInfo.refCode??)>
					<tr>
						<td class="td_right">
							<span style="margin-right:20px;">参考编号:</span>
						</td>
						<td align="left">
							<span style="margin-right:20px;">
								${couponUserInfo.refCode}
							</span>
						</td>
						<td colspan="2">
						</td>
					</tr>
				</#if>
				<tr>
					<td class="td_right">
						<span style="margin-right:20px;">适用范围:</span>
					</td>
					<td align="left" colspan="3">
						<#if (couponUserInfo.allRange)>
							<span style="margin-right:20px;">全网</span>
						</#if>
						<#if (couponUserInfo.selfRange)>
							<span style="margin-right:20px;">自营+海淘</span>
						</#if>
						<#if (couponUserInfo.jointRange)>
							<span style="margin-right:20px;">联营</span>
						</#if>
					</td>
				</tr>
				<tr>
					<td class="td_right">
						<span style="margin-right:20px;">适用平台:</span>
					</td>
					<td align="left" colspan="3">
						<#if (couponUserInfo.allPlat)>
							<span style="margin-right:20px;">全网</span>
						</#if>
						<#if (couponUserInfo.pcPlat)>
							<span style="margin-right:20px;">PC</span>
						</#if>
						<#if (couponUserInfo.appPlat)>
							<span style="margin-right:20px;">APP</span>
						</#if>
						<#if (couponUserInfo.wapPlat)>
							<span style="margin-right:20px;">WAP</span>
						</#if>
						<#if (couponUserInfo.happergPlat)>
							<span style="margin-right:20px;">快乐孕期</span>
						</#if>
					</td>
				</tr>
				<tr>
					<td class="td_right">
						<span style="margin-right:20px;">发放人:</span>
					</td>
					<td align="left" colspan="3">
						<span>${couponUserInfo.sendEmp}</span>
					</td>
				</tr>
				<tr>
					<td class="td_right">
						<span style="margin-right:20px;">发放日期:</span>
					</td>
					<td align="left" colspan="3">
						<span>${couponUserInfo.sendTime?string("yyyy-MM-dd HH:mm:ss")!}</span>
					</td>
				</tr>
				<#if (subOrders??)>
					<tr>
						<td style="text-align:left" colspan="4">
							<span style="margin-right:20px;">订单详情:</span>
						</td>
					</tr>
					<tr>
						<td class="td_right" colspan="4">
							<table id="topicTable" cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
								<tr>
									<th style="width:30%;text-align:center">订单号</th>
									<th style="width:30%;text-align:center">使用金额</th>
									<th style="width:30%;text-align:center">订单金额</th>
								</tr>
								<#list subOrders as order>
									<tr>
										<td class="td_right" style="width:30%;text-align:center"><span>${order.code!}</span></td>
										<td class="td_right" style="width:30%;text-align:center"><span><#if (order.discount??)>${order.discount?string('#.##')}</#if></span></td>
										<td class="td_right" style="width:30%;text-align:center"><span><#if (order.payTotal??)>${order.payTotal?string('#.##')}</#if></span></td>
									</tr>
								</#list>
							</table>
						</td>
					</tr>
				</#if>
				<tr>
					<td colspan="4" align="center">
						<input type="button" class="btn btn82 btn_del closebtn" id="cancel" value="关闭" />
					</td>
				</tr>
			</table>
		</div>
	</div>
</@backend>