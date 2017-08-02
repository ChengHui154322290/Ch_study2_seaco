<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/attribute.js'] 
	css=[] >
    <form class="jqtransform" method="post" id="attrCateUpdateRelation">
	 <div id="attView" class="mt10">
        <div class="box">
          <div class="box_border">
            <div class="box_center pt10 pb10">
            <input type='hidden' name='cateId' value ="${cateId}" >
               <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                   <th width="100">是否已添加</th>
                   <th width="100">属性组code</th>
                   <th width="100">属性名称</th>
                   <th width="80">属性状态</th>   
                </tr>
                <#assign selectedStatus="${selectedStatus}">
                 <#list list as att>
                 <tr class="tr" >
                    <td size="30">            
 	                 <#assign s1="${att.id}" />        	                       
                     <input type='checkbox' name="ids" 
                     <#list slectLinkInfo as info> 
              		 <#assign s2="${info.attributeId}" />
              		 <#if s1==s2>checked="checked" </#if>
                     </#list>     
                     value="${att.id}"/></td>
                   <td>${att.code}</td>
                   <td>${att.name}</td>
                   <td> ${(att.status==1) ?string("有效", "无效")}</td>
                 </tr> 
                 </#list>                           
   </table>
   </div>
</div>
</div>
</div>
        <div id="div1_submit" style="text-align:center;">
			<input class="btn btn82 btn_save2" type="button" value="保存" param='/basedata/category/updateCateAttrLinked.htm' onclick="attrCateUpdate(this)" />
    		<input class="btn btn82 btn_del closebtn" type="button" value="取消" name="button1" />
		</div>
   </form>
</@backend>