<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="广告管理" 
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
		
		'/statics/cms/js/addRuleRedis.js',
		
		'/statics/cms/js/jquery/jquery.ui.core.js',
		'/statics/cms/js/jquery/jquery.form.js'
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
<input type="hidden" value="${sessionId}" id="sessionId" />
<form id="inputForm" action="saveAdvertiseTemp.htm" method="post" enctype="multipart/form-data">	

	<input type="hidden" id="functionNameBak" 	value="${functionNameBak}" >
	<input type="hidden" id="areaBak" value="${areaBak}" >
	<input type="hidden" id="platformTypeBak" value="${platformTypeBak}" >
	<input type="hidden" id="statusBak" 	value="${statusBak}" >
	
	<div class="mt10" id="forms">
	    <div class="box">
	        <div class="box_border">
	            <div class="box_top">
	                <b class="pl15">CMS管理->首页管理->缓存规则管理</b> 
	            </div>
	            <div class="box_center">
	                
	                <div class="box_border">
						<div class="box_top">
							<b class="pl15">详情信息</b>
						</div>
						<table class="input tabContent">
							<tr>
								<td class="td_right" rowspan="1"><span class="requiredField">*</span>功能名称：</td>
								<td>
									<input type="text" name="functionName" maxlength="30" value="${detail.functionName}" rows="3" 
										size="20" class="input-text lh30 functionName">
									<input type="hidden" name="idd" value="${detail.id}" class="idd">
								</td>
								
								<td class="td_right" rowspan="1"><span class="requiredField">*</span>功能代码：</td>
								<td>
									<input type="text" name="functionCode" maxlength="30" value="${detail.functionCode}" rows="3" 
										size="20" class="input-text lh30 functionCode">
								</td>
								
								<td class="td_right">平台类型：</td>
								<td>
									<span class="fl"> 
										<select name="platformType" class="select platformType" onChange="oncl()">
											<option value="PC" <#if "${detail.platformType}"=="PC"> selected</#if>>PC</option>
											<option value="APP" <#if "${detail.platformType}"=="APP"> selected</#if>>APP</option>
										</select>
									</span>
								</td>
                                
							</tr>
							<tr>
								<td class="td_right"><span class="requiredField">*</span>地区：</td>
								<td class="">
									<input type="text" name="area" id="area" value="${detail.area}" class="input-text lh30 area" size="20">
								</td>
								
								<td class="td_right"><span class="requiredField">*</span>地区代码：</td>
								<td class="">
									<input type="text" id="areaCode" name="areaCode" value="${detail.areaCode}" rows="3" 
										size="20" class="input-text lh30 areaCode">
								</td>
								
								<td class="td_right">状态：</td>
								<td>
									<span class="fl"> 
										<select name="status" class="select status" onChange="oncl()">
											<option value="0" <#if "${detail.status}"=="0"> selected</#if>>启用</option>
											<option value="1" <#if "${detail.status}"=="1"> selected</#if>>禁用</option>
										</select>
									</span>
								</td>
								
							</tr>
							
							<tr>
								<td class="td_right"><span class="requiredField">*</span>一级缓存：</td>
								<td class="">
                               	 	<input name="firstKey" id="firstKey" value="${detail.firstKey}" class="input-text lh30 firstKey" size="20">
								</td>
								
								<td class="td_right"><span class="requiredField">*</span>二级缓存：</td>
								<td class="">
                               	 	<input name="secondKey" id="secondKey" value="${detail.secondKey}" class="input-text lh30 secondKey" size="20">
								</td>
								
								<td class="td_right"><span class="requiredField">*</span>页数：</td>
								<td class="">
                               	 	<input name="page" id="page" value="${detail.page}" class="input-text lh30 page" size="20">
								</td>
								
							</tr>
							
						</table>
					</div>
					
					<div class="tc">
							<input type="button" id="inputFormSaveBtn" value="保存" class="ext_btn ext_btn_submit m10">
							<input type="button" id="returnPage" value="返回" class="ext_btn m10">
					</div>
					
	           </div>
	        </div>
	    </div>
	</div>
</form>
</@backend>
