<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/jquery.tools.js',
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/editor/kindeditor-all.js',
	'/statics/backend/js/editorUtil.js', 
	'/statics/backend/js/tab.js',
	'/statics/backend/js/form.js',
	'/statics/backend/js/util.js',
	'/statics/js/jquery.cookie.js',
	"/statics/backend/js/swfupload/swfupload.js",
	"/statics/backend/js/swfupload/js/fileprogress.js",
	"/statics/backend/js/swfupload/js/handlers.js",
	"/statics/backend/js/swfupload/js/swfupload.queue.js",
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
	'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
	'/statics/backend/js/comment/review/list.js'
	]
    css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css']  >
<div class="mt10">
	<div class="box">
		<div class="box_border">
            <div class="box_center pt10 pb10">
            	<form class="jqtransform" method="post" id="reviewAdd">
            		<table class="form_table" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td valign="middle" width="80">SPU:</td>
							<td valign="middle" align="left">
								<input type="text" name="spu" value="" class="input-text lh25">
							</td>
						</tr>
						<tr>
							<td valign="middle" width="80">用户账户:</td>
							<td valign="middle" align="left">
								<input type="text" name="memLoginName" id="memLoginName" placeholder="手机号" value="" class="input-text lh25">
								<input type="button" value="查询" id= "queryUserByAccountBtn" class="ext_btn ext_btn_submit m10" />
							</td>
						</tr>
						<tr>
							<td valign="middle" width="80">用户昵称:</td>
							<td valign="middle" align="left">
								<input type="text" name="userName" id="userName" placeholder="昵称" value="" class="input-text lh25" readonly = readonly>
								<input type="hidden" id="uid" name="uid" id="uid" value=""  />
							</td>
						</tr>
						<tr>
							<td valign="middle" width="80">评论日期:</td>
							<td valign="middle" align="left">
								<input type="text" name="createBeginTime" id="createBeginTime" class="input-text lh25" size="20">
							</td>
						</tr>
						<tr>
							<td>分值:</td>
							<td valign="middle" align="left">
								<input type="radio" name="mark" checked="checked" value="5"/>
								<label style="margin-right:8px;">5星</label>
								<input type="radio" name="mark" <#if review.mark="4">checked</#if> value="4"/>
								<label style="margin-right:8px;">4星</label>
								<input type="radio" name="mark" <#if review.mark="3">checked</#if> value="3"/>
								<label style="margin-right:8px;">3星</label>
								<input type="radio" name="mark" <#if review.mark="2">checked</#if> value="2"/>
								<label style="margin-right:8px;">2星</label>
								<input type="radio" name="mark" <#if review.mark="1">checked</#if> value="1"/>
								<label>1星</label>
							</td>
						</tr>
						<tr>
							<td>置顶:</td>
							<td valign="middle" align="left">
								<input type="radio" name="isTop" checked="checked" value="1"/>
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
								<input type="radio" name="isHide" checked="checked" value="2"/>
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
								<textarea name="content" cols="30" rows="3" id="subTitle" maxlength=100 onMouseOver="this.title=this.value" ></textarea>
							</td>
						</tr>
					</table>
					<div style="margin-left:92px;margin-bottom:20px;">（输入10个字以上5000字以下）</div>
            		<div id="div1_submit" style="text-align:center;">
						<input class="btn btn82 btn_save2" type="button" value="保存" onclick="saveAdd(this)" />
    					<input class="btn btn82 btn_del closebtn" type="button" value="取消" name="button1" />
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
</@backend>
