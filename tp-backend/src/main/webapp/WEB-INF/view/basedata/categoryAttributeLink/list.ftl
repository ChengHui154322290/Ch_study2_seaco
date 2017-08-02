<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/categoryAttribute.js',
	'/statics/backend/js/basedata/category.js'
	] 
	css=[] >
	
	<#--Title: 属性类别中间表-->
<div class="box_top"><b class="pl15"> 类别 : ${ansNameStr}</b></div>

	 <div id="forms" class="mt10">
        <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">小类属性组</b></div>
            <a href="#" class="ext_btn fr selectMore"  param='${attAndValues.category.id}'><span class="add"></span>添加</a> 
            <div class="box_center pt10 pb10">
               <table class="form_table" border="0" cellpadding="0" cellspacing="0">
               <tr style="text-align:center">
                   <th width="100">属性名称</th>
                   <th width="100">属性值</th>
                   <th width="80">状态</th>
                   <th width="200">操作</th>
               </tr>
               	<#list attAndValues.attributesAndValues as attAndValue>
	                  <tr style="text-align:center">
		                  <td class="td_center">
			                 ${attAndValue.attribute.name}
		                  </td>
                        <#if attAndValue !=null> 
		                  <td class="td_center112" style="text-align:center">
			                    <span class="fl">
			                      <div class="select_border">
			                        <div class="select_containers">
			                      
			                        <select name="" class="select">			                       
			                        	<#list attAndValue.attributeValues as attValue> 	
				                          <#if attValue.status??&&attValue.status==1><option>${attValue.name}</option></#if>
			                        	</#list>
			                        </select>
			                       
			                        </div>
			                      </div>
			                    </span>
		                  </td>
		                  <td class="td_center"> <#if attAndValue.attribute.status??&&attAndValue.attribute.status==1>有效<#else>无效</#if></td>
		                  <td class="td_center"><a href="#" class="editAttributeValue" param='${attAndValue.attribute.id}'  param1='${attAndValues.category.id}'>[编辑属性值]</a> &nbsp;
		                      <a href="#" class="deleteAttrCateLinked" param='${attAndValue.attribute.id}'  param1='${attAndValues.category.id}'>[移除属性组]</a></td>
		                       </#if> 
	                    </tr>   
               </#list>  
   </table>
   </div>
</div>
</div>
</div>
</div>

  <!-- <div style="text-align:center;"> 
    <input class="btn btn82 btn_save2" type="button" value="保存" name="button" >
    <input class="btn btn82 btn_nochecked closebtn" type="button" value="取消" name="button">
</div>  -->
    
    </@backend>