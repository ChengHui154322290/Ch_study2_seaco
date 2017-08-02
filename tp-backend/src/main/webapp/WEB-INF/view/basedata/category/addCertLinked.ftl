<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/category.js'] 
	css=[] >
    <form class="jqtransform" method="post" id="updateCateCertFrom">
	 <div id="attView" class="mt10">
        <div class="box">
          <div class="box_border">
            <div class="box_center pt10 pb10">
            <input type='hidden' name='cateId' value ="${cateId}" >
               <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                   <th width="100">是否已添加</th>
                   <th width="300">资质名称</th>
                </tr>
                 <#list certDicts as att>
                 <tr class="tr" >
                    <td class="td_center" >            
 	                 <#assign s1="${att.id}" />        	                       
                     <input type='checkbox' name="ids"  
                     <#list hasSelDictionaryInfos as info> 
              		 <#assign s2="${info.id}" />
              		 <#if s1==s2>checked="checked"</#if>
                     </#list>   
                     value="${att.id}"/></td>
                   <td class="td_center">${att.name}</td>
                 </tr> 
                 </#list>                           
   </table>
   </div>
</div>
</div>
</div>
        <div id="div1_submit" style="text-align:center;">
			<input class="btn btn82 btn_save2" type="button" value="保存" param='/basedata/category/updateCateCertLinked.htm' onclick="updateCateCertLinked(this)" />
    		<input class="btn btn82 btn_del closebtn" type="button" value="取消" name="button1" />
		</div>
   </form>
</@backend>