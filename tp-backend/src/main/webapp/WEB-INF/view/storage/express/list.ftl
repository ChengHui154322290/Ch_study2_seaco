<#include "/common/common.ftl"/>
<@backend title="快递公司维护" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/storage/js/express.js'
] css=[] >

	 <form class="jqtransform" method="post" id="queryAttForm" action="">
    <div >
	   <div>	      
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>快递公司名称:</td>
                  <td><input type="text" name="name" class="input-text lh25" size="20" value='${name}'></td>
                </tr>
              </table>
	   </div>
	  <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <input class="btn btn82 btn_search" type=""   id="searthAtt" value="查询" >
                 <input class="btn btn82 btn_add addcatabtn expressaddbtn" type="button" value="新增" >
              </div>
        </div>
    </div>
    
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">ID</th>
	                  <th width="100">快递公司编号</th>
	                  <th width="100">快递公司名称</th>
	                  <th width="150">编辑</th>       
                </tr>
            <#list queryAllBrandByPageInfo.rows as zz>               
                <tr class="tr" >
		              <td class="td_center">${1}
                      <input type="hidden" value=${2} />
                      </td>
		              <td class="td_center">${3}</td>
		              <td class="td_center">${4}</td>                     
		              <td class="td_center"><a href="javascript:void(0);"  class="editcatabtn expresseditbtn" param='${5}'>修改</a></td>	
	             </tr>
	            </#list>
              </table>
	     </div>
	</div>
   <@pager  pagination=queryAllBrandByPageInfo  formId="queryAttForm"  />  
   </form>


</@backend>