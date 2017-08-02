<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/advertPlat.js'] 
	css=[] >
	<form class="jqtransform" method="post" id="queryForm" action="${domain}/basedata/advertPlat/list.htm">
    <div >
	   <div>	      
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>                 
                  <td>平台名称</td>
                  <td><input type="text" name="advertPlatName" class="input-text lh25" size="20" value='${advertPlatform.advertPlatName}'></td>
                  <td>平台编码</td>
                   <td>
                       <input type="text" name="advertPlatCode" class="input-text lh25" size="20" value='${advertPlatform.advertPlatCode}'></td>
                  </td>
                </tr>
              </table>
	   </div>
	
	  <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:center;">
                 <input class="btn btn82 btn_search" type="submit" value="查询"/ >
                 <input class="btn btn82 btn_res " type="button" value="重置"  onclick="dataReset(this)" />
                 <input class="btn btn82 btn_add addcatabtn advertPlataddbtn" type="button" value="新增" />
              </div>
        </div>
    </div>
    
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">ID</th>
	                  <th width="100">平台名称</th>
	                  <th width="150">平台编码</th>
					  <th width="100">操作</th>
                </tr>
            <#list queryAlladvertPlatByPage.rows as advertPlat>
                <tr class="tr" >
		              <td class="td_center">${advertPlat.id}
                      	<input type="hidden" value=${advertPlat.id} />
                      </td>
		              <td class="td_center">${advertPlat.advertPlatName}</td>
 					  <td class="td_center">${advertPlat.advertPlatCode}</td>
		              <td class="td_center"><a href="javascript:void(0);"  class="editcatabtn advertPlateditbtn" param='${advertPlat.id}'>[编辑]</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		              </td>	
	             </tr>
	            </#list>
              </table>
	     </div>
	</div>
	<@pager  pagination=queryAlladvertPlatByPage  formId="queryForm"  />  
   </form>
</@backend>