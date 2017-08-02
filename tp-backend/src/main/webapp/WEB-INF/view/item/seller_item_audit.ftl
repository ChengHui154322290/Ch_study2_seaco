<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/layer/layer.min.js',
    '/statics/backend/js/item/item-seller.js'
    ] 
	css=[
    '/statics/backend/js/formValidator/style/validator.css'
	] >
    <div >
	   <div>	  
	     <form action="" class="jqtransform"  method="post"  id='auditForm'  > 
	     	<input type="hidden" name="sellerSkuId" value="${sellerSkuId}" />
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">	      	        
   		 	
				<tr>
				  <td class="td_right">审核结果:</td>
				   <td>
				      <input id="at3" type="radio" name="auditResult"  checked='checked' value="A"/>
				      <label for="at3" >通过</label>
					   <input id="at4" type="radio" name="auditResult" value="R"/>
					  <label for="at4" >驳回</label> 
                   </td>
				 </tr> 
				<tr>   
				   <td class="td_right" valign="top">驳回理由:</td>
				   <td  valign="top">
				   
				    <#if rejectTypes?exists>
				                <div>
                                    <input id="checkAll" type="checkbox" disabled='disabled'  />全选
                               </div>   
			                <#list rejectTypes?keys as key> 
			                    <div>
                                   <input type='checkbox'  name="listRejectKey" disabled='disabled' value="${rejectTypes[key]}"/>${key}
                               </div>    
			                </#list>
			            </#if>
                  </td>
				</tr>				
				<tr>
				  <td class="td_right"  valign="top">审核意见:</td>
				  <td rowspan="1">
					 <textarea name="auditDesc" cols="30" rows="3" id="subTitle" maxlength=100  ></textarea>
				  </td>
				</tr>
				
				<tr>
				   <td class="td_left">
						<div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		                   <input class="btn btn82 btn_save2" type="button"  id='datasubmit' value="提交" />
		                 </div>
		                </div>
                  </td> 
                  
				  
                  <td class="td_rigth">
                    <div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		             	     <input class="btn btn82 btn_nochecked closebtn" type="button" value="取消" id="buttoncancel"/ >
		                 </div>
		            </div>
                  </td>
               </tr>
              </table>    
              </form>            
	   </div>	
    </div>
</@backend>