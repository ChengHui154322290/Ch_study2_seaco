<#include "/common/common.ftl"/>
<@backend title="" 
js=['/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/item/item-detailsales.js']
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css']
	 >
<div class="container">
		<div class="box">
				<table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0" style=" overflow:outo"> 
					<tr>
						<td class="td_left" style="width: 20%">条形码：</td>
						<td class="" style="width: 30%">
							${dto.barcode}
						</td>
						<td class="td_left" style="width: 20%">PRDID：</td>
						<td class="" style="width: 30%">${dto.prdid}</td>
					</tr>
					
					<tr>
						<td class="td_left" style="width: 20%">商品名称：</td>
						<td class="" style="width: 30%">
							${dto.mainTitle}
						</td>
					</tr>
					<tr>
						<td class="td_left" style="width: 20%">基数：</td>
						<td class="" style="width: 30%">
							${dto.defaultSalesCount}
						</td>
					</tr>
					
					<tr>
						<td class="td_left" style="width: 30%">基数最后修改操作员：</td>
						<td class="" style="width: 30%">
							${dto.lastUpdateUser}
						</td>
						<td class="td_left" style="width: 20%">基数最后修改时间：</td>
						<td class="" style="width: 30%"><#if dto.updateDefaultCountTime??>${dto.updateDefaultCountTime?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
						
					</tr>
					<tr>
						<td class="td_left" style="width: 30%">下单数：</td>
						<td class="" style="width: 30%">
							${dto.relSalesCount}
						</td>
						<td class="td_left" style="width: 20%">基数最后修改时间：</td>
						<td class="" style="width: 30%"><#if dto.updateTime??>${dto.updateTime?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
						
					</tr>
				</table>
			</div>
	</div>
					<div class="tc">  
							<td>
								<input type="button" class="ext_btn ext_btn_submit m10 closelayerbtn"  value="关闭">
							</td>
						</div>
</@backend>