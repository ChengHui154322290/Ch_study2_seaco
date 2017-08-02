<#include "/common/common.ftl"/> 
<@backend title="专场活动变更单"
		  js=['/statics/backend/js/jquery.min.js',
		  	'/statics/backend/js/jquery.tools.js',
			'/statics/backend/js/jquery.form.2.2.7.js',
			'/statics/backend/js/editor/kindeditor-all.js',
	        '/statics/backend/js/layer/layer.min.js',
			'/statics/backend/js/json2.js',
			'/statics/backend/js/promotion/editorUtils.js',
			'/statics/backend/js/promotion/utils.js',
			'/statics/backend/js/promotion/promotion_change_submit.js',
			'/statics/backend/js/promotion/promotion_edit.js',
			'/statics/backend/js/promotion/promotionCoupon_edit.js',

			'/statics/backend/js/promotion/promotionRelation_edit.js']
		css=['/statics/backend/js/dateTime2/css/jquery-ui-1.8.17.custom.css',
			 '/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css',
			 '/statics/backend/css/style.css']>
		<form action="${domain + "/topicChange/save"}" method="POST" ID="submitTopic">
			<input type="hidden" id="page-type-info" value="topicChange">
			<input type="hidden" id="topicChangeId" name="topicChangeId" value="${topicChangeId!}"/>
			<input type="hidden" id="orderStatus" value="${order!}"/>
			<div class="box_border">
				<div class="box_top">
					<b class="pl15">活动变更单</b>
				</div>
				<#include "/promotion/subpages/topicChangeOrder.ftl"/>
			</div>
		</form>
</@backend>