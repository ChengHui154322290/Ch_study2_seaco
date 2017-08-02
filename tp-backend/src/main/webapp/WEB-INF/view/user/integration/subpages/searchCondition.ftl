<table class="form_table" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="50"><div class="pl10">类 型:</div></td>
        <td width="150">
			<span class="fl">
	            <div class="select_border">
					<div class="select_containers ">
						<select name="freightType" class="select" style="size:6;width:80pt" data-v="${freightType}"> 
	                    	<option value="2">全部</option>
	                    	<option value="0">国内</option>
	                    	<option value="1" >海淘</option>
	                    </select>
	                </div>
	            </div> 
            </span>
         </td>
         <td>计算方式:</td>
         <td width="150">
			<span class="fl">
	            <div class="select_border">
					<div class="select_containers ">
						<select name="calculateMode" class="select" style="size:6;width:80pt"  selected="bb" data-v="${calculateMode}"> 
	                    	<option value="2">全部</option>
	                    	<option value="0" id="aa">满包邮</option>
	                    	<option value="1" id="bb">统一邮资</option>
	                    </select>
	                </div>
	            </div> 
            </span>
         </td>
         <td>模板名称:</td>
         <td><input type="text" name="templateName" class="input-text lh25" size="30" data-v="${templateName}"/></td>
	</tr>
</table>

<script>
$('[name=freightType] option').each(function(){
	if( $(this).val() == '${freightType}' ){
		this.selected = true;
		}
});
$('[name=calculateMode] option').each(function(){
	if( $(this).val() == '${calculateMode}' ){
		this.selected = true;
		}
});
</script>
          