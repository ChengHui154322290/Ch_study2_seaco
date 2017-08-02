<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="兑换码详细" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
        
        '/statics/cms/js/common/time/js/jquery-ui-1.9.2.custom.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-addon.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-zh-CN.js',
		'/statics/backend/js/dss/promotercoupon.js',
		'/statics/backend/js/coupon/exchangeCouponChannelCode.js'
       ] 
    css=['/statics/backend/css/style.css',
    	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css',
    	'/statics/cms/js/common/time/css/jquery-ui-timepicker-addon.css',
    	'/statics/cms/js/common/time/css/jquery-ui-1.8.17.custom.css'] >
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">优惠券管理->兑换码详细</b> 
            </div>
            <div class="box_center">
                <form id="change_coupon_code_list_form" class="jqtransform" method="post" action="${domain}/topicCoupon/listExchangeCodeDetail.htm">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td width="10%" align="right">活动ID：</td>
                                <td width="15%" align="left"><input type="text" id="actNameBak"
                                    name="actId" value="${query.actId}" class="input-text lh30 actName" size="20"></td>
                                <td width="10%" align="right">推广员：</td>
                                <td width="15%" align="left">
                                	<input type="text"  name="promoterName" class="input-text lh30 promotername ui-autocomplete-input" size="20" autocomplete="off" value="${query.promoterName}">
									<input type="hidden" name="promoterId" value="${query.promoterId}">
                                </td>
                                <td width="10%" align="right">是否兑换：</td>
                                <td width="15%" align="left">
                                	<select class="select" name="status">
                                		<option value="">请选择</option>
                                		<option value="-1"<#if query.status==-1>selected</#if>>已封存</option>
                                		<option value="0"<#if query.status==0>selected</#if>>未兑换</option>
                                		<option value="1"<#if query.status==1>selected</#if>>已兑换</option>
                                		<option value="3"<#if query.status==3>selected</#if>>作废</option>
                                	</select>
                                </td> 
                         	 </tr>
                         	 
                            <tr>
                                <td width="50" align="right">优惠券批次：</td>
                                <td width="50" align="left">
	                                <input type="text"  name="couponName" class="input-text lh30 couponname ui-autocomplete-input" size="20" autocomplete="off" value="${query.couponName}">
	                                <input type="hidden" name="couponId" value="${query.couponId}">
                                </td>
                                <td width="50" align="right">兑换码：</td>
                                <td class="" width="50" align="left">
                                	<input type="text" name="exchangeCode" value="${query.exchangeCode}" class="input-text lh30 actName" size="20">
                                </td>
                                <td width="10%" align="right">使用状态：</td>
                                <td width="15%" align="left">
                                   <select class="select" name="useStatus">
                                		<option value="">请选择</option>
                                		<#list useStatusList as useStatus>
                                		   <option value="${useStatus.code}"<#if useStatus.code==query.useStatus>selected</#if>>${useStatus.cnName}</option>
                                		</#list>
                                	</select>
                                </td>
                            </tr>
							<tr>
                                <td width="50" align="right">批次内序列：</td>
                                <td>
                                	<input type="text" name="beginCodeSeq" value="${query.beginCodeSeq}" class="input-text lh30 actName" size="8" title="包含此序列">~
                                	<input type="text" name="endCodeSeq" value="${query.endCodeSeq}" class="input-text lh30 actName" size="8" title="包含此序列">
                                </td>
                
                                <td colspan="4" align="center">
                                    <input type="submit" id="advertiseListQuery" class="btn btn82 btn_search" value="查询">
                                    <input type="button" id="bingcouponcodetopromoterbtn" class="btn btn82 btn_save2 bingcouponcodetopromoterbtn" value="绑卡" refreshbind="">
                                    <input type="button" id="bingcouponcodetopromoterbtn" class="btn btn82 btn_save2 bingcouponcodetopromoterbtn" value="重新绑定" title="强制重新绑定到推广员" refreshbind="1">
                                    <input type="button" id="disablecouponcodepromoterbtn" class="btn btn82 btn_save2 disablecouponcodepromoterbtn" value="封存">
                                    <input type="button" id="disablecouponcodepromoterbtn" class="btn btn82 btn_save2 disablecouponcodepromoterbtn" value="激活" enabled="1">
                                    <input type="button" id="canclecouponcodepromoterbtn" class="btn btn82 btn_save2 canclecouponcodepromoterbtn"  value="作废" >
                                </td>
                            </tr>
                            <!--虚线-->
                            <tr>
                                <td colspan="8">
                                    <hr style="border: 0.1px dashed #247DFF;" />
                                </td>
                            </tr>

                        </tbody>
                    </table>
                    
					
					<div id="table" class="mt10">
						<table width="100%" cellspacing="0" cellpadding="0" border="0"
	                        class="list_table CRZ customertable" id="CRZ0">
	                        
	                        <thead>
								<tr>
									<th width="20"><input type="checkbox" name="allCouponCodeBtn" class="allcouponcodebtn"/></th>
									<th width="60">活动ID</th>
									<th width="60">批次号</th>
									<th>优惠券批次名称</th>
									<th width="60">序列号</th>
									<th width="60">推广员</th>
									<th width="150">绑定时间</th>
									<th width="100">绑定人</th>
									<th width="150">兑换码</th>
									<th width="60">使用状态</th>
									<th width="60">是否兑换</th>
									<th width="80">会员ID</th>
									<th>兑换的用户名</th>
								</tr>
							</thead>
							<tbody>
								<#if (pageList?? && pageList.getRows()??)>
									<#list pageList.getRows() as page>
										<tr>
											<td><input type="checkbox" class="couponcodeid" value="${page.id}"/></td>
											<td>${page.actId}</td>
											<td>${page.couponId}</td>
											<td>${page.couponName}</td>
											<td>${page.codeSeq}</td>
											<td>${page.promoterName}</td>
											<td><#if page.bindTime??>${page.bindTime?string('yyyy-MM-dd HH:mm:ss')}</#if></td>
											<td>${page.bindUser}</td>
											<td>${page.exchangeCode?substring(0,8)}****</td>
											<td><#if page.couponUser??>${page.couponUser.statusCn}</#if></td>
											<td><#if page.status==0>未兑换<#elseif page.status==-1><#elseif page.status==3>已作废<#else>已兑换</#if></td>
											<td>${page.memberId}</td>
											<td>${page.memberName}</td>
										</tr>
									</#list>
								</#if>
							</tbody>
	                            
	                    </table>
	
						<@pager  pagination=pageList  formId="change_coupon_code_list_form"  />
					</div>
				</form>
            </div>
        </div>
    </div>
</div>


</@backend>
