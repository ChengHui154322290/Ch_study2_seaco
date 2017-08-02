<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/specGroup.js'] 
	css=[] >
 <form class="jqtransform" method="post" id="queryForm" action="${domain}/basedata/specGroup/group/list.htm">
    <div >
	   <div>	      
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>规格组名称</td>
                  <td><input type="text" name="name" class="input-text lh25" size="20"  value='${specGroup.name}'></td>
                  <td>规格组状态</td>
                 <td>
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                       <select name="status" class="select"> 
                            <option value=''      <#if  specGroup.status==null>selected='selected'</#if>>全部</option> 
	                        <option value="1"  <#if specGroup.status??&&specGroup.status?string=='1'>selected='selected'</#if>>有效</option> 
	                        <option value="0" <#if specGroup.status??&&specGroup.status?string=='0'>selected='selected'</#if>>无效</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                </tr>
              </table>
	   </div>
	
	  <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:center;">
                 <input class="btn btn82 btn_search" type="submit" value="查询" name="button">
                 <input class="btn btn82 btn_res " type="button" value="重置"  onclick="dataReset(this)" />
                 <input class="btn btn82 btn_add addcatabtn specGroupaddbtn" type="button" value="新增" name="button">
              </div>
        </div>
    </div>
    
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">规格组表ID</th>
	                  <th width="100">规格组编号</th>
	                  <th width="100">规格组</th>
	                  <th width="150">状态</th>
	                  <th width="100">创建时间</th>
	                  <th width="100">更新时间</th>
					  <th width="100">备注</th>
					  <th width="100">操作</th>
                </tr>
            <#list queryAllSpecGroupByPage.rows as specGroup>
                <tr class="tr" >
		              <td class="td_center">${specGroup.id}
                      <input type="hidden" value=${specGroup.id} />
                      </td>
		              <td class="td_center">${specGroup.code}</td>
		              <td class="td_center">${specGroup.name}</td>
		              <td class="td_center"><#assign sta="${specGroup.status}" /><#if sta=='1'>有效<#else>无效</#if></td>		            
		              <td class="td_center">${specGroup.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                      <td class="td_center">${specGroup.modifyTime?string("yyyy-MM-dd HH:mm:ss")}</td>  
                      <td class="td_center">${specGroup.remark}</td>					  
		              <td class="td_center">
		              <a href="javascript:void(0);"  class="editcatabtn specGroupViewbtn" param='${specGroup.id}'>[查看]</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		              <a href="javascript:void(0);"  class="editcatabtn specGroupeditbtn" param='${specGroup.id}'>[编辑]</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		              <a href="javascript:void(0);">[日志]</a></td>	
	             </tr>
	            </#list>
              </table>
	     </div>
	</div>
	 <@pager  pagination=queryAllSpecGroupByPage  formId="queryForm"  />  
	</form>
</@backend>