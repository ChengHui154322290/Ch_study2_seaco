<#include "/common/common.ftl"/> 
<@backend title="黏贴输入" 
			js=['/statics/backend/js/json2.js',
	        	'/statics/backend/js/layer/layer.min.js',
				'/statics/backend/js/promotion/utils.js',
				'/statics/backend/js/promotion/promotionItem_batchAdd.js']
			css=[]>
			<form id="batchAdd" method="post">
				<input type="hidden" id="topicId" value="${topicId}"/>
				<input type="hidden" id="sortIndex" name="sortIndex" value="${sortIndex}"/>
				<input type="hidden" id="type" value="${type}"/>
				<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15" style="width:100%;margin:0 auto;">
					<tr>
						<td colspan="2">
							参考格式: <br />
							条码 供应商编号 sku 活动价格 限购数量 限购总量 仓库编号 购买方式 备注	排序<br />
							<strong style="color:red">(条码+供应商编号，sku 二者必填其一)</strong>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<textarea id="batchInput" name="batchInput" style="width:100%;height:200px;" ></textarea>
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
				</table>
			</form>
</@backend>