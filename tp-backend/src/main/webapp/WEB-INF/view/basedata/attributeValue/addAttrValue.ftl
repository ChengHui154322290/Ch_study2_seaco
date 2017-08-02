<#include "/common/common.ftl"/>
<@backend title="" js=[
'/statics/backend/js/basedata/attributeValue.js',
'/statics/backend/js/layer/layer.min.js'
] 
css=[] >
 
 <!--新增属性值-->
  <form class="jqtransform" method="post" id="cataValueAdd">
	 <div id="forms" class="mt10">
        <div class="box">
          <div class="box_border">属性名称:${attribute.name}
            <div class="box_top"><b class="pl15">属性项</b></div>
            <div class="box_center pt10 pb10">
           
               <table class="form_table" border="0" cellpadding="0" cellspacing="0">     
   	 	    <tr>
             <td><input type="text" name="name" class="input-text lh25" size="20" maxlength="20"></td>     
                <td>
                   <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="status" class="select"> 
                        <option value="1" >有效</option> 
                        <option value="0" >无效</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                </td>
                <input type='hidden' name='attributeId' value='${attribute.id}'>
                <td> 
                <input class="btn btn82 btn_save2" type="button" value="保存" name="button" param='/basedata/attributeValue/saveAttrValue.htm' onclick="saveAttrValue(this)" >
                </td>
                <td> 
                <input class="btn btn82 btn_save2" type="button" value="日志" name="button">
                </td>
           </tr>
    </table>  
    </div>
     </div>
      </div>
       </div>
        </div>
        </form>
    	</@backend>