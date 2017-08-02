<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/spec.js'] 
	css=[] >
<form class="jqtransform" method="post" id="queryForm" action="${domain}/basedata/specGroup/spec/list.htm">
    <div >
	   <div>	      
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>规格名称</td>
                  <td><input type="text" name="spec" class="input-text lh25" size="20" value=${spec.spec}></td>
                  <td>规格状态</td>
                 <td>
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="status" class="select"> 
		                    <option value=''      <#if spec.status==null>selected='selected'</#if>>全部</option> 
	                        <option value="1"  <#if spec.status??&&spec.status?string=='1'>selected='selected'</#if>>有效</option> 
	                        <option value="0" <#if spec.status??&&spec.status?string=='0'>selected='selected'</#if>>无效</option> 
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
                 <input class="btn btn82 btn_search" type="submit" value="查询"/>
                 <input class="btn btn82 btn_res " type="button" value="重置"  onclick="dataReset(this)" />
                 <input class="btn btn82 btn_add addcatabtn specaddbtn" type="button" value="新增" />
              </div>
        </div>
    </div>
    
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">规格表ID</th>
	                  <th width="100">规格编号</th>
	                  <th width="100">规格</th>
	                  <th width="150">状态</th>
	                  <th width="100">创建时间</th>
	                  <th width="100">更新时间</th>
					  <th width="100">备注</th>
					  <th width="100">操作</th>
                </tr>
            <#list queryAllSpecByPage.rows as spec>
                <tr class="tr" >
		              <td class="td_center">${spec.id}
                      <input type="hidden" value=${spec.id} />
                      </td>
		              <td class="td_center">${spec.code}</td>
		              <td class="td_center">${spec.spec}</td>
		              <td class="td_center"><#assign sta="${spec.status}" /><#if sta=='1'>有效<#else>无效</#if></td>		            
		              <td class="td_center">${spec.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                      <td class="td_center">${spec.modifyTime?string("yyyy-MM-dd HH:mm:ss")}</td>  
                      <td class="td_center">${spec.remark}</td>					  
		              <td class="td_center"><a href="javascript:void(0);"  class="editcatabtn speceditbtn" param='${spec.id}'>[编辑]</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0);">[日志]</a></td>	
	             </tr>
	            </#list>
              </table>
	     </div>
	</div>
	 <@pager  pagination=queryAllSpecByPage  formId="queryForm"  />  
	</form>
</@backend>