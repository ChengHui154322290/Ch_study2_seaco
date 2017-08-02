<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="达人计划管理" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
        
        '/statics/backend/js/dateTime/jquery.datetimepicker.js',
		
		'/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/cms/js/masterplan/masterPlanRuleList.js'
       ] 
    css=['/statics/backend/js/dateTime/jquery.datetimepicker.css',
			 '/statics/backend/css/style.css'] >
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">CMS管理->首页管理->达人计划管理</b> 
            </div>
            <div class="box_center">
                <form id="contract_list_form" class="jqtransform" action="">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td colspan="1" class="td_right" width="50" align="right">活动编号：</td>
                                <td class="" width="50" align="left"><input type="text"
                                    name="cmaNum" class="input-text lh30 cmaNum" size="20"></td>

								<td colspan="1" class="td_right" width="50" align="right">活动名称：</td>
                                 <td class="" width="50" align="left"><input type="text"
                                    name="cmaActivityName" class="input-text lh30 cmaActivityName" size="20"></td>
                                
                                <td colspan="1" class="td_right" width="50" align="left">状态：</td>
                            	<td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> <select class="select cmaActivityStatus" name="cmaActivityStatus" 
                                                style="width: 150px;">
                                                    <option value="">全部</option>
                                                    <option value="1">编辑中</option>
                                                    <option value="2">审核中</option>
                                                    <option value="3">已取消</option>
                                                    <option value="4">审核通过</option>
                                                    <option value="5">已驳回</option>
                                                    <option value="6">终止</option>
                                            </select>
                                            </span>
                                         </div>
                                     </div>
                               	 </td>
                               	 
                               	 <td colspan="1" class="td_right" width="50" align="left">进度：</td>
                            	<td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> <select class="select type" name="" 
                                                style="width: 150px;">
                                                    <option value="">全部</option>
                                                    <option value="1">未开始</option>
                                                    <option value="2">招募中</option>
                                                    <option value="3">进行中</option>
                                                    <option value="4">已结束</option>
                                                    <option value="5">已终止</option>
                                            </select>
                                            </span>
                                         </div>
                                     </div>
                               	 </td>
                               	 
                         	 </tr>
                         	 
                            <tr>
                                <td colspan="8" align="center">
                                	<input type="reset" name="button" class="btn btn82 btn_res" value="重置">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                    <input type="button" name="button" id="mastListQuery" class="btn btn82 btn_search" value="查询">
                                </td>
                            </tr>

                            <!--虚线-->
                            <tr>
                                <td colspan="8">
                                    <hr style="border: 0.1px dashed #247DFF;" />
                                </td>
                            </tr>

                            <tr>
                                <td>
                                    <input type="button" name="mast_list_add" id="advertise_list_add" class="btn btn82 btn_add" value="创建">
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
												<th>编号</th>
                                                <th>名称</th>
                                                <th>计划招募人数</th>
                                                <th>实际招募人数</th>
                                                <th>活动有效期</th>
                                                <th>招募有效期</th>
                                                <th>状态</th>
                                                <th>进度</th>
                                                <th>操作</th>
											</tr>
										</thead>
										<tbody>
											<tr class="temp_tr_advertise">
												<td class="td_center"><span class="pop_cmaNum"></span></td>
												<td class="td_center"><span class="pop_cmaActivityName"></span></td>
												<td class="td_center"><span class="pop_cmaPlanRecruit"></span></td>
												<td class="td_center"><span class="pop_cmaActualRecruit"></span></td>
												<td class="td_center"><span class="pop_validityTime"></span></td>
												<td class="td_center"><span class="pop_cecruitTime"></span></td>
												<td class="td_center"><span class="pop_cmaActivityStatus"></span></td>
												<td class="td_center"><span class="pop_progress"></span></td>
												<td class="td_center" style="display:none;"><span class="pop_id"></span></td>
                   								<td class="td_center">
                   									<a href="javascript:;" class="editAtt" param='${att.id}' style="display:none;">【编辑】</a>
                   									<a href="javascript:;" class="examineAtt" param='${att.id}' style="display:none;">【审核】</a>
                   									<a href="javascript:;" class="stopAtt" param='${att.id}' style="display:none;">【终止】</a>
                   									<a href="javascript:;" class="rejectAtt" param='${att.id}' style="display:none;">【驳回】</a>
                   									<a href="javascript:;" class="cancelAtt" param='${att.id}' style="display:none;">【取消】</a>
                   									<a href="javascript:;" class="detailsAtt" param='${att.id}'>【详情】</a>
                   								</td>
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
                    
                    <#include "/cms/masterplan/approvalView.ftl">
                    <!--@pagination value=page /-->
                </form>
            </div>
        </div>
    </div>
</div>

</@backend>
