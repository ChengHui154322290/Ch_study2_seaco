<#include "/common/common.ftl"/>
<@backend title="快递公司信息管理" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/basedata/express.js'
] css=[]>

	<form class="jqtransform" method="post" id="expressFrom" action="${domain}/basedata/express/list.htm">
	<div id="search_bar" class="mt10">
		<div class="box">
		
			<div class="box_border">
				<div class="box_top"><b class="pl15">快递公司管理列表页面</b></div>
				
	            <div class="box_center pt10 pb10">
	              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
	              	<tr>
	              	<!--
		              	<td>编号：</td>
		              	<td><input class="input-text lh25" type="text" id="expressId" name="id" value="${expressInfo.id}"/></td>
		              -->
		              	<td>快递公司名称：</td>
		              	<td><input class="input-text lh25" type="text" id="expressName" name="name" value="${expressInfo.name}"/></td>
		              	<td>code</td>
		              	<td><input class="input-text lh25" type="text" id="expressCode" name="code" value="${expressInfo.code}"/></td>
	              	</tr>
	              </table>
	            </div>
	            
	            <span id="nameInfo" class="error"></span>
           
	            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
	              <div class="search_bar_btn" style="text-align:center;">
		              <input class="btn btn82 btn_search" id="searthAtt" type="submit" value="查询" />
		              <input class="btn btn82 btn_res " type="button" value="重置"  onclick="dataReset(this)" />
		              <input id="expressAddBtn" class="btn btn82 btn_add " type="button" value="新增" name="button"/>
	              </div>
	            </div>
	            
	        </div>
	        
	        <div class="box_center pt10 pb10">
	            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table" >
	            	<tr>
	            		   <th width="100">编号</th>
		                   <th width="100">快递公司名称</th>
		                   <th width="100">code</th>
		                   <th width="100">排序值(sort_no)</th>
		                   <th width="100">操作</th>
	            	</tr>
	            	<#if page.rows?default([])?size !=0>
	            		<#list page.rows as expressInfo>
	            		<tr class="tr">
		            		<td width="100">${expressInfo.id}</td>
		            		<td width="100">${expressInfo.name}</td>
		            		<td width="100">${expressInfo.code}</td>
		            		<td width="100">${expressInfo.sortNo}</td>
		            		<td width="100">
			            		<a href="javascript:void(0)" class="editexpressInfo" param="${expressInfo.id}">[编辑]</a>&nbsp;
			            		<a href="javascript:void(0)" class="journalReview" param="${expressInfo.id}">[日志]</a>
		            		</td>
	            		</tr>
	            		</#list>
	            	</#if>
	            </table>
	        </div>
	        <div class="box" style="font-size:16px;boder:1px">
    			<#if noRecoders??>${noRecoders}</#if>
    		</div>
	        
		</div>
	</div>
	
	<@pager  pagination=page  formId="expressFrom" />
	
	</form>
</@backend>