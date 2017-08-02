<table class="form_table" border="0" cellpadding="0" cellspacing="0">
	<input type='hidden' name="sceneCode" value="5">
	<tr>
		<td>积分数:</td>
        <td><input type="text" name="point" class="input-text lh25" size="5" data-v="${point}"/></td>
	
		<td width="69"><div class="pl10">固定期限:</div></td>
       	<td valign="middle" align="left">
       		<input id="at5" type="radio" name="isExpiry" data-v="${isExpiry}" checked="checked" value="5"/>
			<label for="at5" >全部</label>&nbsp&nbsp&nbsp
			<input id="at5" type="radio" name="isExpiry" data-v="${isExpiry}" value="0"/>
			<label for="at5" >无</label>&nbsp&nbsp&nbsp
			<input id="at6" type="radio" name="isExpiry" data-v="${isExpiry}" value="1"/>
			<label for="at6" >有</label>
		</td>
        
        <td id="expiryTitle">生效时间:</td>
        <td id="expiryTime" colspan="3">
         	<input type="text" name="createBeginTime" id="createBeginTime" data-v="${createBeginTime}" class="input-text lh25" size="20">
            <span>～</span>
            <input type="text" name="createEndTime" id="createEndTime" data-v="${createEndTime}" class="input-text lh25" size="20">
        </td>
     </tr>
</table>

<script>
	$(function(){
		$("#expiryTime").hide();
		$("#expiryTitle").hide();
	});
</script>

          