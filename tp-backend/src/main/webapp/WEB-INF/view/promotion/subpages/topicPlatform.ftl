<!-- 平台 -->
<table id="topicPlatform" class="input tabContent">
	<tr>
		<td class="td_left" colspan="6">
			<h4>平台</h4>
		</td>
	</tr>
	<tr>
		<td class="td_right"></td>
		<td colspan="5">
			<#if ("view" != "${mode}")>
				<ul style="width:100%;list-style:none;">
						<#list  platformEnum as pf>
                        <li style="float:left;padding-right:20px;">
                            <label><input type="checkbox" name="platformCodes" id="" value="${pf.code}"
							<#list topicDetail.platformCodes as code >
								<#if code = pf.code>
									checked
								</#if>
							</#list>

							/> ${pf.name()} </label>
                        </li>
						</#list>
				</ul>
			<#else>
				<ul style="width:100%;list-style:none;">
					<#list  platformEnum as pf>
                        <li style="float:left;padding-right:20px;">
                            <label><input type="checkbox" name="platformCodes" id="" value="${pf.code}" readonly
								<#list topicDetail.platformCodes as code >
									<#if code = pf.code>
                                          checked
									</#if>
								</#list>

                            /> ${pf.name()} </label>
                        </li>
					</#list>
				</ul>
			</#if>
		</td>	
	</tr>
	<tr>
		<td class="td_left" colspan="6">
			<h4>分销渠道</h4>
		</td>
	</tr>
	<tr>
		<td></td>
		<td class="td_left" colspan="5">
			<#list  promoterInfoList as promoterInfo>
               <input type="checkbox" name="promoterIdList" id="" value="${promoterInfo.promoterId}" <#if promoterInfo.userAgreed>checked</#if> />${promoterInfo.promoterName}
			</#list>
		</td>
	</tr>
</table>