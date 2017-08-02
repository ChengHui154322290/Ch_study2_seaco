<#include "/common/common.ftl"/>
<@backend title="导出兑换码" js=['/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/promotion/promotionAddExchangeCode.js',
'/statics/cms/js/common/time/js/jquery-ui-1.9.2.custom.js',
'/statics/backend/js/coupon/couponCancle.js',
'/statics/cms/js/common/time/js/jquery-ui-timepicker-addon.js',
'/statics/cms/js/common/time/js/jquery-ui-timepicker-zh-CN.js']
                	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css',
					'/statics/cms/js/common/time/css/jquery-ui-timepicker-addon.css',
					'/statics/cms/js/common/time/css/jquery-ui-1.8.17.custom.css'] >

<div class="mt10">
        <div class="box">
          <div class="box_border">
            <div class="box_center pt10 pb10">
                <form id="inputForm" action="" method="post" enctype="multipart/form-data">
				<input type="hidden" id="cancleExchangeIds" value="${cancleExchangeIds}" >
				<div ">作废兑换码：
				<textarea rows="10" cols="80"  readonly="readonly" >
				${cancleExchangeCodes}
                </textarea>
                </div>
				
				<div>作废原因：
                                	<select class="select" id="cancleReason" name="cancleReason">
                                		<option value="">请选择</option>
                                		<option value="1">转线下购物卡</option>
                                		<option value="2">业务作废</option>
                                		<option value="3">其他</option>
                                	</select>
               </div>
		<div id="div1_submit" style="text-align:center;">
			<input class="btn btn82 btn_save2  cancleCoupon" id="cancleCoupon" type="button" value="作废"  />
    		<input class="btn btn82 btn_del closebtn" id="closebtn" type="button" value="关闭" name="button1" />
		</div>
			</form>
		</div>
	</div>
</div>
</div>

</@backend>