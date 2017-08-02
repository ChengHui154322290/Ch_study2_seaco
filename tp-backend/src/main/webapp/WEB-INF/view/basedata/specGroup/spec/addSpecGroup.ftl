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
	     <form action="${domain}/basedata/specGroup/group/add" class="jqtransform"  method="post"  id='specGroupUpdate'  >
	        <table class="form_table" border="0" cellpadding="0" cellspacing="0">
	        <tr>
	          <td valign="top">
	          <table class="form_table" border="0" cellpadding="0" cellspacing="0">
		       <tr>
					<td class="td_right">规格组编号:</td>
					<td class="td_left">系统自动产生</td>
			   </tr>
		  	   
		        <tr>
					<td class="td_right">规格组名称:</td>
					<td><input type="text" name="name" id='name' class="input-text lh25" size="20" ></td>
					<td><div id="nameTip" style="width:200px"></div></td> 	 
				</tr>
   		
		        <tr>
				  <td class="td_right">备注:</td>
				  <td><input type="text" name="remark" id='remark' class="input-text lh25" size="20" ></td>
			      <td><div id="remarkTip" style="width:200px"></div></td>   
				</tr>				
				<tr>   
				   <td class="td_right">状态:</td>
				   <td>
                  <input type="radio" name="status" checked="checked" value="true"> 有效
                  <input type="radio" name="status" value="false"> 无效
                  </td>
				</tr>
				<tr>
				   <td class="td_left">
						<div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		                   <input class="btn btn82 btn_save2" type="button"  id='datasubmit' value="保存" param='/basedata/specGroup/group/addSpecGroupSubmit.htm'  />
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
	          </tr>
          </table>
         </form>            
	   </div>	
    </div>
</@backend>