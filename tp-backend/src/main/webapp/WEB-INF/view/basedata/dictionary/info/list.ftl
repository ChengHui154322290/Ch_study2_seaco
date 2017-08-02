<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/dictionaryInfo.js'] 
	css=[] >
<form class="jqtransform" method="post" id="queryForm" action="${domain}/basedata/dictionary/info/list.htm">
    <div >
	   <div>	      
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>名称</td>
                  <td><input type="text" name="name" id="name" class="input-text lh25" size="20" value="${dictionaryInfoDO.name}"></td>
                  <td>商业排序号</td>
                  <td><input type="text" name="sortNo" id="sortNo" class="input-text lh25" size="20" value="${dictionaryInfoDO.sortNo}"></td>
                  <td class="td_right">分类列表:</td>
	              <td>
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="code" id="code" class="select"> 
                         <option value="">---全部分类---</option> 
                         <#if cataGoryInfoMap?exists>
			                <#list cataGoryInfoMap?keys as key> 
			                  <option value="${key}"  <#if key==dictionaryInfo.code>selected='selected'</#if>>${cataGoryInfoMap[key]}</option> 
			                </#list>
			            </#if>
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
                 <input class="btn btn82 btn_search" type="submit" value="查询" />
                  <input class="btn btn82 btn_res " type="button" value="重置"  onclick="dataReset(this)" />
                 <input class="btn btn82 btn_add addcatabtn dictionaryInfoaddbtn" type="button" value="新增" />
              </div>
        </div>
    </div>
    
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">ID</th>
	                  <th width="100">名称</th>
	                  <th width="100">code码</th>
	                  <th width="150">所属类别名称</th>
	                  <th width="150">商业排序号</th>
	                  <th width="100">创建时间</th>
	                  <th width="100">更新时间</th>
					  <th width="100">操作</th>
                </tr>
            <#list queryAllDictionaryInfoByPage.rows as dictionaryInfo>
                <tr class="tr" >
		              <td class="td_center">${dictionaryInfo.id}
                        <input type="hidden" value=${dictionaryInfo.id} />
                      </td>
		              <td class="td_center">${dictionaryInfo.name}</td>
		              <td class="td_center">${dictionaryInfo.code}</td>
		              <td class="td_center">	        
			              <#if cataGoryInfoMap?exists>
				                <#list cataGoryInfoMap?keys as key> 
				                   <#if dictionaryInfo.code==key>${cataGoryInfoMap[key]}</#if>
				                </#list>
				          </#if>	
			          </td>
					  <td class="td_center">${dictionaryInfo.sortNo}</td>
		              <td class="td_center">${dictionaryInfo.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                      <td class="td_center">${dictionaryInfo.modifyTime?string("yyyy-MM-dd HH:mm:ss")}</td>  				  
		              <td class="td_center"><a href="javascript:void(0);"  class="editcatabtn dictionaryInfoeditbtn" param='${dictionaryInfo.id}'>[编辑]</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="">[日志]</a></td>	
	             </tr>
	        </#list>
              </table>
	     </div>
	</div>
	<@pager  pagination=queryAllDictionaryInfoByPage  formId="queryForm"  />  
	</form>
</@backend>