<#include "/common/common.ftl"/>
<@backend title="" js=['/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/basedata/category.js'] 
                	css=[] >

<div class="mt10">
        <div class="box">
          <div class="box_border">
            <div class="box_center pt10 pb10">
              <form class="jqtransform" method="post" id="catEdit">
               <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
            <!--    <input type='hidden' name='level' value='1'> -->
                <input type='hidden' name='parentId' value='0'>
                <input type='hidden' name='id' value='${category.id}'>
                
					<td>类别:</td>
					<td> 大类</td>
				</tr>
				<tr>
					<td>类别编号</td>
					<td>
						 系统自动生成
					</td>
				</tr>
				
				<tr>
					<td>类别名称</td>
					<td>
						<input type="text" name="name" class="input-text lh25" size="20"  value="${category.name}" maxlength="50"/>
					</td>
				</tr>
		
				<tr>
					<td>备注</td>
					<td>
						<input type="text" name="remark" class="input-text lh25" size="20" value="${category.remark}" maxlength="250"/>
					</td>
				</tr>
				<tr>
				<td valign="middle">状态:</td>
				<td valign="middle" align="left">
					<input id="at5" type="radio" name="status" <#if category.status=="1"> checked="checked"</#if>  value="1"/>
					<label for="at5" >有效</label>
					<input id="at6" type="radio" name="status" <#if category.status !="1"> checked="checked"</#if> value="0"/>
					<label for="at6" >无效</label>
				</td>
				</tr>

			</table>
			
		<div id="div1_submit" style="text-align:center;">
	
			<input class="btn btn82 btn_save2" type="button" value="保存" param='/basedata/category/update.htm' onclick="cateUpdate(this)" />
    		<input class="btn btn82 btn_del closebtn" type="button" value="取消" name="button1" />
		</div>
			</form>
		</div>
	</div>
</div>
</div>

</@backend>