<!-- 商品信息 -->
<!-- 限购政策块 -->
<table id="topicPolicy" class="input tabContent">
	<tr>
		<td class="td_left" colspan="6">
			<h4>限购政策:</h4>
		</td>
	</tr>
	<tr>
		<td><input type="hidden" name="policy.id" id="policyId" value="${topicDetail.topic.limitPolicyId!}" /></td>
		<#if ("view" != "${mode}")>
			<td colspan="5">
				<div style="float:left;">
					<#if (1 == topicDetail.byRegisterTime)>
						<input type="checkbox" name="byRegisterTimeValue" id="registerTime" checked/>注册时间:
					<#else>
						<input type="checkbox" name="byRegisterTimeValue" id="registerTime" />注册时间:
					</#if>
				</div>
				<div style="float:left;margin-left:30px;">
					<#if (1 == topicDetail.byRegisterTime)>
						<#if (topicDetail.userRegisterStartTime??)>
							<input type="text" class="input-text lh30 dateInput" readonly="true" datafmt="yyyy-MM-dd" name="lateThanTime" id="userRegisterStartTime" value="${topicDetail.userRegisterStartTime!}" />
						<#else>
							<input type="text" class="input-text lh30 dateInput" readonly="true" datafmt="yyyy-MM-dd" name="lateThanTime" id="userRegisterStartTime" value="" />
						</#if>
						<span>&lt;&nbsp;用户注册时间&nbsp;&lt;</span>
						<#if (topicDetail.userRegisterEndTime??)>
							<input type="text" class="input-text lh30 dateInput" readonly="true" datafmt="yyyy-MM-dd" name="earlyThanTime" id="userRegisterEndTime" value="${topicDetail.userRegisterEndTime!}" />
						<#else>
							<input type="text" class="input-text lh30 dateInput" readonly="true" datafmt="yyyy-MM-dd" name="earlyThanTime" id="userRegisterEndTime" value="" />
						</#if>
					<#else>
						<input type="text" class="input-text lh30 dateInput" datafmt="yyyy-MM-dd" readonly="true" name="lateThanTime" id="userRegisterStartTime" value="" disabled="true"/>
						<span>&lt;&nbsp;用户注册时间&nbsp;&lt;</span>
						<input type="text" class="input-text lh30 dateInput" datafmt="yyyy-MM-dd" readonly="true" name="earlyThanTime" id="userRegisterEndTime" value="" disabled="true"/>
					</#if>
				</div>
			</td>
		<#else>
			<td colspan="5">
				<div style="float:left;">
					<#if (1 == topicDetail.byRegisterTime)>
						<input type="checkbox" name="byRegisterTimeValue" disabled="true" id="registerTime" checked/>注册时间:
					<#else>
						<input type="checkbox" name="byRegisterTimeValue" disabled="true" id="registerTime" />注册时间:
					</#if>
				</div>
				<div style="float:left;margin-left:30px;">
					<#if (1 == topicDetail.byRegisterTime)>
						<#if topicDetail.userRegisterStartTime??>
							<span>${topicDetail.userRegisterStartTime!}&lt;&nbsp;</span>
						</#if>
						<span>用户注册时间</span>
						<#if topicDetail.userRegisterEndTime??>
							<span>&nbsp;&lt;${topicDetail.userRegisterEndTime!}</span>
						</#if>
					<#else>
						<span></span>
						<span>无限制</span>
						<span></span>
					</#if>
				</div>
			</td>
		</#if>
	</tr>
	<tr>
		<td></td>
		<#if ("view" != "${mode}")>
			<td colspan="5">
				<#if ("1" == "${topicDetail.byUid!}")>
					<input type="checkbox" name="byUidValue" id="byUid" checked/>UID限制:
				<#else>
					<input type="checkbox" name="byUidValue" id="byUid" />UID限制:
				</#if>
				<span style="margin-left:30px;">本活动限制相同用户的购买</span>
			</td>
		<#else>
			<td colspan="5">
				<#if ("1" == "${topicDetail.byUid!}")>
					<input type="checkbox" name="byUidValue" disabled="true" id="uidLimit" checked/>UID限制:
				<#else>
					<input type="checkbox" name="byUidValue" disabled="true" id="uidLimit" />UID限制:
				</#if>
				<span style="margin-left:30px;">本活动限制相同用户的购买</span>
			</td>
		</#if>
	</tr>
	<tr>
		<td></td>
		<#if ("view" != "${mode}")>
			<td colspan="5">
				<#if ("1" == "${topicDetail.byIp!}")>
					<input type="checkbox" name="byIpValue" id="ipLimit" checked/>IP限制:
				<#else>
					<input type="checkbox" name="byIpValue" id="ipLimit" />IP限制:
				</#if>
				<span style="margin-left: 45px;">本活动限制相同IP的购买</span>
			</td>
		<#else>
			<td colspan="5">
				<#if ("1" == "${topicDetail.byIp!}")>
					<input type="checkbox" name="byIpValue" disabled="true" id="ipLimit" checked/>IP限制:
				<#else>
					<input type="checkbox" name="byIpValue" disabled="true" id="ipLimit" />IP限制:
				</#if>
				<span style="margin-left:45px;">本活动限制相同IP的购买</span>
			</td>
		</#if>
	</tr>
	<tr>
		<td></td>
		<#if ("view" != "${mode}")>
			<td colspan="5">
				<#if ("1" == "${topicDetail.byMobile!}")>
					<input type="checkbox" name="byMobileValue" id="phoneLimit" checked/>手机号码:
				<#else>
					<input type="checkbox" name="byMobileValue" id="phoneLimit" />手机号码:
				</#if>
				<span style="margin-left:30px;">本活动限制相同收货手机的购买</span>
			</td>
		<#else>
			<td colspan="5">
				<#if ("1" == "${topicDetail.byMobile!}")>
					<input type="checkbox" name="byMobileValue" disabled="true" id="phoneLimit" checked/>手机号码:
				<#else>
					<input type="checkbox" name="byMobileValue" disabled="true" id="phoneLimit" />手机号码:
				</#if>
				<span style="margin-left:30px;">本活动限制相同收货手机的购买</span>
			</td>
		</#if>
	</tr>
	
	<tr>
		<td></td>
		<#if ("view" != "${mode}")>
			<td colspan="5">
				<#if ("1" == "${topicDetail.byTopic!}")>
					<input type="checkbox" name="byTopic" id="topicLimit" checked/>活动限制:
				<#else>
					<input type="checkbox" name="byTopic" id="topicLimit" />活动限制:
				</#if>
				<span style="margin-left: 45px;">本活动同一用户只能购买一次商品</span>
			</td>
		<#else>
			<td colspan="5">
				<#if ("1" == "${topicDetail.byTopic!}")>
					<input type="checkbox" name="byTopic" disabled="true" id="topicLimit" checked/>活动限制:
				<#else>
					<input type="checkbox" name="byTopic" disabled="true" id="topicLimit" />活动限制:
				</#if>
				<span style="margin-left:45px;">本活动同一用户只能购买一次商品</span>
			</td>
		</#if>
	</tr>
</table>