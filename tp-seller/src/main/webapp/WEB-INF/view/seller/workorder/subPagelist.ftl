<#include "/commons/base/page.ftl" />
		<div class="box span10 oh">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
            <tr>
               <th width="200">订单编号</th>
           	   <th width="200">创建时间</th>
           	   <th width="200">供应商处理方式</th>
               <th width="200">工单状态</th>
               <th width="200">回复状态</th>
               <th width="200">业务类型</th>
               <th width="300">操作</th>
            </tr>
            <#if page.list?default([])?size !=0>
            <#list page.list as workorder>
             <tr>
               <td>${workorder.orderNo}</td>
               <td>${workorder.createDate?string('yyyy-MM-dd HH:mm:ss')}</td>
			   <td>${workorder.cnSupplierMethodId}</td>
               <td>${workorder.cnStatus}</td>
               <td>${workorder.cnTranslateStatus}</td>
               <td>${workorder.cnBizType}</td>
               <td class="search_bar_btn">
               	<!--<#if workorder.auditStatus == 1 >
               	<a class="queryworkorderinfobtn" href="javascript:void(0);" workorderid="${workorder.id}" workorderno="${workorder.workorderNo}" onclick="showDetailPage('${workorder.id}')" >[审核]</a> 
               	</#if>-->
				<a class="queryworkorderinfobtn" href="javascript:void(0);" workorderid="${workorder.id}" workorderno="${workorder.workorderNo}" onclick="showDetailPage('${workorder.id}')" operate="show">[查看]</a>
				<#if workorder.auditStatus != 2&&workorder.status != 3&&workorder.status != 4>
				<a class="queryworkorderinfobtn" href="javascript:void(0);" workorderid="${workorder.id}" workorderno="${workorder.workorderNo}" onclick="showInsertLogPage('${workorder.id}')" operate="auditshow">[处理]</a>
			    </#if>
			   </td>
              </tr>
            </#list>
            </#if>
	        </table>
	          <#if page?exists>
				<@p page=page.pageNo totalpage=page.getTotalPageCount() />
				</#if>
	    	</div>
	   <script>
	   function getDay(datesrc){
			var temp=datesrc.split(" ");
			var t1=temp[0].split("-");
			var t2=temp[1].split(":");
			var date=new Date(t1[0],t1[1]-1,t1[2],t2[0],t2[1],t2[2]);
			var nowTime=new Date();
			var daylong=date.getTime();
			var day=24*3600*1000;
			var res=Math.floor((nowTime.getTime()-daylong)/(24*3600*1000));
			return res;
		}
	   	$(function (){
	   		$(".list_table tr").each(function(i){
		   		if(i!=0){
				var	cnStatus=$(this).find("td:eq(3)").text();
				var	createTime=$(this).find("td:eq(1)").text();
				var dayNum=getDay(createTime);
				
					if(cnStatus=="强制关闭"){
						$(this).css("color","green");
					}else if(cnStatus=="已关闭"){
						$(this).css("color","black");
					}else if(dayNum>=2){
						$(this).css("color","red");
					}
				}
			});
	   	});
	   		
	   </script>