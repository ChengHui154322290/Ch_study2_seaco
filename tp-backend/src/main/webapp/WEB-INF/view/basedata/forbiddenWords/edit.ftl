<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/formValidator/formValidator-4.0.1.min.js',
    '/statics/backend/js/formValidator/formValidatorRegex.js',
    '/statics/backend/js/formValidator/DateTimeMask.js',
    '/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/forbiddenWords.js'] 
	css=[
    '/statics/backend/js/formValidator/style/validator.css'
	] >
    <div >
	   <div>	  
	     <form action="${domain}/basedata/forbiddenWords/edit" class="jqtransform"  method="post"  id='forbiddenWordsupdate'  > 
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
		        <tr>
					<td  class="td_right">违禁词编号:</td>
					<td>${forbiddenWords.code}
						<input type="hidden" name="id" value="${forbiddenWords.id}" />                   
                    </td>
				</tr>		        
   		   

				<tr>
				  <td class="td_right">违禁词:</td>
				  <td><input type="text" name="words" id='words' class="input-text lh25" size="20" value="${forbiddenWords.words}" /></td>
				  <td><div id="wordsTip" style="width:250px"></div></td>				  	  
				</tr>
				
				<tr>   
				   <#assign sta="${forbiddenWords.status}" />
				   <td class="td_right">状态:</td>
				   <td>
                    <input type="radio" name="status" <#if sta =="1">checked="checked"</#if>  value="1"> 有效
                    <input type="radio" name="status" <#if sta !="1">checked="checked"</#if>  value="0"> 无效
                  </td>
				</tr>	
			
				<tr>
				  <td class="td_right">备注:</td>
				  <td><input type="text" name="remark" id='remark' class="input-text lh25" size="20" value="${((forbiddenWords.remark)!'')?xhtml}"  /></td>
				  <td><div id="remarkTip" style="width:250px"></div></td>	
				</tr>
				
				<tr>
				   <td  class="td_left">
						<div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		                   <input class="btn btn82 btn_save2" type="button" id='datasubmit' value="保存" param='/basedata/forbiddenWords/update.htm' } />
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
              </form>            
	   </div>	
    </div>
</@backend>