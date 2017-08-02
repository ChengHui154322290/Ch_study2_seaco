<!-- 优惠券信息 -->
<!-- 添加/编辑优惠券块 -->
<table id="topicCouponDetails" class="input tabContent">
	<tr>
		<td class="td_left">
			<h4>专题优惠券:</h4>
		</td>
		<td colspan="5">
			<#if ("view" != "${mode}")>
				<input type="button" class="ext_btn ext_btn_submit" id="addCoupon" value="新增" />
			</#if>
		</td>
	</tr>
	<tr>
		<td colspan="6">
			<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="topicCouponsList">
				<tr align="center">
					<th style="display:none"></th>
					<th>排序</th>
					<th>优惠券序号</th>
					<th>优惠券名称</th>
					<th style="width:100px;">优惠券类型</th>
					<th style="width:160px;">图片栏位大小</th>
					<th style="width:160px;">展示图片</th>
					<#if ("view" != "${mode}")>
						<th style="width:320px;">操作</th>
					</#if>
				</tr>
				<#if topicDetail.couponList??>
					<#list topicDetail.couponList as tCoupon>
						<tr align="center" class="topicCouponList" style="background-color: rgb(255, 255, 255);">
							<td style="display:none">
								<input type="hidden" name="topicCouponId" value="${tCoupon.id}" />
								<input type="hidden" name="couponId" value="${tCoupon.couponId}" />
								<input type="hidden" name="couponImage" value="${tCoupon.couponImage}" />
							</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 topicInteger" width="120px" name="couponSortIndex" value="${tCoupon.sortIndex!}" />
								<#else>
									<span>${tCoupon.sortIndex!}</span>
								</#if>
							</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 topicInteger" readonly="true" width="120px" name="couponIdValue" value="${tCoupon.couponId!}" />
								<#else>
									<span>${tCoupon.couponId!}</span>
								</#if>
							</td>
							<td>
								<span>${tCoupon.couponName!}</span>
							</td>
							<td>
								<#if tCoupon.couponType??&&tCoupon.couponType?string=='0'><span>满减券</span></#if>
								<#if tCoupon.couponType??&&tCoupon.couponType?string=='1'><span>现金券</span></#if>
							</td>
							<td>
								<#if ("view" != "${mode}")>
									<select class="select" style="width:150px;" name="couponSize">
										<option value="1" <#if tCoupon.couponSize??&&tCoupon.couponSize?string=='1'>selected='selected'</#if>>1*1</option>
										<option value="2" <#if tCoupon.couponSize??&&tCoupon.couponSize?string=='2'>selected='selected'</#if>>2*2</option>
										<option value="4" <#if tCoupon.couponSize??&&tCoupon.couponSize?string=='4'>selected='selected'</#if>>4*4</option>
									</select>
								<#else>
									<#if tCoupon.couponSize??&&tCoupon.couponSize?string=='1'><span>1*1</span></#if>
									<#if tCoupon.couponSize??&&tCoupon.couponSize?string=='2'><span>1*2</span></#if>
									<#if tCoupon.couponSize??&&tCoupon.couponSize?string=='4'><span>1*4</span></#if>
								</#if>
							</td>
							<td style="width:120px;height:80px;">
								<!-- 图片可以点击看完整图片 -->
								<div style="float:left;width:90%;">
									<img name="selectCouponImage" style="width:120px;height:80px;" key="${tCoupon.couponFullImage!}" src=""/>
								</div>
								<#if ("view" != "${mode}")>
									<div style="float:left;">
										<a name="removeCouponImage" href="javascript:void(0);">X</a>
									</div>
								</#if>
							</td>
							<#if ("view" != "${mode}")>
								<td>
									<input type="button" class="btn btn82 btn_del closebtn" name="removeCoupon" value="移除" />
								</td>
							</#if>
						</tr>
					</#list>
				</#if>
			</table>
		</td>
	</tr>
</table>