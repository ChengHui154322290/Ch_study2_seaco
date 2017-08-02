<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/formValidator/formValidator-4.0.1.min.js',
    '/statics/backend/js/formValidator/formValidatorRegex.js',
    '/statics/backend/js/formValidator/DateTimeMask.js',
    '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
    '/statics/backend/js/jquery.form.2.2.7.js',
    '/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/*.js'] 
	css=[
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/formValidator/style/validator.css'
	] >
    <div >
	   <div>	  
	     <form action="${domain}/basedata/brand/update" class="jqtransform"  method="post"  id='brandupdate'  > 
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
		        <tr>
					<td class="td_right">快递公司编号:</td>
					<td><input type="text" name="name" id='name' class="input-text lh25" size="20" value='${brandDO.name}'>
                          <input type="hidden" name="expressId" value="${brandDO.id}" />
                     </td>
                     <td><div id="nameTip" style="width:250px"></div></td>
				</tr>
        
				
				<tr>
				  <td class="td_right">快递公司名称:</td>
				  <td><input type="text" name="nameEn" id="nameEn" class="input-text lh25" size="20" value='${brandDO.nameEn}'></td>
				   <td><div id="nameEnTip" style="width:250px"></div></td>
				</tr>
				
				<tr>
				  <td class="td_left">
						<div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		                   <input class="btn btn82 btn_save2" type="button"  id='datasubmit' value="保存" param='/basedata/brand/update.htm'  />
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