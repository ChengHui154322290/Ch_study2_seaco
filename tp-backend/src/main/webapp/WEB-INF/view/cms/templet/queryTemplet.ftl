
  <div class="box_border"  style="display:none;overflow-y:auto;overflow-x:hidden;height:400px;" id="templet_type_pop" > 
	  <div class="box_center">
	      <p>
		     <input type="text" class="input-text lh30 dev_name" size="20"/>
		     <input type="hidden" id="dev_page_id" value="${page.pageId}" class="input-text lh30 dev_page_id" size="20"/>
		     <input id="dev_templet_type_query" class="ext_btn" type="button" value="查询">
		     <input id="dev_templet_type_close" class="ext_btn" type="button" value="关闭">
		  </p>
		  <table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ dev_templettable" id="CRZ1">
		     <tr>
			     <!--th style="width:10%;"-->
			      <th>模块ID</th>
			     <th>模块名称</th>
			     <th>模块编号</th>
			     <th>元素类型</th>
			     <th>元素数量</th>
		     </tr>
			 <tr  class="temp_dev_tr_templet">
			 	<!--td><input class="dev_ck" type="checkbox" name="dev_announce_check" /></td-->
			 	<td class="td_center"><span class="pop_templet_id"></span></td>
			 	<td class="td_center"><span class="pop_templet_name"></span></td>
			 	<td class="td_center"><span class="pop_templet_code"></span></td>
			 	<td class="td_center"><span class="pop_templet_emttype"></span></td>
			 	<td class="td_center"><span class="pop_templet_num"></span></td>
			 </tr>
		  </table>
	  </div>
  </div>
  