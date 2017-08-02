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
	 <div id="forms" class="mt10">
        <div class="box">
          <div class="box_border"> 
             <form class="jqtransform" method="post" id="attributeAdd">
          <table class="form_table" border="0" cellpadding="0" cellspacing="0">
               <tr>
               <td valign="top" style='border: 1px dashed #BBBBBB;width:50%;'>
                 <table class="form_table"   cellpadding="0" cellspacing="0">
		<tr>
			<td>属性名称:</td>
				<td><input type="text" name="name" class="input-text lh25" size="20" maxlength="50"/></td>
		</tr>
		<tr>
			<td>备注:</td>
		    <td><input type="text" name="remark" class="input-text lh25" size="20" maxlength="250" /></td>
		</tr>
		
		<tr>
			<td valign="middle">状态:</td>
				<td valign="middle" align="left">
					<input id="at1" type="radio" name="status" checked='checked' value="1"/>
					<label for="at1" >有效</label>
					<input id="at2" type="radio" name="status" value="0"/>
					<label for="at2" >无效</label>
				</td>
			</tr>
			<tr><td>----属性特征----</td></tr>

			<tr>
			     <td valign="middle">是否必输:</td>
			     <td valign="middle" align="left">
					<input id="at3" type="radio" name="isRequired"  checked='checked' value="1"/>
					<label for="at3" >必须</label>
					<input id="at4" type="radio" name="isRequired" value="0"/>
					<label for="at4" >无须</label>
				</td>
			</tr>
			<tr>
			    <td valign="middle">是否多选:</td>
			    <td valign="middle" align="left">
					<input id="at5" type="radio" name="allowMultiSelect"  value="1"/>
					<label for="at5" >多选</label>
					<input id="at6" type="radio" name="allowMultiSelect" checked='checked'  value="0"/>
					<label for="at6" >不多选</label>
				</td>
			</tr>

			<tr>
			   <td>
			       <div id="div1_submit" style="text-align:center;">
					   <input class="btn btn82 btn_save2" type="button" value="保存"  param='/basedata/attribute/saveAdd.htm' onclick="saveAddAtt(this)">			         
				   </div>
			   </td>
			   <td>
			       <div id="div1_submit" style="text-align:center;">
			           <input class="btn btn82 btn_del closebtn" type="button" value="取消" name="button">
				   </div>
			   </td>
			</tr>
			</table>
			</td>
			<td valign="top" style='border: 1px dashed #BBBBBB;width:50%;'>
			    <table id="tree"></table>
			</td>
			</tr>
			</form>
		</div>
    </div>
</div>
	</@backend>