<#include "/common/common.ftl"/> 
<#include "/coupon/common/page.ftl" />
<@backend title="优惠券列表" 
    js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/coupon/coupon.js']  
    css=[] >
<form class="jqtransform" method="post" id="queryAttForm" action="${domain}/coupon/list.htm">
		   <div >
			   <div>	
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td class="td_right" width="50" align="left">批次：</td>
                                <td class="" width="50" align="left"><input type="text"
                                    name="id" class="input-text lh30" value='${couponDO.id}'></td> 

                                <td class="td_right" width="50" align="left">名称：</td>
                                <td class="" width="50" align="left"><input type="text"
                                    name="couponName" class="input-text lh30" size="17" value='${couponDO.couponName}'></td>
                                    
                                <td class="td_right" width="50" align="right">状态：</td>
                                <td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> 
                                            <select class="select" name="status"
                                                style="width: 130px;">
                                                    <option value=''  <#if couponDO.status??&&couponDO.status==null>selected='selected'</#if>>全部</option> 
							                        <option value="0" <#if couponDO.status??&&couponDO.status?string=='0'>selected='selected'</#if>>编辑中</option> 
							                        <option value="1" <#if couponDO.status??&&couponDO.status?string=='1'>selected='selected'</#if>>审核中</option> 
							                        <option value="2" <#if couponDO.status??&&couponDO.status?string=='2'>selected='selected'</#if>>已取消</option> 
							                        <option value="3" <#if couponDO.status??&&couponDO.status?string=='3'>selected='selected'</#if>>审核通过</option> 
							                        <option value="4" <#if couponDO.status??&&couponDO.status?string=='4'>selected='selected'</#if>>已驳回</option> 
							                        <option value="5" <#if couponDO.status??&&couponDO.status?string=='5'>selected='selected'</#if>>已终止</option> 
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                                
                                <td class="td_right" width="50" align="right">适用海淘：</td>
                                <td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> 
                                            <select class="select" name="hitaoSign"
                                                style="width: 130px;">
                                                    <option value=''  <#if couponDO.hitaoSign??&&couponDO.hitaoSign==null>selected='selected'</#if>>全部</option> 
							                        <option value="1" <#if couponDO.hitaoSign??&&couponDO.hitaoSign?string=='1'>selected='selected'</#if>>海淘</option> 
							                        <option value="2" <#if couponDO.hitaoSign??&&couponDO.hitaoSign?string=='2'>selected='selected'</#if>>非海淘</option> 
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                                
                                <td class="td_right" width="50" align="right">适用平台：</td>
                                <td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> 
	                                            <select class="select" name="usePlantform"
	                                                style="width: 130px;">
	                                                    <option value=''  <#if couponDO.usePlantform??&&couponDO.usePlantform==null>selected='selected'</#if>>全网</option> 
								                        <option value="1" <#if couponDO.usePlantform??&&couponDO.usePlantform?string=='1'>selected='selected'</#if>>PC</option> 
								                        <option value="2" <#if couponDO.usePlantform??&&couponDO.usePlantform?string=='2'>selected='selected'</#if>>APP</option> 
								                        <option value="3" <#if couponDO.usePlantform??&&couponDO.usePlantform?string=='3'>selected='selected'</#if>>WAP</option> 
	                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                                
                            </tr>
                            <tr>
                          		 <td class="td_right" width="50" align="right">发券主体：</td>
                                <td align="left" colspan="9">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> 
                                             <select class="select" name="sourceType" style="width: 80px;">
                                                    <option value=''  <#if couponDO.sourceType??&&couponDO.sourceType==null>selected='selected'</#if>>全部</option> 
							                        <option value="1" <#if couponDO.sourceType??&&couponDO.sourceType?string=='1'>selected='selected'</#if>>西客商城</option>
							                        <option value="2" <#if couponDO.sourceType??&&couponDO.sourceType?string=='2'>selected='selected'</#if>>商户</option> 
                                            </select>
                                            <span><input type="text" name="sourceName" class="input-text lh30" size="17" value='${couponDO.sourceName}'></span>
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
                                            <th>批次</th>
                                            <th>名称</th>
											<th>编码</th>
                                            <th>备注</th>
                                            <th>类型</th>
                                            <th>状态</th>
                                            <th>操作</th>
                                        </tr>
						               <#list queryAllCouponByPage.getRows() as cdo>
						                <tr class="tr" >
								              <td class="td_center">${cdo.id}
						                      <input type="hidden" value=${cdo.id} />
						                      </td>
								              <td class="td_center">${cdo.couponName}</td>
								              <td class="td_center">${cdo.code}</td>
								              <td class="td_center">${cdo.remark}</td>
								              <td class="td_center"><#assign sta="${cdo.couponType}" /><#if sta=='0'>满减券<#else>现金券</#if></td>
								              <td class="td_center coupon_status">
								              		<#assign sta="${cdo.status}" />
								              		<#if sta=='0'>编辑中
								              		<#elseif sta=='1'>审核中
								              		<#elseif sta=='2'>已取消
								              		<#elseif sta=='3'>审核通过
								              		<#elseif sta=='4'>已驳回
								              		<#elseif sta=='5'>已终止
								              		</#if>
								              		<#if cdo.activeStatus==0>-未激活<#else>-已激活</#if>
								              </td> 
								              <td class="td_center" style="text-align:left">
								              		<#if (sta == 0 || sta == 3 || sta == 4)>
														<a style="padding-right:5px;"  class="editcatabtn couponeditbtn" param='${cdo.id}'  href="javascript:void(0);">[编辑]</a>
														<#if cdo.couponType=1><a style="padding-right:5px;"  class="couponactivebtn" param='${cdo.id}'  activestatus="${cdo.activeStatus}" href="javascript:void(0);">[<#if cdo.activeStatus==0>激活<#else>制卡</#if>]</a></#if>
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
													<#if (sta == 3)>
														<a style="padding-right:5px;"  class="coupon_stop" param='${cdo.id}'  href="javascript:void(0);">[终止]</a>
													</#if>
								              		<a style="padding-right:5px;"  class="editcatabtn couponviewbtn" param='${cdo.id}' href="javascript:void(0);">[详细]</a>
								              
									              	<!-- <a href="javascript:void(0);"  class="editcatabtn couponeditbtn" param='${cdo.id}'>编辑</a> -->
									                <!-- <#if sta=='0'><a href="javascript:void(0);"  class="coupon_invalid" param='${cdo.id}'>设为无效</a></#if>-->
								              </td>	
							             </tr>
							         </#list>
                     </table>
	     	</div>
		</div>
   <@pager  pagination=queryAllCouponByPage  formId="queryAttForm"  />  
  </form>
</@backend>
