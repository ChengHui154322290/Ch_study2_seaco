<#include "/common/common.ftl"/> 
<@backend title="促销人员管理" 
 js=[ '/statics/backend/js/layer/layer.min.js',
	  '/statics/backend/js/dss/promoterinfo.js'] 
css=[] >
<#if resultInfo.msg??>${resultInfo.msg.message}<#else>
<#assign promoterInfo = resultInfo.data>
<input type="hidden" name="parentPromoterId" value="${promoterInfo.promoterId}"/>
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr><td><h1>个人资料</h1></td></tr>
					<tr>
						<td>姓名</td>
						<td>${promoterInfo.promoterName}</td>
						<td>手机号</td>
						<td>${promoterInfo.mobile}</td>
					</tr>
					<tr>
						<td>开通时间</td>
						<td><#if promoterInfo.passTime??>${(promoterInfo.passTime)?string('yyyy-MM-dd HH:mm:ss')}</#if></td>
						<td>QQ号</td>
						<td>${promoterInfo.qq}</td>
					</tr>
					<tr>
						<td>店铺昵称</td>
						<td>${promoterInfo.nickName}</td>
						<td>出生日期</td>
						<td><#if promoterInfo.birthday??>${(promoterInfo.birthday)?string('yyyy-MM-dd')}</#if></td>
					</tr>
					
					<tr><td><h1>证件信息</h1></td></tr>
					<tr>
						<td>证件类型</td>
						<td>${promoterInfo.credentialTypeCn}</td>
					</tr>
					<tr>
						<td>证件号码</td>
						<td>${promoterInfo.credentialCode}</td>
					</tr>
					
					<tr><td><h1>银行卡信息</h1></td></tr>
					<tr>
						<td>开户银行</td>
						<td>${promoterInfo.bankName}</td>
					</tr>
					<tr>
						<td>银行卡号码</td>
						<td>${promoterInfo.bankAccount}</td>
					</tr>
					<tr>
						<td>支付宝帐户</td>
						<td>${promoterInfo.alipay}</td>
					</tr>
					<tr><td><h1>推广成绩</h1></td></tr>
					<tr>
						<td>未提现金额</td>
						<td>${promoterInfo.surplusAmount?string('0.00')}</td>
						<td>已提现金额</td>
						<td>${promoterInfo.withdrawAmount?string('0.00')}</td>
					</tr>
					<tr>
						<td>可提现金额</td>
						<td>${promoterInfo.surplusAmount?string('0.00')}</td>
						<td>卡券数量</td>
						<td>${promoterInfo.couponCount}</td>
					</tr>
					<tr>
						<td>新用户返现金额</td>
						<td>${promoterInfo.referralFees?string('0.00')}</td>
						<td>订单数量</td>
						<td>${promoterInfo.orderCount}</td>
					</tr>
					<tr><td><h1>码</h1></td></tr>
					<tr>
						<td>邀请码</td>
						<td>${promoterInfo.inviteCode}</td>
						<td>拉新佣金</td>
						<td>${promoterInfo.referralUnit}元</td>
					</tr>
					<tr>
						<td>返佣比例</td>
						<td>${promoterInfo.commisionRate}%</td>
						<td>邀请员类型</td>
						<td>${promoterInfo.promoterLevel}</td>
					</tr>
					<#if promoterInfo.promoterType==1>
					<tr>
						<td>店铺个性化</td>
						<td><#if promoterInfo.pageShow==0>不<#else>是</#if></td>
					</tr>					
					</#if>
					<#if promoterInfo.promoterType==2>
					<tr><td><h1>扫码推广二维码</h1></td></tr>					
					<tr>					
					<td class="td_center" ><img width='150' height='150' src="${promoterInfo.scanAttentionImage}" /></td>
					</tr>
					</#if>
				</table>
			</div>
		</div>
	</div>
</div>
</#if>
</@backend>
