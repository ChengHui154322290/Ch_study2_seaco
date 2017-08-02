<#include "/common/common.ftl"/>
<@backend title="" js=[
'/statics/backend/js/basedata/attributeValue.js',
'/statics/backend/js/layer/layer.min.js'
] 
css=[] >
 
 <!--新增属性值-->
 <div class="box_top"><b class="pl15"> 类别 >>${nameStr} </b></div>
  <form class="jqtransform" method="post" id="cataValueEdit">
	 <div id="forms" class="mt10">
        <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">属性项:${attributeName}</b></div>
              <div class="box_center pt10 pb10">
			    <table class="form_table" border="0" cellpadding="0" cellspacing="0">
			                   <tr>
			     					<th width="100">属性值code</th>
		                            <th width="100">属性值名称</th>
		                            <th width="100">属性值状态</th>
                                </tr>
			                <#list listOfAttributeValue as att>
				                <tr class="tr">
				                   <td><input type='hidden' name='ids' value="${att.id}">${att.code}</td>
				                   <td>${att.name}</td>
				                   <td> 
					                    <span class="fl">
					                      <div class="select_border"> 
					                        <div class="select_containers "> 
					                        <select name="status" class="select"> 
					                         <option value="1"  <#if att.status??&&att.status?string=='true'>selected='selected'</#if>>有效</option> 
	                                         <option value="0" <#if att.status??&&att.status?string=='false'>selected='selected'</#if>>无效</option> 
					                        </select> 
					                        </div> 
					                      </div> 
					                    </span>
		                             </td>
				                 </tr>
                            </#list>
                            <tr>
							   <td>
							       <div id="div1_submit" style="text-align:center;">
									   <input class="btn btn82 btn_save2" type="button" value="保存"  param='/basedata/attributeValue/saveEdit.htm' onclick="saveEditAttValue(this)">			         
								   </div>
							   </td>
							   <td>
							       <div id="div1_submit" style="text-align:center;">
							           <input class="btn btn82 btn_del closebtn" type="button" value="取消" name="button">
								   </div>
							   </td> 
							    <td>
							       <div id="div1_submit" style="text-align:center;">
									   <input class="btn btn82 btn_save2 attValueAddBtn" type="button" value="新增"  param="${attId}">			         
								   </div>
							   </td>
                            </tr>
			              </tr>
			           </table>  
                       </div>
                     </div>
                  </div>
                </div>
              </div>
           </form>
    	</@backend>