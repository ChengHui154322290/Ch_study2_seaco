<#include "/common/common.ftl"/> 
<@backend title="促销人员管理" 
 js=['/statics/backend/js/layer/layer.min.js',
 	 '/statics/js/validation/jquery.validate.min.js',
      '/statics/backend/js/dss/promotercoupon.js'] 
	css=[] >
<form method="post" action="${domain}/dss/promotercoupon/save.htm" id="promotercouponForm">
<input type="hidden" name="promoterId" value="${promoterId}"/>
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>卡券名称</td>
						<td>
							<select class="select" with="120px">
								<#if couponList??>
									<#list couponList as coupon>
										<option value="${coupon.id}" >${coupon.couponName}</option>
									</#list>
								</#if>
							</select>
						</td>
					</tr>
					<tr>
						<td>本次发放数量</td>
						<td><input type="number" name="number"  value=""  class="input-text lh25" size="20" maxlength="5"></td>
					</tr>
					<tr>
						<td></td>
						<td>
							<input type="button" value="发放" class="btn btn82 btn_add sendcoupontopromoterbtn" name="button" id="sendcoupontopromoterbtn">
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
</form>
</@backend>
