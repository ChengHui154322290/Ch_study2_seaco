<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/brand.js',
	'/statics/select2/js/select2.js',
	'/statics/select2/js/select2Util.js',
	'/statics/select2/js/select2_locale_zh-CN.js',
	'/statics/backend/js/basedata/base-select2.js'
	] 
	css=[
	'/statics/select2/css/select2.css'
	] >
	 <form class="jqtransform" method="post" id="queryAttForm" action="${domain}/basedata/brand/list.htm">
    <div >
	   <div>	      
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>中文名</td>
                  <td><input type="text" name="name" class="input-text lh25" size="20" value='${brand.name}'></td>
                  <td>英文名</td>
                  <td><input type="text" name="nameEn" class="input-text lh25" size="20" value='${brand.nameEn}'></td>
				  <td>产地:</td>
				  <td>
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers " id="conutrydiv"> 
                        <select name="countryId" id='countryId' class="select2" style="width:160px; margin-left: 1px"> 
                         <option value="">---全部产地---</option> 
                         <#if allCountryAndAllProvince?exists>
			                <#list allCountryAndAllProvince?keys as key> 
			                  <option <#if key==brand.countryId>selected='selected'</#if> value="${key}">${allCountryAndAllProvince[key]}</option> 
			                </#list>
			            </#if>
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                  <td>状态</td>
                 <td>
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers ">
                        <select name="status" class="select"> 
                            <option value=''      <#if  brand.status==null>selected='selected'</#if>>全部</option> 
	                        <option value="1"  <#if brand.status??&&brand.status?string=='1'>selected='selected'</#if>>有效</option> 
	                        <option value="0" <#if brand.status??&&brand.status?string=='0'>selected='selected'</#if>>无效</option> 
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
                 <input class="btn btn82 btn_search"  type="submit"   id="searthAtt" value="查询" >
               	 <a  href="${domain}/basedata/brand/list.htm"><input class="btn btn82 btn_res " type="button" value="重置"  onclick="dataReset(this)" /></a>
                 <input class="btn btn82 btn_add addcatabtn brandaddbtn" type="button" value="新增" >
              </div>
        </div>
    </div>
    
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">ID</th>
                      <th width="40">LOGO</th>
	                  <th width="100">中文名</th>
	                  <th width="100">英文名</th>
	                  <th width="150">产地</th>
	                  <th width="100">备注</th>  
	                  <th width="50">状态</th>	                 
	                  <th width="100">创建时间</th>
	                  <th width="100">更新时间</th>
	                  <th width="100">操作</th>	                 
                </tr>
            <#list queryAllBrandByPageInfo.rows as brand>               
                <tr class="tr" >
		              <td class="td_center">${brand.id}
                      <input type="hidden" value=${brand.id} />
                      </td>
                       <td class="td_center"><img src="${brand.logo}" /></td>
		              <td class="td_center">${brand.name}</td>
		              <td class="td_center">${brand.nameEn}</td>
		              <td class="td_center">
		               <#if allCountryAndAllProvince?exists>
			                <#list allCountryAndAllProvince?keys as key> 
			                   <#if brand.countryId==key>${allCountryAndAllProvince[key]}</#if>
			                </#list>
			            </#if>
		              </td>
		              <td class="td_center">${brand.remark}</td>		             
		              <td class="td_center">
                     <#assign sta="${brand.status}" /><#if sta=='1'>有效<#else>无效</#if></td>
		              <td class="td_center">${brand.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                      <td class="td_center">${brand.modifyTime?string("yyyy-MM-dd HH:mm:ss")}</td>                      
		              <td class="td_center"><a href="javascript:void(0);"  class="editcatabtn brandeditbtn" param='${brand.id}'>[编辑]</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		              <a href="javascript:void(0);"  class="editcatabtn journalReview" param='${brand.id}'>[日志]</a></td>		
	             </tr>
	            </#list>
              </table>
	     </div>
	</div>
   <@pager  pagination=queryAllBrandByPageInfo  formId="queryAttForm"  />  
   </form>
</@backend>