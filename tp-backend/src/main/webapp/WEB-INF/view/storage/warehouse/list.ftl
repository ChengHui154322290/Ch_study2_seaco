<#include "/common/common.ftl"/>
<@backend title="仓库管理" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/storage/js/warehouselist.js'
] 
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >

<form method="post" action="${domain}/storage/warehouse/list.htm" id="wareHouseForm">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">仓库管理列表页面</b></div>
            <div class="box_center pt10 pb10">
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>供应商名称：</td>
                  <td>
                	  	<input type="text" name="spName" value="${warehouse.spName}" class="input-text lh25" size="20">
                  </td>
                  <td>供应商编号：</td>
                  <td>
                	  	<input type="text" name="spId" value="${warehouse.spId}" class="input-text lh25" size="20">
                  </td>                  
                  <td>仓库编号：</td>
                  <td>
                	  	<input type="text" name="code" value="${warehouse.code}" class="input-text lh25" size="20">
                  </td>
                  <td>仓库ID：</td>
                  <td>
                	  	<input type="text" name="id" value="${warehouse.id}" class="input-text lh25" size="20">
                  </td>
                </tr>
                <tr>
                  <td>WMS编号：</td>
                  <td>
                	  	<input type="text" name="wmsCode" value="${warehouse.wmsCode}" class="input-text lh25" size="20">
                  </td>
                  <td>主仓库：</td>
                  <td>
                	  	<select id="mainType" name="mainType" class="select">
                	  		<option value=''>请选择</option>
                	  		<option value='0' <#if warehouse.mainType==0>selected="selected"</#if>>否</option>
                	  		<option value='1' <#if warehouse.mainType==1>selected="selected"</#if>>是</option>
                	  	</select>
                  </td>
                  <td>主仓库ID：</td>
                  <td>
                	  	<input type="text" name="mainWarehouseId" value="${warehouse.mainWarehouseId}" class="input-text lh25" size="20">
                  </td>
                </tr>
              </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="$('#wareHouseForm').submit();" type="button" value="查询" name="button" /></a>
                 <a href="add.htm">
                 	<input class="btn btn82 btn_add warehouseaddbtn" type="button" value="新增" name="button"
                 		onclick='window.open(this.parentNode.href,"_self");'  />
                 </a>
              </div>
            </div>
          </div>
        </div>
    </div>
   <div id="table" class="mt10">
    	<div class="box span10 oh">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table" id="wareHouseDODataList">
            <tr>
           	   <th width="150">供应商名称</th>
           	   <th width="100">商家编号</th>
               <th width="100">机构</th>
               <th width="100">仓库ID</th>
               <th width="200">仓库编号</th>
               <th width="300">仓库名称</th>
               <th width="300">仓库地址</th>
               <th width="150">联系电话</th>
               <!--<th width="200">退货信息</th> -->
               <th width="80">编辑</th>
            </tr>
            <#if page.rows?default([])?size !=0>
            <#list page.rows as wareHouseDO>
	             <tr>
	               <td style="display:none">${wareHouseDO.id} </td>
	               <td width="150">${wareHouseDO.spName}</td>
	               <td width="100">${wareHouseDO.spId}</td>
	               <td width="100">${wareHouseDO.districtName}</td>
	               <td width="50">${wareHouseDO.id}</td>
	               <td width="200">${wareHouseDO.code}</td>
	               <td width="300">${wareHouseDO.name}</td>
	               <td width="300">${wareHouseDO.address}</td>
	               <td width="150">${wareHouseDO.phone}</td>
	               <!--<td width="200">${wareHouseDO.statusDesc}</td> -->
	               <td width=80">
	               <a href="edit.htm?id=${wareHouseDO.id}" name="id" class="editcatabtn brandeditbtn" param='${wareHouseDO.id}' onclick='window.open(this.parentNode.href,"_self");'>[编辑]</a>
	               <a href="#" id="deleteWareHourse"  param='${wareHouseDO.id}' >[删除]</a>
	               </td>
	            </tr>
            </#list>
            </#if>
          </table>
    	</div>
</div>
   <@pager  pagination=page  formId="wareHouseForm" /> 
</form>

</@backend>