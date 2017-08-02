<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="广告管理" 
    js=['/statics/cms/js/common.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/common/hi-common.js',
        '/statics/cms/js/jquery/jquery.json-2.4.min.js',
        '/statics/cms/js/layerly/layer.js',
		'/statics/cms/js/addSingleTemple.js',
		'/statics/backend/js/swfupload/swfupload.js',
		'/statics/backend/js/swfupload/js/fileprogress.js',
		'/statics/backend/js/swfupload/js/handlers.js',
		'/statics/backend/js/swfupload/js/swfupload.queue.js',
		'/statics/cms/js/common/cms_file_upload.js',
		'/statics/cms/js/jquery/jquery.ui.core.js',
		'/statics/cms/js/jquery/jquery.form.js'
       ] 
   css=['/statics/backend/css/style.css',
    	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css'] >
<style type="text/css">
	.container{margin-top:30px}
	.table{text-align:center}
	.table .form-control{box-shadow:none;border:0;cursor:pointer}
</style>	
<form id="inputForm" action="saveSingleTemp.htm" method="post" enctype="multipart/form-data">	

	<input type="hidden" id="templeNameBak" 	value="${templeNameBak}" >
	<input type="hidden" id="positionNameBak" value="${positionNameBak}" >
	<input type="hidden" id="statusBak" value="${statusBak}" >
	<input type="hidden" id="platformTypeBak" 	value="${platformTypeBak}" >
	<input type="hidden" id="typeBak" value="${typeBak}" >
	
	<div class="mt10" id="forms">
	    <div class="box">
	        <div class="box_border">
	            <div class="box_top">
	                <b class="pl15">CMS管理->首页管理->单品团创建管理</b> 
	            </div>
	            <div class="box_center">
	                
	                <div class="box_border sel_singletemp_table">
						<div class="box_top">
							<b class="pl15">模板信息</b>
						</div>
						
						<#if detaillist?exists>
							<#list detaillist as detail>
							
								<#if !detail_has_next>
									<table class="input tabContent">
										<tr>
											<td class="td_right" rowspan="1">位置名称：</td>
											<td>
												<input type="text" maxlength="30" name="cmsSingleTempleDTO.positionName" value="${detail.positionName}" rows="3" 
													size="20" class="input-text lh30 positionName">
												<input type="hidden" name="templeNodeId" value="${detail.singleTempleNodeId}" class="templeNodeId">
											</td>
											
											<td class="td_right" rowspan="1">位置尺寸：</td>
											<td>
												<input type="text" name="cmsSingleTempleDTO.positionSize" value="${detail.positionSize}" rows="3" 
													size="20" class="input-text lh30 positionSize">
											</td>
										</tr>
										<tr>
											<td class="td_right" rowspan="1">顺序：</td>
											<td>
												<input type="text" name="cmsSingleTempleDTO.positionSort" value="${detail.positionSort}" rows="3" 
													size="20" class="input-text lh30 positionSort">
											</td>
											
											<td class="td_right" rowspan="1">埋点编码：</td>
											<td>
												<input type="text" name="cmsSingleTempleDTO.buriedCode" value="${detail.buriedCode}" rows="3" 
													size="20" class="input-text lh30 buriedCode">
											</td>
										</tr>
										<tr>
											<td >
			                                    <input type="button" name="button" class="btn btn82 btn_add singletemple_list_add" value="增加">
			                                </td>
			                                <td>
			                                    <input type="button" name="button" class="btn btn82 btn_del singletemple_list_del" value="移除">
			                                </td>
										</tr>
									</table>
								</#if>
								<#if detail_has_next>
									<table class="input tabContent">
										<tr>
											<td class="td_right" rowspan="1">位置名称：</td>
											<td>
												<input type="text" maxlength="30" name="cmsSingleTempleDTO.positionName" value="${detail.positionName}" rows="3" 
													size="20" class="input-text lh30 positionName">
												<input type="hidden" name="templeNodeId" value="${detail.singleTempleNodeId}" class="templeNodeId">
											</td>
											
											<td class="td_right" rowspan="1">位置尺寸：</td>
											<td>
												<input type="text" name="cmsSingleTempleDTO.positionSize" value="${detail.positionSize}" rows="3" 
													size="20" class="input-text lh30 positionSize">
											</td>
										</tr>
										<tr>
											<td class="td_right" rowspan="1">顺序：</td>
											<td>
												<input type="text" name="cmsSingleTempleDTO.positionSort" value="${detail.positionSort}" rows="3" 
													size="20" class="input-text lh30 positionSort">
											</td>
											
											<td class="td_right" rowspan="1">埋点编码：</td>
											<td>
												<input type="text" name="cmsSingleTempleDTO.buriedCode" value="${detail.buriedCode}" rows="3" 
													size="20" class="input-text lh30 buriedCode">
											</td>
										</tr>
										<tr>
											<td style="display:none">
			                                    <input type="button" name="button" class="btn btn82 btn_add singletemple_list_add" value="增加">
			                                </td>
			                                <td >
			                                    <input type="button" name="button" class="btn btn82 btn_del singletemple_list_del" value="移除">
			                                </td>
										</tr>
									</table>
								</#if>	
									
							</#list>
						</#if>
		
					</div>
					<br>
					<div class="tc">
						
						<table class="tab_temple_info">
							<tr>
								<td class="td_right" rowspan="1">模板名称：</td>
								<td>
									<input type="text" name="templeName" maxlength="50" value="${templeName}" rows="3" 
										size="20" class="input-text lh30 templeName">
									<input type="hidden" name="idd" value="${id}" class="tempId">
								</td>
								<td class="td_right">模板路径：</td>
								<td><input type="text" name="templePath" value="${templePath}"
									class="input-text lh30 templePath" size="20" readonly="readonly"></td>
								<td class="td_right">模板状态：</td>
								<td class=""><span class="fl"> <select
										name="status" class="select status">
											<option value="0" <#if "${status}"=="0"> selected</#if>>启用</option>
											<option value="1" <#if "${status}"=="1"> selected</#if>>未启用</option>
									</select>
								</span></td>
								
								<td class="td_right">
									<input type="file" id= "ftltemple" name="ftltemple" class="input-text lh30 ftltemple" size="10">
                  					<input type="hidden" value="ftltemple" name="fieName" id= "ftltemplename" />
                  				</td>
							</tr>
							
							<tr>
								<td class="td_right" rowspan="1">模板类型：</td>
								<td class=""><span class="fl"> <select
										name="type" class="select type">
											<option value="10" <#if "${type}"=="10"> selected</#if>>单品团(pc)</option>
											<option value="15" <#if "${type}"=="15"> selected</#if>>海淘首页(pc)</option>
											<option value="16" <#if "${type}"=="16"> selected</#if>>西客商城首页-热销榜单(pc)</option>
											<option value="17" <#if "${type}"=="17"> selected</#if>>西客商城首页-品牌精选(pc)</option>
											<option value="102" <#if "${type}"=="102"> selected</#if>>单品团(APP)</option>
											<option value="206" <#if "${type}"=="206"> selected</#if>>秒杀-单品团(APP)</option>
											<option value="207" <#if "${type}"=="207"> selected</#if>>WAP-今日精选-单品(APP)</option>
											<option value="208" <#if "${type}"=="208"> selected</#if>>WAP今日精选-专场(APP)</option>
									</select>
								</span></td>
								
								<td class="td_right">平台标识：</td>
								<td class=""><span class="fl"> <select
										name="platformType" class="select platformType">
											<option value="0" <#if "${platformType}"=="0"> selected</#if>>PC</option>
											<option value="1" <#if "${platformType}"=="1"> selected</#if>>APP</option>
									</select>
								</span></td>
							</tr>
							
							<tr>
								<td class="td_right" colspan="6"><font color="red"> 
									注意：APP的不需要模板上传功能，暂时只有PC的需要模板上传</font></td>
							</tr>
							
							<!--tr>
								<#include "/cms/uplode/add_file.ftl">
							</tr-->
						</table>
						
						
					</div>
					<br>
					<div class="tc">
							<input type="button" id="inputFormPreviewBtn" value="预览" class="ext_btn ext_btn_preview m10">
							<input type="button" id="inputFormSaveBtn" value="保存" class="ext_btn ext_btn_submit m10">
							<input type="button" value="返回" id="returnPage"	class="ext_btn m10">
					</div>
	                
	           </div>
	        </div>
	    </div>
	</div>

	<!-- 专门拷贝用的table -->
	<table class="kptabContent" style="display:none">
		<tr>
			<td class="td_right" rowspan="1">位置名称：</td>
			<td>
				<input type="text" name="positionName" maxlength="30" value="${detail.positionName}" rows="3" 
					size="20" class="input-text lh30 positionName">
				<input type="hidden" name="templeNodeId" value="${detail.singleTempleNodeId}" class="templeNodeId">
			</td>
			
			<td class="td_right" rowspan="1">位置尺寸：</td>
			<td>
				<input type="text" name="positionSize" value="${detail.positionSize}" rows="3" 
					size="20" class="input-text lh30 positionSize">
			</td>
		</tr>
		<tr>
			<td class="td_right" rowspan="1">顺序：</td>
			<td>
				<input type="text" name="positionSort" value="${detail.positionSort}" rows="3" 
					size="20" class="input-text lh30 positionSort">
			</td>
			
			<td class="td_right" rowspan="1">埋点编码：</td>
			<td>
				<input type="text" name="buriedCode" value="${detail.buriedCode}" rows="3" 
					size="20" class="input-text lh30 buriedCode">
			</td>
		</tr>
		<tr>
			<td >
                <input type="button" name="button" class="btn btn82 btn_add singletemple_list_add" value="增加">
            </td>
            <td>
                <input type="button" name="button" class="btn btn82 btn_del singletemple_list_del" value="移除">
            </td>
		</tr>
	</table>
</form>
</@backend>
