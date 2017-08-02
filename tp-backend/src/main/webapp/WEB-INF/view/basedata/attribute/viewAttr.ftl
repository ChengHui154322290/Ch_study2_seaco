<#include "/common/common.ftl"/>
<@backend title="" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/basedata/attribute.js',
'/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js'
] 
 css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
'/statics/backend/js/jqgrid/css/ui.jqgrid.css'
 ] >
<input type='hidden' value='${checkValue}' id='listValue'/>
	 <div id="forms" class="mt10">
        <div class="box">
          <div class="box_border"> 
             <form class="jqtransform" method="post" id="attributeUpdate">
                   <input type="hidden" name="id" class="input-text lh25" size="20" value='${attribute.id}'/>
           <table class="form_table" border="0" cellpadding="0" cellspacing="0">
               <tr>
               <td valign="top" style='border: 1px dashed #BBBBBB;width:50%;'>
                 <table class="form_table"   cellpadding="0" cellspacing="0">
                   <tr>
				     <td>属性编号:</td>
				     <td>${attribute.code}</td>
		           </tr>         
                 
	           <tr>
			<td>属性名称:</td>
				<td><input type="text" name="name" class="input-text lh25" size="20" value='${attribute.name}'></td>
		</tr>
		<tr>
			<td>备注:</td>
		    <td><input type="text" name="remark" class="input-text lh25" size="20" value='${attribute.remark}'></td>
		</tr>
		
		<tr>
			<td valign="middle">状态:</td>
				<td valign="middle" align="left">
					<input id="at5" type="radio" name="status" <#if attribute.status=="1"> checked="checked"</#if> value="1"/>
					<label for="at5" >有效</label>
					<input id="at6" type="radio" name="status" <#if attribute.status!="1">checked</#if> value="0"/>
					<label for="at6" >无效</label>
				</td>
			</tr>
			<tr><td>----属性特征----</td></tr>

			<tr>
			     <td valign="middle">是否必输:</td>
			     <td valign="middle" align="left">
					<input id="at5" type="radio" name="isRequired" <#if attribute.isRequired=="1">checked</#if> value="1"/>
					<label for="at5" >必须</label>
					<input id="at6" type="radio" name="isRequired" <#if attribute.isRequired!="1">checked</#if> value="0"/>
					<label for="at6" >无须</label>
				</td>
			</tr>
			<tr>
			    <td valign="middle">是否多选:</td>
			    <td valign="middle" align="left">
					<input id="at5" type="radio" name="allowMultiSelect" <#if attribute.allowMultiSelect=="1">checked</#if> value="1"/>
					<label for="at5" >多选</label>
					<input id="at6" type="radio" name="allowMultiSelect" <#if attribute.allowMultiSelect!="1">checked</#if> value="0"/>
					<label for="at6" >不多选</label>
				</td>
			</tr>

			<tr>
			   <td  class="td_center">
			           <input class="btn btn82 btn_del closebtn" type="button" value="取消" name="button">
			   </td>
			</tr>
			</table>
			</td>
			<td valign="top" style='border: 1px dashed #BBBBBB;width:50%;'>   
			   <table>
			   <#if listOfCategory?default([])?size !=0>
			   <tr>
			     <th>类别id</th>
			     <th>类别名称</th>
			     <th>类别状态</th>
			   </tr>
			   </#if>
			    <#list listOfCategory as att>
                <tr class="tr">
                   <td class="td_center">${att.id}</td>
                   <td class="td_center">${att.name}</td>       
                   <td class="td_center"> ${(att.status==1) ?string("有效", "无效")}</td>
                </tr>
                 </#list>
			   </table>
			</td>
			</tr>
			</form>
		</div>
    </div>
</div>
	</@backend>