<#include "/common/common.ftl"/> 
<@backend title="优惠券明细" 
			js=['/statics/backend/js/layer/layer.min.js',
			'/statics/backend/js/coupon/couponDetail.js'
				]
			css=[]>
<form class="jqtransform" method="post" id="couponDetailForm" action="${domain}/coupon/detailSearch.htm">
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">促销管理->优惠券->优惠券明细</b> 
            </div>
	        <table cellspacing="0" cellpadding="0" border="0" width="100%"
	            class="form_table pt15 pb15">
	            <tbody>
	                <tr>
	                    <td class="td_right" width="50" align="left">批号：</td>
	                    <td class="" width="50" align="left">
	                    	<input type="text" name="couponId" class="input-text lh30" size="17" value="${couponInfoQuery.couponId}" />
	                    </td> 
	                    <td class="td_right" width="50" align="left">优惠券名称：</td>
	                    <td class="" width="50" align="left">
	                    	<input type="text" name="couponName" class="input-text lh30" size="17" value='${couponInfoQuery.couponName}'>
	                    </td>
	                     <td class="td_right" width="50" align="left">优惠券ID：</td>
	                    <td class="" width="50" align="left">
	                    	<input type="text" name="couponUserId" class="input-text lh30" size="17" value='${couponInfoQuery.couponUserId}'>
	                    </td>
	                    <td class="td_right" width="50" align="right">状态：</td>
	                    <td align="left">
	                        <div class="select_border">
	                            <div class="select_containers">
	                                <span class="fl"> 
	                                <select class="select" name="status"
	                                    style="width: 130px;">
	                                        <option value=''  <#if couponInfoQuery.status??&&couponInfoQuery.status==null>selected='selected'</#if>>全部</option> 
					                        <option value="0" <#if couponInfoQuery.status??&&couponInfoQuery.status?string=='0'>selected='selected'</#if>>可用</option> 
					                        <option value="1" <#if couponInfoQuery.status??&&couponInfoQuery.status?string=='1'>selected='selected'</#if>>已使用</option> 
					                        <option value="2" <#if couponInfoQuery.status??&&couponInfoQuery.status?string=='2'>selected='selected'</#if>>已过期</option> 
					                        <option value="3" <#if couponInfoQuery.status??&&couponInfoQuery.status?string=='3'>selected='selected'</#if>>作废</option> 
	                                </select>
	                                </span>
	                            </div>
	                        </div>
	                    </td>
	                </tr>
	                 <tr>
		                    <td class="td_right" width="50" align="left">用户账号：</td>
		                    <td class="" width="50" align="left">
		                    	<input type="text" name="userLogin" class="input-text lh30" size="17" value='${couponInfoQuery.userLogin}'>
		                    </td>
	                <#--
		                    <td class="td_right" width="50" align="left">手机号码：</td>
		                    <td class="" width="50" align="left">
		                    	<input type="text" name="number" class="input-text lh30" size="17" value='${couponInfoQuery.mobile}'>
		                    </td>
		              -->
		              </tr>
	    		 </table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
		          <div class="search_bar_btn" style="text-align:center;">
		             <input class="btn btn82 btn_res" type="reset" id="resetAtt" value="重置" >
		             <input class="btn btn82 btn_search" type="submit" id="searthAtt" value="查询" >
		          </div>
			</div>
		</div> 
	    <div id="table" class="mt10">
       	  <div class="box span10 oh">
        	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
	                <tr align="center">
	                    <th>批号</th>
	                    <th>优惠券名称</th>
	                    <th>用户账号</th>
	                    <!--<th>名字</th>-->
	                    <th>优惠券ID</th>
	                    <th>面值</th>
	                    <th>使用金额</th>
	                    <th>类型</th>
	                    <th>状态</th>
	                    <th>操作</th>
	                </tr>
	                <#if (queryAllCouponUsersByPage??)>
		               <#list queryAllCouponUsersByPage.getRows() as cdo>
		                <tr>
				              <td class="td_center">
				              	${cdo.couponId}
		                      </td>
				              <td class="td_center">
				              	${cdo.couponName}
				              </td>
				              <td class="td_center">
				              	${cdo.toUserLogin}
				              </td>
				              <!--
				              <td class="td_center">
				              	${cdo.toUserName}
				              </td>
				              -->
				              <td class="td_center">
				              	${cdo.couponUserId}
				              </td>
				              <td class="td_center">
				              	${cdo.faceValue}
				              </td>
				              <td class="td_center">
				              	${cdo.needOverMon}
				              </td>
				              <td class="td_center"><#assign sta="${cdo.couponType}" /><#if sta=='0'>满减券<#else>现金券</#if></td>
				              <td class="td_center"><#assign sta="${cdo.couponUserStatus}" /><#if sta=='0'>可用<#elseif sta=='1'>已使用<#elseif sta=='2'>已过期<#elseif sta=='3'>作废<#elseif sta=='4'>删除</#if></td> 
				              <td class="td_center">
				              	<a href="javascript:void(0);" class="viewdetailbtn" param='${cdo.couponUserId}'>[查看更多]</a>
				              	<#assign sta="${cdo.couponUserStatus}" />
				              	<#if sta=='0'>
				              		<a href="javascript:void(0);" class="canceldetailbtn" param='${cdo.couponUserId}'>[作废]</a>
				              	</#if>
							  </td>	
			             </tr>
			         </#list>
			        </#if>
              </table>
     		</div>
		</div>
	  <#--<#if (queryAllCouponUsersByPage??)>-->
		<#--<div style="margin-top:10px;width:100%">-->
			<#--<div style="float:right;padding-right:10px;">-->
				<#--<a href="javascript:void(0);" id="nextPage">下一页</a>-->
			<#--</div>-->
			<#--<div style="float:right;padding-right:10px;">-->
				<#--<a href="javascript:void(0);" id="prePage">上一页</a>-->
			<#--</div>-->
			<#--<div style="float:right;padding-right:10px;">-->
				<#--总：<span>${queryAllCouponUsersByPage.totalCount!}</span>-->
				<#--每页：-->
				<#--<#if ("50" == "${queryAllCouponUsersByPage.pageSize!}")>-->
					<#--<select id="perCount" name="pageSize" class="select">-->
						<#--<option value="30">30</option>-->
						<#--<option value="50" selected>50</option>-->
						<#--<option value="100">100</option>-->
						<#--<option value="200">200</option>-->
					<#--</select>-->
				<#--<#elseif ("100" == "${queryAllCouponUsersByPage.pageSize!}")>-->
					<#--<select id="perCount" name="pageSize" class="select">-->
						<#--<option value="30">30</option>-->
						<#--<option value="50">50</option>-->
						<#--<option value="100" selected>100</option>-->
						<#--<option value="200">200</option>-->
					<#--</select>-->
				<#--<#elseif ("200" == "${queryAllCouponUsersByPage.pageSize!}")>-->
					<#--<select id="perCount" name="pageSize" class="select">-->
						<#--<option value="30">30</option>-->
						<#--<option value="50">50</option>-->
						<#--<option value="100">100</option>-->
						<#--<option value="200" selected>200</option>-->
					<#--</select>-->
				<#--<#else>-->
					<#--<select id="perCount" name="pageSize" class="select">-->
						<#--<option value="30" selected>30</option>-->
						<#--<option value="50">50</option>-->
						<#--<option value="100">100</option>-->
						<#--<option value="200">200</option>-->
					<#--</select>-->
				<#--</#if>-->
				<#--<input type="hidden" id="pageId" name="pageId" value="${queryAllCouponUsersByPage.pageNo!}" />-->
				<#--<span id="currPage">${queryAllCouponUsersByPage.pageNo!}</span>/<span id="totalPage">${queryAllCouponUsersByPage.totalPageCount!}</span>页-->
			<#--</div>-->
		<#--</div>-->
	<#--</#if>-->
	<@pager  pagination=queryAllCouponUsersByPage  formId="couponDetailForm"  />
</form>
</@backend>