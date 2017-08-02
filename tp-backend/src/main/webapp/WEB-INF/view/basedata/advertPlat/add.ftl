<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/formValidator/formValidator-4.0.1.min.js',
    '/statics/backend/js/formValidator/formValidatorRegex.js',
    '/statics/backend/js/formValidator/DateTimeMask.js',
    '/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/advertPlat.js'] 
	css=[
    '/statics/backend/js/formValidator/style/validator.css'
	] >
    <div >
	   <div>	  
	     <form action="${domain}/basedata/advertPlat/add" class="jqtransform"  method="post"  id='advertPlatupdate'  > 
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">	      	        
   		 
				<tr>
				  <td class="td_right">广告平台名称:</td>
				  <td><input type="text" name="advertPlatName" id='advertPlatName' class="input-text lh25" size="20" ></td>
				  <td><div id="wordsTip" style="width:250px"></div></td>				  
				 </tr> 
				<tr>   
				   <td class="td_right">广告平台编码:</td>
				   <td>
				   <input type="text" name="advertPlatCode" id='advertPlatCode' class="input-text lh25" size="20" >
				   <td>
                  </td>
				</tr>				
				
				<tr>
				   <td class="td_left">
						<div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		                   <input class="btn btn82 btn_save2" type="button"  id='datasubmit' value="保存" param='/basedata/advertPlat/advertPlatSubmit.htm' } />
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