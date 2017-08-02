<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/dictionaryCategory.js'] 
	css=[] >
<form class="jqtransform" method="post" id="queryForm" action="${domain}/basedata/dictionary/category/list.htm">
  
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">ID</th>
	                  <th width="100">类别码</th>
	                  <th width="100">类别名称</th>
	                  <th width="150">排序</th>
	                  <th width="100">创建时间</th>
	                  <th width="100">更新时间</th>
                </tr>
            <#list queryAllDictionaryCategoryByPageInfo.rows as dictionaryCategory>
                <tr class="tr" >
		              <td class="td_center">${dictionaryCategory.id}
                      <input type="hidden" value=${dictionaryCategory.id} />
                      </td>
		              <td class="td_center">${dictionaryCategory.code}</td>
		              <td class="td_center">${dictionaryCategory.name}</td>
					  <td class="td_center">${dictionaryCategory.sortNo}</td>
		              <td class="td_center">${dictionaryCategory.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                      <td class="td_center">${dictionaryCategory.modifyTime?string("yyyy-MM-dd HH:mm:ss")}</td>  				  		       
	             </tr>
	            </#list>
              </table>
	     </div>
	</div>
	  <@pager  pagination=queryAllDictionaryCategoryByPageInfo  formId="queryForm"  />  
   </form>
</@backend>