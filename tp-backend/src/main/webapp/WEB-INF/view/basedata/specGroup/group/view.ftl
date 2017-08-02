<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/specGroup.js'] 
	css=[] >
       <div >
	   <div>	  
	     <form action="${domain}/basedata/specGroup/group/edit" class="jqtransform"  method="post"  id='specGroupUpdate'  >
	        <table class="form_table" border="0" cellpadding="0" cellspacing="0">
	        <tr>
	          <td valign="top">
	          <table class="form_table" border="0" cellpadding="0" cellspacing="0">
		       <tr>
					<td class="td_right">规格组编号:</td>
					<td class="td_left">${specGroup.code}</td>
					 <input type="hidden" name="id" value="${specGroup.id}" />
			   </tr>
		  	   
		        <tr>
					<td class="td_right">规格组名称:</td>
					<td><input type="text" name="name" class="input-text lh25" size="20" value='${specGroup.name}' ></td>				 
				</tr>
   		
		        <tr>
				  <td class="td_right">备注:</td>
				  <td><input type="text" name="remark" class="input-text lh25" size="20" value='${specGroup.remark}' ></td>
				</tr>				
				<tr>   
				   <td class="td_right">状态:</td>
				   <#assign sts="${specGroup.status}" />
				   <td>
                    <input type="radio" name="status" <#if sts=="1">checked="checked"</#if> value="1">有效
                    <input type="radio" name="status" <#if sts!="1">checked="checked"</#if> value="0">无效
                  </td>
				<tr colspan='2' >                			  
                  <td class="td_center >
                    <div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		             	   <input class="btn btn82 btn_nochecked closebtn " type="button" value="取消" id="buttoncancel"/ >
		                 </div>
		            </div>
                  </td>
               </tr>
              </table>
               </td>
		           <td valign="top" style='border: 1px dashed #BBBBBB;width:50%;'>   
					   <table>
					   <#if specSelectedList?default([])?size !=0>
					   <tr><td class="td_right" style="color:green">已经关联的规格列表</td></tr>
					   <tr>
					     <th>规格id</th>
					     <th>规格</th>
					     <th>规格状态</th>
					   </tr>
					   </#if>
					    <#list specSelectedList as att>
		                <tr class="tr">
		                   <td class="td_center">${att.id}</td>
		                   <td class="td_center">${att.spec}</td>       
		                   <td class="td_center"> ${(att.status!true) ?string("有效", "无效")}</td>
		                </tr>
		                 </#list>
					   </table>
					</td>
	          </tr>
          </table>
         </form>            
	   </div>	
    </div>
</@backend>