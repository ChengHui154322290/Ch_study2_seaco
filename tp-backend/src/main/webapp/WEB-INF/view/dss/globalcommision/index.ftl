<#include "/common/common.ftl"/> 
<@backend title="促销人员管理" 
 js=['/statics/backend/js/layer/layer.min.js',
 	 '/statics/js/validation/jquery.validate.min.js',
      '/statics/backend/js/dss/globalcommison.js'] 
	css=[] >
<#if resultInfo.msg??>${resultInfo.msg.message}<#else>
<#assign globalcommison = resultInfo.data>
<form method="post" action="${domain}/dss/globalcommison/save.htm" id="globalcommisonForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>上上级店铺分销佣金占比</td>
						<td><input id="firstCommisionRate" type="float" name="firstCommisionRate" value="<#if globalcommison.firstCommisionRate??>${globalcommison.firstCommisionRate?string('0.00')}</#if>" class="input-text lh25" size="20" maxlength="5"></td>
					</tr>
					<tr>
						<td>上级店铺分销佣金占比</td>
						<td><input id="secondCommisionRate" type="float" name="secondCommisionRate"  value="<#if globalcommison.secondCommisionRate??>${globalcommison.secondCommisionRate?string('0.00')}</#if>"  class="input-text lh25" size="20" maxlength="5"></td>
					</tr>
					<tr>
						<td>当前店铺分销佣金占比</td>
						<td><input id="threeCommisionRate" type="float" name="threeCommisionRate"  value="<#if globalcommison.threeCommisionRate??>${globalcommison.threeCommisionRate?string('0.00')}</#if>"  class="input-text lh25" size="20" maxlength="5"></td>
					</tr>
					<tr>
						<td>扫码推广佣金比率(%)</td>
						<td><input id="scanCommisionRate" type="float" name="scanCommisionRate" placeholder="扫码佣金=商品佣金*扫码推广佣金比率" value="<#if globalcommison.scanCommisionRate??>${globalcommison.scanCommisionRate?string('0.00')}</#if>"  class="input-text lh25" size="20" maxlength="5"></td>
					</tr>
					
					<tr>
						<td></td>
						<td>
							<input type="submit" value="编辑" class="btn btn82 btn_add saveglobalcommisonbtn" name="button" id="saveglobalcommisonbtn">
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
</form>
</#if>
<#if commisionList??>
<div id="table" class="mt10">
	<div class="box span10 oh">
		<table  id="CRZ0" width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
			<tr>
				<th>上上级店铺分销佣金占比</th>
				<th>上级店铺分销佣金占比</th>
				<th>当前店铺分销佣金占比</th>
				<th>扫码推广佣金比率(%)</th>
				<th>创建时间</th>
			</tr>
			<#list commisionList as commision>
				<tr>
					<td>${commision.firstCommisionRate?string('0.00')}</td>
					<td>${commision.secondCommisionRate?string('0.00')}</td>
					<td><#if commision.threeCommisionRate??>${commision.threeCommisionRate?string('0.00')}</#if></td>
					<td><#if commision.scanCommisionRate??>${commision.scanCommisionRate?string('0.00')}</#if></td>
					<td>${commision.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
				</tr>
			</#list>
		</table>
	</div>
<div>
</#if>
</@backend>
