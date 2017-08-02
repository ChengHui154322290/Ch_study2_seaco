<#include "/promotion/subpages/topicVariable.ftl"/>
<input type="hidden" name="topic.changeTopicId" id="changeTopicId" value="${topicDetail.topic.changeTopicId!}" />
<input type="hidden" name="topic.brandId" id="brandId" value="${topicDetail.topic.brandId!}" />
<input type="hidden" name="topic.brandName" id="brandName" value="${topicDetail.topic.brandName!}" />
<input type="hidden" name="topic.supplierId" id="supplierId" value="${topicDetail.topic.supplierId!}" />
<input type="hidden" name="topic.supplierName" id="supplierName" value="${topicDetail.topic.supplierName!}" />
<table id="topicTable" class="input tabContent">
    <input type="hidden" id="bucketname" name="bucketname" value="${bucketname}">
    <input type="hidden" id="bucketURL" name="bucketURL" value="${bucketURL}">
	<#if (changeTopicId)??>
		<tr>
			<td class="td_right">
				活动序号
			</td>
			<td colspan="5">
				${changeTopicId!}
			</td>
		</tr>
	</#if>
	<tr>
		<td class="td_left" colspan="6">
			<h4>基本信息</h4>
		</td>
	</tr>
	<!-- 基本信息块 -->
	<tr>
		<td class="td_right"><strong style="color:red;">*</strong>专场类型:</td>
		<td>
			<#if (2 == topicDetail.topic.type)>
				品牌团
			<#elseif (3 == topicDetail.topic.type)>
				主题团
			<#elseif (5 == topicDetail.topic.type)>
				商家店铺
			<#elseif (1 == topicDetail.topic.type)>
				单品团
			</#if>
		</td>
		<td class="td_right">专场时间:</td>
		<td>
			<#if ("view" != "${mode}")>
				<#if (1 == topicDetail.topic.lastingType)>
					<input type="radio" name="topic.lastingType" value="0"/>长期在线
					<input type="radio" name="topic.lastingType" value="1" checked="checked" />固定期限
				<#else>
					<input type="radio" name="topic.lastingType" value="0" checked="checked"/>长期在线
					<input type="radio" name="topic.lastingType" value="1" />固定期限
				</#if>
			<#else>
				<#if (1 == topicDetail.topic.lastingType)>
					固定期限
				<#else>
					长期在线
				</#if>
			</#if>
		</td>
		<td class="td_right">变更单状态:</td>
		<td>
			<#if (1 == topicDetail.topic.status)>
				审核中
			<#elseif (2 == topicDetail.topic.status)>
				已取消
			<#elseif (3 == topicDetail.topic.status)>
				审核通过
			<#elseif (4 == topicDetail.topic.status)>
				已驳回
			<#elseif (5 == topicDetail.topic.status)>
				终止
			<#else>
				编辑中
			</#if>
		</td>
	</tr>
	</tr>
		<td class="td_right">专场编号:</td>
		<td>
			<#if ("view" != "${mode}")>
				<input type="text" class="input-text lh30 topicInteger" name="topic.number" id="number" value="${topicDetail.topic.number!}" />
			<#else>
				<span>${topicDetail.topic.number!}</span>
			</#if>
		</td>
		<td class="td_right"><strong style="color:red;">*</strong>开始时间:</td>
		<td>
			<#if ("view" != "${mode}")>
				<input type="text" class="input-text lh30 dateInput" readonly="true" name="startTime" datafmt="yyyy-MM-dd HH:mm:ss" id="startTime" value="${topicDetail.startTime!}" />
			<#else>
				<span>${topicDetail.startTime!}</span>
			</#if>
		</td>
		<td class="td_right">支持商户提报:</td>
		<td>
			<#if ("view" != "${mode}")>
				<#if (0 == topicDetail.topic.isSupportSupplier)>
					<input type="radio" name="topic.isSupportSupplier" value="0" checked="checked" />支持
					<input type="radio" name="topic.isSupportSupplier" value="1" />不支持
				<#else>
					<input type="radio" name="topic.isSupportSupplier" value="0" />支持
					<input type="radio" name="topic.isSupportSupplier" value="1" checked="checked" />不支持
				</#if>
			<#else>
				<#if (0 == topicDetail.topic.isSupportSupplier)>
					支持
				<#else>
					不支持
				</#if>
			</#if>
		</td>
	</tr>
	<tr>
		<td class="td_right"><strong style="color:red;">*</strong>专场名称:</td>
		<td>
			<#if ("view" != "${mode}")>
				<input type="text" class="input-text lh30" name="topic.name" id="name" value="${topicDetail.topic.name!}" />
			<#else>
				<span>${topicDetail.topic.name!}</span>
			</#if>
		</td>
		<td class="td_right"><strong style="color:red;">*</strong>结束时间:</td>
		<td>
			<#if ("view" != "${mode}")>
				<input type="text" class="input-text lh30 dateInput" readonly="true" name="endTime" datafmt="yyyy-MM-dd HH:mm:ss" id="endTime" value="${topicDetail.endTime!}" />
			<#else>
				<span>${topicDetail.endTime!}</span>
			</#if>
		</td>
		<td class="td_right">折扣:</td>
		<td>
			<#if ("view" != "${mode}")>
				<input type="text" class="input-text lh30" name="topic.discount" id="discount" value="${topicDetail.topic.discount!}" />
			<#else>
				<span>${topicDetail.topic.discount!}</span>
			</#if>
		</td>
		
	</tr>
	<tr>
		<td class="td_right">活动卖点:</td>
		<td>
			<#if ("view" != "${mode}")>
				<input type="text" class="input-text lh30" style="width:100%;" name="topic.topicPoint" id="topicPoint" value="${topicDetail.topic.topicPoint!}" />
			<#else>
				<span>${topicDetail.topic.topicPoint!}</span>
			</#if>
		</td>
		<td class="td_right"> <#if topicDetail.topic.type==2>品牌: <#elseif  topicDetail.topic.type==5>商家   </#if> </td>
		<td>
			<span> <#if topicDetail.topic.type==2>  ${topicDetail.topic.brandName!}<#elseif  topicDetail.topic.type==5> ${topicDetail.topic.supplierName!}  </#if></span>
		</td>
		<td class="td_right">销售类型:</td>
		<td>
			<#if ("view" != "${mode}")>
				<select class="select" id="salesPartten"
                    style="width: 130px;">
                    <option value="1" <#if topicDetail.topic.salesPartten??&&topicDetail.topic.salesPartten?string=='1'>selected='selected'</#if>>不限</option> 
                    <option value="2" <#if topicDetail.topic.salesPartten??&&topicDetail.topic.salesPartten?string=='2'>selected='selected'</#if>>旗舰店</option> 
                    <option value="3" <#if topicDetail.topic.salesPartten??&&topicDetail.topic.salesPartten?string=='3'>selected='selected'</#if>>商城</option>
                    <option value="4" <#if topicDetail.topic.salesPartten??&&topicDetail.topic.salesPartten?string=='4'>selected='selected'</#if>>洋淘派</option> 
                    <option value="5" <#if topicDetail.topic.salesPartten??&&topicDetail.topic.salesPartten?string=='5'>selected='selected'</#if>>闪购</option> 
                    <option value="6" <#if topicDetail.topic.salesPartten??&&topicDetail.topic.salesPartten?string=='6'>selected='selected'</#if>>秒杀</option> 
                    <option value="8" <#if topicDetail.topic.salesPartten??&&topicDetail.topic.salesPartten?string=='8'>selected='selected'</#if>>分销</option>
                </select>
			<#else>
				<#if (1 == topicDetail.topic.salesPartten)>
					不限
				<#elseif (2 == topicDetail.topic.salesPartten)>
					旗舰店
				<#elseif (3 == topicDetail.topic.salesPartten)>
					商城
				<#elseif (4 == topicDetail.topic.salesPartten)>
					海淘
				<#elseif (5 == topicDetail.topic.salesPartten)>
					闪购
				<#elseif (6 == topicDetail.topic.salesPartten)>
					秒杀
				<#elseif (8 == topicDetail.topic.salesPartten)>
					分销
				</#if>
			</#if>
		</td>
	</tr>
	<tr>
		<td class="td_right">备注:</td>
		<td >
			<#if ("view" != "${mode}")>
				<input type="text" class="input-text lh30" style="width:100%;" name="topic.remark" id="remark" value="${topicDetail.topic.remark!}" />
			<#else>
				<span>${topicDetail.topic.remark!}</span>
			</#if>
		</td>
		<td class="td_right">是否可使用西狗币</td>
		<td colspan="3">
							<#if ("view" != "${mode}")>
									<#if (1 == topicDetail.topic.canUseXgMoney)>
										<input type="radio" name="topic.canUseXgMoney" value="1" checked="checked" />允许
										<input type="radio" name="topic.canUseXgMoney" value="0" />不允许
									<#else>
										<input type="radio" name="topic.canUseXgMoney" value="1" />允许
										<input type="radio" name="topic.canUseXgMoney" value="0" checked="checked" />不允许
									</#if>
								<#else>
									<#if (1 == topicDetail.topic.canUseXgMoney)>
										允许
									<#else>
										不允许
									</#if>
								</#if>
							</td>
						</tr>
						<tr>
							<td class="td_right">是否预留库存</td>
							<td colspan="5">
								<#if (1 == topicDetail.topic.reserveInventoryFlag)>
									预留
								<#else>
									不预留
								</#if>
								<input type=hidden value="${topicDetail.topic.reserveInventoryFlag!}" name="reserveInventoryFlag">
							</td>
						</tr>
</table>
<#include "/promotion/subpages/topicPolicy.ftl"/>
<#include "/promotion/subpages/topicArea.ftl"/>
<#include "/promotion/subpages/topicChangeItemList.ftl"/>
<#include "/promotion/subpages/topicPlatform.ftl"/>
<#include "/promotion/subpages/topicDesc.ftl"/>
<#include "/promotion/subpages/topicCouponList.ftl"/>
<#include "/promotion/subpages/topicRelateList.ftl"/>
<#include "/promotion/subpages/topicAuditInfo.ftl"/>
<table id="topicChangeSaveBtn" class="input tabContent">
	<tr>
		<td align="center">
			<#if ("view" != "${mode}")>
				<input type="button" class="ext_btn ext_btn_submit" id="saveTopic" value="保存" />
				<input type="button" class="ext_btn ext_btn_submit" id="submTopic" value="提交" />
			</#if>
			<input type="button" class="ext_btn ext_btn_submit" id="cancel" value="取消" />
		</td>
	</tr>
</table>