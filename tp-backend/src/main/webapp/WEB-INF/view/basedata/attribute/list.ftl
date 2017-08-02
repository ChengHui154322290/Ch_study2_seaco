<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/attribute.js'] 
	css=[] >
<form class="jqtransform" method="post" id="attrForm" action="${domain}/basedata/attribute/list.htm">
	<div id="search_bar" class="mt10">
       <div class="box">
      
          <div class="box_border">
            <div class="box_center pt10 pb10">
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
              
                 <!-- <td>属性编号:</td> -->
                 <!-- <td><input type="text" name="code" id='code' class="input-text lh25" size="30" value="${attribute.code}" /></td> -->
                  <td>属性名称:</td>
                  <td><input type="text" name="name" id='name' class="input-text lh25" size="30"  value="${attribute.name}" /></td>
                  <td>状态</td>
                  <td>
                    <span class="fl">
                      <div class="select_border">
                        <div class="select_containers ">
                       <select name="status" class="select"> 
                            <option value=''      <#if  attribute.status==null>selected='selected'</#if>>全部</option> 
	                        <option value="1"  <#if attribute.status??&&attribute.status?string=='1'>selected='selected'</#if>>有效</option> 
	                        <option value="0" <#if attribute.status??&&attribute.status?string=='0'>selected='selected'</#if>>无效</option> 
                        </select> 
                        </div>
                      </div> 
                    </span>
                  </td>
                </tr>
              </table>
            </div>
           
           <span id="nameInfo" class="error"></span>
           
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:center;">
              <input class="btn btn82 btn_search" id="searthAtt" type="submit" value="查询" />
              <input class="btn btn82 btn_res " type="button" value="重置"  onclick="dataReset(this)" />
              <input class="btn btn82 btn_add attAddBtn" type="button" value="新增" name="button"/>
              </div>
            </div>
            </div>       
        </div>
    </div>
    
       <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                   <th width="100">属性编号</th>
                   <th width="100">属性名称</th>
                   <th width="100">备注</th>
                   <th width="80">状态</th>
                   <th width="100">操作</th>
                </tr>

                <#list queryPageListByAttribute.data.rows as att>
                <tr class="tr">
                   <td class="td_center">${att.code}</td>
                   <td class="td_center">${att.name}</td>
                   <td class="td_center">${att.remark}</td>
                   <td class="td_center"> ${(att.status==1) ?string("有效", "无效")}</td>
                   <td class="td_center">
                   <a href="javascript:void(0);" class="viewAtt" param='${att.id}'>[查看]</a>
                   <a href="javascript:void(0);" class="editAtt" param='${att.id}'>[编辑]</a> <a>[日志]</a>
                   </td>
                 </tr>
                 </#list>
                 
              </table>
        </div>
     </div>
 <@pager  pagination=queryPageListByAttribute.data  formId="attrForm" />
 </form>  
</@backend>