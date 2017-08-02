<#include "/common/common.ftl"/> 
<#include "/coupon/common/page.ftl" />
<@backend title="优惠券发放列表" 
    js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/coupon/coupon_send.js']  
    css=[] >
<form class="jqtransform" method="post" id="queryAttForm" action="${domain}/coupon/sendList.htm">
		   <div >
			   <div>	
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td class="td_right" width="50" align="left">编号：</td>
                                <td class="" width="50" align="left">
                                <input type="text"  name="id" class="input-text lh30" value='${CouponSend.id}'></td> 

                                <td class="td_right" width="50" align="left">名称：</td>
                                <td class="" width="50" align="left">
                                <input type="text" name="name" class="input-text lh30" size="17" value='${CouponSend.name}'></td>
                                    
                                <td class="td_right" width="50" align="right">状态：</td>
                                <td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl">
                                            <select class="select" name="status"
                                                style="width: 130px;">
                                                    <option value=''  <#if CouponSend.status??&&CouponSend.status==null>selected='selected'</#if>>全部</option> 
							                        <option value="0" <#if CouponSend.status??&&CouponSend.status?string=='0'>selected='selected'</#if>>编辑中</option> 
							                        <option value="1" <#if CouponSend.status??&&CouponSend.status?string=='1'>selected='selected'</#if>>审核中</option> 
							                        <option value="2" <#if CouponSend.status??&&CouponSend.status?string=='2'>selected='selected'</#if>>已取消</option> 
							                        <option value="3" <#if CouponSend.status??&&CouponSend.status?string=='3'>selected='selected'</#if>>审核通过</option> 
							                        <option value="4" <#if CouponSend.status??&&CouponSend.status?string=='4'>selected='selected'</#if>>已驳回</option> 
							                        <option value="5" <#if CouponSend.status??&&CouponSend.status?string=='5'>selected='selected'</#if>>已终止</option> 
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                                
                            </tr>
                		 </table>
	  				 </div>
	  				 
	  				 
	  				 
		  	<div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
	              <div class="search_bar_btn" style="text-align:right;">
	                 <input class="btn btn82 btn_search" type="submit" id="searthAtt" value="查询" >
	                 <input class="btn btn82 btn_add  couponAddBtn" type="button" value="新增" >
	              </div>
	       		</div>
	   	 </div> 
	
	    <div id="table" class="mt10">
       	  <div class="box span10 oh">
                	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                                        <tr align="center">
                                            <th>编号</th>
                                            <th>批次</th>
                                            <th>名称</th>
                                            <th>张数</th>
                                            <th>状态</th>
                                            <th>操作</th>
                                        </tr>
						               <#list queryAllCouponSendByPage.getRows() as cdo>
						                <tr class="tr" >
								              <td class="td_center">${cdo.id}
						                      <input type="hidden" value=${cdo.id} />
						                      </td>
								              <td class="td_center">${cdo.couponIds}</td>
								              <td class="td_center">${cdo.name}</td>
								              <td class="td_center"><#assign to_all="${cdo.toAll}" /> <#if (to_all ==1)>全体用户  <#else>  ${cdo.sendSize} </#if></td>
								              <td class="td_center coupon_status">
								              		<#assign sta="${cdo.status}" />
								              		<#if sta=='0'>编辑中
								              		<#elseif sta=='1'>审核中
								              		<#elseif sta=='2'>已取消
								              		<#elseif sta=='3'>审核通过
								              		<#elseif sta=='4'>已驳回
								              		<#elseif sta=='5'>已终止
								              		</#if>
								              </td> 
								              <td class="td_center" style="text-align:left">
								              		<#if (sta == 0 || sta == 4)>
														<a style="padding-right:5px;"  class="editcatabtn couponeditbtn" param='${cdo.id}'  href="javascript:void(0);">[编辑]</a>
													</#if>
													<#if (sta == 0 || sta == 4)>
														<a style="padding-right:5px;"  class="coupon_cancel" param='${cdo.id}'  href="javascript:void(0);">[取消]</a>
													</#if>
													<#if (sta == 1)>
														<a style="padding-right:5px;" class="coupon_approve" param='${cdo.id}'  href="javascript:void(0);">[批准]</a>
													</#if>
													<#if (sta == 1)>
														<a style="padding-right:5px;"   class="coupon_refused" param='${cdo.id}'  href="javascript:void(0);">[驳回]</a>
													</#if>
													<!--
													<#if (sta == 3)>
														<a style="padding-right:5px;"  class="coupon_stop" param='${cdo.id}'  href="javascript:void(0);">[终止]</a>
													</#if>
													-->
								              		<a style="padding-right:5px;"  class="editcatabtn couponviewbtn" param='${cdo.id}' href="javascript:void(0);">[详细]</a>
								              
								              </td>	
							             </tr>
							         </#list>
                     </table>
	     	</div>
		</div>
   <@pager  pagination=queryAllCouponSendByPage  formId="queryAttForm"  />  
  </form>
</@backend>
