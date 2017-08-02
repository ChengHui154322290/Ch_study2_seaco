<#include "/common/common.ftl"/> 
<@backend title="促销人员管理" 
 js=[ 
    '/statics/backend/js/dateTime2/js/jquery-ui.min.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
    '/statics/backend/js/layer/layer.min.js',
 	'/statics/js/validation/jquery.validate.min.js',
    '/statics/js/validation/jquery.validate.method.min.js',
	'/statics/backend/js/dss/scanpromoterinfo.js'] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css','/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css'] >
<form method="post" action="${domain}/dss/scan/promoterinfo/save.htm" id="savepromoterinfoForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>公司名或姓名</td>
						<td><input id="promoterName" type="text" name="promoterName" value="${promoterInfo.promoterName}" class="input-text lh25" size="20"></td>
					</tr>
					<tr>
						<td>推广员大小</td>
						<td><#list promoterLevelList as promoterLevel>
							<input type="radio" name="promoterLevel" value="${promoterLevel.code}" <#if promoterLevel.code==promoterInfo.promoterLevel>checked</#if> >${promoterLevel.cnName}
						</#list></td>
					</tr>
					<!--
					<tr>
						<td>拉新佣金</td>
						<td><input id="referralUnit" type="text" name="referralUnit"  value="${promoterInfo.referralUnit}" class="input-text lh25" size="20"></td>
					</tr>
					-->
					<tr>
						<td>返佣比例</td>
						<td><input id="commisionRate" type="text" name="commisionRate"  value="${promoterInfo.commisionRate}" class="input-text lh25" size="20">%</td>
					</tr>
				</table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
				<div class="search_bar_btn" style="text-align: center;">
					<input type="submit" value="生成邀请码" wid class="btn btn82 btn_add2 savepromoterinfobtn" name="button"  style="width:100px" id="savepromoterinfobtn">
				</div>
			</div>
		</div>
	</div>
</div>
</form>
</@backend>
