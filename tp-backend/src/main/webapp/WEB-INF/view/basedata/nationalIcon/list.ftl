<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/nationalIcon.js',
	'/statics/select2/js/select2.js',
	'/statics/select2/js/select2Util.js',
	'/statics/select2/js/select2_locale_zh-CN.js',
	'/statics/backend/js/basedata/base-select2.js'
	] 
	css=[
	'/statics/select2/css/select2.css'
	] >
	 <form class="jqtransform" method="post" id="queryAttForm" action="${domain}/basedata/nationalIcon/list.htm">
    <div >
	   <div>	      
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                 <tr>
				  <td class="td_right">产地:</td>
				  <td>
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers " id="conutrydiv"> 
                        <select name="countryId" id='countryId' class="select2" style="width:160px; margin-left: 1px"> 
                         <option value="">---全部产地---</option> 
                         <#if allCountryAndAllProvince?exists>
			                <#list allCountryAndAllProvince?keys as key> 
			                  <option <#if key==nationalIcon.countryId>selected='selected'</#if> value="${key}">${allCountryAndAllProvince[key]}</option> 
			                </#list>
			            </#if>
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                  <td>排序号:</td>
                  <td><input type="text" name="sortNoString" class="input-text lh25" size="20" value="${nationalIcon.sortNo}"></td>
                   <td>英文名:</td>
                  <td><input type="text" name="nameEn" class="input-text lh25" size="20" value="${nationalIcon.nameEn}"></td>
                   <td>大洲:</td>
	              <td>
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="continentId" class="select"> 
                         <option value="">-全部-</option> 
                         <#if continentInformations?exists>
			                <#list continentInformations?keys as key> 
			                  <option value="${key}"  <#if key==nationalIcon.continentId>selected='selected'</#if>>${continentInformations[key]}</option> 
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
                            <option value=''      <#if  nationalIcon.status==null>selected='selected'</#if>>全部</option> 
	                        <option value="1"  <#if nationalIcon.status??&&nationalIcon.status?string=='1'>selected='selected'</#if>>有效</option> 
	                        <option value="0" <#if nationalIcon.status??&&nationalIcon.status?string=='0'>selected='selected'</#if>>无效</option> 
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
               	 <a href="${domain}/basedata/nationalIcon/list.htm"><input class="btn btn82 btn_res " type="button" value="重置"/></a>
                 <input class="btn btn82 btn_add addcatabtn nationalIconaddbtn" type="button" value="新增" >
              </div>
        </div>
    </div>
    
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">地区编号</th>
                      <th width="40">国旗或区旗</th>
	                  <th width="100">地区名称</th>
	                  <th width="100">英文名</th>
	                  <th width="100">大洲</th>
	                  <th width="100">SortNo</th>
	                  <th width="150">备注</th>	                               
	                  <th width="50">状态</th>	                               
	                  <th width="100">创建时间</th>
	                  <th width="100">更新时间</th>
	                  <th width="100">操作</th>	                 
                </tr>
            <#list queryAllNationalIconByPage.rows as nationalIcon>               
                <tr class="tr" >
		              <td class="td_center">${nationalIcon.id}
                         <input type="hidden" value=${nationalIcon.id} />
                      </td>
                      <td class="td_center"><img src="${nationalIcon.picPath}" /></td>
                      <td class="td_center">
		               <#if allCountryAndAllProvince?exists>
			                <#list allCountryAndAllProvince?keys as key> 
			                   <#if nationalIcon.countryId==key>${allCountryAndAllProvince[key]}</#if>
			                </#list>
			            </#if>
		              </td>
                      <td class="td_center">${nationalIcon.nameEn}</td>	                   
                      <td class="td_center">
		                <#if continentInformations?exists>
			                <#list continentInformations?keys as key> 
			                   <#if nationalIcon.continentId==key>${continentInformations[key]}</#if>
			                </#list>
			            </#if>
		              </td>
                      <td class="td_center">${nationalIcon.sortNo}</td>	
                      <td class="td_center">${nationalIcon.remark}</td>	 
		              <td class="td_center"><#assign sta="${nationalIcon.status}" /><#if sta=='1'>有效<#else>无效</#if></td>		            
		              <td class="td_center">${nationalIcon.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                      <td class="td_center">${nationalIcon.modifyTime?string("yyyy-MM-dd HH:mm:ss")}</td>                      
		              <td class="td_center"><a href="javascript:void(0);"  class="editcatabtn nationalIconeditbtn" param='${nationalIcon.id}'>[编辑]</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="">[日志]</a></td>	
	             </tr>
	            </#list>
              </table>
	     </div>
	</div>
   <@pager  pagination=queryAllNationalIconByPage  formId="queryAttForm"  />  
   </form>
</@backend>