<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/formValidator/formValidator-4.0.1.min.js',
    '/statics/backend/js/formValidator/formValidatorRegex.js',
    '/statics/backend/js/formValidator/DateTimeMask.js',
    '/statics/backend/js/layer/layer.min.js',
    '/statics/select2/js/select2.js',
	'/statics/select2/js/select2Util.js',
	'/statics/select2/js/select2_locale_zh-CN.js',
	'/statics/backend/js/basedata/dictionaryInfoAddEdit.js'] 
	css=[
    	'/statics/backend/js/formValidator/style/validator.css',
    	'/statics/select2/css/select2.css'
	] >
    <div >
	   <div>	  
	     <form action="${domain}/basedata/dictionaryInfo/add" class="jqtransform"  method="post"  id='dictionaryInfoupdate'  > 
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
	            <tr>
	             <td class="td_right">分类列表:</td>
	              <td>
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="code" id="code" class="select"> 
                         <option value="">---全部分类---</option> 
                         <#if cataGoryInfoMap?exists>
			                <#list cataGoryInfoMap?keys as key> 
			                  <option value="${key}">${cataGoryInfoMap[key]}</option> 
			                </#list>
			            </#if>
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                 </tr>	
		        <tr>
					<td class="td_right">字典名称:</td>
					<td><input type="text" name="name" id='name' class="input-text lh25" size="20" ></td>
				    <td><div id="nameTip" style="width:200px"></div></td>
				</tr>
   				<tr class="customsunit" style="display:none;">
   					<td class="td_right">*海关计量:</td>
   					<td>
						<select name="customsUnitId"  id="customsUnitId" class="select2" style="width:150px;"></select> 
					</td>
   				</tr>
				
				<tr>
				   <td class="td_left">
						<div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		                   <input class="btn btn82 btn_save2" type="button" id='datasubmit' value="保存" param='/basedata/dictionary/info/addDictionaryInfoSubmit.htm' />
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