<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/formValidator/formValidator-4.0.1.min.js',
    '/statics/backend/js/formValidator/formValidatorRegex.js',
    '/statics/backend/js/formValidator/DateTimeMask.js',
    '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
    '/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/dictionaryCategory.js'] 
	css=[
	'/statics/backend/js/formValidator/style/validator.css'
	] >
    <div >
	   <div>	  
	     <form action="${domain}/basedata/dictionaryCategory/edit" class="jqtransform"  method="post"  id='dictionaryCategoryupdate'  > 
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
		        <tr>
					<td class="td_right">类别码:</td>
					<td><input type="text" name="code"  id='code' class="input-text lh25" size="20"  value="${dictionaryCategoryDO.code}"></td>
					  <input type="hidden" name="id" value="${dictionaryCategoryDO.id}" />
					  <td><div id="codeTip" style="width:200px"></div></td>
				</tr>
   		
				<tr>
				  <td class="td_right">类别名称:</td>
				  <td><input type="text" name="name" id='name' class="input-text lh25" size="20" value="${dictionaryCategoryDO.name}"></td>
				  <td><div id="nameTip" style="width:200px"></div></td>
				 </tr>				
				
				<tr>
				   <td class="td_left">
						<div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		                   <input class="btn btn82 btn_save2" type="button" id='datasubmit' value="保存" param='/basedata/dictionary/category/update.htm' onclick="dictionaryCategorySubmit(this)"} />
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