<#include "/common/common.ftl"/>
   <div>
   			<table class="submit_hint">
						<tr><td>
							<div id="tabBtnContainer" width="100%">
								<ul id="tabBtnUi">
									<li><a href="#"></a>失败提示信息</li>
								</ul>
							</div></td>
						</tr>
								<tr><td style="font-size: 14px;color:#ff0000;padding-top: 2px;" align='center' valign="middle">
								<img alt="" src="" align="middle" height="30" width="30" />
								<span style="vertical-align: middle">您没有权限[${exception.message!}]</span>
								</td></tr>
					</table>
					<table width='100%'>
						<tr><td align="center">
									<input type="button" align="middle" class="button" value="确定" onclick="history.go(-1);" />
							</td></tr>
					</table>
	</body>
   </div>
