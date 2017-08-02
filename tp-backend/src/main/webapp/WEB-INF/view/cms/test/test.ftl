<#include "/common/common.ftl"/> 
<@backend title="测试活动页面"
		  js=['/statics/backend/js/jquery.min.js',
		  	'/statics/backend/js/jquery.tools.js',
			'/statics/backend/js/jquery.form.2.2.7.js',
			'/statics/cms/js/test/cms_test.js']
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
						返回:${amount}个对象
					</td>
				</tr>
				<tr>
					<td>
						用时:${diff}毫秒
					</td>
				</tr>
			</table>
		</form>
</@backend>