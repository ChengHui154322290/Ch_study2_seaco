<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="类型管理" 
    js=['/statics/cms/js/common.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
		
        '/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/cms/js/base/addAdvertType.js'
       ] 
    css=['/statics/backend/css/style.css'] >
<style type="text/css">
	.container{margin-top:30px}
	.table{text-align:center}
	.table .form-control{box-shadow:none;border:0;cursor:pointer}
</style>
<form id="inputForm" action="saveAdvertType.htm" method="post">	
	<div class="mt10" id="forms">
	    <div class="box">
	        <div class="box_border">
	            <div class="box_top">
	                <b class="pl15">CMS管理->首页管理->图片类型管理</b> 
	            </div>
	            <div class="box_center">
	                
	                <div class="box_border">
						<div class="box_top">
							<b class="pl15">详情信息</b>
						</div>
						<table class="input tabContent">
							<tr>
								<td class="td_right" rowspan="1"><span class="requiredField">*</span>类型名称：</td>
								<td>
									<input type="text" name="name" maxlength="30" value="${detail.name}" rows="3" 
										size="20" class="input-text lh30 name">
									<input type="hidden" name="idd" value="${detail.id}" class="idd">
								</td>
								
								<td class="td_right">接口标识：</td>
								<td>
									<input type="text" name="ident" maxlength="30" value="${detail.ident}" rows="3" 
										size="20" class="input-text lh30 ident">
								</td>
							</tr>
							<tr>
								<td class="td_right"><span class="requiredField">*</span>是否启用：</td>
								<td class=""><span class="fl"> <select
										name="status" class="select status">
											<option value="0" <#if "${detail.status}"=="0"> selected</#if>>启用</option>
											<option value="1" <#if "${detail.status}"=="1"> selected</#if>>禁用</option>
									</select>
								</span></td>
								<td class="td_right"></td>
								<td class="">
									
								</td>
								
							</tr>
						</table>
					</div>
					
					<div class="tc">
							<input type="button" id="inputFormSaveBtn" value="保存" class="ext_btn ext_btn_submit m10">
							<!--input type="hidden" value="${detailId}" name="detail.id" />
							<input type="hidden" name="skuListJson"  id="skuListJson"/-->
							<input type="button" value="返回" onclick="location.href='javascript:history.go(-1)'"	class="ext_btn m10">
					</div>
	                
	           </div>
	        </div>
	    </div>
	</div>
</form>
</@backend>
