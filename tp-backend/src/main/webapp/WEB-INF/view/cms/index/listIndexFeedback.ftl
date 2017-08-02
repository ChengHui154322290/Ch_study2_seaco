<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="反馈信息表" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
        
        '/statics/backend/js/dateTime/jquery.datetimepicker.js',
		
		'/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/cms/js/listIndexFeedback.js'
       ] 
    css=['/statics/backend/js/dateTime/jquery.datetimepicker.css',
			 '/statics/backend/css/style.css'] >
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">CMS管理->首页管理->反馈信息管理</b> 
            </div>
            <div class="box_center">
                <form id="contract_list_form" class="jqtransform" action="">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td colspan="1" class="td_right" width="50" align="right">用户id：</td>
                                <td class="" width="50" align="left"><input type="text"
                                    name="userId" value="${userId}" class="input-text lh30 userId" size="20"></td>

								<td colspan="1" class="td_right" width="50" align="right">用户姓名：</td>
                                 <td class="" width="50" align="left"><input type="text"
                                    name="userName" value="${userName}" class="input-text lh30 userName" size="20"></td>
                                    
                         	 </tr>
                         	 
                            <tr>
                                <td colspan="8" align="center">
                                	<input type="reset" name="button" class="btn btn82 btn_res" value="重置">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                    <input type="button" name="button" id="advertiseListQuery" class="btn btn82 btn_search" value="查询">
                                </td>
                            </tr>
                            
                            <!--虚线-->
                            <tr>
                                <td colspan="8">
                                    <hr style="border: 0.1px dashed #247DFF;" />
                                </td>
                            </tr>

                            <tr>
                                <td colspan="8">
                                    <table width="100%" cellspacing="0" cellpadding="0" border="0"
                                        class="list_table CRZ customertable" id="CRZ0">
                                        
                                        <thead>
											<tr>
												<th width="2%"><input type="checkbox" id="cust_check"></th>
												<th>用户姓名</th>
												<th>手机号码</th>
												<th>邮箱号码</th>
                                                <th>反馈时间</th>
                                                <th>反馈信息</th>
                                                <th>操作</th>
											</tr>
										</thead>
										<tbody>
												
													<tr class="temp_tr_singleActivitytemp">
														<td><input class="dev_ck" type="checkbox" name="pop_announce_check" /></td>
														<td class="td_center"><span class="userName"></span></td>
														<td class="td_center"><span class="mobile"></span></td>
														<td class="td_center"><span class="email"></span></td>
														<td class="td_center"><span class="feedbackDate"></span></td>
														<td class="td_center"><span class="feedbackInfo"></span></td>
														<td class="td_center">
															<a href="javascript:;" class="viewAtt" param='${att.id}'>【查看】</a>
															<a href="javascript:;" class="delAtt" param='${att.id}'>【删除】</a>
														</td>
														<td style="display:none;"><span type="hidden" class="pop_Id" /></td>
													</tr>
													
										</tbody>
                                            
                                    </table>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    
                    
                    <div style="margin-top:10px;width:100%">
					<div style="float:right;padding-right:10px;">
						<a href="javascript:void(0);" id="nextPage">下一页</a>
					</div>
					<div style="float:right;padding-right:10px;">
						<a href="javascript:void(0);" id="prePage">上一页</a>
					</div>
					<div style="float:right;padding-right:10px;">
						总：<span  id="topicCount">${topicCount!}</span>
						每页：
						<#if ("50" == "${perCount!}")>
							<select id="perCount">
								<option value="20">20</option>
								<option value="50" selected>50</option>
								<option value="100">100</option>
								<option value="200">200</option>
							</select>
						<#elseif ("100" == "${perCount!}")>
							<select id="perCount">
								<option value="20">20</option>
								<option value="50">50</option>
								<option value="100" selected>100</option>
								<option value="200">200</option>
							</select>
						<#elseif ("200" == "${perCount!}")>
							<select id="perCount">
								<option value="20">20</option>
								<option value="50">50</option>
								<option value="100">100</option>
								<option value="200" selected>200</option>
							</select>
						<#else>
							<select id="perCount">
								<option value="20" selected>20</option>
								<option value="50">50</option>
								<option value="100">100</option>
								<option value="200">200</option>
							</select>
						</#if>
						<span id="currPage">${currPage!}</span>/<span id="totalPage">${totalPage!}</span>页
					</div>
				</div>
                    
                    <!--@pagination value=page /-->
                </form>
            </div>
        </div>
    </div>
</div>

</@backend>
