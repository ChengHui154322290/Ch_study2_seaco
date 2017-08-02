<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="位置节点管理" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
        
        '/statics/cms/js/jquery/jquery.ui.core.js',
		'/statics/cms/js/jquery/jquery.form.js',
		
		'/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/cms/js/templet/positionEdit.js'
       ] 
    css=['/statics/backend/css/style.css',
    		'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css']>
	<div class="box">
        <div class="box_border">
            <div class="box_top">位置节点管理</b>
            </div>
            <div class="box_center" style="margin-bottom: 30px;">
                <form id="inputForm" class="jqtransform" action="savePositionTemp.htm" method="post" >	

					<input type="hidden" id="pageNameBak" 	value="${pageNameBak}" >
					<input type="hidden" id="templeNameBak" value="${templeNameBak}" >
					<input type="hidden" id="positionNameBak" value="${positionNameBak}" >
					
					<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
					    <tbody>
					        <tr>
					            <td class="td_right"><font color="red">*</font>页面名称： </td>
					            <td class=""  width="100">
					            	<input type="text" readonly="readonly" name="pageName" value="${page.pageName}" id="pageName" class="input-text lh30 pageName" size="20">
                               	 	<input type="hidden" name="pageId" value="${page.pageId}" id="pageId" class="input-text lh30 pageId" size="20">
                               	 	<input id="advert_type_query" class="ext_btn" type="button" value="查询">
					            </td>
					            
					            <td class="td_right"><font color="red">*</font>模块名称 </td>
					             <td class=""  width="100">
					             
					             	<input type="text" readonly="readonly" name="templeName" value="${page.templeName}" id="templeName" class="input-text lh30 templeName" size="20">
                               	 	<input type="hidden" name="templeId" value="${page.templeId}" id="templeId" class="input-text lh30 templeId" size="20">
                               	 	<input id="templet_type_query" class="ext_btn" type="button" value="查询">
					            </td>
					        </tr>
					        
					        <tr>
					            <td class="td_right"><font color="red">*</font>位置名称： </td>
					            <td class=""  width="100">
					                <input type="text" maxlength="30" name="positionName" value="${page.positionName}" class="_req input-text lh30 positionName" maxlength="60" size="40">
					            </td>
					            
					            <td class="td_right"><font color="red">*</font>位置编号： </td>
					             <td class=""  width="100">
					                <input type="text" maxlength="30" name="positionCode" value="${page.positionCode}" class="_req input-text lh30 positionCode" maxlength="60" size="40">
					            </td>
					            
					            <input type="hidden" name="id" class="_req input-text lh30 positionId" value="${page.id}" >
					        </tr>
					        
					        <tr>
					            <td class="td_right"><font color="red">*</font>状态： </td>
					            <td>
						            <div class="select_border">
					                	<div class="select_containers">
                                            <span class="fl"> <select class="select status" name="status" 
                                                style="width: 130px;">
                                                    <option value="">请选择</option>
                                                    <option value="0"<#if "${page.status}"=="0"> selected</#if>>启用</option>
                                                    <option value="1"<#if "${page.status}"=="1"> selected</#if>>草稿</option>
                                            </select>
                                            </span>
                                         </div>
						            </div>
					            </td>
					            
					            <td class="td_right"><font color="red">*</font>顺序： </td>
					            <td class=""  width="100">
					                <input type="text" name="seq"  value="${page.seq}" maxlength="60" class="_req input-text lh30 seq" size="40">
					            </td>
					        </tr>
					        
					        <tr>
					            <td class="td_right"><font color="red">*</font>元素类型： </td>
					            <td>
						            <div class="select_border">
					                	<div class="select_containers">
                                            <span class="fl"> <select class="select elementType_bak" name="elementType_bak" disabled="disabled"
                                                style="width: 130px;">
                                                	<option value="">请选择</option>
                                                    <option value="1"<#if "${page.elementType}"=="1"> selected</#if>>活动</option>
                                                    <option value="2"<#if "${page.elementType}"=="2"> selected</#if>>图片</option>
                                                    <option value="3"<#if "${page.elementType}"=="3"> selected</#if>>文字</option>
                                                    <option value="4"<#if "${page.elementType}"=="4"> selected</#if>>自定义编辑</option>
                                                    <option value="5"<#if "${page.elementType}"=="5"> selected</#if>>SEO</option>
                                                    <option value="6"<#if "${page.elementType}"=="6"> selected</#if>>公告</option>
                                                    <option value="7"<#if "${page.elementType}"=="7"> selected</#if>>资讯</option>
                                            </select>
                                            </span>
                                         </div>
						            </div>
						            <input type="hidden" name="elementType" class="_req input-text lh30 elementType" value="${page.elementType}" >
					            </td>
					            
					        </tr>
					        
					    </tbody>
					</table>


                    <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td class="td_right"></td>
                                <td class="td_right"></td>
                                <td align="center">
                                    <input class="btn btn82 btn_sab" id="inputFormSaveBtn" type="button" value="提交" name="button">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input class="btn btn82 btn_sab" id="inputFormSaveAndCreatBtn" type="button" value="提交并新增" name="button">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input class="btn btn82 btn_nochecked" id="returnPage" type="button" value="返回" name="button">
                                </td>
                                <td class="td_right"></td>
                                <td class="td_right"></td>
                                <td class="td_right"></td>
                            </tr>
                        </tbody>
                    </table>
                    
                    <#include "/cms/templet/queryPage.ftl">
                    <#include "/cms/templet/queryTemplet.ftl">
                    
                </form>
            </div>
        </div>
    </div>
</div>
</@backend>