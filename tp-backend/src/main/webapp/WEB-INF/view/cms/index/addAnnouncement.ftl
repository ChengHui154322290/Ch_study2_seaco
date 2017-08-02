<#include "/common/common.ftl"/>
<#include "/cms/common/page.ftl" />
<@backend title="公告资讯管理" 
	js=['/statics/cms/js/common.js',
	'/statics/backend/js/editor/kindeditor-all.js',
        '/statics/cms/js/addAnnouncement.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-common.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/jquery/jquery.json-2.4.min.js',
        '/statics/cms/js/layerly/layer.js',
         '/statics/cms/js/common/editorAnnouncement.js'
        
        ] 
    css=['/statics/backend/js/dateTime/jquery.datetimepicker.css',
			 '/statics/backend/css/style.css'] >
<style type="text/css">
.pb15 {
    padding-bottom: 0px;
}
</style>
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">创建公告资讯</b>
            </div>
            <div class="box_center" style="margin-bottom: 30px;">
                <form class="jqtransform" action="">

					<input type="hidden" id="titleNameBak" 	value="${titleNameBak}" >
					<input type="hidden" id="statusBak" value="${statusBak}" >
					<input type="hidden" id="typeBak" value="${typeBak}" >
					
					<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
					    <tbody>
					        <tr>
					            <td class="td_right"><font color="red">*</font>标题： </td>
					            <td class=""  width="100">
					                <input type="text" maxlength="30" name="name" value="${title}" class="_req input-text lh30 title" maxlength="60" size="40">
					            </td>
					            
					            <td class="td_right"><font color="red">*</font>页面位置： </td>
					            <td>
						            <div class="select_border">
						                
					                	<div class="select_containers">
                                            <span class="fl"> <select class="select type" name="" 
                                                style="width: 130px;">
                                                    <option value="">请选择</option>
                                                    <option value="1" <#if "${type}"=="1"> selected</#if>>西客商城首页-资讯</option>
                                                    <!--option value="2" <#if "${type}"=="2"> selected</#if>>西客商城首页-市场资讯</option>
                                                    <option value="3" <#if "${type}"=="3"> selected</#if>>西客商城最后疯抢-资讯</option>
                                                    <option value="4" <#if "${type}"=="4"> selected</#if>>西客商城最后疯抢-市场资讯</option-->
                                                    <option value="5" <#if "${type}"=="5"> selected</#if>>海淘首页-自定义区</option>
                                                    <option value="6" <#if "${type}"=="6"> selected</#if>>西客商城首页-自定义区</option>
                                            </select>
                                            </span>
                                         </div>
						            </div>
					            </td>
					            
					            <input type="hidden" class="_req input-text lh30 announce_id" value="${id}" >
					            <input type="hidden" class="_req input-text lh30 announce_status" value="${status}" >
					        </tr>
					        <tr>
					            <td class="td_right"><font color="red">*</font>状态： </td>
					            <td>
						            <div class="select_border">
						                
					                	<div class="select_containers">
                                            <span class="fl"> <select class="select status" name="" 
                                                style="width: 130px;">
                                                    <option value="">请选择</option>
                                                    <option value="1"<#if "${type}"=="1"> selected</#if>>草稿</option>
                                                    <option value="0"<#if "${type}"=="0"> selected</#if>>启用</option>
                                            </select>
                                            </span>
                                         </div>
						            </div>
					            </td>
					            
					            <td class="td_right"><font color="red">*</font>链接： </td>
					            <td class=""  width="100">
					                <input type="text" name="link" value="${link}" class="_req input-text lh30 link" maxlength="60" size="40">
					            </td>
					        </tr>
					        <tr>
					            <td class="td_right"><font color="red">*</font>顺序： </td>
					            <td class=""  width="100">
					                <input type="text" name="linkName"  value="${sort}" maxlength="60" class="_req input-text lh30 sort" size="40">
					            </td>
					            <td class="td_right"></td>
					            <td></td>
					        </tr>
					        
					        <!--tr>
					            <td class="td_right"><font color="red">*</font>内容： </td>
					            <td class=""  >
					            	<textarea name="content" class="content" cols="80"  rows="10">${content}</textarea>
					            </td>
					        </tr-->
					        
					        <tr rowspan="5">
								<td class="td_right"><strong style="color:red;">*</strong>内容</td>
								<td colspan="5">
									<div class="box_center">
										<ul id="tabContent" class="tab" >
											<li>
												<input type="button" value="PC模板 " />
											</li>
											<!--li>
												<input type="button" value="手机模板" />
											</li-->
										</ul>
										<table class="input tabContent"">
											<tr>
												<td>
												    <textarea class="editor" id="pcContentEditor" name="announce.content" style="width: 100%;height:200px;">
												    	${content}
												    </textarea>
												</td>
											</tr>
										</table>
										<!-- -->
										<!--table  class="input tabContent">
											<tr>
												<td>
													<textarea class="editor" id="phoneContentEditor" name="announce.contentMobile" style="width: 100%;height:200px;">
												    </textarea>
												</td>
											</tr>
										</table-->
									</div>
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
                                    <input class="btn btn82 btn_sab" type="button" value="提交" name="button">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input class="btn btn82 btn_nochecked" type="button" value="返回" name="button">
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