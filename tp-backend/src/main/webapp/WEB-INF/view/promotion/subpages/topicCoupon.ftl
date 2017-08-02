<tr align="center" class="topicCouponList" style="background-color: rgb(255, 255, 255);">
	<td style="display:none">
		<input type="hidden" name="couponId" value="${tCoupon.couponId}" />
		<input type="hidden" name="couponImage" value="${tCoupon.couponImage}" />
	</td>
	<td>
		<input type="text" class="input-text lh30 topicInteger" width="120px" name="couponSortIndex" value="${tCoupon.sortIndex!}" />
	</td>
	<td>
		<input type="text" class="input-text lh30 topicInteger" width="120px" name="couponIdValue" value="${tCoupon.couponId!}" />
	</td>
	<td>
		<span>${tCoupon.couponName!}</span>
	</td>
	<td>
		<span></span>
	</td>
	<td>
		<select class="select" style="width:150px;" name="couponSize">
			<option value="1" <#if tCoupon.couponSize??&&tCoupon.couponSize?string=='1'>selected='selected'</#if>>1*1</option>
			<option value="2" <#if tCoupon.couponSize??&&tCoupon.couponSize?string=='2'>selected='selected'</#if>>1*2</option>
			<option value="4" <#if tCoupon.couponSize??&&tCoupon.couponSize?string=='4'>selected='selected'</#if>>1*4</option>
		</select>
	</td>
	<td style="width:150px;height:80px;">
		<!-- 图片可以点击看完整图片 -->
		<div style="float:left;width:90%;">
			<img name="selectCouponImage" style="width:120px;height:80px;" key="${tCoupon.couponImageFullPath!}" src="${domain + '/statics/backend/images/wait_upload.png'}"/>
		</div>
		<div style="float:left;">
			<a name="removeCouponImage" href="javascript:void(0);">X</a>
		</div>
	</td>
	<td>
		<input type="button" class="btn btn82 btn_save2" name="confirmCoupon" value="确定" />
		<input type="button" class="btn btn82 btn_search" name="searchCoupon" value="查找" />
		<input type="button" class="btn btn82 btn_del closebtn" name="removeCoupon" value="移除" />
	</td>
</tr>