<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/formValidator/formValidator-4.0.1.min.js',
    '/statics/backend/js/formValidator/formValidatorRegex.js',
    '/statics/backend/js/formValidator/DateTimeMask.js'
    '/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/taxRate.js'] 
	css=[
	'/statics/backend/js/formValidator/style/validator.css'
	] >
    <div >
	   <div>	  
	     <form action="${domain}/basedata/taxRate/edit" class="jqtransform"  method="post"  id='taxRateupdate'  > 
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
		        <tr>
					<td class="td_right">税率ID:</td>
					<td>${taxRate.id}</td>
					 <input type="hidden" name="id" value="${taxRate.id}" />
				</tr>
   		
				<tr>
				  <td class="td_right">税率类型:</td>
                  	<td>
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="type" class="select"> 
                         <#if taxRateTypes?exists>
			                <#list taxRateTypes?keys as key> 
			                  <option <#if key==taxRate.type>selected='selected'</#if> value="${key}">${taxRateTypes[key]}</option> 
			                </#list>
			            </#if>
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
				</tr>
				<tr>
				  <td class="td_right">税率:</td>
				  <td><input type="text" name="rate" id='rate' class="input-text lh25" size="20"  value='${taxRate.rate}'/></td>
				  <td><div id="rateTip" style="width:250px"></div></td>
				</tr>
				<tr>
				  <td class="td_right">完税金额:</td>
				  <td><input type="text" name="dutiableValue" id='dutiableValue' class="input-text lh25" size="20" value='${taxRate.dutiableValue}'/></td>
				  <td><div id="dutiableValueTip" style="width:250px"></div></td>
				</tr>
				<tr>
				  <td class="td_right">税号:</td>
				  <td><input type="text" name="code" id='code' class="input-text lh25" size="20" value='${taxRate.code}'/></td>
				  <td><div id="codeTip" style="width:250px"></div></td>
				</tr>
				<tr>
				  <td class="td_right">备注:</td>
				  <td><input type="text" name="remark" id='remark' class="input-text lh25" size="20" value="${taxRate.remark}" /></td>
				  <td><div id="remarkTip" style="width:250px"></div></td>
				</tr>
				<tr>   
				   <td class="td_right">状态:</td>
				   <#assign sts="${taxRate.status}" />
				   <td>
                    <input type="radio" name="status" <#if sts=="1">checked="checked"</#if> value="1"> 有效
                    <input type="radio" name="status" <#if sts!="1">checked="checked"</#if> value="0"> 无效
                  </td>
				</tr>					
				
				<tr>
				   <td class="td_left">
						<div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		                   <input class="btn btn82 btn_save2" type="button"  id='datasubmit' value="保存" param='/basedata/taxRate/update.htm'  />
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