<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/forbiddenWords.js'] 
	css=[] >
	<form class="jqtransform" method="post" id="queryForm" action="${domain}/basedata/forbiddenWords/list.htm">
    <div >
	   <div>	      
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>                 
                  <td>违禁词</td>
                  <td><input type="text" name="words" class="input-text lh25" size="20" value='${forbiddenWordsDO.words}'></td>
                  <td>状态</td>
                 <td>
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="status" class="select"> 
		                    <option value=''      <#if  forbiddenWordsDO.status==null>selected='selected'</#if>>全部</option> 
	                        <option value="1"  <#if  forbiddenWordsDO.status??&&forbiddenWordsDO.status?string=='1'>selected='selected'</#if>>有效</option> 
	                        <option value="0" <#if  forbiddenWordsDO.status??&&forbiddenWordsDO.status?string=='0'>selected='selected'</#if>>无效</option> 
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
                 <input class="btn btn82 btn_search" type="submit" value="查询"/ >
                 <input class="btn btn82 btn_res " type="button" value="重置"  onclick="dataReset(this)" />
                 <input class="btn btn82 btn_add addcatabtn forbiddenWordsaddbtn" type="button" value="新增" />
              </div>
        </div>
    </div>
    
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">ID</th>
	                  <th width="100">违禁词编号</th>
	                  <th width="150">违禁词</th>
	                  <th width="150">状态</th>
	                  <th width="100">创建时间</th>
	                  <th width="100">更新时间</th>
					  <th width="100">备注</th>
					  <th width="100">操作</th>
                </tr>
            <#list queryAllForbiddenWordsByPage.rows as forbiddenWords>
                <tr class="tr" >
		              <td class="td_center">${forbiddenWords.id}
                      <input type="hidden" value=${forbiddenWords.id} />
                      </td>
		              <td class="td_center">${forbiddenWords.code}</td>
 					  <td class="td_center">${forbiddenWords.words}</td>
		              <td class="td_center"><#assign sta="${forbiddenWords.status}" /><#if sta=='1'>有效<#else>无效</#if></td>		            
		              <td class="td_center">${forbiddenWords.createdTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                      <td class="td_center">${forbiddenWords.modifyTime?string("yyyy-MM-dd HH:mm:ss")}</td>  
                      <td class="td_center">${forbiddenWords.remark}</td>					  
		              <td class="td_center"><a href="javascript:void(0);"  class="editcatabtn forbiddenWordseditbtn" param='${forbiddenWords.id}'>[编辑]</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                                    <a href="javascript:void(0);"  class="editcatabtn journalReview" param='${forbiddenWords.id}'>[日志]</a></td>	
	             </tr>
	            </#list>
              </table>
	     </div>
	</div>
	<@pager  pagination=queryAllForbiddenWordsByPage  formId="queryForm"  />  
   </form>
</@backend>