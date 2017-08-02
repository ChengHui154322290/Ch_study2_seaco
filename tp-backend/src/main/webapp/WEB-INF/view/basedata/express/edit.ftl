<#include "/common/common.ftl"/>
<@backend title="快递公司编辑" js=[	
'/statics/backend/js/formValidator/formValidator-4.0.1.min.js',
'/statics/backend/js/formValidator/formValidatorRegex.js',
'/statics/backend/js/formValidator/DateTimeMask.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/basedata/express.js',
'/statics/backend/js/form.js'] 
	css=[
	'/statics/backend/js/formValidator/style/validator.css'
	] >
    <div >
	   <div>	  
	     <form action="${domain}/basedata/express/edit.htm" class="jqtransform"  method="post"  id='expressUpdateForm'  > 
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
		        <tr>
					<td class="td_right">ID:</td>
					<td>${expressInfoDO.id}</td>
					 <input type="hidden" name="id" value="${expressInfoDO.id}" />
				</tr>
   		
				<tr>
				  <td class="td_right">快递公司名称:</td>
				  <td><input type="text" name="name" id='name' class="input-text lh25" size="20"  value='${expressInfoDO.name}'/></td>
				  <td><div id="nameTip" style="width:230px"></div></td>
				</tr>
				<tr>
				  <td class="td_right">code:</td>
				  <td><input type="text" name="code" id='code' class="input-text lh25" size="20" value="${expressInfoDO.code}" /></td>
				  <td><div id="codeTip" style="width:230px"></div></td>
				</tr>
				<tr>
				  <td class="td_right">排序值(sort_no):</td>
				  <td><input type="text" name="sortNo" id='sortno' class="input-text lh25" size="20" value="${expressInfoDO.sortNo}" /></td>
				  <td><div id="sortnoTip" style="width:230px"></div></td>
				</tr>
				<tr><td colspan="2"><div style="color:red">(提示：快递公司名称和code均不能存在重复的值)</div></td></tr>
				<tr>
				   <td class="td_left">
						<div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		                   <input class="btn btn82 btn_save2" type="button"  id='datasubmit' value="保存" param="editexpress" />
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