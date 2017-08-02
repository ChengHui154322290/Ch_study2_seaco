<#include "/common/common.ftl"/> 
<@backend title="优惠券发放" 
			js=['/statics/cms/js/masterplan/couponSearch.js']
			css=[]>
<form class="jqtransform" method="post" id="queryAttForm" action="${domain}/cms/searchCoupon.htm">
   <div id="applay_pop">
	   <div>	
            <table cellspacing="0" cellpadding="0" border="0" width="100%"
                class="form_table pt15 pb15">
                <tbody>
                    <tr>
                        <td class="td_right" width="50" align="left">批次编号：</td>
                        <td class="" width="50" align="left">
                        	<input type="text" name="id" class="input-text lh30" value='${couponDO.id}'></td> 

                        <td class="td_right" width="50" align="left">名称：</td>
                        <td class="" width="50" align="left">
                        	<input type="text" name="couponName" class="input-text lh30" size="17" value='${couponDO.couponName}'>
                        </td>
                    </tr>
        		 </table>
			 </div>
		  	<div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:center;">
                 <input class="btn btn82 btn_search" type="submit" id="searthAtt" value="查询" >
              </div>
       		</div>
   		 </div> 
	
	    <div id="table" class="mt10">
       	  <div class="box span10 oh">
        	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr align="center">
                    <th>选择</th>
                    <th>批次</th>
                    <th>名称</th>
                    <th>类型</th>
                </tr>
                <#if (queryAllCouponByPage??)>
	               <#list queryAllCouponByPage.getList() as cdo>               
		                <tr>
							<td class="td_center" style="width:30px;text-align:center;">
								<input type="radio" name="couponId" value=${cdo.id} />
							</td>
							<td style="width:80px;text-align:center;">
								${cdo.id}
							</td>
							<td class="td_center couponName">${cdo.couponName}</td>
							<td class="td_center"><#assign sta="${cdo.couponType}" /><#if sta=='0'>满减券<#else>现金券</#if></td>
			             </tr>
		        	 </#list>
		        </#if>
             </table>
     	  </div>
		</div>
	   	<#if (queryAllCouponByPage??)>
			<div style="margin-top:10px;width:100%;height:30px;">
				<div style="float:right;padding-right:10px;">
					<a href="javascript:void(0);" id="nextPage">下一页</a>
				</div>
				<div style="float:right;padding-right:10px;">
					<a href="javascript:void(0);" id="prePage">上一页</a>
				</div>
				<div style="float:right;padding-right:10px;">
					总：<span>${queryAllCouponByPage.totalCount!}</span>
					每页：
					<#if ("20" == "${queryAllCouponByPage.pageSize!}")>
						<select id="perCount" name="pageSize">
							<option value="10">10</option>
							<option value="20" selected>20</option>
						</select>
					<#else>
						<select id="perCount" name="pageSize">
							<option value="10" selected>10</option>
							<option value="20">20</option>
						</select>
					</#if>
					<input type="hidden" id="pageId" name="pageId" value="${queryAllCouponByPage.pageNo!}" />
					<span id="currPage">${queryAllCouponByPage.pageNo!}</span>/<span id="totalPage">${queryAllCouponByPage.totalPageCount!}</span>页
				</div>
			</div>
		</#if>
	   <div>
	   		<input type="hidden" id="selectRow" name="selectRow" value="${selectRow}" />
			<div class="search_bar_btn" style="text-align:center;">
				<input type="button" class="btn btn82 btn_save2" id="selectedRd" value="选择" />
				<input type="button" class="btn btn82 btn_del closebtn" id="cancelRd" value="取消" />
			</div>
	   </div>
  </form>
</@backend>