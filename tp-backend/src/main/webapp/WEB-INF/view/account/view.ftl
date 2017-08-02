<#include "/common/common.ftl"/> 
<@backend title="" 
js=['/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
	'/statics/backend/js/account/view.js'
	]
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css'] >
<div class="container">
	<div class="box">
		<div id="forms" class="mt10">
			<div class="box">
				<form method="post" action="${domain}/account/opt_${type}.htm" class="jqtransform">
					<div class="box_border">
						<div class="box_center">
							<input type="hidden" name="id" value="${sub.account.id!}"/>
							<table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td class="td_right" style="width: 10%">对接方：</td>
									<td>
										<input type="text" class="input-text lh25" style="width:300px" name="name" value="${sub.account.name!}"/>
									</td>
								</tr>
								<tr>
									<td class="td_right" style="width: 10%">供应商：</td>
									<td>
										<textarea style="width:500px;height:60px;" name="supplier">
										<#compress>
										<#if sub.supplierList??> 
										<#list sub.supplierList as sup>
											<#t>${sup.supplierId!}<#if sup_has_next>,</#if><#t>
										</#list> 
										</#if>
										</#compress>
										</textarea>
										<button class="btn btn82 btn_search" name="btn_supplier">供应商查询</button>
									</td>
								</tr>
								<tr>
									<td class="td_right" style="width: 10%">AppKey：</td>
									<td>
										<input type="text" class="input-text lh25" style="width:300px" name="appkey" value="${sub.account.appkey!}"/>
									</td>
								</tr>
								<#if type=='edit'>
								<tr>
									<td class="td_right" style="width: 10%">账户状态：</td>
									<td>
										<#if sub.account.status=1>有效<#elseif sub.account.status=0>无效<#else>已删除</#if>
									</td>
								</tr>
								
								<tr>
									<td class="td_right" style="width: 10%">修改时间：</td>
									<td>${sub.account.modifyTime!?datetime}</td>
								</tr>
								<tr>
									<td class="td_right" style="width: 10%">创建时间：</td>
									<td>${sub.account.createTime!?datetime}</td>
								</tr>
								</#if>
							</table>
						</div>
						
						<div class="box_border" style="text-align:center;">
							<input class="btn btn82 btn_add" type="submit" onclick="return verify_form()" value="保存" />
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
</@backend>