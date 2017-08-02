<#include "/common/common.ftl"/> 
<@backend title="专场活动变更单"
		  js=['/statics/backend/js/jquery.min.js',
		  	'/statics/backend/js/jquery.tools.js',
			'/statics/backend/js/jquery.form.2.2.7.js',
			'/statics/backend/js/editor/kindeditor-all.js',
	        '/statics/backend/js/layer/layer.min.js',
			'/statics/backend/js/json2.js',
			'/statics/backend/js/promotion/utils.js',
			'/statics/backend/js/promotion/promotion_change_submit.js',
			'/statics/backend/js/promotion/promotion_edit.js',
			'/statics/backend/js/promotion/promotionRelation_edit.js']
		css=['/statics/backend/js/dateTime2/css/jquery-ui-1.8.17.custom.css',
			 '/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css',
			 '/statics/backend/css/style.css']>
	<input type="hidden" id="topicChangeId" name="topicChangeId" value="${topicChangeId!}"/>
	<input type="hidden" id="orderStatus" value="${order!}"/>
	<div class="box_border">
		<div class="box_top">
			<b class="pl15">导入活动变更单</b>
		</div>
		<div class="box_center">
			<table id="topicSelection" class="input tabContent">
				<tr>
					<td class="td_right">
						活动序号
					</td>
					<td colspan="5">
						<input type="text" class="input-text lh30" name="topicChange.changeTopicId" id="changeTopicIdInput" value="${changeTopicId!}" />
						<input type="button" class="btn btn82 btn_save2" id="confirmChangeTopicId" value="导入" />
						<input type="button" class="btn btn82 btn_save2" id="return" value="返回" />
					</td>
				</tr>
			</table>
		</div>
	</div>
</@backend>