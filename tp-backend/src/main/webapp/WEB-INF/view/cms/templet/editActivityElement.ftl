<form id="inputForm" method="post" enctype="multipart/form-data">	  
  <div class="box_border"  style="display:none;overflow-y:auto;overflow-x:hidden;height:400px;" id="advert_type_pop" > 
	  <div class="box_center">
	      <p>
		     <input id="add_activityElementId" class="ext_btn" type="button" value="保存">
		     <input id="dev_advert_type_close" class="ext_btn" type="button" value="关闭">
		  </p>
		  
		 <table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ dev_customertable" id="CRZ0">
			<tr>
				<td class="td_right" rowspan="1">启用时间：</td>
				<td>
					<input type="text" name="startdatebak" id="startdatebak"
						size="20" class="input-text lh30 startdatebak">
					<input type="hidden" id="layer_Id" name="layer_Id" />
					<input type="hidden" id="position_layer_Id" name="position_layer_Id" />
					<input type="hidden" id="activity_layer_Id" name="activity_layer_Id" />
					<input type="hidden" id="fgStatus" name="fgStatus" />
				</td>
			 </tr>
			<tr>
				<td class="td_right" rowspan="1">失效时间：</td>
				<td>
					<input type="text" name="enddatebak" id="enddatebak"
						size="20" class="input-text lh30 enddatebak">
				</td>
			 </tr>
			 <tr>
				<td class="td_right"><font color="red">*</font>状态： </td>
	            <td>
		            <div class="select_border">
	                	<div class="select_containers">
                            <span class="fl"> <select class="select statusbak" name="statusbak" 
                                style="width: 130px;">
                                    <option value="">请选择</option>
                                    <option value="0">启用</option>
                                    <option value="1">草稿</option>
                            </select>
                            </span>
                         </div>
		            </div>
	            </td>
			 </tr>
			 <tr>
				<td class="td_right" rowspan="1">顺序：</td>
				<td>
					<input type="text" name="seqbak" 
						size="20" class="input-text lh30 seqbak">
				</td>
			 </tr>
			 <tr>
				<td class="td_right" rowspan="1">跳转链接：</td>
				<td>
					<input type="text" name="linkbak" 
						size="20" class="input-text lh30 linkbak">
				</td>
			 </tr>
			
			<tr>
				<td class="td_right" rowspan="1">上传图片地址：</td>
				<td>
					<input type="text" name="picture" id="picture" readonly="readonly"
						size="20" class="input-text lh30 picture">
						
					<input name="button" id="clearRollPic" class="btn btn82"  value="清除地址">
				</td>
			 </tr>
			 
			<tr>
                <td colspan="2">
                    <hr style="border: 0.1px dashed #247DFF;" />
                </td>
            </tr>
	                            
			<!--tr>
				<#include "/cms/img/add_Element_pictures.ftl">
			</tr-->
			
			<tr>
            	<td colspan="2" valign="top" style="height:100px;">
					<img id="imageImg" style="margin-left:10px;width:120px;height:80px;" src="${domain + '/statics/backend/images/wait_upload.png'}"/>
					
					<img id="imageImg_src" style="margin-left:10px;width:120px;height:80px;" src=""/>
				</td>
            </tr>
            
		</table>
							
							
	  </div>
  </div>
 </form>
