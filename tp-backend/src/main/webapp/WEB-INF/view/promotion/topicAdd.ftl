<#include "/common/common.ftl"/> 
<@backend title="新增专场活动" 
		js=['/statics/backend/js/json2.js',
			'/statics/backend/js/jquery.form.2.2.7.js',
	        '/statics/backend/js/layer/layer.min.js',
			'/statics/backend/js/promotion/utils.js',
			'/statics/backend/js/promotion/promotion_add.js']
		css=[]>
		<form id="addTopic" method="POST">
			<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15" id="addTopicForm">  
				<tbody>
					<tr>
						<td class="td_right" width="50" align="right"><strong style="color:red;">*</strong>专场类型:</td>
						<td class="" width="50" align="left">
							<div class="select_border">
		                        <div class="select_containers">
		                            <span class="fl"> 
		                            	<select class="select" name="type" id="type" style="width:150px;">
											<option value="1" selected>单品团</option>
											<option value="2">品牌团</option>
											<option value="3">主题团</option>
											<option value="5">商家店铺</option>
										</select>
									</span>
		                        </div>
		                    </div>
						</td>
					</tr>
					<tr>
						<td class="td_right" width="50" align="right"><strong style="color:red;">*</strong>专场名称：</td>
						<td class="" width="50" align="left"><input type="text" id="name" name="name" class="input-text lh30" /></td>
					</tr>
					<tr>
						<td class="td_right" width="50" align="right">支持商户提报:</td>
						<td class="" width="50" align="left">
							<input type="radio" id="isSupportSupplier_0" name="isSupportSupplier" value="0" checked />支持
							<input type="radio" id="isSupportSupplier_1" name="isSupportSupplier" value="1" />不支持
						</td>
					</tr>
					<tr>
						<td class="td_right" width="50" align="right">是否可使用西客币:</td>
						<td class="" width="50" align="left">
						    <input type="radio" id="canUseXgMoney_1" name="canUseXgMoney" value="1" checked />允许
							<input type="radio" id="canUseXgMoney_0" name="canUseXgMoney" value="0"  />不允许
						</td>
					</tr>
					<tr>
						<td class="td_right" width="50" align="right">是否预留库存:</td>
						<td class="" width="50" align="left">
						    <input type="radio" name="reserveInventoryFlag" value="1"  />预留
							
							<input type="radio" name="reserveInventoryFlag" value="0" checked />不预留
							
						</td>
					</tr>
					<tr>
						<td align="right">
							<div style="padding-right:10px">
								<input type="button" class="btn btn82 btn_save2" id="save" value="保存" />
							</div>
						</td>
						<td align="left">
							<div style="padding-left:10px">
								<input type="button" class="btn btn82 btn_del closebtn" id="cancel" value="取消" />
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
</@backend>