<#include "/common/common.ftl"/>
<@backend title="" js=['/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
	'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
	'/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	'/statics/backend/js/user/review.js',
	'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js']
    css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css']  >
<div class="mt10">
	<div class="box">
		<div class="box_border">
            <div class="box_center pt10 pb10">
            	<form class="jqtransform" method="post" id="reviewEdit">
            		<table class="form_table" border="0" cellpadding="0" cellspacing="0">
            			<input type='hidden' name='id' value='${review.id}'>
              			<tr>
							<td valign="middle" width="80">条形码:</td>
							<td>
								${review.barcode}
							</td>
						</tr>
						<tr>
							<td>PRD:</td>
							<td>
								${review.prd}
							</td>
						</tr>
						<tr>
							<td>商品名称:</td>
							<td>
								${review.itemName}
							</td>
						</tr>
						<tr>
							<td>用户名:</td>
							<td>
								${review.userName}
							</td>
						</tr>
						<tr>
							<td>姓名:</td>
							<td>
								${review.userName}
							</td>
						</tr>
						<tr>
							<td>订单号:</td>
							<td>
								${review.orderCode}
							</td>
						</tr>
						<tr>
							<td>评论日期:</td>
							<td>
								${review.getCreateTime()}
							</td>
						</tr>
						<tr>
							<td>分值:</td>
							<td valign="middle" align="left">
								<input type="radio" name="star" <#if review.star="5"> checked="checked"</#if> value="5"/>
								<label style="margin-right:8px;">5星</label>
								<input type="radio" name="star" <#if review.star="4">checked</#if> value="4"/>
								<label style="margin-right:8px;">4星</label>
								<input type="radio" name="star" <#if review.star="3">checked</#if> value="3"/>
								<label style="margin-right:8px;">3星</label>
								<input type="radio" name="star" <#if review.star="2">checked</#if> value="2"/>
								<label style="margin-right:8px;">2星</label>
								<input type="radio" name="star" <#if review.star="1">checked</#if> value="1"/>
								<label>1星</label>
							</td>
						</tr>
						<tr>
							<td>置顶:</td>
							<td valign="middle" align="left">
								<input type="radio" name="isTop" <#if review.isTop==1> checked="checked"</#if> value="1"/>
								<label style="margin-right:8px;">不限</label>
								<input type="radio" name="isTop" <#if review.isTop==2> checked="checked"</#if> value="2"/>
								<label style="margin-right:8px;">置顶</label>
								<input type="radio" name="isTop" <#if review.isTop==0> checked="checked"</#if> value="0"/>
								<label style="margin-right:8px;">置底</label>
							</td>
						</tr>
						<tr>
							<td>隐藏:</td>
							<td valign="middle" align="left">
								<input type="radio" name="isHide" <#if review.isHide==2> checked="checked"</#if> value="2"/>
								<label style="margin-right:8px;">不限</label>
								<input type="radio" name="isHide" <#if review.isHide==1> checked="checked"</#if> value="1"/>
								<label style="margin-right:8px;">隐藏</label>
								<input type="radio" name="isHide" <#if review.isHide==0> checked="checked"</#if> value="0"/>
								<label style="margin-right:8px;">仅自己可见</label>
							</td>
						</tr>
						<tr>
							<td>评论内容:</td>
							<td rowspan="2">
								<textarea name="content" cols="30" rows="3" id="subTitle" maxlength=100 onMouseOver="this.title=this.value" >${review.content}</textarea>
							</td>
						</tr>			
					</table>
					<div style="margin-left:92px;margin-bottom:20px;">（输入10个字以上5000字以下）</div>
            		<div id="div1_submit" style="text-align:center;">
						<input class="btn btn82 btn_save2" type="button" value="保存" param='/user/review/edit.htm' onclick="upd(this)" />
    					<input class="btn btn82 btn_del closebtn" type="button" value="取消" name="button1" />
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
</@backend>
