<#include "/common/common.ftl"/>
<#include "/cms/common/page.ftl" />
<@backend title="反馈信息管理" 
	js=['/statics/cms/js/common.js',
        '/statics/backend/js/editor/kindeditor-all.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-common.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/jquery/jquery.json-2.4.min.js',
        '/statics/cms/js/layerly/layer.js',
         '/statics/cms/js/viewIndexFeedback.js',
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
                <b class="pl15">查看反馈信息</b>
            </div>
            <div class="box_center" style="margin-bottom: 30px;">
                <form class="jqtransform" action="">

					<input type="hidden" id="userIdBak" 	value="${userIdBak}" >
					<input type="hidden" id="userNameBak" value="${userNameBak}" >
					
					<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
					    <tbody>
					        <tr>
					            <td class="td_right"><font color="red">*</font>用户名： </td>
					            <td class=""  width="100">
					                <input type="text" maxlength="30" name="userName" value="${obj.userName}" class="_req input-text lh30 userName" maxlength="60" size="40">
					            </td>
					            
					            <td class="td_right"><font color="red">*</font>手机号码： </td>
					            <td class=""  width="100">
					                <input type="text" maxlength="30" name="mobile" value="${obj.mobile}" class="_req input-text lh30 mobile" maxlength="60" size="40">
					            </td>
					            
					        </tr>
					        <tr>
					            <td class="td_right"><font color="red">*</font>邮箱： </td>
					            <td class=""  width="100">
					                <input type="text" maxlength="30" name="email" value="${obj.email}" class="_req input-text lh30 email" maxlength="60" size="40">
					            </td>
					            
					            <td class="td_right"><font color="red">*</font>反馈日期： </td>
					            <td class=""  width="100">
					                <input type="text" maxlength="30" name="feedbackDate" value="${obj.feedbackDate?string("yyyy-MM-dd HH:mm:ss")}" class="_req input-text lh30 feedbackDate" maxlength="60" size="40">
					            </td>
					        </tr>
					        
					        <tr rowspan="5">
								<td class="td_right"><strong style="color:red;">*</strong>反馈信息</td>
								<td colspan="5">
									<div class="box_center">
										<table class="input tabContent"">
											<tr>
												<td>
												    <textarea class="editor" id="pcContentEditor" name="announce.content" style="width: 100%;height:200px;">
												    	${obj.feedbackInfo}
												    </textarea>
												</td>
											</tr>
										</table>
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