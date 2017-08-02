<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="自定义管理" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
        '/statics/backend/js/editor/kindeditor-all.js',
		
		'/statics/cms/js/common/time/js/jquery-ui-1.9.2.custom.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-addon.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-zh-CN.js',
		
		'/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/cms/js/common/editorAnnouncement.js',
		
		'/statics/cms/js/templet/definedElementEdit.js'
       ] 
    css=['/statics/backend/css/style.css',
    	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css',
    	'/statics/cms/js/common/time/css/jquery-ui-timepicker-addon.css',
    	'/statics/cms/js/common/time/css/jquery-ui-1.8.17.custom.css'] >
	<div class="mt10" id="forms">
	 	
	 	<input type="hidden" id="pageNameBak" 	value="${pageNameBak}" >
		<input type="hidden" id="templeNameBak" value="${templeNameBak}" >
		<input type="hidden" id="positionNameBak" value="${positionNameBak}" >
	
	    <div class="box">
	        <div class="box_border">
	            <div class="box_top">
	                <b class="pl15">CMS管理->首页管理->元素管理(自定义)</b> 
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
				                                    <th>区域名称</th>
				                                    <th>内容</th>
				                                    <th>启用时间</th>
				                                    <th>失效时间</th>
				                                    <th>当前状态</th>
				                                    <th>操作</th>
												</tr>
											</thead>
											<tbody>
											
												<#if detaillist?exists>
													<#list detaillist as detail>
													
														<tr class="temp_tr_singleActivitytemp">
															<!--td class="td_center"><span class="activityCode">${detail.activityCode}</span></td-->
															<td class="td_center"><span class="name">${detail.name}</span></td>
															<td class="td_center"><span class="content">${detail.content}</span></td>
															<td class="td_center"><span class="startdate">${detail.startdate?string("yyyy-MM-dd HH:mm:ss")}</span></td>
															<td class="td_center"><span class="enddate">${detail.enddate?string("yyyy-MM-dd HH:mm:ss")}</span></td>
															<td class="td_center"><span class="status">
																<#if (0 == detail.status)>
																	启用
																<#elseif (1 == detail.status)>
																	停用
																</#if>
															</span></td>
															<td class="td_center">
																<a href="javascript:;" class="editAtt" param='${att.id}'>【修改】</a>|
																<a href="javascript:;" class="delLocalActivity" param='${detail.id}'>【移除】</a>
															</td>
															<td style="display:none;"><span type="hidden" class="definedId">${detail.id}</span></td>
															<td style="display:none;"><span type="hidden" class="positionId">${detail.positionId}</span></td>
															<td style="display:none;"><span type="hidden" class="statusCode">${detail.status}</span></td>
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
					
					<div class="box_border">
						<div class="box_top">
							<b class="pl15">添加自定义元素</b>
						</div>
						<table cellspacing="0" cellpadding="0" border="0" width="100%"
	                        class="form_table pt15 pb15">
	                        <tbody>
	                            <tr>
	                                <td colspan="1" class="td_right" width="50" align="right">区域名称：</td>
	                                <td class="" width="50" align="left"><input type="text" id="name"
	                                    name="name" value="${mod.name}" class="input-text lh30 name" size="17"></td>
	
									<td colspan="1" class="td_right" width="50" align="right">状态：</td>
	                         	 	<td>
							            <div class="select_border">
						                	<div class="select_containers">
					                            <span class="fl"> <select class="select status" name="status" id="status" 
					                                style="width: 130px;">
					                                    <option value="">请选择</option>
					                                    <option value="0" <#if "${mod.status}"=="0"> selected</#if>>启用</option>
					                                    <option value="1" <#if "${mod.status}"=="1"> selected</#if>>草稿</option>
					                            </select>
					                            </span>
					                         </div>
							            </div>
						            </td>
	                                    
	                         	 </tr>
	                         	 
	                         	 <tr>
	                         	 	<td colspan="1" class="td_right" width="50" align="right">开始时间：</td>
	                                <td class="" width="50" align="left"><input type="text" id="startdate"
	                                    name="startdate" class="input-text lh30 startdate" value="${mod.startdate?string('yyyy-MM-dd HH:mm:ss')}" size="17"></td>
	
									<td colspan="1" class="td_right" width="50" align="right">结束时间：</td>
	                                 <td class="" width="50" align="left"><input type="text" id="enddate" 
	                                    name="enddate" value="${mod.enddate?string('yyyy-MM-dd HH:mm:ss')}" class="input-text lh30 enddate size="17"></td>
	                         	 </tr>
	                         	 
	                         	 <tr>
						            <td colspan="1" class="td_right" width="50" align="right">表ID：</td>
	                                <td class="" width="50" align="left"><input type="text" id="definedId" disabled="disabled"
	                                     class="input-text lh30 definedId" value="${mod.id}" size="17"></td>
	                                     
	                         	 </tr>
	                         	 
	                         	 <tr rowspan="2">
						            <td class="td_right"><strong style="color:red;">*</strong>内容</td>
									<td colspan="5">
										<div class="box_center">
											<ul id="tabContent" class="tab" >
												<li>
													<input type="button" value="PC模板 " />
												</li>
											</ul>
											<table class="input tabContent"">
												<tr>
													<td>
													    <textarea class="editor" id="pcContentEditor" name="announce.content" style="width: 100%;height:200px;">
													    	${mod.content}
													    </textarea>
													</td>
												</tr>
											</table>
										</div>
									</td>
	                         	 </tr>
	                         	 
	                         	 <tr>
	                         	 	<td colspan="8" align="center">
	                                	<input type="reset" name="button" id="returnPage" class="btn btn82 btn_res resetButtonInput" value="取消">
	                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
	                                    <input type="button" name="button" id="createWritten" class="btn btn82 btn_search" value="创建">
	                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
	                                    <input type="button" name="button" id="addWritten" class="btn btn82 btn_search" value="保存">
	                                </td>  
	                         	 </tr>
	
	                        </tbody>
	                    </table>
					</div>
					
	                
	           </div>
	        </div>
	    </div>
	</div>

</@backend>
