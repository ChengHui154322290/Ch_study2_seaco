<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="页面管理" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
        
        '/statics/cms/js/jquery/jquery.ui.core.js',
		'/statics/cms/js/jquery/jquery.form.js',
		
		'/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/cms/js/templet/pageEdit.js'
       ] 
    css=['/statics/backend/css/style.css',
    		'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css']>
	<div class="box">
        <div class="box_border">
            <div class="box_top">页面管理</b>
            </div>
            <div class="box_center" style="margin-bottom: 30px;">
                <form id="inputForm" class="jqtransform" action="savePageTemp.htm" method="post" >	

					<!--input type="hidden" id="titleNameBak" 	value="${titleNameBak}" >
					<input type="hidden" id="statusBak" value="${statusBak}" >
					<input type="hidden" id="typeBak" value="${typeBak}" -->
					
					<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
					    <tbody>
					        <tr>
					            <td class="td_right"><font color="red">*</font>页面名称： </td>
					            <td class=""  width="100">
					                <input type="text" maxlength="30" name="pageName" value="${page.pageName}" class="_req input-text lh30 pageName" maxlength="60" size="40">
					            </td>
					            
					            <td class="td_right"><font color="red">*</font>页面编码： </td>
					             <td class=""  width="100">
					                <input type="text" maxlength="30" name="pageCode" value="${page.pageCode}" class="_req input-text lh30 pageCode" maxlength="60" size="40">
					            </td>
					            
					            <input name="id" type="hidden" class="_req input-text lh30 pageId" value="${page.id}" >
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
					        
					    </tbody>
					</table>


                    <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td class="td_right"></td>
                                <td class="td_right"></td>
                                <td align="center">
                                    <input class="btn btn82 btn_sab" id="inputFormSaveBtn" type="button" value="提交" name="button">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input class="btn btn82 btn_nochecked" id="returnPage" type="button" value="返回" name="button">
                                </td>
                                <td class="td_right"></td>
                                <td class="td_right"></td>
                                <td class="td_right"></td>
                            </tr>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
</@backend>