<#include "/common/common.ftl"/> 
<@backend title="上传商品图片" 
		js=['/statics/backend/js/json2.js',
			'/statics/backend/js/swfupload/swfupload.js',
			'/statics/backend/js/swfupload/js/fileprogress.js',
			'/statics/backend/js/swfupload/js/handlers.js',
			'/statics/backend/js/swfupload/js/swfupload.queue.js',
			'/statics/backend/js/promotion/promotionCoupon_upload.js',
	        '/statics/backend/js/layer/layer.min.js',
			'/statics/backend/js/promotion/utils.js']
		css=[]>
	<form id="uploadItemPicture" method="post" enctype="multipart/form-data">
		<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
			<tr>
				<td class="td_right" width="120" align="right">选择上传图片:</td>
				<td  width="80%" align="left">
					<span id="spanButtonCouponPic" style="margin-left:20px;"></span>
					<br/>
					<span style="margin-left:20px;">可以上传 jpg;jpeg;png;类型图片，最大1 MB 第一张默认为主图</span>
					<div class="fieldset flash" style="display:none" id="couponPicUploadProgress">
						<span class="legend"></span>
					</div>
					<input type="hidden" id="selectCouponIndex" value="${couponIndex}" />
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<div style="padding-right:10px">
						<input type="button" class="btn btn82 btn_del closebtn" id="close" value="关闭" style="margin-top: 10px;" />
					</div>
				</td>
			</tr>
		</table>
	</form>
</@backend>