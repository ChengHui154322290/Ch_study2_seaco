<#include "/common/common.ftl"/> 
<@backend title="促销人员管理" 
 js=['/statics/themes/layout.js',
 	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.js', 
 	'/statics/backend/js/layer/layer.min.js',
 	'/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
	'/statics/backend/js/dss/jqGridformatter.js',
	'/statics/backend/js/dss/promoterinfo.js',
	'/statics/backend/js/dss/commisiondetail.js',
	'/statics/backend/js/dss/accountdetail.js',
	'/statics/backend/js/dss/withdrawdetail.js',
	'/statics/backend/js/dss/refereedetail.js'
	'/statics/backend/js/dss/promotercoupon.js',
	'/statics/supplier/component/date/WdatePicker.js'
	] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css',
'/statics/backend/css/style.css',
'/statics/supplier/component/date/skin/WdatePicker.css'
	] >

<form method="post" action="${domain}/dss/promoterinfo/list.htm" id="promoterinfoListForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">营销人员列表</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>姓名</td>
						<td><input type="text" name="promoterName" class="input-text lh25" size="10" value="${promoterInfo.promoterName}"></td>
						<td>手机号</td>
						<td><input type="text" name="mobile" class="input-text lh25" size="15" value="${promoterInfo.mobile}"></td>
						<td>开通状态</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="promoterStatus" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list promoterStatusList as promoterStatus>
			                       		<option value="${promoterStatus.code}" <#if promoterStatus.code==promoterInfo.promoterStatus>selected</#if> >${promoterStatus.cnName}</option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
                    	</td>
						<td>营销类型</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="promoterType" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list promoterTypeList as promoterType>
			                       		<option value="${promoterType.code}" <#if promoterType.code==promoterInfo.promoterType>selected</#if> >${promoterType.cnName}</option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
						</td>
						<td class="td_right" width="50" align="right">开通时间:</td>
						<td class="" width="150" align="left" colspan="4">
                        	<input type="text" class="input-text dateInput" width="20px" name="startTime" readonly="true" id="startTime"   value="<#if promoterInfo.startTime??>${promoterInfo.startTime?string('yyyy-MM-dd hh:mm:ss')}</#if>"  onClick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                    	</td>
                    	<td>至</td>
                    	<td class="" width="150" align="left" colspan="4">
                    		<input type="text" class="input-text dateInput" width="20px" name="endTime" readonly="true"  id="endTime" value="<#if promoterInfo.endTime??>${promoterInfo.endTime?string('yyyy-MM-dd hh:mm:ss')}</#if>"  onClick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
						</td>
					</tr>
				</table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
				<div class="search_bar_btn" style="text-align: right;">
					<input type="submit" value="查询" class="btn btn82 btn_search querypagebtn" name="button">
					<input type="button" value="新增" class="btn btn82 btn_add editpromoterinfobtn" name="button" param="">
					<input type="button" value="新增分销公司" class="btn btn_add editpromotercompanybtn" name="button" param="">
				</div>
			</div>
		</div>
	</div>
</div>
<#if resultInfo.msg??>${resultInfo.msg.message}<#else>
<#assign page = resultInfo.data>
<div id="table" class="mt10">
	<div class="box span10 oh">
		<table  id="CRZ0" width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
			<tr>
				<th width="80">姓名</th>
				<th width="40">性别</th>
				<th width="100">手机号</th>
				<th width="80">QQ</th>
				<th width="100">微信</th>
				<th width="70">营销类型</th>
				<th width="70">开通状态</th>
				<th width="140">开通时间</th>
				<th width="70">佣金比(%)</th>
				<th width="140">创建时间</th>
				<th>操作</th>
				<th width="80">店铺个性化</th>
			</tr>
			<#if page.rows??> 
				<#list page.rows as promoterInfo>
				<tr>
					<td><a href="javascript:void(0);" class="showpromoterinfobtn" promoterid="${promoterInfo.promoterId}">${promoterInfo.promoterName}</a></td>
					<td>${promoterInfo.genderCn}</td>
					<td>${promoterInfo.mobile}</td>
					<td>${promoterInfo.qq}</td>
					<td>${promoterInfo.weixin}</td>
					<td>${promoterInfo.promoterTypeCn}</td>
					<td>${promoterInfo.promoterStatusCn}</td>
					<td><#if promoterInfo.passTime??>${promoterInfo.passTime?string('yyyy-MM-dd HH:mm:ss')}</#if></td>
					<td>${promoterInfo.commisionRate?string('0.00')}</td>
					<td>${promoterInfo.createTime?datetime}</td>
					<td>
						<input type="button" value="编辑" class="ext_btn ext_btn_submit <#if promoterInfo.promoterType==3>editpromotercompanybtn<#else>editpromoterinfobtn</#if>" param="${promoterInfo.promoterId}">
						<#if promoterInfo.promoterType==1 && promoterInfo.promoterStatus==0>
						<#else>
							<input type="button" value="<#if promoterInfo.promoterStatus==0 >开通<#elseif promoterInfo.promoterStatus==1>禁用<#else>启用</#if>" class="ext_btn ext_btn_submit updatestatusbtn" promoterid="${promoterInfo.promoterId}" promoterstatus="${promoterInfo.promoterStatus}">
						</#if>
						<#if promoterInfo.promoterType==0>
						  <input type="button" value="发券" class="ext_btn ext_btn_submit sendcouponbtn" promoterid="${promoterInfo.promoterId}">
						  <input type="button" value="卡券明细" class="ext_btn ext_btn_submit couponlistbtn" promoterid="${promoterInfo.promoterId}" promotername="${promoterInfo.promoterName}">
						<#elseif promoterInfo.promoterType==1>
						  <input type="button" value="下级列表" class="ext_btn ext_btn_submit childrenlistbtn" promoterid="${promoterInfo.promoterId}">
						  <input type="button" value="拉新佣金" class="ext_btn ext_btn_submit refereelistbtn" promoterid="${promoterInfo.promoterId}">
						<#elseif promoterInfo.promoterType == 3>
						  <input type="button" value="下级列表" class="ext_btn ext_btn_submit childrenlistbtn" promoterid="${promoterInfo.promoterId}">
						</#if>
						<input type="button" value="订单列表" class="ext_btn ext_btn_submit orderlistbtn" promoterid="${promoterInfo.promoterId}" promotername="${promoterInfo.promoterName}" promotertype="${promoterInfo.promoterType}">
						<input type="button" value="佣金明细" class="ext_btn ext_btn_submit commisionlistbtn" promoterid="${promoterInfo.promoterId}">
						<input type="button" value="提现明细" class="ext_btn ext_btn_submit withdrawlistbtn" promoterid="${promoterInfo.promoterId}">
						<input type="button" value="流水帐" class="ext_btn ext_btn_submit accountdetailbtn" promoterid="${promoterInfo.promoterId}">
					</td>
					<td>
						<#if promoterInfo.promoterType==1>
							<input type="button" value="<#if promoterInfo.pageShow==0>开启<#else>关闭</#if>" class="ext_btn updatepageshowbtn" promoterid="${promoterInfo.promoterId}" pageshow="${promoterInfo.pageShow}">
						</#if>
					</td>
				</tr>
				</#list> 
			</#if>
		</table>
	</div>
</div>
<@pager  pagination=page  formId="promoterinfoListForm"  /> 
</#if>
</form>
</@backend>
