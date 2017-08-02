
  <div class="box_border"  style="display:none;overflow-y:auto;overflow-x:hidden;height:230px;" id="applay_pop" > 
	  <div class="box_center">
		  <table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ table_dev_apply" id="CRZ0">
			 <tr>
				 	<td width="25%" colspan="1" class="td_center" align="center" valign="middle">审核结果：</td>
	                <td class="td_center" align="center" valign="middle">
	                	<div class="select_border">
                            <div class="select_containers">
                                <span class="fl"> <select class="select cmeResultTmp" name="cmeResultTmp" 
                                    style="width: 180px;">
                                        <option id="cancelAttId"  value="3">取消</option>
                                        <option id="examineAttId"  value="4">审核</option>
                                        <option id="rejectAttId"  value="5">驳回</option>
                                        <option id="stopAttId"  value="6">终止</option>
                                </select>
                                </span>
                             </div>
                         </div>
	                </td>
			 	</tr>
			 	
			 	<tr rowspan="5">
					<td width="25%" colspan="1" rowspan="1" class="td_center" align="center" valign="middle">审核意见：</td>
                    <td colspan="1"  class="td_center" align="center" valign="middle" >
                    	<textarea style="height:100px;width:150px" name="cmeSuggestionTmp" class="cmeSuggestionTmp"></textarea>
                    </td>
			 	</tr>
			 	<tr>
			 		<td colspan="2" class="td_center" width="50" align="center" valign="middle">
						<input id="dev_submit" class="ext_btn" type="button" value="提交">
		     			<input id="dev_cancel" class="ext_btn" type="button" value="取消">
		     		</td>
			 	</tr>
		  </table>
	  </div>
  </div>