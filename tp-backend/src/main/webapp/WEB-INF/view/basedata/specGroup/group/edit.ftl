<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/formValidator/formValidator-4.0.1.min.js',
    '/statics/backend/js/formValidator/formValidatorRegex.js',
    '/statics/backend/js/formValidator/DateTimeMask.js',
    '/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/specGroup.js'] 
	css=[
	 	'/statics/backend/js/formValidator/style/validator.css'
	] >
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
					<td><input type="text" name="name" id='name' class="input-text lh25" size="20" value='${specGroup.name}' ></td>	
					<td><div id="nameTip" style="width:200px"></div></td>			 
				</tr>
   		
		        <tr>
				  <td class="td_right">备注:</td>
				  <td><input type="text" name="remark" id='remark' class="input-text lh25" size="20" value='${specGroup.remark}' ></td>
				  <td><div id="remarkTip" style="width:200px"></div></td>	
				</tr>				
				<tr>   
				   <td class="td_right">状态:</td>
				   <#assign sts="${specGroup.status}" />
				   <td>
                    <input type="radio" name="status" <#if sts=="1">checked="checked"</#if> value="1">有效
                    <input type="radio" name="status" <#if sts!="1">checked="checked"</#if> value="0">无效
                  </td>
				<tr>
				   <td class="td_left">
						<div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		                   <input class="btn btn82 btn_save2" type="button" id='datasubmit'  value="保存" param='/basedata/specGroup/group/update.htm' />
		                 </div>
		                </div>
                  </td> 
                  			  
                  <td class="td_rigth">
                    <div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		             	   <input class="btn btn82 btn_nochecked closebtn " type="button" value="取消" id="buttoncancel"/ >
		                 </div>
		            </div>
                  </td>
               </tr>
              </table>
               </td>
               <td valign="top">
                 <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                   <tr>
                       <td class="td_left"><input id="searchSp"  type="text"  class="input-text lh25" size="20"></td>
					   <td>
					      <a href="javascript:void(0);" param='/basedata/specGroup/group/addSpec.htm'  onclick="addSpec(this)" >新增</a>
					   </td>
                     </tr>
                      <tr><td style='color:green'>可编辑的规格</td></tr>
                 	<tr>
                 	    <td>
                 	     <div id="editcheckboxs" style="overflow: auto; height: 300px;width:220px; position:relative;" >
                 	     <#list allSpecList as specAll>
                 	              <#assign s1="${specAll.id}" /> 
                 	               <div>      	                       
		                             <input type='checkbox' name="ids"  
		                             <#list specSelectedList as spec> 
		                              		<#assign s2="${spec.id}" />
		                              		<#if s1==s2>checked="checked"</#if>
		                             </#list>   
		                             value="${specAll.id}"/><span> ${specAll.code} 
		                             &nbsp;&nbsp;&nbsp;${specAll.spec}</span>
		                           </div>                    	                      
	                      </#list> 	     
                 	     </div>
                 	    </td>
                 	</tr>
                 	
                 		<br/>	<br/>	<br/>
                 	
             	<#if invalidSpecList?default([])?size !=0> <tr><td style='color:red'>可删除的已失效的规格</td></tr></#if>              
                 	<tr>
                 	    <td>
                 	     <div id="editcheckboxs" style="overflow: auto; height: 400px;width:220px;">
                 	     <#list invalidSpecList as specInvalid>
                 	                 	                       
		                             <input type='checkbox' name="ids"  onclick="return false";
		                              	checked="checked"
		                             value="${specInvalid.id}"/> ${specInvalid.code} 
		                             &nbsp;&nbsp;&nbsp;${specInvalid.spec}
		                              <a href="javascript:void(0);"  param='/basedata/specGroup/spec/deleteSpecGroup.htm?specId=${specInvalid.id}&specGroupId=${specGroup.id}'  onclick="deleteSpec(this)">删除</a>
		                             <br/>                       	                      
	                      </#list> 	     
                 	     </div>
                 	    </td>
                 	</tr>
                 </table>
               </td>
	          </tr>
          </table>
         </form>            
	   </div>	
    </div>
</@backend>