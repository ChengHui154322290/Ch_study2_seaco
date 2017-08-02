<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="元素管理" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
        
		'/statics/cms/js/common/time/js/jquery-ui-1.9.2.custom.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-addon.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-zh-CN.js',
		
        '/statics/cms/js/jquery/jquery.json-2.4.min.js',
        
		'/statics/backend/js/swfupload/swfupload.js',
		'/statics/backend/js/swfupload/js/fileprogress.js',
		'/statics/backend/js/swfupload/js/handlers.js',
		'/statics/backend/js/swfupload/js/swfupload.queue.js',
		'/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
		'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
		'/statics/cms/js/common/cms_file_upload.js',
		'/statics/cms/js/jquery/jquery.ui.core.js',
		'/statics/cms/js/jquery/jquery.form.js',
		
		'/statics/cms/js/templet/pictureElementEdit.js'
       ] 
    css=['/statics/backend/css/style.css',
    	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css',
    	'/statics/cms/js/common/time/css/jquery-ui-timepicker-addon.css',
    	'/statics/cms/js/common/time/css/jquery-ui-1.8.17.custom.css'] >
<style type="text/css">
	.container{margin-top:30px}
	.table{text-align:center}
	.table .form-control{box-shadow:none;border:0;cursor:pointer}
</style>
	<div class="mt10" id="forms">
	 	
	 	<input type="hidden" id="pageNameBak" 	value="${pageNameBak}" >
		<input type="hidden" id="templeNameBak" value="${templeNameBak}" >
		<input type="hidden" id="positionNameBak" value="${positionNameBak}" >
	
	    <div class="box">
	        <div class="box_border">
	            <div class="box_top">
	                <b class="pl15">CMS管理->首页管理->元素管理(活动)</b> 
	            </div>
	            <div class="box_center">
	                
	                <div class="box_border">
						<div>
							<table class="tabContent_lab">
								<tr>
									<td class="td_right" rowspan="1">页面名称：</td>
									<td>
										<input type="text" id="pageName" name="pageName" value="${pageName}" readonly="readonly"
											size="20" class="input-text lh30 pageName" disabled="true">
										<input type="hidden" id="positionId" value="${positionId}"/>
									</td>
									<td class="td_right">模块名称：</td>
									<td>
										<input type="text" id="templeName" name="templeName" value="${templeName}" readonly="readonly"
										class="input-text lh30 templeName" size="20" disabled="true">
									</td>
									<td class="td_right">位置名称：</td>
									<td>
										<input type="text" id="positionName" name="positionName" value ="${positionName}" readonly="readonly"
										class="input-text lh30 positionName" size="20" disabled="true">
									</td>
								</tr>
							</table>
						</div>
						
						<div class="box_top">
							<b class="pl15">添加元素</b>
						</div>
						<table cellspacing="0" cellpadding="0" border="0" width="100%"
	                        class="form_table pt15 pb15">
	                        <tbody>
	                            <tr>
	                                <td colspan="10">
	                                    <table width="100%" cellspacing="0" cellpadding="0" border="0"
				                            class="list_table CRZ customertable">
				                            <thead>
												<tr>
				                                    <!--th>序号</th-->
				                                    <th>图片名称</th>
				                                    <th>图片属性</th>
				                                    <th>活动名称</th>
				                                    <th>跳转地址</th>
				                                    <th>启用时间</th>
				                                    <th>失效时间</th>
				                                    <th>当前状态</th>
				                                    <th>展示图片</th>
				                                    <th>卷帘图片</th>
				                                    <th>操作</th>
												</tr>
											</thead>
											<tbody>
											
												<#if detaillist?exists>
													<#list detaillist as detail>
													
														<tr class="temp_tr_singleActivitytemp">
															<!--td class="td_center"><span class="activityCode">${detail.activityCode}</span></td-->
															<td class="td_center"><span class="name">${detail.name}</span></td>
															<td class="td_center"><span class="attr">${detail.attr}</span></td>
															<td class="td_center"><span class="actName">${detail.actname}</span></td>
															<td class="td_center"><span class="link">${detail.link}</span></td>
															<td class="td_center"><span class="startdate">${detail.startdate?string("yyyy-MM-dd HH:mm:ss")}</span></td>
															<td class="td_center"><span class="enddate">${detail.enddate?string("yyyy-MM-dd HH:mm:ss")}</span></td>
															<td class="td_center"><span class="status">
																<#if (0 == detail.status)>
																	启用
																<#elseif (1 == detail.status)>
																	停用
																</#if>
															</span></td>
															<td class="td_center"><span class="picSrcBak">
																<a href="javascript:void(0);"><img id="imgPrivewsrc" src="${detail.picSrcStr}" width="100" height="100" /></a>
															</span></td>
															<td class="td_center"><span class="picSrcBak">
																<a href="javascript:void(0);"><img id="rollPrivewsrc" src="${detail.rollPicSrcStr}" width="100" height="100" /></a>
															</span></td>
															<td class="td_center">
																<a href="javascript:;" class="editAtt" param='${att.id}'>【修改】</a>|
																<a href="javascript:;" class="delLocalActivity" param='${detail.id}'>【移除】</a>
															</td>
															<td style="display:none;"><span type="hidden" class="statusCode">${detail.status}</span></td>
															<td style="display:none;"><span type="hidden" class="picSrc">${detail.picSrc}</span></td>
															<td style="display:none;"><span type="hidden" class="rollPicSrc">${detail.rollPicSrc}</span></td>
															<td style="display:none;"><span type="hidden" class="picId">${detail.id}</span></td>
															
															<td style="display:none;"><span type="hidden" class="sku">${detail.sku}</span></td>
															<td style="display:none;"><span type="hidden" class="actType">${detail.acttype}</span></td>
															<td style="display:none;"><span type="hidden" class="activityId">${detail.activityid}</span></td>
														</tr>
														
													</#list>
												</#if>
											
											</tbody>
				                        </table>
	                                </td>
	                            </tr>
	                        </tbody>
	                    </table>
						
						
					</div>
					<br><br>
					
					
					
				<input type="hidden" value="${sessionId}" id="sessionId" />
				<form id="inputForm" method="post" enctype="multipart/form-data">	
					<div class="box_border">
						<div class="box_top">
							<b class="pl15">添加图片</b>
						</div>
						
						<#include "/cms/img/add_Element_pictures.ftl">
						
						<table cellspacing="0" cellpadding="0" border="0" width="100%"
	                        class="form_table pt15 pb15">
	                        <tbody>
	                         	  <tr>
	                         	 	<td colspan="1" class="td_right" width="50" align="right">广告图片名称：</td>
	                                <td class="" width="50" align="left"><input type="text" id="name"
	                                    name="name" class="input-text lh30 name" size="17"></td>
	                                    
	                                 <td colspan="1" class="td_right" width="50" align="right">广告图片属性：</td>
	                                 <td class="" width="50" align="left"><input type="text" id="attr"
	                                    name="attr" class="input-text lh30 attr" size="20"></td>
	                                    
	                                 <input type="hidden" name="positionId" value="${positionId}"/>
	                                    
	                             </tr>
	                         	 
	                         	 <tr>
	                         	 	<td colspan="1" class="td_right" width="50" align="right">活动名称：</td>
	                                <td class="" width="50" align="left"><input type="text" id="actName"
	                                    name="actName" class="input-text lh30 actName" size="17"></td>
	                                
	                                <td colspan="1" class="td_right" width="50" align="right">跳转链接：</td>
	                                <td class="" width="50" align="left"><input type="text" id="link"
	                                    name="link" class="input-text lh30 link" size="17"></td>
	                             </tr>
	                             
	                             <tr>
	                         	 	<td colspan="1" class="td_right" width="50" align="right">开始时间：</td>
	                                <td class="" width="50" align="left"><input type="text" id="startdate"
	                                    name="startdate" class="input-text lh30 startdate" size="17"></td>
	                                    
	                                <td colspan="1" class="td_right" width="50" align="right">结束时间：</td>
	                                <td class="" width="50" align="left"><input type="text" id="enddate"
	                                    name="enddate" class="input-text lh30 enddate" size="17"></td>
	                             </tr>
	                             
	                             <tr>
	                             	<td colspan="1" class="td_right" width="50" align="right">图片上传地址：</td>
	                                <td class="" width="50" align="left"><input type="text" readonly="readonly" id="picSrc"
	                                    name="picSrc" class="input-text lh30 picSrc" size="17"></td>
	                                    
	                         	 	<td colspan="1" class="td_right" width="50" align="right">状态：</td>
	                         	 	<td>
							            <div class="select_border">
						                	<div class="select_containers">
					                            <span class="fl"> <select class="select status" name="status" id="status" 
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
	                         	 	<td colspan="1" class="td_right" width="50" align="right">活动类型：</td>
	                                <td>
										<select name="actType" id ="actType" class="select actType">
											<#list actTypeList as actTypeItem>
												<option value="${actTypeItem.value}">${actTypeItem.description}</option>
											</#list>
										</select>
									</td>
	                                    
	                                <td colspan="1" class="td_right" width="50" align="right">活动id：</td>
	                                <td><input type="text" name="activityId" id ="activityId"
										class="input-text lh30 activityId" width="50" ></td>
	                             </tr>
	                             
	                             <tr>
	                             	<td colspan="1" class="td_right" width="50" align="right">sku：</td>
	                                <td><input type="text" name="sku" id ="sku"
										class="input-text lh30 sku" width="50" ></td>
										
	                         	 	<td colspan="1" class="td_right" width="50" align="right">表ID：</td>
	                                <td class="" width="50" align="left"><input type="text" id="picElementId" disabled="disabled"
	                                     class="input-text lh30 picElementId" size="17"></td>
	                                 
	                                 <input type="hidden" name="picId" id="picId"/>
	                                    
	                             </tr>
	                             
	                             <tr>
	                         	 	<td colspan="8" align="center">
	                                	<input type="reset" name="button" id="returnPage" class="btn btn82 btn_res resetButtonInput" value="返回">
	                                	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
	                                    <input type="button" name="button" id="createBtn" class="btn btn82 btn_search" value="创建">
	                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
	                                    <input type="button" name="button" id="inputFormSaveBtn" class="btn btn82 btn_search" value="保存">
	                                </td>  
	                         	 </tr>
	                         	 
								<tr>
	                                <td colspan="8">
	                                    <hr style="border: 0.1px dashed #247DFF;" />
	                                </td>
	                            </tr>
	                            
	                            <tr>
	                            	<td valign="top" style="height:100px;">
										<input type="hidden" name="topic.image" id="pcImage" value="${topicDetail.topic.image}" />
										<img id="rollImageImg" style="margin-left:10px;width:120px;height:80px;" src="${domain + '/statics/backend/images/wait_upload.png'}"/>
										<!--div style="float:right;margin-right:5px;margin-top:85px;">
											<a id="removePcImage" href="javascript:void(0);">X</a>
										</div-->
										<div style="width:100%;text-align:center;height:20px;margin-top:5px;">
											如果图片有卷帘效果，请在此处上传卷帘效果的大图
										</div>
									</td>
									
									<td colspan="1" class="td_right" width="50" align="right">卷帘图片上传地址：</td>
	                                <td class="" width="50" align="left"><input type="text" readonly="readonly" id="rollPicSrc" readonly="readonly"
	                                    name="rollPicSrc" class="input-text lh30 rollPicSrc" size="17"></td>
	                                    
	                                <td colspan="1" align="center">
	                                	<input name="button" id="clearRollPic" class="btn btn82" value="清除地址">
	                                </td>  
	                            </tr>
	                        </tbody>
	                    </table>
                                
					</div>
				</form>
				
				
					
	           </div>
	        </div>
	    </div>
	</div>

</@backend>
