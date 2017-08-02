<#include "/common/common.ftl"/> 
<@backend title="测试活动页面"
		  js=['/statics/backend/js/jquery.min.js',
		  	'/statics/backend/js/jquery.tools.js',
			'/statics/backend/js/jquery.form.2.2.7.js',
			'/statics/backend/js/promotion/promotion_test.js']
		css=[]>
		<form method="POST" ID="submitTest">
			<table>
				<tr>
					<td>
						<input type="button" class="ext_btn ext_btn_submit" style="width:100px;" id="today" value="今日特卖" />
					</td>
				</tr>
				<tr>
					<td>
						返回:${jrtmAmount}个对象
					</td>
				</tr>
				<tr>
					<td>
						用时:${jrtmDiff}毫秒
					</td>
				</tr>
				<tr>
					<td>
						<input type="button" class="ext_btn ext_btn_submit" style="width:100px;" id="mtt" value="西客商城商城-活动" />
					</td>
				</tr>
				<tr>
					<td>
						返回:${mttAmount}个对象
					</td>
				</tr>
				<tr>
					<td>
						用时:${mttDiff}毫秒
					</td>
				</tr>
				<tr>
					<td>
						<input type="button" class="ext_btn ext_btn_submit" style="width:100px;" id="mtb" value="西客商城商城-品牌" />
					</td>
				</tr>
				<tr>
					<td>
						返回:${mtbAmount}个对象
					</td>
				</tr>
				<tr>
					<td>
						用时:${mtbDiff}毫秒
					</td>
				</tr>
				<tr>
					<td>
						<input type="button" class="ext_btn ext_btn_submit" style="width:100px;" id="mti" value="西客商城商城-商品" />
					</td>
				</tr>
				<tr>
					<td>
						返回:${mtiAmount}个对象
					</td>
				</tr>
				<tr>
					<td>
						用时:${mtiDiff}毫秒
					</td>
				</tr>
			</table>
		</form>
</@backend>