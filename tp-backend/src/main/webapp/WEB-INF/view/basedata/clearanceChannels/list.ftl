<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/clearanceChannels.js'] 
	css=[] >
 <form class="jqtransform" method="post" id="queryForm" action="${domain}/basedata/clearanceChannels/list.htm">
   <!-- <div >  -->
	  <!--  <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;"> -->
        <!--        <div class="search_bar_btn" style="text-align:center;">-->
           <!--        <input class="btn btn82 btn_add addcatabtn clearanceChanneladdbtn" type="button" value="新增" name="button">-->
           <!--     </div>-->
        <!--  </div>-->
     <!-- </div>-->
    
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">通关渠道ID</th>
	                  <th width="100">通关渠道编号</th>
	                  <th width="100">名称</th>
	                  <th width="100">通关状态</th>
	                  <th width="100">创建时间</th>
	                  <th width="100">更新时间</th>
					  <th width="100">备注</th>
					<!--  <th width="100">操作</th> -->
                </tr>
            <#list listOfClearanceChannels as clearanceChannel>
                <tr class="tr" >
		              <td class="td_center">${clearanceChannel.id}
                      <input type="hidden" value=${clearanceChannel.id} />
                      </td>
		              <td class="td_center">${clearanceChannel.code}</td>
		              <td class="td_center">${clearanceChannel.name}</td>
		              <td class="td_center"><#if clearanceChannel.status=='true'>已对接<#else>未对接</#if></td>		            
		              <td class="td_center">${clearanceChannel.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                      <td class="td_center">${clearanceChannel.modifyTime?string("yyyy-MM-dd HH:mm:ss")}</td>  
                      <td class="td_center">${clearanceChannel.remark}</td>					  
		            <!--  <td class="td_center"><a href="javascript:void(0);"  class="editcatabtn clearanceChanneleditbtn" param='${clearanceChannel.id}'>[编辑]</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="">[日志]</a></td> -->	
	             </tr>
	            </#list>
              </table>
	     </div>
	</div>
	</form>
</@backend>